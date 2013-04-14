package com.talool.website.panel.merchant.definition;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DealOffer;
import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.service.ServiceFactory;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.component.MerchantIdentitySelect;
import com.talool.website.models.AvailableDealOffersListModel;
import com.talool.website.models.AvailableMerchantsListModel;
import com.talool.website.models.DealModel;
import com.talool.website.models.ModelUtil;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.AdminModalWindow;
import com.talool.website.panel.BaseDefinitionPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.util.SessionUtils;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealOfferDealPanel extends BaseDefinitionPanel
{

	private static final long serialVersionUID = 661849211369766802L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferDealPanel.class);
	private String tags;
	private MerchantIdentity merchantIdentity;
	private DealOffer dealOffer;

	public DealOfferDealPanel(final String id, final SubmitCallBack callback)
	{
		super(id, callback, true);
		setMerchantContext(SessionUtils.getSession().getMerchantAccount().getMerchant());
		Deal deal = domainFactory.newDeal();
		setDefaultModel(Model.of(deal));
	}

	public DealOfferDealPanel(final String id, final Long dealOfferId, final SubmitCallBack callback)
	{
		super(id, callback, true);

		try
		{
			dealOffer = ServiceFactory.get().getTaloolService().getDealOffer(dealOfferId);
			setMerchantContext(SessionUtils.getSession().getMerchantAccount().getMerchant());
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading deal offer", se);
		}

		Deal deal = domainFactory.newDeal(dealOffer);
		setDefaultModel(Model.of(deal));
	}

	public DealOfferDealPanel(final String id, final SubmitCallBack callback, final Long dealId)
	{
		super(id, callback, false);
		setDefaultModel(new DealModel(dealId));
		
		try
		{
			Deal deal = ServiceFactory.get().getTaloolService().getDeal(dealId);
			dealOffer = deal.getDealOffer();
			setMerchantContext(deal.getMerchant()); 
		}
		catch (ServiceException se)
		{
			LOG.error("problem loading deal offer", se);
		}
		
	}
	
	private void setMerchantContext(Merchant merchant) 
	{
		merchantIdentity = domainFactory.newMerchantIdentity(merchant.getId(), merchant.getName());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		// form.add(new DealTypeDropDownChoice("merchant").setRequired(true));

		form.add(new MerchantIdentitySelect("availableMerchants", new PropertyModel<MerchantIdentity>(
				this, "merchantIdentity"), new AvailableMerchantsListModel()).setRequired(true));
		
		form.add(new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(
				this, "dealOffer"), new AvailableDealOffersListModel()).setRequired(true));
		
		form.add(new AjaxLink<Void>("newDealLink")
		{

			private static final long serialVersionUID = -2124601710236716290L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				BasePage page = (BasePage)getPage();
				final AdminModalWindow modal = page.getModal();

				SubmitCallBack callback = new SubmitCallBack()
				{

					private static final long serialVersionUID = 6420614586937543567L;

					@Override
					public void submitSuccess(AjaxRequestTarget target)
					{
						modal.close(target);

						// TODO reopen the original modal
						//target.add(oModal);
					}

					@Override
					public void submitFailure(AjaxRequestTarget target)
					{

					}
				};
				
				// TODO probably need a different callback
				MerchantDealOfferPanel panel = new MerchantDealOfferPanel(modal.getContentId(), callback);
				modal.setContent(panel);
				modal.setTitle("New Deal Offer");
				modal.show(target);
			}
		});

		form.add(new TextField<String>("title").setRequired(true));
		form.add(new TextArea<String>("summary").setRequired(true));
		form.add(new TextArea<String>("details").setRequired(true));
		form.add(new TextField<String>("tags", new PropertyModel<String>(this, "tags")));
		// TODO we need a validator on this
		form.add(new TextField<String>("code")); 
		// TODO we need a validator=on this
		form.add(new TextField<String>("imageUrl").setRequired(true)); 
		
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		DateTextField expires = new DateTextField("expires", converter);
		expires.add(new DatePicker());
		form.add(expires);
		
		form.add(new CheckBox("isActive"));

	}

	@SuppressWarnings("unchecked")
	@Override
	public CompoundPropertyModel<Deal> getDefaultCompoundPropertyModel()
	{
		return new CompoundPropertyModel<Deal>((IModel<Deal>) getDefaultModel());
	}

	@Override
	public String getObjectIdentifier()
	{
		Deal deal = (Deal) form.getDefaultModelObject();
		return deal.getTitle();
	}

	@Override
	public void save() throws ServiceException
	{
		Deal deal = (Deal) form.getDefaultModelObject();

		if (StringUtils.isNotEmpty(tags))
		{
			Set<Tag> selectedTags = taloolService.getOrCreateTags(tags.split(","));
			deal.setTags(selectedTags);
		}
		else
		{
			deal.clearTags();
		}

		Merchant merchant = taloolService.getMerchantById(merchantIdentity.getId());

		deal.setDealOffer(dealOffer);
		deal.setMerchant(merchant);

		taloolService.save(deal);
	}

	@Override
	public String getSaveButtonLabel()
	{
		return "Save Deal";
	}

	public String getTags()
	{
		final Deal deal = (Deal) getDefaultModelObject();
		return ModelUtil.getCommaSeperatedTags(deal);
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public MerchantIdentity getMerchantIdentity() {
		return merchantIdentity;
	}

	public void setMerchantIdentity(MerchantIdentity merchantIdentity) {
		this.merchantIdentity = merchantIdentity;
	}

	public DealOffer getDealOffer() {
		return dealOffer;
	}

	public void setDealOffer(DealOffer dealOffer) {
		this.dealOffer = dealOffer;
	}
}
