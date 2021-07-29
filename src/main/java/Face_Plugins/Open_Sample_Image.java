package Face_Plugins;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import imagingbook.lib.ij.IjUtils;
import imagingbook.lib.util.resource.ResourceLocation;


/**
 * ImageJ plugin. 
 * @author WB
 *
 */
public class Open_Sample_Image implements PlugIn {
	
	static ResourceLocation loc = new imagingbook.violajones.DATA.images.Resources();
	
	String imgName = null;

	@Override
	public void run(String arg) {
		if (!runDialog()) return;
		
		ImagePlus im = loc.getResource(imgName).openAsImage(); // IjUtils.openImage(loc.getPath(imgName));
		im.show();
	}

	private boolean runDialog() {
		GenericDialog gd = new GenericDialog("Set Face Detector Parameters");
		String[] fileNames = loc.getResourceNames();
		gd.addChoice("Sample image", fileNames, fileNames[0]);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return false;
		}
		
		imgName = gd.getNextChoice();
		return true;
	}
}
