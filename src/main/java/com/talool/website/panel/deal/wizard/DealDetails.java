package com.talool.website.panel.deal.wizard;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
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
import com.talool.website.component.MerchantIdentitySelect;
import com.talool.website.models.AvailableMerchantsListModel;
import com.talool.website.panel.deal.DealPreview;
import com.talool.website.panel.deal.DealPreviewUpdatingBehavior;
import com.talool.website.panel.deal.definition.ImageSelectPanel;
import com.talool.website.panel.deal.definition.ImageUploadPanel;
import com.talool.website.panel.deal.definition.template.DealTemplateSelectPanel;
import com.talool.website.util.SessionUtils;

public class DealDetails extends WizardStep {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(DealDetails.class);
	private Image image;
	private MerchantIdentity merchantIdentity;

	public DealDetails()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        
        DomainFactory domainFactory = FactoryManager.get().getDomainFactory();
		Merchant merchant = SessionUtils.getSession().getMerchantAccount().getMerchant();
		merchantIdentity = domainFactory.newMerchantIdentity(merchant.getId(), merchant.getName());

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
		
		final WebMarkupContainer imageSelect = new WebMarkupContainer("imageSelectContainer");
		imageSelect.setOutputMarkupId(true);
		addOrReplace(imageSelect);
		
		ImageSelectPanel images = new ImageSelectPanel("imageSelectPanel", new PropertyModel<Image>(
				this, "image"), dealPreview);
		images.setOutputMarkupId(true);
		imageSelect.add(images);
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

	protected void checkUploads() {
		Component imagePanel = get("imageSelectContainer:imageSelectPanel");
		if (imagePanel instanceof ImageUploadPanel)
		{
			Deal deal = (Deal) getDefaultModelObject();
			deal.setImageUrl(((ImageUploadPanel) imagePanel).getUpload());
		}
	}
	
	@Override
	public void applyState() {
		// TODO Auto-generated method stub
		super.applyState();
		checkUploads();
	}
	
	
}
