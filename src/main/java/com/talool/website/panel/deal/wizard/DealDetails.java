package com.talool.website.panel.deal.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Image;
import com.talool.core.Merchant;
import com.talool.core.MerchantIdentity;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.domain.ImageImpl;
import com.talool.website.component.DealImageSelect;
import com.talool.website.component.MerchantIdentitySelect;
import com.talool.website.models.AvailableDealImagesListModel;
import com.talool.website.models.AvailableMerchantsListModel;
import com.talool.website.pages.UploadPage;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;
import com.talool.website.panel.deal.definition.template.DealTemplateSelectPanel;
import com.talool.website.util.SessionUtils;

public class DealDetails extends WizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealDetails.class);
	private Image image;
	private List<Image> myImages;
	private AvailableDealImagesListModel imageListModel;
	private MerchantIdentity merchantIdentity;

	public DealDetails()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        
        DomainFactory domainFactory = FactoryManager.get().getDomainFactory();
		Merchant merchant = SessionUtils.getSession().getMerchantAccount().getMerchant();
		merchantIdentity = domainFactory.newMerchantIdentity(merchant.getId(), merchant.getName());
		
		imageListModel = new AvailableDealImagesListModel();
		myImages = imageListModel.getObject();

    }
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		Deal deal = (Deal) getDefaultModelObject();
		
		final DealPreview dealPreview = new DealPreview("dealBuilder", deal);
		dealPreview.setOutputMarkupId(true);
		addOrReplace(dealPreview);
		
		MerchantIdentitySelect merchantSelect = new MerchantIdentitySelect("availableMerchants",
				new PropertyModel<MerchantIdentity>(this, "merchantIdentity"),
				new AvailableMerchantsListModel());
		merchantSelect.add(new DealPreviewUpdatingBehavior(dealPreview,
				DealPreviewUpdatingBehavior.DealComponent.MERCHANT, "onChange"));

		addOrReplace(merchantSelect.setRequired(true).setOutputMarkupId(true));
		
		DealTemplateSelectPanel templates = new DealTemplateSelectPanel("templateSelectPanel", dealPreview);
		templates.setDefaultModel(getDefaultModel());
		addOrReplace(templates);
		
		final DealImageSelect images = new DealImageSelect("availableImages", 
				new PropertyModel<Image>(this, "image"), 
				imageListModel);
		images.setRequired(true);
		images.add(new DealPreviewUpdatingBehavior(dealPreview, DealPreviewUpdatingBehavior.DealComponent.IMAGE, "onChange"));
		addOrReplace(images);
		
		/*
		 * Add an iframe that keep the upload in a sandbox
		 */
		final InlineFrame iframe = new InlineFrame("uploaderIFrame", UploadPage.class);
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
				String name = params.getParameterValue("name").toString();
				
				// Create a new image with the returned url and add it to the list of images
				setImage(new ImageImpl(name,url));
				myImages.add(image);
				imageListModel.setObject(myImages);
				
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
	}
	
	public MerchantIdentity getMerchantIdentity() {
		Deal deal = (Deal) getDefaultModelObject();
		DomainFactory domainFactory = FactoryManager.get().getDomainFactory();
		Merchant merchant = deal.getMerchant();
		if (merchant == null) {
			merchant = SessionUtils.getSession().getMerchantAccount().getMerchant();
		}
		return domainFactory.newMerchantIdentity(merchant.getId(), merchant.getName());
	}

	public void setMerchantIdentity(MerchantIdentity merchantIdentity) {
		this.merchantIdentity = merchantIdentity;
		Deal deal = (Deal) getDefaultModelObject();
		TaloolService taloolService = FactoryManager.get().getServiceFactory().getTaloolService();
		Merchant merchant;
		try {
			merchant = taloolService.getMerchantById(merchantIdentity.getId());
		} 
		catch(ServiceException se)
		{
			merchant = null;
		}
		deal.setMerchant(merchant);
		
	}
	
	public Image getImage() {
		Deal deal = (Deal) getDefaultModelObject();
		Image img;
		if (StringUtils.isEmpty(deal.getImageUrl()))
		{
			img = new ImageImpl("Test Image 1",
					"http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test.png");
		} else {
			img = new ImageImpl("Test Image 1",
					deal.getImageUrl());
		}
		return img;	
	}

	public void setImage(Image image) {
		this.image = image;
		Deal deal = (Deal) getDefaultModelObject();
		deal.setImageUrl(image.getUrl());
	}

}
