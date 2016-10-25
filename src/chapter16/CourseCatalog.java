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
import java.io.*;

public class CourseCatalog extends CollectionWrapper {
	//------------
	// Attributes.
	//------------

	// This Hashtable stores Course object references, using
	// the (String) course no. of the Course as the key.

	private Hashtable courses;

	//----------------
	// Constructor(s).
	//----------------

	public CourseCatalog() {
		// Instantiate a new Hashtable.

		courses = new Hashtable();
	}

	//-----------------
	// Get/set methods.
	//-----------------


	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// Used for testing purposes.
	
	public void display() {
		System.out.println("Course Catalog:");
		System.out.println("");

		// Step through the Hashtable and display all entries.

		Enumeration e = courses.elements();

		while (e.hasMoreElements()) {
			Course c = (Course) e.nextElement();
			c.display();
			System.out.println("");
		}
	}

	public void addCourse(Course c) {
		// We use the course no. as the key.

		String key = c.getCourseNo();
		courses.put(key, c);
	}

	public void parseData(String line) {
		// We're going to parse tab-delimited records into
		// three attributes -- courseNo, courseName, and credits --
		// and then call the Course constructor to fabricate a new
		// course.

		// First, make a copy of the record.

		String restOfLine = line;

		// Use the indexOf() method to search for the first
		// occurrence of a tab character.  The variable "index"
		// will contain an integer pointing to the character
		// position in the record (starting with 0 for the first
		// position) of where the first tab character sits.

		int index = restOfLine.indexOf("\t");

		// We subdivide the line into two segments:  the portion
		// which precedes the tab ...

		String courseNo = restOfLine.substring(0, index);

		// ... and the portion which follows it (note that by
		// adding 1 to the value of index we have jumped past
		// the first tab character).

		restOfLine = restOfLine.substring(index+1);

		// We now search the REST of the line for the NEXT
		// tab character, and once again subdivide the
		// remainder of the record into two segments.

		index = restOfLine.indexOf("\t");
		String courseName = restOfLine.substring(0, index);
		String creditValue = restOfLine.substring(index+1);

		// We have to convert the last value into a number,
		// using a static method on the Double class to do so.

		double credits = Double.parseDouble(creditValue);

		// Finally, we call the Course constructor to create
		// an appropriate Course object, and store it in our
		// collection.

		Course c = new Course(courseNo, courseName, credits);
		addCourse(c);
	}

	public Course findCourse(String courseNo) {
		return (Course) courses.get(courseNo);
	}

	// We must read a second file defining the prerequisites, in
	// order to "hook" Course objects together.
	// This next version is used when reading in prerequisites.

	public void parseData2(String line) {
		// We're going to parse tab-delimited records into
		// two values, representing the courseNo "A" of 
		// a course that serves as a prerequisite for 
		// courseNo "B".

		// First, make a copy of the record.

		String restOfLine = line;
		int index = restOfLine.indexOf("\t");
		String courseNoA = restOfLine.substring(0, index);
		String courseNoB = restOfLine.substring(index+1);

		// Look these two courses up in the CourseCatalog.

		Course a = findCourse(courseNoA); 
		Course b = findCourse(courseNoB); 
		if (a != null && b != null) b.addPrerequisite(a);
	}

	// Test scaffold.
	public static void main(String[] args) {
		// We instantiate a CourseCatalog object ...

		CourseCatalog cc = new CourseCatalog();

		// ... cause it to read both the CourseCatalog.dat and
		// Prerequisites.dat files, thereby testing both
		// the parseData() and parseData2() methods internally
		// to the initializeObjects() method ...

		cc.initializeObjects("CourseCatalog.dat", true);
		cc.initializeObjects("Prerequisites.dat", false);

		// ... and use its display() method to demonstrate the
		// results!

		cc.display();
	}
}

