package imagingbook.violajones.DATA;

import imagingbook.core.resource.NamedResource;

public enum XmlResource implements NamedResource {
		haarcascade_eye("haarcascade_eye.xml"),
		haarcascade_frontalface_alt2("haarcascade_frontalface_alt2.xml"),
		haarcascade_frontalface_default("haarcascade_frontalface_default.xml");

	private static final String BASEDIR = "xml/";
	
	private final String relPath;
	
	private XmlResource(String name) {
		this.relPath = BASEDIR + name;
	}
	
	@Override
	public String getRelativePath() {
		return relPath;
	}

}
