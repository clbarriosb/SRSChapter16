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

public class Section {
	//------------
	// Attributes.
	//------------

	private int sectionNo;
	private char dayOfWeek;
	private String timeOfDay;
	private String room;
	private int seatingCapacity;
	private Course representedCourse;
	private ScheduleOfClasses offeredIn;
	private Professor instructor;

	// The enrolledStudents Hashtable stores Student object references,
	// using each Student's ssn as a String key.

	private Hashtable enrolledStudents; 

	// The assignedGrades Hashtable stores TranscriptEntry object
	// references, using a reference to the Student to whom it belongs 
	// as the key.

	private Hashtable assignedGrades; 
	
	// We use PUBLIC STATIC int attributes to declare status codes 
	// that other classes can then inspect/interpret (see method
	// reportStatus() in class SRS.java for an example of how these
	// are used).

	public static final int SUCCESSFULLY_ENROLLED = 0;
	public static final int SECTION_FULL = 1;
	public static final int PREREQ_NOT_SATISFIED = 2;
	public static final int PREVIOUSLY_ENROLLED = 3;

	//----------------
	// Constructor(s).
	//----------------

	public Section(int sNo, char day, String time, Course course,
		       String room, int capacity) {
		setSectionNo(sNo);
		setDayOfWeek(day);
		setTimeOfDay(time);
		setRepresentedCourse(course);
		setRoom(room);
		setSeatingCapacity(capacity);

		// A Professor has not yet been identified.

		setInstructor(null);

		// Note that we must instantiate empty hash tables.

		enrolledStudents = new Hashtable();
		assignedGrades = new Hashtable();
	}
									
	//-----------------
	// Get/set methods.
	//-----------------

	public void setSectionNo(int no) {
		sectionNo = no;
	}
	
	public int getSectionNo() {
		return sectionNo;
	}
	
	public void setDayOfWeek(char day) {
		dayOfWeek = day;
	}
	
	public char getDayOfWeek() {
		return dayOfWeek;
	}
		
	public void setTimeOfDay(String time) {
		timeOfDay = time;
	}
	
	public String getTimeOfDay() {
		return timeOfDay;
	}

	public void setInstructor(Professor prof) {
		instructor = prof;
	}
	
	public Professor getInstructor() {
		return instructor;
	}
	
	public void setRepresentedCourse(Course c) {
		representedCourse = c;
	}
	
	public Course getRepresentedCourse() {
		return representedCourse;
	}		

	public void setRoom(String r) {
		room = r;
	}

	public String getRoom() {
		return room;
	}

