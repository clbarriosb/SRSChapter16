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

public abstract class Person {
	//------------
	// Attributes.
	//------------

	private String name;
	private String ssn;
	
	//----------------
	// Constructor(s).
	//----------------

	public Person(String name, String ssn) {
		setName(name);
		setSsn(ssn);
	}
	
	// We're replacing the default constructor that got "wiped out"
	// as a result of having created a constructor above.

	public Person() {
		setName("?");
		setSsn("???-??-????");
	}

	//-----------------
	// Get/set methods.
	//-----------------

	public void setName(String n) {
		name = n;
	}
	
	public String getName() {
		return name;
	}
	
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	public String getSsn() {
		return ssn;
	}

	//-----------------------------
	// Miscellaneous other methods.
	//-----------------------------

	// We'll let each subclass determine how it wishes to be
	// represented as a String value.

	public abstract String toString(); 

	// Used for testing purposes.

	public void display() {
		System.out.println("Person Information:");
		System.out.println("\tName:  " + getName());
		System.out.println("\tSoc. Security No.:  " + getSsn());
	}
}	

