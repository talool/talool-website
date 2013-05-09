package com.talool.website.panel.deal.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.dynamic.DynamicWizardStep;
import org.apache.wicket.extensions.wizard.dynamic.IDynamicWizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;

import com.talool.core.Deal;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.website.component.DealImageSelect;
import com.talool.website.models.MerchantMediaListModel;
import com.talool.website.pages.UploadPage;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;
import com.talool.website.panel.deal.definition.template.DealTemplateSelectPanel;

public class DealDetails extends DynamicWizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealDetails.class);
	private MerchantMedia image;
	private MerchantMediaListModel mediaListModel;
	private List<MerchantMedia> myImages;
	private final IDynamicWizardStep nextStep;
	private DealWizard wizard;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public DealDetails(DealWizard wiz)
    {
        super(null, new ResourceModel("title"), new ResourceModel("summary"));
        this.nextStep = new DealTags(this, wiz);
        this.wizard = wiz;
    }
	
	public void init() {
		Deal deal = (Deal) getDefaultModelObject();
		Merchant merchant = deal.getMerchant();
		mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchant.getId());
		mediaListModel.setMediaType(MediaType.DEAL_IMAGE);
		myImages = mediaListModel.getObject();
	}
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		init();
		
		final Deal deal = (Deal) getDefaultModelObject();
		
		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
		final DealImageSelect images = new DealImageSelect("availableImages", 
				new PropertyModel<MerchantMedia>(this, "image"), 
				mediaListModel);
		images.setRequired(true);
		images.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.IMAGE, "onChange"));
		addOrReplace(images);
		
		/*
		 * Add an iframe that keep the upload in a sandbox
		 */
		PageParameters params = new PageParameters();
		params.add("id", deal.getMerchant().getId());
		params.add("type", MediaType.DEAL_IMAGE);
		final InlineFrame iframe = new InlineFrame("uploaderIFrame", UploadPage.class, params);
		addOrReplace(iframe);
		
		/*
		 *  Enable messages to be posted from that sandbox
		 */
		iframe.add(new AbstractDefaultAjaxBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
			protected void respond(AjaxRequestTarget target) {
				IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
				String url = params.getParameterValue("url").toString();
				
				MerchantMedia dealImage = domainFactory.newMedia(deal.getMerchant().getId(), url, MediaType.DEAL_IMAGE);
				saveMedia(dealImage);
				setImage(dealImage);
				myImages.add(image);
				mediaListModel.setObject(myImages);
				
				// re-init the preview
				Deal deal = (Deal) getDefaultModelObject();
				dealPreview.init(deal);
				
				target.add(images);
				target.add(dealPreview);
			}
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				
				PackageTextTemplate ptt = new PackageTextTemplate( DealDetails.class, "DealDetails.js" );

				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "callbackUrl", getCallbackUrl().toString() );
				
				response.render(JavaScriptHeaderItem.forScript(ptt.asString(map), "dealupload"));
			}
			
		});
		
		DealTemplateSelectPanel templates = new DealTemplateSelectPanel("templateSelectPanel", dealPreview);
		templates.setDefaultModel(getDefaultModel());
		addOrReplace(templates);
	}
	
	public MerchantMedia getImage() {
		// TODO Remove this loop when MerchantMedia is stored on the deal 
		Deal deal = (Deal) getDefaultModelObject();
		for (MerchantMedia image:myImages) 
		{
			if (image.getMediaUrl().equals(deal.getImageUrl()))
			{
				return image;
			}
		}
	
		return null;	
	}

	public void setImage(MerchantMedia image) {
		this.image = image;
		Deal deal = (Deal) getDefaultModelObject();
		deal.setImageUrl(image.getMediaUrl());
	}
	
	private boolean saveMedia(MerchantMedia media)
	{
		try
		{
			taloolService.saveMerchantMedia(media);
			return true;
		}
		catch (ServiceException se)
		{
			LOG.error("failed to save media:", se);
		}
		catch (DataIntegrityViolationException dve)
		{
			// TODO Don't try to save the same media for the same merchant
			// ERROR: duplicate key value violates unique constraint
			// "merchant_media_merchant_id_media_url_key"
			LOG.info("merchant tried to upload the same image twice");
		}
		catch (Exception e)
		{
			LOG.error("random-ass-exception saving new merchant media:", e);
		}
		
		return false;
	}

	@Override
	public boolean isLastStep() {
		return false;
	}

	@Override
	public IDynamicWizardStep next() {
		return nextStep;
	}
	
	@Override
	public IDynamicWizardStep last() {
		return new DealSave(this);
	}

}
