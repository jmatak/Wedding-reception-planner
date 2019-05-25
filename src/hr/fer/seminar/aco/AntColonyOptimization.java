package hr.fer.seminar.aco;

import java.util.ArrayList;

import hr.fer.seminar.util.Parameters;
import hr.fer.seminar.util.ReceptionInfo;
import hr.fer.seminar.wedding.Person;

public class AntColonyOptimization {
	private double[][] pheromoneLevelsMatrix = null;
	private double[][] distancesMatrix = null;
	private ArrayList<Vertex> vertexes = new  ArrayList<>();
	private int vertexesSize;
	public ArrayList<Person> initialRoute;
	
	public AntColonyOptimization(ReceptionInfo info) {
		this.vertexesSize = info.initialRoute.size();
		this.initialRoute = info.initialRoute;
		
		initializeDistances();
		initializePheromoneLevels();
		
		for (Person p : initialRoute) {
			Vertex v = (Vertex) p;
			vertexes.add(v);
		}
	}
	
	
	private void initializePheromoneLevels() {
		pheromoneLevelsMatrix = new double[vertexesSize][vertexesSize];
		
		for (int  i = 0 ; i < vertexesSize ; i++) {
			for (int  j = 0 ; j < vertexesSize ; j++) {
				pheromoneLevelsMatrix[i][j] = Parameters.TAU_MAX;
			}
		}
	}
	
	private void initializeDistances() {
		HeuristicDistance d = new HeuristicDistance(initialRoute);
		distancesMatrix = d.getDistances();
	}


	public double[][] getPheromoneLevelsMatrix() {
		return pheromoneLevelsMatrix;
	}
	
	public double[][] getDistancesMatrix() {
		return distancesMatrix;
	}
	
	 public void maximize() {
	        int n = pheromoneLevelsMatrix.length;
	        for (int i = 0; i < n; i++) {
	            for (int j = 0; j < n ; j++) {
	                pheromoneLevelsMatrix[i][j] = Parameters.TAU_MAX;
	            }
	        }
	    }
	
	public void updateGlobal(Route iterationBest) {
		int n = pheromoneLevelsMatrix.length;
		
		//Evaporacija
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                pheromoneLevelsMatrix[i][j] = pheromoneLevelsMatrix[j][i] = (1-Parameters.RO) * pheromoneLevelsMatrix[i][j];
            }
        }
        
        double d = 1. / iterationBest.getDistance();
        for (int i = 0; i < iterationBest.getVertexes().size(); i++) {
            int ti = iterationBest.getVertexes().get(i).getPosition();
            int tj = iterationBest.getVertexes().get((i + 1) % iterationBest.getVertexes().size()).getPosition();
            update(ti, tj, d);
        }
		
	}
	
	public void setMaxMinParameters(double shortestDistance, int size) {
		Parameters.TAU_MAX = 1. / (Parameters.RO) / shortestDistance;
		Parameters.TAU_MIN = Parameters.TAU_MAX * Parameters.K / size;
		
	}


	private void update(int ti, int tj, double newValue) {
		pheromoneLevelsMatrix[ti][tj] = pheromoneLevelsMatrix[tj][ti] = pheromoneLevelsMatrix[ti][tj] + newValue;
	}


}
