package com.talool.website.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.website.models.MerchantModel;

/**
 * 
 * @author clintz
 * 
 */
public class MerchantPanel extends BasePanel
{
	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);
	private static final long serialVersionUID = -8074065320919062316L;

	private String tags;

	private ModalWindow window;

	private SubmitCallBack callback;

	private boolean isNew = false;

	public MerchantPanel(final String id, final SubmitCallBack callback)
	{
		super(id);
		this.callback = callback;

		Merchant merchant = domainFactory.newMerchant();
		merchant.setPrimaryLocation(domainFactory.newMerchantLocation());
		merchant.getPrimaryLocation().setAddress(domainFactory.newAddress());
		merchant.getPrimaryLocation().setLogoUrl("");
		isNew = true;
		setDefaultModel(Model.of(merchant));
	}

	public MerchantPanel(final String id, final SubmitCallBack callback, final Long merchantId)
	{
		super(id);
		this.callback = callback;
		setDefaultModel(new MerchantModel(merchantId));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");

		Form<Void> form = new Form<Void>("form");

		add(form);

		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		form.add(locationPanel);

		CompoundPropertyModel<Merchant> merchLocModel = new CompoundPropertyModel<Merchant>(
				(IModel<Merchant>) getDefaultModel());

		form.setDefaultModel(merchLocModel);

		add(feedback.setOutputMarkupId(true));

		form.add(new TextField<String>("name").setRequired(true));
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
				StringBuilder sb = new StringBuilder();

				try
				{
					Merchant merchant = (Merchant) form.getDefaultModelObject();
					taloolService.save(merchant);
					target.add(feedback);
					sb.append("Successfully ").append(isNew ? "created '" : "updated '")
							.append(merchant.getName()).append("'");
					getSession().info(sb.toString());
					callback.submitSuccess(target);
				}
				catch (ServiceException e)
				{
					sb.append("Problem saving merchant: ").append(e.getLocalizedMessage());
					getSession().error(sb.toString());
					LOG.error(sb.toString());
					callback.submitFailure(target);
				}
			}

		});

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
