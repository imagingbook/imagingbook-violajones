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

/**
 * A binary tree for detection. At each node of the tree, a test (feature) is done on the window. Depending on the
 * result, either the left or right child of the node is selected and so on. When the process reaches a leaf, the
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
