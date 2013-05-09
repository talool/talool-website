package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class MerchantLogoMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 250x75.
	 * then grayscale and colorize the image to match
	 * the teal bg of the coupons.
	 * 
	 * TODO convert the white to trasparent before the clut
	 */
	@Override
	public IMOperation getOperation() {
		IMOperation op = new IMOperation();
		op.addImage();
		op.resize(250,75);
		op.fx("(r+g+b)/3");
		op.addImage(getTealGradientFilePath());
		op.clut();
		op.addImage();
		
		return op;
	}


}
