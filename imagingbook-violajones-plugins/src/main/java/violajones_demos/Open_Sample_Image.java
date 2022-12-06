/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package violajones_demos;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import imagingbook.violajones.images.SampleFaceImage;

/**
 * ImageJ plugin. 
 * @author WB
 *
 */
public class Open_Sample_Image implements PlugIn {
	
	static SampleFaceImage imgChoice = SampleFaceImage.Nobelaureates2007_jpg;
	
	String imgName = null;

	@Override
	public void run(String arg) {
		if (!runDialog()) return;
		
		ImagePlus im = imgChoice.getImagePlus();
		im.show();
	}

	private boolean runDialog() {
		GenericDialog gd = new GenericDialog(this.getClass().getSimpleName());
		gd.addEnumChoice("Select test image", imgChoice);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		
		imgChoice = gd.getNextEnumChoice(SampleFaceImage.class);
		return true;
	}
}
