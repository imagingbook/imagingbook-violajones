package imagingbook.violajones.DATA;

import imagingbook.core.resource.NamedResource;

/**
 * This class gives easy access to the Haar training sets, obtained
 * from OpenCV. The associated XML files are located in relative 
 * resource directory "./xml".
 * 
 * @author WB
 *
 */
public enum HaarTrainingSet implements NamedResource {
	HaarCascadeEye("haarcascade_eye.xml"),
	FrontalFaceDefault("haarcascade_frontalface_default.xml"),
	FrontalFaceAlt2("haarcascade_frontalface_alt2.xml");
	
	private static final String BASEDIR = "xml";
	private final String filename;
	
	private HaarTrainingSet(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String getFileName() {
		return filename;
	}
	
	@Override
	public String getRelativeDirectory() {
		return BASEDIR;
	}
	
	//--------------------------------------------

	public static void main(String[] args) {
		for (NamedResource nr : HaarTrainingSet.values()) {
			System.out.println("resource URL = " + nr.getURL());
		}
	}



}
