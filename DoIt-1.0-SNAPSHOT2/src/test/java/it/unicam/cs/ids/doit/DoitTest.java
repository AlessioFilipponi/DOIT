package it.unicam.cs.ids.doit;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.RichiestaValutazione;
import it.unicam.cs.ids.doit.notifiche.StatiRichieste;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.progetto.Valutazione;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import it.unicam.cs.ids.doit.utilities.DBManager;

class DoitTest {
	static ResultSet CategorieCompetenze;
	static 	ResultSet Collaborazione;
	static 	ResultSet CompetenzeRichiesteProgetto;
	static 	ResultSet CompetenzeUtente;
	static 	ResultSet Enti;
	static 	ResultSet Notifiche;
	static 	ResultSet Partecipazioni;
	static 	ResultSet Progetti;
	static 	ResultSet Utenti;
	static 	ResultSet Valutazioni_Progetti;
	static 	ResultSet Competenze;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		partialDump();
		dbclear();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		dbclear();
		dumpRecover();
		
	}

	/**
	 * Test del ciclo di vita di un progetto.
	 */
	@Test
	void SimulatedScenario() {
		Utente u;
		u=new Utente("Andrea Polini");
		Progetto p;
		p=new Progetto(u.getID());
		p.setId(123);
		assertThrows(IllegalArgumentException.class,()->p.setNumPartecipanti(-1));
		p.setNumPartecipanti(3);
		p.setTitolo("DOIT");
		p.setSpecifiche("Questo sistema deve funzionare bene e avere una bella interfaccia grafica");
		assertEquals("Questo sistema deve funzionare bene e avere una bella interfaccia grafica",p.getSpecifiche());
		p.setSpecifiche("Questo sistema deve funzionare bene");
		assertEquals("DOIT",p.getTitolo());
		assertEquals("Questo sistema deve funzionare bene",p.getSpecifiche());
		assertEquals(StatiProgetto.PENDING,p.getStato());
		assertEquals(u.getID(),p.getIDSelezionatore());
		assertEquals(u.getID(),p.getCreatorID());
		assertEquals(p.getCreatorID(),p.getIDSelezionatore());
		Utente p1,p2,p3,p4,p5,p6;
		p1 = new Utente("Progettista1");
		Progettista prog1=new Progettista(p1);
		p2=new Utente("Progettista2");
		Progettista prog2=new Progettista(p2);
		p3=new Utente("Progettista3");
		Progettista prog3=new Progettista(p3);
		p4=new Utente("Progettista4");
		Progettista prog4=new Progettista(p4);
		p5=new Utente("Progettista4");
		Progettista prog5=new Progettista(p5);
		p6=new Utente("Progettista4");
		Progettista prog6=new Progettista(p6);
		assertEquals(prog1,p1.getRole());
		assertEquals(prog2,p2.getRole());
		assertEquals(prog3,p3.getRole());
		assertEquals(prog4,p4.getRole());
		assertEquals(prog5,p5.getRole());
		assertEquals(prog6,p6.getRole());
		Utente e1,e2,e3;
		e1=new Utente("Esperto1");
		e2=new Utente("Esperto2");
		e3=new Utente("Esperto3");
		Esperto ex1,ex2,ex3;
		ex1=new Esperto(e1);
		ex2=new Esperto(e2);
		ex3=new Esperto(e3);
		Partecipazione part1=new Partecipazione(p1,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part1.getStato());
		Partecipazione part2=new Partecipazione(p2,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part2.getStato());
		Partecipazione part3=new Partecipazione(p3,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part3.getStato());
		Partecipazione part4=new Partecipazione(p4,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part4.getStato());
		Partecipazione part5=new Partecipazione(p5,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part5.getStato());
		Partecipazione part6=new Partecipazione(p6,p);
		assertEquals(StatiRichieste.IN_VALUTAZIONE,part6.getStato());
		assertEquals(StatiProgetto.PENDING,p.getStato());
		assertEquals(6,p.getPartecipazioni().size());
		assertEquals(0,p.getPartecipanti().size());
		part1.accetta();
		assertEquals(StatiProgetto.PENDING,p.getStato());
		assertEquals(6,p.getPartecipazioni().size());
		assertEquals(1,p.getPartecipanti().size());
		part2.accetta();
		assertEquals(StatiProgetto.PENDING,p.getStato());
		assertEquals(6,p.getPartecipazioni().size());
		assertEquals(2,p.getPartecipanti().size());
		Utente expert = new Utente("ESPERTO");
		new Esperto(expert);
		RichiestaValutazione rv=new RichiestaValutazione(expert,p);
		assertThrows(IllegalStateException.class,()->new RichiestaValutazione(expert,p));
		assertThrows(IllegalStateException.class,()->new RichiestaValutazione(e1,p));
		rv.conferma();
		assertEquals(1,expert.getNotifiche().size());
		p.setSelezionatore(expert);
		p.getSelezionatore().setRuolo(new Esperto(p.getSelezionatore()));
		part3.rifiuta();
		assertEquals(StatiProgetto.PENDING,p.getStato());
		assertEquals(6,p.getPartecipazioni().size());
		assertEquals(2,p.getPartecipanti().size());
		part4.accetta();
		assertEquals(6,p.getPartecipazioni().size());
		assertEquals(3,p.getPartecipanti().size());
		assertEquals(StatiProgetto.PUBBLICATO,p.getStato());
		assertThrows(IllegalArgumentException.class,()->new Valutazione(e1,6,"Commento"));
		assertThrows(IllegalArgumentException.class,()->new Valutazione(e1,0,"Commento"));
		assertThrows(IllegalArgumentException.class,()->new Valutazione(e1,-1,"Commento"));
		assertThrows(NullPointerException.class,()->new Valutazione(e1,5,null));
		p.getValutazioni().add(new Valutazione(e1,5,"Commento"));
		p.getValutazioni().add(new Valutazione(e2,1,"Commento"));
		p.getValutazioni().add(new Valutazione(e1,3,"Commento"));
		p.getValutazioni().add(new Valutazione(e2,4,"Commento"));
		assertEquals(3.25,p.mediaVoti());
		assertThrows(IllegalStateException.class,()->p.setTitolo("nuovo titolo"));
		assertThrows(IllegalStateException.class,()->p.setSpecifiche("nuove specs"));
		assertThrows(IllegalStateException.class,()->p.setNumPartecipanti(100));
		assertEquals(StatiRichieste.CONFERMATO,part1.getStato());
		assertEquals(StatiRichieste.CONFERMATO,part2.getStato());
		assertEquals(StatiRichieste.RIFIUTATO,part3.getStato());
		assertEquals(StatiRichieste.CONFERMATO,part4.getStato());
		assertEquals(StatiRichieste.RIFIUTATO,part5.getStato());
		assertEquals(StatiRichieste.RIFIUTATO,part6.getStato());
		assertEquals(3,p.getPartecipanti().size());
		rv=new RichiestaValutazione(expert,p);
		assertThrows(IllegalStateException.class,()->new RichiestaValutazione(expert,p));
		assertThrows(IllegalStateException.class,()->new RichiestaValutazione(e1,p));
		rv.rifiuta("Il progetto è troppo bello");
		assertEquals(StatiProgetto.ARCHIVIATO,p.getStato());
		Utente alessio,alice,andrea,david;
		alice=new Utente("Alice Girolamini");
		alessio=new Utente("Alessio Filipponi");
		andrea=new Utente("Andrea Polini");
		david=new Utente("David Castagnaro");
		Progettista ali = new Progettista(alice);
		Progettista ale= new Progettista(alessio);
		Ente dav = new Ente(david);
		Esperto and=new Esperto(andrea);
		ali.getCompetenze().add("Programmare");
		ale.getCompetenze().add("Progettare");
		assertEquals(0,dav.getCompetenze().size());
		Invito i1=new Invito(david,alice);
		Invito i2=new Invito(david,alessio);
		assertEquals(0,dav.getCompetenze().size());
		i1.accetta();
		i2.rifiuta();
		assertThrows(IllegalStateException.class,()->i1.accetta());
		assertThrows(IllegalStateException.class,()->i2.rifiuta());
		assertEquals(1,dav.getCompetenze().size());
		Invito i3=new Invito(david,alessio);
		i3.accetta();
		assertEquals(2,dav.getCompetenze().size());
		assertEquals(2,dav.getCompetenze().size());
		ale.getCompetenze().add("Inventare casi d'uso");
		assertEquals(3,dav.getCompetenze().size());
		ali.getCompetenze().add("Disegnare Sequence diagram");
		assertEquals(4,dav.getCompetenze().size());
		Progetto doit=new Progetto(andrea.getID());
		doit.setNumPartecipanti(1);
		new Partecipazione(david,doit).accetta();
		assertEquals(StatiProgetto.PUBBLICATO,doit.getStato());
		assertEquals(1,doit.getPartecipanti().size());
	}
	
	/**
	 * CANCELLA TUTTO IL CONTENUTO DELLE TABELLE NEL DATABASE
	 * Rispettando l'ordine sequenziale per non causare conflitti
	 * con le chiavi esterne durante la rimozione.
	 * @throws SQLException 
	 */
	private static void dbclear() throws SQLException {

		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Collaborazione;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Competenze_RichiesteProgetto;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Competenze_Utente;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement( "DELETE FROM doit.Competenze;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.CategorieCompetenze;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Partecipazioni;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Valutazioni_Progetti;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Notifiche;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Progetti;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Enti;").executeUpdate();
		DBManager.getInstance().getConnection().prepareStatement("DELETE FROM doit.Utenti;").executeUpdate();
	}
	
	/**
	 * Ripristina il database allo stato del dump
	 * @throws SQLException
	 */
	private static void dumpRecover() throws SQLException {
		while(Utenti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Utenti VALUES ('"+Utenti.getString(1)+"',	'"+Utenti.getString(2)+"',	'"+Utenti.getString(3)+"',	'"+Utenti.getString(4)+"',	'"+Utenti.getString(5)+"');").executeUpdate();
		while(Enti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Enti VALUES ('"+Enti.getString(1)+"',	'"+Enti.getString(2)+"',	'"+Enti.getString(3)+"',	'"+Enti.getString(4)+"');").executeUpdate();
		while(Progetti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Progetti VALUES ('"+Progetti.getString(1)+"',	'"+Progetti.getString(2)+"',	'"+Progetti.getString(3)+"',	'"+Progetti.getString(4)+"',	'"+Progetti.getString(5)+"',	'"+Progetti.getString(6)+"',	'"+Progetti.getString(7)+"',	'"+Progetti.getString(8)+"');").executeUpdate();
		while(Notifiche.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Notifiche VALUES ('"+Notifiche.getString(1)+"',	'"+Notifiche.getString(2)+"',	'"+Notifiche.getString(3)+"',	'"+Notifiche.getString(4)+"',	'"+Notifiche.getString(5)+"');").executeUpdate();
		while(Valutazioni_Progetti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Valutazioni_Progetti VALUES ('"+Valutazioni_Progetti.getString(1)+"',	'"+Valutazioni_Progetti.getString(2)+"',	'"+Valutazioni_Progetti.getString(3)+"',	'"+Valutazioni_Progetti.getString(4)+"',	'"+Valutazioni_Progetti.getString(5)+"');").executeUpdate();
		while(Partecipazioni.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Partecipazioni VALUES ('"+Partecipazioni.getString(1)+"',	'"+Partecipazioni.getString(2)+"',	'"+Partecipazioni.getString(3)+"',	'"+Partecipazioni.getString(4)+"',	'"+Partecipazioni.getString(5)+"');").executeUpdate();
		while(CategorieCompetenze.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.CategorieCompetenze VALUES ('"+CategorieCompetenze.getString(1)+");").executeUpdate();
		while(Competenze.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze VALUES ('"+Competenze.getString(1)+"',	'"+Competenze.getString(2)+"',	'"+Competenze.getString(3)+"',	'"+Competenze.getString(4)+"');").executeUpdate();
		while(CompetenzeUtente.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze_Utente VALUES ('"+CompetenzeUtente.getString(1)+"',	'"+CompetenzeUtente.getString(2)+"');").executeUpdate();
		while(CompetenzeRichiesteProgetto.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze_RichiesteProgetto VALUES ('"+CompetenzeRichiesteProgetto.getString(1)+"',	'"+CompetenzeRichiesteProgetto.getString(2)+"');").executeUpdate();
		while(Collaborazione.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Collaborazione VALUES ('"+Collaborazione.getString(1)+"',	'"+Collaborazione.getString(2)+"',	'"+Collaborazione.getString(3)+"',	'"+Collaborazione.getString(4)+"');").executeUpdate();
		
	}
	
	/**
	 * FACCIO UN BACKUP DEL CONTENUTO DI TUTTE LE TABELLE NEL DATABASE
	 * Questo è un dump delle righe salvato dentro a delle tabelle JDBC
	 * @throws SQLException 
	 */
	private static void partialDump() throws SQLException {
		CategorieCompetenze = DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM CategorieCompetenze").executeQuery();
		Collaborazione = DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Collaborazione").executeQuery();
		CompetenzeRichiesteProgetto= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Competenze_RichiesteProgetto").executeQuery();
		CompetenzeUtente= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Competenze_Utente").executeQuery();
		Enti = DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Enti").executeQuery();
		Notifiche= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Notifiche").executeQuery();
		Partecipazioni= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Partecipazioni").executeQuery();
		Progetti = DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Progetti").executeQuery();
		Utenti= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Utenti").executeQuery();
		Valutazioni_Progetti= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Valutazioni_Progetti").executeQuery();
		Competenze= DBManager.getInstance().getConnection().prepareStatement
				("SELECT *  FROM Competenze").executeQuery();
	}

}
