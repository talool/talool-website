package com.talool.website.panel.message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.service.ServiceException;
import com.talool.messaging.job.MerchantGiftJob;
import com.talool.messaging.job.MessagingJob;
import com.talool.website.models.MerchantModel;
import com.talool.website.models.MessagingJobListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantPanel;
import com.talool.website.panel.message.wizard.MessageWizard;
import com.talool.website.util.SessionUtils;

public class MerchantMessages extends BaseTabPanel {


	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);
	private static final long serialVersionUID = 1177862345946906145L;
	private UUID _merchantId;
	private List<MerchantAccount> mal;
	private MessageWizard wizard;
	
	public MerchantMessages(String id)
	{
		super(id);
		Merchant m = SessionUtils.getSession().getMerchantAccount().getMerchant();
		_merchantId = m.getId();
		mal = new ArrayList<MerchantAccount>();
		try
		{
			taloolService.reattach(m);
			mal.addAll(m.getMerchantAccounts());
		}
		catch(ServiceException e)
		{
			LOG.error("failed to reattach merchant",e);
		}
		
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		final BasePage page = (BasePage) getPage();
		
		final WebMarkupContainer container = new WebMarkupContainer("list");
		container.setOutputMarkupId(true);
		add(container);
		
		MessagingJobListModel model = new MessagingJobListModel();
		model.setMerchantAccounts(mal);
		
		int jobCount = model.getObject().size();
		container.add(new Label("totalCount",jobCount));
		container.add(new Label("navigator",""));
		
		final ListView<MessagingJob> locations = new ListView<MessagingJob>(
				"jobRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MessagingJob> item)
			{
				final MessagingJob job = item.getModelObject();

				item.setModel(new CompoundPropertyModel<MessagingJob>(job));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}
				
				item.add(new Label("jobNotes"));
				item.add(new Label("jobState"));
				item.add(new Label("usersTargeted"));
				item.add(new Label("sends"));
				item.add(new Label("emailOpens"));
				item.add(new Label("giftOpens"));
				
				String jobType = "";
				if (job instanceof MerchantGiftJob)
				{
					jobType = "Gift";
				}
				else
				{
					jobType = "Book";
				}
				item.add(new Label("jobType",jobType));

				DateTimeZone tz = DateTimeZone.forTimeZone(SessionUtils.getSession().getBestGuessTimeZone());
				DateTime localDate = new DateTime(job.getScheduledStartDate().getTime(), tz);
				DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM d, yyyy h:mm a z");
				String startDate = formatter.print(localDate);
				item.add(new Label("date",startDate));

			}

		};
		container.add(locations);
		
		// override the action button
		AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();

				Merchant merchant = new MerchantModel(_merchantId, true).getObject();
				MessageJobPojo mg = new MessageJobPojo(merchant);
				wizard.setModelObject(mg);
				wizard.open(target);
			}
		};
		page.setActionLink(actionLink);
		Label actionLabel = new Label("actionLabel", getActionLabel());
		actionLabel.setOutputMarkupId(true);
		actionLink.add(actionLabel);
		actionLink.setOutputMarkupId(true);
		
		// Wizard
		wizard = new MessageWizard("messageWiz", "Message Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a message is created
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
	}
	
	@Override
	public String getActionLabel() {
		return "Create Message";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return null;
	}

	

}
