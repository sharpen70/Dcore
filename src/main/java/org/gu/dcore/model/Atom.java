package org.gu.dcore.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.gu.dcore.interf.Term;

/**
 * Atom class, without functional terms
 * @author sharpen
 * @version 1.0, April 2018. 
 */
public class Atom {
	private Predicate p;
	private ArrayList<Term> terms;
	private Set<Variable> vars;
	
	public Atom(Predicate p, ArrayList<Term> terms) {
		this.p = p;
		this.terms = terms;
	}
	/**
	 * @return the p
	 */
	public Predicate getPredicate() {
		return p;
	}
	/**
	 * @param p the p to set
	 */
	public void setP(Predicate p) {
		this.p = p;
	}
	/**
	 * @return the terms
	 */
	public ArrayList<Term> getTerms() {
		return terms;
	}
	/**
	 * @param terms the terms to set
	 */
	public void setTerms(ArrayList<Term> terms) {
		this.terms = terms;
	}
	
	@Override
	public String toString() {
		String out = p.getName();
		out = out + "(";
		
		for(int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			out = out + t;
			if(i != terms.size() - 1) {
				out = out + ", ";
			}
		}
		
		out = out + ")";
		
		return out;
	}
}
