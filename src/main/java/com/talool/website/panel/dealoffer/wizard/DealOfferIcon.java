package com.talool.website.panel.dealoffer.wizard;

import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import com.talool.core.DealOffer;
import com.talool.core.MediaType;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.panel.dealoffer.FindDealsPreview;
import com.talool.website.panel.image.selection.MediaSelectionPanel;
import com.vividsolutions.jts.geom.Geometry;

public class DealOfferIcon extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private MerchantMedia icon;
	private MerchantLocation geoCenter;

	public DealOfferIcon()
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final DealOffer offer = (DealOffer) getDefaultModelObject();

		final FindDealsPreview preview = new FindDealsPreview("findDealsBuilder", offer);
		preview.setOutputMarkupId(true);
		addOrReplace(preview);

		// define the Geo for this offer
		MerchantLocationListModel choices = new MerchantLocationListModel();
		choices.setMerchantId(offer.getMerchant().getId());
		ChoiceRenderer<MerchantLocation> choiceRenderer = new ChoiceRenderer<MerchantLocation>("address1", "id")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(MerchantLocation loc) {
				StringBuilder val = new StringBuilder();
				if (loc.getLocationName() == null || loc.getLocationName().isEmpty())
				{
					val.append(loc.getAddress1()).append(", ").append(loc.getNiceCityState());
				}
				else
				{
					val.append(loc.getLocationName());
				}
				return val.toString();
			}
			
		};
		DropDownChoice<MerchantLocation> locations =
				new DropDownChoice<MerchantLocation>("locations", new PropertyModel<MerchantLocation>(this, "geoCenter"), choices, choiceRenderer);
		addOrReplace(locations.setRequired(true));

		icon = offer.getDealOfferIcon();
		PropertyModel<MerchantMedia> iconModel = new PropertyModel<MerchantMedia>(this, "icon");

		MediaSelectionPanel imagePanel = new MediaSelectionPanel("dealOfferIcon", offer.getMerchant().getId(), MediaType.DEAL_OFFER_MERCHANT_LOGO, iconModel)
		{

			private static final long serialVersionUID = 1L;
			
			@Override
			public void onMediaPicked(AjaxRequestTarget target, MerchantMedia media) {
				preview.init(offer);
				target.add(preview);
			}
			
		};
		addOrReplace(imagePanel);

	}

	public MerchantMedia getIcon()
	{
		final DealOffer offer = (DealOffer) getDefaultModelObject();
		return offer.getDealOfferIcon();
	}

	public void setIcon(final MerchantMedia image)
	{
		this.icon = image;
		DealOffer offer = (DealOffer) getDefaultModelObject();
		offer.setDealOfferIcon(image);
	}

	public MerchantLocation getGeoCenter()
	{
		final DealOffer offer = (DealOffer) getDefaultModelObject();
		Geometry geo = offer.getGeometry();
		Set<MerchantLocation> locations = offer.getMerchant().getLocations();
		MerchantLocation loc = null;
		for (MerchantLocation location : locations)
		{
			if (location.getGeometry() != null && location.getGeometry().equalsNorm(geo))
			{
				loc = location;
				break;
			}
		}
		if (loc == null)
		{
			loc = offer.getMerchant().getPrimaryLocation();
		}
		return loc;
	}

	public void setGeoCenter(MerchantLocation geoCenter)
	{
		this.geoCenter = geoCenter;
		DealOffer offer = (DealOffer) getDefaultModelObject();
		offer.setGeometry(geoCenter.getGeometry());
	}

}
