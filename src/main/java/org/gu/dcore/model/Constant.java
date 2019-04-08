package org.gu.dcore.model;

import org.gu.dcore.interf.Term;

/**
 * Constant class
 * @author sharpen
 * @version 1.0, April 2018 
 */
public class Constant implements Term {
	private String name;
	private long id;
	
	
	public Constant(String _name, long _id) {
		this.name = _name;
		this.id = _id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
