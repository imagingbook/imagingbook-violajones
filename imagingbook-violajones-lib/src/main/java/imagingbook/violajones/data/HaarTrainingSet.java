/*
 *  This software is provided as a supplement to the authors' textbooks on digital
 * image processing published by Springer-Verlag in various languages and editions.
 * Permission to use and distribute this software is granted under the BSD 2-Clause
 * "Simplified" License (see http://opensource.org/licenses/BSD-2-Clause).
 * Copyright (c) 2006-2022 Wilhelm Burger, Mark J. Burge.
 * All rights reserved. Visit https://imagingbook.com for additional details.
 */
package imagingbook.violajones.data;

import imagingbook.core.resource.NamedResource;

/**
 * This class gives easy access to the Haar training sets, obtained from OpenCV. The associated XML files are located in
 * relative resource directory "./xml".
 *
 * @author WB
 */
public enum HaarTrainingSet implements NamedResource {
	HaarCascadeEye("haarcascade_eye.xml"),
	FrontalFaceDefault("haarcascade_frontalface_default.xml"),
	FrontalFaceAlt2("haarcascade_frontalface_alt2.xml");
	
	private static final String BASEDIR = "xml";
	private final String filename;
	
	private HaarTrainingSet(String filename) {
		this.filename = filename;
	}
	
	@Override
	public String getFileName() {
		return filename;
	}
	
	@Override
	public String getRelativeDirectory() {
		return BASEDIR;
	}
	
	//--------------------------------------------

	public static void main(String[] args) {
		for (NamedResource nr : HaarTrainingSet.values()) {
			System.out.println("resource URL = " + nr.getURL());
		}
	}



}
