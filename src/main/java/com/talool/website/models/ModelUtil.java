package com.talool.website.models;

import java.io.File;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import com.talool.core.Deal;
import com.talool.core.Merchant;
import com.talool.core.Tag;
import com.talool.website.Config;

/**
 * 
 * @author clintz
 * 
 */
public final class ModelUtil
{
	private static final String NO_TAG_SUMMARY = "(0)";

	private static String getTagsSummary(final Set<Tag> tags)
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
		return getTagsSummary(deal.getTags());
	}

	public static String geTagSummary(final Merchant merchant)
	{
		return getTagsSummary(merchant.getTags());
	}

	public static String getCommaSeperatedTags(Merchant merchant)
	{
		return getCommaSeperatedTags(merchant.getTags());
	}

	private static String getCommaSeperatedTags(Set<Tag> tags)
	{
		if (CollectionUtils.isNotEmpty(tags))
		{
			final StringBuilder sb = new StringBuilder();

			for (final Tag tag : tags)
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

	public static String getCommaSeperatedTags(final Deal deal)
	{
		return getCommaSeperatedTags(deal.getTags());
	}

	// TODO - generate and return a unique image name!
	public static String saveUploadImage(final FileUpload fileUpload) throws Exception
	{
		if (fileUpload != null)
		{
			final File newFile = new File(Config.get().getUploadDir() + fileUpload.getClientFileName());

			if (newFile.exists())
			{
				newFile.delete();
			}

			try
			{
				newFile.createNewFile();
				fileUpload.writeTo(newFile);
				return fileUpload.getClientFileName();

			}
			catch (Exception e)
			{
				throw new IllegalStateException("Problem saving upload: " + e.getLocalizedMessage(), e);
			}
		}
		return null;
	}

}
