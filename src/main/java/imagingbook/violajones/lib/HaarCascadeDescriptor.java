package imagingbook.violajones.lib;


import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * This class represents a trained Haar cascade classifier. Training results were obtained
 * from OpenCV's 'opencv_haartraining' application (now obsolete, see 
 * http://docs.opencv.org/doc/user_guide/ug_traincascade.html), available as XML files.
 * Currently only the old-type XML format is supported.
 * TODO: Write a reader for the newer format (produced by 'opencv_traincascade').
 * 
 * @author WB
 *
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
	public static HaarCascadeDescriptor createFrom(String xmlFilename) {
		HaarCascadeDescriptor hc = null;
		try {
			Document xmlDoc = new SAXBuilder().build(new File(xmlFilename));
			hc = new HaarCascadeDescriptor();
			hc.buildFrom(xmlDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hc;
	}
	
	/**
	 * Creates a Haar cascade from the specification given in a
	 * XML stream.
	 * 
	 * @param xmlStrm input stream providing XML content
	 * @return a new Haar cascade object
	 */
	public static HaarCascadeDescriptor createFrom(InputStream xmlStrm) {
		HaarCascadeDescriptor hc = null;
		try {
			Document xmlDoc = new SAXBuilder().build(xmlStrm);
			hc = new HaarCascadeDescriptor();
			hc.buildFrom(xmlDoc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hc;
	}

	// --- getters and setters ------------------------
	
	/**
	 * Returns the width of this Haar cascade.
	 * @return the width of this Haar cascade (in pixels units)
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of this Haar cascade.
	 * @return the height of this Haar cascade (in pixels units)
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the number of stages in this Haar cascade.
	 * @return the number of stages this Haar cascade
	 */
	public List<Stage> getStages() {
		return stages;
	}

	// ------------------------------------------------
	
	// See jdom2 API documentation: http://www.jdom.org/docs/apidocs/
	
	private void buildFrom(Document xmlDoc) throws Exception {
		/*
		 * The detector is constituted by stages, each of them telling whether
		 * the considered zone represents the object with probability a bit
		 * greater than 0.5. If a zone passes all stages, it is considered as
		 * representing the object.
		 */
		
		Element root = xmlDoc.getRootElement().getChildren().get(0);
		
		// added by WB, TODO: different readers may be needed for other OpenCV training files
		String type_id = root.getAttributeValue("type_id");
		//IJ.log("root attribute =" + type_id);

		if (!type_id.equals(XML_TYPE_ID1)) {
			throw new Exception("XML file of type " + XML_TYPE_ID1 + " expected.");
		}
		
		/* Read the size (in pixels) of the detector. */
		Scanner scanner = new Scanner(root.getChild("size").getText());
		width = scanner.nextInt();
		height = scanner.nextInt();
		scanner.close();
		
		stages = new LinkedList<Stage>();

		/* Iterate over the stages nodes to read the stages. */
		for (Element stageElem : root.getChild("stages").getChildren("_")) {
			// read the stage threshold:
			double stageThreshold = Float.parseFloat(stageElem.getChild("stage_threshold").getText());
			Stage stage = new Stage(stageThreshold);
			
			// read all trees of this stage:
			for (Element treeElem : stageElem.getChild("trees").getChildren("_")) {
				
				// collect the features contained in this tree:
				List<FeatureNode> nodes = new LinkedList<>();
				for (Element featureElem : treeElem.getChildren("_")) {
					double featureThreshold = Double.parseDouble(featureElem.getChild("threshold").getText());
					
					// get the LEFT value OR child (one of them is empty)
					int childL = FeatureNode.NO_CHILD;
					double valL = FeatureNode.NO_VALUE;
					Element el = featureElem.getChild("left_val");
					if (el != null) {
						valL = Double.parseDouble(el.getText());
					} else {
						childL = Integer.parseInt(featureElem.getChild("left_node").getText());
					}

					// get the RIGHT value OR child (one of them is empty)
					int childR = FeatureNode.NO_CHILD;
					double valR = FeatureNode.NO_VALUE;
					Element er = featureElem.getChild("right_val");
					if (er != null) {
						valR = Double.parseDouble(er.getText());
					} else {
						childR = Integer.parseInt(featureElem.getChild("right_node").getText());
					}
					
					List<FeaturePatch> patches = new LinkedList<FeaturePatch>();
					
					// read rectangles of this feature and add:
					for (Element rectElem : featureElem.getChild("feature").getChild("rects").getChildren("_")) {
						String s = rectElem.getText().trim();
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
	
	@Deprecated
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
	

}
