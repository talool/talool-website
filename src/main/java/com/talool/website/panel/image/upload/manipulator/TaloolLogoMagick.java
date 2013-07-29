package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class TaloolLogoMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 250x75.
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
		
		op.addImage();
		
		return op;
	}
	
}
