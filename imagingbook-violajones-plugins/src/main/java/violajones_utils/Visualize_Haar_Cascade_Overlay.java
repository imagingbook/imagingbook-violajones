package violajones_utils;

import java.awt.Color;
import java.awt.geom.Path2D;
import java.io.InputStream;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.ShapeRoi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.common.ij.GuiTools;
import imagingbook.violajones.DATA.HaarTrainingSet;
import imagingbook.violajones.lib.FeatureNode;
import imagingbook.violajones.lib.FeaturePatch;
import imagingbook.violajones.lib.FeatureTree;
import imagingbook.violajones.lib.HaarCascadeDescriptor;
import imagingbook.violajones.lib.Stage;

/**
 * ImageJ plugin. Visualizes the evaluation sequence for the selected Haar cascade.
 * The current image is scaled to the size of the cascade (typ. 32x32)
 * and shown in the background. 
 * The result is an image stack, with one slice for classifier
 * each decision step.
 * 
 * Feature support regions are shown as vector overlays.
 * 
 * @author WB
 *
 */
public class Visualize_Haar_Cascade_Overlay implements PlugInFilter {
	
	static HaarTrainingSet desc = HaarTrainingSet.FrontalFaceAlt2;

	public int setup(String arg0, ImagePlus im) {
		return DOES_ALL + NO_CHANGES;
	}

	public void run(ImageProcessor ip) {
	
		InputStream strm = desc.getStream();
		if (strm == null) {
			IJ.log("could not open training set");
			return;
		}
		
		HaarCascadeDescriptor cascade = HaarCascadeDescriptor.fromInputStream(strm);
		int w = cascade.getWidth();
		int h = cascade.getHeight();
		
		List<Stage> stages = cascade.getStages();
		
		// create and populate the stack
		ImageStack stack = new ImageStack(w, h);
		ImageProcessor sp1 = ip.resize(w, h);
		
		int stageCtr = 0;
		for (Stage stage : stages) {
			int treeCtr = 0;
			for (FeatureTree tree : stage.getTrees()) {
				int featureCtr = 0;
				for (@SuppressWarnings("unused") 
						FeatureNode feature : tree.getFeatures()) {
					ImageProcessor sp = sp1; // duplicate needed?
					stack.addSlice("Stage=" + stageCtr + " Tree=" + treeCtr + " Feat=" + featureCtr, sp);
					featureCtr++;
				}
				treeCtr++;
			}
			stageCtr++;
		}

		// add overlay graphics
		Overlay oly = new Overlay();
		int sliceNo = 1;
		for (Stage stage : stages) {
			for (FeatureTree tree : stage.getTrees()) {
				for (FeatureNode feature : tree.getFeatures()) {
					for (FeaturePatch r : feature.getPatches()) {
						Color col = (r.weight > 0) ? Color.green : Color.red;
						Roi box = makeBoxRoi(r.x, r.y, r.width, r.height, col);
						box.setPosition(sliceNo);
						oly.add(box);
					}
					sliceNo++;
				}
			}
		}
		
		ImagePlus stackim = new ImagePlus("Cascade", stack);
		stackim.setOverlay(oly);
		stackim.show();
		GuiTools.zoomExact(stackim, 10);
		
	}
	
	private Roi makeBoxRoi(int u, int v, int width, int height, Color col) {
		Path2D path = new Path2D.Double();
		path.moveTo(u, v);
		path.lineTo(u + width, v);
		path.lineTo(u + width, v + height);
		path.lineTo(u, v + height);
		path.lineTo(u, v);
		path.lineTo(u + width, v + height);
		ShapeRoi box = new ShapeRoi(path);
		box.setStrokeColor(col);
		//box.setStrokeWidth(0.2);
		return box;
	}
	
//	private List<Roi> getRectangles(FeatureNode feature) {
//		List<Roi> rectRois = new ArrayList<Roi>();
//		for (FeaturePatch r : feature.getPatches()) {
//			rectRois.add(new Roi(r.x, r.y, r.width, r.height));
//		}
//		return rectRois;
//	}

//	@SuppressWarnings("unused")
//	private void draw(ImageProcessor sp, FeatureNode feature) {
//		for (FeaturePatch r : feature.getPatches()) {
//			Color col = (r.weight > 0) ? Color.green : Color.red;
//			sp.setColor(col);
//			for (int u = 0; u < r.width; u++) {
//				for (int v = 0; v < r.height; v++) {
//					sp.drawPixel(u + r.x, v + r.y);
//				}
//			}
//			//sp.drawRect(r.x, r.y, r.width, r.height);
//			
//		}
//		
//	}

//	@SuppressWarnings("unused")
//	private void draw(FaceRegion f, ImageProcessor cp) {
//		cp.setColor(Color.green);
//		cp.drawRect(f.x, f.y, f.width, f.height);
//	}

}
