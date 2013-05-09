package com.talool.website.panel.image.upload.manipulator;

import org.im4java.core.IMOperation;

public interface IMagick {

	public IMOperation getOperation();
	public void process();
}
