package hr.fer.seminar.GUI;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import hr.fer.seminar.GUI.StartScreen.PersonListModel;

public class AddUserWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private String weddingList;
	private StartScreen main;
	private PersonListModel model;
	private JInfoText data;

	public AddUserWindow(StartScreen main, String weddingList, PersonListModel model, JInfoText data) {
		setTitle("New user: ");
		this.main = main;
		this.model = model;
		this.data = data;
		this.weddingList = weddingList;
		
		setLocation(main.getLocation());
		setSize(main.getWidth(), main.getHeight());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		initGUI();
	}

	private void initGUI() {
		Container cp = getContentPane();
		
		JPanel mainPanel = new JPanel(new GridLayout(9, 1));
		
		JLabel nameInfo = new MainLabel("Enter name: ");
		JLabel  familyInfo = new MainLabel("Enter family list: ");
		JLabel  friendsInfo = new MainLabel("Enter friends list:  ");
		JLabel  invitationSide = new MainLabel("Enter side of invitation:  ");
		
		JInfoText name = new JInfoText();
		JInfoText family = new JInfoText();
		JInfoText friends = new JInfoText();
		JComboBox<String> side = new JComboBox<>(new String[] {"Groom","Bride"});
		
		JButton start = new StyledButton("DONE");
		
		ActionListener l = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendInfo(name, family, friends, side);
			}
		};
		
		start.addActionListener(l);
		
		mainPanel.add(nameInfo);
		mainPanel.add(name);
		mainPanel.add(familyInfo);
		mainPanel.add(family);
		mainPanel.add(friendsInfo);
		mainPanel.add(friends);
		mainPanel.add(invitationSide);
		mainPanel.add(side);
		mainPanel.add(start);
		
		cp.add(mainPanel);
	}

	protected void sendInfo(JInfoText name, JInfoText family, JInfoText friends, JComboBox<String> side) {
		if (name.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Fill name",
					"ERROR",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		refreshList(name.getText(), family.getText(), friends.getText(), (String)side.getSelectedItem());
	}

	private void refreshList(String name, String family, String friends, String side) {
		try (BufferedWriter wr = new BufferedWriter(new FileWriter(weddingList, true));) {
			wr.newLine();
			wr.write(name + " ");
			wr.write(side.equals("Groom") ? "M " : "Z ");
			wr.write(family + " | ");
			wr.write(friends);
			wr.close();
			main.setList(model, data);
			dispose();
		} catch (IOException e) {
			return;
		}
	}
}
