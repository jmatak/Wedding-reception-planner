package hr.fer.seminar.GUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import hr.fer.seminar.aco.ACOSolution;
import hr.fer.seminar.aco.Ant;
import hr.fer.seminar.aco.AntColonyOptimization;
import hr.fer.seminar.aco.Vertex;
import hr.fer.seminar.util.ReceptionInfo;

public class StartScreen extends JFrame{
	private static final long serialVersionUID = 1L;
	private static final int NUMBER_OF_ANTS = 10_000;
	private ReceptionInfo info = null;
	private String weddingList;
	private boolean gotList;

	public StartScreen() {
		setTitle("RECEPTION");
		setLocation(500, 200);
		setSize(1100, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		Container cp = getContentPane();
		
		JPanel mainPanel = new JPanel(new GridLayout(1, 1));
		JPanel input = new JPanel(new BorderLayout());
		JPanel starter = new JPanel(new GridLayout(1, 1));
		JPanel inputElems = new JPanel(new GridLayout(4, 1));
		
		JLabel label = new MainLabel("<html>Welcome to wedding reception <br> here is your guest list =></html>");
		JButton start = new StyledButton("START");
		JButton getList = new StyledButton("GET LIST");
		JButton addPerson = new StyledButton("ADD PERSON");
		JLabel  getterInfo = new MainLabel("Insert wedding list: ");
		JLabel  numberInfo = new MainLabel("Number of persons per table: ");
		JInfoText data = new JInfoText("C:/Users/josip/Desktop/wList.txt");
		JInfoText personsPerTable = new JInfoText("12");
		
		personsPerTable.setFont(personsPerTable.getFont().deriveFont(Font.BOLD, 20));
		label.setFont(label.getFont().deriveFont(Font.BOLD, 45));
		
		inputElems.add(getterInfo);
		inputElems.add(data);
		inputElems.add(numberInfo);
		inputElems.add(personsPerTable);
		starter.add(start);
		starter.add(getList);
		starter.add(addPerson);
		
		
		input.add(inputElems, BorderLayout.PAGE_START);
		input.add(starter, BorderLayout.PAGE_END);
		input.add(label, BorderLayout.CENTER);
		

		PersonListModel model = new PersonListModel();
		JList<Vertex> list = new JList<>(model);
		list.setFont(list.getFont().deriveFont(Font.BOLD, 20));
		
		ActionListener a = t -> {
			if (!gotList) {
				JOptionPane.showMessageDialog(StartScreen.this,
							"Please insert wedding list",
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			new AddUserWindow(this, weddingList, model, data).setVisible(true);
		};
		
		ActionListener s = t -> {
			if (!gotList) {
				return;
			}
			Integer numbPerTable = personsPerTable.getIntText(info.initialRoute.size());
			if (numbPerTable == null) {
				JOptionPane.showMessageDialog(StartScreen.this,
							"'" + personsPerTable.getText() + "' is not valid number for persons per table!",
							"ERROR",
							JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			AntColonyOptimization aco = new AntColonyOptimization(info);
			ACOSolution solution = new ACOSolution(aco);
			Thread m = new Thread(() -> {
				for (int i = 0; i < NUMBER_OF_ANTS; i++) {
					Ant ant = new Ant(aco, i, solution, info);
					ant.walkGraph();
				}
			});
			
			m.start();
			try {
				m.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(solution.optimalRoutes);
			
			SwingUtilities.invokeLater(()  -> {
				new Tables(solution.optimalRoutes,solution.shortestRoute.getVertexes(), numbPerTable).setVisible(true);
			});
		};
		
		ActionListener g = t -> {
			if (data.getText().isEmpty()) {
				return;
			}
			
			weddingList = data.getText();
			setList(model, data);
		};
		
		start.addActionListener(s);
		getList.addActionListener(g);
		addPerson.addActionListener(a);
		
		mainPanel.add(input);
		mainPanel.add(new JScrollPane(list));
		
		cp.add(mainPanel);
	}
	
	public class PersonListModel implements ListModel<Vertex> {
		private List<Vertex> data = new ArrayList<>();
		private List<ListDataListener> listeners = new ArrayList<>();
		
		@Override
		public int getSize() {
			return data.size();
		}

		@Override
		public Vertex getElementAt(int index) {
			return data.get(index);
		}

		@Override
		public void addListDataListener(ListDataListener l) {
			listeners.add(l);
		}

		@Override
		public void removeListDataListener(ListDataListener l) {
			listeners.remove(l);
		}
		
		private void addInfo(ReceptionInfo info, StartScreen startScreen) {
			ListDataEvent event;
			int currentSize = data.size();
			if (info == null) {
				startScreen.gotList = false;
				data.clear();
				event = new ListDataEvent(this,
						ListDataEvent.CONTENTS_CHANGED,
						currentSize, data.size());
				JOptionPane.showMessageDialog(startScreen,
						"Please provide valid wedding list file",
						"ERROR",
						JOptionPane.ERROR_MESSAGE);
			} else {
				startScreen.gotList = true;
				data.clear();
				for (Vertex v : info.initialRoute) data.add(v);
				if (data.isEmpty()) startScreen.gotList = false;
				event = new ListDataEvent(this,
						ListDataEvent.CONTENTS_CHANGED,
						currentSize, data.size());
			}
			
			for (ListDataListener l :listeners) {
				l.intervalAdded(event);
			}
		}
	}
	
	public void setList(PersonListModel model, JInfoText data) {
		try {
			info = new ReceptionInfo(data.getText());
		} catch (InvalidPathException e) {
			return;
		}
		model.addInfo(info, this);
	}
	
	public static void main(String[] args) {
		new StartScreen().setVisible(true);
	}
}
