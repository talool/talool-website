package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class TaloolImageMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 
	 * 320x160 with a center crop.
	 */
	@Override
	public IMOperation getOperation() {
		IMOperation op = new IMOperation();
		op.addImage();
		op.resize(320);
		op.gravity("center");
		op.crop(320,100,0,0);
		op.addImage();
		return op;
	}

}
