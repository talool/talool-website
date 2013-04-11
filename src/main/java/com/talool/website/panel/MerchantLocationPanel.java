package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantManagedLocation;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.MerchantManagedLocationModel;

/**
 * 
 * @author dmccuen
 * 
 */
public class MerchantLocationPanel extends BasePanel
{

	private static final long serialVersionUID = 661849211369766802L;

	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocationPanel.class);

	private SubmitCallBack callback;

	private boolean isNew = false;

	public MerchantLocationPanel(final String id, final Long merchantId, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;
		
		Merchant merchant = null;
		try {
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(merchantId);
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}

		MerchantManagedLocation managedLocation = domainFactory.newMerchantManagedLocation(merchant);
		managedLocation.getMerchantLocation().setAddress(domainFactory.newAddress());
		managedLocation.getMerchantLocation().setLogoUrl("");
		isNew = true;
		setDefaultModel(Model.of(managedLocation));
	}

	public MerchantLocationPanel(final String id, final SubmitCallBack callback, final Long merchantManagedLocationId)
	{
		super(id);
		this.callback = callback;
		setDefaultModel(new MerchantManagedLocationModel(merchantManagedLocationId));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		add(feedback.setOutputMarkupId(true));
		
		Form<Void> form = new Form<Void>("form");
		add(form);

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		CompoundPropertyModel<MerchantManagedLocation> merchLocModel = new CompoundPropertyModel<MerchantManagedLocation>(
				(IModel<MerchantManagedLocation>) getDefaultModel());
		form.setDefaultModel(merchLocModel);

		form.add(new AjaxButton("submitButton", form)
		{
			private static final long serialVersionUID = 1756620552045829235L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				// attempting to scroll to top
				target.appendJavaScript("$('.content').scrollTop();");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				StringBuilder sb = new StringBuilder();

				try
				{

					MerchantManagedLocation managedLocation = (MerchantManagedLocation) form.getDefaultModelObject();
					
					taloolService.save(managedLocation);
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
							.append(managedLocation.getMerchantLocation().getLocationName()).append("'");
					getSession().info(sb.toString());
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					sb.append("Problem saving location: ").append(e.getLocalizedMessage());
					getSession().error(sb.toString());
					LOG.error(sb.toString());
					callback.submitFailure(target);
				}
			}

		});

		locationPanel.add(new TextField<String>("merchantLocation.address.address1").setRequired(true));

		locationPanel.add(new TextField<String>("merchantLocation.address.address2"));
		locationPanel.add(new TextField<String>("merchantLocation.address.city").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.stateProvinceCounty")
				.setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.zip").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.address.country").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.locationName"));
		locationPanel.add(new TextField<String>("merchantLocation.phone").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.email").setRequired(true));
		locationPanel.add(new TextField<String>("merchantLocation.websiteUrl"));

	}
}
