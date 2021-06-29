package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.Arco;
import it.polito.tdp.crimes.db.EventsDAO;

public class Model {
	
	EventsDAO dao = new EventsDAO();
	private SimpleWeightedGraph<String,DefaultWeightedEdge> grafo;
	private Map<Object,Object> idMap;
	private ArrayList<String> soluzioni;
	String first;
	String last;
	double peso;
	double pesoTot;
	
	
public List<String> trovaSequenza(DefaultWeightedEdge de) {
		
		//Mi creo una lista di xxxx diponibili, utile per la ricorsione ed inizializzo i valori

		List<String> parziale = new ArrayList<>();
		this.soluzioni = new ArrayList<>();
		this.peso=0;
		this.pesoTot=0;
		first = grafo.getEdgeSource(de);
		last = grafo.getEdgeTarget(de);
		parziale.add(first);
		
		
		//Livello non sempre utile

		cerca(parziale, 0);
		return this.soluzioni;
		
	}
	
	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
	return grafo;
}

public void setGrafo(SimpleWeightedGraph<String, DefaultWeightedEdge> grafo) {
	this.grafo = grafo;
}

	/**
	 * Procedura ricorsiva per il calcolo di sequenze di xxxx
	 * @param parziale parte iniziale della sequenza di xxxx
	 * @param livello indica il livello della ricorsione, sempre uguale a parziale.size().
	 * @param disponibili numero di oggetti non ancora utilizzati
	 */
	private void cerca(List<String> parziale, int livello) {

		//Sequenza di istruzioni sempre eseguite (solo in casi rari)
		//Condizione di terminazione
		if(parziale.get(parziale.size()-1).equals(last)) {
			if(parziale.size()==grafo.vertexSet().size()) {
			if(this.peso<this.pesoTot)  {
				this.pesoTot=peso;
				this.soluzioni.clear();
				this.soluzioni.addAll(parziale);
			}
			return;
			}
		}
		if(this.pesoTot!=0 && this.peso>this.pesoTot)
	       return;

		
		//Ciclo for o while
		for(String l: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
           if(!parziale.contains(l)) {
        	   this.peso+= grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1), l));
        	   parziale.add(l);
        	   cerca(parziale, livello+1);
        	   parziale.remove(l);
        	   this.peso-= grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1), l));
        	   
        	 
           }
		}
	}
	
	
public double getPesoTot() {
		return pesoTot;
	}

	public void setPesoTot(double pesoTot) {
		this.pesoTot = pesoTot;
	}

public void creaGrafo(int year, String s) {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap= new LinkedHashMap<>();
		
		
		Graphs.addAllVertices(grafo, dao.listVertex(year, s));
		

		
		for(Arco ar:dao.listArchi(year, s)) {
			if(grafo.containsVertex(ar.getId1())&&grafo.containsVertex(ar.getId2()))
			if(!grafo.containsEdge(grafo.getEdge(ar.getId1(), ar.getId2()))&&!grafo.containsEdge(grafo.getEdge(ar.getId2(), ar.getId2()))) {
				Graphs.addEdge(grafo, ar.getId1(), ar.getId2(), ar.getPeso());
				
			}
			
		}
		
	}

	
   public String migliore() {
	   String s="";
	   
	   double pesoMax = 0;
	   for(DefaultWeightedEdge de: grafo.edgeSet()) {
		   if(grafo.getEdgeWeight(de)>pesoMax)
			   pesoMax=grafo.getEdgeWeight(de);
		   
	   }
	   for(DefaultWeightedEdge de: grafo.edgeSet()) {
		   if(grafo.getEdgeWeight(de)==pesoMax)
			   s = grafo.getEdgeSource(de) + " "+ grafo.getEdgeTarget(de) + " " + grafo.getEdgeWeight(de) +"\n";
		   
	   }
	   
	   return s;
	   
   }

	public String VA() {
		
		String s= "Numero di vertici e archi: #" + grafo.vertexSet().size()+" #"+ grafo.edgeSet().size()+"\n";
		return s;
	}
	
	public List<String> listaCate(){
		return  this.dao.listCategoria();
	}
	
	public List<Integer> listaYear(){
		return  this.dao.listAnni();
	}
	

}
