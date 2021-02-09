package it.unicam.cs.ids.doit.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Ruolo;
import it.unicam.cs.ids.doit.user.Utente;

public class DBManager {
	private static DBManager instance;
	private Connection connection;
	

	
	public static DBManager getInstance() {
		if (instance ==null)
			instance = new DBManager();
		return instance;
	}

	public Connection getConnection() throws SQLException {
		if(connection == null) {
			MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
			dataSource.setServerName("127.0.0.1");
			dataSource.setPortNumber(3306);
			dataSource.setUser("root");
			dataSource.setPassword("pass123");
			dataSource.setDatabaseName("ProgettoIDS");
			
			connection = dataSource.getConnection();
		}
		
		return connection;
	}
	
	public List<Progetto> listaProgetti() throws SQLException{
String sql = "SELECT titolo, specifiche, proponente FROM progetti";
		
		PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		List<Progetto> contatti = new ArrayList<Progetto>();
		while(rs.next()) {
			Progetto c = new Progetto(getUtente(rs.getString(3)));
			
//			(rs.getInt(1));
			c.setTitolo(rs.getString(1));
			c.setSpecifiche(rs.getString(2));
//			c.setNumPartecipanti(rs.getInt(3));
			
			contatti.add(c);
		}
		
		return contatti;
	}

	private Utente getUtente(String string) throws SQLException {
		String sql = "SELECT nome, cognome FROM utenti where username = '"+ string+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Utente u = new Utente(string);
		while(rs.next()) { u.insertName(rs.getString(1)+ " "+ rs.getString(2));}
//		switch(rs.getInt(3)) {
//		case 1:u.setRuolo(new Progettista(u));break;
//		case 2:u.setRuolo(new Esperto(u));break;
//		case 3:u.setRuolo(new Ente(u));break;	
//		}
		return u;
	}
	
	public void insertUtente(Utente u, String password) throws SQLException {
String sql = "INSERT INTO Utenti(username, nome, password) VALUES(?, ?, ?)";
		
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getName());
		ps.setInt(3, password.hashCode());
		
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		
		
	}
}
