package com.talool.website.panel.image.upload;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.protocol.http.servlet.MultipartServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.resource.AbstractResource;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.MediaType;
import com.talool.image.upload.FileManager;
import com.talool.image.upload.FileNameUtils;

/**
 * The resource that handles the file uploads. Reads the file items from the
 * request parameters and uses FileManager to store them. Additionally cares
 * about the response's content type and body.
 */
public abstract class AbstractFileUploadResource extends AbstractResource
{
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory.getLogger(AbstractFileUploadResource.class);

	private final FileManager fileManager;

	public AbstractFileUploadResource(FileManager fileManager)
	{
		this.fileManager = fileManager;
	}

	/**
	 * Reads and stores the uploaded files
	 * 
	 * @param attributes
	 * @return
	 */
	@Override
	protected ResourceResponse newResourceResponse(Attributes attributes)
	{
		final ResourceResponse resourceResponse = new ResourceResponse();
		List<File> outFiles = null;

		final ServletWebRequest webRequest = (ServletWebRequest) attributes.getRequest();

		try
		{
			MultipartServletWebRequest multiPartRequest = webRequest.newMultipartWebRequest(getMaxSize(), "ignored");

			UUID merchantId = null;
			StringValue id = multiPartRequest.getPostParameters().getParameterValue("merchantId");

			if (id != null && !id.toString().equals("null"))
			{
				merchantId = UUID.fromString(id.toString());
			}

			MediaType mediaType = null;
			StringValue type = multiPartRequest.getPostParameters().getParameterValue("mediaType");
			if (type != null && !type.toString().equals("null"))
			{
				mediaType = MediaType.valueOf(type.toString());
			}

			Map<String, List<FileItem>> files = multiPartRequest.getFiles();
			List<FileItem> fileItems = files.get(FileUploadBehavior.PARAM_NAME);

			if (mediaType == null || merchantId == null)
			{
				saveFiles(fileItems);
			}
			else
			{
				outFiles = saveFiles(fileItems, merchantId, mediaType);
			}

			prepareResponse(resourceResponse, webRequest, fileItems, outFiles, merchantId, mediaType);
		}
		catch (Exception fux)
		{
			LOG.error("An error occurred while uploading a file", fux);
			throw new AbortWithHttpErrorCodeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, fux.getMessage());
		}

		return resourceResponse;
	}

	/**
	 * Sets the response's content type and body
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param fileItems
	 * @throws FileUploadException
	 * @throws IOException
	 */
	protected void prepareResponse(ResourceResponse resourceResponse, ServletWebRequest webRequest, List<FileItem> fileItems,
			List<File> outFiles,
			UUID merchantId, MediaType mediaType)
			throws FileUploadException, IOException
	{

		final String responseContent;
		String accept = webRequest.getHeader("Accept");
		if (wantsHtml(accept))
		{
			// Internet Explorer
			resourceResponse.setContentType("text/html");

			responseContent = generateHtmlResponse(resourceResponse, webRequest, fileItems, outFiles);
		}
		else
		{
			// a real browser
			resourceResponse.setContentType("application/json");

			responseContent = generateJsonResponse(resourceResponse, webRequest, fileItems, outFiles, merchantId, mediaType);
		}

		resourceResponse.setWriteCallback(new WriteCallback()
		{
			@Override
			public void writeData(Attributes attributes) throws IOException
			{
				attributes.getResponse().write(responseContent);
			}
		});
	}

	/**
	 * Delegates to FileManager to store the uploaded files
	 * 
	 * @param fileItems
	 * @throws IOException
	 */
	protected void saveFiles(List<FileItem> fileItems) throws IOException
	{
		for (FileItem fileItem : fileItems)
		{
			File file = FileNameUtils.getFile(fileItem.getName());
			fileItem.write(file);
			fileManager.save(file);
		}
	}

	/**
	 * Delegates to FileManager to store the uploaded files
	 * 
	 * @param fileItems
	 * @param merchantId
	 * @param mediaType
	 * @return The File objects representing the stored files on disk
	 * @throws IOException
	 */
	protected List<File> saveFiles(List<FileItem> fileItems, UUID merchantId, MediaType mediaType) throws IOException
	{
		final List<File> outFiles = new ArrayList<File>(fileItems.size());

		for (FileItem fileItem : fileItems)
		{
			File file = FileNameUtils.getFile(fileItem.getName());
			fileItem.write(file);
			outFiles.add(fileManager.process(file, null, mediaType, merchantId));
		}

		return outFiles;
	}

	/**
	 * Decides what should be the response's content type depending on the
	 * 'Accept' request header. HTML5 browsers work with "application/json", older
	 * ones use IFrame to make the upload and the response should be HTML. Read
	 * http://blueimp.github.com/jQuery-File-Upload/ docs for more info.
	 * 
	 * @param acceptHeader
	 * @return
	 */
	protected boolean wantsHtml(String acceptHeader)
	{
		return !Strings.isEmpty(acceptHeader) && acceptHeader.contains("text/html");
	}

	/**
	 * Defines what is the maximum size of the uploaded files. TODO: integrate
	 * this in FileUploadBehavior to set the max size at the client side too
	 * 
	 * @return
	 */
	protected Bytes getMaxSize()
	{
		return Bytes.megabytes(100);
	}

	/**
	 * Should generate the response's body in JSON format
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param files
	 * @return
	 */
	protected abstract String generateJsonResponse(ResourceResponse resourceResponse,
			ServletWebRequest webRequest, List<FileItem> files, List<File> outFiles, UUID merchantId, MediaType mediaType);

	/**
	 * Should generate the response's body in HTML format
	 * 
	 * @param resourceResponse
	 * @param webRequest
	 * @param files
	 * @return
	 */
	protected abstract String generateHtmlResponse(ResourceResponse resourceResponse,
			ServletWebRequest webRequest, List<FileItem> files, List<File> outFiles);

}