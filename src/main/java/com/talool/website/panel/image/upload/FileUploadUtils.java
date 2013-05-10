package com.talool.website.panel.image.upload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.upload.FileItem;

import com.talool.website.Config;

public class FileUploadUtils {

	private static final String baseUrl = Config.get().getStaticLogoBaseUrl();
	private static final String baseUploadDir = Config.get().getUploadDir();
	
	public static String getImageUrl(FileItem image, UUID merchantId) {
		StringBuilder sb = new StringBuilder(baseUrl);
    	if (merchantId != null)
    	{
    		sb.append(getMerchantFolderName(merchantId))
    			.append("/");
    	}
    	sb.append(getPngFileName(image));
        return sb.toString();
	}
	
	public static Folder getImageDir(UUID merchantId, boolean original) throws IOException {
		StringBuilder folderPath = new StringBuilder(baseUploadDir);
    	folderPath.append("/").append(getMerchantFolderName(merchantId));
    	
    	if (original)
    	{
    		folderPath.append("/original");
    	}
    	
    	Folder merchantFolder = new Folder(folderPath.toString());
    	merchantFolder.ensureExists();
    	
    	return merchantFolder;
	}
	
	public static File getFile(FileItem image) throws IOException 
	{
		StringBuilder folderPath = new StringBuilder(baseUploadDir);
		folderPath.append("/");
		Folder folder = new Folder(folderPath.toString());
    	return new File(folder, image.getName());
	}
	
	public static File getFile(FileItem image, UUID merchantId, boolean original) throws IOException 
	{
		Folder folder = getImageDir(merchantId, original);
		String name = (original)?image.getName():getPngFileName(image);
    	return new File(folder, name);
	}
	
	/*
	 * TODO consider renaming file if there is a name collision
	 * renaming happen automatically with imagemagick, 
	 * but i don't think we want it to
	 */
	public static int saveImage(FileItem image, UUID merchantId, boolean original) throws IOException 
	{
		File imageFile = getFile(image, merchantId, original);
    	FileOutputStream fileOS = new FileOutputStream(imageFile, false);
    	return IOUtils.copy(image.getInputStream(), fileOS);
	}
	
	/*
	 * TODO hash the UUID if we don't like it in the url 
	 * and the file system
	 */
	private static String getMerchantFolderName(UUID merchantId)
	{
		return merchantId.toString();
	}
	
	/*
	 * Helps ensure we always save PNGs for display in the apps
	 * We should avoid this method for original files.
	 */
	private static String getPngFileName(FileItem image)
	{
		String name = image.getName();
		int dot = name.lastIndexOf(".");
		StringBuilder sb = new StringBuilder(name.substring(0,dot));
		return sb.append(".png").toString();
	}
	
}
