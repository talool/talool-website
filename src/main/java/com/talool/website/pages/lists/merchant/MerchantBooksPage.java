package com.talool.website.pages.lists.merchant;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.models.DealOfferListModel;
import com.talool.website.pages.define.BookPage;
import com.talool.website.pages.lists.BooksPage;

public class MerchantBooksPage extends BooksPage {

	private static final long serialVersionUID = -8423706231787649811L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantBooksPage.class);
	private Long _id;
	private DealOfferListModel _model;
	
	public MerchantBooksPage(PageParameters parameters)
	{
		super(parameters);
		StringValue id = parameters.get("id");
		if (!id.isNull()) {
			_id = parameters.get("id").toLongObject();
		}
		
		_model = new DealOfferListModel();
		_model.setMerchantId(_id);
	}
	
	@Override
	public DealOfferListModel getDealOfferListModel()
	{
		return _model;
	}
	
	@Override
	public String getPageTitle()
	{
		StringBuffer sb = new StringBuffer("Deal Offers for ");
		Merchant merchant;
		try {
			merchant = ServiceFactory.get().getTaloolService().getMerchantById(_id);
			sb.append(merchant.getName());
		} catch (ServiceException se) {
			LOG.error("problem loading merchant", se);
		}
		return sb.toString();
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public BookmarkablePageLink getCreateLink() {
		PageParameters createParams = new PageParameters();
		createParams.set("id", _id);
		BookmarkablePageLink<Void> createLink = new BookmarkablePageLink<Void>("createLink",BookPage.class,createParams);
		return createLink;
	}

}
