package imagingbook;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Line;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.violajones.lib.FaceRegion;
import imagingbook.violajones.lib.Feature;
import imagingbook.violajones.lib.FeaturePatch;
import imagingbook.violajones.lib.FeatureTree;
import imagingbook.violajones.lib.HaarCascadeDescriptor;
import imagingbook.violajones.lib.Stage;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

import java.awt.Color;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Visualize_Haar_Cascade implements PlugInFilter {

	public int setup(String arg0, ImagePlus im) {
		return DOES_ALL + NO_CHANGES;
	}

	public void run(ImageProcessor ip) {
	
		HaarTrainingSet desc = HaarTrainingSet.FACE2;
		InputStream strm = desc.getStream();
		if (strm == null) {
			IJ.log("could not open stream");
			return;
		}
		
		HaarCascadeDescriptor cascade = HaarCascadeDescriptor.createFrom(strm);
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
				for (@SuppressWarnings("unused") Feature feature : tree.getFeatures()) {
					ImageProcessor sp = sp1.duplicate();
//					sp.setColor(Color.gray);
//					sp.fill();
//					draw(sp, feature);
					stack.addSlice("Stage=" + stageCtr + " Tree=" + treeCtr + " Feat=" + featureCtr, sp);
					featureCtr++;
				}
				treeCtr++;
			}
			stageCtr++;
		}

		
		
		Overlay oly = new Overlay();
		int sliceNo = 1;
		for (Stage stage : stages) {
			for (FeatureTree tree : stage.getTrees()) {
				for (Feature feature : tree.getFeatures()) {
					for (FeaturePatch r : feature.getRectangles()) {
						Color col = (r.weight > 0) ? Color.green : Color.red;
						Roi box = new Roi(r.x, r.y, r.width, r.height);
						box.setStrokeColor(col);
						box.setPosition(sliceNo);
						Roi diag = new Line(r.x, r.y, r.x + r.width, r.y + r.height);
						diag.setStrokeColor(col);
						diag.setPosition(sliceNo);
						oly.add(box);
						oly.add(diag);
					}
					sliceNo++;
				}
			}
		}
		
		ImagePlus stackim = new ImagePlus("Cascade", stack);
		stackim.setOverlay(oly);
		stackim.show();
		
	}
	
	List<Roi> getRectangles(Feature feature) {
		List<Roi> rectRois = new ArrayList<Roi>();
		for (FeaturePatch r : feature.getRectangles()) {
			rectRois.add(new Roi(r.x, r.y, r.width, r.height));
		}
		return rectRois;
	}

	@SuppressWarnings("unused")
	private void draw(ImageProcessor sp, Feature feature) {
		for (FeaturePatch r : feature.getRectangles()) {
			Color col = (r.weight > 0) ? Color.green : Color.red;
			sp.setColor(col);
			for (int u = 0; u < r.width; u++) {
				for (int v = 0; v < r.height; v++) {
					sp.drawPixel(u + r.x, v + r.y);
				}
			}
			//sp.drawRect(r.x, r.y, r.width, r.height);
			
		}
		
	}

	@SuppressWarnings("unused")
	private void draw(FaceRegion f, ImageProcessor cp) {
		cp.setColor(Color.green);
		cp.drawRect(f.x, f.y, f.width, f.height);
	}

}
