package models;

/**
 * This is the base-class that initializes a person in our database, making him/her eligible for an {@link Account}.
 * This class is an absolute requirement for using the Calendar-system, as a person cannot have an
 * {@link Account} without it.
 * 
 * @author gruppe37
 * @version %I%, %G%
 */
public class Person {

	//Final attributes
	private final int employee_nr;
	private final String first_name;
	private final String last_name;
	private final String mobile_nr;
	
	//Property-attributes
	
	//Constructor
	public Person(int employee_nr, String first_name, String last_name, String internal_nr, String mobile_nr) {
		this.employee_nr = employee_nr;
		this.first_name = first_name;
		this.last_name = last_name;
		this.mobile_nr = mobile_nr;
	}
	
	//Getters, Setters & Properties
	public int getEmployee_nr() {
		return employee_nr;
	}
	
	public String getFirst_name() {
		return first_name;
	}
	
	public String getLast_name() {
		return last_name;
	}
	
	public String getMobile_nr() {
		return mobile_nr;
	}
}
