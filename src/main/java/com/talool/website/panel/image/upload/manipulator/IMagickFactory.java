package com.talool.website.panel.image.upload.manipulator;

import com.talool.core.MediaType;

public interface IMagickFactory {

	public IMagick getTaloolImageMagick();
	public IMagick getTaloolLogoMagick();
	public IMagick getTaloolIconMagick();
	public IMagick getTaloolBackgroundMagick();
	public IMagick getMagickForMediaType(MediaType type);
	
}
