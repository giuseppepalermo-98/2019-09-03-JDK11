package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private Graph<String, DefaultWeightedEdge> grafo;
	private FoodDao dao;
	private List<String> vertici;
	private List<Arco> archi;
	private List<String> best;
	private Integer pesoMax;
	
	public Model() {
		dao=new FoodDao();
		vertici=new ArrayList<>();
		archi= new ArrayList<>();
	}
	
	public String creaGrafo (int calorie) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		vertici=dao.getPortionDisplayName(calorie);
		
		Graphs.addAllVertices(this.grafo, vertici);
		System.out.println("Il numero di vertici sono: "+grafo.vertexSet().size());
		
		archi=dao.getListArco();
		for(Arco a: archi) {
			if(grafo.containsVertex(a.getVertice1()) && grafo.containsVertex(a.getVertice2()))
			Graphs.addEdge(this.grafo, a.getVertice1(), a.getVertice2(), a.getPeso());
		}
		
		return String.format("GRAFO CREATO CON %d VERTICI E %d ARCHI", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public List<String> getVerticiGrafo(){
		return vertici;
	}
	
	public List<Adiacente> getListAdiacenti(String selezionato){
		List<String>vicini=Graphs.neighborListOf(this.grafo, selezionato);
		List<Adiacente>result= new ArrayList<>();
		
		for(String s: vicini) {
			result.add(new Adiacente(s, this.grafo.getEdgeWeight(this.grafo.getEdge(selezionato, s))));
		}
		
		return result;
	}
	
	public List<String> cammino(String porzione, int N) {
		best=new ArrayList<>();
		List<String> parziale = new ArrayList<>();
		
		parziale.add(porzione);
		
		cerca(parziale, N);
		pesoMax=this.calcolaPeso(best);
		return best;
	}

	private void cerca(List<String> parziale, int N) {
		
		if(parziale.size() == N+1) {
			if(calcolaPeso(parziale)>calcolaPeso(best)) {
				best = new ArrayList<>(parziale);
				return;
			}
		}
		
		List<String> vicini  = Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
		for(String s: vicini) {
			if(!parziale.contains(s) ) {
				parziale.add(s);
				cerca(parziale, N);
				parziale.remove(s);
			}
		}
	}

	private int calcolaPeso(List<String> parziale) {
		int peso=0;
		for(int i=1; i<parziale.size(); i++) {
			int p=(int)this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i)));
			peso += p;
		}
		return peso;
	}

	public List<String> getBestCammino() {
		return best;
	}

	public Integer getPesoMax() {
		return pesoMax;
	}
	
}
