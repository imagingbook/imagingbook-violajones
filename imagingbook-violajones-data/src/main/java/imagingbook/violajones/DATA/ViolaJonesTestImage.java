package imagingbook.violajones.DATA;

import imagingbook.core.resource.ImageResource;

public enum ViolaJonesTestImage implements ImageResource {
		bishops1_jpg("bishops1.jpg"),
		bishops2_jpg("bishops2.jpg"),
		group2006_jpg("group2006.jpg"),
		lena_jpg("lena.jpg"),
		nobelaureates2007_jpg("nobelaureates2007.jpg");

	private static final String BASEDIR = "images/";
	private final String relPath;
	
	private ViolaJonesTestImage(String name) {
		this.relPath = BASEDIR + name;
	}
	
	@Override
	public String getRelativePath() {
		return relPath;
	}

}
