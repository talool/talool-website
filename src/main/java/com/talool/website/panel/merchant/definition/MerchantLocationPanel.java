package com.talool.website.panel.merchant.definition;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.MediaType;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.service.ErrorCode;
import com.talool.utils.HttpUtils;
import com.talool.website.behaviors.OnChangeAjaxFormBehavior;
import com.talool.website.component.MerchantMediaWizardPanel;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.models.MerchantLocationModel;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantLocationPanel extends BaseDefinitionPanel
{
	private static final long serialVersionUID = 661849211369766802L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationPanel.class);
	private UUID merchantId;
	private MerchantMedia selectedImage;
	private MerchantMedia selectedLogo;

	public MerchantLocationPanel(final String id, final UUID merchantId, final SubmitCallBack callback)
	{
		super(id, callback);
		this.merchantId = merchantId;
		MerchantLocation merchantLocation = domainFactory.newMerchantLocation();

		MerchantMedia logo = domainFactory.newMedia(merchantId, "", MediaType.MERCHANT_LOGO);
		merchantLocation.setLogo(logo);
		setDefaultModel(Model.of(merchantLocation));
	}

	public MerchantLocationPanel(final String id, final SubmitCallBack callback,
			final Long merchantLocationId)
	{
		super(id, callback);
		setDefaultModel(new MerchantLocationModel(merchantLocationId));
	}

	public StateOption getStateOption()
	{
		final MerchantLocation loc = (MerchantLocation) getDefaultModelObject();
		if (loc.getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(loc.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final MerchantLocation loc = (MerchantLocation) getDefaultModelObject();
		loc.setStateProvinceCounty(stateOption.getCode());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		locationPanel.add(new TextField<String>("address1").setRequired(true));

		locationPanel.add(new TextField<String>("address2"));
		locationPanel.add(new TextField<String>("city").setRequired(true));
		locationPanel.add(new TextField<String>("zip"));
		locationPanel.add(new TextField<String>("locationName"));
		locationPanel.add(new TextField<String>("phone").setRequired(true));
		locationPanel.add(new TextField<String>("websiteUrl"));

		final StateSelect state = new StateSelect("stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		state.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(state.setRequired(true));
		
		MerchantLocation loc = (MerchantLocation) getDefaultModelObject();
		selectedImage = loc.getMerchantImage();
		selectedLogo = loc.getLogo();
		
		if (merchantId == null)
		{
			merchantId = loc.getMerchant().getId();
		}
		
		PropertyModel<MerchantMedia> selectedImageModel = new PropertyModel<MerchantMedia>(this,"selectedImage");
		MerchantMediaWizardPanel imagePanel = new MerchantMediaWizardPanel("merchantMediaImage", merchantId, MediaType.MERCHANT_IMAGE, selectedImageModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) {
				// TODO show the uploaded image
			}
			
		};
		locationPanel.add(imagePanel);
		
		PropertyModel<MerchantMedia> selectedLogoModel = new PropertyModel<MerchantMedia>(this,"selectedLogo");
		MerchantMediaWizardPanel logoPanel = new MerchantMediaWizardPanel("merchantMediaLogo", merchantId, MediaType.MERCHANT_LOGO, selectedLogoModel)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media) {
				// TODO show the uploaded logo
			}
			
		};
		locationPanel.add(logoPanel);

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<MerchantLocation> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<MerchantLocation>(
				(IModel<MerchantLocation>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		MerchantLocation merchantLocation = (MerchantLocation) form
				.getDefaultModelObject();
		return merchantLocation.getLocationName();
	}

	@Override
	public void save() throws ServiceException
	{
		MerchantLocation merchantLocation = (MerchantLocation) form
				.getDefaultModelObject();

		if (merchantLocation.getId() == null && merchantId != null)
		{
			// we are creating, not updating
			// TODO Optimize this save so we don't pull in the whole merchant
			merchantLocation.setMerchant(taloolService.getMerchantById(merchantId));
			merchantLocation.setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
		}
		if (merchantLocation.getEmail() == null)
		{
			merchantLocation.setEmail("foo@talool.com");
		}
		merchantLocation.setMerchantImage(selectedImage);
		merchantLocation.setLogo(selectedLogo);
		
		final Geometry oldGeo = merchantLocation.getGeometry();
		Geometry newGeo = null;
		try
		{
			newGeo = HttpUtils.getGeometry(merchantLocation);
			merchantLocation.setGeometry(newGeo);
		}
		catch (ServiceException se)
		{
			error(se.getErrorCode().getMessage());
			LOG.error("There was an exception resolving lat/long of location: " + se.getLocalizedMessage(), se);
		}
		catch (Exception e)
		{
			LOG.error("There was an exception resolving lat/long of location: " + e.getLocalizedMessage(), e);
		}

		if (newGeo != null)
		{
			taloolService.save(merchantLocation);
			
			// update any offers using this geo
			updateOfferGeo(oldGeo, newGeo);
			
			SessionUtils.successMessage("Successfully saved location");
		}
		else
		{
			throw new ServiceException(ErrorCode.MERCHANT_LOCATION_GEOMETRY_NULL);
		}

	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Merchant Location";
	}
	
	private void updateOfferGeo(Geometry oldGeo, Geometry newGeo) throws ServiceException
	{
		if (oldGeo == null || newGeo == null || oldGeo.equals(newGeo)) return;
		
		List<DealOffer> offers = taloolService.getDealOffersByMerchantId(merchantId);
		for (DealOffer offer:offers)
		{
			Geometry geo = offer.getGeometry();
			if (geo != null && geo.equals(oldGeo))
			{
				offer.setGeometry(newGeo);
				taloolService.save(offer);
			}
		}
	}
}