	public void setSeatingCapacity(int c) {
		seatingCapacity = c;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setOfferedIn(ScheduleOfClasses soc) {
		offeredIn = soc;
	}

	public ScheduleOfClasses getOfferedIn() {
		return offeredIn;
	}	

	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// For a Section, we want its String representation to look
	// as follows:
	//
	//	MATH101 - 1 - M - 8:00 AM

	public String toString() {
		return getRepresentedCourse().getCourseNo() + " - " +
		       getSectionNo() + " - " +
		       getDayOfWeek() + " - " +
		       getTimeOfDay();
	}

	// The full section number is a concatenation of the
	// course no. and section no., separated by a hyphen;
	// e.g., "ART101 - 1".

	public String getFullSectionNo() {
		return getRepresentedCourse().getCourseNo() + 
		       " - " + getSectionNo();
	}

	public int enroll(Student s) {
		// First, make sure that this Student is not already
		// enrolled for this Section, and that he/she has
		// NEVER taken and passed the course before.  
		
		Transcript transcript = s.getTranscript();

		if (s.isCurrentlyEnrolledInSimilar(this) || 
		    transcript.verifyCompletion(this.getRepresentedCourse()))
			return PREVIOUSLY_ENROLLED;

		// If there are any prerequisites for this course,
		// check to ensure that the Student has completed them.

		Course c = getRepresentedCourse();
		if (c.hasPrerequisites()) {
			Enumeration e = c.getPrerequisites(); 
			while (e.hasMoreElements()) {
				Course pre = (Course) e.nextElement();
	
				// See if the Student's Transcript reflects
				// successful completion of the prerequisite.

				if (!transcript.verifyCompletion(pre)) 
					return PREREQ_NOT_SATISFIED;
			}
		}
		
		// If the total enrollment is already at the
		// the capacity for this Section, we reject this 
		// enrollment request.

		if (!confirmSeatAvailability()) return SECTION_FULL;
		
		// If we made it to here in the code, we're ready to
		// officially enroll the Student.

		// Note bidirectionality:  this Section holds
		// onto the Student via the Hashtable, and then
		// the Student is given a handle on this Section.

		enrolledStudents.put(s.getSsn(), s);
		s.addSection(this);
		return SUCCESSFULLY_ENROLLED;
	}
	
	// A private "housekeeping" method.

	private boolean confirmSeatAvailability() {
		if (enrolledStudents.size() < getSeatingCapacity()) 
			return true;
		else return false;
	}

	public boolean drop(Student s) {
		// We may only drop a student if he/she is enrolled.

		if (!s.isEnrolledIn(this)) return false;
		else {
			// Find the student in our Hashtable, and remove it.

			enrolledStudents.remove(s.getSsn());

			// Note bidirectionality.

			s.dropSection(this);
			return true;
		}
	}

	public int getTotalEnrollment() {
		return enrolledStudents.size();
	}	

	// Used for testing purposes.

	public void display() {
		System.out.println("Section Information:");
		System.out.println("\tSemester:  " + getOfferedIn().getSemester());
		System.out.println("\tCourse No.:  " + 
				   getRepresentedCourse().getCourseNo());
		System.out.println("\tSection No:  " + getSectionNo());
		System.out.println("\tOffered:  " + getDayOfWeek() + 
				   " at " + getTimeOfDay());
		System.out.println("\tIn Room:  " + getRoom());
		if (getInstructor() != null) 
			System.out.println("\tProfessor:  " + 
				   getInstructor().getName());
		displayStudentRoster();
	}
	
	// Used for testing purposes.

	public void displayStudentRoster() {
		System.out.print("\tTotal of " + getTotalEnrollment() + 
				   " students enrolled");

		// How we punctuate the preceding message depends on 
		// how many students are enrolled (note that we used
		// a print() vs. println() call above).

		if (getTotalEnrollment() == 0) System.out.println(".");
		else System.out.println(", as follows:");

		// Use an Enumeration object to "walk through" the
		// hash table.

		Enumeration e = enrolledStudents.elements();
		while (e.hasMoreElements()) {
			Student s = (Student) e.nextElement();
			System.out.println("\t\t" + s.getName());
		}
	}

	// This method returns the value null if the Student has not
	// been assigned a grade.

	public String getGrade(Student s) {
		// Retrieve the Student's transcript entry object, if one
		// exists, and in turn retrieve its assigned grade.

		TranscriptEntry te = (TranscriptEntry) assignedGrades.get(s);
		if (te != null) {
			String grade = te.getGrade(); 
			return grade;
		}

		// If we found no TranscriptEntry for this Student, return
		// a null value to signal this.

		else return null;
	}

	public boolean postGrade(Student s, String grade) {
		// Make sure that we haven't previously assigned a
		// grade to this Student by looking in the Hashtable
		// for an entry using this Student as the key.  If
		// we discover that a grade has already been assigned,
		// we return a value of false to indicate that
		// we are at risk of overwriting an existing grade.  
		// (A different method, eraseGrade(), can then be written
		// to allow a Professor to change his/her mind.)

		if (assignedGrades.get(s) != null) return false;

		// First, we create a new TranscriptEntry object.  Note
		// that we are passing in a reference to THIS Section,
		// because we want the TranscriptEntry object,
		// as an association class ..., to maintain
		// "handles" on the Section as well as on the Student.
		// (We'll let the TranscriptEntry constructor take care of
		// "hooking" this T.E. to the correct Transcript.)

		TranscriptEntry te = new TranscriptEntry(s, grade, this);

		// Then, we "remember" this grade because we wish for
		// the connection between a T.E. and a Section to be
		// bidirectional.

		assignedGrades.put(s, te);

		return true;
	}
	
	public boolean isSectionOf(Course c) {
		if (c == representedCourse) return true;
		else return false;
	}
}
