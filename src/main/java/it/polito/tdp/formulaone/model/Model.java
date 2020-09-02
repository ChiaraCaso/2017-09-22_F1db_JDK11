package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {

	FormulaOneDAO dao ;
	Graph<Race, DefaultWeightedEdge> grafo ;
	Map <Integer,Race> idMap;
	List <Race> vertici ;
	List <Arco> archi ;
	
	public Model () {
		this.dao = new FormulaOneDAO();
		this.idMap = new HashMap<Integer, Race>();
		this.vertici = new ArrayList<Race>();
		this.archi = new ArrayList<Arco>();
		
	}
	
	
	public void creaGrafo(Integer x) {
		
		this.grafo = new SimpleWeightedGraph<Race, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getGare(x,idMap);
		
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		this.archi = dao.getArchi(x, idMap);
		
		for (Arco a : archi) {
			if (this.grafo.containsVertex(a.getR1()) && this.grafo.containsVertex(a.getR2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getR1(), a.getR2(), a.getPeso());
			}
		}
		
		
	}
	
	public List <Integer> getAllSeason () {
		return dao.getAnno();
	}
	
	public int nVertici () {
		return this.grafo.vertexSet().size();
	}
		
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	
	public List<Arco> getPesoMax() {
		
		List <Arco> result = new ArrayList<Arco>(); 
		int pesoMax = 0;
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e) > pesoMax) {
				pesoMax = (int) this.grafo.getEdgeWeight(e);
			}
		}
		
		for (DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e) == pesoMax) {
				result.add(new Arco(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e)));
			}
		}
		
		
		return result;
		
	}
	
	
}
