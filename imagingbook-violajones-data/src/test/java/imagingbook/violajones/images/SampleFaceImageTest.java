package imagingbook.violajones.images;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import imagingbook.core.resource.ImageResource;

public class SampleFaceImageTest {

	@Test
	public void test1() {
		for (ImageResource ir : SampleFaceImage.values()) {
//			System.out.println(ir.getRelativePath());
			assertNotNull("could not find URL for resource " + ir, ir.getURL());
			assertNotNull("could not open stream for resource " + ir, ir.getStream());
			assertNotNull("could not open image for resource " + ir, ir.getImage());
		}
	}

}
