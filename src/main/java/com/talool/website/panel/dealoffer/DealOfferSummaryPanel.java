package com.talool.website.panel.dealoffer;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DealOffer;
import com.talool.core.DealType;
import com.talool.core.MerchantLocation;
import com.talool.core.service.ServiceException;
import com.talool.stats.DealOfferMetrics;
import com.talool.stats.DealOfferMetrics.MetricType;
import com.talool.website.behaviors.AJAXDownload;
import com.talool.website.models.DealOfferModel;
import com.talool.website.pages.BasePage;
import com.talool.website.panel.BaseTabPanel;
import com.talool.website.panel.SubmitCallBack;
import com.talool.website.panel.dealoffer.wizard.DealOfferWizard;
import com.talool.website.util.SessionUtils;
import com.vividsolutions.jts.geom.Geometry;

public class DealOfferSummaryPanel extends BaseTabPanel {

	private static final long serialVersionUID = 2170124491668826388L;
	private static final Logger LOG = LoggerFactory.getLogger(DealOfferSummaryPanel.class);
	
	private UUID _dealOfferId;
	
	private DealOffer offer;
	private MerchantLocation loc;
	private Map<String, Long> metrics;
	
	private int downloadCodeCount;
	
	private DealOfferWizard wizard;
	
	private String merchantLabel;
	private String priceLabel;
	private String locationLabel;
	private String expiresLabel;
	private Long merchantCount;
	private Long dealCount;
	
	public DealOfferSummaryPanel(String id, PageParameters parameters) {
		super(id);
		_dealOfferId = UUID.fromString(parameters.get("id").toString());
		setPanelModel();
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		final WebMarkupContainer container = new WebMarkupContainer("container");
		add(container.setOutputMarkupId(true));
		
		container.add(new AjaxLink<Void>("editLink")
		{
			private static final long serialVersionUID = 268692101349122303L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				wizard.setModelObject(new DealOfferModel(_dealOfferId).getObject());
				wizard.open(target);
			}
		});
		
		container.add(new Label("merchantName",new PropertyModel<String>(this,"merchantLabel")));
		container.add(new Label("price", new PropertyModel<String>(this,"priceLabel")));
		container.add(new Label("loc", new PropertyModel<String>(this,"locationLabel")));
		container.add(new Label("exp", new PropertyModel<String>(this,"expiresLabel")));
		container.add(new Label("merchantCount", new PropertyModel<Long>(this,"merchantCount")));
		container.add(new Label("dealCount",new PropertyModel<Long>(this,"dealCount")));
		
		final FindDealsPreview findDealsPreview = new FindDealsPreview("findDealsPreview", offer);
		container.add(findDealsPreview);
		
		final DealOfferPreview offerPreview = new DealOfferPreview("dealOfferPreview", offer);
		container.add(offerPreview);
		
		final AJAXDownload download = new AJAXDownload()
		{

			private static final long serialVersionUID = 3028684784843907550L;

			@Override
			protected String getFileName() {
				return offer.getTitle() + " Access Codes.txt";
			}

			@Override
			protected IResourceStream getResourceStream() {
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

							// TODO we should have a method that only returns the most recent X codes
							List<String> codes = taloolService.getActivationCodes(_dealOfferId);
							int lastIndex = codes.size();
							List<String> recentCodes = codes.subList(lastIndex-finalCount, lastIndex);

							for (final String code : recentCodes)
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
				return resourceStream;
			}
			
		};
		container.add(download);
		
