package imagingbook.violajones.DATA;

import imagingbook.core.resource.ImageResource;

public enum SampleFaceImage implements ImageResource {
		Bishops1("bishops1.jpg"),
		Bishops2("bishops2.jpg"),
		Group2006("group2006.jpg"),
		lena("lena.jpg"),
		Nobelaureates2007("nobelaureates2007.jpg");

	private static final String BASEDIR = "images/";
	private final String relPath;
	
	private SampleFaceImage(String name) {
		this.relPath = BASEDIR + name;
	}
	
	@Override
	public String getRelativePath() {
		return relPath;
	}

}
