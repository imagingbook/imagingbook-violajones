package imagingbook.violajones.lib;

import java.util.LinkedList;
import java.util.List;

import imagingbook.lib.image.IntegralImage;
//import imagingbook.violajones.lib.integralIB.IntegralImage;
//import imagingbook.violajones.lib.integral1.IntegralImage;
//import imagingbook.violajones.lib.integral2.IntegralImage;

/**
 * A stage of the detector. Each stage consists of several trees and a
 * threshold. When using the detector on a window, each tree returns a value. If
 * the sum of these values exceeds the threshold, the stage succeeds, else it
 * fails (and the window is not the object looked for).
 *
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
		for (FeatureTree tree : trees) {
			tree.print();
		}
	}
	

	protected boolean pass(IntegralImage II, int u, int v, double scale) {
		// sum up the values returned by each tree of this stage:
		double sum = 0;
		for (FeatureTree tree : getTrees()) {
			sum = sum + tree.getVal(II, u, v, scale);
		}
		// stage succeeds only if the sum exceeds the stage threshold:
		//IJ.log("sum = " + sum + " " + ( sum > this.threshold));
		return sum > this.threshold;
	}

}
