package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class MerchantLogoMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 250x75.
	 * then grayscale and colorize the image to match
	 * the teal bg of the coupons.
	 * 
	 * We may get some JPEGs that are in the
	 * CMYK colorspace.  These will need to be 
	 * processed differently.
	 * 
	 */
	@Override
	public IMOperation getOperation() {
		
		IMOperation op = new IMOperation();
		op.addImage();
		op.resize(250,75);
		
		if (isRGB())
		{
			op.fx("(r+g+b)/3");
			op.addImage(getTealGradientFilePath());
			op.clut();
		}
		else
		{
			/*
			 * we're saving as a png, so convert to RGB
			 * and try to get some alpha
			 */
			op.colorspace("sRGB");
			op.type("GrayscaleMatte");
			op.p_levelColors("teal","none");
			op.background("teal");
			op.alpha("shape");
		}
		
		op.addImage();
		
		return op;
	}

}
