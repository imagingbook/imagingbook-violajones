
package imagingbook.violajones.lib.integral1;

import ij.process.ByteProcessor;

/**
 * This uses the initialization from the original implementation in class Detector.
 * @deprecated
 */
public class IntegralImage {
	
	private final int M, N;
	private final long[][] S1, S2;
	
	public IntegralImage(int[][] I) {
		M = I.length;		// width
		N = I[0].length;	// height
		S1 = new long[M][N];
		S2 = new long[M][N];
		
		for (int i = 0; i < M; i++) {
			long col = 0;
			long col2 = 0;
			for (int j = 0; j < N; j++) {
				int value = I[i][j];
				S1[i][j] = (i > 0 ? S1[i - 1][j] : 0) + col + value;
				S2[i][j] = (i > 0 ? S2[i - 1][j] : 0) + col2 + value * value;
				col += value;
				col2 += value * value;
			}
		}
//		// initialize top-left corner (0,0)
//		S1[0][0] = I[0][0];
//		S2[0][0] = I[0][0] * I[0][0];
//		// do line v = 0:
//		for (int u = 1; u < M; u++) {
//			S1[u][0] = S1[u-1][0] + I[u][0];
//			S2[u][0] = S2[u-1][0] + I[u][0] * I[u][0];
//		}
//		
//		// do lines v = 1,...,h-1
//		for (int v = 1; v < N; v++) {
//			S1[0][v] = S1[0][v-1] + I[0][v];
//			S2[0][v] = S2[0][v-1] + I[0][v] * I[0][v];
//			for (int u = 1; u < M; u++) {
//				S1[u][v] = S1[u-1][v] + S1[u][v-1] - S1[u-1][v-1] + I[u][v];
//				S2[u][v] = S2[u-1][v] + S2[u][v-1] - S2[u-1][v-1] + I[u][v] * I[u][v];
//			}
//		}
	}
	
	public IntegralImage(ByteProcessor ip) {
		this(ip.getIntArray());
	}
	
	// -------------------------------------------------------
	
	public long[][] getS1() {	//TODO: make non-public
		return S1;
	}
	
	public long[][] getS2() {	//TODO: make non-public
		return S2;
	}
	
	// -------------------------------------------------------
	
	/**
	 * Calculates the sum of the pixel values in the rectangle
	 * R, specified by the corner points (u0, v0) and (u1, v1).
	 * @param ua leftmost position in R
	 * @param va top position in R
	 * @param ub rightmost position in R (u1 >= u0)
	 * @param vb bottom position in R (v1 >= v0)
	 * @return
	 */
	public long getBlockSum1(int ua, int va, int ub, int vb) {
		return S1[ub][vb] + S1[ua][va] - S1[ua][vb] - S1[ub][va];
	}
	
	/**
	 * Calculates the sum of the squared pixel values in the rectangle
	 * R, specified by the corner points (u0, v0) and (u1, v1).
	 * @param u0 leftmost position in R
	 * @param v0 top position in R
	 * @param u1 rightmost position in R (u1 >= u0)
	 * @param v1 bottom position in R (v1 >= v0)
	 * @return
	 */
	public long getBlockSum2(int u0, int v0, int u1, int v1) {
		return S2[u1][v1] + S2[u0][v0] - S2[u0][v1] - S2[u1][v0];
	}
	
	/**
	 * Calculates the mean of the region defined by the u/v
	 * parameters.
	 * @param u0 leftmost position in R
	 * @param v0 top position in R
	 * @param u1 rightmost position in R (u1 >= u0)
	 * @param v1 bottom position in R (v1 >= v0)
	 * @return
	 */
	public double getMean(int u0, int v0, int u1, int v1) {
		int N = (1 + u1 - u0) * (1 + v1 - v0);
		if (N <= 0)
			throw new IllegalArgumentException("region size must be positive");
		double S1 = getBlockSum1(u0, v0, u1, v1);
		return S1 / N;
	}
	
	/**
	 * Calculates the variance of the region defined by the u/v
	 * parameters.
	 * @param u0 leftmost position in R
	 * @param v0 top position in R
	 * @param u1 rightmost position in R (u1 >= u0)
	 * @param v1 bottom position in R (v1 >= v0)
	 * @return
	 */
	public double getVariance(int u0, int v0, int u1, int v1) {
		int N = (1 + u1 - u0) * (1 + v1 - v0);
		if (N <= 0)
			throw new IllegalArgumentException("region size must be positive");
		double S1 = getBlockSum1(u0, v0, u1, v1);
		double S2 = getBlockSum2(u0, v0, u1, v1);
		return (S2 - (S1 * S1) / N) / N;
	}

}
