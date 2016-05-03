package imagingbook.violajones.lib;

import java.util.ArrayList;
import java.util.List;

import imagingbook.violajones.lib.Feature;

import imagingbook.lib.image.IntegralImage;
//import imagingbook.violajones.lib.integralIB.IntegralImage;
//import imagingbook.violajones.lib.integral1.IntegralImage;
//import imagingbook.violajones.lib.integral2.IntegralImage;

/**
 * A binary tree for detection. At each node of the tree, a test (feature) is
 * done on the window. Depending on the result, either the left or right child
 * of the node is selected and so on. When the process reaches a leaf, the
 * corresponding value is returned.
 */
public class FeatureTree {
	
	protected enum Direction {
		LEFT, RIGHT;
	}

	final List<Feature> features;

	protected FeatureTree() {
		features = new ArrayList<Feature>();
	}
	
	public List<Feature> getFeatures() {
		return features;
	}

	protected void addFeature(Feature f) {
		features.add(f);
	}

	protected double getVal(IntegralImage II, int u, int v, double scale) {
		Feature curFeature = features.get(0); // start at root of this tree
		while (true) {
			// evaluate the current feature to decide where to proceed:
			Direction dir = curFeature.getLeftOrRight(II, u, v, scale);
			if (dir == Direction.LEFT) {
				// if the left child has a value, return it:
				if (!Double.isNaN(curFeature.leVal)) { // (cur_node.hasLeVal)
					return curFeature.leVal;
				}
				// else move to the left child node
				else {
					curFeature = features.get(curFeature.leNode);
				}
			} 
			else {
				// if the right child has a value, return it:
				if (!Double.isNaN(curFeature.riVal)) { // (cur_node.hasRiVal)
					return curFeature.riVal;
				}
				// else move to the right child node:
				else {
					curFeature = features.get(curFeature.riNode);
				}
			}
		}
	}

	public void print() {
		for (Feature f : features) {
			f.print();
		}
	}

}
