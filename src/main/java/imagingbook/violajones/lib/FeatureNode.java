package imagingbook.violajones.lib;

import java.util.LinkedList;
import java.util.List;

import imagingbook.lib.image.IntegralImage;


/**
 * Represents a Haar feature, usually composed of multiple rectangular
 * patches.
 * 
 * @author WB
 *
 */
public class FeatureNode {
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	public static final double NO_VALUE = Double.NaN;
	public static final int NO_CHILD = -1;

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
	 * Evaluates this feature node.
	 * @param II
	 * @param u
	 * @param v
	 * @param scale
	 * @return
	 */
	protected int eval(IntegralImage II, int u, int v, double scale) {
		// calculate the area of the search window:
		final int w = (int) Math.round(scale * width);
		final int h = (int) Math.round(scale * height);
		
		// calculate variance (std. deviation) for the current search window:
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
			patch.print(patchCnt);
			patchCnt++;
		}
	}
}
