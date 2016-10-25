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

import java.io.*;
import java.util.*;

public class Student extends Person {
	//------------
	// Attributes.
	//------------

	private String major;
	private String degree;
	private Transcript transcript;
	private Vector attends; // of Sections
	
	// This next attribute was added for use with the GUI.

	private String password;

	//----------------
	// Constructor(s).
	//----------------

	public Student(String ssn) {
		// First, construct a "dummy" Student object.  Then, 
		// attempt to pull this Student's information from the 
		// appropriate file (ssn.dat:  e.g., 111-11-1111.dat).  
		// The file consists of a header record, containing 
		// the student's basic info. (ssn, name, etc.), and 
		// 0 or more subsequent records representing a list of 
		// the sections that he/she is currently registered for.

		this();

		String line = null;
		BufferedReader bIn = null;
		boolean outcome = true;
		String pathToFile = ssn + ".dat";

		try {
			// Open the file. 

			bIn = new BufferedReader(new FileReader(pathToFile));

			// The first line in the file contains the header info.

		    	line = bIn.readLine();
			if (line != null) parseData(line);

			// Any remaining lines contain section references.
			// Note that we must instantiate an empty vector
			// so that the parseData2() method may insert
			// items into the Vector.

			attends = new Vector();

		    	line = bIn.readLine();
		    	while (line != null) {
				parseData2(line);
		    		line = bIn.readLine();
			}

			bIn.close();
		}
		catch (FileNotFoundException f) {
			// Since we are encoding a "dummy" Student to begin
			// with, the fact that his/her name will be equal
			// to "???" flags an error.  We have included
			// a boolean method successfullyInitialized() 
			// which allows client code to verify the success
			// or failure of this constructor (see code below).
		}
		catch (IOException i) {
			// See comments for FileNotFoundException above.
		}

		// Initialize the password to be the first three digits
		// of the student's ssn.

		setPassword(getSsn().substring(0, 3)); // added for GUI purposes

		// Create a brand new Transcript.
		// (Ideally, we'd read in an existing Transcript from 
		// a file, but we're not bothering to do so in this
		// example).

		setTranscript(new Transcript(this));
	}

	// A second form of constructor, used when a Student's data
	// file cannot be found for some reason.

