package violajones_demos;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import imagingbook.violajones.DATA.SampleFaceImage;

/**
 * ImageJ plugin. 
 * @author WB
 *
 */
public class Open_Sample_Image implements PlugIn {
	
	static SampleFaceImage imgChoice = SampleFaceImage.Nobelaureates2007;
	
	String imgName = null;

	@Override
	public void run(String arg) {
		if (!runDialog()) return;
		
		ImagePlus im = imgChoice.getImage();
		im.show();
	}

	private boolean runDialog() {
		GenericDialog gd = new GenericDialog(this.getClass().getSimpleName());
		gd.addEnumChoice("Select test image", imgChoice);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		
		imgChoice = gd.getNextEnumChoice(SampleFaceImage.class);
		return true;
	}
}
