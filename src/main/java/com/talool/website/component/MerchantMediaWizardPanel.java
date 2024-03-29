package com.talool.website.component;

import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.models.MerchantMediaListModel;

abstract public class MerchantMediaWizardPanel extends Panel
{

	private static final long serialVersionUID = -4183138880833396304L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantMediaWizardPanel.class);
	private PageParameters params = new PageParameters();
	private UUID merchantId;
	private MediaType mediaType;
	private MerchantMediaListModel mediaListModel;
	private List<MerchantMedia> myMediaChoices;
	private ChoiceRenderer<MerchantMedia> cr;
	private IModel<MerchantMedia> selectedMediaModel;
	private DropDownChoice<MerchantMedia> mediaSelect;

	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public MerchantMediaWizardPanel(String id, UUID merchantId, MediaType mediaType, IModel<MerchantMedia> model)
	{
		super(id);
		this.merchantId = merchantId;
		this.mediaType = mediaType;
		params.add("id", merchantId);
		params.add("type", mediaType);

		mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchantId);
		mediaListModel.setMediaType(mediaType);

		myMediaChoices = mediaListModel.getObject();

		cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");

		selectedMediaModel = model;

		mediaSelect = new DropDownChoice<MerchantMedia>("availableMedia", selectedMediaModel, mediaListModel, cr);
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		/*
		 * Add the Media Selector
		 */
		add(mediaSelect.setOutputMarkupId(true));

		/*
		 * Add the Upload iFrame
		 */
		add(new UploadIFrame("uploaderIFrame", params)
		{

			private static final long serialVersionUID = 1L;

			@Override
			public void onUploadComplete(AjaxRequestTarget target, String url)
			{
				final MerchantMedia merchantMedia = domainFactory.newMedia(merchantId, url, mediaType);
				saveMedia(merchantMedia);

				myMediaChoices.add(merchantMedia);
				mediaListModel.setObject(myMediaChoices);
				selectedMediaModel.setObject(merchantMedia);
				target.add(mediaSelect);

				onMediaUploadComplete(target, merchantMedia);
			}

		});
		
		// Labels and text
		add(new Label("mediaPanelTitle",new StringResourceModel(getResourceKey("title"), this, null)));
		add(new Label("mediaPanelSummary",new StringResourceModel(getResourceKey("summary"), this, null)));
		add(new Label("mediaLabel",new StringResourceModel(getResourceKey("label"), this, null)));
		
		
	}
	
	private String getResourceKey(String field)
	{
		StringBuilder sb = new StringBuilder("media.");
		if (mediaType.equals(MediaType.DEAL_OFFER_LOGO) || mediaType.equals(MediaType.MERCHANT_LOGO))
		{
			sb.append("logo.");
		}
		else if (mediaType.equals(MediaType.DEAL_OFFER_MERCHANT_LOGO))
		{
			sb.append("icon.");
		}
		else if (mediaType.equals(MediaType.DEAL_OFFER_BACKGROUND_IMAGE))
		{
			sb.append("background.");
		}
		else if (mediaType.equals(MediaType.MERCHANT_IMAGE) || mediaType.equals(MediaType.DEAL_IMAGE))
		{
			sb.append("image.");
		}
		sb.append(field);
		return sb.toString();
	}

	private void saveMedia(MerchantMedia media)
	{
		try
		{
			taloolService.saveMerchantMedia(media);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to save media:", se);
		}
		catch (DataIntegrityViolationException dve)
		{
			// ERROR: duplicate key value violates unique constraint
			// "merchant_media_merchant_id_media_url_key"
			LOG.info("merchant tried to upload the same image twice");
		}
		catch (Exception e)
		{
			LOG.error("random-ass-exception saving new merchant media:", e);
		}
	}

	public DropDownChoice<MerchantMedia> getMediaSelect()
	{
		return mediaSelect;
	}

	abstract public void onMediaUploadComplete(AjaxRequestTarget target, MerchantMedia media);

}
