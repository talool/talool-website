package com.talool.website.panel.image.upload.manipulator;

import org.apache.wicket.util.upload.FileItem;

public abstract class AbstractMagick implements IMagick {
	
	private FileItem image;

	public FileItem getImage() {
		return image;
	}

	public void setImage(FileItem image) {
		this.image = image;
	}

}
