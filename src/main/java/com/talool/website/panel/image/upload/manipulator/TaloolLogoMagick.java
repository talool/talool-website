package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class TaloolLogoMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 140x44.
	 * then grayscale the image.
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
		WizardApprentice image = new WizardApprentice();
		
		if (image.isTooBig)
		{
			op.resize(maxLogoWidth,maxLogoHeight);
		}
		
		// convert to grayscale
		if (image.isRGB || !image.isJPEG)
		{
			op.fx("(r+g+b)/3");
		}
		else
		{
			/*
			 * we're saving as a png, so convert to RGB
			 * and try to get some alpha
			 */
			op.colorspace("RGB");
			op.type("GrayscaleMatte");
		}
		
		// filter the logo to have a teal tint
		if (image.hasAlpha)
		{
			op.addImage(getGradientFilePath());
			op.clut();
		}
		else
		{
			op.p_levelColors("white","none");
			op.background("gray30");
			op.alpha("shape");
		}
		op.addImage();
		
		return op;
	}
	
}
