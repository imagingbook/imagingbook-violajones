package imagingbook.violajones.lib;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import imagingbook.violajones.DATA.HaarTrainingSet;

public class HaarCascadeDescriptorTest {

	@Test
	public void test1() {
		for (HaarTrainingSet hts : HaarTrainingSet.values()) {
			assertNotNull("could not find resource " + hts, hts.getURL());
			
			InputStream strm = hts.getStream();
			assertNotNull("could not open stream for resource " + hts, hts.getStream());
			
			HaarCascadeDescriptor hcd = HaarCascadeDescriptor.fromInputStream(strm);
			assertNotNull("could not create HaarCascadeDescriptor for resource " + hts, hcd);
		}
	}

}
