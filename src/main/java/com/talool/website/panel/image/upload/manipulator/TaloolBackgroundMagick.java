package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;


public class TaloolBackgroundMagick extends AbstractMagick {

	/*
	 * This will resize the image down to 
	 * 304x192.
	 */
	@Override
	public IMOperation getOperation() {
		IMOperation op = new IMOperation();
		op.addImage();
		op.resize(304, 192);
		op.addImage();
		return op;
	}

}
