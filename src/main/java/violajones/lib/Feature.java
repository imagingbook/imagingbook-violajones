package violajones.lib;

import java.util.LinkedList;
import java.util.List;

import violajones.iimg.IntegralImage;
//import imagingbook.lib.image.IntegralImage;
import violajones.lib.FeatureTree.Direction;

//-------------------------------------------------------------------------------
/**
 * Made a non-static nested class, such that width, height are inherited from the enclosing 
 * HaarCascade instance.
 * @author WB
 *
 */
public class Feature {

	private final List<FeaturePatch> rectangles;
	private final double threshold;
	final double leVal;
	final double riVal;
	final int leNode;
	final int riNode;
	final int width, height;

	public Feature(int width, int height, double threshold, double leVal, int leNode, double riVal, int riNode) {
		rectangles = new LinkedList<FeaturePatch>();
		this.width = width;
		this.height = height;
		
		this.threshold = threshold;
		
		this.leVal = leVal;
		this.leNode = leNode;
		
		this.riVal = riVal;	
		this.riNode = riNode;
	}
	
	public List<FeaturePatch> getRectangles() {
		return rectangles;
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
		final int w = (int) Math.round(scale * width);	// TODO: could be precalculated?
		final int h = (int) Math.round(scale * height);
		final double winArea = w * h;
		
		//double winMean  = II.getMean(u, v, u + w, v + h); 		 //total1 / winArea;

		// calculate weighted sum over all rectangles of this feature
		double sum = 0.0;
		for (FeaturePatch rect : this.rectangles) {
			// scale the rectangle
			final int u0 = u + (int) Math.round(scale *  rect.x);
			final int u1 = u + (int) Math.round(scale * (rect.x + rect.width));
			final int v0 = v + (int) Math.round(scale *  rect.y);
			final int v1 = v + (int) Math.round(scale * (rect.y + rect.height));
			// get rectangle's sum and apply the assigned weight (pos/neg)
			sum = sum + II.getBlockSum1(u0, v0, u1, v1) * rect.weight;
		}
		// normalize result by the window's area:
		double sumNorm = sum / winArea;
		
		// calculate variance (std. deviation) for the current window
		double winVariance = II.getVariance(u, v, u + w, v + h); // total2 / winArea - winMean * winMean;
		double winStdDev = (winVariance > 1) ? Math.sqrt(winVariance) : 1.0;
		
		// return LEFT or RIGHT depending on how the total sum compares to threshold
		return (sumNorm < threshold * winStdDev) ? Direction.LEFT : Direction.RIGHT;
	}

	protected void add(FeaturePatch r) {
		rectangles.add(r);
	}

	public void print() {
		System.out.format("     feature: w=%d h=%d leVal=%.4f riVal=%.4f th=%.4f rects=%d\n", 
				width, height, leVal, riVal, threshold, rectangles.size());
		
	}
}
