package imagingbook.violajones.lib;

import imagingbook.lib.image.IntegralImage;

/**
 * A binary tree for detection. At each node of the tree, a test (feature) is
 * done on the window. Depending on the result, either the left or right child
 * of the node is selected and so on. When the process reaches a leaf, the
 * corresponding value is returned.
 */
public class FeatureTree {
	
	//private final List<FeatureNode> nodes;
	private final FeatureNode[] nodes;

	protected FeatureTree(FeatureNode[] nodes) {
		this.nodes = nodes;
	}
	
	public FeatureNode[] getFeatures() {
		return nodes;
	}

//	protected double eval(IntegralImage II, int u, int v, double scale) {
//		FeatureNode node = nodes[0]; // start at root of this tree
//		while (true) {
//			// evaluate the current feature node to decide where to proceed:
//			int dir = node.eval(II, u, v, scale);
//			// if the current node has a value for 'dir', return it:
//			if (node.hasValue(dir)) {
//				return node.getValue(dir);
//			}
//			// otherwise continue with the child node for 'dir'
//			else {
//				node = nodes[node.getChild(dir)];
//			}
//		}
//	}
	
	// evaluate the feature tree
	protected double eval(IntegralImage II, int u, int v, double scale) {
		FeatureNode feature = nodes[0]; // start at root of this tree
		while (true) {
			// evaluate the current feature node to decide where to proceed:
			int dir = feature.eval(II, u, v, scale);
			// if the current node has a value for 'dir', return it:
			if (feature.hasValue(dir)) {
				return feature.getValue(dir);
			}
			// otherwise continue with the child node for 'dir'
			else {
				feature = nodes[feature.getChild(dir)];
			}
		}
	}

	public void print() {
		int featureCnt = 0;
		for (FeatureNode node : nodes) {
			node.print(featureCnt);
			featureCnt++;
		}
	}

}
