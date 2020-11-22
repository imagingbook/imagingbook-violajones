package face_plugins_tests;

import java.io.File;
import java.io.InputStream;

import ij.IJ;
import ij.io.LogStream;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.util.ResourceUtils;
import imagingbook.violajones.data.Data;
import imagingbook.violajones.lib.HaarCascadeDescriptor;
import imagingbook.violajones.resources.xml.HaarTrainingSet;


/**
 * This ImageJ plugin lists the available OpenCV cascade descriptor files.
 * Currently only "old style" XML files (of type "opencv-haar-classifier")
 * can be read.
 * 
 * @author WB
 */
public class List_Haar_Cascade implements PlugIn {
	
	static {
		LogStream.redirectSystem();
	}

	public void run(String arg) {
		String xmlRoot = ResourceUtils.getResourcePath(Data.class, "xml").toString();
		//String xmlRoot = ResourceUtils.getResourcePath(HaarTrainingSet.class, "").toString();
		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");
		String xmlPath = od.getPath();
		if (xmlPath == null) return;
		if (!xmlPath.endsWith(".xml")) {
			IJ.error("this is not an XML file!");
			return;
		}

		System.out.println("XML path: " + xmlPath);
		
		File file = new File(xmlPath);
		String name = file.getName();
		System.out.println("XML file: " + name);
				
//		InputStream strm = ResourceUtils.getResourceStream(Data.class, "xml/haarcascade_frontalface_alt2.xml");
//		InputStream strm = Data.class.getResourceAsStream("xml/haarcascade_frontalface_alt2.xml");
//		
		InputStream strm = HaarTrainingSet.class.getResourceAsStream(name);
		if (strm == null) {
			IJ.log("could not open XML stream for " + xmlPath);
			return;
		}

		IJ.log("Reading XML stream ...");
		HaarCascadeDescriptor hc = HaarCascadeDescriptor.fromInputStream(strm);
		hc.print();

		IJ.log("done.");
	}

}
