package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class MerchantLogoMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 250x75.
	 * then grayscale and colorize the image to match
	 * the teal bg of the coupons.
	 * 
	 * TODO convert the white to trasparent before the clut
	 * 
	 * convert -colorspace sRGB -type GrayscaleMatte OrangeColterraLogo.jpg +level-colors teal,none -background black -alpha shape colterra6.png
	 * 
	 */
	@Override
	public IMOperation getOperation() {
		IMOperation op = new IMOperation();
		op.addImage();
		
		op.fx("(r+g+b)/3");
		op.resize(250,75);
		op.addImage(getTealGradientFilePath());
		op.clut();
		
		//op.colorspace("sRGB");
		//op.type("GrayscaleMatte");
		//op.p_levelColors("teal","none");
		//op.background("teal");
		//op.alpha("shape");
		
		op.addImage();
		
		return op;
	}


}
