/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package imagingbook.violajones.lib;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import imagingbook.violajones.data.HaarTrainingSet;

/**
 * This class represents a trained Haar cascade classifier. Training results were obtained
 * from OpenCV's 'opencv_haartraining' application (now obsolete, see 
 * http://docs.opencv.org/doc/user_guide/ug_traincascade.html), available as XML files.
 * Currently only the old-type XML format is supported.
 * 
 * 2018/12/10: Refactored to use javax.xml and org.w3c.dom to eliminate org.jdom2 dependency.
 * 2020/11/23: Adapted to new resource access scheme.
 * 2022/04/15: Resource access scheme again revised.
 * 
 * TODO: Write a reader for the newer format (produced by 'opencv_traincascade').
 * 
 * @author WB
 */
public class HaarCascadeDescriptor {
	
	public static final String XML_TYPE_ID1 = "opencv-haar-classifier";		// OpenCV "old style"
	public static final String XML_TYPE_ID2 = "opencv-cascade-classifier";		// OpenCV new style?
	
	private int width = 0;
	private int height = 0;
	private List<Stage> stages = null;
	
	// --- constructors ------------------------------
	
	private HaarCascadeDescriptor() {	
	}
	
	// --- static factory methods -------------------
	
	/**
	 * Creates a Haar cascade from the specification given in a
	 * XML stream.
	 * 
	 * @param strm input stream providing XML content
	 * @return a new Haar cascade object
	 */
	public static HaarCascadeDescriptor fromInputStream(InputStream strm) {
		//HaarCascadeDescriptor hcd = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document xmlDoc = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			xmlDoc = builder.parse(strm);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			throw new RuntimeException(HaarCascadeDescriptor.class.getSimpleName() +
					": could not create descriptor from XML stream");
		}
		return fromXmlDocument(xmlDoc);
	}
	
	public static HaarCascadeDescriptor fromXmlDocument(Document xmlDoc) {
		HaarCascadeDescriptor hcd = new HaarCascadeDescriptor();
		hcd.buildFromXmlDoc(xmlDoc);
		return hcd;
	}

	// --- getters and setters ------------------------
	
	/**
	 * Returns the width of this Haar cascade.
	 * @return the width of this Haar cascade (in pixels units).
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of this Haar cascade.
	 * @return the height of this Haar cascade (in pixels units).
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the number of stages in this Haar cascade.
	 * @return the number of stages this Haar cascade.
	 */
	public List<Stage> getStages() {
		return stages;
	}

	// ------------------------------------------------
	
	/**
	 * The detector consists of stages, each of them telling whether
	 * the considered zone represents the object with probability a bit
	 * greater than 0.5. If a zone passes all stages, it is considered as
	 * representing the object.
	 * 
	 * @param xmlDoc the XML document object
	 * @throws Exception
	 */
	private void buildFromXmlDoc(Document xmlDoc) {
		Element root = getFirstChildElement(xmlDoc.getDocumentElement());
		//System.out.println("Root node name = " + root.getNodeName());
		
		//TODO: different readers may be needed for other OpenCV training files
		String type_id = root.getAttribute("type_id");
		if (!type_id.equals(XML_TYPE_ID1)) {
			throw new RuntimeException("XML file of type " + XML_TYPE_ID1 + " expected.");
		}
		
		/* Read the size (in pixels) of the detector. */
		Scanner scanner = new Scanner(getChildElement(root, "size").getTextContent()); //new2
		width = scanner.nextInt();
		height = scanner.nextInt();
		scanner.close();
		
		stages = new LinkedList<Stage>();

		
		/* Iterate over the stages nodes to read the stages. */
		Element stagesE = getChildElement(root, "stages");
		for (Element stageElem : getChildElements(stagesE, "_")) {
			// read the stage threshold:
			double stageThreshold = Float.parseFloat(getChildElement(stageElem, "stage_threshold").getTextContent());
			Stage stage = new Stage(stageThreshold);
			
			// read all trees of this stage:
			Element treesE = getChildElement(stageElem, "trees");
			for (Element treeElem : getChildElements(treesE, "_")) {
				
				// collect the features contained in this tree:
				List<FeatureNode> nodes = new LinkedList<>();
				for (Element featureElem : getChildElements(treeElem, "_")) {
					double featureThreshold = Double.parseDouble(getChildElement(featureElem, "threshold").getTextContent());
					
					// get the LEFT value OR child (one of them is empty)
					int childL = FeatureNode.NO_CHILD;
					double valL = FeatureNode.NO_VALUE;
					Element el = getChildElement(featureElem, "left_val");
					if (el != null) {
						valL = Double.parseDouble(el.getTextContent());
					} else {
						childL = Integer.parseInt(getChildElement(featureElem, "left_node").getTextContent());
					}

					// get the RIGHT value OR child (one of them is empty)
					int childR = FeatureNode.NO_CHILD;
					double valR = FeatureNode.NO_VALUE;
					Element er = getChildElement(featureElem, "right_val");
					if (er != null) {
						valR = Double.parseDouble(er.getTextContent());
					} else {
						childR = Integer.parseInt(getChildElement(featureElem, "right_node").getTextContent());
					}
					
					List<FeaturePatch> patches = new LinkedList<FeaturePatch>();
					
					// read rectangles of this feature and add:
					Element rectsE = getChildElement(getChildElement(featureElem, "feature"), "rects");
					for (Element rectElem : getChildElements(rectsE, "_")) {
						String s = rectElem.getTextContent().trim();
						FeaturePatch r = FeaturePatch.fromString(s);
						patches.add(r);
					}
					
					FeatureNode feature = new FeatureNode(width, height, featureThreshold, valL, childL, valR, childR, 
														patches.toArray(new FeaturePatch[0]));
					nodes.add(feature);
				}
				
				FeatureTree tree = new FeatureTree(nodes.toArray(new FeatureNode[0]));
				stage.addTree(tree);
			}
			stages.add(stage);
		}
	}
	
	// -------------------------------------------------------------------------
	
	/**
	 * Returns the first child node that is of type {@link Element}.
	 * @param parent the parent node
	 * @return the first child node of type {@link Element}.
	 */
	private Element getFirstChildElement(Node parent) {
		Node child = parent.getFirstChild();
		while (!(child instanceof Element)) {
			child = child.getNextSibling();
		}
		return (Element) child;
	}
	
	/**
	 * Collects all child elements into a list.
	 * @param parent the parent node
	 * @return a list of child elements, which may be empty
	 */
	@SuppressWarnings("unused")
	private List<Element> getChildElements(Node parent) {
		List<Element> elements = new LinkedList<>();
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				elements.add((Element) child);
			}
		}
		return elements;
	}
	
	/**
	 * Collects all child elements with the given name into a list.
	 * @param parent the parent node
	 * @param name the name of the child element
	 * @return a list of child elements, which may be empty
	 */
	private List<Element> getChildElements(Node parent, String name) {
		List<Element> elements = new LinkedList<>();
		NodeList children = parent.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element && name.equals(child.getNodeName())) {
				elements.add((Element) child);
			}
		}
		return elements;
	}

	/**
	 * Searches for the first immediate child element with the given name.
	 * @param parent the parent node
	 * @param name the name of the child element
	 * @return the first matching child element or {@code null} if none exists.
	 */
	private Element getChildElement(Node parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName()))
				return (Element) child;
		}
		return null;
	}
	
	// -------------------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		PrintStream strm = new PrintStream(bas);
		printToStream(strm);
		return bas.toString();
	}
	
	public void printToStream(PrintStream strm) {
		strm.println("Listing of " + this.getClass().getSimpleName());
		strm.format("Size = %d x %d\n", width, height);
		strm.format("Number of stages = %d\n", stages.size());
		int scnt = 0;
		for (Stage stage : stages) {
			scnt++;
			strm.format("******* Stage %d *********\n", scnt);
			stage.print();
		}
	}
	
	// -------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		for (HaarTrainingSet hts : HaarTrainingSet.values()) {
			System.out.println("*****  Resource = " + hts.toString() + " *********");
			HaarCascadeDescriptor hcd = HaarCascadeDescriptor.fromInputStream(hts.getStream());
			hcd.printToStream(System.out);
			System.out.println();
		}
		
		System.out.println("done.");
	}
	

}
