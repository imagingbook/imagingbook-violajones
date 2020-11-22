package imagingbook.violajones.lib;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import imagingbook.lib.util.ResourceUtils;
import imagingbook.violajones.data.Data;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

/**
 * This class represents a trained Haar cascade classifier. Training results were obtained
 * from OpenCV's 'opencv_haartraining' application (now obsolete, see 
 * http://docs.opencv.org/doc/user_guide/ug_traincascade.html), available as XML files.
 * Currently only the old-type XML format is supported.
 * 
 * 2018/12/10: Refactored to use javax.xml and org.w3c.dom to eliminate org.jdom2 dependency.
 * TODO: Write a reader for the newer format (produced by 'opencv_traincascade').
 * 
 * @author WB
 */
public class HaarCascadeDescriptor {
	
	static final String XML_TYPE_ID1 = "opencv-haar-classifier";		// OpenCV "old style"
	static final String XML_TYPE_ID2 = "opencv-cascade-classifier";		// OpenCV new style?
	
	private int width = 0;
	private int height = 0;
	private List<Stage> stages = null;
	
	// --- constructors ------------------------------
	
	// we dont't want to use this from outside:
	private HaarCascadeDescriptor() {	
	}
	
	// --- static factory methods -------------------
	
	/**
	 * Creates a Haar cascade from the specification given in a
	 * XML file.
	 * 
	 * @param xmlFilename path to the XML file
	 * @return a new Haar cascade object
	 */
	public static HaarCascadeDescriptor fromFileName(String xmlFilename) {
		File file = new File(xmlFilename);
		String name = file.getName();
		InputStream strm = HaarTrainingSet.class.getResourceAsStream(name);
		if (strm == null) {
			throw new RuntimeException(HaarCascadeDescriptor.class.getSimpleName() + ": could not find resource " + name);
		}
		return HaarCascadeDescriptor.fromInputStream(strm);
	}
	
	/**
	 * Creates a Haar cascade from the specification given in a
	 * XML stream.
	 * 
	 * @param xmlStrm input stream providing XML content
	 * @return a new Haar cascade object
	 */
	public static HaarCascadeDescriptor fromInputStream(InputStream xmlStrm) {
		HaarCascadeDescriptor hc = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xmlDoc = builder.parse(xmlStrm);
			hc = new HaarCascadeDescriptor();
			hc.buildFromXmlDoc(xmlDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hc;
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
		System.out.println("Root node name = " + root.getNodeName());
		
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
	Element getFirstChildElement(Node parent) {
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
	List<Element> getChildElements(Node parent) {
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
	List<Element> getChildElements(Node parent, String name) {
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
	Element getChildElement(Node parent, String name) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child instanceof Element && name.equals(child.getNodeName()))
				return (Element) child;
		}
		return null;
	}
	
	// -------------------------------------------------------------------------------------------------
	
	public void print() {
		System.out.println("Listing of " + this.getClass().getSimpleName());
		System.out.format("Size = %d x %d\n", width, height);
		System.out.format("Number of stages = %d\n", stages.size());
		int scnt = 0;
		for (Stage stage : stages) {
			scnt++;
			System.out.format("******* Stage %d *********\n", scnt);
			stage.print();
		}
	}
	
	// -------------------------------------------------------------------------------------------------
	
	public static void main(String[] args) {
		String xmlRoot = ResourceUtils.getResourcePath(Data.class, "xml").toString();
		System.out.println("XML root: " + xmlRoot);
		
//		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");
//		String xmlPath = od.getPath();
		
		String xmlPath = xmlRoot + "\\haarcascade_frontalface_alt2.xml";

		if (!xmlPath.endsWith(".xml")) {
			System.out.println("this is not an XML file!");
			return;
		}

		System.out.println("XML path: " + xmlPath);

//		InputStream strm = ResourceUtils.getResourceStream(Data.class, "xml/haarcascade_frontalface_alt2.xml");
//		InputStream strm = Data.class.getResourceAsStream("xml/haarcascade_frontalface_alt2.xml");
		
		HaarCascadeDescriptor hc = HaarCascadeDescriptor.fromFileName(xmlPath);
		hc.print();

		System.out.println("done.");
	}
	

}
