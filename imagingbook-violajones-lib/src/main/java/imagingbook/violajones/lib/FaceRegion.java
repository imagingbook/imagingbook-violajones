/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package imagingbook.violajones.lib;

import java.awt.Rectangle;

/**
 * Merely a rectangle which can check for overlap with another rectangle.
 *
 * @author WB
 */
public class FaceRegion extends Rectangle {
	private static final long serialVersionUID = 1L;

	FaceRegion(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	// TODO: check!!
	boolean overlaps(Rectangle r2, double sizeFac) {
		final double sizeFac1 = 1 + sizeFac;
		int distance = (int) (this.width * sizeFac);
		if (r2.x <= this.x + distance && r2.x >= this.x - distance
				&& r2.y <= this.y + distance && r2.y >= this.y - distance
				&& r2.width <= (int) (this.width * sizeFac1)
				&& (int) (r2.width * sizeFac1) >= this.width) {
			return true;
		}
		if (this.x >= r2.x && this.x + this.width <= r2.x + r2.width && this.y >= r2.y
				&& this.y + this.height <= r2.y + r2.height) {
			return true;
		}
			
		return false;
	}

}
