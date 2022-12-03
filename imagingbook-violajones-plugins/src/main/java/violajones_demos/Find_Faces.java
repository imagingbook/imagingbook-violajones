/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package violajones_demos;

import java.awt.Color;
import java.util.List;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.io.LogStream;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.common.ij.IjUtils;
import imagingbook.common.math.PrintPrecision;
import imagingbook.violajones.DATA.HaarTrainingSet;
import imagingbook.violajones.lib.FaceDetector;
import imagingbook.violajones.lib.FaceDetector.Parameters;
import imagingbook.violajones.lib.FaceRegion;
import imagingbook.violajones.lib.HaarCascadeDescriptor;


/**
 * ImageJ plugin. Face detection ImageJ plugin using 'imagingbook.violajones' implementation
 * of the Viola-Jones face detection algorithm: Paul Viola and Michael Jones, 
 * "Robust Real-time Face Detection", International Journal of Computer Vision 
 * 57.2 (2004), pp. 137–154.
 * 
 * @author W. Burger
 * @version 2018/12/09
 */
public class Find_Faces implements PlugInFilter {
	
	static {
		LogStream.redirectSystem();
		PrintPrecision.set(6);
	}
	
	static boolean extractFaceImages = false;
	static HaarTrainingSet trainingSet = HaarTrainingSet.FrontalFaceAlt2;
	
	ImagePlus im = null;

	@Override
	public int setup(String arg0, ImagePlus im) {
		this.im = im;
		return DOES_ALL + NO_CHANGES;
	}
	
	@Override
	public void run(ImageProcessor ip) {
		
		Parameters params = new Parameters();
		
//		params.baseScale = 1.5;
//		params.scaleStep = 1.05;
//		params.winShiftFraction = 0.1;
//		params.minNeighbors = 1;
//		params.minMergeRegionOverlap = 0.2;
//		
//		params.doGradientPruning = false;
//		params.gradientSigma = 1.0;
//		params.minGradientMagnitude = 5;
//		params.maxGradientMagnitude = 100;
		
		if (!runDialog(params)) return;
		
		IjUtils.setRgbConversionWeights(ip);
//		FaceDetector detector = FaceDetector.create(trainingSet.getStream(), params);
		HaarCascadeDescriptor descriptor = HaarCascadeDescriptor.fromInputStream(trainingSet.getStream());
		FaceDetector detector = new FaceDetector(descriptor, params);
		List<FaceRegion> faces = detector.findFaces(ip.convertToByteProcessor());
		IJ.log(faces.size() + " faces found!");
		
		// show results
		ImageProcessor cp = ip.convertToColorProcessor();
		for (FaceRegion r : faces) {
			draw(r, cp);
		}
		new ImagePlus(this.getClass().getSimpleName() + ": results", cp).show();
		
		if (extractFaceImages && !faces.isEmpty()) {
			int faceCtr = 0;
			for (FaceRegion r : faces) {
				ip.setRoi(r.x, r.y, r.width, r.height);
				new ImagePlus("Face " + faceCtr, ip.crop()).show();
				faceCtr++;
			}
		}
	}
	
	private boolean runDialog(Parameters params) {
		GenericDialog gd = new GenericDialog("Set Face Detector Parameters");
		gd.addChoice("Haar training set", getEnumNames(HaarTrainingSet.class), trainingSet.name());
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
	
	private static <E extends Enum<E>> String[] getEnumNames(Class<E> enumclass) {
		E[] eConstants = enumclass.getEnumConstants();
		String[] eNames = new String[eConstants.length];
		for (int i = 0; i < eConstants.length; i++) {
			eNames[i] = eConstants[i].name();
		}
		return eNames;
	}

}
