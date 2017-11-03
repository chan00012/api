package com.lotus.rest.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.lotus.rest.domain.Animal;

public class AnimalOJDBDAO implements AnimalDAO {
	private static AnimalOJDBDAO instance = null;

	public static AnimalOJDBDAO getInstance() {
		if (instance == null) {
			instance = new AnimalOJDBDAO();
		}
		return instance;
	}

	private AnimalOJDBDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Unable to establish database connection");
		}
	}

	private static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "cedric",
				"password");
		connection.setAutoCommit(false);
		return connection;
	}

	public void create(Animal a) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			statement = connection.prepareStatement(
					"INSERT INTO ANIMALS(id,name, species, energy, aliveyn) VALUES(animals_seq.nextval, ?, ?, ? ,?)");
			statement.setString(1, a.getName());
			statement.setString(2, a.getSpecies());
			statement.setInt(3, a.getEnergy());
			statement.setBoolean(4, a.isAlive());

			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
			
		}

	}

	public void update(Animal a) {
		if (a == null || a.getId() == 0) {
			throw new IllegalArgumentException("Attempt to update a non-existing or non persisted animal");
		}
		
		Connection connection = null;
		PreparedStatement statement = null;
		
		try {
			connection = getConnection();
			statement = connection.prepareStatement(
					"UPDATE animals set name = ?, species = ?, energy = ?, aliveyn = ? where id = ?");
			statement.setString(1, a.getName());
			statement.setString(2, a.getSpecies());
			statement.setInt(3, a.getEnergy());
			statement.setBoolean(4, a.isAlive());
			statement.setLong(5, a.getId());

			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

			throw new RuntimeException("Data error: " + e.getMessage());
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
			
		}

	}

	public void delete(Animal a) {
		if (a == null || a.getId() == 0) {
			throw new IllegalArgumentException("Attempt to delete a non-existing or non persisted animal");
		}

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			statement = connection.prepareStatement("DELETE from animals where id = ?");
			statement.setLong(1, a.getId());
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
			
			
		}

	}

	public List<Animal> list() {
		List<Animal> animals = new ArrayList<Animal>();
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sql = "SELECT id, name, aliveYN, energy, species FROM Animals";

			ResultSet rs = statement.executeQuery(sql);
			// STEP 5: Extract data from result set
			while (rs.next()) {
				animals.add(extractAnimalFromResult(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return animals;
	}

	private Animal extractAnimalFromResult(ResultSet rs) throws SQLException {
		long id = rs.getLong("id");
		String name = rs.getString("name");
		String species = rs.getString("species");
		int energy = rs.getInt("energy");
		boolean alive = rs.getBoolean("aliveYN");
		Animal animal = new Animal(id, name, species, energy, alive);
		return animal;
	}

	public Animal getByName(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is required");
		}
		Animal animal = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT id, name, aliveYN, energy, species FROM Animals where name = ?");
			statement.setString(1, name);

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				animal = extractAnimalFromResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return animal;
	}

	public Animal getById(long id) {
		if (id == 0) {
			throw new IllegalArgumentException("valid id is required");
		}
		Animal animal = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT id, name, aliveYN, energy, species FROM Animals where id = ?");
			statement.setLong(1, id);

			ResultSet rs = statement.executeQuery();

			if (rs.next()) {
				animal = extractAnimalFromResult(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return animal;
	}

}
