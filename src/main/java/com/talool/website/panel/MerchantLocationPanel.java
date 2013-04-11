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
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.website.models.MerchantLocationModel;

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

	public MerchantLocationPanel(final String id, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;

		MerchantLocation location = domainFactory.newMerchantLocation();
		location.setAddress(domainFactory.newAddress());
		location.setLogoUrl("");
		isNew = true;
		setDefaultModel(Model.of(location));
	}

	public MerchantLocationPanel(final String id, final SubmitCallBack callback, final Long merchantLocationId)
	{
		super(id);
		this.callback = callback;
		setDefaultModel(new MerchantLocationModel(merchantLocationId));
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

		CompoundPropertyModel<MerchantLocation> merchLocModel = new CompoundPropertyModel<MerchantLocation>(
				(IModel<MerchantLocation>) getDefaultModel());
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

					MerchantLocation location = (MerchantLocation) form.getDefaultModelObject();
					
					taloolService.save(location);
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
							.append(location.getLocationName()).append("'");
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

		locationPanel.add(new TextField<String>("address.address1").setRequired(true));

		locationPanel.add(new TextField<String>("address.address2"));
		locationPanel.add(new TextField<String>("address.city").setRequired(true));
		locationPanel.add(new TextField<String>("address.stateProvinceCounty")
				.setRequired(true));
		locationPanel.add(new TextField<String>("address.zip").setRequired(true));
		locationPanel.add(new TextField<String>("address.country").setRequired(true));
		locationPanel.add(new TextField<String>("locationName"));
		locationPanel.add(new TextField<String>("phone").setRequired(true));
		locationPanel.add(new TextField<String>("email").setRequired(true));
		locationPanel.add(new TextField<String>("websiteUrl"));

	}
}
