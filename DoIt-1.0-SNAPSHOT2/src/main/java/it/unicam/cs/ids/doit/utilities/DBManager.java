package it.unicam.cs.ids.doit.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.StatiRichieste;
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
	
	private DBManager() {

	}
	
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
		String sql = "SELECT ID, Creatore, Selezionatore, Specifiche, Stato, Titolo, MAXPartecipanti, data  FROM Progetti";
		
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
			c.setData(rs.getDate(8));
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
				"join Competenze_Utente as c on id = c.competenza where utente ='"+ u.getUsername()+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Collection<String> e = new HashSet<String>();
		while(rs.next()) { 
			e.add(rs.getString(1));
			
		}
		return e;
	}

	public void insertUtente(Utente u, String password) throws SQLException {
		String sql = "INSERT INTO Utenti(username, nome, password, ruolo, email) VALUES(?, ?, ?, ?, ?)";
		
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getName());
		ps.setInt(3, password.hashCode());
		ps.setInt(4, u.getRuolo());
		ps.setString(5, u.getEmail());
		ps.executeUpdate();
		insertCompetenzeUtente(u, u.getCompetenze());
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		
		
	}
	public Ente getEnte(Utente u) throws SQLException {
		String sql = "SELECT ID, descrizione FROM enti where CapoGruppo = '"+ u.getUsername()+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Ente e = new Ente(u);
		while(rs.next()) { 
			e.setDescrizione(rs.getString(2));
			addCollaboratori(e,rs.getInt(1));
		}
		return e;
	}
	/*
	 * le collaborazioni con stato 0 sono quelle accettate
	 * */
	
	private void addCollaboratori(Ente e, int id) throws SQLException {
		String sql = "SELECT Utente FROM Collaborazione WHERE Ente = '" + id+"' and Stato = 0" ;
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) { 
			e.addCollaboratore(getUtente(rs.getString(1)));
			
		}
		
		
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
	public List<Utente> getListaUtenti() throws SQLException{
		String sql = "SELECT Username, Nome, Ruolo, email, password FROM Utenti";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		List<Utente> utenti = new ArrayList<Utente>();
		while(rs.next()) { 
			Utente u = new Utente(rs.getString(1));
			switch(rs.getInt(3)) {
			case 0: u.setRuolo(new Progettista(u));break;
			case 1: u.setRuolo(new Esperto(u)); break;
			case 2: u.setRuolo(getEnte(u));break;
			}
			u.insertName(rs.getString(2));
			u.insertEmail(rs.getString(4));
			//inserisco tutti gli utenti nelle mappe di SystemUtilities 
			SystemUtilities.getInstance().getPassword().put(u.getUsername(), rs.getInt(5));
			SystemUtilities.getInstance().getUtenti().put(u.getUsername(), u);
			u.getCompetenze().addAll(getCompetenze(u));
			utenti.add(u);
		}
		
		return utenti;
	}
	
	public void insertProgetto(Progetto p) throws SQLException {
		String sql = "INSERT INTO Progetti (ID, Creatore, Selezionatore, "
				+ "Specifiche, Stato, Titolo, MAXPartecipanti, data) VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, p.getId());
		ps.setString(2, p.getProponente().getUsername());
		ps.setString(3, p.getSelezionatore().getUsername());
		ps.setString(4, p.getSpecifiche());
		switch(p.getStato()) {
		case PENDING: ps.setInt(5, 0); break;
		case PUBBLICATO: ps.setInt(5, 1); break;
		case ARCHIVIATO: ps.setInt(5, 2); break;
		case IN_VALUTAZIONE: ps.setInt(5, 3); break;
		}
		ps.setString(6, p.getTitolo());
		ps.setInt(7, p.getNumPartecipanti());
		Calendar data = Calendar.getInstance();
		data.setTime(p.getData());
		String sqlData = data.get(Calendar.YEAR) + "-" + data.get(Calendar.MONTH) + "-"+ data.get(Calendar.DAY_OF_MONTH);
	 	ps.setString(8, sqlData);
	 	ps.executeUpdate();
		insertCompetenzeRichieste(p, p.getCompetenzeNecessarie());
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
	}
	public Map<String, Integer> getCompetenze() throws SQLException{
		String sql = "SELECT ID, Categoria FROM Competenze ";
		Map<String, Integer> competenze = new HashMap<String, Integer>();
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			competenze.put(rs.getString(2), rs.getInt(1));
		}
		
		return competenze;
	}
	
	public void insertCompetenzeRichieste(Progetto p, Set<String> competenze) throws SQLException {
		for (String string : competenze) {
		String sql = "INSERT INTO Competenze_RichiesteProgetto(Progetto, Competenza) VALUES(?, ?)";
		
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, p.getId());
		ps.setInt(2, SystemUtilities.getInstance().getMapCompetenze().get(string).intValue());
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		}
	}
	
	public void insertCompetenzeUtente(Utente u, Set<String> competenze) throws SQLException {
		for (String string : competenze) {
			String sql = "INSERT INTO Competenze_Utente(Utente, Competenza) VALUES(?, ?)";
			
			PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, u.getUsername());
			ps.setInt(2, SystemUtilities.getInstance().getMapCompetenze().get(string));
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			}
	}
	
	public void insertPartecipazione(Partecipazione p) throws SQLException {
		String sql = "INSERT INTO Partecipazioni(Utente, Progetto, Stato) VALUES(?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, p.getProgettista().getUsername());
		ps.setInt(2, p.getProgetto().getId());
		ps.setInt(3, 1);
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
	}
	
	public void modificaProgetto(Progetto p) throws SQLException {
		String sql = "DELETE FROM Progetti where ID ="+ p.getId();
		PreparedStatement ps = getConnection().prepareStatement(sql);
	}
	
	public void addPartecipazioni(Progetto p) throws SQLException {
		String sql = "SELECT Utente, Stato FROM Partecipazioni WHERE Progetto = "+ p.getId();
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Partecipazione par = new Partecipazione(getUtente(rs.getString(1)), p);
			switch (rs.getInt(2)) {
			case 0: par.setStato(StatiRichieste.CONFERMATO);break;

			case 1: par.setStato(StatiRichieste.IN_VALUTAZIONE);break;
			case 2: par.setStato(StatiRichieste.RIFIUTATO); break;
			}
//			p.getPartecipazioni().add(par);
		}
	}
	public void updatePartecipazione(Progetto p, Utente u, StatiRichieste s) throws SQLException {
		String sql = "UPDATE Partecipazioni SET Stato =" + s.ordinal()+ 
				"WHERE Progetto ="+ p.getId()+" and Utente ='"+ u.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.executeUpdate();
	}
	
	
}
