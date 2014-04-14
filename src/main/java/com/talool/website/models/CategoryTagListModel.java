package com.talool.website.models;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.talool.cache.TagCache;
import com.talool.core.Category;
import com.talool.core.Tag;

/**
 * 
 * @author clintz
 * 
 */
public class CategoryTagListModel extends LoadableDetachableModel<List<Tag>>
{
	private static final long serialVersionUID = 3260469156953165360L;

	private static final Logger LOG = LoggerFactory.getLogger(CategoryTagListModel.class);
	
	private Category category;

	public CategoryTagListModel(final Category category)
	{
		this.category = category;
	}

	@Override
	protected List<Tag> load()
	{
		if (category == null)
		{
			return ImmutableList.<Tag> builder().build();
		}

		List<Tag> tagList = TagCache.get().getTagsByCategoryName(category.getName());
		Collections.sort(tagList, new TagComparator());
		
		return tagList;
	}
	
	public class TagComparator implements Comparator<Tag> {
	    @Override
	    public int compare(Tag object1, Tag object2) {
	        return object1.getName().compareToIgnoreCase(object2.getName());
	    }
	}
}
