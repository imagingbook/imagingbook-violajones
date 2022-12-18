/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package imagingbook.violajones.lib;

import ij.process.ByteProcessor;
import imagingbook.common.image.IntegralImage;
import imagingbook.common.util.ParameterBundle;

import java.util.LinkedList;
import java.util.List;

/**
 * The core class.
 * 
 * @author WB
 * @version 2022/04/15
 */
public class FaceDetector {
	// TODO: change constructors to use the source image, pre-calculate everything.
	// TODO: use annotations on Parameters fields
	/**
	 * Nested class representing parameters and their default values.
	 */
	public static class Parameters implements ParameterBundle {
		/** The initial ratio between the window size and the Haar classifier size (default 2). */
		public double baseScale = 2.00;
		
		/** The scale increment of the window size, at each step (default 1.25). */
		public double scaleStep = 1.25;
		
		/** The shift of the window at each sub-step, in terms of percentage of the window size. */
		public double winShiftFraction = 0.1;
		
		/** The minimum number of rectangles needed for the corresponding detection to be kept */
		public int minNeighbors = 1;
		
		/** Flag indicating if Canny pruning should be applied. */
		public boolean doGradientPruning = false;
		
		/** The minimum percentage of overlap for merging two regions. */
		public double minMergeRegionOverlap = 0.2;
		
		/** The width of the Gaussian blur applied before gradient calculation. */
		public double gradientSigma = 2.0;
		
		/** The minimum (normalized) gradient magnitude if pruning is on. */
		public double minGradientMagnitude = 20;
		
		/** The maximum (normalized) gradient magnitude if pruning is on. */
		public double maxGradientMagnitude = 100;
	}
	
	private final Parameters params;
	private final HaarCascadeDescriptor cascade;
	
	// --------------------------------------------------------------------------------
	
	/**
	 *  The only constructor.
	 * @param cascade a Haar cascade descriptor
	 * @param params a parameter object (default settings are used if {@code null} is passed)
	 */
	public FaceDetector(HaarCascadeDescriptor cascade, Parameters params) {
		this.cascade = cascade;
		this.params = (params != null) ? params : new Parameters();
	}
	
	// --------------------------------------------------------------------------------

	/**
	 * Detects faces in the supplied image.
	 * @param ip the input image
	 * @return the list of rectangles containing searched objects
	 */
	public List<FaceRegion> findFaces(ByteProcessor ip) {
		return findFaces(ip.getIntArray());
	}
	
	public List<FaceRegion> findFaces(int[][] I) {
		List<FaceRegion> facelist = new LinkedList<FaceRegion>();
		final int imgW = I.length;
		final int imgH = I[0].length;
	
		// maximum size of detector is the whole image 
		double maxScale = Math.min(
				(double) imgW / cascade.getWidth(),
				(double) imgH / cascade.getHeight());
		
		//IJ.log("maxScale = " + maxScale);

		IntegralImage II = new IntegralImage(I);

		// precalculate Canny edges if option is on
		//int[][] canny = null;
		IntegralImage canny = null;
		if (params.doGradientPruning) {
			//canny = CannyPruner.getIntegralCanny(I);
			canny = Gradient.makeGradientIntegral(I, params.gradientSigma);
		}

		// apply the detector at each scale:
		double scale = params.baseScale;
		while (scale < maxScale) {
			//IJ.log("scale = " + scale);
			// calculate the sliding step of the window in each direction:
			final int uStep = (int) Math.round(scale * cascade.getWidth() * params.winShiftFraction);
			final int vStep = (int) Math.round(scale * cascade.getHeight() * params.winShiftFraction);
			final int curW =  (int) Math.round(scale * cascade.getWidth());
			final int curH =  (int) Math.round(scale * cascade.getHeight());
	
			// check each position of the window on the image:
			for (int v = 0; v < imgH - curH; v += vStep) {
				for (int u = 0; u < imgW - curW; u += uStep) {
					/*
					 * If Canny pruning is on, compute the edge density of the
					 * zone. If it is too low, the object should not be there so
					 * skip the region.
					 */
					if (params.doGradientPruning) {
						//int edges_density = canny[u + curW][v + curH] + canny[u][v] - canny[u][v + curH] - canny[u + curW][v];
						double gradientMag = canny.getBlockSum1(u, v, u + curW - 1, v + curH - 1);
						double d = gradientMag / (curW * curH);
						if (d < params.minGradientMagnitude || d > params.maxGradientMagnitude)
							continue;	// skip this region
					}
					
					// apply each detector stage to the current window, reject if one stage fails
					boolean pass = true;
					for (Stage stage : cascade.getStages()) {
						if (!stage.pass(II, u, v, scale)) {
							pass = false;
							break;
						}
					}
					// if current window passed all stages, add to the results
					if (pass) {
						facelist.add(new FaceRegion(u, v, curW, curH));
					}
				}
			}
			scale = scale * params.scaleStep;
		}

		return merge(facelist.toArray(new FaceRegion[0]),
				params.minNeighbors, params.minMergeRegionOverlap);
	}
		
	/**
	 * Merge the raw detections resulting from the detection step to avoid
	 * multiple detections of the same object. A threshold on the minimum
	 * numbers of rectangles that need to be merged for the resulting detection
	 * to be kept can be given, to lower the rate of false detections. Two
	 * rectangles need to be merged if they overlap enough.
	 * TODO: CHECK!! Make rectangles immutable!
	 * 
	 * @param facesInit the raw detections returned by the detection algorithm
	 * @param min_neighbors the minimum number of rectangles needed for the corresponding detection to be kept
	 * @param minOverlap the minimum overlap required for merging two faces
	 * @return The merged rectangular detections.
	 */
	protected List<FaceRegion> merge(FaceRegion[] facesInit, int min_neighbors, double minOverlap) {
		List<FaceRegion> facesMerged = new LinkedList<FaceRegion>();
		int[] result = new int[facesInit.length];
		int nb_classes = 0;
		for (int i = 0; i < facesInit.length; i++) {
			boolean found = false;
			for (int j = 0; j < i; j++) {
				if (facesInit[j].overlaps(facesInit[i], minOverlap)) {
					found = true;
					result[i] = result[j];
				}
			}
			if (!found) {
				result[i] = nb_classes;
				nb_classes++;
			}
		}
		int[] neighbors = new int[nb_classes];
		FaceRegion[] facesTmp = new FaceRegion[nb_classes];
		
		for (int i = 0; i < nb_classes; i++) {
			neighbors[i] = 0;
			facesTmp[i] = new FaceRegion(0, 0, 0, 0);	// TODO: make more efficient/polish!
		}
		
		for (int i = 0; i < facesInit.length; i++) {
			neighbors[result[i]]++;
			int x = facesTmp[result[i]].x + facesInit[i].x;
			int y = facesTmp[result[i]].y + facesInit[i].y;
			int w = facesTmp[result[i]].height + facesInit[i].height;
			int h = facesTmp[result[i]].width + facesInit[i].width;
			facesTmp[result[i]] = new FaceRegion(x, y, w, h);
		}
		
		for (int i = 0; i < nb_classes; i++) {
			int n = neighbors[i];
			if (n >= min_neighbors) {
				int x = (facesTmp[i].x * 2 + n) / (2 * n);
				int y = (facesTmp[i].y * 2 + n) / (2 * n);
				int w = (facesTmp[i].width * 2 + n) / (2 * n);
				int h = (facesTmp[i].height * 2 + n) / (2 * n);
				facesMerged.add(new FaceRegion(x, y, w, h));
			}
		}
		
		return facesMerged;
	}

}
