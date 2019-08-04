package net.favor.imagecapter.hibernate;

import org.hibernate.Session;

public class Tester {
	public static void main(String[] args) {
		System.out.println("Try to get a session.");
		Session session = HibernateUtil.currentSession();
		System.out.println("Session obtained.");
		HibernateUtil.closeSession();
		System.out.println("Session closed.");
	}

}
