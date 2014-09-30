package com.talool.rest.resource;

import org.apache.wicket.request.http.WebResponse;
import org.wicketstuff.rest.contenthandling.json.objserialdeserial.GsonObjectSerialDeserial;
import org.wicketstuff.rest.contenthandling.json.webserialdeserial.JsonWebSerialDeserial;
import org.wicketstuff.rest.resource.AbstractRestResource;
import org.wicketstuff.rest.resource.MethodMappingInfo;

import com.talool.core.FactoryManager;
import com.talool.core.service.TaloolService;

public class ApiTaloolResource extends AbstractRestResource<JsonWebSerialDeserial> 
{
	private static final long serialVersionUID = 1L;
	
	protected transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	public ApiTaloolResource() {
		super(new JsonWebSerialDeserial(new GsonObjectSerialDeserial()));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onAfterMethodInvoked(MethodMappingInfo mappedMethod,
			Attributes attributes, Object result) {
		super.onAfterMethodInvoked(mappedMethod, attributes, result);
		
		((WebResponse) getCurrentWebResponse()).setHeader("Access-Control-Allow-Origin", "*" );
	}
	
	

}
