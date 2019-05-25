package hr.fer.seminar.GUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class JInfoText extends JTextArea{
	private static final long serialVersionUID = 1L;
	
	public JInfoText() {
		super();
		intGUI();
	}
	
	public JInfoText(String text) {
		super(text);
		intGUI();
	}

	private void intGUI() {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFont(new Font("Roboto Slab", Font.BOLD, 16));
	}

	public Integer getIntText(int size) {
		try {
			int n = Integer.parseInt(super.getText());
			
			if (n < 0 || n > size) {
				return null;
			}
			
			return n;
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
}
