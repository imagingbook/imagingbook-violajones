/*******************************************************************************
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2016-2023 Wilhelm Burger. All rights reserved.
 * Visit https://imagingbook.com for additional details.
 ******************************************************************************/
package imagingbook.violajones.lib;

import imagingbook.violajones.data.HaarTrainingSet;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public class HaarCascadeDescriptorTest {

	@Test
	public void test1() {
		for (HaarTrainingSet hts : HaarTrainingSet.values()) {
			assertNotNull("could not find resource " + hts, hts.getURL());
			
			InputStream strm = hts.getStream();
			assertNotNull("could not open stream for resource " + hts, strm);
			
			HaarCascadeDescriptor hcd = HaarCascadeDescriptor.fromInputStream(strm);
			assertNotNull("could not create HaarCascadeDescriptor for resource " + hts, hcd);
		}
	}

}
