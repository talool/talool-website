package com.talool.website.panel.merchant.wizard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.GMap;
import org.wicketstuff.gmap.api.GLatLng;
import org.wicketstuff.gmap.api.GMarker;
import org.wicketstuff.gmap.api.GMarkerOptions;

import com.talool.core.Address;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.util.HttpUtils;
import com.vividsolutions.jts.geom.Point;

public class MerchantMap extends WizardStep
{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMap.class);
	private final MerchantWizard wizard;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
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
		if (merchant.getId() != null)
		{
			try
			{
				taloolService.merge(merchant);
			}
			catch (ServiceException se)
			{
				LOG.error("There was an exception merging the merchant: ", se);
			}
		}

		GMap map = new GMap("map");
		map.setStreetViewControlEnabled(false);
		map.setScaleControlEnabled(true);
		map.setScrollWheelZoomEnabled(true);
		addOrReplace(map);

		/*
		 * Center the map
		 */
		MerchantLocation loc = merchant.getLocations().get(0);
		Address address;
		try
		{
			address = loc.getAddress();
			Point point = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
					address.getCity(), address.getStateProvinceCounty());

			GLatLng center = new GLatLng(point.getY(), point.getX());
			map.setCenter(center);
		}
		catch (Exception e)
		{
			LOG.error("There was an exception resolving lat/long to center the map: " + e.getLocalizedMessage(), e);
		}

		/*
		 * Put the pins on the map
		 */
		List<MerchantLocation> locs = merchant.getLocations();
		Point pin;

		for (MerchantLocation location : locs)
		{
			address = location.getAddress();

			try
			{
				pin = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
						address.getCity(), address.getStateProvinceCounty());

				map.addOverlay(new GMarker(new GMarkerOptions(map, new GLatLng(pin.getY(), pin.getX()))));

				location.setGeometry(pin);
			}
			catch (Exception e)
			{
				LOG.error("There was an exception resolving lat/long to pin the map: " + e.getLocalizedMessage(), e);
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
				location.setAddress(domainFactory.newAddress());
				location.setLogo(domainFactory.newMedia(merchant.getId(), "", MediaType.MERCHANT_LOGO));
				merchant.addLocation(location);
				merchant.setCurrentLocation(location);

				// go back to the previous step
				wizard.goBack(target);
			}

		});

		/*
		 * Location List
		 */
		MerchantLocationListModel model = new MerchantLocationListModel();
		model.setMerchantId(merchant.getId());
		model.setObject(merchant.getLocations());
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
				item.add(new Label("email"));
				item.add(new Label("phone"));
				item.add(new Label("address.address1"));
				item.add(new Label("address.address2"));
				item.add(new Label("address.city"));
				item.add(new Label("address.stateProvinceCounty"));
				item.add(new Label("address.zip"));

				item.add(new AjaxLink<Void>("editLink")
				{

					private static final long serialVersionUID = 8817599057544892359L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						merchant.setCurrentLocation(merchLoc);
						// go back to the previous step
						wizard.goBack(target);
					}
				});
			}

		};
		addOrReplace(locations);

	}

}
