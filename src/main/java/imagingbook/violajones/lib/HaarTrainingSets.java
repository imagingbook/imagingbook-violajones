package imagingbook.violajones.lib;

import java.io.InputStream;
import java.nio.file.Path;

import imagingbook.lib.util.resource.ResourceLocation;

/**
 * This class gives easy access to the Haar training sets, obtained
 * from OpenCV. The associated XML files are supposed to be in the SAME 
 * directory as this class.
 * @author WB
 *
 */
public enum HaarTrainingSets {
	
	eye("haarcascade_eye.xml"),
	frontalface_default("haarcascade_frontalface_default.xml"),
	frontalface_alt2("haarcascade_frontalface_alt2.xml")
	;
	
	ResourceLocation loc = new imagingbook.violajones.DATA.xml.Resources();
	private final String xmlFileName;
	
	private HaarTrainingSets(String filename) {
		this.xmlFileName = filename;
	}
	
	public String getXmlFileName() {
		return xmlFileName;
	}
	
	public Path getPath() {
		//return this.getClass().getResource(filename).getPath();
		return loc.getResource(xmlFileName).getPath(); //loc.getPath(xmlFileName);
	}
	
	public InputStream getStream() {
		//return this.getClass().getResourceAsStream(filename);
		return loc.getResource(xmlFileName).getStream(); // loc.getResourceAsStream(xmlFileName);
	}
	
	
	//--------------------------------------------
	
	public static void printAll() {
		HaarTrainingSets[] allSets = HaarTrainingSets.values(); //Enums.getEnumNames(HaarTrainingSet.class);
		for (HaarTrainingSets s : allSets) {
			System.out.println("Haar set type (enum) = " + s.name());
			System.out.println("   xmlFileName = " + s.getXmlFileName());
			System.out.println("   full path   = " + s.getPath());
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		printAll();
	}

}
