package imagingbook.violajones.lib;

import java.util.LinkedList;
import java.util.List;

import imagingbook.lib.image.IntegralImage;


// TODO: Make a non-static inner class, such that width, height are inherited from the enclosing HaarCascade instance.

public class FeatureNode {
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	public static final double NO_VALUE = Double.NaN;
	public static final int NO_CHILD = -1;

	private final int width, height;
	private final double threshold;
	private final double[] values;	// terminal values
	private final int[] children;		// indexes of child nodes
	private final List<FeaturePatch> patches;

	public FeatureNode(int width, int height, double threshold, double leVal, int leNode, double riVal, int riNode) {
		this.width = width;
		this.height = height;
		this.threshold = threshold;
		this.values = new double[] {leVal, riVal};
		this.children = new int[] {leNode, riNode};
		this.patches = new LinkedList<FeaturePatch>();
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
	
	public List<FeaturePatch> getRectangles() {
		return patches;
	}
	
	protected void add(FeaturePatch r) {
		patches.add(r);
	}

	/**
	 * Evaluates this feature consisting of multiple rectangles.
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
		final double area = w * h;
		
		// calculate variance (std. deviation) for the current search window:
		double variance = II.getVariance(u, v, u + w - 1, v + h - 1);
		double stdDev = (variance > 1) ? Math.sqrt(variance) : 1.0;
		
		//double winMean  = II.getMean(u, v, u + w, v + h); 		 //total1 / winArea;

		// calculate weighted sum over all rectangles (patches) of this feature
		double patchSum = 0.0;
		for (FeaturePatch patch : patches) {
			// scale the patch's relative position and size
			final int ua = u + (int) Math.round(scale *  patch.x);
			final int va = v + (int) Math.round(scale *  patch.y);
			final int ub = u + (int) Math.round(scale * (patch.x + patch.width) - 1);
			final int vb = v + (int) Math.round(scale * (patch.y + patch.height) - 1);
			
			// get the patch's sum and apply the assigned weight (pos/neg)
			patchSum = patchSum + II.getBlockSum1(ua, va, ub, vb) * patch.weight;
		}
		// normalize the sum of all patches by the window's area:
		double patchSumNorm = patchSum / area;
			
		// return LEFT or RIGHT depending on how the normalized patch sum compares to the threshold
		return (patchSumNorm < threshold * stdDev) ? LEFT : RIGHT;
	}

	public void print(int featureCnt) {
		System.out.format("     feature %d: w=%d h=%d leVal=%.4f riVal=%.4f leNode=%d riNode=%d th=%.4f rects=%d\n", 
				featureCnt, width, height, getValue(LEFT), getValue(RIGHT), getChild(LEFT), getChild(RIGHT), 
				threshold, patches.size());
		
	}
}
