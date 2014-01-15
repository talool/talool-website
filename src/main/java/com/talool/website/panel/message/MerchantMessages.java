package com.talool.website.panel.message;

import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.website.models.MerchantMessageListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;
import com.talool.website.panel.merchant.definition.MerchantPanel;

public class MerchantMessages extends BaseTabPanel {


	private static final Logger LOG = LoggerFactory.getLogger(MerchantPanel.class);
	private static final long serialVersionUID = 1177862345946906145L;
	private UUID _merchantId;
	
	
	public MerchantMessages(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		MerchantMessageListModel model = new MerchantMessageListModel();
		model.setMerchantId(_merchantId);
		final ListView<MerchantMessage> messages = new ListView<MerchantMessage>("messageRptr", model)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<MerchantMessage> item)
			{

				MerchantMessage message = item.getModelObject();
				final String messageId = message.getMerchantId();

				item.setModel(new CompoundPropertyModel<MerchantMessage>(message));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("message"));
				item.add(new Label("radius"));
				item.add(new Label("criteria"));
				item.add(new Label("deliveryStats"));
				item.add(new Label("isActive"));
				item.add(new Label("expiresOn"));
				item.add(new Label("createdBy"));
				item.add(new Label("editedBy"));

				BasePage page = (BasePage) this.getPage();
				final AdminModalWindow modal = page.getModal();
				final SubmitCallBack callback = page.getCallback(modal);
				item.add(new AjaxLink<Void>("editLink")
				{
					private static final long serialVersionUID = 268692101349122303L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getSession().getFeedbackMessages().clear();
						
						MerchantMessagePanel panel = new MerchantMessagePanel(modal.getContentId(), callback,
								messageId);
						modal.setContent(panel);
						modal.setTitle("Edit Message");
						modal.show(target);
						
					}
				});
			}

		};

		add(messages);
	}
	
	@Override
	public String getActionLabel() {
		return "Create Message";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		return new MerchantMessagePanel(contentId, _merchantId, callback);
	}

	

}
