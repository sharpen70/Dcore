package org.gu.dcore.model;

/**
 * This interface is for terms in atoms 
 * @author sharpen
 * @version 1.0, April 2018
 */
public interface Term {
	public boolean isVariable();
	public boolean isConstant();
	public String toRDFox();
	public String toVLog();
	public String toDLV();
}
