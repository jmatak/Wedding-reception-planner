package hr.fer.seminar.GUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class MainLabel extends JLabel {
	private static final long serialVersionUID = -1660666560679283415L;

	public MainLabel(String text) {
		super(text);
		setOpaque(true);
		setBackground(Color.WHITE);
		setFont(new Font("Edwardian Script ITC", Font.PLAIN, 28));
	}
}
