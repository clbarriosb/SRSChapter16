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

import java.util.*;

public class Course {
	//------------
	// Attributes.
	//------------

	private String courseNo;
	private String courseName;
	private double credits;
	private Vector offeredAsSection; // of Section object references
	private Vector prerequisites; // of Course object references
	
	//----------------
	// Constructor(s).
	//----------------

	public Course(String cNo, String cName, double credits) {
		setCourseNo(cNo);
		setCourseName(cName);
		setCredits(credits);

		// Note that we must instantiate empty vectors.

		offeredAsSection = new Vector();
		prerequisites = new Vector();
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setCourseNo(String cNo) {
		courseNo = cNo;
	}
	
	public String getCourseNo() {
		return courseNo;
	}
	
	public void setCourseName(String cName) {
		courseName = cName;
	}
	
	public String getCourseName() {
		return courseName;
	}
	
	public void setCredits(double c) {
		credits = c;
	}
	
	public double getCredits() {
		return credits;
	}
	
	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// Used for testing purposes.

	public void display() {
		System.out.println("Course Information:");
		System.out.println("\tCourse No.:  " + getCourseNo());
		System.out.println("\tCourse Name:  " + getCourseName());
		System.out.println("\tCredits:  " + getCredits());
		System.out.println("\tPrerequisite Courses:");

		// We take advantage of another of the Course class's
		// methods in stepping through all of the prerequisites.

		if (hasPrerequisites()) {
			Enumeration e = getPrerequisites();
			while (e.hasMoreElements()) {
				Course c = (Course) e.nextElement();
				System.out.println("\t\t" + c.toString());
			}
		}
		else System.out.println("\t\t(none)");

		// Note use of print vs. println in next line of code.

		System.out.print("\tOffered As Section(s):  ");
		for (int i = 0; i < offeredAsSection.size(); i++) {
			Section s = (Section) offeredAsSection.elementAt(i);
			System.out.print(s.getSectionNo() + " ");
		}

		// Print a blank line.
		System.out.println("");
	}
	
	public String toString() {
		return getCourseNo() + ":  " + getCourseName();
	}

	public void addPrerequisite(Course c) {
		prerequisites.add(c);
	}

	public boolean hasPrerequisites() {
		if (prerequisites.size() > 0) return true;
		else return false;
	}

	public Enumeration getPrerequisites() {
		return prerequisites.elements();
	}

	public Section scheduleSection(int secNo, char day, String time, 
				       String room, int capacity) {
		// Create a new Section (note that we're now assigning
		// the section number by passing it in).
		Section s = new Section(secNo, day, time, this, room, capacity);
		
		// ... and then remember it!
		offeredAsSection.addElement(s);
		
		return s;
	}
}

