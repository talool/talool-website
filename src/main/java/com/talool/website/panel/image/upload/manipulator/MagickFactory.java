package com.talool.website.panel.image.upload.manipulator;

import com.talool.core.MediaType;

public class MagickFactory implements IMagickFactory {

	@Override
	public IMagick getTaloolImageMagick() {
		return new TaloolImageMagick();
	}

	@Override
	public IMagick getTaloolLogoMagick() {
		return new TaloolLogoMagick();
	}

	@Override
	public IMagick getMagickForMediaType(MediaType type) {
		IMagick magick;
		switch (type){
		case DEAL_IMAGE:
		case MERCHANT_IMAGE:
			magick = getTaloolImageMagick();
			break;
		case MERCHANT_LOGO: 
		case DEAL_OFFER_LOGO: 
			magick = getTaloolLogoMagick();
			break;
		default:
			magick = null;
			break;
		}
		return magick;
	}

	
}
