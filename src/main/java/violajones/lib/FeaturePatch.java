package violajones.lib;


/** 
 * Represents a rectangle, along with a weight. 
 */
public class FeaturePatch extends Rectangle {

	public final double weight;

	public FeaturePatch(int x, int y, int w, int h, double weight) {
		super(x, y, w, h);
		this.weight = weight;
	}

	public static FeaturePatch fromString(String text) {
		String[] tab = text.split(" ");
		FeaturePatch fp = null;
		try {
			int x = Integer.parseInt(tab[0]);
			int y = Integer.parseInt(tab[1]);
			int w = Integer.parseInt(tab[2]);
			int h = Integer.parseInt(tab[3]);
			double weight = Double.parseDouble(tab[4]);
			fp = new FeaturePatch(x, y, w, h, weight);
		} catch (NumberFormatException e) {}
		return fp;
	}
}
