/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package violajones_demos;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import imagingbook.common.ij.DialogUtils;
import imagingbook.common.ij.IjUtils;
import imagingbook.core.jdoc.JavaDocHelp;
import imagingbook.core.resource.ImageResource;
import imagingbook.violajones.data.HaarTrainingSet;
import imagingbook.violajones.images.SampleFaceImage;
import imagingbook.violajones.lib.FaceDetector;
import imagingbook.violajones.lib.FaceDetector.Parameters;
import imagingbook.violajones.lib.FaceRegion;
import imagingbook.violajones.lib.HaarCascadeDescriptor;

import java.awt.Color;
import java.util.List;

import static imagingbook.common.ij.IjUtils.noCurrentImage;


/**
 * ImageJ plugin. Face detection ImageJ plugin using 'imagingbook.violajones' implementation of the Viola-Jones face
 * detection algorithm: Paul Viola and Michael Jones, "Robust Real-time Face Detection", International Journal of
 * Computer Vision 57.2 (2004), pp. 137–154.
 *
 * @author W. Burger
 * @version 2018/12/09
 */
public class Find_Faces implements PlugInFilter, JavaDocHelp {

	private static ImageResource sampleImage = SampleFaceImage.Nobelaureates2007_jpg;
	private static HaarTrainingSet trainingSet = HaarTrainingSet.FrontalFaceAlt2;
	private static boolean extractFaceImages = false;

	private static FaceDetector.Parameters params = new Parameters();
	static {
		// customize initial face detection parameters if desired
		params.baseScale = 2.0;
		params.scaleStep = 1.25;
		params.winShiftFraction = 0.1;
		params.minMergeRegionOverlap = 0.2;
		params.minNeighbors = 1;
		params.gradientSigma = 2.0;
		params.doGradientPruning = false;
		params.minGradientMagnitude = 20;
		params.maxGradientMagnitude = 100;
	}

	private static Color FaceMarkerColor = Color.green;
	private static double FaceMarkerStrokewidth = 1.0;
	
	private ImagePlus im = null;

	/** Constructor, asks to open a predefined sample image if no other image is currently open. */
	public Find_Faces() {
		if (noCurrentImage()) {
			DialogUtils.askForSampleImage(sampleImage);
		}
	}

	@Override
	public int setup(String arg0, ImagePlus im) {
		this.im = im;
		return DOES_ALL;
	}
	
	@Override
	public void run(ImageProcessor ip) {

		if (!runDialog(params))
			return;
		
		IjUtils.setRgbConversionWeights(ip);
		HaarCascadeDescriptor descriptor = HaarCascadeDescriptor.fromInputStream(trainingSet.getStream());
		FaceDetector detector = new FaceDetector(descriptor, params);
		List<FaceRegion> faces = detector.findFaces(ip.convertToByteProcessor());

		if (faces.isEmpty()) {
			IJ.log("no faces found.");
			return ;
		}

		IJ.log("found " + faces.size() + " faces.");
		
		// mark detected faces
		im.setOverlay(drawFacesToOverlay(faces));
		
		if (extractFaceImages) {
			int cnt = 0;
			for (FaceRegion r : faces) {
				ip.setRoi(r.x, r.y, r.width, r.height);
				new ImagePlus("Face " + cnt, ip.crop()).show();
				cnt++;
			}
		}
	}

	private Overlay drawFacesToOverlay(List<FaceRegion> faces) {
		Overlay oly = new Overlay();
		for (FaceRegion f : faces) {
			Roi roi = new Roi(f.x, f.y, f.width, f.height);
			roi.setStrokeColor(FaceMarkerColor);
			roi.setStrokeWidth((float)FaceMarkerStrokewidth);
			oly.add(roi);
		}
		return oly;
	}

	// --------------------------------------------------

	private boolean runDialog(Parameters params) {
		GenericDialog gd = new GenericDialog("Set Face Detector Parameters");
		gd.addHelp(getJavaDocUrl());
		gd.addEnumChoice("Haar training set", trainingSet);
		gd.addMessage("Face detection parameters:");
		DialogUtils.addToDialog(params, gd);
		gd.addMessage("Output:");
		gd.addCheckbox("extractFaceImages", extractFaceImages);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}

		trainingSet = gd.getNextEnumChoice(HaarTrainingSet.class);
		DialogUtils.getFromDialog(params, gd);
		extractFaceImages = gd.getNextBoolean();
		return true;
	}

}
