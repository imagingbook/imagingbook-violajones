package imagingbook.violajones.resources.xml;

import java.io.InputStream;

/**
 * This class gives easy access to the Haar training sets, obtained
 * from OpenCV. The associated XML files are supposed to be in the same 
 * directory as this class.
 * @author WB
 *
 */
public enum HaarTrainingSet {
	EYE("haarcascade_eye.xml"),
	FACE1("haarcascade_frontalface_default.xml"),
	FACE2("haarcascade_frontalface_alt2.xml");
	
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

}
