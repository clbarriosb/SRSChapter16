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

public class SRS {
	// We can effectively create "global" data by declaring
	// PUBLIC STATIC attributes in the main class.  

	// Entry points/"roots" for getting at objects.  

	public static Faculty faculty = new Faculty();

	public static CourseCatalog courseCatalog = new CourseCatalog(); 

	public static ScheduleOfClasses scheduleOfClasses = 
		      new ScheduleOfClasses("SP2001");

	// We don't create a collection for Student objects, because
	// we're only going to handle one Student at a time -- namely,
	// whichever Student is logged on.
	//? public static StudentBody studentBody = new StudentBody();

	public static void main(String[] args) {
		// Initialize the key objects by reading data from files.
		faculty.initializeObjects("Faculty.dat", true);
		courseCatalog.initializeObjects("CourseCatalog.dat", true);
		scheduleOfClasses.initializeObjects("SoC_SP2001.dat", true);

		// We'll handle the students differently:  that is,
		// rather than loading them all in at application outset,
		// we'll pull in the data that we need just for one
		// Student when that Student logs on -- see the Student
		// class constructor for the details.
		//? studentBody.initializeObjects("StudentBody.dat");

		// Establish some prerequisites (c1 => c2 => c3 => c4).

		courseCatalog.initializeObjects("Prerequisites.dat", false);

		// Recruit a professor to teach each of the sections.

		faculty.initializeObjects("TeachingAssignments.dat", false);

		// Create and display an instance of the main GUI window.

		//? mainFrame = new MainFrame();
		new MainFrame();
	}
}

