package hr.fer.seminar.aco;

public abstract class Vertex {
	private String name;
	private int position;
	
	public Vertex(String name, int position) {
		super();
		this.name = name;
		this.position = position;
	}

	public abstract double measureDistance(Vertex v, double[][] distances);
	
	public int getPosition() {
		return position;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
