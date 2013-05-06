package com.talool.website.panel.merchant.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Image;
import com.talool.core.MediaType;
import com.talool.core.Merchant;
import com.talool.core.MerchantMedia;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;
import com.talool.domain.ImageImpl;
import com.talool.website.component.StateOption;
import com.talool.website.component.StateSelect;
import com.talool.website.models.MerchantMediaListModel;
import com.talool.website.pages.UploadPage;

public class MerchantLocations extends WizardStep {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantLocations.class);
	private Image logo;
	private List<String> countries = new ArrayList<String>();
	private List<MerchantMedia> unsavedMedia = new ArrayList<MerchantMedia>();
	private List<MerchantMedia> myLogoChoices = new ArrayList<MerchantMedia>();
	private DropDownChoice<MerchantMedia> mediaSelect;
	private MerchantMedia selectedLogo;
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();
	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();
	
	public MerchantLocations()
    {
        super(new ResourceModel("title"), new ResourceModel("summary"));
        // TODO add a country list
        countries.add("USA");
        
    }
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		setDefaultModel(new CompoundPropertyModel<Merchant>((IModel<Merchant>) getDefaultModel()));
		
		final Merchant merchant = (Merchant) getDefaultModelObject();
		
		// TODO add logo image
		// TODO Dependent on a change to the merchant
		final MerchantMediaListModel mediaListModel = new MerchantMediaListModel();
		mediaListModel.setMerchantId(merchant.getId());
		mediaListModel.setMediaType(MediaType.MERCHANT_LOGO);
		myLogoChoices = mediaListModel.getObject();
		ChoiceRenderer<MerchantMedia> cr = new ChoiceRenderer<MerchantMedia>("mediaName", "mediaUrl");
		mediaSelect = new DropDownChoice<MerchantMedia>("logoSelect",new PropertyModel<MerchantMedia>(this,"selectedLogo"), mediaListModel, cr); 
		addOrReplace(mediaSelect.setOutputMarkupId(true));
		
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
				
				MerchantMedia merchantLogo = domainFactory.newMedia(merchant.getId(), url, MediaType.MERCHANT_LOGO);
				if (merchant.getId() != null)
				{
					saveMedia(merchantLogo);
				} 
				else
				{
					unsavedMedia.add(merchantLogo);
				}
				
				// TODO this is a little wacky... need to straighten it out.
				myLogoChoices.add(merchantLogo);
				mediaListModel.setObject(myLogoChoices);
				mediaSelect.setChoices(mediaListModel);
				selectedLogo = merchantLogo;

				target.add(mediaSelect);
			}
			
			@Override
			public void renderHead(Component component, IHeaderResponse response) {
				super.renderHead(component, response);
				
				PackageTextTemplate ptt = new PackageTextTemplate( MerchantLocations.class, "MerchantLocations.js" );

				Map<String, Object> map = new HashMap<String, Object>();
				map.put( "callbackUrl", getCallbackUrl().toString() );
				
				response.render(JavaScriptHeaderItem.forScript(ptt.asString(map), "logoupload"));
			}
			
		});
		
		WebMarkupContainer locationPanel = new WebMarkupContainer("locationPanel");
		addOrReplace(locationPanel);

		final TextField<String> addr1 = new TextField<String>("primaryLocation.address.address1");
		locationPanel.add(addr1.setRequired(true));

		final TextField<String> addr2 = new TextField<String>("primaryLocation.address.address2");
		locationPanel.add(addr2);

		final TextField<String> city = new TextField<String>("primaryLocation.address.city");
		locationPanel.add(city.setRequired(true));

		final StateSelect state = new StateSelect("primaryLocation.address.stateProvinceCounty",
				new PropertyModel<StateOption>(this, "stateOption"));
		locationPanel.add(state.setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.address.zip").setRequired(true));

		locationPanel.add(new DropDownChoice<String>("primaryLocation.address.country",countries).setRequired(true));

		locationPanel.add(new TextField<String>("primaryLocation.locationName"));

		
		WebMarkupContainer contactPanel = new WebMarkupContainer("contactPanel");
		addOrReplace(contactPanel);
		
		contactPanel.add(new TextField<String>("primaryLocation.phone").setRequired(true));

		contactPanel.add(new TextField<String>("primaryLocation.email").setRequired(true).add(
				EmailAddressValidator.getInstance()));

		contactPanel.add(new TextField<String>("primaryLocation.websiteUrl").add(new UrlValidator()));

	}
	
	public Image getLogo() {
		if (logo==null)
		{
			logo= new ImageImpl("temp image","404.png");
		}
		return logo;
	}

	public void setLogo(Image logo) {
		this.logo = logo;
	}
	
	public StateOption getStateOption()
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		if (merch.getPrimaryLocation().getAddress().getStateProvinceCounty() == null)
		{
			return null;
		}

		return StateSelect.getStateOptionByCode(merch.getPrimaryLocation().getAddress()
				.getStateProvinceCounty());

	}

	public void setStateOption(final StateOption stateOption)
	{
		final Merchant merch = (Merchant) getDefaultModelObject();
		merch.getPrimaryLocation().getAddress().setStateProvinceCounty(stateOption.getCode());
	}

	public MerchantMedia getSelectedLogo() {
		return selectedLogo;
	}

	public void setSelectedLogo(MerchantMedia selectedMedia) {
		this.selectedLogo = selectedMedia;
	}

	/*
	 * Save the state of the Merchant.
	 */
	@Override
	public void applyState() {
		super.applyState();
		
		final Merchant merch = (Merchant) getDefaultModelObject();
		
		if (merch.getId() == null)
		{
			try 
			{
				// New users need to be saved before we can save the logos
				taloolService.save(merch);
				StringBuilder sb = new StringBuilder("Saved Merchant with id:");
				LOG.debug(sb.append(merch.getId()).toString());
			}
			catch (ServiceException se)
			{
				LOG.error("failed to save new merchant:",se);
			}
			catch (Exception e)
			{
				// TODO ? same merchant uploading the same file ?
				LOG.error("random-ass-exception saving new merchant:",e);
			}
		}
		
		for (MerchantMedia media:unsavedMedia)
		{
			media.setMerchantId(merch.getId());
			saveMedia(media);
		}
		
		// get the selected MerchantMedia and add it to the location
		// TODO the location needs to store a MerchantMedia object, not the url
		// TODO don't save it if it's the default
		if (selectedLogo != null)
		{
			merch.getPrimaryLocation().setLogoUrl(selectedLogo.getMediaUrl());
		}
	}
	
	private void saveMedia(MerchantMedia media)
	{
		try 
		{
			taloolService.saveMerchantMedia(media);
		}
		catch (ServiceException se)
		{
			LOG.error("failed to save media:",se);
		}
		catch (Exception e)
		{
			LOG.error("random-ass-exception saving new merchant media:",e);
		}
	}
}

