package com.talool.website.models;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.cache.TagCache;
import com.talool.core.Category;

/**
 * 
 * @author clintz
 * 
 */
public class CategoryListModel extends LoadableDetachableModel<List<Category>>
{
	private static final long serialVersionUID = 3260469156953165360L;

	private static final Logger LOG = LoggerFactory.getLogger(CategoryListModel.class);

	@Override
	protected List<Category> load()
	{
		return TagCache.get().getCategories();
	}
}
