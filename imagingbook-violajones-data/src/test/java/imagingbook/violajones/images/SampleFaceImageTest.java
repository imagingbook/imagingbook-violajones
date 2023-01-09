/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package imagingbook.violajones.images;

import imagingbook.core.resource.ImageResource;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SampleFaceImageTest {

	@Test
	public void test1() {
		for (ImageResource ir : SampleFaceImage.values()) {
			// System.out.println(ir.getRelativePath());
			assertNotNull("could not find URL for resource " + ir, ir.getURL());
			assertNotNull("could not open stream for resource " + ir, ir.getStream());
			assertNotNull("could not open image for resource " + ir, ir.getImagePlus());
		}
	}

}
