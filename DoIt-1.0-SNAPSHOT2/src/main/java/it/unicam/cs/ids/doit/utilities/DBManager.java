package it.unicam.cs.ids.doit.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
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
			dataSource.setServerName("www.innovation-technology.it");
			dataSource.setPortNumber(3306);
			dataSource.setUser("doit");
			dataSource.setPassword("doit");
			dataSource.setDatabaseName("doit");
			
			connection = dataSource.getConnection();
		}
		
		return connection;
	}
	
	public List<Progetto> listaProgetti() throws SQLException{
		String sql = "SELECT ID, Creatore, Selezionatore, Specifiche, Stato, Titolo, MAXPartecipanti  FROM Progetti";
		
		PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		
		List<Progetto> progetti = new ArrayList<Progetto>();
		while(rs.next()) {
			Progetto c = new Progetto(getUtente(rs.getString(2)));
			c.setId(rs.getInt(1));
            c.setSelezionatore(getUtente(rs.getString(3)));
			c.setTitolo(rs.getString(6));
			c.setSpecifiche(rs.getString(4));
			switch(rs.getInt(5)) {
			case 0 : c.setStato(StatiProgetto.PENDING);break;
			case 1 : c.setStato(StatiProgetto.PUBBLICATO);break;
			case 2 : c.setStato(StatiProgetto.IN_VALUTAZIONE);break;
			case 3 : c.setStato(StatiProgetto.ARCHIVIATO);break;
			
			}
			c.setNumPartecipanti(rs.getInt(7));
			c.setCompetenzeProgettisti(getCompetenzeProgetto(c));
			progetti.add(c);
		}
		
		return progetti;
	}

	private Utente getUtente(String string) throws SQLException {
		String sql = "SELECT Nome, Ruolo, email FROM Utenti where Username = '"+ string+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Utente u = new Utente(string);
		while(rs.next()) { 
			
			switch(rs.getInt(2)) {
			case 0: u.setRuolo(new Progettista(u));break;
			case 1: u.setRuolo(new Esperto(u)); break;
			case 2: u.setRuolo(new Ente(u));break;
			}
			u.insertName(rs.getString(1));
			u.insertEmail(rs.getString(3));
//			u.getCompetenze().addAll(getCompetenze(u));
		}

		return u;
	}
	
	private Collection<? extends String> getCompetenze(Utente u) throws SQLException {
		String sql = "select comp.categoria, comp.competenza, comp.descrizione from Competenze as comp \n" + 
				"join Competenze_Utente as c on id = c.competenza where utente '"+ u.getUsername()+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Collection<String> e = new HashSet<String>();
		while(rs.next()) { 
			e.add(rs.getString(1));
			
		}
		return e;
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
	public Ente getEnte(Utente u) throws SQLException {
		String sql = "SELECT descrizione FROM enti where username = '"+ u.getUsername()+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Ente e = new Ente(u);
		while(rs.next()) { 
			e.setDescrizione(rs.getString(1));
			
		}
		return e;
	}
	
	public Set<String> getCompetenzeProgetto(Progetto p) throws SQLException{
		String sql = "select comp.categoria, comp.competenza, comp.descrizione from Competenze as comp\n" + 
				"join Competenze_RichiesteProgetto as c on id = c.competenza where Progetto =" +p.getId();
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		Set<String> e = new HashSet<String>();
		while(rs.next()) { 
			e.add(rs.getString(1));

		}
		return e;
		
	}
}
