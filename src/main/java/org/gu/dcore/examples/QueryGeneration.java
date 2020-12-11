package org.gu.dcore.examples;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.gu.dcore.factories.PredicateFactory;
import org.gu.dcore.grd.IndexedByHeadPredRuleSet;
import org.gu.dcore.model.Atom;
import org.gu.dcore.model.AtomSet;
import org.gu.dcore.model.ConjunctiveQuery;
import org.gu.dcore.model.Predicate;
import org.gu.dcore.model.Program;
import org.gu.dcore.model.Rule;
import org.gu.dcore.model.Term;
import org.gu.dcore.model.Variable;
import org.gu.dcore.parsing.DcoreParser;
import org.gu.dcore.tuple.Pair;
import org.gu.dcore.utils.Utils;

public class QueryGeneration {
	private static Map<Integer, List<Predicate>> predLevels = null;
	private static IndexedByHeadPredRuleSet ihs = null;
	
	public static void main(String[] args) throws IOException {
//		genDeep();
//		genONT();
//		genSTB();
		genOG();
	}
	
	private static void genDeep() throws IOException {
		String ontofile = "/home/peng/projects/evaluations/benchmarks/existential_rules/deep100/";
		Program P = new DcoreParser().parseFile(ontofile + "deep100.dlp");
		List<Rule> rules = Utils.compute_single_rules(P.getRuleSet());
		ihs = new IndexedByHeadPredRuleSet(rules);
		predLevels = getPredLevels(rules);
		System.out.println(predLevels.keySet());
//		String longQ = ontofile + "QueriesInLength/";
//		File f = new File(longQ);
//		if(!f.exists()) f.mkdir();
//		
//
//		List<ConjunctiveQuery> queries = new LinkedList<>();
//		queries.addAll(genQuery(2, 2, 3));
//		queries.addAll(genQuery(3, 2, 3));
//		queries.addAll(genQuery(4, 2, 3));
//		queries.addAll(genQuery(5, 2, 3));
//		queries.addAll(genQuery(6, 2, 3));
//		queries.addAll(genQuery(7, 2, 3));
//		queries.addAll(genQuery(8, 2, 3));
		
		String longQ = ontofile + "QueriesInDepth/";
		File f = new File(longQ);
		if(!f.exists()) f.mkdir();
		

		List<ConjunctiveQuery> queries = new LinkedList<>();
		queries.addAll(genQuery(3, 2, 5));
		queries.addAll(genQuery(3, 3, 5));
		queries.addAll(genQuery(3, 4, 5));
		queries.addAll(genQuery(3, 5, 5));
		queries.addAll(genQuery(3, 6, 5));
		queries.addAll(genQuery(3, 7, 5));
		queries.addAll(genQuery(3, 8, 5));
		
		
		for(int i = 0; i < queries.size(); i++) {
			String qfile = longQ + "q" + (i + 1);
			PrintWriter writer = new PrintWriter(new File(qfile));
			writer.println(queries.get(i));
			writer.close();
		}
	}
	
	private static void genOG() throws IOException {
		String ontofile = "/home/peng/projects/evaluations/benchmarks/owl/OG/";
		Program P = new DcoreParser().parseFile(ontofile + "OG.dlp");
		List<Rule> rules = Utils.compute_single_rules(P.getRuleSet());
		ihs = new IndexedByHeadPredRuleSet(rules);
		predLevels = getPredLevels(rules);
		System.out.println(predLevels.keySet());
		String longQ = ontofile + "QueriesInDepth/";
		File f = new File(longQ);
		if(!f.exists()) f.mkdir();
		
		List<ConjunctiveQuery> queries = new LinkedList<>();
		queries.addAll(genQuery(1, 2, 5));
		queries.addAll(genQuery(1, 5, 5));
		queries.addAll(genQuery(1, 8, 5));
		queries.addAll(genQuery(1, 11, 5));
		queries.addAll(genQuery(1, 14, 5));
		queries.addAll(genQuery(1, 17, 5));
		queries.addAll(genQuery(1, 20, 5));
		
		for(int i = 0; i < queries.size(); i++) {
			String qfile = longQ + "q" + (i + 1);
			PrintWriter writer = new PrintWriter(new File(qfile));
			writer.println(queries.get(i));
			writer.close();
		}
	}
	
