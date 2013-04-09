package com.talool.website.pages.lists;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.pages.BasePage;
import com.talool.website.pages.define.BookPage;
import com.talool.website.panel.AdminMenuPanel;

public class BooksPage extends BasePage {
	
	private static final long serialVersionUID = 4465255303084700956L;
	private static final Logger LOG = LoggerFactory.getLogger(BooksPage.class);
	private String _method;
	private Long _id;
	
	public static final String METHOD_MERCHANT = "merchant";
	public static final String METHOD_ALL = "all";

	public BooksPage()
	{
		super();
	}

	public BooksPage(PageParameters parameters)
	{
		super(parameters);
		_method = parameters.get("method").toString();
		StringValue id = parameters.get("id");
		if (!id.isNull()) {
			_id = parameters.get("id").toLongObject();
		}
	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new AdminMenuPanel("adminMenuPanel").setRenderBodyOnly(true));
		
		StringBuffer pageTitle = new StringBuffer("Deal Books");
		
		DealOfferListModel model = new DealOfferListModel();
		if (_method.equalsIgnoreCase(METHOD_MERCHANT)) {
			model.setMerchantId(_id);
			try {
				Merchant merchant = ServiceFactory.get().getTaloolService().getMerchantById(_id);
				pageTitle.append(" for ").append(merchant.getName());
			} catch (ServiceException se) {
				LOG.error("problem loading merchant", se);
			}
		}
		
		add(new Label("pageTitle",pageTitle.toString()));
		
		final ListView<DealOffer> books = new ListView<DealOffer>("bookRptr", model)
		{

			private static final long serialVersionUID = -5116981666528423158L;

			@Override
			protected void populateItem(ListItem<DealOffer> item)
			{
				DealOffer book = item.getModelObject();

				item.setModel(new CompoundPropertyModel<DealOffer>(book));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd"));
				}
				else
				{
					item.add(new AttributeModifier("class", "even"));
				}

				item.add(new Label("title"));
				item.add(new Label("merchant.name"));
				item.add(new Label("price"));
				item.add(new Label("isActive"));
				item.add(new Label("expires"));
				item.add(new Label("purchaces", 10));
				//item.add(new Label("purchaces", book.getNumberOfPurchases()));
				
				PageParameters editParams = new PageParameters();
				editParams.set("id", book.getId());
				BookmarkablePageLink<Void> editLink = new BookmarkablePageLink<Void>("editLink",BookPage.class,editParams);
				item.add(editLink);
				
				PageParameters dealsParams = new PageParameters();
				dealsParams.set("method", DealsPage.METHOD_BOOK);
				dealsParams.set("id", book.getId());
				BookmarkablePageLink<Void> dealsLink = new BookmarkablePageLink<Void>("dealsLink",DealsPage.class,dealsParams);
				item.add(dealsLink);
			}

		};

		add(books);
		
	}

}
