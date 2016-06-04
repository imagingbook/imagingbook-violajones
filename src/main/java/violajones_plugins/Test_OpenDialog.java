package violajones_plugins;

import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.util.FileUtils;
import imagingbook.violajones.resources.Data;

public class Test_OpenDialog implements PlugIn {
	
	public void run(String args) {
		
		/* use option JFileChooser in ImageJ, otherwise default directory is ignored */
		
		String xmlRoot = FileUtils.getResourcePath(Data.class, "xml").toString();
		IJ.log("xmlRoot = " + xmlRoot);
		
		String p = this.getClass().getResource("").getPath();
		IJ.log("p = " + p);
		
//		OpenDialog.setDefaultDirectory("/");
		OpenDialog.setDefaultDirectory(p);
		IJ.log("getDefaultDirectory = " + OpenDialog.getDefaultDirectory());
		
//		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");

	}


}
