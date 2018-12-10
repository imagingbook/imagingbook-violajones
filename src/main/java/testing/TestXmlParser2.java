package testing;

import java.io.File;
import java.io.InputStream;

import ij.io.OpenDialog;
import imagingbook.lib.util.ResourceUtils;
import imagingbook.violajones.lib.HaarCascadeDescriptor2;
import imagingbook.violajones.resources.Data;
import imagingbook.violajones.resources.xml.HaarTrainingSet;

public class TestXmlParser2 {

	public static void main(String[] args) {
		String xmlRoot = ResourceUtils.getResourcePath(Data.class, "xml").toString();
		//String xmlRoot = ResourceUtils.getResourcePath(HaarTrainingSet.class, "").toString();
		System.out.println("XML root: " + xmlRoot);
		
		
//		OpenDialog od = new OpenDialog("Select XML file", xmlRoot, "");
//		String xmlPath = od.getPath();
		
		String xmlPath = xmlRoot + "\\haarcascade_frontalface_alt2.xml";
		if (xmlPath == null) return;
		if (!xmlPath.endsWith(".xml")) {
			System.out.println("this is not an XML file!");
			return;
		}

		System.out.println("XML path: " + xmlPath);
		
		//if(true) return;
		
		File file = new File(xmlPath);
		String name = file.getName();
		System.out.println("XML file: " + name);
				
//		InputStream strm = ResourceUtils.getResourceStream(Data.class, "xml/haarcascade_frontalface_alt2.xml");
//		InputStream strm = Data.class.getResourceAsStream("xml/haarcascade_frontalface_alt2.xml");
//		
		InputStream strm = HaarTrainingSet.class.getResourceAsStream(name);
		if (strm == null) {
			System.out.println("could not open XML stream for " + xmlPath);
			return;
		}

		System.out.println("Reading XML stream ...");
		HaarCascadeDescriptor2 hc = HaarCascadeDescriptor2.createFrom(strm);
		hc.print();

		System.out.println("done.");
	}

}
