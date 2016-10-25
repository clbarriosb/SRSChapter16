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

public class ScheduleOfClasses extends CollectionWrapper {
	//------------
	// Attributes.
	//------------

	private String semester;

	// This Hashtable stores Section object references, using
	// a String concatenation of course no. and section no. as the
	// key, e.g., "MATH101 - 1".

	private Hashtable sections; 

	//----------------
	// Constructor(s).
	//----------------

	public ScheduleOfClasses(String semester) {
		setSemester(semester);
		
		// Instantiate a new Hashtable.

		sections = new Hashtable();
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSemester() {
		return semester;
	}

	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// Used for testing purposes.
	
	public void display() {
		System.out.println("Schedule of Classes for " + getSemester());
		System.out.println("");

		// Step through the Hashtable and display all entries.

		Enumeration e = sections.elements();

		while (e.hasMoreElements()) {
			Section s = (Section) e.nextElement();
			s.display();
			System.out.println("");
		}
	}

	public void addSection(Section s) {
		// We formulate a key by concatenating the course no.
		// and section no., separated by a hyphen.

		String key = s.getRepresentedCourse().getCourseNo() + 
			     " - " + s.getSectionNo();
		sections.put(key, s);

		// Bidirectionally hook the ScheduleOfClasses back to the Section.

		s.setOfferedIn(this);
	}

	public void parseData(String line) {
		// We're going to parse tab-delimited records into
		// six attributes -- courseNo, sectionNo, dayOfWeek, 
		// timeOfDay, room, and capacity.  We'll use courseNo to 
		// look up the appropriate Course object, and will then
		// call the scheduleSection() method to fabricate a
		// new Section object.

		// First, make a copy of the record.

		String restOfLine = line;
		int index = restOfLine.indexOf("\t");
		String courseNo = restOfLine.substring(0, index);
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");

		// We have to parse the next value as a number.
		String sectionNumber = restOfLine.substring(0, index);
		int sectionNo = Integer.parseInt(sectionNumber);
		restOfLine = restOfLine.substring(index+1);

		index = restOfLine.indexOf("\t");
		String dayOfWeek = restOfLine.substring(0, index);
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		String timeOfDay = restOfLine.substring(0, index);
		restOfLine = restOfLine.substring(index+1);
		index = restOfLine.indexOf("\t");
		String room = restOfLine.substring(0, index);
		
		// We have to parse the last value as a number.
		String capacityValue = restOfLine.substring(index+1);
		int capacity = Integer.parseInt(capacityValue);

		// Look up the Course object in the Course Catalog.
		// Having made courseCatalog a public static attribute
		// of the SRS class comes in handy!
		Course c = SRS.courseCatalog.findCourse(courseNo);

		// Schedule the Section.
		Section s = c.scheduleSection(sectionNo, dayOfWeek.charAt(0), 
					      timeOfDay, room, capacity);
		String key = courseNo + " - " + s.getSectionNo();
		addSection(s);
	}

	// The full section number is a concatenation of the
	// course no. and section no., separated by a hyphen;
	// e.g., "ART101 - 1".

	public Section findSection(String fullSectionNo) {
		return (Section) sections.get(fullSectionNo);
	}

	// We don't need this method, but we have to provide it because
	// we inherited an abstract signature.

	public void parseData2(String line) { }

	// This next method was added for use with the GUI.

	// Convert the contents of the Hashtable into a Vector
	// that is sorted in alphabetic order.

	public Vector getSortedSections() {
		Vector sortedKeys = new Vector();
		Vector sortedSections = new Vector();

		// Note that we can pull the keys as an Enumeration as
		// well as the objects themselves.

		Enumeration e = sections.keys();
		while (e.hasMoreElements()) {
			boolean inserted = false;
			String key = (String) e.nextElement();
			for (int i = 0; i < sortedKeys.size(); i++) {
				String val = (String) sortedKeys.elementAt(i);

				// The compareTo() method compares two Strings,
				// returning a negative value if "key" comes
				// alphabetically before "val", zero if 
				// they are equal, and a positive value if 
				// "key" comes alphabetically after "val".

				if (key.compareTo(val) <= 0) {
					sortedKeys.insertElementAt(key, i);
					inserted = true;
					break;
				}
			}

			// If it hasn't been inserted yet, stick it at the end
			// of the "sortedKeys" Vector.

			if (!inserted) sortedKeys.add(key);
		}

		// Now that "sortedKeys" Vector contains all of the KEYS in 
		// alphabetic order, we'll step through that Vector and use 
		// it to pull the Section objects themselves out of the 
		// Hashtable and into another Vector "sortedSections".

		for (int i = 0; i < sortedKeys.size(); i++) {
			String key = (String) sortedKeys.elementAt(i);
			Section s = (Section) sections.get(key);
			sortedSections.add(s);
		}

		return sortedSections;
	}
}

