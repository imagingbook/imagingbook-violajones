/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package violajones_utils;

import ij.IJ;
import ij.plugin.PlugIn;
import imagingbook.violajones.images.HaarTrainingSet;

/**
 * ImageJ plugin. 
 * @author WB
 *
 */
public class List_Haar_Cascade_Files implements PlugIn {

	public void run(String arg0) {
		for (HaarTrainingSet s : HaarTrainingSet.values()) {
			IJ.log("Haar set type (enum) = " + s.toString());
			IJ.log("   full path   = " + s.getURL());
			IJ.log("");
		}
	}

}
