package com.talool.website.panel.merchant.wizard;

import java.util.ArrayList;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.MerchantMedia;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.panel.merchant.wizard.MerchantWizard.WizardMarker;
import com.talool.website.util.SessionUtils;
import com.vividsolutions.jts.geom.Point;

public class MerchantMap extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private final MerchantWizard wizard;

	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public MerchantMap(MerchantWizard wiz)
	{
		super(new ResourceModel("title"), new ResourceModel("summary"));
		wizard = wiz;
	}

	@Override
	protected void onConfigure()
	{
		super.onConfigure();

		final Merchant merchant = (Merchant) getDefaultModelObject();

		addOrReplace(new Label("merchantName", merchant.getName()));

		GMap map = new GMap("map");
		map.setStreetViewControlEnabled(false);
		map.setScaleControlEnabled(true);
		map.setScrollWheelZoomEnabled(true);
		map.setZoom(10);
		addOrReplace(map);

		/*
		 * Put the pins on the map and center the map
		 */
		Set<MerchantLocation> locs = merchant.getLocations();
		Point pin;
		boolean centerMap = true;
		for (final MerchantLocation location : locs)
		{
			pin = (Point) location.getGeometry();
			if (pin != null)
			{
				if (centerMap)
				{
					GLatLng center = new GLatLng(pin.getY(), pin.getX());
					map.setCenter(center);
					centerMap = false;
				}
				map.addOverlay(new GMarker(new GMarkerOptions(map, new GLatLng(pin.getY(), pin.getX()))));
			}
		}

		/*
		 * Link to add more locations
		 */
		addOrReplace(new AjaxLink<Void>("addLocation")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{

				// create a new location and add it to the merchant
				MerchantLocation location = domainFactory.newMerchantLocation();
				MerchantMedia merchLogo = merchant.getCurrentLocation().getLogo();
				if (merchLogo != null)
				{
					location.setLogo(merchLogo);
				}
				MerchantMedia merchImage = merchant.getCurrentLocation().getMerchantImage();
				if (merchImage != null)
				{
					location.setMerchantImage(merchImage);
				}

				location.setCreatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());
				merchant.addLocation(location);
				merchant.setCurrentLocation(location);

				wizard.gotoMarker(target, WizardMarker.NewLocation);
			}

		});

		/*
		 * Location List
		 */
		MerchantLocationListModel model = new MerchantLocationListModel();
		model.setMerchantId(merchant.getId());
		model.setObject(new ArrayList<MerchantLocation>(merchant.getLocations()));
		final ListView<MerchantLocation> locations = new ListView<MerchantLocation>(
				"locationRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantLocation> item)
			{
				final MerchantLocation merchLoc = item.getModelObject();

				item.setModel(new CompoundPropertyModel<MerchantLocation>(merchLoc));

				item.add(new Label("locationName"));
				item.add(new Label("websiteUrl"));
				item.add(new Label("phone"));
				item.add(new Label("address1"));
				item.add(new Label("address2"));
				item.add(new Label("city"));
				item.add(new Label("stateProvinceCounty"));
				item.add(new Label("zip"));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						merchant.setCurrentLocation(merchLoc);
						wizard.gotoMarker(target, WizardMarker.NewLocation);
					}
				});
			}

		};
		addOrReplace(locations);

	}

}
