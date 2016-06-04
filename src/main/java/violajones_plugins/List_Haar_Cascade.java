package violajones_plugins;

import java.io.InputStream;

import ij.IJ;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.ij.IjLogStream;
import imagingbook.lib.util.ResourceUtils;
import imagingbook.violajones.lib.HaarCascadeDescriptor;
import imagingbook.violajones.resources.Data;

public class List_Haar_Cascade implements PlugIn {
	
	static {
		IjLogStream.redirectSystem();
	}


	public void run(String arg) {
		String xmlRoot = ResourceUtils.getResourcePath(Data.class, "xml").toString();
		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");
		String xmlPath = od.getPath();
		if (xmlPath == null) return;
		if (!xmlPath.endsWith(".xml")) {
			IJ.error("this is not an XML file!");
			return;
		}

		System.out.println("XML-file: " + xmlPath);
				
//		InputStream strm = ResourceUtils.getResourceStream(Data.class, "xml/haarcascade_frontalface_alt2.xml");
		InputStream strm = Data.class.getResourceAsStream("xml/haarcascade_frontalface_alt2.xml");
		if (strm == null) {
			IJ.log("could not open stream");
			return;
		}

		HaarCascadeDescriptor hc = HaarCascadeDescriptor.createFrom(strm);
		hc.print();
		
	}




}
