package jviolajones_orig;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.violajones.lib.FaceDetector.Parameters;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

/**
 * ImageJ plugin using the original implementation.
 * @author W. Burger
 *
 */
public class Plugin_Find_Faces implements PlugInFilter {
	
	static HaarTrainingSet trainingSet = HaarTrainingSet.frontalface_alt2;
	static boolean extractFaceImages = false;
	
	ImagePlus im = null;

	public int setup(String arg0, ImagePlus im) {
		this.im = im;
		return DOES_ALL + NO_CHANGES;
	}
	
	public void run(ImageProcessor ip) {
		
		Parameters params = new Parameters();
		params.baseScale = 1.5;
		params.scaleStep = 1.05;
		params.winShiftFraction = 0.1;
		params.minNeighbors = 1;
		params.doGradientPruning = false;
				
		String XMLFile = trainingSet.getPath();
		IJ.log("XML = " + XMLFile);
	
		Detector detector = Detector.create(XMLFile);
		BufferedImage bi = ip.getBufferedImage();
		
		// original values (in Test.java)
//		float baseScale = 1.5f;		// 1.0f
//		float scale_inc = 1.05f;	// 1.25f
//		float increment = 0.1f;		// 0.1f
//		int min_neighbors = 1;		// 1
//		boolean doCannyPruning = true;
		
		
		List<Rectangle> res = detector.getFaces(bi, 
				(float) params.baseScale,
				(float) params.scaleStep, 
				(float) params.winShiftFraction, 
				params.minNeighbors, 
				params.doGradientPruning);

		IJ.log(res.size() + " faces found!");
		
		// show results
		ImageProcessor cp = ip.convertToColorProcessor();
		for (Rectangle r : res) {
			draw(r, cp);
		}
		new ImagePlus(this.getClass().getSimpleName() + ": results", cp).show();
//		
//		if (extractFaceImages && !res.isEmpty()) {
//			int faceCtr = 0;
//			for (FaceRegion r : res) {
//				ip.setRoi(r.x, r.y, r.width, r.height);
//				new ImagePlus("Face " + faceCtr, ip.crop()).show();
//				//faceStack.addSlice(ip.crop());
//				faceCtr++;
//			}
//		}

	}

	private void draw(Rectangle f, ImageProcessor cp) {
		cp.setColor(Color.green);
		cp.drawRect(f.x, f.y, f.width, f.height);
	}

}
