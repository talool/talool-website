package com.talool.website.panel.merchant.wizard;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
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
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.website.models.MerchantLocationListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantLocationPanel;
import com.talool.website.util.HttpUtils;
import com.vividsolutions.jts.geom.Point;

public class MerchantMap extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMap.class);
	
	public MerchantMap()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        
    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		Merchant merchant = (Merchant) getDefaultModelObject();
		
		MerchantLocation loc = merchant.getLocations().get(0);
		Address address = loc.getAddress();
		
		try {
			Point point = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
					address.getCity(), address.getStateProvinceCounty());
			
			GLatLng center = new GLatLng(point.getY(), point.getX());
			
			GMap map = new GMap("map");
	        map.setStreetViewControlEnabled(false);
	        map.setScaleControlEnabled(true);
	        map.setScrollWheelZoomEnabled(true);
	        map.setCenter(center);    
	        
	        List<MerchantLocation> locs = merchant.getLocations();
	        Point pin;
	        for (MerchantLocation l:locs)
	        {
	        	address = l.getAddress();
	        	pin = HttpUtils.getGeometry(address.getAddress1(), address.getAddress2(),
						address.getCity(), address.getStateProvinceCounty());
				
				map.addOverlay(new GMarker(new GMarkerOptions(map, new GLatLng(pin.getY(), pin.getX()))));
				
				// TODO does this save the point?
				l.setGeometry(pin);
	        }
	        
	        
	        addOrReplace(map);
	        
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
					MerchantLocation managedLocation = item.getModelObject();
					final Long merchantLocationId = managedLocation.getId();

					item.setModel(new CompoundPropertyModel<MerchantLocation>(managedLocation));

					item.add(new Label("locationName"));
					item.add(new Label("websiteUrl"));
					item.add(new Label("email"));
					item.add(new Label("phone"));
					item.add(new Label("address.address1"));
					item.add(new Label("address.address2"));
					item.add(new Label("address.city"));
					item.add(new Label("address.stateProvinceCounty"));
					item.add(new Label("address.zip"));

					BasePage page = (BasePage) this.getPage();
					final AdminModalWindow modal = page.getModal();
					final SubmitCallBack callback = page.getCallback(modal);
					item.add(new AjaxLink<Void>("editLink")
					{

						private static final long serialVersionUID = 8817599057544892359L;

						@Override
						public void onClick(AjaxRequestTarget target)
						{
							getSession().getFeedbackMessages().clear();
							MerchantLocationPanel panel = new MerchantLocationPanel(modal.getContentId(), callback,
									merchantLocationId);
							modal.setContent(panel);
							modal.setTitle("Edit Merchant Location");
							modal.show(target);
						}
					});
				}

			};
			addOrReplace(locations);
		}
        catch (Exception e)
		{
			LOG.error("There was an exception resolving lat/long: " + e.getLocalizedMessage(), e);
			WebMarkupContainer bug = new WebMarkupContainer("map");
			addOrReplace(bug);
		}
	}
	
}

