package violajones;

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
		
		violajones.iimg.IntegralImage iICvj = new violajones.iimg.IntegralImage((ByteProcessor) ip);
		imagingbook.lib.image.IntegralImage iICib = new imagingbook.lib.image.IntegralImage((ByteProcessor) ip);
		
		long[][] II1Cvj = iICvj.getS1();
		long[][] II1Cib = iICib.getS1();
		
		
//		FloatProcessor II1fp_vj = new FloatProcessor(toFloatArray(II1Cvj));
//		new ImagePlus("II1 Viola Jones", II1fp_vj).show();
//		
//		FloatProcessor II1fp_ib = new FloatProcessor(toFloatArray(II1Cib));
//		new ImagePlus("II1 ImagingBook", II1fp_ib).show();
	
		// -----------------------
		
		long[][] II2Cvj = iICvj.getS2();
		long[][] II2Cib = iICib.getS2();
		
//		FloatProcessor II2fp_vj = new FloatProcessor(toFloatArray(II2Cvj));
//		new ImagePlus("II2 Viola Jones", II2fp_vj).show();
//		
//		FloatProcessor II2fp_ib = new FloatProcessor(toFloatArray(II2Cib));
//		new ImagePlus("II2 ImagingBook", II2fp_ib).show();
		
		int ua = 0, ub = 0;
		int va = 0, vb = 0;
		
		IJ.log("Blocksum1 - VJ: " + iICvj.getBlockSum1(ua, ub, va, vb));
		IJ.log("Blocksum1 - IB: " + iICib.getBlockSum1(ua, ub, va, vb));
		
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
