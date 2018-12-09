package imagingbook.violajones.lib.util;

import java.awt.Color;

import ij.ImagePlus;
import imagingbook.lib.color.RandomColorGenerator;

/**
 * Helper class for visualizing the detection process.
 * @author WB
 *
 */
public class Visualizer {
	
	private static RandomColorGenerator rcg = new RandomColorGenerator();
	
	public static ImagePlus im = null;
	public static boolean on = false;
	
	private static Color drawColor = Color.black;
	private static Color fillColor = Color.white;
	
	public static void setDrawColor(Color col) {
		drawColor = col;
		if (im != null) {
			im.getProcessor().setColor(col);
		}
	}
	
	public static void clear() {
		if (im != null) {
			im.getProcessor().setColor(fillColor);
			im.getProcessor().fill();
			im.getProcessor().setColor(drawColor);
		}
	}
	
	public static void refresh() {
		if (im != null) {
			im.updateAndDraw();
		}
	}
		
	public static Color nextColor() {
		return rcg.nextColor();
	}
	
	private static int enableCtr =-1;
	
	public static void enable() {
		enableCtr = 0;
	}
	
	public static void disable() {
		enableCtr = 1;
	}
	
	public static boolean isEnabled() {
		return im != null && enableCtr == 0;
	}
	
	public static void suspend() {
		enableCtr = enableCtr + 1;
	}
	
	public static void unsuspend() {
		enableCtr = Math.max(0, enableCtr - 1);
	}
	
	public static int getEnableCtr() {
		return enableCtr;
	}

}
