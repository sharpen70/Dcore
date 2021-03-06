package org.gu.dcore.checker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gu.dcore.grd.IndexedBlockRuleSet;
import org.gu.dcore.grd.TGraph;
import org.gu.dcore.model.Atom;
import org.gu.dcore.modularization.Block;
import org.gu.dcore.modularization.BlockRule;
import org.gu.dcore.modularization.Modularizor;

public class SeparableChecker {
	private Modularizor m;
	private Map<Integer, Block> blockMap;
	private Map<Block, Integer> inverse_blockMap;
	private TGraph blockGraph;
	
	private int size;
	
	public SeparableChecker(Modularizor modularizor) {
		this.m = modularizor;
	}
	
	private void buildBlockGraph() {
		int node = 0;
		
		IndexedBlockRuleSet irs = this.m.getIndexedBlockOnto();
		List<BlockRule> blockonto = this.m.getBlockOnto();
		
		this.blockMap = new HashMap<>();
		this.inverse_blockMap = new HashMap<>();
		
		for(BlockRule br : blockonto) {
			for(Block b : br.getBlocks()) {
				blockMap.put(node, b);
				inverse_blockMap.put(b, node);
				node++;
			}
		}
		
		size = this.blockMap.size();		
		this.blockGraph = new TGraph(size);
//		for(int i = 0; i < size; i++) 
//			for(int j = 0; j < size; j++)
//				this.blockGraph[i][j] = 0;
		
		for(Block b : this.inverse_blockMap.keySet()) {
			for(Atom a : b.getBricks()) {
				Set<BlockRule> rs = irs.getRules(a.getPredicate());
				for(BlockRule br : rs) {
					for(Block nb : br.getBlocks()) {
						int i = inverse_blockMap.get(b);
						int j = inverse_blockMap.get(nb);
						this.blockGraph.addEdge(i, j);
					}
				}
			}
		}
	}
	
	public boolean check() {
		buildBlockGraph();
		List<List<Integer>> loops =  this.blockGraph.getSCCs();
		
		for(List<Integer> loop : loops) {
			for(Integer i : loop) {
				Block b = this.blockMap.get(i);
				if(b.getBricks().size() > 1) return false;
			}
		}
		
		return true;
	}
	
	public boolean checkShy() {
		List<BlockRule> blockonto = this.m.getBlockOnto();
		for(BlockRule r : blockonto) {
			for(Block b : r.getBlocks()) {
				if(b.getBricks().size() > 1) return false;
			}
		}
		
		return true;
	}
}
