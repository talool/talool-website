package com.talool.website.panel.merchant;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import com.talool.core.ActivationSummary;
import com.talool.core.service.ServiceException;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.merchant.definition.MerchantAccountPanel;
import com.talool.website.resources.DealOfferCodeResource;

/**
 * This is a temporary utility panel that allows downloading and creation of
 * unique codes in deal offers.
 * 
 * Note: Please be ware, if this is determined to not be temporary concider
 * better security around creating of codes!
 * 
 * @author clintz
 * 
 */
public class BookCodesPanel extends BaseTabPanel
{
	private static final Logger LOG = Logger.getLogger(BookCodesPanel.class);
	private static final long serialVersionUID = 3634980968241854373L;

	private static int MAX_CODES_IN_ONE_CREATION = 10000;

	private UUID merchantId;

	private class ActivationSummaryModel extends LoadableDetachableModel<List<ActivationSummary>>
	{
		private static final long serialVersionUID = -3244765952093420052L;

		@Override
		protected List<ActivationSummary> load()
		{
			List<ActivationSummary> summaries = null;

			try
			{
				summaries = taloolService.getActivationSummaries(merchantId);
			}
			catch (ServiceException e)
			{

				LOG.error("Problem getting activiation summaries: " + e.getLocalizedMessage(), e);
			}

			return summaries;
		}

	}

	public BookCodesPanel(String id, PageParameters parameters)
	{
		super(id);
		merchantId = UUID.fromString(parameters.get("id").toString());
	}

	@Override
	protected void onInitialize()
	{
		super.onInitialize();

		add(new ListView<ActivationSummary>("codeRptr", new ActivationSummaryModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<ActivationSummary> item)
			{
				final ActivationSummary summary = item.getModelObject();
				item.setDefaultModel(new CompoundPropertyModel<ActivationSummary>(summary));
				item.add(new Label("title"));
				item.add(new Label("totalCodes"));
				item.add(new Label("totalActivations"));

				if (item.getIndex() % 2 == 0)
				{
					item.add(new AttributeModifier("class", "odd-row-bg"));
				}

				Form<Void> form = new Form<Void>("generateCodesForm")
				{

					@Override
					protected void onSubmit()
					{
						int newCodesTotal = Integer.parseInt((String) get("newCodesTotal").getDefaultModelObject());
						if (newCodesTotal != 0 && newCodesTotal <= MAX_CODES_IN_ONE_CREATION)
						{
							try
							{
								taloolService.createActivationCodes(summary.getDealOfferId(), newCodesTotal);
							}
							catch (ServiceException e)
							{
								Session.get().error("Problem creating codes");
								LOG.error("Problem creating codes: " + e.getLocalizedMessage());
							}
						}

					}

					private static final long serialVersionUID = 883777399637896695L;

				};

				item.add(form);
				form.add(new TextField<Integer>("newCodesTotal", new Model<Integer>(0)));

				item.add(new Link<Void>("downloadCodesLink")
				{

					private static final long serialVersionUID = 4307406664103802161L;

					@Override
					public void onClick()
					{
						IResourceStream resourceStream = new AbstractResourceStreamWriter()
						{
							private static final long serialVersionUID = 659665452240222410L;

							@Override
							public void write(OutputStream output)
							{
								try
								{
									PrintWriter writer = new PrintWriter(output);

									List<String> codes = taloolService.getActivationCodes(summary.getDealOfferId());

									for (final String code : codes)
									{
										writer.println(code);
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

						getRequestCycle().scheduleRequestHandlerAfterCurrent(
								new DealOfferCodeResource(resourceStream, getNiceDownloadName(summary)));
					}
				}.setVisible(summary.getTotalCodes() > 0));
			}

		});

	}

	private static String getNiceDownloadName(final ActivationSummary summary)
	{
		final StringBuilder sb = new StringBuilder();

		sb.append(summary.getTitle().replaceAll(" ", "-"))
				.append("_codes_").append(summary.getTotalCodes()).append(".txt");

		return sb.toString();

	}

	@Override
	public Panel getNewDefinitionPanel(String contentId, SubmitCallBack callback)
	{
		return new MerchantAccountPanel(contentId, merchantId, callback);
	}

	@Override
	public String getActionLabel()
	{
		// TODO Auto-generated method stub
		return null;
	}

}