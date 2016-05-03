package imagingbook.violajones.lib;

import ij.plugin.filter.Convolver;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import imagingbook.lib.image.IntegralImage;
import imagingbook.pub.color.edge.CannyEdgeDetector;

/**
 * Used to calculate the gradient magnitude inside a given
 * rectangular region.
 * @author W. Burger
 *
 */
public class Gradient {
	
	public static IntegralImage makeGradientIntegral(int[][] I, double sigma) {
		CannyEdgeDetector.Parameters par = new CannyEdgeDetector.Parameters(); 
		par.gSigma = sigma; // sigma of Gaussian

		CannyEdgeDetector detector = new CannyEdgeDetector(new FloatProcessor(I), par); 
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
