package com.talool.website.pages.error;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class BaseErrorPage extends WebPage {

	private static final long serialVersionUID = 1L;
	 
    public BaseErrorPage() {
    	add(new FeedbackPanel("feedback"));
    }
 
    @Override
    public boolean isVersioned() {
        return false;
    }
 
    @Override
    public boolean isErrorPage() {
        return true;
    }
}
