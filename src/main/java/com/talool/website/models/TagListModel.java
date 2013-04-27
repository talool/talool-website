package com.talool.website.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.talool.core.DomainFactory;
import com.talool.core.FactoryManager;
import com.talool.core.Tag;
import com.talool.core.service.ServiceException;
import com.talool.core.service.TaloolService;

public class TagListModel extends LoadableDetachableModel<List<Tag>>{
	
	private static final long serialVersionUID = 3260469156953165360L;
	
	private static final Logger LOG = LoggerFactory.getLogger(TagListModel.class);
	
	private transient static final TaloolService taloolService = FactoryManager.get()
			.getServiceFactory().getTaloolService();

	private transient static final DomainFactory domainFactory = FactoryManager.get()
			.getDomainFactory();

	public static enum CATEGORY_CONTEXT { 
		ROOT("root"), 
		DINE_IN("Dine In"), 
		CASUAL("Casual & Fast Food"), 
		SHOPPING_SERVICES("Shopping & Services"), 
		FUN("Attractions & Fun"), 
		FOOD("Food"), 
		NIGHTLIFE("Nightlife");
		
		private final String name;
		
		private CATEGORY_CONTEXT(String n) {
			name = n.toLowerCase();
		}
		
		public boolean equalsName(String otherName){
	        return (otherName == null)? false:name.equals(otherName);
	    }
		
		public String toString() {
			return name;
		}
		
	}
	
	private CATEGORY_CONTEXT context;
	
	public TagListModel(CATEGORY_CONTEXT level) {
		super();
		this.context = level;
	}
	
	public TagListModel(Tag category) {
		super();
		
		if (category == null ||
			CATEGORY_CONTEXT.DINE_IN.equalsName(category.getName()) ||
			CATEGORY_CONTEXT.CASUAL.equalsName(category.getName())) 
		{
			this.context = CATEGORY_CONTEXT.FOOD; 
		}
		else if (CATEGORY_CONTEXT.SHOPPING_SERVICES.equalsName(category.getName())) 
		{
			this.context = CATEGORY_CONTEXT.SHOPPING_SERVICES; 
		}
		else if (CATEGORY_CONTEXT.FUN.equalsName(category.getName())) 
		{
			this.context = CATEGORY_CONTEXT.FUN; 
		}
		else
		{
			this.context = CATEGORY_CONTEXT.NIGHTLIFE;
		}
	}

	
	// TODO email to Ted
	
