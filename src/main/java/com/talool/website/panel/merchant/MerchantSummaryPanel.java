package com.talool.website.panel.merchant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.Tag;
import com.talool.website.component.StaticImage;
import com.talool.website.models.DealListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;

public class MerchantSummaryPanel extends BaseTabPanel {

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantSummaryPanel.class);
	
	private UUID _merchantId;
	
	private Merchant merchant;
	
	private MerchantWizard wizard;
	
	private String categoryLabel;
	private String tagsLabel;
	private String logoUrl;
	
	private List<String> warnings;
	
	public MerchantSummaryPanel(String id, PageParameters parameters) {
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
		setPanelModel();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final WebMarkupContainer warningContainer = new WebMarkupContainer("warnings");
		container.add(warningContainer.setOutputMarkupId(true));
		final ListView<String> warningList = new ListView<String>("warningRptr", new PropertyModel<List<String>>(this,"warnings"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("warning",item.getModelObject()));
			}

		};
		warningContainer.add(warningList);
		warningContainer.setVisible(!warnings.isEmpty());
		
		container.add(new AjaxLink<Void>("editLink")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				wizard.setModelObject(new MerchantModel(_merchantId, true).getObject());
				wizard.open(target);
			}
		});
		
		container.add(new Label("categoryLabel",new PropertyModel<String>(this,"categoryLabel")));
		container.add(new Label("tagsLabel",new PropertyModel<String>(this,"tagsLabel")));
		
		container.add(new StaticImage("logo", false, new PropertyModel<String>(this, "logoUrl")));
		
		final MapPreview mapPreview = new MapPreview("mapPreview", merchant);
		container.add(mapPreview);
		
		final MerchantPreview merchantPreview = new MerchantPreview("merchantPreview", merchant);
		container.add(merchantPreview);
		
		// Wizard
		wizard = new MerchantWizard("wiz", "Merchant Wizard", MerchantWizardMode.MERCHANT)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				setPanelModel();
				merchantPreview.init(merchant);
				mapPreview.init(merchant);
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
		
		// hide the action button
		final BasePage page = (BasePage) this.getPage();
		page.getActionLink().add(new AttributeModifier("class","hide"));
	}



	@Override
	public String getActionLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void setPanelModel()
	{
		warnings = new ArrayList<String>();
		
		merchant = new MerchantModel(_merchantId, true).getObject();
		
		categoryLabel = merchant.getCategory().getName();
		
		StringBuilder sb = new StringBuilder();
		Iterator<Tag> tags = merchant.getTags().iterator();
		while (tags.hasNext())
		{
			sb.append(tags.next().getName());
			if (tags.hasNext())
			{
				sb.append(", ");
			}
		}
		tagsLabel = sb.toString();
		
		MerchantLocation loc = merchant.getPrimaryLocation();
		if (loc.getLogo() ==  null)
		{
			logoUrl = "/img/000.png";
		}
		else
		{
			logoUrl = loc.getLogo().getMediaUrl();
		}
		
		// check for location images and logos
		for (MerchantLocation location:merchant.getLocations())
		{
			if (location.getMerchantImage()==null)
			{
				warnings.add("A location for this merchant is missing an image.");
			}
			
			if (location.getLogo()==null)
			{
				warnings.add("A location for this merchant is missing a logo.");
			}
		}
		
		// check for accounts
		if (merchant.getMerchantAccounts().isEmpty())
		{
			warnings.add("There are no accounts for this merchant, so they can't receive redemption information.");
		}
		
		// check for deal images
		DealListModel model = new DealListModel();
		model.setMerchantId(_merchantId);
		for (Deal deal:model.getObject())
		{
			if (deal.getImage()==null)
			{
				warnings.add("A deal for this merchant is missing an image.");
			}
		}
		
		
		
	}

	

}
