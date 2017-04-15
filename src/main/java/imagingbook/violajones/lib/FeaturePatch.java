package imagingbook.violajones.lib;

import java.awt.Rectangle;

/** 
 * Represents a rectangle, along with a weight. 
 */
public class FeaturePatch extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public final double weight;

	public FeaturePatch(int x, int y, int w, int h, double weight) {
		super(x, y, w, h);
		this.weight = weight;
	}

	public static FeaturePatch fromString(String text) {
		String[] tab = text.split(" ");
		FeaturePatch fp = null;
		try {
			int x = java.lang.Integer.parseInt(tab[0]);
			int y = java.lang.Integer.parseInt(tab[1]);
			int w = java.lang.Integer.parseInt(tab[2]);
			int h = java.lang.Integer.parseInt(tab[3]);
			double weight = java.lang.Double.parseDouble(tab[4]);
			fp = new FeaturePatch(x, y, w, h, weight);
		} catch (NumberFormatException e) {
			System.out.println(FeaturePatch.class.getSimpleName() + ": NumberFormatException");
		}
		return fp;
	}
	
	public void print(int patchCnt) {
		System.out.format("           patch %d: x=%d y=%d w=%d h=%d weight=%.2f\n", 
				patchCnt, this.x, this.y, this.width, this.height, this.weight);
	}
}
