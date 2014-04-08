package com.talool.website.panel.analytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.template.PackageTextTemplate;

public class CubismBehavior extends Behavior {

	private static final long serialVersionUID = 3989069660334116805L;
	private PackageTextTemplate jsTmpl;
	private List<CubismHorizon> horizons;
	private double chartStep;
	
	public CubismBehavior(List<CubismHorizon> horizons, IModel<CubismStep> model)
    {
    	super();
    	this.horizons = horizons;
    	
    	chartStep = model.getObject().step;
    }

	/**
     * Configures the connected component to render its markup id
     * because it is needed to initialize the JavaScript widget.
     * @param component
     */
    @Override
    public void bind(Component component) {
        super.bind(component);
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        setChartConfig(component, response);
    }
    
    public void setChartConfig(Component component, IHeaderResponse response)
    {	
        response.render(JavaScriptHeaderItem.forScript(getChartConfig(component), "js"+component.getMarkupId()));
    }
    
    public String getChartConfig(Component component)
    {
    	jsTmpl = new PackageTextTemplate(CubismBehavior.class, "cubismConfig.js");
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.put("componentMarkupId", component.getMarkupId());
        variables.put("data",getJSONArrayForMetrics());
        variables.put("chartStep",chartStep);

        return jsTmpl.asString(variables);
    }
    
    private JSONArray getJSONArrayForMetrics()
    {
    	JSONArray data = new JSONArray();
    	try
    	{
    		for (CubismHorizon h:horizons)
    		{
    			JSONObject horizonObject = new JSONObject();
    			horizonObject.put("title",h.getTitle());
    			JSONArray metricArray = new JSONArray();
    			for (GraphiteMetric m:h.getMetrics())
        		{
    				JSONObject metricObject = new JSONObject();
    				metricObject.put("title",m.getTitle());
    				metricObject.put("definition", m.getDefinition());
    				metricArray.put(metricObject);
        		}
    			horizonObject.put("metrics", metricArray);
    			data.put(horizonObject);
    		}
    		
    	}
    	catch (JSONException e)
    	{
    		// log exception
    	}
		return data;
    }
    
    
}
