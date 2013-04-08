package com.talool.website.pages.define;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPage extends BasePage
{
	private static final long serialVersionUID = -7718256037209979704L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantPage.class);

	private Merchant merchant = domainFactory.newMerchant();

	private String tags;

	public MerchantPage()
	{
		super();
	}

	public MerchantPage(PageParameters parameters)
	{
		super(parameters);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		merchant.setPrimaryLocation(domainFactory.newMerchantLocation());
		merchant.getPrimaryLocation().setAddress(domainFactory.newAddress());
		merchant.getPrimaryLocation().setLogoUrl("");

		add(new FeedbackPanel("feedback"));

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));

		Form<Void> form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 2388954745106388617L;

			@Override
			protected void onSubmit()
			{
				super.onSubmit();

				try
				{
					taloolService.save(merchant);
					getSession().error("Successfully created '" + merchant.getName() + "'");
				}
				catch (ServiceException e)
				{
					String errMsg = "Problem saving merchant: " + e.getLocalizedMessage();
					getSession().error(errMsg);
					LOG.error("Problem saving merchant: " + errMsg);
				}
			}

		};

		add(form);

		form.add(new TextField<String>("name", new PropertyModel<String>(merchant, "name"))
				.setRequired(true));
		form.add(new TextField<String>("tags", new PropertyModel<String>(this, "tags")));

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		CompoundPropertyModel<Merchant> merchLocModel = new CompoundPropertyModel<Merchant>(merchant);

		locationPanel.setDefaultModel(merchLocModel);

		locationPanel.add(new TextField<String>("primaryLocation.address.address1").setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.address2"));
		locationPanel.add(new TextField<String>("primaryLocation.address.city").setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.address.stateProvinceCounty")
				.setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.address.zip").setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.address.country").setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.locationName"));
		locationPanel.add(new TextField<String>("primaryLocation.phone").setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.email").setRequired(true));
		locationPanel.add(new TextField<String>("primaryLocation.websiteUrl"));

	}
}
