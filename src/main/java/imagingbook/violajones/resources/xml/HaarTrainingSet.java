package imagingbook.violajones.resources.xml;

import java.io.InputStream;

import ij.IJ;
import imagingbook.lib.util.Enums;

/**
 * This class gives easy access to the Haar training sets, obtained
 * from OpenCV. The associated XML files are supposed to be in the SAME 
 * directory as this class.
 * @author WB
 *
 */
public enum HaarTrainingSet {
	eye("haarcascade_eye.xml"),
	frontalface_default("haarcascade_frontalface_default.xml"),
	frontalface_alt2("haarcascade_frontalface_alt2.xml")
	;
	
	private final String filename;
	
	private HaarTrainingSet(String filename) {
		this.filename = filename;
	}
	
	public String getPath() {
		return this.getClass().getResource(filename).getPath();
	}
	
	public InputStream getStream() {
		return this.getClass().getResourceAsStream(filename);
	}
	
	//--------------------------------------------
	
	public static void listAll() {
		String[] names = Enums.getEnumNames(HaarTrainingSet.class);
		
		for (String name : names) {
			IJ.log("Haar set name (enum) = " + name);
			HaarTrainingSet set = HaarTrainingSet.valueOf(name);
			IJ.log("   filename = " + set.filename);
			IJ.log("   full path = " + set.getPath());
		}
		
	}

}
