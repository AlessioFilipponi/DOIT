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

/**
 * Classe che gestisce l'interazione con il Database
 *
 */
public class DBManager {
	private static DBManager instance;
	private Connection connection;
	
	private DBManager() {

	}
	
	/**
	 * Metodo che ritorna un'istanza del DBManger assicurando che sia l'unica
	 * @return instance istanza del DBManager
	 */
	public static DBManager getInstance() {
		if (instance ==null)
			instance = new DBManager();
		return instance;
	}

	/**
	 * Metodo che avvia la connessione con il DB
	 * @return connection 	oggetto dell'Interfaccia Connection
	 * @throws SQLException     L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	/**
	 * Metodo che restituisce la lista dei progetti presenti nel DB
	 * @return list 	lista di progetto
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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

	/**
	 * Metodo che ritorna le valutazioni associate ad un progetto p
	 * @param p progetto
	 * @return valutazioni 	set delle valutazioni associate ad un progetto
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	private Collection<? extends Valutazione> getValutazioni(Progetto p) throws SQLException {
		String sql = "SELECT * FROM Valutazioni_Progetti WHERE Progetto ="+ p.getId();
		 PreparedStatement ps = getConnection().prepareStatement(sql);
			
		 ResultSet rs = ps.executeQuery();
         Set<Valutazione> s = new HashSet<Valutazione>(); 
		 while(rs.next()) { 
			 Valutazione v = new Valutazione(getUtente(rs.getString(2)), rs.getInt(4), rs.getString(4));
             s.add(v);

		 }
		 return s;
	}

	/**
	 * Metodo che ritorna un utente 
	 * @param username username dell'utente
	 * @return utente istanza dell'utente con l'username passato
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	private Utente getUtente(String username) throws SQLException {
		if (SystemUtilities.getInstance().getUtenti().get(username)==null ){
		String sql = "SELECT Nome, Ruolo, email, Password FROM Utenti where Username = '"+ username+"'";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		Utente u = new Utente(username);
		while(rs.next()) { 
			
			switch(rs.getInt(2)) {
			case 0: u.setRuolo(new Progettista(u));break;
			case 1: u.setRuolo(new Esperto(u)); break;
			case 2: u.setRuolo(getEnte(u));break;
			}
			
			u.insertName(rs.getString(1));
			u.setEmail(rs.getString(3));
			u.getCompetenze().addAll(getCompetenze(u));
			u.getNotifiche().addAll(getNotifiche(u));
			SystemUtilities.getInstance().getPassword().put(u.getUsername(), rs.getInt(4));
			SystemUtilities.getInstance().getUtenti().put(u.getUsername(), u);
		}

		return u;}
		else
			return SystemUtilities.getInstance().getUtente(username);
	}
	
	/**
	 * Metodo privato che restituisce le competenze dell'utente u passato
	 * @param u		utente 
	 * @return		competenze dell'utente
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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

	/**
	 * Metodo per inserire un utente con la password associata nel DB
	 * @param u		utente
	 * @param password		password dell'utente
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per ottenere l'Ente dato un Utente. Il metodo può essere chiamato 
	 * solo se l'utente ha il ruolo di Ente
	 * @param u utente
	 * @return	ente associato all'utente
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	private Ente getEnte(Utente u) throws SQLException {
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
	/** 
	 * Metodo per aggiungere i collaboratori ad un ente
	 * le collaborazioni con stato 1 sono quelle accettate
	 * @param e		ente a cui aggiungere le collaborazioni
	 * @param id	id dell'ente 
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	
	private void addCollaboratori(Ente e, int id) throws SQLException {
		String sql = "SELECT Utente FROM Collaborazione WHERE Ente = '" + id+"' and Stato = 1" ;
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) { 
			e.addCollaboratore(getUtente(rs.getString(1)));
			
		}
		
		
	}
	/**
	 * Metodo che ritorna il set delle competenze necessarie associate ad
	 * un progetto passato p
	 * @param p		progetto
	 * @return		set di competenze
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo che ritorna la lista degli utenti presente nel DB
	 * @return		lista degli utenti
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	public List<Utente> getListaUtenti() throws SQLException{
		String sql = "SELECT Username, Nome, Ruolo, email, password FROM Utenti";
        PreparedStatement ps = getConnection().prepareStatement(sql);
		
		ResultSet rs = ps.executeQuery();
		List<Utente> utenti = new ArrayList<Utente>();
		while(rs.next()) { 
			if (SystemUtilities.getInstance().getUtente(rs.getString(1))!=null) {
				Utente u = SystemUtilities.getInstance().getUtente(rs.getString(1));
				utenti.add(u);
				}
			else {
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
			utenti.add(u);}
		}
		
		return utenti;
	}
	
	/**
	 * Metodo per inserire un progetto p nel DB
	 * @param p		progetto
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per ottenere la mappa delle competenze con l'id associato
	 * @return	mappa delle competenze
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo che inserisce le competenze richieste
	 * @param p		progetto
	 * @param competenze	set delle competenze necessarie
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per inserire le competenze nel Db all'utente u
	 * @param u		utente a cui associare le competenze
	 * @param competenze		compenteze da associare all'utente
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per inserire una partecipazione nel DB
	 * @param p		partecipazione
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per modificare il progetto p
	 * @param p		progetto
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	public void modificaProgetto(Progetto p) throws SQLException {
		String sql = "DELETE FROM Progetti where ID ="+ p.getId();
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.execute(sql);
		insertProgetto(p);
	}
	
	/**
	 * Metodo per aggiungere le partecipazioni al progetto p
	 * @param p		progetto
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per aggiornare una partecipazione nel DB con il nuovo statto s
	 * @param p		progetto
	 * @param u		utente
	 * @param s		nuovo stato
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	public void updatePartecipazione(Progetto p, Utente u, StatiRichieste s) throws SQLException {
		String sql = "UPDATE Partecipazioni SET Stato =" + s.ordinal()+ 
				"WHERE Progetto ="+ p.getId()+" and Utente ='"+ u.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		ps.executeUpdate();
	}
	
	/**
	 * Metodo per inserire un invito i nel DB
	 * @param i		invito
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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
	
	/**
	 * Metodo per ottenere l'id dell'Utente con il Ruolo di Ente
	 * @param ente utente con il ruolo di ente di cui si vuole conoscere l'id
	 * @return id
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	private int getEnteId(Utente ente) throws SQLException {
		String sql = "SELECT Id FROM Enti WHERE CapoGruppo= '"+ ente.getUsername()+"'";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		int id = -1;
		while(rs.next()) {
			id = rs.getInt(1);
		}
		return id;
	}
	
	/**
	 * Metodo che ritorna il set di notifiche associate all'utente nel DB
	 * @param u		utente
	 * @return set delle notifiche dell'utente
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
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

	/**
	 * Metodo che restituisce il progetto con l'id passato
	 * @param id		id del progetto
	 * @return	progetto
	 */
   private Progetto getProgetto(int id) {
	   List<Progetto> progetto = Bacheca.getInstance().getCatalogoProgetti().search(p->p.getId()==id);
	   return progetto.get(0);
   }
	

