package com.talool.website.models;

import java.io.File;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.markup.html.form.upload.FileUpload;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
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

	private static class ImageInfo
	{
		protected String rawPath;
		protected String url;
	}

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

	private static ImageInfo generateUniqueFilePath(final String fileName)
	{
		final StringBuilder sb = new StringBuilder();
		final ImageInfo imageInfo = new ImageInfo();

		final HashFunction hf = Hashing.md5();
		final HashCode hashCode = hf.newHasher().putString(fileName)
				.putLong(System.currentTimeMillis()).hash();

		final int hashDir = Math.abs(hashCode.asInt() % 1000);

		sb.append(hashDir).append("/").append(Hashing.sha1().hashString(fileName)).append(".")
				.append(FilenameUtils.getExtension(fileName));

		final String fileNamePre = sb.toString();

		imageInfo.rawPath = Config.get().getUploadDir() + fileNamePre;
		imageInfo.url = Config.get().getStaticLogoBaseUrl() + fileNamePre;

		return imageInfo;

	}

	/**
	 * Hashes image directory and name, and saves image.
	 * 
	 * @param fileUpload
	 * @return URL to image
	 * @throws Exception
	 */
	public static String saveUploadImage(final FileUpload fileUpload) throws Exception
	{
		if (fileUpload != null)
		{
			ImageInfo newFileInfo = generateUniqueFilePath(fileUpload.getClientFileName());

			final File newFile = new File(newFileInfo.rawPath);

			if (newFile.exists())
			{
				newFile.delete();
			}

			try
			{

				newFile.setReadable(true, false);
				newFile.getParentFile().mkdirs();

				fileUpload.writeTo(newFile);
				return newFileInfo.url;

			}
			catch (Exception e)
			{
				throw new IllegalStateException("Problem saving upload: " + e.getLocalizedMessage(), e);
			}
		}
		return null;
	}
}
