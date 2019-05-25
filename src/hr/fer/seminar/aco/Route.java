package hr.fer.seminar.aco;

import java.util.ArrayList;
import java.util.Arrays;

public class Route {
	private ArrayList<Vertex> vertexes;
	private double distance;
	
	public Route(ArrayList<Vertex> vertexes, double distance) {
		this.vertexes = vertexes;
		this.distance = distance;
	}
	
	public ArrayList<Vertex> getVertexes() {
		return vertexes;
	}
	public double getDistance() {
		return distance;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(vertexes.toArray()) + " | " + distance;
	}
}
