package imagingbook.violajones.DATA;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import imagingbook.lib.util.ResourceLocation;

public class DataTest {

	@Test
	public void testDataImages() {
		ResourceLocation rl = new imagingbook.violajones.DATA.images.Resources();
		assertNotNull(rl.getResourcePath("bishops1.jpg"));
		assertNotNull(rl.getResourcePath("bishops2.jpg"));
		assertNotNull(rl.getResourcePath("lena.jpg"));
		assertNotNull(rl.getResourcePath("nobelaureates2007.jpg"));
	}
	
	@Test
	public void testDataXml() {
		ResourceLocation rl = new imagingbook.violajones.DATA.xml.Resources();
		assertNotNull(rl.getResourcePath("haarcascade_eye.xml"));
		assertNotNull(rl.getResourcePath("haarcascade_frontalface_alt2.xml"));
		assertNotNull(rl.getResourcePath("haarcascade_frontalface_default.xml"));
	}

}
