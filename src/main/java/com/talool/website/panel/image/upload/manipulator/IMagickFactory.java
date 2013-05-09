package com.talool.website.panel.image.upload.manipulator;

import com.talool.core.MediaType;

public interface IMagickFactory {

	public IMagick getMerchantLogoMagick();
	public IMagick getMerchantImageMagick();
	public IMagick getDealImageMagick();
	public IMagick getDealOfferLogoMagick();
	public IMagick getMagickForMediaType(MediaType type);
	
}
