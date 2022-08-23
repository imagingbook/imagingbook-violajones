package imagingbook.violajones.DATA;

import imagingbook.core.resource.ImageResource;

public enum SampleFaceImage implements ImageResource {
		Bishops1("bishops1.jpg"),
		Bishops2("bishops2.jpg"),
		Group2006("group2006.jpg"),
		lena("lena.jpg"),
		Nobelaureates2007("nobelaureates2007.jpg");

	private static final String BASEDIR = "images";
	private final String filename;
	
	private SampleFaceImage(String filename) {
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
	
}
