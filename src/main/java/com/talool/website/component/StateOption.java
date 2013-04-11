package com.talool.website.component;

import java.io.Serializable;

/**
 * 
 * @author clintz
 * 
 */
public class StateOption implements Serializable
{
	private static final long serialVersionUID = -5483641302736639663L;

	private String code;

	private String state;

	public StateOption(String code)
	{
		this.code = code;
	}

	public StateOption(String code, String state)
	{
		this.code = code;
		this.state = state;
	}

	public String getCode()
	{
		return this.code;
	}

	public String getState()
	{
		return this.state;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public void setState(String state)
	{
		this.state = state;
	}

}