   /**
    * Metodo per ottenere il set degli inviti associate all'utente u
    * @param u 	utente
    * @return set degli inviti associati all'utente
    * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
    */
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
    
	/**
	 * Metodo per aggiornare l'invito i 
	 * @param i		invito
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
	public void updateInvito(Invito i) throws SQLException {
    	String sql = "update Collaborazione set stato =" +i.getStato().ordinal()+" where Utente ='"+ i.getProgettista()+"' and Ente = "+ getEnteId(i.getEnte());	
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    }
    
	/**
	 * Metodo per aggiornare le partecipazione p nel DB
	 * @param p		partecipazione
	 * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
	 */
    public void updatePartecipazione(Partecipazione p) throws SQLException {
    	String sql = "update Partecipazioni set stato =" +p.getStato().ordinal()+" "
    			+ "where Utente ='"+ p.getProgettista()+"' and Progetto = "+ p.getProgetto().getId();	
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    	
    }
    
    /**
     * Metodo per aggiornare la Richiesta Valutazione r nel DB
     * @param r		RichiestaValutazione che deve essere aggiornata 
     * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
     */
    public void updaterichiestaValutazione(RichiestaValutazione r) throws SQLException {
    	String sql = "update Notifiche set stato =" +r.getStato()+ " where Utente= '" +r.getEsperto()+ "'and IDtab = "+r.getProgetto();
    	PreparedStatement ps = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
    	ps.executeUpdate();

    	ResultSet rs = ps.getGeneratedKeys();
    	rs.next();
    }
    
    /**
     * Metodo per inserire una Richiesta di Valutazione r di un Progetto
     * @param r		richiesta valutazione
     * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
     */
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

    /**
     * Metodo per inserire un Ente nel DB
     * @param ente da inserire nel DB
     * @throws SQLException		L'eccezione viene lanciata in caso di problemi alla 
	 * 							connessione al DB
     */
	public void insertEnte(Ente ente ) throws SQLException {
		String sql = "INSERT INTO Enti(ID, CapoGruppo, Nome, Descrizione) VALUES(?,?,?,?)";
		PreparedStatement ps = getConnection().prepareStatement(sql);
		ps.setInt(1, ente.getUtente().getUsername().hashCode());
		ps.setString(2, ente.getUsername());
		ps.setString(3, ente.getName());
		ps.setString(4, ente.getDescrizione());
		ps.executeUpdate();
		
	} 
}
