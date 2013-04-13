package com.talool.website.models;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.talool.core.Deal;
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

	private static String getCommaSepTags(final Set<Tag> tags)
	{
		if (CollectionUtils.isNotEmpty(tags))
		{
			final StringBuilder sb = new StringBuilder();
			final int size = tags.size();

			for (final Tag tag : tags)
			{
				sb.append("(").append(size).append(") ").append(tag.getName());
				return sb.toString();
			}
		}

		return NO_TAG_SUMMARY;
	}

	public static String geTagSummary(final Deal deal)
	{
		return getCommaSepTags(deal.getTags());
	}

	public static String geTagSummary(final Merchant merchant)
	{
		return getCommaSepTags(merchant.getTags());
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
