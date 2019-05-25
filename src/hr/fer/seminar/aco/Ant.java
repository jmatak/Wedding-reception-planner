package hr.fer.seminar.aco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import hr.fer.seminar.util.Parameters;
import hr.fer.seminar.util.ReceptionInfo;
import hr.fer.seminar.wedding.Person;

public class Ant{
	private int invalidVertexIndex = -1;
	public ArrayList<Person> initialRoute;
	private int numbOfVertexes ;
	private ACOSolution solution;
	
	private AntColonyOptimization aco;
	private int antNumb;
	private Route route ;

	public Ant(AntColonyOptimization aco, int antNumb, ACOSolution solution , ReceptionInfo info) {
		this.aco = aco;
		this.antNumb = antNumb;
		this.solution = solution;
		this.numbOfVertexes = info.initialRoute.size();
		this.initialRoute = info.initialRoute;
	}

	public void walkGraph()  {
		//Generiraj početni čvor
		int originatingVertexIndex = ThreadLocalRandom.current().nextInt(numbOfVertexes);
		
		ArrayList<Vertex> routeV = new ArrayList<>(numbOfVertexes);
		Map<String, Boolean> visitedVertexes = new HashMap<>(numbOfVertexes);
		
		//Na početku nije posjećen nijedan čvor
		for (int  i = 0 ; i < numbOfVertexes ; i++) {
			visitedVertexes.put(initialRoute.get(i).getName(), false);
		}
		int numbOfVisitedVertexes = 0;
		
		//Dragi mrave, posjeti prvoga, hvala
		visitedVertexes.put(initialRoute.get(originatingVertexIndex).getName(), true);
		double currentRouteDistance = 0d;
		int from = originatingVertexIndex;
		int to = invalidVertexIndex;
		if (numbOfVisitedVertexes != numbOfVertexes) to = getTO(from, visitedVertexes);
		
		//Tražim sljedeće
		while (to != invalidVertexIndex) {
			routeV.add(numbOfVisitedVertexes++,  initialRoute.get(from));
			currentRouteDistance += aco.getDistancesMatrix()[from][to];
			
			//Vrlo važna metoda, dohvati idućeg po vjerojatnosti
			visitedVertexes.put(initialRoute.get(from).getName(), true);
			
			//Za ponoviti postupak , sada onaj čvor iz kojeg smo došli postaje početak
			from = to ;
			
			//Isti postupak kao gore
			if (numbOfVisitedVertexes != numbOfVertexes) {
				to = getTO(from, visitedVertexes) ;
			} else {
				to = invalidVertexIndex;
			}
		}
		routeV.add(numbOfVisitedVertexes++, initialRoute.get(from));
		
		//Za moj problem ovo nije potrebno !!
//		currentRouteDistance += aco.getDistancesMatrix()[from][originatingVertexIndex];
		
		route = new Route(routeV, currentRouteDistance);
		
		solution.processAnt(this);
	}
	
	//Dohvati do kojeg sljedećeg vrha idemo
	private int getTO(int from, Map<String, Boolean> visitedVertexes) {
		int to = invalidVertexIndex;
		double random = ThreadLocalRandom.current().nextDouble() ;
		ArrayList<Double> transitionProb = getTransitionProbabilities(from, visitedVertexes);
		
		for (int next = 0; next < numbOfVertexes ; next++) {
			if (transitionProb.get(next) > random) {
				to = next;
				break;
			} else {
				random -= transitionProb.get(next);
			}
		}
		return to;
	}

	//Dohvati vjerojatnosti
	private ArrayList<Double> getTransitionProbabilities(int from, Map<String, Boolean> visitedVertexes) {
		ArrayList<Double> transitionProbabilities = new ArrayList<>(numbOfVertexes);
		for (int i = 0 ; i < numbOfVertexes ; i++) {
			transitionProbabilities.add(i, 0.0);
		};
		double denominator = getTPDenominator(transitionProbabilities, from, visitedVertexes);
		
		
		for (int  i = 0 ; i < numbOfVertexes ; i++) {
			transitionProbabilities.set(i, transitionProbabilities.get(i) / denominator);
		}
		return transitionProbabilities;
		
	}
	
	//Dohvati nazivnik
	private double getTPDenominator(ArrayList<Double> transitionProbabilities, int from , Map<String, Boolean> visitedVertexes) {
		double denominator = 0d;
		for (int i = 0 ; i < numbOfVertexes ; i++) {
			if (!visitedVertexes.get(initialRoute.get(i).getName())) {
				
				//Ne smijemo ici u isti čvor, nema petlji
				if (i == from ) {
					transitionProbabilities.set(i, 0d);
				} else {
					transitionProbabilities.set(i, getTPNumerator(from, i));
				}
				denominator += transitionProbabilities.get(i);
			}
		}
		return denominator;
	}
	
	//Dohvati brojnik
	private double getTPNumerator (int from ,int to ) {
		double numerator = 0d;
		double pheromoneLevel = aco.getPheromoneLevelsMatrix()[from][to];
		
		if (pheromoneLevel != 0d) {
			//Apsolutno važna formula -- seminar
			numerator = Math.pow(pheromoneLevel, Parameters.ALPHA) * Math.pow(1./aco.getDistancesMatrix()[from][to], Parameters.BETA);
			
		}
		return numerator;
	}

	public Route getRoute() {
			return route;
	}
	public int getAntNumb() {
		return antNumb;
	}
}
