/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package imagingbook.violajones.lib;

import imagingbook.common.image.IntegralImage;


/**
 * Represents a Haar feature, usually composed of multiple rectangular patches (of type {@link FeaturePatch}). Instances
 * of this class make the nodes of a {@link FeatureTree}.
 *
 * @author WB
 */
public class FeatureNode {
	
	protected static final double NO_VALUE = Double.NaN;
	protected static final int NO_CHILD = -1;
	protected static final int LEFT = 0;
	protected static final int RIGHT = 1;

	private final int width, height;
	private final double threshold;
	private final double[] values;	// terminal values
	private final int[] children;	// indexes of child nodes
	private final FeaturePatch[] patches;


	public FeatureNode(int width, int height, double threshold, double valL, int childL, double valR, int childR,
			FeaturePatch[] patches) {
		this.width = width;
		this.height = height;
		this.threshold = threshold;
		this.values = new double[] {valL, valR};
		this.children = new int[] {childL, childR};
		this.patches = patches;
	}
	
	public double getValue(int dir) {
		return values[dir];
	}
	
	public boolean hasValue(int dir) {
		return !Double.isNaN(values[dir]);
	}
	
	public int getChild(int dir) {
		return children[dir];
	}
	
	public boolean hasChild(int dir) {
		return children[dir] != NO_CHILD;
	}
	
	public FeaturePatch[] getPatches() {
		return patches;
	}

	/**
	 * Evaluates this feature node by applying its rectangular patches.
	 * Returns the branching direction (LEFT or RIGHT), depending on how the normalized patch 
	 * sum compares to the threshold.
	 * @param II input image (as an integral image) 
	 * @param u left position of the candidate subimage
	 * @param v top position of the candidate subimage
	 * @param scale the current scale to be applied
	 * @return The branching direction (LEFT or RIGHT).
	 */
	public int eval(IntegralImage II, int u, int v, double scale) {
		// calculate the area of the subimage:	TODO: w,h never change, make more efficient
		final int w = (int) Math.round(scale * width);
		final int h = (int) Math.round(scale * height);
		
		// calculate variance (std. deviation) for the complete subimage: TODO: do only once for the subimage?
		double variance = II.getVariance(u, v, u + w - 1, v + h - 1);
		double stdDev = (variance > 1) ? Math.sqrt(variance) : 1.0;

		// calculate weighted sum over all patches (rectangles) of this feature
		double patchSum = 0.0;
		for (FeaturePatch patch : patches) {
			// scale the patch's relative position and size
			final int ua = u + (int) Math.round(scale *  patch.x);
			final int va = v + (int) Math.round(scale *  patch.y);
			final int ub = u + (int) Math.round(scale * (patch.x + patch.width) - 1);
			final int vb = v + (int) Math.round(scale * (patch.y + patch.height) - 1);
			
			// add the patch's block sum and apply the assigned weight (pos/neg)
			patchSum = patchSum + II.getBlockSum1(ua, va, ub, vb) * patch.weight;
		}
		// normalize the sum of all patches by the effective window area:
		double patchSumNorm = patchSum / (w * h);
			
		// return direction depending on how the normalized patch sum compares to the threshold
		return (patchSumNorm < threshold * stdDev) ? LEFT : RIGHT;
	}


	public void print(int featureCnt) {
		System.out.format("     feature %d: w=%d h=%d valL=%.4f valR=%.4f childL=%d childR=%d th=%.4f patches=%d\n", 
				featureCnt, width, height, getValue(LEFT), getValue(RIGHT), getChild(LEFT), getChild(RIGHT), 
				threshold, patches.length);
		int patchCnt = 0;
		for (FeaturePatch patch : patches) {
			System.out.println("           patch " + patchCnt + " " + patch.toString());
			patchCnt++;
		}
	}
}