	@Override
	protected List<Tag> load() {
		List<Tag> tags = new ArrayList<Tag>();
		
		if (context == CATEGORY_CONTEXT.ROOT) {
			tags.add(getTag(CATEGORY_CONTEXT.DINE_IN.toString()));
			tags.add(getTag(CATEGORY_CONTEXT.CASUAL.toString()));
			tags.add(getTag(CATEGORY_CONTEXT.SHOPPING_SERVICES.toString()));
			tags.add(getTag(CATEGORY_CONTEXT.FUN.toString()));
			tags.add(getTag(CATEGORY_CONTEXT.NIGHTLIFE.toString()));
		} else if (context == CATEGORY_CONTEXT.FOOD) {
			tags.add(getTag("Breakfast"));
			tags.add(getTag("Lunch"));
			tags.add(getTag("Dinner"));
			
			tags.add(getTag("Burgers"));
			tags.add(getTag("BBQ"));
			tags.add(getTag("Bakery"));
			tags.add(getTag("Coffee"));
			tags.add(getTag("Bagels"));
			tags.add(getTag("Pizza"));
			tags.add(getTag("Pasta"));
			tags.add(getTag("Burritos"));
			tags.add(getTag("Cafe"));
			tags.add(getTag("Coffee"));
			tags.add(getTag("Dessert"));
			tags.add(getTag("Coffee"));
			tags.add(getTag("Donut"));
			tags.add(getTag("Fish"));
			tags.add(getTag("Seafood"));
			tags.add(getTag("Sushi"));
			tags.add(getTag("Gluten-free"));
			tags.add(getTag("Ice Cream"));
			tags.add(getTag("Yogurt"));
			tags.add(getTag("Gelato"));
			tags.add(getTag("Juice Bar"));
			tags.add(getTag("Ramen / Noodel House"));
			tags.add(getTag("Salad"));
			tags.add(getTag("Steakhouse"));
			tags.add(getTag("Taco"));
			tags.add(getTag("Tapas"));
			tags.add(getTag("Tea"));
			tags.add(getTag("Vegetarian / Vegan"));
			tags.add(getTag("Wings"));
			tags.add(getTag("Wine"));
			tags.add(getTag("Cocktails"));
			tags.add(getTag("Brewery"));
			
			tags.add(getTag("American"));
			tags.add(getTag("Latin American"));
			tags.add(getTag("Asian"));
			tags.add(getTag("Argentinian"));
			tags.add(getTag("Brazilian"));
			tags.add(getTag("Italian"));
			tags.add(getTag("Mexican"));
			tags.add(getTag("Cajun"));
			tags.add(getTag("Caribbean"));
			tags.add(getTag("Chinese"));
			tags.add(getTag("Deli"));
			tags.add(getTag("Diner"));
			tags.add(getTag("Dim Sum"));
			tags.add(getTag("Europeen"));
			tags.add(getTag("Ethiopian"));
			tags.add(getTag("Japanese"));
			tags.add(getTag("French"));
			tags.add(getTag("German"));
			tags.add(getTag("Greek"));
			tags.add(getTag("Indian"));
			tags.add(getTag("Korean"));
			tags.add(getTag("Mediterranean"));
			tags.add(getTag("Middle Eastern"));
			tags.add(getTag("Mongolian"));
			tags.add(getTag("Moroccan"));
			tags.add(getTag("Spanish"));
			tags.add(getTag("Thai"));
			tags.add(getTag("Vietnamese"));
			
		} else if (context == CATEGORY_CONTEXT.SHOPPING_SERVICES) {		
			tags.add(getTag("Antique Shop"));
			tags.add(getTag("Arts & Crafts Store"));
			tags.add(getTag("Automotive Shop"));
			tags.add(getTag("Bike Shop"));
			tags.add(getTag("Board Shop"));
			tags.add(getTag("Bookstore"));
			tags.add(getTag("Bridal Shop"));
			tags.add(getTag("Camera Store"));
			tags.add(getTag("Candy Store"));
			tags.add(getTag("Car Wash"));
			tags.add(getTag("Clothing Store"));
			tags.add(getTag("Accessories Store"));
			tags.add(getTag("Boutique"));
			tags.add(getTag("Kids Store"));
			tags.add(getTag("Lingerie Store"));
			tags.add(getTag("Men’s Store"));
			tags.add(getTag("Shoe Store"));
			tags.add(getTag("Women’s Store"));
			tags.add(getTag("Convenience Store"));
			tags.add(getTag("Cosmetics Shop"));
			tags.add(getTag("Department Store"));
			tags.add(getTag("Design Studio"));
			tags.add(getTag("Drugstore / Pharmacy"));
			tags.add(getTag("Electronics Store"));
			tags.add(getTag("Financial or Legal Services"));
			tags.add(getTag("Flower Shop"));
			tags.add(getTag("Butcher"));
			tags.add(getTag("Cheese Shop"));
			tags.add(getTag("Farmers Market"));
			tags.add(getTag("Fish Market"));
			tags.add(getTag("Gourmet Shop"));
			tags.add(getTag("Grocery Store"));
			tags.add(getTag("Liquor Store"));
			tags.add(getTag("Wine Shop"));
			tags.add(getTag("Furniture / Home Store"));
			tags.add(getTag("Garden Center"));
			tags.add(getTag("Gas Station / Garage"));
			tags.add(getTag("Gift Shop"));
			tags.add(getTag("Hardware Store"));
			tags.add(getTag("Hobby Shop"));
			tags.add(getTag("Jewelry Store"));
			tags.add(getTag("Laundry Service"));
			tags.add(getTag("Music Store"));
			tags.add(getTag("Nail Salon"));
			tags.add(getTag("Paper / Office Supplies Store"));
			tags.add(getTag("Pet Services"));
			tags.add(getTag("Pet Store"));
			tags.add(getTag("Photography Lab"));
			tags.add(getTag("Record Shop"));
			tags.add(getTag("Salon / Barbershop"));
			tags.add(getTag("Smoke Shop"));
			tags.add(getTag("Spas / Massages"));
			tags.add(getTag("Sporting Goods Shop"));
			tags.add(getTag("Tailor Shops"));
			tags.add(getTag("Tanning Salon"));
			tags.add(getTag("Tattoo Parlor"));
			tags.add(getTag("Thrift / Vintage Store"));
			tags.add(getTag("Toy / Game Store"));
			tags.add(getTag("Travel Agency"));
			tags.add(getTag("Video Game Store"));
			tags.add(getTag("Video Store"));
			tags.add(getTag("Resale / Consignment"));
			
		} else if (context == CATEGORY_CONTEXT.FUN) {
			tags.add(getTag("Arcade"));
			tags.add(getTag("Games"));
			tags.add(getTag("Amusement Center"));
			tags.add(getTag("Kids / Children"));
			tags.add(getTag("Pets"));
			tags.add(getTag("Dogs"));
			tags.add(getTag("Cats"));
			tags.add(getTag("Golf"));
			tags.add(getTag("Skiing / Boarding"));
			tags.add(getTag("Gym"));
			tags.add(getTag("Martial Arts Dojo"));
			tags.add(getTag("Climbing"));
			tags.add(getTag("Athletic Club / Health Club"));
			tags.add(getTag("Pool"));
			tags.add(getTag("Swimming"));
			tags.add(getTag("Bounce House"));
			tags.add(getTag("Community / Rec Center"));
			tags.add(getTag("Yoga"));
			tags.add(getTag("Skating"));
			tags.add(getTag("Theater"));
			tags.add(getTag("Movies"));
			tags.add(getTag("Bowling"));
			tags.add(getTag("Museum"));
		} else if (context == CATEGORY_CONTEXT.NIGHTLIFE) {
			tags.add(getTag("Bar"));
			tags.add(getTag("Beer"));
			tags.add(getTag("Cocktails"));
			tags.add(getTag("Brewery"));
			tags.add(getTag("Wine"));
			tags.add(getTag("Karaoke"));
			tags.add(getTag("Lounge"));
			tags.add(getTag("Nightclub"));
			tags.add(getTag("Pub"));
			tags.add(getTag("Sake"));
			tags.add(getTag("Speakeasy"));
			tags.add(getTag("Dancing"));
			tags.add(getTag("Sports Bar"));
		}
		
		return tags;
	}
	
	private Tag getTag(String name) {
		Tag tag = null;
		try 
		{
			tag = taloolService.getTag(name);
			if (tag == null)
			{
				tag = domainFactory.newTag(name);
			}
		} catch (ServiceException se) {
			LOG.debug("Failed to get/create tag",se);
		}
		
		return tag;
	}

}
