package com.talool.website.util;

import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.Model;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

public class CssClassToggle extends AttributeModifier {
	
	private static final long serialVersionUID = 4046919084433206109L;

	private String newClass;
	
	public CssClassToggle(String oldClass, String newClass) {
        super("class", new Model<String>(oldClass));
        this.newClass = newClass;
    }

    @Override
    protected String newValue(String currentValue, String valueToRemove) {
        if (currentValue == null) return "";

        Set<String> classes = Sets.newHashSet(Splitter.on(" ").split(currentValue));
        classes.remove(valueToRemove);
        classes.add(newClass);
        return Joiner.on(" ").join(classes); 
    }
}
