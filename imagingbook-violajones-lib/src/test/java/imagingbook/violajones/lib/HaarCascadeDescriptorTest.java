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
