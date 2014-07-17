package com.talool.website.behaviors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.util.template.PackageTextTemplate;

public class CoBrandBehavior extends Behavior {

	private static final long serialVersionUID = 2831726536357678771L;

	private String cobrandName;
	
	public CoBrandBehavior(String name) {
		super();
		cobrandName = name;
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		CoBrandBehavior.setHeadResources(component, response);
        setUploadConfig(component, response);
	}
	
	public static void setHeadResources(Component component, IHeaderResponse response)
    {
    	response.render(CssHeaderItem.forReference(
                new CssResourceReference(CoBrandBehavior.class, "cobrand.css")));
    	
    	response.render(JavaScriptHeaderItem.forReference(
                component.getApplication().getJavaScriptLibrarySettings().getJQueryReference()));

    }
	
	public void setUploadConfig(Component component, IHeaderResponse response){
    	PackageTextTemplate jsTmpl = new PackageTextTemplate(CoBrandBehavior.class, "cobrand.js");
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.put("cobrandName", cobrandName);

        String s = jsTmpl.asString(variables);
        response.render(JavaScriptHeaderItem.forScript(s, "cobrand"));
        
        try {
        	jsTmpl.close();
        } catch (IOException e) {}
        
    }
}
