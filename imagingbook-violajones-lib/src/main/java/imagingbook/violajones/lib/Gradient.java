/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package imagingbook.violajones.lib;

import ij.process.FloatProcessor;
import imagingbook.common.edges.CannyEdgeDetector;
import imagingbook.common.edges.CannyEdgeDetector.Parameters;
import imagingbook.common.image.IntegralImage;

/**
 * Used to calculate the gradient magnitude inside a given rectangular region.
 *
 * @author W. Burger
 */
public class Gradient {
	// TODO: make more efficient
	static IntegralImage makeGradientIntegral(int[][] I, double sigma) {
		
		Parameters params = new Parameters();
		params.gSigma = sigma;
		params.normGradMag = false;

		CannyEdgeDetector detector = new CannyEdgeDetector(new FloatProcessor(I), params);
		FloatProcessor Em = detector.getEdgeMagnitude();
		
//		Em.resetMinAndMax();
//		new ImagePlus("Em", Em).show();
		
		int w = Em.getWidth();
		int h = Em.getHeight();
		
		int[][] emi = new int[w][h];
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				emi[u][v] = (int) Em.getf(u, v);
			}
		}
		return new IntegralImage(emi);
	}
	
}
