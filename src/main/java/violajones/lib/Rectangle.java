package violajones.lib;

/** A simple class describing a rectangle, along with a weight. */
public class Rectangle {

	public final int x;	// TODO: make getters?
	public final int y;
	public final int width;
	public final int height;

	public Rectangle(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	// TODO: check!!
	public boolean overlaps(Rectangle r2, double sizeFac) {
		final double sizeFac1 = 1 + sizeFac;
		int distance = (int) (this.width * sizeFac);
		if (r2.x <= this.x + distance && r2.x >= this.x - distance
				&& r2.y <= this.y + distance && r2.y >= this.y - distance
				&& r2.width <= (int) (this.width * sizeFac1)
				&& (int) (r2.width * sizeFac1) >= this.width)
			return true;
		if (this.x >= r2.x && this.x + this.width <= r2.x + r2.width && this.y >= r2.y
				&& this.y + this.height <= r2.y + r2.height)
			return true;
		return false;
	}

}
