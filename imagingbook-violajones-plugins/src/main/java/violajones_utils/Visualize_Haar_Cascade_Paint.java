/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package violajones_utils;

import java.awt.Color;
import java.io.InputStream;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
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
 * Feature support regions are shown as drawn into the image (to 
 * allow saving as a video).
 * 
 * @author WB
 *
 */
public class Visualize_Haar_Cascade_Paint implements PlugInFilter {
	
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
				for (FeatureNode feature : tree.getFeatures()) {
					ImageProcessor sp = sp1.duplicate();
					for (FeaturePatch r : feature.getPatches()) {
						Color col = (r.weight > 0) ? Color.green : Color.red;
						sp.setColor(col);
						sp.fillRect(r.x, r.y, r.width, r.height);
					}
					stack.addSlice("Stage=" + stageCtr + " Tree=" + treeCtr + " Feat=" + featureCtr, sp);
					featureCtr++;
				}
				treeCtr++;
			}
			stageCtr++;
		}
		
		ImagePlus stackim = new ImagePlus("Cascade", stack);
		stackim.show();
		GuiTools.zoomExact(stackim, 10);
		
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
//		}
//	}

//	@SuppressWarnings("unused")
//	private void draw(FaceRegion f, ImageProcessor cp) {
//		cp.setColor(Color.green);
//		cp.drawRect(f.x, f.y, f.width, f.height);
//	}

}
