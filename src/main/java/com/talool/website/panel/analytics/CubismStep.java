package com.talool.website.panel.analytics;

import java.io.Serializable;

public class CubismStep implements Serializable {
	private static final long serialVersionUID = 1L;
	public double step;
	public String label;
	
	public CubismStep(double step, String label) {
		super();
		this.step = step;
		this.label = label;
	}
}
