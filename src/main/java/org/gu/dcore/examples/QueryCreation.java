package org.gu.dcore.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
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
import org.gu.dcore.parsing.QueryParser;
import org.gu.dcore.tuple.Pair;

public class QueryCreation {
	public static void main(String[] args) throws IOException {
//		retrieveQueryPredicate();
		
//		genOG();
//		genAdolena();
//		genReactome();
		genUniprot();
	}
	
	public static void composeQuery(String ontology, String target, String... sources) throws FileNotFoundException {
		if(sources.length < 2) return;
		
		String root = sources[0];
		
		String queryDir = ontology + "/dlp_queries";
		String targetFile = queryDir + "/" + target;
		
		Scanner scanner = new Scanner(new File(queryDir + "/" + root));
		String s = scanner.nextLine();
		scanner.close();
		
		ConjunctiveQuery root_query = new QueryParser().parse(s);
//		System.out.println(root_query.toString());
		Variable rightv = selecJoinVar(root_query.getBody());
		int offset = root_query.getBody().getMaxVarValue();
		
		for(int i = 1; i < sources.length; i++) {
			String cur = sources[i];
			Scanner scanner1 = new Scanner(new File(queryDir + "/" + cur));
			String s1 = scanner1.nextLine();
			scanner1.close();
			ConjunctiveQuery query = new QueryParser().parse(s1);
//			System.out.println(query.toString());
			AtomSet qbody = query.getBody();
			int toffset = qbody.getMaxVarValue();
			Pair<Variable, Variable> joinvars = selectJoinVars(qbody);
			Variable leftv = joinvars.a;
			Variable _rightv = joinvars.b;
			
			replaceVar(qbody, leftv, rightv, offset + 1);
			offset += toffset;
			root_query.getBody().addAll(qbody);
			
			if(!leftv.equals(_rightv)) rightv = _rightv;
		}
		
		PrintWriter writer = new PrintWriter(new File(targetFile));		
		writer.println(root_query.toString());
		writer.close();
		System.out.println("Done compose query " + target);
	}
	
	private static Pair<Variable, Variable> selectJoinVars(AtomSet atomset) {
		Variable leftv = selecJoinVar(atomset);
		
		if(atomset.getVariables().size() <= 1) return new Pair<>(leftv,leftv);
		
		Variable rightv;
		
		do {
			rightv = selecJoinVar(atomset);
		} while(leftv.equals(rightv));
		
		return new Pair<>(leftv, rightv);
	}
	
	private static Variable selecJoinVar(AtomSet atomset) {
		Random rand = new Random();
		Atom a = atomset.getAtom(rand.nextInt(atomset.size()));
		
		Term t;
		int i = 0;
		do {
			t = a.getTerm(rand.nextInt(a.getTerms().size()));
			i++;
		} while(!(t instanceof Variable) && i < a.getTerms().size());
		
		if(!(t instanceof Variable)) return null;
		return (Variable)t;
	}
	
	private static void replaceVar(AtomSet atomset, Variable vs, Variable vt, int offset) {
		for(Atom a : atomset) {
			for(int i = 0; i < a.getTerms().size(); i++) {
				Term t = a.getTerm(i);
				if(t.equals(vs)) a.setTerm(i, vt);
				else {
					if(t instanceof Variable) {
						Variable nv = new Variable(((Variable)t).getValue() + offset);
						a.setTerm(i, nv);
					}
				}
			}
		}
	}
	
	public static void retrieveQueryPredicate() throws IOException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/owl/Uniprot/Uniprot.dlp";
		
		Program P = new DcoreParser().parseFile(ontofile);	
	
		Map<Integer, List<Predicate>> predLevels = getPredLevels(P.getRuleSet());
		
		int[] select = {2, 3, 4, 5};
		
		Random rand = new Random();
		
		for(int i = 0; i < select.length; i++) {
			List<Predicate> preds = predLevels.get(select[i]);
			if(preds == null) continue;
			int index = rand.nextInt(preds.size());
			Predicate pred = preds.get(index);
			System.out.println("level " + select[i] + ": " + pred + "[" + pred.getArity() + "]");
		}
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
	
	private static void genOG() throws FileNotFoundException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/owl/OG";		
//		composeQuery(ontofile, "q10", "q1", "q3");
//		composeQuery(ontofile, "q11", "q6", "q3");
		composeQuery(ontofile, "q6", "q1", "q3");
		composeQuery(ontofile, "q7", "q3", "q5");
		composeQuery(ontofile, "q8", "q1", "q3", "q4");
		composeQuery(ontofile, "q9", "q1", "q2", "q3", "q5");
		composeQuery(ontofile, "q10", "q1", "q2", "q3", "q4", "q5");
	}
	
	private static void genAdolena() throws FileNotFoundException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/owl/Adolena";		
		composeQuery(ontofile, "q7", "q2", "q6");
		composeQuery(ontofile, "q8", "q1", "q2");
		composeQuery(ontofile, "q9", "q2", "q4");
		composeQuery(ontofile, "q10", "q1", "q3");
		composeQuery(ontofile, "q11", "q4", "q5");
	}
	
	private static void genReactome() throws FileNotFoundException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/owl/Reactome";		
		composeQuery(ontofile, "q8", "q1", "q6");
		composeQuery(ontofile, "q9", "q1", "q7");
		composeQuery(ontofile, "q10", "q1", "q6", "q7");
		composeQuery(ontofile, "q11", "q6", "q3");
	}
	
	private static void genUniprot() throws FileNotFoundException {
		String ontofile = "/home/sharpen/projects/evaluations/benchmarks/owl/Uniprot";		
		composeQuery(ontofile, "q9", "q6", "q5");
		composeQuery(ontofile, "q10", "q4", "q8");
	}
}
