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
 * @author WB
 *
 */
public class HaarCascadeDescriptor {
	
	static final String XML_TYPE_ID = "opencv-haar-classifier";
	
	private int width = 0;
	private int height = 0;
	private List<Stage> stages = null;
	
	// --- constructors ------------------------------
	
	// we dont't want to use this from outside:
	private HaarCascadeDescriptor() {	
	}
	
	// --- static factory methods -------------------
	
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
	 * XML input from an input stream. To be used when reading from
	 * Java resources.
	 * @param xmlStrm
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
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
		
		Element root = (Element) xmlDoc.getRootElement().getChildren().get(0);
		
		// added by WB, TODO: different readers may be needed for other OpenCV training files
		String type_id = root.getAttributeValue("type_id");
		//IJ.log("root attribute =" + type_id);
		if (!XML_TYPE_ID.equals(type_id)) {
			throw new Exception("XML file of type " + XML_TYPE_ID + " expected.");
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
			double thres = Float.parseFloat(stageElem.getChild("stage_threshold").getText());
			Stage stage = new Stage(thres);
			
			// read all trees of this stage:
			for (Element treeElem : stageElem.getChild("trees").getChildren("_")) {
				FeatureTree tree = new FeatureTree();	
				
				// read all features contained in this tree:
				for (Element node : treeElem.getChildren("_")) {
					double thres2 = Double.parseDouble(node.getChild("threshold").getText());

					int leNode = -1;
					double leVal = Double.NaN;
					Element el = node.getChild("left_val");
					if (el != null) {
						leVal = Double.parseDouble(el.getText());
					} else {
						leNode = Integer.parseInt(node.getChild("left_node").getText());
					}

					int riNode = -1;
					double riVal = Double.NaN;
					Element er = node.getChild("right_val");
					if (er != null) {
						riVal = Double.parseDouble(er.getText());
					} else {
						riVal = Double.NaN;
						riNode = Integer.parseInt(node.getChild("right_node").getText());
					}
					Feature feature = new Feature(width, height, thres2, leVal, leNode, riVal, riNode);
					
					// read rectangles of this feature and add:
					for (Element rectElem : node.getChild("feature").getChild("rects").getChildren("_")) {
						String s = rectElem.getText().trim();
						FeaturePatch r = FeaturePatch.fromString(s);
						feature.add(r);
					}
					tree.addFeature(feature);
				}
				stage.addTree(tree);
			}
			stages.add(stage);
		}
	}
	
	
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
