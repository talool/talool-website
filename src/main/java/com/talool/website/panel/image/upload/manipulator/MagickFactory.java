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
	public IMagick getTaloolIconMagick() {
		return new TaloolIconMagick();
	}
	
	@Override
	public IMagick getTaloolBackgroundMagick() {
		return new TaloolBackgroundMagick();
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
		case DEAL_OFFER_MERCHANT_LOGO: 
			magick = getTaloolIconMagick();
			break;
		case DEAL_OFFER_BACKGROUND_IMAGE: 
			magick = getTaloolBackgroundMagick();
			break;
		default:
			magick = null;
			break;
		}
		return magick;
	}

	
}
