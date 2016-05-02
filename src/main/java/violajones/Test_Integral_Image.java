package violajones;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import violajones.iimg.IntegralImage;
//import imagingbook.lib.image.IntegralImage;
//import violajones.lib.IntegralImage;

public class Test_Integral_Image implements PlugInFilter {

	public int setup(String arg0, ImagePlus im) {
		return DOES_8G + NO_CHANGES;
	}

	public void run(ImageProcessor ip) {
		

		// --------------
		
		IntegralImage iIC = new IntegralImage((ByteProcessor) ip);
		
		long[][] II1C = iIC.getS1();
		long[][] II2C = iIC.getS2();
		
		FloatProcessor II1fpC = new FloatProcessor(toFloatArray(II1C));
		new ImagePlus("II1 C", II1fpC).show();
		
		FloatProcessor II2fpC = new FloatProcessor(toFloatArray(II2C));
		new ImagePlus("II2 C", II2fpC).show();
	}
	
	
	float[][] toFloatArray(long[][] A) {
		float[][] B = new float[A.length][A[0].length];
		for (int i = 0; i < A.length; i++) {
			for (int j = 0; j < A[0].length; j++) {
				B[i][j] = (float) A[i][j];
			}
		}
		return B;
	}

}
