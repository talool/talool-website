package com.talool.website.panel.merchant.definition;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.wicket.datetime.DateConverter;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.talool.core.Deal;

public class DealPreview extends Panel {
	public String title, summary, details, code, imageUrl;
	public String expires;
	public Label titleLabel, summaryLabel, detailsLabel, imageLabel;
	public DateLabel expiresLabel;
	
	private static final long serialVersionUID = 7091914958360426987L;

	public DealPreview(String id, Deal deal) {
		super(id);
		if (deal.getId() != null) {
			title = deal.getTitle();
			summary = deal.getSummary();
			details = deal.geDetails();
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			expires = formatter.format(deal.getExpires());
			code = deal.getCode();
			imageUrl = deal.getImageUrl();
		}

	}
	
	@Override
	protected void onInitialize()
	{
		super.onInitialize();
		
		add(titleLabel = new Label("title", new PropertyModel<String>(this,"title")));
		titleLabel.setOutputMarkupId(true);
		add(summaryLabel = new Label("summary", new PropertyModel<String>(this,"summary")));
		summaryLabel.setOutputMarkupId(true);
		add(detailsLabel = new Label("details", new PropertyModel<String>(this,"details")));
		detailsLabel.setOutputMarkupId(true);
		
		DateConverter converter = new PatternDateConverter("MM/dd/yyyy", false);
		add(expiresLabel = new DateLabel("expires", new PropertyModel<Date>(this,"expires"), converter));
		expiresLabel.setOutputMarkupId(true);
		
		//add(codeLabel = new Label("code", new PropertyModel<String>(this,"title")));
		//codeLabel.setOutputMarkupId(true);
		add(imageLabel = new Label("imageUrl", new PropertyModel<String>(this,"imageUrl")));
		imageLabel.setOutputMarkupId(true);
	}

}
