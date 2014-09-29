package com.talool.rest.resource;

import org.wicketstuff.rest.contenthandling.json.objserialdeserial.GsonObjectSerialDeserial;
import org.wicketstuff.rest.contenthandling.json.webserialdeserial.JsonWebSerialDeserial;
import org.wicketstuff.rest.resource.AbstractRestResource;

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

}
