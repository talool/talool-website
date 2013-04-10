package com.talool.website.models;

import org.apache.commons.collections.CollectionUtils;

import com.talool.core.Merchant;
import com.talool.core.Tag;

/**
 * 
 * @author clintz
 * 
 */
public final class ModelUtil
{
	private static final String NO_TAG_SUMMARY = "(0)";

	public static String geTagSummary(Merchant merchant)
	{
		if (CollectionUtils.isNotEmpty(merchant.getTags()))
		{
			final StringBuilder sb = new StringBuilder();
			final int size = merchant.getTags().size();

			for (final Tag tag : merchant.getTags())
			{
				sb.append("(").append(size).append(") ").append(tag.getName());
				return sb.toString();
			}
		}

		return NO_TAG_SUMMARY;
	}

	public static String getCommaSeperatedTags(Merchant merchant)
	{
		if (CollectionUtils.isNotEmpty(merchant.getTags()))
		{
			final StringBuilder sb = new StringBuilder();

			for (final Tag tag : merchant.getTags())
			{
				if (sb.length() == 0)
				{
					sb.append(tag.getName());
				}
				else
				{
					sb.append(",").append(tag.getName());
				}

			}
			return sb.toString();
		}

		return null;
	}
}
