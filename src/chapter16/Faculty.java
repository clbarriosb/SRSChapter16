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

public class Faculty extends CollectionWrapper {
	//------------
	// Attributes.
	//------------

	// This Hashtable stores Professor object references, using
	// the (String) ssn of the Professor as the key.

	private Hashtable professors;

	//----------------
	// Constructor(s).
	//----------------

	public Faculty() {
		// Instantiate a new Hashtable.

		professors = new Hashtable();
	}

	//-----------------
	// Get/set methods.
	//-----------------


	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// Used for testing purposes.
	
	public void display() {
		System.out.println("Faculty:");
		System.out.println("");

		// Step through the Hashtable and display all entries.

		Enumeration e = professors.elements();

		while (e.hasMoreElements()) {
			Professor p = (Professor) e.nextElement();
			p.display();
			System.out.println("");
		}
	}

	public void addProfessor(Professor p) {
		professors.put(p.getSsn(), p);
	}

	public void parseData(String line) {
		// We're going to parse tab-delimited records into
		// four attributes -- name, ssn, title, and dept --
		// and then call the Professor constructor to fabricate a new
		// professor.

		// First, make a copy of the record.

		String restOfLine = line;
		int index = restOfLine.indexOf("\t");
		String name = restOfLine.substring(0, index);
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		String ssn = restOfLine.substring(0, index);
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		String title = restOfLine.substring(0, index);
		String dept = restOfLine.substring(index+1);
		
		// Call the constructor ...
		Professor p = new Professor(name, ssn, title, dept);
		addProfessor(p);
	}

	public Professor findProfessor(String ssn) {
		return (Professor) professors.get(ssn);
	}

	// We have to read a second file containing the teaching
	// assignments.
	// This next version is used when reading in the file that defines
	// teaching assignments.

	public void parseData2(String line) {
		// We're going to parse tab-delimited records into
		// two values, representing the professor's SSN
		// and the section number that he/she is going to teach.

		// First, make a copy of the record.

		String restOfLine = line;
		int index = restOfLine.indexOf("\t");
		String ssn = restOfLine.substring(0, index);

		// The full section number is a concatenation of the
		// course no. and section no., separated by a hyphen;
		// e.g., "ART101 - 1".

		String fullSectionNo = restOfLine.substring(index+1);

		// Look these two objects up in the appropriate collections.
		// Note that having made scheduleOfClasses a public
		// static attribute of the SRS class helps!

		Professor p = findProfessor(ssn); 
		Section s = SRS.scheduleOfClasses.findSection(fullSectionNo); 
		if (p != null && s != null) p.agreeToTeach(s);
	}

	// Test scaffold.
	public static void main(String[] args) {
		Faculty f = new Faculty();
		f.initializeObjects("Faculty.dat", true);

		// We cannot test the next feature, because the code
		// of parseData2() expects the SRS.scheduleOfClasses
		// collection object to have been instantiated, but 
		// it will not have been if we are running this test
		// scaffold instead.
		// f.initializeObjects("TeachingAssignments.dat", false);

		f.display();
	}
}