	private static void genONT() throws IOException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/existential_rules/ONT-256/";
		Program P = new DcoreParser().parseFile(ontofile + "ONT-256.dlp");
		List<Rule> rules = Utils.compute_single_rules(P.getRuleSet());
		ihs = new IndexedByHeadPredRuleSet(rules);
		predLevels = getPredLevels(rules);
		System.out.println(predLevels.keySet());
		String longQ = ontofile + "QueriesInLength/";
		File f = new File(longQ);
		if(!f.exists()) f.mkdir();
		
		for(Rule r : rules) {
			System.out.println(r);
		}
		List<ConjunctiveQuery> queries = new LinkedList<>();
		queries.addAll(genQuery(2, 5, 5));
		queries.addAll(genQuery(4, 5, 5));
		queries.addAll(genQuery(6, 5, 5));
		queries.addAll(genQuery(8, 5, 5));
		queries.addAll(genQuery(10, 5, 5));
		queries.addAll(genQuery(12, 5, 5));
		queries.addAll(genQuery(14, 5, 5));
		
		for(int i = 0; i < queries.size(); i++) {
			String qfile = longQ + "q" + (i + 1);
			PrintWriter writer = new PrintWriter(new File(qfile));
			writer.println(queries.get(i));
			writer.close();
		}
	}
	
	private static void genSTB() throws IOException {
		String ontofile = "/home/peng/projects/evaluations/benchmarks/existential_rules/STB-128/";
		Program P = new DcoreParser().parseFile(ontofile + "STB-128.dlp");
		List<Rule> rules = Utils.compute_single_rules(P.getRuleSet());
		ihs = new IndexedByHeadPredRuleSet(rules);
		predLevels = getPredLevels(rules);
		System.out.println(predLevels.keySet());
		String longQ = ontofile + "QueriesInLength/";
		File f = new File(longQ);
		if(!f.exists()) f.mkdir();
		
		for(Rule r : rules) {
			System.out.println(r);
		}
		List<ConjunctiveQuery> queries = new LinkedList<>();
		queries.addAll(genQuery(2, 4, 5));
		queries.addAll(genQuery(4, 4, 5));
		queries.addAll(genQuery(6, 4, 5));
		queries.addAll(genQuery(8, 4, 5));
		queries.addAll(genQuery(10, 4, 5));
		queries.addAll(genQuery(12, 4, 5));
		queries.addAll(genQuery(14, 4, 5));
		
		for(int i = 0; i < queries.size(); i++) {
			String qfile = longQ + "q" + (i + 1);
			PrintWriter writer = new PrintWriter(new File(qfile));
			writer.println(queries.get(i));
			writer.close();
		}
	}
	
	public static List<ConjunctiveQuery> genQuery(int len, int depth, int num) {				
		List<ConjunctiveQuery> qlist = new LinkedList<>();
		
		for(int i = 0; i < num; i++) {
			AtomSet init = new AtomSet();
		    List<Term> ansVar = new LinkedList<>();
		    Variable joinVar = null;
			Random rand = new Random();
		
			List<Predicate> preds = predLevels.get(depth);
			if(preds == null) return null;
			Predicate pred = preds.get(rand.nextInt(preds.size()));
			Set<Rule> brs = ihs.getRulesByPredicate(pred);
		    List<Rule> lrs = new LinkedList<>(brs);
		    Rule rr = null;
		    
		    do {
		    	rr = lrs.get(rand.nextInt(lrs.size()));
		    } while(rr.getFrontierVariables().isEmpty());
		    
		    int offset = 0;
		    
		    for(Atom a : rr.getHead()) {
		    	boolean found = false;
		    	for(Term t : a.getTerms()) {
		    		if(t instanceof Variable) {
		    			if(rr.getFrontierVariables().contains(t)) {
		    				joinVar = (Variable)t;
		    				ansVar.add(t);
		    				init.add(a);
		    				offset = rr.getHead().getMaxVarValue();
		    				found = true;
		    				break;
		    			}
		    		}
		    	}
		    	if(found) break;
		    }
		    
			int l = 1;
			
			while(l < len) {
				int _depth = rand.nextInt(depth - 1) + 2;
				List<Predicate> _preds = predLevels.get(_depth);
				if(_preds == null) continue;
				Predicate _pred = _preds.get(rand.nextInt(_preds.size()));
				Set<Rule> _brs = ihs.getRulesByPredicate(_pred);
			    List<Rule> _lrs = new LinkedList<>(_brs);
			    
			    Rule _rr = null;
			    
			    do {
			    	_rr = _lrs.get(rand.nextInt(_lrs.size()));
			    } while(_rr.getFrontierVariables().isEmpty());
			    
			    for(Atom a : _rr.getHead()) {
			    	boolean found = false;
			    	for(Term t : a.getTerms()) {
			    		if(t instanceof Variable) {
			    			if(_rr.getFrontierVariables().contains(t)) {
			    				Atom na = replaceVar(a, (Variable)t, joinVar, offset + 1);
			    				offset += _rr.getHead().getMaxVarValue();
			    				init.add(na);
			    				found = true;
			    				break;
			    			}
			    		}
			    	}
			    	if(found) break;
			    }
			    
			    l++;
			}
			
			qlist.add(new ConjunctiveQuery(ansVar, init));			
		}
		
		System.out.println("Gen queries with length " + len + " and depth " + depth);
		return qlist;
	}
	
	private static Atom replaceVar(Atom atom, Variable vs, Variable vt, int offset) {
		ArrayList<Term> terms = new ArrayList<>();
		for(int i = 0; i < atom.getTerms().size(); i++) {
			Term t = atom.getTerm(i);
			if(t.equals(vs)) terms.add(vt);
			else {
				if(t instanceof Variable) {
					Variable nv = new Variable(((Variable)t).getValue() + offset);
					terms.add(nv);
				}
				else terms.add(t);
			}
		}
		return new Atom(atom.getPredicate(), terms);
	}
	
	public static Map<Integer, List<Predicate>> getPredLevels(List<Rule> ruleset) {
		Collection<Predicate> predicates = PredicateFactory.instance().getPredicates();
		
		IndexedByHeadPredRuleSet ihs = new IndexedByHeadPredRuleSet(ruleset);
		
		Map<Integer, List<Predicate>> predicate_levels = new HashMap<>();
		
		for(Predicate predicate : predicates) {
			int max_depth = 0;
			
			Queue<Pair<Predicate,Integer>> queue = new LinkedList<>();
			queue.add(new Pair<>(predicate, 0));
			
			Set<Rule> visited = new HashSet<>();
			
			while(!queue.isEmpty()) {
				Pair<Predicate,Integer> pair = queue.poll();
				Predicate pred = pair.a;
				int level = pair.b;
				
				int cur_level = level + 1;
				
				if(cur_level > max_depth) max_depth = cur_level;
				
				Set<Rule> rules = ihs.getRulesByPredicate(pred);
				
				if(rules != null) {
					for(Rule r : rules) {
						if(visited.add(r)) {
							for(Atom a : r.getBody()) {
								queue.add(new Pair<>(a.getPredicate(), cur_level));
							}
						}
					}
				}
			}
			
			List<Predicate> preds = predicate_levels.get(max_depth);
			if(preds == null) {
				preds = new LinkedList<Predicate>();
				predicate_levels.put(max_depth, preds);
			}
			preds.add(predicate);
		}
		
		return predicate_levels;
	}
}
