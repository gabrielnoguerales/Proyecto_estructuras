package es.uned.lsi.eped.pract2018_2019;

import es.uned.lsi.eped.DataStructures.List;

public class Test {
	public static void main(String[] args) {
		List list1 = new List();
		list1.insert(1,1);
		list1.insert(1,5);
		list1.remove(2);
		System.out.println(list1.get(list1.size()));
	}
}
