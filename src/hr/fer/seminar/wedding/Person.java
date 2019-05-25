package hr.fer.seminar.wedding;

import java.util.LinkedList;
import java.util.List;

import hr.fer.seminar.aco.Vertex;
 
public class Person  extends Vertex {
		String name;
		String surname;
		String side;
		List<Person> familyList = new LinkedList<>();
		List<Person> friendList = new LinkedList<>();
		
		public Person(String name, String surname , String side, int i) {
			super(name + " " + surname, i);
			this.name = name;
			this.surname = surname;
			this.side = side;
		}
		
		public Person(String name, String surname , int i) {
			super(name + " " + surname, i);
			this.name = name;
			this.surname = surname;
		}
		
		@Override
		public boolean equals(Object obj) {
			Person person = (Person) obj;
			if (person.name.equals(this.name) && person.surname.equals(this.surname)) {
				return true;
			}
			return false;
		}
		
		@Override
		public double measureDistance(Vertex v, double[][] distances) {
			Person p = (Person) v;
			return distances[this.getPosition()][p.getPosition()];
		}
		
		public void addToFamilyList(Person person) {
			familyList.add(person);
		}
		
		public void addToFriendList(Person person) {
			friendList.add(person);
		}
		@Override
		public String toString() {
			String family = printList(familyList);
			String friends = printList(friendList);
			return String.format("%s %s", this.name, this.surname, family, friends);
		}
		
		private String printList(List<Person> list) {
			String print = " ";
			for(Person person : list) {
				print += person.name + " " + person.surname +  " ";
			}
			return print;
		}

}
