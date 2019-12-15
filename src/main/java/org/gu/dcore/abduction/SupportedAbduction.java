package org.gu.dcore.abduction;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.gu.dcore.grd.IndexedByHeadPredRuleSet;
import org.gu.dcore.model.Atom;
import org.gu.dcore.model.AtomSet;
import org.gu.dcore.model.ConjunctiveQuery;
import org.gu.dcore.model.LiftedAtomSet;
import org.gu.dcore.model.Predicate;
import org.gu.dcore.model.Rule;
import org.gu.dcore.model.Term;
import org.gu.dcore.reasoning.Unifier;
import org.gu.dcore.reasoning.Unify;
import org.gu.dcore.store.DatalogEngine;
import org.gu.dcore.tuple.Pair;
import org.gu.dcore.utils.Utils;
import org.semanticweb.vlog4j.parser.ParsingException;

public class SupportedAbduction extends QueryAbduction {
	private IndexedByHeadPredRuleSet irs;
	
	public SupportedAbduction(List<Rule> onto, ConjunctiveQuery q, DatalogEngine D, Set<Predicate> abdu) {
		super(onto, q, D, abdu);
		this.irs = new IndexedByHeadPredRuleSet(onto);
	}
	
	public List<Explanation> getExplanations() throws IOException, ParsingException {
		List<Explanation> result = new LinkedList<>();
		
		List<AtomSet> final_set = new LinkedList<>();
		List<AtomSet> exploration_set = new LinkedList<>();
		
		Queue<AtomSet> queue = new LinkedList<>();
		queue.add(this.query.getBody());
		
		while(!queue.isEmpty()) {
			AtomSet current = queue.poll();
			
			List<Pair<LiftedAtomSet, Map<Term, Term>>> cleaned = atomset_reduce(current);
			List<AtomSet> rewritings = new LinkedList<>();
			
			if(cleaned.isEmpty()) {
				if(allAbducibles(current))
					final_set.add(current);
				rewritings.addAll(rewrite(current));
			}
			else {
				for(Pair<LiftedAtomSet, Map<Term, Term>> la : cleaned) {
					AtomSet as = la.a;
					if(allAbducibles(as))
						final_set.add(as);
					rewritings.addAll(rewrite(as));
				}
			}
			
			Utils.removeSubsumed(rewritings, final_set, false);
			Utils.removeSubsumed(rewritings, exploration_set, false);
			Utils.removeSubsumed(exploration_set, rewritings, false);
			Utils.removeSubsumed(final_set, rewritings, false);
			
			exploration_set.addAll(rewritings);
		}
		
		return result;
	}
	
	private boolean allAbducibles(AtomSet atomset) {
		for(Atom a : atomset) {
			if(!this.abducibles.contains(a.getPredicate()))
				return false;
		}
		
		return true;
	}
	
	private List<AtomSet> rewrite(AtomSet atomset) {
		List<AtomSet> rewritings = new LinkedList<>();
		
		for(Atom a : atomset) {
			Set<Rule> rules_to_rewrite = this.irs.getRulesByPredicate(a.getPredicate());
			if(!this.abducibles.contains(a.getPredicate()) &&
					rules_to_rewrite.isEmpty()) return new LinkedList<>();
			
			for(Rule r : rules_to_rewrite) {
				List<Unifier> unifiers = Unify.getSinglePieceUnifiers(atomset, r);
				
				for(Unifier u : unifiers) {
					 rewritings.add(Utils.rewrite(atomset, r.getBody(), u));
				}
			}	
		}
		return null;
	}
}
