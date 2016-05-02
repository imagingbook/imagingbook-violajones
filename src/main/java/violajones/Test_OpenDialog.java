package violajones;

import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.util.FileUtils;
//import violajones.lib.Rect;
import violajones.resources.Data;

public class Test_OpenDialog implements PlugIn {
	
	public void run(String args) {
		
		/* use option JFileChooser in ImageJ, otherwise default directory is ignored */
		
		String xmlRoot = FileUtils.getResourcePath(Data.class, "xml");
		IJ.log("xmlRoot = " + xmlRoot);
		
		String p = this.getClass().getResource("").getPath();
		IJ.log("p = " + p);
		
//		OpenDialog.setDefaultDirectory("/");
		OpenDialog.setDefaultDirectory(p);
		IJ.log("getDefaultDirectory = " + OpenDialog.getDefaultDirectory());
		
//		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");

	}


}
