/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package imagingbook.violajones.lib;

import java.awt.Rectangle;

/** 
 * Represents a rectangle, along with a weight. 
 */
public class FeaturePatch extends Rectangle {
	private static final long serialVersionUID = 1L;
	
	public final double weight;

	FeaturePatch(int x, int y, int w, int h, double weight) {
		super(x, y, w, h);
		this.weight = weight;
	}

	static FeaturePatch fromString(String text) {
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

	@Override
	public String toString() {
		return String.format("x=%d y=%d w=%d h=%d weight=%.2f", 
				this.x, this.y, this.width, this.height, this.weight);
	}
}
