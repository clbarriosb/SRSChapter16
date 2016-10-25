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
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class MainFrame extends JFrame {
	// Define all of the components here as attributes of this class.

	private JPanel leftPanel;
	private JPanel topLeftPanel;
	private JPanel labelPanel;
	private JPanel fieldPanel;
	private JPanel bottomLeftPanel;
	private JPanel rightPanel;
	private JPanel buttonPanel;
	private JTextField ssnField;
	private JTextField nameField;
	private JLabel totalCoursesLabel;
	private JButton dropButton;
	private JButton addButton;
	private JButton logoffButton;
	private JButton saveScheduleButton; 
	private JLabel l1;
	private JLabel l2;
	private JLabel l3;
	private JLabel l4;
	private JList studentCourseList;
	private JList scheduleOfClassesList;

	// Maintain a handle on the Student who is logged in.
	// (Whenever this is set to null, nobody is officially logged on.)

	private Student currentUser;

	// Constructor.

	public MainFrame() {
		// Initialize attributes.

		currentUser = null;

		// Note that using "this." as a prefix is unnecessary -
		// any method calls that stand alone (without a dot notation
		// prefix) are UNDERSTOOD to be invoked on THIS object.

		this.setTitle("Student Registration System");
		this.setSize(500, 300);
		Container contentPane = this.getContentPane( );

		// Technique for centering a frame on the screen.

		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width)/2, 
		                 (screenSize.height - frameSize.height)/2);

		// Create a few panels.

		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 1));  

		topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new GridLayout(1, 2));  

		labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(4, 1));

		fieldPanel = new JPanel();
		fieldPanel.setLayout(new GridLayout(4, 1));

		bottomLeftPanel = new JPanel();
		bottomLeftPanel.setLayout(new BorderLayout());
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 4)); 

		// We'll allow the main frame's layout to remain the
		// default BorderLayout.

		// Note that we are adding labels without maintaining 
		// handles on them! 

		JLabel l = new JLabel("SSN:  ");
		l.setForeground(Color.black);
 		labelPanel.add(l);
		l = new JLabel("Name:  ");
		l.setForeground(Color.black);
 		labelPanel.add(l);
		l = new JLabel("Total Courses:  ");
		l.setForeground(Color.black);
		labelPanel.add(l);

		// Add an empty label for padding/white space.

		l = new JLabel("");
 		labelPanel.add(l);

		// We DO maintain handles on the text fields, however, so
		// that we can later go back and read their contents
		// by name.  Note our choice of descriptive names for these.

 		ssnField = new JTextField(10);
 		nameField = new JTextField(10);

		// Because this next field is not editable, we are making it
		// a JLabel.  (I could have also made it a non-editable 
		// JTextField, but I wanted it to look like a label ...)

		totalCoursesLabel = new JLabel(); 
		totalCoursesLabel.setForeground(Color.black);
		fieldPanel.add(ssnField);
		fieldPanel.add(nameField);
		fieldPanel.add(totalCoursesLabel);
		
		// Add an empty label for padding/white space.

		l = new JLabel("");
 		fieldPanel.add(l);

		// Create the buttons and add them to their panel.  Again,
		// note use of descriptive names.

		dropButton = new JButton("Drop");
		addButton = new JButton("Add");
		logoffButton = new JButton("Log Off");

		// Technique for creating a multi-line button label.

		saveScheduleButton = new JButton();
		saveScheduleButton.setLayout(new GridLayout(2, 1));
		l1 = new JLabel("Save My", JLabel.CENTER);
		l1.setForeground(Color.black);
		l2 = new JLabel("Schedule", JLabel.CENTER);
		l2.setForeground(Color.black);
		saveScheduleButton.add(l1);
		saveScheduleButton.add(l2);

		buttonPanel.add(dropButton);
		buttonPanel.add(saveScheduleButton);
		buttonPanel.add(new JLabel("")); // white space padding
		buttonPanel.add(addButton);
		buttonPanel.add(logoffButton);

		studentCourseList = new JList();
		studentCourseList.setFixedCellWidth(200);
		bottomLeftPanel.add(studentCourseList, BorderLayout.CENTER);

		l = new JLabel("Registered For:");
		l.setForeground(Color.black);
		bottomLeftPanel.add(l, BorderLayout.NORTH);

		l = new JLabel("--- Schedule of Classes ---", JLabel.CENTER);
		l.setForeground(Color.black);
		rightPanel.add(l, BorderLayout.NORTH);

		scheduleOfClassesList = new JList(SRS.scheduleOfClasses.
						  getSortedSections());
		scheduleOfClassesList.setFixedCellWidth(250);
		rightPanel.add(scheduleOfClassesList, BorderLayout.EAST);

		// Initialize the buttons to their proper enabled/disabled
		// state.

		resetButtons();

		// Finally, attach all of the panels to one another
		// and to the frame.
 		// Add in ascending row, then column, order.

		topLeftPanel.add(labelPanel);
		topLeftPanel.add(fieldPanel);
		leftPanel.add(topLeftPanel);
		leftPanel.add(bottomLeftPanel);
 		contentPane.add(leftPanel, BorderLayout.WEST);
 		contentPane.add(rightPanel, BorderLayout.CENTER);
 		contentPane.add(buttonPanel, BorderLayout.SOUTH);
 
		// ------------------
		// Add all behaviors.
		// ------------------

		// Different types of components require different types
		// of listeners:
		//
		// o	Text fields respond to an ActionListener
		//	whenever the Enter key is pressed.
		//
		// o	Buttons respond to an ActionListener
		// 	whenever the button is clicked.
		//
		// o	JLists respond to a ListSelectionListener
		//	whenever an item is selected.

		ActionListener aListener;
		ListSelectionListener lListener;
		WindowAdapter wListener;

		// ssnField

		aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// First, clear the fields reflecting the
				// previous student's information.
				clearFields();
		
				// We'll try to construct a Student based on
				// the ssn we read, and if a file containing
				// Student's information cannot be found,
				// we have a problem.
				String id = ssnField.getText();
				Student theStudent = new Student(id);
				if (!theStudent.successfullyInitialized()) {
				    // Drat!  The ID was invalid.
				    currentUser = null;

				    // Let the user know that login failed,
				    // UNLESS the ID typed was blank,
				    // signalling a successful log-off.
				    JOptionPane.showMessageDialog(null, 
					"Invalid student ID; please try again.",
					"Invalid Student ID",
					JOptionPane.WARNING_MESSAGE);
				}
				else {
				    // Hooray!  We found one!  Now, we need
				    // to request and validate the password.
				    PasswordPopup pp = new PasswordPopup(
							   MainFrame.this);
				    String pw = pp.getPassword();
				    pp.dispose();

				    if (theStudent.validatePassword(pw)) {
					    currentUser = theStudent;
					    setFields(theStudent);

					    // Let the user know that the
					    // login succeeded.
					    JOptionPane.showMessageDialog(null, 
						"Log in succeeded for " + 
						theStudent.getName() + ".",
						"Log In Succeeded",
						JOptionPane.INFORMATION_MESSAGE);
				    }
				    else {
					    // Password validation failed;
					    // notify the user of this.
					    JOptionPane.showMessageDialog(null, 
						"Invalid password; please " +
						"try again.",
						"Invalid Password",
						JOptionPane.WARNING_MESSAGE);
				    }
				}

				MainFrame.this.repaint();

				// Check states of the various buttons.
				resetButtons();
			}
		};
		ssnField.addActionListener(aListener);

		// addButton

		aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Determine which section is selected
				// (note that we must cast it, as it
				// is returned as an Object reference).
				Section selected = (Section)
					scheduleOfClassesList.getSelectedValue();

				// Check to see if this COURSE is already
				// one that the student registered for,
				// even if the SECTION is different.
				// If so, warn them of this.
				if (currentUser.isCurrentlyEnrolledInSimilar(
					selected)) {
				      // Create a String array of TWO lines
				      // of messsage text, so that the popup 
				      // window won't be too wide.
				      String[] message = 
					{ "You are already enrolled in " +
					      "(or have already ",
					  "successfully completed) a section " +
					      "of this course.",
					  " " };

				      // Then, we can just hand the String
				      // array in to the showMessageDialog()
				      // call.
				      JOptionPane.showMessageDialog(null, 
					message,
					"Request Denied",
					JOptionPane.WARNING_MESSAGE);
				}
				else {
					// Attempt to enroll the student, noting
					// the status code that is returned.
					int success =
						selected.enroll(currentUser);

					// Report the status to the user.
					if (success == Section.SECTION_FULL) {
					    JOptionPane.showMessageDialog(
						null, 
						"Sorry - that section is full.",
						"Request Denied",
						JOptionPane.WARNING_MESSAGE);
					}
					else if (success == 
						 Section.PREREQ_NOT_SATISFIED) {
					    JOptionPane.showMessageDialog(
						null, 
						"You haven't satisfied all " +
						"of the prerequisites for " +
						"this course.",
						"Request Denied",
						JOptionPane.WARNING_MESSAGE);
					}
					else if (success == 
						 Section.PREVIOUSLY_ENROLLED) {
				            String[] message = 
					      { "You are already enrolled in " +
					            "(or have already",
					        "successfully completed) a " +
						    "section of this course.",
						" " };

					    JOptionPane.showMessageDialog(
						null, 
						message,
						"Request Denied",
						JOptionPane.WARNING_MESSAGE);
					}
					else { // success!
					    // Display a confirmation message.
					    JOptionPane.showMessageDialog(
						null, 
						"Seat confirmed in " +
						    selected.
						        getRepresentedCourse().
						        getCourseNo() + ".",
						"Request Successful",
						JOptionPane.INFORMATION_MESSAGE);

					    // Update the list of sections 
					    // that this student is 
					    // registered for.
					    studentCourseList.setListData(
						currentUser.
						getSectionsEnrolled());

					    // Update the field representing
					    // student's course total.
					    int total = 
						currentUser.getCourseTotal();
					    totalCoursesLabel.setText("" + 
								      total);

					    // Clear the selection in the
					    // schedule of classes list.
					    scheduleOfClassesList.
						clearSelection();
					}
				}

				// Check states of the various buttons.
				resetButtons();
			}
		};
		addButton.addActionListener(aListener);

		// dropButton

		aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Determine which section is selected
				// (note that we must cast it, as it
				// is returned as an Object reference).
				Section selected = (Section)
					studentCourseList.getSelectedValue();

				// Drop the course.
				selected.drop(currentUser);

				// Display a confirmation message.
				JOptionPane.showMessageDialog(null, 
					"Course " + selected.
					    getRepresentedCourse().
					    getCourseNo() + " dropped.",
					"Request Successful",
					JOptionPane.INFORMATION_MESSAGE);

				// Update the list of sections that 
				// this student is registered for.
				studentCourseList.setListData(
					currentUser.
					getSectionsEnrolled());

				// Update the field representing
				// student's course total.
				int total = currentUser.getCourseTotal();
				totalCoursesLabel.setText("" + total);

				// Check states of the various buttons.
				resetButtons();
			}
		};
		dropButton.addActionListener(aListener);

		// saveScheduleButton

		aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean success = currentUser.persist();
				if (success) {
				        // Let the user know that his/her
					// schedule was successfully saved.
				        JOptionPane.showMessageDialog(null, 
						"Schedule saved.", 
						"Schedule Saved",
						JOptionPane.INFORMATION_MESSAGE);
				}
				else {
				        // Let the user know that there
					// was a problem.
				        JOptionPane.showMessageDialog(null, 
						"Problem saving your " +
						"schedule; please contact " +
						"the SRS Support Staff for " +
						"assistance.",
						"Problem Saving Schedule",
						JOptionPane.WARNING_MESSAGE);
				}
			}
		};
		saveScheduleButton.addActionListener(aListener);

		// logoffButton

		aListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearFields();
				ssnField.setText("");
				currentUser = null;

				// Clear the selection in the
				// schedule of classes list.
				scheduleOfClassesList.clearSelection();

				// Check states of the various buttons.
				resetButtons();
			}
		};
		logoffButton.addActionListener(aListener);

		// studentCourseList

		lListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// When an item is selected in this list,
				// we clear the selection in the other list.
				if (!(studentCourseList.isSelectionEmpty())) 
					scheduleOfClassesList.clearSelection();

				// Check states of the various buttons.
				resetButtons();
			}
		};
		studentCourseList.addListSelectionListener(lListener);

		// scheduleOfClassesList

		lListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// When an item is selected in this list,
				// we clear the selection in the other list.
				if (!(scheduleOfClassesList.isSelectionEmpty())) 
					studentCourseList.clearSelection();

				// Check states of the various buttons.
				resetButtons();
			}
		};
		scheduleOfClassesList.addListSelectionListener(lListener);

		wListener = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(wListener);

              	this.setVisible(true);
         }

	// Because there are so many different situations in which one or
	// more buttons need to be (de)activated, and because the logic is
	// so complex, we centralize it here and then just call this method
	// whenever we need to check the state of one or more of the buttons.  
	// It is a tradeoff of code elegance for execution efficiency:  
	// we are doing a bit more work each time (because we don't need to 
	// reset all four buttons every time), but since the execution time
	// is minimal, this seems like a reasonable tradeoff.
	private void resetButtons() {
		// There are four conditions which collectively govern the
		// state of each button:
		//	
		// o	Whether a user is logged on or not.
		boolean isLoggedOn;
		if (currentUser != null) isLoggedOn = true;
		else isLoggedOn = false;
		
		// o	Whether the user is registered for at least one course.
		boolean atLeastOne;
		if (currentUser != null && currentUser.getCourseTotal() > 0)
			atLeastOne = true;
		else atLeastOne = false;

		// o	Whether a registered course has been selected.
		boolean courseSelected;
		if (studentCourseList.isSelectionEmpty()) 
			courseSelected = false;
		else courseSelected = true;
		
		// o    Whether an item is selected in the Schedule of Classes.
		boolean catalogSelected;
		if (scheduleOfClassesList.isSelectionEmpty()) 
			catalogSelected = false;
		else catalogSelected = true;

		// Now, verify the conditions on a button-by-button basis.

		// Drop button:
		if (isLoggedOn && atLeastOne && courseSelected)
			dropButton.setEnabled(true);
		else dropButton.setEnabled(false);

		// Add button:
		if (isLoggedOn && catalogSelected)
			addButton.setEnabled(true);
		else addButton.setEnabled(false);

		// Save My Schedule button:
		if (isLoggedOn) {
			saveScheduleButton.setEnabled(true);

			// Because of the way that we created the latter two 
			// buttons, we have do a bit of extra work to make them
			// APPEAR to be turned on or off.
			l1.setEnabled(true); 
			l2.setEnabled(true); 
		}
		else {
			saveScheduleButton.setEnabled(false);
			l1.setEnabled(false); 
			l2.setEnabled(false); 
		}

		// Log Off button:
		if (isLoggedOn) logoffButton.setEnabled(true);  
		else logoffButton.setEnabled(false);  
	}

	// Called whenever a user is logged off.
	private void clearFields() {
		nameField.setText("");
		totalCoursesLabel.setText("");
		studentCourseList.setListData(new Vector());
	}

	// Set the various fields, lists, etc. to reflect the information
	// associated with a particular student.  (Used when logging in.)
	private void setFields(Student theStudent) {
		nameField.setText(theStudent.getName());
		int total = theStudent.getCourseTotal();
		totalCoursesLabel.setText("" + total);
	
		// If the student is registered for any courses, list these, too.
		if (total > 0) {
			// Because we already have a vector containing the
			// sections that the student is registered for, 
			// and because these objects have defined a toString()
			// method, we can merely hand the vector to the JList.
			studentCourseList.setListData(theStudent.
				getSectionsEnrolled());
		}
	}
 }

