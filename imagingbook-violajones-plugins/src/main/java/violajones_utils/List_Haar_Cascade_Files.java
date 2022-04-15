package violajones_utils;

import ij.IJ;
import ij.plugin.PlugIn;
import imagingbook.violajones.DATA.HaarTrainingSet;

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
