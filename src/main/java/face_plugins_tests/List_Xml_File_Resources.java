package face_plugins_tests;

import ij.plugin.PlugIn;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

public class List_Xml_File_Resources implements PlugIn {

	
	public void run(String arg0) {
		
//		String xml1 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_eye.xml").toString();
//		String xml2 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_frontalface_alt2.xml").toString();
//		String xml3 = ResourceUtils.getResourcePath(Data.class, "xml/haarcascade_frontalface_default.xml").toString();
//		
//		IJ.log("xml1 = " + xml1);
//		IJ.log("xml2 = " + xml2);
//		IJ.log("xml3 = " + xml3);
//		
//		IJ.log("Myenum 1 (path): " + HaarTrainingSet.EYE.getPath());
//		IJ.log("Myenum 2 (path): " + HaarTrainingSet.frontalface_alt2.getPath());
//		IJ.log("Myenum 3 (path): " + HaarTrainingSet.frontalface_default.getPath());
//		
//		IJ.log("Myenum 1 (stream): " + HaarTrainingSet.EYE.getStream());
//		IJ.log("Myenum 2 (stream): " + HaarTrainingSet.frontalface_alt2.getStream());
//		IJ.log("Myenum 3 (stream): " + HaarTrainingSet.frontalface_default.getStream());
		
		HaarTrainingSet.listAll();
		
	}
	
	

}
