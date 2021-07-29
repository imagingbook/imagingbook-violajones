package imagingbook.violajones.DATA;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import imagingbook.lib.util.resource.ResourceLocation;

public class DataTest {

	@Test
	public void testDataImages() {
		ResourceLocation rl = new imagingbook.violajones.DATA.images.Resources();
		assertNotNull(rl.getResource("bishops1.jpg"));
		assertNotNull(rl.getResource("bishops2.jpg"));
		assertNotNull(rl.getResource("lena.jpg"));
		assertNotNull(rl.getResource("nobelaureates2007.jpg"));
	}
	
	@Test
	public void testDataXml() {
		ResourceLocation rl = new imagingbook.violajones.DATA.xml.Resources();
		assertNotNull(rl.getResource("haarcascade_eye.xml"));
		assertNotNull(rl.getResource("haarcascade_frontalface_alt2.xml"));
		assertNotNull(rl.getResource("haarcascade_frontalface_default.xml"));
	}

}
