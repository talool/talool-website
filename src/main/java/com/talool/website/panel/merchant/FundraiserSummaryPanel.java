package com.talool.website.panel.merchant;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.Merchant;
import com.talool.core.MerchantAccount;
import com.talool.core.MerchantCode;
import com.talool.core.MerchantCodeGroup;
import com.talool.core.service.ServiceException;
import com.talool.domain.Properties;
import com.talool.service.ServiceFactory;
import com.talool.utils.KeyValue;
import com.talool.website.behaviors.AJAXDownload;
import com.talool.website.component.PropertyComboBox;
import com.talool.website.models.MerchantModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.analytics.CubismHorizon;
import com.talool.website.panel.analytics.CubismHorizonFactory;
import com.talool.website.panel.analytics.CubismPanel;
import com.talool.website.panel.merchant.wizard.MerchantWizard;
import com.talool.website.panel.merchant.wizard.MerchantWizard.MerchantWizardMode;
import com.talool.website.util.PermissionUtils;
import com.talool.website.util.SessionUtils;

public class FundraiserSummaryPanel extends BaseTabPanel
{

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(FundraiserSummaryPanel.class);

	private UUID _fundraiserId;
	private UUID _publisherId;

	private Merchant fundraiser;

	private MerchantWizard wizard;
	private int percentage;
	private List<KeyValue> keyValues;

	private List<String> warnings;

	private int downloadCodeCount;
	private MerchantCodeGroup merchantCodeGrp;

	public FundraiserSummaryPanel(String id, PageParameters parameters)
	{
		super(id);
		_fundraiserId = UUID.fromString(parameters.get("id").toString());
		_publisherId = UUID.fromString(parameters.get("pid").toString());
		setPanelModel();
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		final BasePage page = (BasePage) getPage();

		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));

		final WebMarkupContainer warningContainer = new WebMarkupContainer("warnings");
		container.add(warningContainer.setOutputMarkupId(true));
		final ListView<String> warningList = new ListView<String>("warningRptr", new PropertyModel<List<String>>(this, "warnings"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item)
			{
				item.add(new Label("warning", item.getModelObject()));
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
				wizard.setModelObject(new MerchantModel(_fundraiserId, true).getObject());
				wizard.open(target);
			}
		});

		container.add(new Label("percentage", new PropertyModel<Integer>(this, "percentage")));

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

		final PropertyComboBox comboBox = new PropertyComboBox("comboBox",
				Model.of(fundraiser.getProperties()), Merchant.class)
		{

			private static final long serialVersionUID = 7609398573563991376L;

			@Override
			public void onPropertySave(Properties props,
					AjaxRequestTarget target)
			{
				try
				{
					ServiceFactory.get().getTaloolService().merge(fundraiser);
					LOG.info(fundraiser.getProperties().dumpProperties());

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
		container.add(comboBox.setVisible(page.isSuperUser));

		final AJAXDownload download = new AJAXDownload()
		{

			private static final long serialVersionUID = 3028684784843907550L;

			@Override
			protected String getFileName()
			{
				return merchantCodeGrp.getCodeGroupTitle() + ".txt";
			}

			@Override
			protected IResourceStream getResourceStream()
			{
				// download the new codes
				final int finalCount = downloadCodeCount;
				IResourceStream resourceStream = new AbstractResourceStreamWriter()
				{
					private static final long serialVersionUID = 659665452240222410L;

					@Override
					public void write(OutputStream output)
					{
						try
						{
							PrintWriter writer = new PrintWriter(output);

							Set<MerchantCode> codes = merchantCodeGrp.getCodes();
							for (final MerchantCode code : codes)
							{
								writer.println(code.getCode());
							}

							writer.close();
						}
						catch (Exception e)
						{
							LOG.error("Problem writing codes: " + e.getLocalizedMessage(), e);
						}
					}

					@Override
					public String getContentType()
					{
						return "application/text";
					}
				};
				return resourceStream;
			}

		};
		container.add(download);

		final IndicatingAjaxLink<Void> codesLink = new IndicatingAjaxLink<Void>("codeLink")
		{
			private static final long serialVersionUID = 268692101349122303L;
			private final int MAX_CODES_IN_ONE_CREATION = 10000;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();

				// Extract the code count parameter from RequestCycle
				final Request request = RequestCycle.get().getRequest();
				final String jsCodeCountString = request.getRequestParameters()
						.getParameterValue("codeCount").toString("");

				try
				{
					downloadCodeCount = Integer.parseInt(jsCodeCountString);
					if (downloadCodeCount > MAX_CODES_IN_ONE_CREATION)
					{
						SessionUtils.errorMessage("Please enter a number smaller than " + MAX_CODES_IN_ONE_CREATION + ".");
						downloadCodeCount = 0;
					}
				}
				catch (Exception e)
				{
					SessionUtils.errorMessage("Please enter numbers only.");
					downloadCodeCount = 0;
				}

				if (downloadCodeCount > 0)
				{
					// generate the codes and start the download
					try
					{
						MerchantAccount merchantAccount = SessionUtils.getSession().getMerchantAccount();
						StringBuilder title = new StringBuilder();
						title.append(fundraiser.getName()).append(" Tracking Codes");
						String notes = "";
						merchantCodeGrp = taloolService.createMerchantCodeGroup(fundraiser,
								merchantAccount.getId(), _publisherId,
								title.toString(), notes, (short) downloadCodeCount);

						download.initiate(target);
						Session.get().success("Codes created and download started.");
					}
					catch (ServiceException e)
					{
						Session.get().error("Problem creating codes");
						LOG.error("Problem creating codes: " + e.getLocalizedMessage());
					}
				}

				BasePage page = (BasePage) getPage();
				target.add(page.feedback);

			}

			@Override
			protected void updateAjaxAttributes(
					AjaxRequestAttributes attributes)
			{
				super.updateAjaxAttributes(attributes);

				List<CharSequence> urlArgumentMethods = attributes.getDynamicExtraParameters();
				urlArgumentMethods.add("return {'codeCount': prompt('How many tracking codes would you like to generate?')};");

			}

		};
		container.add(codesLink.setOutputMarkupId(true));

		// Wizard
		wizard = new MerchantWizard("wiz", "Merchant Wizard", MerchantWizardMode.FUNDRAISER)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				setPanelModel();
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));

		// hide the action button
		page.getActionLink().add(new AttributeModifier("class", "hide"));
	}

	@Override
	public String getActionLabel()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private void setPanelModel()
	{
		warnings = new ArrayList<String>();

		fundraiser = new MerchantModel(_fundraiserId, true).getObject();

		// check for accounts
		if (fundraiser.getMerchantAccounts().isEmpty())
		{
			warnings.add("There are no accounts for this fundraiser, so they can't receive sales information.");
		}

		keyValues = KeyValue.getKeyValues(fundraiser.getProperties());
		percentage = fundraiser.getProperties().getAsInt(KeyValue.percentage);

	}

}
