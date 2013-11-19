package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class TaloolIconMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 
	 * 50x50.
	 */
	@Override
	public IMOperation getOperation() {
		IMOperation op = new IMOperation();
		op.addImage();
		op.resize(50, 50);
		op.addImage();
		return op;
	}

}
