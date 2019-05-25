package hr.fer.seminar.GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import hr.fer.seminar.aco.Route;
import hr.fer.seminar.aco.Vertex;
import hr.fer.seminar.wedding.Person;
import hr.fer.seminar.wedding.WeddingCorrelation;

public class Tables extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Color CHAIR_COLOR = Color.BLACK;//new Color(222, 230, 232);
	private static Color FLOOR_COLOR = Color.WHITE; //new Color(116, 77, 62);
	private static Color TABLE_COLOR = new Color(0, 145, 234);//new Color(163, 57, 77);
	private static Color PLATE_COLOR = Color.WHITE;//new Color(222, 230, 232);
	private List<Vertex> persons;
	private int personsPerTable;
	private List<Route> optimalRoutes;

	public Tables(List<Vertex> persons, int personsPerTable) {
		this.personsPerTable = personsPerTable;
		this.persons = persons;
		setTitle("RECEPTION");
		setLocation(0, 0);
		setSize(getMaximumSize().width, getMaximumSize().height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(FLOOR_COLOR);
		initGUI();
	}

	public Tables(List<Route> optimalRoutes,List<Vertex> persons, Integer numbPerTable) {
		this.optimalRoutes = optimalRoutes;
		this.persons = persons;
		this.personsPerTable = numbPerTable;
		setTitle("RECEPTION");
		setLocation(0, 0);
		setSize(getMaximumSize().width, getMaximumSize().height);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBackground(FLOOR_COLOR);
		initGUI();
	} 

	private void initGUI() {
		int n = persons.size();
		int max = (int) Math.round(n / personsPerTable);
		double round = Math.abs(max - n *1d / personsPerTable);
		int numberOfTables = round > 0.5 ? max + 1 : max ;
		int gridRow = (int) Math.ceil(Math.sqrt(numberOfTables));
		
		int g = numberOfTables;
		while (true) {
			if (g % gridRow != 0) g++;
			else break;
		}
		int gridColumn = g / gridRow;
		Container cp = getContentPane();
		JPanel mainPanel = new JPanel(new GridLayout(gridColumn , gridRow ));
		
		persons = findBestTable(numberOfTables);
		
		for (int i = 0; i < numberOfTables; i++) {
			int end = (i == numberOfTables - 1) ? persons.size() : (i + 1) * personsPerTable;

			List<Vertex> personsOnTable = persons.subList((i * personsPerTable), end);

			Table table = new Table(personsOnTable);
			mainPanel.add(table);
		}
		
		for (int i = numberOfTables ; i < gridColumn * gridRow  ; i++) {
			Table table = new Table(new ArrayList<>());
			mainPanel.add(table);
		}

		cp.add(mainPanel);
	}

	private List<Vertex> findBestTable(int numberOfTables) {
		double best = -1; 
		Route bestRoute = null;
		
		for (Route r : optimalRoutes) {
			double sum = 0 ;
			for (int i = 0; i < numberOfTables; i++) {
				int end = (i == numberOfTables - 1) ? persons.size() : (i + 1) * personsPerTable;

				List<Vertex> personsOnTable = r.getVertexes().subList((i * personsPerTable), end);
				sum += evaluateTable(personsOnTable);
			}
			
			if (best == -1 || sum/numberOfTables < best) {
				best = sum/numberOfTables;
				bestRoute = r;
				System.out.println("Nova ruta! ");
			}
		}
		
		return bestRoute.getVertexes();
	}

	private double evaluateTable(List<Vertex> personsOnTable) {
		double sum = 0 ;
		for (int i = 0 ; i < personsOnTable.size() ; i++) {
			for (int j = i ; j < personsOnTable.size() ; j++) {
				sum += WeddingCorrelation.evaluate((Person)personsOnTable.get(i),
													(Person)personsOnTable.get(j));
			}
		}
		
		return sum / personsOnTable.size();
	}

	private class Table extends JComponent {
		private static final long serialVersionUID = 1L;
		private List<Vertex> personsOnTable;

		public Table(List<Vertex> personsOnTable) {
			this.personsOnTable = personsOnTable;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Dimension dim = getSize();
			g.setColor(FLOOR_COLOR);
			g.fillRect(0, 0, dim.width, dim.height);
			Point center = new Point(dim.width /2, dim.height /2);
			
			int n = personsOnTable.size();
			double omega = (2 * Math.PI) / n;
			
			int greater = (dim.height < dim.width) ? dim.height : dim.width;
			
			float fontSize = getFontSize(greater, omega);
			g.setFont(getFont().deriveFont(Font.BOLD, fontSize ));
			FontMetrics fm = getFontMetrics(getFont());
			int radius = (int) ((greater - 6 * fontSize) / 2);
			
			g.setColor(TABLE_COLOR);
			g.fillOval(center.x - radius, center.y - radius, radius * 2, radius * 2);
			for (int i = 0; i < personsOnTable.size(); i++) {
				String name = personsOnTable.get(i).getName();
				double theta = i * (omega) - Math.PI/2;
				int x = (int) (radius * Math.cos(theta));
				int y = (int) (radius * Math.sin(theta));
//				g.drawString(name, x + center.x, y + center.y);
				theta =  -(Math.PI/2-theta) + Math.PI;
				g.setColor(Color.BLACK);
				drawRotated((Graphics2D) g, fm, theta, radius, name, x + center.x, y + center.y);
			}
		}


		private float getFontSize(int greater , double omega) {
			return (float) (0.05 *  Math.sin(omega) * greater);
		}

//		private int calculateMax() {
//			return persons.stream()
//						  .mapToInt(t -> t.getName().length())
//						  .max()
//						  .getAsInt();
//		}

		private void drawRotated(Graphics2D g, FontMetrics fm, double theta,int radius,  String name, int x, int y) {
			g.translate(x, y);
			g.rotate(theta);
			g.setColor(CHAIR_COLOR);
			fm = g.getFontMetrics();
			g.fillRect(-fm.stringWidth(name)/4, -5, fm.stringWidth(name)/2, 5);
			g.drawString(name, -fm.stringWidth(name)/2, -6 );
			g.setColor(PLATE_COLOR);
			g.fillOval((int)-radius/11, 0, (int)(radius/5.5), (int)(radius/5.5));
			g.rotate(-theta);
			g.translate(-x, -y);
		}
	}

}