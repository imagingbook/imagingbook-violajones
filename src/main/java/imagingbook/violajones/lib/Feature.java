package imagingbook.violajones.lib;

import java.util.LinkedList;
import java.util.List;

import imagingbook.lib.image.IntegralImage;
import imagingbook.violajones.lib.FeatureTree.Direction;

//import imagingbook.violajones.lib.integralIB.IntegralImage;
//import imagingbook.violajones.lib.integral1.IntegralImage;
//import imagingbook.violajones.lib.integral2.IntegralImage;

//-------------------------------------------------------------------------------
/**
 * Made a non-static nested class, such that width, height are inherited from the enclosing 
 * HaarCascade instance.
 * @author WB
 *
 */
public class Feature {

	private final List<FeaturePatch> patches;
	private final double threshold;
	final double leVal;
	final double riVal;
	final int leNode;
	final int riNode;
	final int width, height;

	public Feature(int width, int height, double threshold, double leVal, int leNode, double riVal, int riNode) {
		this.patches = new LinkedList<FeaturePatch>();
		this.width = width;
		this.height = height;
		this.threshold = threshold;
		this.leVal = leVal;
		this.leNode = leNode;
		this.riVal = riVal;	
		this.riNode = riNode;
	}
	
	public List<FeaturePatch> getRectangles() {
		return patches;
	}

	/**
	 * Evaluates this feature consisting of multiple rectangles.
	 * @param II
	 * @param u
	 * @param v
	 * @param scale
	 * @return
	 */
	protected Direction getLeftOrRight(IntegralImage II, int u, int v, double scale) {
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
		return (patchSumNorm < threshold * stdDev) ? Direction.LEFT : Direction.RIGHT;
	}

	protected void add(FeaturePatch r) {
		patches.add(r);
	}

	public void print() {
		System.out.format("     feature: w=%d h=%d leVal=%.4f riVal=%.4f th=%.4f rects=%d\n", 
				width, height, leVal, riVal, threshold, patches.size());
		
	}
}
