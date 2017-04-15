package imagingbook.violajones.lib;

import java.util.LinkedList;
import java.util.List;

import imagingbook.lib.image.IntegralImage;
//import imagingbook.violajones.lib.integralIB.IntegralImage;
//import imagingbook.violajones.lib.integral1.IntegralImage;
//import imagingbook.violajones.lib.integral2.IntegralImage;

/**
 * A single stage of the cascade. Each stage consists of several trees and a
 * threshold. When using the detector on a window, each tree returns a value. If
 * the sum of these values exceeds the threshold, the stage succeeds, else it
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

	public void print() {
		System.out.format("   threshold = %.4f\n", threshold);
		int treeCnt = 0;
		for (FeatureTree tree : trees) {
			System.out.format("   tree = %d\n", treeCnt);
			tree.print();
			treeCnt++;
		}
	}
	
	/**
	 * Sum up the values returned by each tree of this stage.
	 */
	protected double sum(IntegralImage II, int u, int v, double scale) {
		double sum = 0;
		for (FeatureTree tree : trees) {
			sum = sum + tree.eval(II, u, v, scale);
		}
		return sum;
	}
	
	/**
	 * Stage succeeds only if the sum exceeds the stage threshold.
	 */
	protected boolean pass(IntegralImage II, int u, int v, double scale) {
		return sum(II, u, v, scale) > threshold;
	}

}
