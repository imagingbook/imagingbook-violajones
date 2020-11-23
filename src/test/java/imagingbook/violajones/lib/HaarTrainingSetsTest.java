package imagingbook.violajones.lib;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class HaarTrainingSetsTest {

	@Test
	public void test() {
		for (HaarTrainingSets set : HaarTrainingSets.values()) {
			assertNotNull("Haar training set not found : " + set.name(), set.getPath());
		}
	}

}
