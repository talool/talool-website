package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Customer;
import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.DealListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminMenuPanel;

public class DealsPage extends BasePage {

	private static final long serialVersionUID = 1133795226226645331L;
	private static final Logger LOG = LoggerFactory.getLogger(DealsPage.class);
	private String _method;
	private Long _id;
	
	public static final String METHOD_MERCHANT = "merchant";
	public static final String METHOD_BOOK = "book";

	public DealsPage()
	{
		super();
	}

	public DealsPage(PageParameters parameters)
	{
		super(parameters);
		_method = parameters.get("method").toString();
		_id = parameters.get("id").toLongObject();
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		StringBuffer pageTitle = new StringBuffer("Deals");
		
		DealListModel model = new DealListModel();
		if (_method.equalsIgnoreCase(METHOD_MERCHANT)) {
			model.setMerchantId(_id);
			try {
				Merchant merchant = ServiceFactory.get().getTaloolService().getMerchantById(_id);
				pageTitle.append(" for ").append(merchant.getName());
			} catch (ServiceException se) {
				LOG.error("problem loading merchant", se);
			}
		} else if (_method.equalsIgnoreCase(METHOD_BOOK)) {
			model.setDealOfferId(_id);
			try {
				DealOffer book = ServiceFactory.get().getTaloolService().getDealOffer(_id);
				pageTitle.append(" in ").append(book.getTitle());
			} catch (ServiceException se) {
				LOG.error("problem loading deal offer", se);
			}
		}
		
		add(new Label("pageTitle",pageTitle.toString()));
		
		final ListView<Deal> deals = new ListView<Deal>("dealRptr", model)
		{

			private static final long serialVersionUID = -7055359730593625591L;

			@Override
			protected void populateItem(ListItem<Deal> item)
			{
				Deal deal = item.getModelObject();

				item.setModel(new CompoundPropertyModel<Deal>(deal));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("title"));
				item.add(new Label("summary"));
				item.add(new Label("isActive"));
				item.add(new Label("expires"));
			}

		};

		add(deals);
	}

}
