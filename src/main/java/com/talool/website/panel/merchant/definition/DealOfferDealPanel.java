package com.talool.website.panel.merchant.definition;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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
import com.talool.website.Config;
import com.talool.website.component.DealOfferSelect;
import com.talool.website.component.MerchantIdentitySelect;
import com.talool.website.component.StaticImage;
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
	private FileUploadField fileUploadField;
	private List<FileUpload> fileUploads;

	public DealOfferDealPanel(final String id, final SubmitCallBack callback)
	{
		super(id, callback, true);
		setMerchantContext(SessionUtils.getSession().getMerchantAccount().getMerchant());
		Deal deal = domainFactory.newDeal(SessionUtils.getSession().getMerchantAccount());
		setDefaultModel(Model.of(deal));
	}

	public DealOfferDealPanel(final String id, final Long dealOfferId, final SubmitCallBack callback)
	{
		super(id, callback, true);

		try
		{
			dealOffer = taloolService.getDealOffer(dealOfferId);
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

		final DealPreview dealPreview = new DealPreview("dealBuilder", getDefaultCompoundPropertyModel().getObject());
		dealPreview.setOutputMarkupId(true);
		form.add(dealPreview);
		
		// form.add(new DealTypeDropDownChoice("merchant").setRequired(true));

		form.add(new MerchantIdentitySelect("availableMerchants", new PropertyModel<MerchantIdentity>(
				this, "merchantIdentity"), new AvailableMerchantsListModel()).setRequired(true));

		form.add(new DealOfferSelect("availableDealOffers", new PropertyModel<DealOffer>(this,
				"dealOffer"), new AvailableDealOffersListModel()).setRequired(true));

		form.add(new AjaxLink<Void>("newDealLink")
		{

			private static final long serialVersionUID = -2124601710236716290L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				BasePage page = (BasePage) getPage();
				final AdminModalWindow modal = page.getModal();

				SubmitCallBack callback = new SubmitCallBack()
				{

					private static final long serialVersionUID = 6420614586937543567L;

					@Override
					public void submitSuccess(AjaxRequestTarget target)
					{
						modal.close(target);

						// TODO reopen the original modal
						// target.add(oModal);
					}

					@Override
					public void submitFailure(AjaxRequestTarget target)
					{

					}
				};

				// TODO probably need a different callback
				MerchantDealOfferPanel panel = new MerchantDealOfferPanel(modal.getContentId(), callback);

				modal.getCurrentContent().replaceWith(panel.setOutputMarkupId(true));

				modal.setTitle("New Deal Offer");

				// modal.show(target);
				target.add(panel);

			}
		});

		TextField<String> titleField = new TextField<String>("title");
		titleField.setRequired(true);
		titleField.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.TITLE, "onBlur"));
		form.add(titleField);
		
		TextArea<String> summaryField = new TextArea<String>("summary");
		summaryField.setRequired(true);
		summaryField.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.SUMMARY, "onBlur"));
		form.add(summaryField);
		
		TextArea<String> detailsField = new TextArea<String>("details");
		detailsField.setRequired(true);
		detailsField.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.DETAILS, "onBlur"));
		form.add(detailsField);
		
		// TODO we need a validator on this
		TextField<String> codeField = new TextField<String>("code");
		codeField.setRequired(true);
		codeField.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.CODE, "onBlur"));
		form.add(codeField);
		
		form.add(new TextField<String>("tags", new PropertyModel<String>(this, "tags")));

		/* TODO we need a validator=on this
		TextField<String> imageField = new TextField<String>("imageUrl");
		imageField.setRequired(true);
		imageField.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.IMAGE, "onBlur"));
		form.add(imageField);
		*/

		// TODO fix this to be a proper model
		form.add(new Label("imageUrl"));
		form.add(new StaticImage("imagePreview", true, getDefaultCompoundPropertyModel().getObject()
				.getImageUrl()));

		// multi-part for image uploads
		form.setMultiPart(true);
		form.setMaxSize(Config.get().getLogoUploadMaxBytes());
		form.add(new FileUploadField("fileUploads", new PropertyModel<List<FileUpload>>(this,
				"fileUploads")));

		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		DateTextField expires = new DateTextField("expires", converter);
		expires.add(new DatePicker());
		form.add(expires);
		expires.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.EXPIRES, "onChange"));

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

		if (CollectionUtils.isNotEmpty(fileUploads))
		{
			try
			{
				final String imageName = ModelUtil.saveUploadImage(fileUploads.get(0));
				if (imageName != null)
				{
					deal.setImageUrl(Config.get().getStaticLogoBaseUrl() + imageName);
				}
			}
			catch (Exception e)
			{
				LOG.error("Problem with logo upload:" + e.getLocalizedMessage(), e);
				getSession().error("There was a problem with logo upload");
			}
		}

		final Merchant merchant = taloolService.getMerchantById(merchantIdentity.getId());

		deal.setDealOffer(dealOffer);
		deal.setMerchant(merchant);
		deal.setUpdatedByMerchantAccount(SessionUtils.getSession().getMerchantAccount());

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

	public MerchantIdentity getMerchantIdentity()
	{
		return merchantIdentity;
	}

	public void setMerchantIdentity(MerchantIdentity merchantIdentity)
	{
		this.merchantIdentity = merchantIdentity;
	}

	public DealOffer getDealOffer()
	{
		return dealOffer;
	}

	public void setDealOffer(DealOffer dealOffer)
	{
		this.dealOffer = dealOffer;
	}

}
