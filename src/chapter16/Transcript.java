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

public class Transcript {
	//------------
	// Attributes.
	//------------

	private Vector transcriptEntries; // of TranscriptEntry object references
	private Student studentOwner;

	//----------------
	// Constructor(s).
	//----------------

	public Transcript(Student s) {
		setStudentOwner(s);

		// Need to instantiate a new Vector.

		transcriptEntries = new Vector();
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setStudentOwner(Student s) {
		studentOwner = s;
	}

	public Student getStudentOwner() {
		return studentOwner;
	}

	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	public boolean verifyCompletion(Course c) {
		boolean outcome = false;

		// Step through all TranscriptEntries, looking for one
		// which reflects a Section of the Course of interest.

		for (int i = 0; i < transcriptEntries.size(); i++) {
			TranscriptEntry te = (TranscriptEntry) 
				transcriptEntries.elementAt(i);

			Section s = te.getSection();

			if (s.isSectionOf(c)) {
			    // Ensure that the grade was high enough.

			    if (TranscriptEntry.passingGrade(te.getGrade())) {
				outcome = true;

				// We've found one, so we can afford to
				// terminate the loop now.

				break;
			    }
			}
		}

		return outcome;
	}

	public void addTranscriptEntry(TranscriptEntry te) {
		transcriptEntries.add(te);
	}

	// Used for testing purposes.

	public void display() {
		System.out.println("Transcript for:  " +
				   getStudentOwner().toString());

		if (transcriptEntries.size() == 0)
			System.out.println("\t(no entries)");

		else for (int i = 0; i < transcriptEntries.size(); i++) {
			TranscriptEntry te = (TranscriptEntry) 
				transcriptEntries.elementAt(i);

			Section sec = te.getSection();

			Course c = sec.getRepresentedCourse();

			ScheduleOfClasses soc = sec.getOfferedIn();

			System.out.println("\tSemester:        " +
					   soc.getSemester());
			System.out.println("\tCourse No.:      " +
					   c.getCourseNo());
			System.out.println("\tCredits:         " +
					   c.getCredits());
			System.out.println("\tGrade Received:  " +
					   te.getGrade());
			System.out.println("\t-----");
		}
	}
}

