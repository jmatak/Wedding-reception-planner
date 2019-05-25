package hr.fer.seminar.wedding;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Potrebno prepraviti zbog komplikacija !
public class WeddingCorrelation {
	public static double evaluate(Person person1, Person person2) {
		double chemistry = 100;
		if (person1.side.equals(person2.side)) {
			chemistry /= 2;
		}
		if (person1.familyList.contains(person2)) {
			chemistry /= 4;
		}
		if (person1.friendList.contains(person2)) {
			chemistry /= 2;
		}
		return chemistry;
	}

	public static Person makePerson(String t, int position) {
		String[] split = t.split(" +");
		String ime = split[0];
		String prezime = split[1];
		Person person = new Person(ime, prezime, split[2], position);
		boolean del = false;
		for (int i = 3; i < split.length; i += 2) {
			if (split[i].equals("|")) {
				del = true;
				i--;
				continue;
			}
			Person temp = new Person(split[i], split[i + 1] , 0);
			if (!del) {
				person.addToFamilyList(temp);
			} else {
				person.addToFriendList(temp);
			}
		}
		return person;
	}

	public static ArrayList<Person> addPersons(ArrayList<Person> weddingList, Path src) {

		List<String> list = new ArrayList<>();
		try {
			list = Files.readAllLines(src);
		} catch (IOException e) {}
		
		int index = 0;
		for (String line : list) {
			weddingList.add(makePerson(line, index++));
		}
		
		double[][] chemistry = new double[weddingList.size()][weddingList.size()];
		int i = 0, j = 0;
		for (Person person1 : weddingList) {
			j = 0;
			for (Person person2 : weddingList) {
				if (person1.equals(person2)) {
					chemistry[i][j] = 0;
					continue;
				}
				chemistry[i][j] = evaluate(person1, person2);
				j++;
			}
			i++;
		}
		return weddingList;

	}
}
