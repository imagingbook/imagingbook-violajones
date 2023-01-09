/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package violajones_demos;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import imagingbook.core.plugin.JavaDocHelp;
import imagingbook.violajones.images.SampleFaceImage;

/**
 * ImageJ plugin.
 *
 * @author WB
 */
public class Open_Sample_Image implements PlugIn, JavaDocHelp {
	
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
		gd.addHelp(getJavaDocUrl());
		gd.addEnumChoice("Select test image", imgChoice);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		
		imgChoice = gd.getNextEnumChoice(SampleFaceImage.class);
		return true;
	}
}
