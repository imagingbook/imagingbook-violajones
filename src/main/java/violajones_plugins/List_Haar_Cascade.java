package violajones_plugins;

import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.ij.IjLogStream;
import imagingbook.lib.util.FileUtils;
import imagingbook.violajones.lib.HaarCascadeDescriptor;
import imagingbook.violajones.resources.Data;

import java.io.InputStream;

public class List_Haar_Cascade implements PlugIn {
	
	static {
		IjLogStream.redirectSystem();
	}


	public void run(String arg) {

		String xmlRoot = FileUtils.getResourcePath(Data.class, "xml");
		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");
		String xmlPath = od.getPath();
		if (xmlPath == null) return;
		if (!xmlPath.endsWith(".xml")) {
			IJ.error("this is not an XML file!");
			return;
		}

		System.out.println("XML-file: " + xmlPath);
				
		InputStream strm = FileUtils.getResourceStream(Data.class, "xml/haarcascade_frontalface_alt2.xml");
		if (strm == null) {
			IJ.log("could not open stream");
			return;
		}

		HaarCascadeDescriptor hc = HaarCascadeDescriptor.createFrom(strm);
		hc.print();
		
	}




}
