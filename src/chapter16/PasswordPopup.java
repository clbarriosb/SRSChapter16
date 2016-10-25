/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter16;

/**
 *
 * @author Carmen_Lucia3
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class PasswordPopup extends JDialog {
	private String password;

	// Attributes representing the GUI components.

	private Container contentPane;
	private JLabel passwordLabel;
	private JPasswordField passwordField;

	// Constructor.
	public PasswordPopup(Frame parent) {
		// Invoke the generic JDialog constructor first.

		super(parent, "Enter Password", true);

		contentPane = this.getContentPane();
		contentPane.setLayout(new GridLayout(1, 2));

		passwordLabel = new JLabel("Password:  ", JLabel.RIGHT);
		passwordLabel.setForeground(Color.black);

		passwordField = new JPasswordField();
		ActionListener aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Retrieve the password.
				char[] pw = passwordField.getPassword();
				password = new String(pw).trim();

				// Hide, but don't dispose of, this window ...
				// we need to give the client code a chance to
				// retrieve the user's typed response via
				// the getPassword() method first.
				PasswordPopup.this.setVisible(false);
			}
		};
		passwordField.addActionListener(aListener);

		contentPane.add(passwordLabel);
		contentPane.add(passwordField);

		this.setSize(200, 60);

		// Center it on the screen.
		Dimension screenSize = Toolkit.getDefaultToolkit().
					    getScreenSize();
		Dimension popupSize = this.getSize();
	        int width = popupSize.width;
		int height = popupSize.height;
	    	this.setLocation((screenSize.width - width)/2, 
		                 (screenSize.height - height)/2);

		this.setVisible(true);
	}

	public String getPassword() {
		return password;
	}

	// Test scaffold.
	public static void main(String[] args) {
		PasswordPopup pp = new PasswordPopup(new JFrame());
		System.out.println("Password typed:  " + pp.getPassword());
		pp.dispose();
		System.exit(0);
	}
}
