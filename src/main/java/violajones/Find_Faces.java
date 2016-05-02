package violajones;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

import java.awt.Color;
import java.util.List;

import violajones.lib.Detector;
import violajones.lib.Detector.Parameters;
import violajones.lib.FaceRegion;
import violajones.resources.xml.HaarTrainingSet;


public class Find_Faces implements PlugInFilter {
	
	static boolean extractFaceImages = false;
	
	ImagePlus im = null;

	public int setup(String arg0, ImagePlus im) {
		this.im = im;
		return DOES_ALL + NO_CHANGES;
	}
	
	public void run(ImageProcessor ip) {
		
		HaarTrainingSet trainingSet = HaarTrainingSet.FACE2;
	
		Parameters params = new Parameters();
		params.baseScale = 1.5;
		params.scaleStep = 1.05;
		
		Detector detector = Detector.create(trainingSet.getStream(), params);
		
		List<FaceRegion> res = detector.getFaces(ip.convertToByteProcessor());
		IJ.log(res.size() + " faces found!");
		
		// show results
		ImageProcessor cp = ip.convertToColorProcessor();
		for (FaceRegion r : res) {
			draw(r, cp);
		}
		new ImagePlus("faces", cp).show();
		
		
		if (extractFaceImages && !res.isEmpty()) {
//			int w = res.get(0).width;
//			int h =  res.get(0).height;
			//ImageStack faceStack = new ImageStack(w, h);
			int faceCtr = 0;
			for (FaceRegion r : res) {
				ip.setRoi(r.x, r.y, r.width, r.height);
				new ImagePlus("Face " + faceCtr, ip.crop()).show();
				//faceStack.addSlice(ip.crop());
				faceCtr++;
			}
			//new ImagePlus("FaceStack", faceStack).show();
		}

		
	}


	private void draw(FaceRegion f, ImageProcessor cp) {
		cp.setColor(Color.green);
		cp.drawRect(f.x, f.y, f.width, f.height);
	}

}
