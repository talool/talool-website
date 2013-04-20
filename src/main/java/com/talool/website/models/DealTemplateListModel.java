package com.talool.website.models;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

/**
 * 
 * @author dmccuen
 * 
 */
public class DealTemplateListModel extends LoadableDetachableModel<List<String>>
{

	private static final long serialVersionUID = -4905647453239618763L;

	@Override
	protected List<String> load()
	{
		List<String> templates = Arrays.asList(new String[] { 
				"$___ Off 2 or more ___ entrees", 
				"Buy 1 ___, Get 1 Free", 
				"___% off of the regular price of ___", 
				"$___ off lunch over $___", 
				"$___ off dinner over $___", 
				"Other" });
		


		return templates;
	}
}
