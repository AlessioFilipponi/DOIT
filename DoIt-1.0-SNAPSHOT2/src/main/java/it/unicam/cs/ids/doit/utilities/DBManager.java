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

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.RichiestaValutazione;
import it.unicam.cs.ids.doit.notifiche.StatiRichieste;
import it.unicam.cs.ids.doit.notifiche.Subject;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.progetto.Valutazione;
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
//			dataSource.setServerName("www.innovation-technology.it");
//			dataSource.setPortNumber(3306);
//			dataSource.setUser("doit");
//			dataSource.setPassword("doit");
//			dataSource.setDatabaseName("doit");
//			dataSource.setServerName("innovationtechnolog.ddns.net");
//			dataSource.setPortNumber(3306);
//			dataSource.setUser("doit");
//			dataSource.setPassword("doit");
//			dataSource.setDatabaseName("doit");
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
			addPartecipazioni(c);
			c.getValutazioni().addAll(getValutazioni(c));
			progetti.add(c);
		}
		
		return progetti;
	}

	private Collection<? extends Valutazione> getValutazioni(Progetto c) throws SQLException {
		String sql = "SELECT * FROM Valutazioni_Progetti WHERE Progetto ="+ c.getId();
		 PreparedStatement ps = getConnection().prepareStatement(sql);
			
		 ResultSet rs = ps.executeQuery();
         Set<Valutazione> s = new HashSet<Valutazione>(); 
		 while(rs.next()) { 
			 Valutazione v = new Valutazione(getUtente(rs.getString(2)), rs.getInt(4), rs.getString(4));
             s.add(v);

		 }
		 return s;
	}

	private Utente getUtente(String string) throws SQLException {
		if (SystemUtilities.getInstance().getUtenti().get(string)==null ){
		String sql = "SELECT Nome, Ruolo, email, Password FROM Utenti where Username = '"+ string+"'";
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
			u.setEmail(rs.getString(3));
			u.getCompetenze().addAll(getCompetenze(u));
			SystemUtilities.getInstance().getPassword().put(u.getUsername(), rs.getInt(4));
			SystemUtilities.getInstance().getUtenti().put(u.getUsername(), u);
		}

		return u;}
		else
			return SystemUtilities.getInstance().getUtente(string);
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

	public void insertUtente(Utente u, String password) throws SQLException  {
		String sql = "INSERT INTO utenti(username, nome, password, ruolo, email) VALUES(?, ?, ?, ?, ?);";
		
		PreparedStatement ps;

			ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
		String sql = "SELECT Utente FROM Collaborazione WHERE Ente = '" + id+"' and Stato = 1" ;
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
			u.setEmail(rs.getString(4));
			//inserisco tutti gli utenti nelle mappe di SystemUtilities 
			SystemUtilities.getInstance().getPassword().put(u.getUsername(), rs.getInt(5));
			SystemUtilities.getInstance().getUtenti().put(u.getUsername(), u);
			u.getCompetenze().addAll(getCompetenze(u));
			u.getNotifiche().addAll(getNotifiche(u));
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
		ps.execute(sql);
		insertProgetto(p);
	}
	
	public void addPartecipazioni(Progetto p) throws SQLException {
		String sql = "SELECT Utente, Stato, Verso FROM Partecipazioni WHERE Progetto = "+ p.getId();
		PreparedStatement ps = getConnection().prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			Utente u = getUtente(rs.getString(1));
			Partecipazione par = new Partecipazione(u, p);
			switch (rs.getInt(2)) {
			case 0: par.setStato(StatiRichieste.CONFERMATO); 
			break;

			case 1: par.setStato(StatiRichieste.IN_VALUTAZIONE); u.addNotifica(par); u.update();break;
			case 2: par.setStato(StatiRichieste.RIFIUTATO); break;
			}
			p.getPartecipazioni().add(par);
			
		}
	}
	public void updatePartecipazione(Progetto p, Utente u, StatiRichieste s) throws SQLException {
		String sql = "UPDATE Partecipazioni SET Stato =" + s.ordinal()+ 
				"WHERE Progetto ="+ p.getId()+" and Utente ='"+ u.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.executeUpdate();
	}
	
	public void insertInvito(Invito i) throws SQLException {
		String sql = "INSERT INTO Collaborazione(Utente, Ente, Stato) VALUES(?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, i.getProgettista().getUsername());
		ps.setInt(2, getEnteId(i.getEnte()));
		ps.setInt(3, 1);
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
	}
	
	private int getEnteId(Utente ente) throws SQLException {
		String sql = "SELECT Id FROM Ente WHERE CapoGruppo= '"+ ente.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		int id = -1;
		while(rs.next()) {
			id = rs.getInt(1);
		}
		return id;
	}
	public Set<Subject<Utente>> getNotifiche(Utente u) throws SQLException {
		String sql ="SELECT Id, Tipo, IDtab FROM Notifiche WHERE Destinatario ='"+ u.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Set<Subject<Utente>> s = new HashSet<Subject<Utente>>();
		s.addAll(getInviti(u));
		while(rs.next()) {
			switch (rs.getInt(2)) {
			//se sono richieste di valutazione di un progetto
			case 2: s.add(new RichiestaValutazione(u, getProgetto(rs.getInt(3))));
				break;
 
			}
		}
		return s;
	}
	
   private Progetto getProgetto(int id) {
	   List<Progetto> progetto = Bacheca.getInstance().getCatalogoProgetti().search(p->p.getId()==id);
	   return progetto.get(0);
   }
	

	public Set<Invito> getInviti(Utente u) throws SQLException {
		String sql = "SELECT Utente, Ente FROM Inviti WHERE Utente = '"+ u.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		Set<Invito> s = new HashSet<Invito>();
		while(rs.next()) {
			s.add(new Invito(u, getUtente(rs.getString(2))));
		}
		return s;
	}
    
	public void updateInvito(Invito i) throws SQLException {
    	String sql = "update Collaborazione set stato =" +i.getStato().ordinal()+" where Utente ='"+ i.getProgettista()+"' and Ente = "+ getEnteId(i.getEnte());	
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    }
    
    public void updatePartecipazione(Partecipazione p) throws SQLException {
    	String sql = "update Partecipazione set stato =" +p.getStato().ordinal()+" where Utente ='"+ p.getProgettista()+"' and Progetto = "+ getProgetto(p.getProgetto().getId());	
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    	
    }
    
    public void updaterichiestaValutazione(RichiestaValutazione r) throws SQLException {
    	String sql = "update Notifiche set stato =" +r.getStato()+ " where Utente= '" +r.getEsperto()+ "'and IDtab = "+r.getProgetto();
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    }
    public void insertRichiestaValutazioneProgetto(RichiestaValutazione r) throws SQLException {
    	String sql = "INSERT INTO Notifiche(Destinatario, Testo, Tipo, IDtab) VALUES(?,?,?,?)";
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, r.getEsperto().getUsername());
		ps.setString(2, r.toString());
		ps.setInt(3, 1);
		ps.setInt(4, r.getProgetto().getId());
		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
    } 
}
