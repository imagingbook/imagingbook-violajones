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

import java.util.LinkedList;
import java.util.List;

/**
 * A single stage of the cascade. Each stage consists of several trees and a threshold. When using the detector on a
 * window, each tree returns a value. If the sum of these values exceeds the threshold, the stage succeeds, else it
 * fails (and the window is not the object looked for).
 */
public class Stage {
	
	private final double threshold;
	private final List<FeatureTree> trees;

	public Stage(double threshold) {
		this.threshold = threshold;
		trees = new LinkedList<FeatureTree>();
	}

	public void addTree(FeatureTree t) {
		trees.add(t);
	}
	
	public List<FeatureTree> getTrees() {
		return trees;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	// Sum up the values returned by each tree of this stage.
	protected double sum(IntegralImage II, int u, int v, double scale) {
		double sum = 0;
		for (FeatureTree tree : trees) {
			sum = sum + tree.eval(II, u, v, scale);
		}
		return sum;
	}
	
	// Stage succeeds only if the sum exceeds the stage threshold.
	protected boolean pass(IntegralImage II, int u, int v, double scale) {
		return sum(II, u, v, scale) > threshold;
	}
	

	public void print() {
		System.out.format("   threshold = %.4f\n", threshold);
		int treeCnt = 0;
		for (FeatureTree tree : trees) {
			System.out.format("   tree = %d\n", treeCnt);
			tree.print();
			treeCnt++;
		}
	}

}
