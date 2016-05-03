package imagingbook;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

//import violajones.iimg.IntegralImage;
//import imagingbook.lib.image.IntegralImage;


public class Test_Integral_Image2 implements PlugInFilter {

	public int setup(String arg0, ImagePlus im) {
		return DOES_8G + NO_CHANGES;
	}

	public void run(ImageProcessor ip) {
		

		// --------------
		
		imagingbook.violajones.lib.integral1.IntegralImage iICvj1 = new imagingbook.violajones.lib.integral1.IntegralImage((ByteProcessor) ip);
		imagingbook.violajones.lib.integral2.IntegralImage iICvj2 = new imagingbook.violajones.lib.integral2.IntegralImage((ByteProcessor) ip);
		imagingbook.violajones.lib.integralIB.IntegralImage iICib = new imagingbook.violajones.lib.integralIB.IntegralImage((ByteProcessor) ip);
		
		long[][] II1Cvj1 = iICvj1.getS1();
		long[][] II1Cvj2 = iICvj2.getS1();
		long[][] II1Cib = iICib.getS1();
		
		
//		FloatProcessor II1fp_vj = new FloatProcessor(toFloatArray(II1Cvj));
//		new ImagePlus("II1 Viola Jones", II1fp_vj).show();
//		
//		FloatProcessor II1fp_ib = new FloatProcessor(toFloatArray(II1Cib));
//		new ImagePlus("II1 ImagingBook", II1fp_ib).show();
		
//		FloatProcessor II1ib = new FloatProcessor(toFloatArray(II1Cib));
//		new ImagePlus("II1ib", II1ib).show();
	
		// -----------------------
		
		long[][] II2Cvj1 = iICvj1.getS2();
		long[][] II2Cvj2 = iICvj2.getS2();
		long[][] II2Cib = iICib.getS2();
		
//		FloatProcessor II2fp_vj = new FloatProcessor(toFloatArray(II2Cvj));
//		new ImagePlus("II2 Viola Jones", II2fp_vj).show();
//		
//		FloatProcessor II2fp_ib = new FloatProcessor(toFloatArray(II2Cib));
//		new ImagePlus("II2 ImagingBook", II2fp_ib).show();
		
//		FloatProcessor II2fp_ib = new FloatProcessor(toFloatArray(II2Cib));
//		new ImagePlus("II2 ImagingBook", II2fp_ib).show();
		
		int ua = 1, ub = 17;	// x-range
		int va = 1, vb = 123;	// y-range
		
		IJ.log("Blocksum1 - VJ1: " + iICvj1.getBlockSum1(ua, va, ub, vb));
		IJ.log("Blocksum1 - VJ2: " + iICvj2.getBlockSum1(ua, va, ub, vb));
		IJ.log("Blocksum1 - IB: " + iICib.getBlockSum1(ua+1, va+1, ub, vb));
				
//		IJ.log("Size - VJ1: " + iICvj1.getSize(ua, va, ub, vb));
//		IJ.log("Size - VJ2: " + iICvj2.getSize(ua, va, ub, vb));
		IJ.log("Size - IB: " + iICib.getSize(ua+1, va+1, ub, vb));
		
		
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
