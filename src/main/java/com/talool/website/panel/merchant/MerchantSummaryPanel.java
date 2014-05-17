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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.core.MerchantLocation;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.website.component.PropertyComboBox;
import com.talool.website.component.StaticImage;
import com.talool.website.models.DealListModel;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;
import com.talool.website.util.SessionUtils;

public class MerchantSummaryPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(MerchantSummaryPanel.class);

	private UUID _merchantId;

	private Merchant merchant;

	private MerchantWizard wizard;

	private String categoryLabel;
	private String tagsLabel;
	private String logoUrl;
	private List<KeyValue> keyValues;

	private List<String> wizardWarnings;
	private List<String> dealWarnings;
	
	private boolean isOwnerView = false;

	public MerchantSummaryPanel(String id, PageParameters parameters)
	{
		super(id);
		_merchantId = UUID.fromString(parameters.get("id").toString());
		isOwnerView = _merchantId.equals(SessionUtils.getSession().getMerchantAccount().getMerchant().getId());
		
		setPanelModel();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final BasePage page = (BasePage) getPage();

		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		final WebMarkupContainer actionBox = new WebMarkupContainer("actionBox");
		container.add(actionBox.setVisible(!isOwnerView).setOutputMarkupId(true));

		final WebMarkupContainer warningContainer = new WebMarkupContainer("warnings");
		container.add(warningContainer.setOutputMarkupId(true));
		warningContainer.setVisible(!wizardWarnings.isEmpty() || !dealWarnings.isEmpty());
		
		final WebMarkupContainer wizWarningContainer = new WebMarkupContainer("wizardWarnings");
		wizWarningContainer.add(new AjaxLink<Void>("edit")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				openWizard(target);
			}
		});
		final ListView<String> wizWarningList = new ListView<String>("wizWarningRptr", new PropertyModel<List<String>>(this, "wizardWarnings"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("wizWarning", item.getModelObject()));
			}

		};
		wizWarningContainer.add(wizWarningList);
		warningContainer.add(wizWarningContainer.setVisible(!wizardWarnings.isEmpty()));
		
		final WebMarkupContainer dealWarningContainer = new WebMarkupContainer("dealWarnings");
		final ListView<String> dealWarningList = new ListView<String>("dealWarningRptr", new PropertyModel<List<String>>(this, "dealWarnings"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("dealWarning", item.getModelObject()));
			}

		};
		dealWarningContainer.add(dealWarningList);
		warningContainer.add(dealWarningContainer.setVisible(!dealWarnings.isEmpty()));
		
		actionBox.add(new AjaxLink<Void>("editLink")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				openWizard(target);
			}
		});

		container.add(new Label("categoryLabel", new PropertyModel<String>(this, "categoryLabel")));
		container.add(new Label("tagsLabel", new PropertyModel<String>(this, "tagsLabel")));

		container.add(new StaticImage("logo", false, new PropertyModel<String>(this, "logoUrl")));

		final ListView<KeyValue> propteryList = new ListView<KeyValue>("propertyRptr", new PropertyModel<List<KeyValue>>(this, "keyValues"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<KeyValue> item)
			{
				KeyValue prop = item.getModelObject();
				item.add(new Label("pKey", prop.key));
				item.add(new Label("pVal", prop.value));
			}

		};
		container.add(propteryList.setVisible(page.isSuperUser));

		final MapPreview mapPreview = new MapPreview("mapPreview", merchant);
		container.add(mapPreview);

		final MerchantPreview merchantPreview = new MerchantPreview("merchantPreview", merchant);
		container.add(merchantPreview);

		final PropertyComboBox comboBox = new PropertyComboBox("comboBox",
				Model.of(merchant.getProperties()), Merchant.class)
		{

			private static final long serialVersionUID = 7609398573563991376L;

			@Override
			public void onPropertySave(Properties props,
					AjaxRequestTarget target)
			{
				try
				{
					ServiceFactory.get().getTaloolService().saveProperties(merchant, props);
					LOG.info(merchant.getProperties().dumpProperties());

					BasePage page = (BasePage) getPage();
					target.add(page.feedback);

					setPanelModel();
					target.add(container);
				}
				catch (ServiceException e)
				{
					LOG.error("failed to merge merchant after saving properties.", e);
				}

			}

		};
		actionBox.add(comboBox.setVisible(page.isSuperUser));

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
				
				warningContainer.setVisible(!wizardWarnings.isEmpty() || !dealWarnings.isEmpty());
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));

		// hide the action button
		if (!isOwnerView)
		{
			page.getActionLink().add(new AttributeModifier("class", "hide"));
		}
		else
		{
			// override the action button
			AjaxLink<Void> actionLink = new AjaxLink<Void>("actionLink")
			{

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target)
				{
					openWizard(target);
				}
			};
			page.setActionLink(actionLink);
			Label actionLabel = new Label("actionLabel", getActionLabel());
			actionLabel.setOutputMarkupId(true);
			actionLink.add(actionLabel);
			actionLink.setOutputMarkupId(true);
		}
	}
	
	private void openWizard(AjaxRequestTarget target)
	{
		getSession().getFeedbackMessages().clear();
		wizard.setModelObject(new MerchantModel(_merchantId, true).getObject());
		wizard.open(target);
	}

	@Override
	public String getActionLabel()
	{
		return "Edit Merchant Profile";
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return null;
	}

	private void setPanelModel()
	{
		wizardWarnings = new ArrayList<String>();
		dealWarnings = new ArrayList<String>();
		
		merchant = new MerchantModel(_merchantId, true).getObject();

		if (merchant.getCategory() == null)
		{
			wizardWarnings.add("Select a category so customers can find you in the app.");
			categoryLabel = "";
		}
		else
		{
			categoryLabel = merchant.getCategory().getName();
		}
		

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
		if (loc.getLogo() == null)
		{
			logoUrl = "/img/000.png";
		}
		else
		{
			logoUrl = loc.getLogo().getMediaUrl();
		}

		// check for location images and logos
		for (MerchantLocation location : merchant.getLocations())
		{
			if (location.getMerchantImage() == null)
			{
				wizardWarnings.add("Select an image for your location.");
			}

			if (location.getLogo() == null)
			{
				wizardWarnings.add("Upload the logo for your location.");
			}
		}

		// check for deal images
		DealListModel model = new DealListModel();
		model.setMerchantId(_merchantId);
		for (Deal deal : model.getObject())
		{
			if (deal.getImage() == null)
			{
				dealWarnings.add("One of your deals is missing an image.");
			}
		}

		keyValues = KeyValue.getKeyValues(merchant.getProperties());

	}

}
