package com.lotus.rest.dao;

import java.util.List;

import com.lotus.rest.domain.Person;

public class PersonOJDBCDAO implements PersonDAO {
	private static PersonOJDBCDAO instance = null;
	
	public static PersonOJDBCDAO getInstance() {
		if(instance == null) {
			instance = new PersonOJDBCDAO();
		}
		return instance;
	}
	
	private PersonOJDBCDAO() {
		
	}
	
	
	public void create(Person p) {
		// TODO Auto-generated method stub
		
	}

	public void update(Person p) {
		// TODO Auto-generated method stub
		
	}

	public void delete(Person p) {
		// TODO Auto-generated method stub
		
	}

	public List<Person> list() {
		// TODO Auto-generated method stub
		return null;
	}

	public Person getByName() {
		// TODO Auto-generated method stub
		return null;
	}

}
