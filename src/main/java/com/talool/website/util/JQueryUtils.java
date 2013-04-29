package com.talool.website.util;

/**
 * 
 * @author clintz
 * 
 */
public class JQueryUtils
{

	public static String getFadeBackground(final StringBuilder sb, final String markupId,
			final String fromColor, final String toColor, final int delay)
	{
		sb.append("$('#").append(markupId)
				.append("').css('background-color','").append(fromColor).append("');");
		sb.append("$('#").append(markupId)
				.append("').animate({ backgroundColor: '").append(toColor).append("' }, ").append(delay)
				.append(");");

		return sb.toString();
	}

}
