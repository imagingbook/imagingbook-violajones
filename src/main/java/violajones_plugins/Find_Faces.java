package Violajones_Plugins;

import java.awt.Color;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.lib.ij.IjUtils;
import imagingbook.lib.util.Enums;
import imagingbook.violajones.lib.FaceDetector;
import imagingbook.violajones.lib.FaceDetector.Parameters;
import imagingbook.violajones.lib.FaceRegion;
import imagingbook.violajones.resources.xml.HaarTrainingSet;


/**
 * Face detection plugin using the new implementation.
 * @author W. Burger
 * @version 2016/05/04
 */
public class Find_Faces implements PlugInFilter {
	
	static boolean extractFaceImages = false;
	static HaarTrainingSet trainingSet = HaarTrainingSet.FACE2;
	
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
		params.minMergeRegionOverlap = 0.2;
		
		params.doGradientPruning = true;
		params.gradientSigma = 1.0;
		params.minGradientMagnitude = 5;
		params.maxGradientMagnitude = 100;
		
		
		if (!setParameters(params)) return;
			
		FaceDetector detector = FaceDetector.create(trainingSet.getStream(), params);
		IjUtils.setRgbConversionWeights(ip);
		List<FaceRegion> res = detector.findFaces(ip.convertToByteProcessor());
		IJ.log(res.size() + " faces found!");
		
		// show results
		ImageProcessor cp = ip.convertToColorProcessor();
		for (FaceRegion r : res) {
			draw(r, cp);
		}
		new ImagePlus(this.getClass().getSimpleName() + ": results", cp).show();
		
		if (extractFaceImages && !res.isEmpty()) {
//			int w = res.get(0).width;
//			int h =  res.get(0).height;
			//ImageStack faceStack = new ImageStack(w, h);
			int faceCtr = 0;
			for (FaceRegion r : res) {
				ip.setRoi(r.x, r.y, r.width, r.height);
				new ImagePlus("Face " + faceCtr, ip.crop()).show();
				//faceStack.addSlice(ip.crop());
				faceCtr++;
			}
			//new ImagePlus("FaceStack", faceStack).show();
		}

	}
	
	boolean setParameters(Parameters params) {
		GenericDialog gd = new GenericDialog("Set Face Detector Parameters");
		gd.addChoice("Haar training set", Enums.getEnumNames(HaarTrainingSet.class), trainingSet.name());
		gd.addNumericField("baseScale", params.baseScale, 2);
		gd.addNumericField("scaleStep", params.scaleStep, 2);
		gd.addNumericField("winShiftFraction", params.winShiftFraction, 2);
		gd.addNumericField("minNeighbors", params.minNeighbors, 0);
		gd.addNumericField("minMergeRegionOverlap", params.minMergeRegionOverlap, 2);
		
		gd.addCheckbox("doGradientPruning", params.doGradientPruning);
		
		gd.addNumericField("gradientSigma", params.gradientSigma, 2);
		gd.addNumericField("minGradientMagnitude", params.minGradientMagnitude, 0);
		gd.addNumericField("maxGradientMagnitude", params.maxGradientMagnitude, 2);
		
		gd.addCheckbox("extractFaceImages", extractFaceImages);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		
		trainingSet = HaarTrainingSet.valueOf(gd.getNextChoice());
		params.baseScale = gd.getNextNumber();
		params.scaleStep = gd.getNextNumber();
		params.winShiftFraction = gd.getNextNumber();
		params.minNeighbors = (int) gd.getNextNumber();
		params.minMergeRegionOverlap = gd.getNextNumber();
		
		params.doGradientPruning = gd.getNextBoolean();
		
		params.gradientSigma = gd.getNextNumber();
		params.minGradientMagnitude = gd.getNextNumber();
		params.maxGradientMagnitude = gd.getNextNumber();
		
		extractFaceImages = gd.getNextBoolean();
		return true;
	}

	private void draw(FaceRegion f, ImageProcessor cp) {
		cp.setColor(Color.green);
		cp.drawRect(f.x, f.y, f.width, f.height);
	}

}
