package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;

import com.talool.core.Image;
import com.talool.domain.ImageImpl;

/**
 * 
 * @author dmccuen
 * 
 */
public class AvailableDealImagesListModel extends LoadableDetachableModel<List<Image>>
{

	private static final long serialVersionUID = -4905647453239618763L;

	@Override
	protected List<Image> load()
	{
		List<Image> images = new ArrayList<Image>();

		images.add(new ImageImpl("Test Image 1","http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test.png"));
		images.add(new ImageImpl("Test Image 2","http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test2.png"));
		images.add(new ImageImpl("Test Image 3","http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test3.png"));
		images.add(new ImageImpl("Test Image 4","http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test4.png"));
		images.add(new ImageImpl("Test Image 5","http://i567.photobucket.com/albums/ss116/alphabetabeta/bg_test5.png"));

		return images;
	}
}
