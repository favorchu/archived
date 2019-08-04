package net.fchu.sqlformatter.ui.javaquery;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class JavaQueryFrame extends JFrame {

	public JavaQueryFrame() throws HeadlessException {
		setContentPane(new JavaQueryJPane());
		setVisible(true);
		setSize(new Dimension(460, 500));
		setResizable(false);
	}

}
