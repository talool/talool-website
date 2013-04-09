package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPanel extends BasePanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);
	private static final long serialVersionUID = -8074065320919062316L;

	private Merchant merchant;

	private String tags;

	private ModalWindow window;

	private SubmitCallBack callback;

	public MerchantPanel(final String id, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;
		merchant = domainFactory.newMerchant();
		merchant.setPrimaryLocation(domainFactory.newMerchantLocation());
		merchant.getPrimaryLocation().setAddress(domainFactory.newAddress());
		merchant.getPrimaryLocation().setLogoUrl("");
	}

	public MerchantPanel(final String id, final SubmitCallBack callback, final Merchant merchant)
	{
		super(id);
		this.callback = callback;
		this.merchant = merchant;
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");

		add(feedback.setOutputMarkupId(true));

		Form<Void> form = new Form<Void>("form");

		add(form);

		form.add(new TextField<String>("name", new PropertyModel<String>(merchant, "name"))
				.setRequired(true));
		form.add(new TextField<String>("tags", new PropertyModel<String>(this, "tags")));

		form.add(new AjaxButton("submitButton", form)
		{
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				// attempting to scroll to top
				target.appendJavaScript("$('.content').scrollTop();");
			}

			private static final long serialVersionUID = 6708205247945526171L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				try
				{
					taloolService.save(merchant);
					target.add(feedback);
					getSession().info("Successfully created '" + merchant.getName() + "'");
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					String errMsg = "Problem saving merchant: " + e.getLocalizedMessage();
					getSession().error(errMsg);
					LOG.error(errMsg);
					callback.submitFailure(target);
				}
			}

		});

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
