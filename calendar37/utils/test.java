package utils;

public class test {
public static void main(String[] args) {
	System.out.println("\'manBats\'");
	DatabaseInterface test = new DatabaseInterface();
	try{
		System.out.println("" + test.getEmployeeNr("manBats"));
	} catch (Exception e){
		System.out.println("major prøblim");
	}
}
}
