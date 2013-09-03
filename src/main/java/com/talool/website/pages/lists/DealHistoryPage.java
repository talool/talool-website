package com.talool.website.pages.lists;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.talool.core.AcquireStatus;
import com.talool.core.DealAcquire;
import com.talool.core.DealAcquireHistory;
import com.talool.core.gift.EmailGift;
import com.talool.core.gift.FaceBookGift;
import com.talool.core.gift.Gift;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.customer.definition.CustomerPanel;
import com.talool.website.util.SecuredPage;

/**
 * History page. Keep in mind a DealAcquire object only has history records when
 * updates occur. In other words, if a DealAcquire record is created, that
 * record represent the current state.
 * 
 * It isn't until a state change, gift, etc take place (an update) which forms a
 * DealAcquireHistory record. This saves persistence space by not forming
 * duplicate records until updates occur. Therefore special cases occur when
 * looking into the history: either a DealAcquire record exists (current state)
 * or a DealAcquire record + any DealAcquireHistory formed from updates.
 * 
 * 
 * @author clintz
 * 
 */
@SecuredPage
public class DealHistoryPage extends BasePage
{
	private static final Logger LOG = Logger.getLogger(DealHistoryPage.class);

	private static final long serialVersionUID = 2102415289760762365L;

	private static final String HISTORY_REPEATER = "historyRptr";
	private static final String CURRENT_CONTAINER = "currentContainer";

	private List<DealAcquireHistory> histories;
	private DealAcquire dealAcquire;

	private enum HistoryLookupType
	{
		GiftId, DealAcquireId
	};

	private HistoryLookupType selectedHistoryLookupType = HistoryLookupType.DealAcquireId;

	private UUID elementId;

	public DealHistoryPage()
	{
		super();
	}

	public DealHistoryPage(PageParameters parameters)
	{
		super(parameters);
		if (parameters.get("d").toString() != null)
		{
			elementId = UUID.fromString(parameters.get("d").toString());
		}
		else if (parameters.get("g").toString() != null)
		{
			elementId = UUID.fromString(parameters.get("g").toString());
			selectedHistoryLookupType = HistoryLookupType.GiftId;
		}
	}

	public boolean hasActionLink()
	{
		return false;
	}

	private void getHistory()
	{
		try
		{
			WebMarkupContainer container = (WebMarkupContainer) DealHistoryPage.this.get(CURRENT_CONTAINER);

			if (selectedHistoryLookupType == HistoryLookupType.GiftId)
			{
				histories = taloolService.getDealAcquireHistoryByGiftId(elementId, false);
			}
			else
			{
				histories = taloolService.getDealAcquireHistory(elementId, false);
			}

			if (CollectionUtils.isEmpty(histories))
			{
				// get the raw DealAcquire if no history. The current DealAcquire is the
				// only record
				dealAcquire = ServiceFactory.get().getCustomerService().getDealAcquire(elementId);
				if (dealAcquire == null || dealAcquire.getId() == null)
				{
					container.setVisible(false);
					warn("There is no DealAcquire for that ID");
					return;
				}
				container.get(HISTORY_REPEATER).setDefaultModel(Model.of(1));
				container.setDefaultModel(new CompoundPropertyModel<DealAcquire>(dealAcquire));
			}
			else
			{
				// make sure to add 1 so we can stuff the current status
				// (dealAcquire)
				// in the first row
				dealAcquire = histories.get(0).getDealAcquire();
				container.get(HISTORY_REPEATER).setDefaultModel(Model.of(histories.size() + 1));
				container.setDefaultModel(new CompoundPropertyModel<DealAcquire>(histories.get(0).getDealAcquire()));
			}

			container.setVisible(true);
			container.get(HISTORY_REPEATER).setVisible(true);

		}
		catch (ServiceException e)
		{
			LOG.error("Problem getting dealAcquireHistory " + e.getLocalizedMessage(), e);
			error("Problem getting dealAcquireHistory");
		}
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		Form<Void> form = new Form<Void>("historyForm")
		{
			private static final long serialVersionUID = -477362909507132959L;

			@Override
			protected void onSubmit()
			{
				getHistory();
			}
		};

		form.add(new DropDownChoice<HistoryLookupType>("historyLookup",
				new PropertyModel<HistoryLookupType>(this, "selectedHistoryLookupType"),
				Arrays.asList(HistoryLookupType.values())));

		add(form);
		form.add(new TextField<UUID>("elementId", new PropertyModel<UUID>(this, "elementId"), UUID.class).setRequired(true));

		WebMarkupContainer currentContainer = new WebMarkupContainer(CURRENT_CONTAINER);
		add(currentContainer.setVisible(false));

		final Loop history = new Loop(HISTORY_REPEATER, new Model<Integer>(0))
		{

			private static final long serialVersionUID = 462664541713299654L;

			@Override
			protected void populateItem(LoopItem item)
			{
				Fragment giftTo = null;
				Date date = null;
				AcquireStatus acquireStatus = null;
				String customerEmail = null;
				Gift gift = null;

				if (item.getIndex() == 0)
				{
					date = dealAcquire.getUpdated();
					acquireStatus = dealAcquire.getAcquireStatus();
					customerEmail = dealAcquire.getCustomer().getEmail();
					gift = dealAcquire.getGift();
				}
				else
				{
					DealAcquireHistory history = histories.get(item.getIndex() - 1);
					date = history.getUpdated();
					acquireStatus = history.getAcquireStatus();
					customerEmail = history.getCustomer().getEmail();
					gift = history.getGift();
				}

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				item.add(new Label("date", date));
				item.add(new Label("acquireStatus2", acquireStatus));
				item.add(new Label("customerEmail", customerEmail));

				if (gift == null)
				{
					giftTo = new Fragment("giftTo", "notGifted", DealHistoryPage.this);
				}
				// Be careful. Gift is Lazy loaded instanceof will break!
				else if (gift instanceof FaceBookGift)
				{
					giftTo = new Fragment("giftTo", "facebook", DealHistoryPage.this);
					giftTo.add(new Label("name", gift.getReceipientName()));
				}
				else if (gift instanceof EmailGift)
				{
					giftTo = new Fragment("giftTo", "email", DealHistoryPage.this);
					giftTo.add(new Label("email", ((EmailGift) gift).getToEmail()));
				}

				item.add(giftTo);
			}
		};

		currentContainer.add(history);

		currentContainer.add(new Label("deal.merchant.name"));
		currentContainer.add(new Label("deal.title"));
		currentContainer.add(new Label("deal.dealOffer.title"));
		currentContainer.add(new Label("deal.created"));

		if (elementId != null)
		{
			getHistory();
		}

	}

	@Override
	public String getHeaderTitle()
	{
		return "Deal Acquire History";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new CustomerPanel(contentId, callback);
	}

	@Override
	public String getNewDefinitionPanelTitle()
	{
		return null;
	}

}
