package com.talool.website.panel.image.upload.manipulator;

import com.talool.core.MediaType;

public class MagickFactory implements IMagickFactory {

	@Override
	public IMagick getMerchantLogoMagick() {
		return new MerchantLogoMagick();
	}

	@Override
	public IMagick getMerchantImageMagick() {
		return new MerchantImageMagick();
	}

	@Override
	public IMagick getDealImageMagick() {
		return new DealImageMagick();
	}

	@Override
	public IMagick getDealOfferLogoMagick() {
		return new DealOfferLogoMagick();
	}

	@Override
	public IMagick getMagickForMediaType(MediaType type) {
		IMagick magick;
		switch (type){
		case DEAL_IMAGE: 
			magick = getDealImageMagick();
			break;
		case DEAL_OFFER_LOGO: 
			magick = getDealOfferLogoMagick();
			break;
		case MERCHANT_LOGO: 
			magick = getMerchantLogoMagick();
			break;
		case MERCHANT_IMAGE: 
			magick = getMerchantImageMagick();
			break;
		default:
			magick = null;
			break;
		}
		return magick;
	}

	
}