		final IndicatingAjaxLink<Void> codesLink = new IndicatingAjaxLink<Void>("codesLink")
		{
			private static final long serialVersionUID = 268692101349122303L;
			private final int MAX_CODES_IN_ONE_CREATION = 10000;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				getSession().getFeedbackMessages().clear();
				
				//Extract the code count parameter from RequestCycle
	            final Request request = RequestCycle.get().getRequest();
	            final String jsCodeCountString = request.getRequestParameters()
	                            .getParameterValue("codeCount").toString("");
	            
	            try
	            {
	            	downloadCodeCount = Integer.parseInt(jsCodeCountString);
	            	if (downloadCodeCount > MAX_CODES_IN_ONE_CREATION)
	            	{
	            		SessionUtils.errorMessage("Please enter a number smaller than "+MAX_CODES_IN_ONE_CREATION+".");
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
						taloolService.createActivationCodes(_dealOfferId, downloadCodeCount);
						download.initiate(target);
						Session.get().success("Codes created and download started.");
					}
					catch (ServiceException e)
					{
						Session.get().error("Problem creating codes");
						LOG.error("Problem creating codes: " + e.getLocalizedMessage());
					}
	            }
	            
	            BasePage page = (BasePage)getPage();
	            target.add(page.feedback);

			}
			
			@Override
			protected void updateAjaxAttributes(
					AjaxRequestAttributes attributes) {
				super.updateAjaxAttributes(attributes);
				
				List<CharSequence> urlArgumentMethods = attributes.getDynamicExtraParameters();
                urlArgumentMethods.add("return {'codeCount': prompt('How many access codes would you like to generate?')};");
				
			}
			
			
		};
		container.add(codesLink.setOutputMarkupId(true));
		codesLink.setEnabled(offer.isActive());
		
		container.add(new DealOfferPublishToggle("toggle", offer, loc, metrics){

			private static final long serialVersionUID = 1L;

			@Override
			public void onPublishToggle(AjaxRequestTarget target) {
				setPanelModel();
				codesLink.setEnabled(offer.isActive());
				target.add(codesLink);
			}
			
		});
		
		// Wizard
		wizard = new DealOfferWizard("wiz", "Book Wizard")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish(AjaxRequestTarget target)
			{
				super.onFinish(target);
				// refresh the list after a deal is edited
				setPanelModel();
				findDealsPreview.init(offer);
				offerPreview.init(offer);
				target.add(container);
			}
		};
		addOrReplace(wizard.setOutputMarkupId(true));
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
	
	private boolean isKirkeBook(DealOffer offer) {
		return offer.getType().equals(DealType.KIRKE_BOOK.toString());
	}

	private void setPanelModel()
	{
		offer = new DealOfferModel(_dealOfferId).getObject();
		
		try
		{
			Map<UUID, DealOfferMetrics> metricsMap = taloolService.getDealOfferMetrics();
			if (metricsMap != null && !metricsMap.isEmpty())
			{
				DealOfferMetrics m = metricsMap.get(_dealOfferId);
				if (m==null)
				{
					merchantCount = 0L;
					dealCount = 0L;
				}
				else
				{
					metrics = m.getLongMetrics();
					merchantCount = metrics.get(MetricType.TotalMerchants.toString());
					dealCount = metrics.get(MetricType.TotalDeals.toString());
				}
			}
		} 
		catch (ServiceException se)
		{
			LOG.error("Failed to get offer.", se);
		}
		
		Geometry geo = offer.getGeometry();
		Set<MerchantLocation> locs = offer.getMerchant().getLocations();
		if (geo != null)
		{
			for (MerchantLocation l : locs)
			{
				if (l.getGeometry() != null && l.getGeometry().equalsExact(geo))
				{
					loc = l;
					break;
				}
			}
		}
		
		merchantLabel = offer.getMerchant().getName();
		if (loc == null || isKirkeBook(offer))
		{
			locationLabel = "";
		}
		else if (!StringUtils.isEmpty(loc.getLocationName()))
		{
			locationLabel = loc.getLocationName();
		}
		else
		{
			locationLabel = loc.getNiceCityState();
		}
		
		if (isKirkeBook(offer))
		{
			priceLabel = "";
		} 
		else
		{
			NumberFormat formatter = NumberFormat.getCurrencyInstance();
			priceLabel = formatter.format(offer.getPrice());
		}
		
		if (offer.getExpires() != null)
		{
			DateTime localDate = new DateTime(offer.getExpires().getTime());
			DateTimeFormatter dateformatter = DateTimeFormat.forPattern("MMM d, yyyy");
			expiresLabel = dateformatter.print(localDate);
		}
		else
		{
			expiresLabel = "";
		}
		
		
	}

	

}
