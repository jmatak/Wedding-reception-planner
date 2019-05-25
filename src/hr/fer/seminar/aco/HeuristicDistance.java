package hr.fer.seminar.aco;

import java.util.List;

import hr.fer.seminar.wedding.Person;
import hr.fer.seminar.wedding.WeddingCorrelation;

public class HeuristicDistance {
	private double[][] distances;
	
	public HeuristicDistance(List<Person>  weddingList) {
		distances = new double[weddingList.size()][weddingList.size()];
		
		assignValues(weddingList);
	}

	private void assignValues(List<Person> weddingList) {
		 int n = distances.length;
		 
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n ; j++) {
            	Person p1 = weddingList.get(i);
            	Person p2 = weddingList.get(j);
                distances[i][j] =  distances[j][i] = WeddingCorrelation.evaluate(p1, p2);
            }
        }
	}
	
	public double[][] getDistances() {
		return distances;
	}
}
