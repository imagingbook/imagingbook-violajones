package Face_Plugins_Utils;

import ij.io.LogStream;
import ij.plugin.PlugIn;
import imagingbook.lib.settings.PrintPrecision;
import imagingbook.violajones.lib.HaarTrainingSets;

/**
 * ImageJ plugin. 
 * @author WB
 *
 */
public class List_Haar_Cascade_Files implements PlugIn {
	
	static {
		LogStream.redirectSystem();
		PrintPrecision.set(6);
	}

	public void run(String arg0) {
		HaarTrainingSets.printAll();
	}

}
