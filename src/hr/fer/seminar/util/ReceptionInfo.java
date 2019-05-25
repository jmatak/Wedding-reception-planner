package hr.fer.seminar.util;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import hr.fer.seminar.wedding.Person;
import hr.fer.seminar.wedding.WeddingCorrelation;

public class ReceptionInfo {
	private  Path src ;
	public ArrayList<Person> initialRoute = null;
	
	public ReceptionInfo(String s) throws InvalidPathException {
		this.src = Paths.get(s);
		initialRoute = WeddingCorrelation.addPersons(new ArrayList<>(), src);
	}
}
