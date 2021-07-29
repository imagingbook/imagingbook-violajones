package imagingbook.violajones.lib;

import ij.process.FloatProcessor;
import imagingbook.lib.image.IntegralImage;
import imagingbook.pub.color.edge.CannyEdgeDetector;
import imagingbook.pub.color.edge.CannyEdgeDetector.Parameters;

/**
 * Used to calculate the gradient magnitude inside a given rectangular region.
 * TODO: make more efficient
 * 
 * @author W. Burger
 *
 */
public class Gradient {
	
	protected static IntegralImage makeGradientIntegral(int[][] I, double sigma) {
		
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
