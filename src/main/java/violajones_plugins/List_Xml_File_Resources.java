package violajones_plugins;

import ij.IJ;
import ij.plugin.PlugIn;
import imagingbook.lib.util.ResourceUtils;
import imagingbook.violajones.resources.Data;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

public class List_Xml_File_Resources implements PlugIn {

	
	public void run(String arg0) {
		
		String xml1 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_eye.xml").toString();
		String xml2 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_frontalface_alt2.xml").toString();
		String xml3 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_frontalface_default.xml").toString();
		
		IJ.log("xml1 = " + xml1);
		IJ.log("xml2 = " + xml2);
		IJ.log("xml3 = " + xml3);
		
		IJ.log("Myenum 1 (path): " + HaarTrainingSet.EYE.getPath());
		IJ.log("Myenum 2 (path): " + HaarTrainingSet.FACE2.getPath());
		IJ.log("Myenum 3 (path): " + HaarTrainingSet.FACE1.getPath());
		
		IJ.log("Myenum 1 (stream): " + HaarTrainingSet.EYE.getStream());
		IJ.log("Myenum 2 (stream): " + HaarTrainingSet.FACE2.getStream());
		IJ.log("Myenum 3 (stream): " + HaarTrainingSet.FACE1.getStream());
		
	}

}
