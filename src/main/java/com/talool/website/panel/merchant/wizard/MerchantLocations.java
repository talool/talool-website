package com.talool.website.panel.merchant.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.gmap.GMap;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Image;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.domain.ImageImpl;
import com.talool.website.behaviors.OnChangeAjaxFormBehavior;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.pages.UploadPage;

public class MerchantLocations extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocations.class);
	private Image logo;
	private List<String> countries = new ArrayList<String>();
	
	public MerchantLocations()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        // TODO add a country list
        countries.add("United States");
    }
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setDefaultModel(new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel()));
		
		// TODO add logo image
		Merchant merchant = (Merchant) getDefaultModelObject();
		StringBuilder sb = new StringBuilder("number of locations:");
		LOG.debug(sb.append(merchant.getLocations().size()).toString());
		final Label logoUrl = new Label("logoUrl", merchant.getPrimaryLocation().getLogoUrl());
		addOrReplace(logoUrl.setOutputMarkupId(true));
		
		/*
		 * Add an iframe that keep the upload in a sandbox
		 */
		final InlineFrame iframe = new InlineFrame("uploaderIFrame", UploadPage.class);
		addOrReplace(iframe);
		
		/*
		 *  Enable messages to be posted from that sandbox
		 */
		iframe.add(new AbstractDefaultAjaxBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
				String url = params.getParameterValue("url").toString();
				String name = params.getParameterValue("name").toString();
				
				// Create a new image with the returned url and set the Logo
				logo = new ImageImpl(name,url);
				Merchant merchant = (Merchant) getDefaultModelObject();
				merchant.getPrimaryLocation().setLogoUrl(url);
				
				target.add(logoUrl);
			}
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				
				PackageTextTemplate ptt = new PackageTextTemplate( MerchantLocations.class, "MerchantLocations.js" );

				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "callbackUrl", getCallbackUrl().toString() );
				
				response.render(JavaScriptHeaderItem.forScript(ptt.asString(map), "logoupload"));
			}
			
		});
		
		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		addOrReplace(locationPanel);

		/*
		 * OnChangeAjaxFormBehavior addded forAddress so we can (sort of hack)
		 * ensure the address fields are updated in the event we "resolve location"
		 * on our nested form. Could also use it to validate City/Address spellings
		 * later
		 */
		final TextField<String> addr1 = new TextField<String>("primaryLocation.address.address1");
		addr1.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(addr1.setRequired(true));

		final TextField<String> addr2 = new TextField<String>("primaryLocation.address.address2");
		addr2.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(addr2);

		final TextField<String> city = new TextField<String>("primaryLocation.address.city");
		city.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(city.setRequired(true));

		final StateSelect state = new StateSelect("primaryLocation.address.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		state.add(new OnChangeAjaxFormBehavior());
		locationPanel.add(state.setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.zip").setRequired(true));

		locationPanel.add(new DropDownChoice<String>("primaryLocation.address.country",countries).setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.locationName"));

		
		WebMarkupContainer contactPanel = new WebMarkupContainer("contactPanel");
		addOrReplace(contactPanel);
		
		contactPanel.add(new TextField<String>("primaryLocation.phone").setRequired(true));

		contactPanel.add(new TextField<String>("primaryLocation.email").setRequired(true).add(
				EmailAddressValidator.getInstance()));

		contactPanel.add(new TextField<String>("primaryLocation.websiteUrl").add(new UrlValidator()));

	}
	
	public Image getLogo() {
		if (logo==null)
		{
			logo= new ImageImpl("temp image","404.png");
		}
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}
	
	public StateOption getStateOption()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getPrimaryLocation().getAddress().getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(merch.getPrimaryLocation().getAddress()
				.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		merch.getPrimaryLocation().getAddress().setStateProvinceCounty(stateOption.getCode());
	}

	@Override
	public void applyState() {
		// TODO Auto-generated method stub
		super.applyState();
		
		Merchant merchant = (Merchant) getDefaultModelObject();
		StringBuilder sb = new StringBuilder("POST Save: number of locations:");
		LOG.debug(sb.append(merchant.getLocations().size()).toString());
	}
}

