package hr.fer.seminar.aco;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.seminar.util.Parameters;

public class ACOSolution {
	public Route shortestRoute = null;
	private  AntColonyOptimization aco;
	public List<Route> optimalRoutes = new ArrayList<>();
	private  int reset;
	

	public ACOSolution(AntColonyOptimization aco) {
		this.aco = aco;
	}

	public void processAnt(Ant ant) {
		Route current = ant.getRoute();

		if (shortestRoute == null || Double.compare(shortestRoute.getDistance(), current.getDistance()) == 0) {
			
			if (shortestRoute != null && 
				Double.compare(shortestRoute.getDistance(), current.getDistance()) == 0 &&
				!optimalRoutes.contains(current) && 
				optimalRoutes.size() < 1000) {
				
				optimalRoutes.add(current);
			} else if (shortestRoute == null || shortestRoute.getDistance() > current.getDistance()){
				optimalRoutes.clear();
				if (shortestRoute != null) {
					optimalRoutes.add(current);
				}
			}
			shortestRoute = current;
			aco.updateGlobal(shortestRoute);
			aco.setMaxMinParameters(shortestRoute.getDistance(), aco.initialRoute.size());
			
//			printDistance(ant, current);
			reset = 0;
		} else {
			reset++;
		}
		
		if (reset == Parameters.MAX_EFFORT) {
			aco.maximize();
			reset = 0;
		}
	}

	@SuppressWarnings("unused")
	private  void printDistance(Ant ant, Route current) {
		System.out.println(
		Arrays.toString(shortestRoute.getVertexes().toArray()) + " |" + current.getDistance() + "| " + ant.getAntNumb());
	}

}