	public Student() {
		// Reuse the code of the parent's constructor.
		// Question marks indicate that something went wrong!

		super("???", "???");

		setMajor("???");
		setDegree("???");

		// Placeholders for the remaining attributes (this
		// Student is invalid anyway).
		
		setPassword("???"); // added for GUI purposes
		setTranscript(new Transcript(this));
		attends = new Vector();
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMajor() {
		return major;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getDegree() {
		return degree;
	}

	public void setTranscript(Transcript t) {
		transcript = t;
	}

	public Transcript getTranscript() {
		return transcript;
	}

	// This next method was added for use with the GUI.

	public void setPassword(String pw) {
		password = pw;
	}

	// This next method was added for use with the GUI.

	public String getPassword() {
		return password;
	}


	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// Used for testing purposes.

	public void display() {
		// First, let's display the generic Person info.

		super.display();
		
		// Then, display Student-specific info.

		System.out.println("Student-Specific Information:");
		System.out.println("\tMajor:  " + getMajor());
		System.out.println("\tDegree:  " + getDegree());
		displayCourseSchedule();
		printTranscript();
	}	
	
	// We are forced to program this method because it is specified
	// as an abstract method in our parent class (Person); failing to
	// do so would render the Student class abstract, as well.
	//
	// For a Student, we wish to return a String as follows:
	//
	// 	Joe Blow (123-45-6789) [Master of Science - Math]

	public String toString() {
		return getName() + " (" + getSsn() + ") [" + getDegree() +
		       " - " + getMajor() + "]";
	}

	public void displayCourseSchedule() {
		// Display a title first.

		System.out.println("Course Schedule for " + getName());
		
		// Step through the Vector of Section objects, 
		// processing these one by one.

		if (attends.size() == 0) 
			System.out.println("\t(none)");

		else for (int i = 0; i < attends.size(); i++) {
		    Section s = (Section) attends.elementAt(i);

		    // Since the attends Vector contains Sections that the
		    // Student took in the past as well as those for which
		    // the Student is currently enrolled, we only want to
		    // report on those for which a grade has not yet been
		    // assigned.
            
                    if (s.getGrade(this) == null) {
			System.out.println("\tCourse No.:  " + 
				s.getRepresentedCourse().getCourseNo());
			System.out.println("\tSection No.:  " + 
				s.getSectionNo());
			System.out.println("\tCourse Name:  " + 
				s.getRepresentedCourse().getCourseName());
			System.out.println("\tMeeting Day and Time Held:  "  +
				s.getDayOfWeek() + " - " +
				s.getTimeOfDay());
			System.out.println("\tRoom Location:  "  +
				s.getRoom());
			System.out.println("\tProfessor's Name:  " +
				s.getInstructor().getName());
			System.out.println("\t-----");
		    }
		}
	}
	
	public void addSection(Section s) {
		attends.addElement(s);
	}
	
	public void dropSection(Section s) {
		attends.remove(s);
	}
	
	// Determine whether the Student is already enrolled in THIS
	// EXACT Section.

	public boolean isEnrolledIn(Section s) {
		if (attends.contains(s)) return true;
		else return false;
	}
		
	// This next method was added for use with the GUI.

	// Determine whether the Student is already enrolled in ANOTHER
	// Section of this SAME Course.

	public boolean isCurrentlyEnrolledInSimilar(Section s1) {
		boolean foundMatch = false;
		Course c1 = s1.getRepresentedCourse();
		Enumeration e = getEnrolledSections();
		while (e.hasMoreElements()) {
			Section s2 = (Section) e.nextElement();
			Course c2 = s2.getRepresentedCourse();
			if (c1 == c2) {
				// There is indeed a Section in the attends()
				// Vector representing the same Course.
				// Check to see if the Student is CURRENTLY
				// ENROLLED (i.e., whether or not he has
				// yet received a grade).  If there is no
				// grade, he/she is currently enrolled; if
				// there is a grade, then he/she completed
				// the course some time in the past.
				if (s2.getGrade(this) == null) {
					// No grade was assigned!  This means
					// that the Student is currently
					// enrolled in a Section of this
					// same Course.
					foundMatch = true;
					break;
				}
			}
		}

		return foundMatch;
	}
		
	public void printTranscript() {
		getTranscript().display();
	}

	public void parseData(String line) {
		// We're going to parse tab-delimited records into
		// four attributes -- ssn, name, major, and degree. 

		// First, make a copy of the record.

		String restOfLine = line;
		int index = restOfLine.indexOf("\t");
		setSsn(restOfLine.substring(0, index));
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		setName(restOfLine.substring(0, index));
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		setMajor(restOfLine.substring(0, index));
		setDegree(restOfLine.substring(index+1));
	}

	public void parseData2(String line) {
		// The full section number is a concatenation of the
		// course no. and section no., separated by a hyphen;
		// e.g., "ART101 - 1".

		String fullSectionNo = line.trim();
		Section s = SRS.scheduleOfClasses.findSection(fullSectionNo);

		// Note that we are using the Section class's enroll()
		// method to ensure that bidirectionality is established
		// between the Student and the Section.
		s.enroll(this);
	}
	
	// Used after the constructor is called to verify whether or not
	// there were any file access errors.

	public boolean successfullyInitialized() {
		if (getName().equals("???")) return false;
		else return true;
	}

	// This method writes out all of the student's information to
	// his/her ssn.dat file when he/she logs off.
	
	public boolean persist() {
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// Attempt to create the ssn.dat file.  Note that
			// it will overwrite one if it already exists.

			fos = new FileOutputStream(getSsn() + ".dat");
			pw = new PrintWriter(fos);

			// First, we output the header record as a tab-delimited
			// record.

			pw.println(getSsn() + "\t" + getName() + "\t" +
				   getMajor() + "\t" + getDegree());

			// Then, we output one record for every Section that
			// the Student is enrolled in.

			for (int i = 0; i < attends.size(); i++) {
				Section s = (Section) attends.elementAt(i);
				pw.println(s.getFullSectionNo());
			}

			pw.close();
			fos.close(); //? needed?
		}
		catch (IOException e) {
			// Signal that an error has occurred.

			return false;
		}
		
		// All is well!

		return true;
	}

	public Enumeration getEnrolledSections() {
		return attends.elements();
	}

	// This next method was added for use with the GUI.

	public Vector getSectionsEnrolled() {
		return attends;
	}

	// This next method was added for use with the GUI.

	public int getCourseTotal() {
		return attends.size();
	}

	// This next method was added for use with the GUI.

	public boolean validatePassword(String pw) {
		if (pw == null) return false;
		if (pw.equals(password)) return true;
		else return false;
	}
}

