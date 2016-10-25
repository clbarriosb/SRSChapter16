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


public class TranscriptEntry {
	//------------
	// Attributes.
	//------------

	private String grade;
	private Student student;
	private Section section;
	private Transcript transcript;

	//----------------
	// Constructor(s).
	//----------------

	public TranscriptEntry(Student s, String grade, Section se) {
		setStudent(s);
		setSection(se);
		setGrade(grade);

		// Obtain the Student's transcript ...

		Transcript t = s.getTranscript();

		// ... and then hook the Transcript and the TranscriptEntry
		// together bidirectionally.

		setTranscript(t);
		t.addTranscriptEntry(this);
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setStudent(Student s) {
		student = s;
	}

	public Student getStudent() {
		return student;
	}

	public void setSection(Section s) {
		section = s;
	}

	public Section getSection() {
		return section;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGrade() {
		return grade;
	}

	public void setTranscript(Transcript t) {
		transcript = t;
	}

	public Transcript getTranscript() {
		return transcript;
	}

	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// These next two methods are declared to be static, so that they
	// may be used as utility methods.

	public static boolean validateGrade(String grade) {
		boolean outcome = false;

		if (grade.equals("F") ||
		    grade.equals("I")) outcome = true;

		if (grade.startsWith("A") ||
		    grade.startsWith("B") ||
		    grade.startsWith("C") ||
		    grade.startsWith("D")) {
			if (grade.length() == 1) outcome = true;
			else if (grade.length() > 2) outcome = false;
			else {
				if (grade.endsWith("+") ||
				    grade.endsWith("-")) outcome = true;
				else outcome = false; 
			}
		}

		return outcome;
	}

	public static boolean passingGrade(String grade) {
		// First, make sure it is a valid grade.

		if (!validateGrade(grade)) return false;

		// Next, make sure that the grade is a D or better.

		if (grade.startsWith("A") ||
		    grade.startsWith("B") ||
		    grade.startsWith("C") ||
		    grade.startsWith("D")) return true;

		else return false;
	}
}

