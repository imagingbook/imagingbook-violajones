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

	protected double getVal(IntegralImage II, int u, int v, double scale) {
		FeatureNode curFeature = nodes[0]; //nodes.get(0); // start at root of this tree
		while (true) {
			// evaluate the current feature node to decide where to proceed:
			int dir = curFeature.eval(II, u, v, scale);
			// if the current node has a value for 'dir', return it:
			if (curFeature.hasValue(dir)) {
				return curFeature.getValue(dir);
			}
			// otherwise continue with the child node for 'dir'
			else {
				curFeature = nodes[curFeature.getChild(dir)];
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
