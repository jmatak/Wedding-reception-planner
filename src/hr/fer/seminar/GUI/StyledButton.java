package hr.fer.seminar.GUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class StyledButton  extends JButton{
	private static final long serialVersionUID = 1L;

	public StyledButton(String text) {
		super(text);
		setBackground(new Color(0, 145, 234));
		setForeground(Color.WHITE);
		setFont(new Font("Roboto Slab", Font.BOLD, 20));
	}
}