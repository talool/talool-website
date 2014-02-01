package com.talool.website.panel.image.upload;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.apache.wicket.ajax.json.JSONArray;
import org.apache.wicket.ajax.json.JSONException;
import org.apache.wicket.ajax.json.JSONObject;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.upload.FileItem;

import com.talool.core.MediaType;
import com.talool.image.upload.FileManager;
import com.talool.image.upload.FileNameUtils;
import com.talool.website.Config;

/**
 * A resource reference provides default implementation of
 * AbstractFileUploadResource. The implementation generates JSON response as
 * expected by the demo at <a
 * href="http://blueimp.github.com/jQuery-File-Upload/"
 * >http://blueimp.github.com/jQuery-File-Upload</a>
 */
public class FileUploadResourceReference extends ResourceReference
{

	private static final long serialVersionUID = 1L;
	private final FileManager fileManager;

	public FileUploadResourceReference(String baseFolder)
	{
		super(FileUploadResourceReference.class, "file-upload");

		this.fileManager = new FileManager(baseFolder);
	}

	@Override
	public IResource getResource()
	{
		return new AbstractFileUploadResource(fileManager)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected String generateJsonResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest,
					List<FileItem> files, List<File> outFiles, UUID merchantId, MediaType mediaType)
			{
				final JSONArray json = new JSONArray();

				int i = 0;
				for (final File file : outFiles)
				{
					final JSONObject fileJson = new JSONObject();

					try
					{
						fileJson.put("name", file.getName());
						fileJson.put("url", getViewUrl(file, merchantId));
						fileJson.put("thumbnail_url", getViewUrl(file, merchantId));
						fileJson.put("size", files.get(i).getSize());
						fileJson.put("delete_type", "POST");
						fileJson.put("delete_url", getDeleteUrl(files.get(i)));
						i++;
					}
					catch (JSONException e)
					{
						try
						{
							fileJson.put("error", e.getMessage());
						}
						catch (JSONException e1)
						{
							e1.printStackTrace();
						}
					}

					json.put(fileJson);
				}

				return json.toString();
			}

			@Override
			protected String generateHtmlResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest,
					List<FileItem> files, List<File> outFiles)
			{
				String jsonResponse = generateJsonResponse(resourceResponse, webRequest, files, outFiles, null, null);
				String escapedJson = escapeHtml(jsonResponse);
				return escapedJson;
			}

		};
	}

	private CharSequence getViewUrl(File file, UUID merchantId)
	{
		return FileNameUtils.getImageUrl(file, merchantId);
	}

	// NOTE we're not using this, but it's probably broken now
	private CharSequence getDeleteUrl(FileItem fileItem)
	{
		PageParameters params = new PageParameters();
		params.set("filename", fileItem.getName());
		params.set("delete", true);
		CharSequence url = RequestCycle.get().urlFor(new FileManageResourceReference(Config.get().getUploadDir()), params);
		return url;
	}

}
