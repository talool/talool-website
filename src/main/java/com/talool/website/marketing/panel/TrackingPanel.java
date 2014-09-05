package com.talool.website.marketing.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.DealOfferPurchase;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.service.EmailService;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.panel.NiceFeedbackPanel;
import com.talool.website.util.SessionUtils;

public class TrackingPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(TrackingPanel.class);
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	
	protected transient static final EmailService emailService = FactoryManager.get()
			.getServiceFactory().getEmailService();

	protected transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	private String code;
	private List<DealOfferPurchase> purchases = new ArrayList<DealOfferPurchase>();
	
	public TrackingPanel(String id, String cd) {
		super(id);
		code = cd;
	}
	
	@Override
	protected void onInitialize() 
	{
		super.onInitialize();
		
		if (code!=null)
		{
			try {
				lookupCode();
			} catch (ServiceException e) {
				SessionUtils.errorMessage("There was a problem searching for your sales.  Please contact support@talool.com for assistance.");
				LOG.error(e.getLocalizedMessage(), e);
			}
		}
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final WebMarkupContainer results = new WebMarkupContainer("results");
		container.add(results.setOutputMarkupId(true));
		
		final NiceFeedbackPanel feedback = new NiceFeedbackPanel("feedback");
		container.add(feedback.setOutputMarkupId(true));
		
		final ListView<DealOfferPurchase> sales = new ListView<DealOfferPurchase>(
				"repeater", new PropertyModel<List<DealOfferPurchase>>(this, "purchases"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<DealOfferPurchase> item)
			{
				final DealOfferPurchase purchase = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealOfferPurchase>(purchase));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("customer.fullName"));
				item.add(new Label("created"));
				
			}

		};
		results.add(sales.setOutputMarkupId(true));
		
		final Form<Void> form = new Form<Void>("form");
		container.add(form);
		
		AjaxButton submit = new AjaxButton("submitButton", form)
		{

			private static final long serialVersionUID = -6562989540935949813L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form)
			{
				target.add(feedback);
				SessionUtils.errorMessage("There was a problem saving your account");
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				try
				{
					lookupCode();
				}
				catch (Exception e)
				{
					SessionUtils.errorMessage("There was a problem searching for your sales.  Please contact support@talool.com for assistance.");
					LOG.error(e.getLocalizedMessage(), e);
				}
				target.add(results);
				target.add(feedback);

			}

		};
		form.add(submit);
		
		TextField<String> mn = new TextField<String>("code",new PropertyModel<String>(this,"code"));
		form.add(mn.setRequired(true).setOutputMarkupId(true));
		
		

	}
	
	private void lookupCode() throws ServiceException
	{
		purchases = new ArrayList<DealOfferPurchase>();
		try
		{
			purchases = taloolService.getDealOfferPurchasesByTrackingCode(code.toUpperCase());
			StringBuilder message = new StringBuilder("We recorded ");
			message.append(purchases.size()).append(" purchase(s) with tracking code: ").append(code).append(".");
			success(message.toString());
		}
		catch (ServiceException se)
		{
			LOG.error(se.getLocalizedMessage(), se);
		}
	}

}
