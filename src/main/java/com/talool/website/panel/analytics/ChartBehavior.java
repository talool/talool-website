package com.talool.website.panel.analytics;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.util.template.PackageTextTemplate;

import com.talool.website.models.MetricListModel;
import com.talool.website.models.MetricListModel.Metric;

public class ChartBehavior extends Behavior {

	private static final long serialVersionUID = 3989069660334116805L;
	private PackageTextTemplate jsTmpl;
	private MetricListModel model;
	
	public ChartBehavior(MetricListModel model)
    {
    	super();
    	this.model = model;
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
        setUploadConfig(component, response);
    }
    
    public void setUploadConfig(Component component, IHeaderResponse response){
    	jsTmpl = new PackageTextTemplate(ChartBehavior.class, "chartConfig.js");
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.put("componentMarkupId", component.getMarkupId());
        variables.put("labels",getLabelsForConfig());
        variables.put("data",getDataForConfig());

        String s = jsTmpl.asString(variables);
        response.render(JavaScriptHeaderItem.forScript(s, "js"+component.getMarkupId()));

    }
    
    private String getLabelsForConfig()
    {
    	// TODO this should be based on the model.getChartRange()
    	return "['August', 'September', 'October', 'November', 'December', 'January']";
    }
    
    private String getDataForConfig()
    {	
    	JSONArray json = new JSONArray();
    	
    	int lineIndex = 0;
    	for (Metric metric : model.getObject()) {
    		JSONObject m = getJSONObjectForMetric(metric, lineIndex);
			json.put(m);
			lineIndex++;
			for (Metric slice:metric.slices)
			{
				JSONObject s = getJSONObjectForMetric(slice, lineIndex);
				json.put(s);
				lineIndex++;
			}
    	}
    	
    	return json.toString();
    }
    
    private JSONObject getJSONObjectForMetric(Metric metric, int lineIndex)
    {
    	JSONObject m = new JSONObject();
    	try
    	{
    		m.put("fillColor", model.getJsonColor(lineIndex,false));
    		m.put("strokeColor", model.getJsonColor(lineIndex,true));
    		m.put("pointColor", model.getJsonColor(lineIndex,true));
    		m.put("pointStrokeColor", "#fff");
    		JSONArray data = new JSONArray();
    		for (Integer i:metric.data)
    		{
    			data.put(i);
    		}
    		m.put("data", data);
    	}
    	catch (JSONException e)
    	{
    		// log exception
    	}
		return m;
    }
    
    
}
