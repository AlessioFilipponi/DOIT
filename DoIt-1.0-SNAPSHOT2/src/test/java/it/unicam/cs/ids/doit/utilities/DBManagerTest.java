package it.unicam.cs.ids.doit.utilities;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;
import it.unicam.cs.ids.doit.view.IGuest;

class DBManagerTest {

	private Progetto p1;
	private Utente u1;
	@Test
	void testGetInstance() {
		fail("Not yet implemented");
	}

	@Test
	void testGetConnection() {
		fail("Not yet implemented");
	}

	@Test
	void testListaProgetti() {
		fail("Not yet implemented");
	}

	@Test
	void testInsertUtente() {
		fail("Not yet implemented");
	}

	@Test
	void testGetEnte() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCompetenzeProgetto() {
		fail("Not yet implemented");
	}

	@Test
	void testGetListaUtenti() {
		fail("Not yet implemented");
	}

	
	@Test
	void testInsertProgetto() {
//		u1 = new Utente("luigi");
//		SystemUtilities.getInstance().getPassword().put("luigi", "pass".hashCode());
//		SystemUtilities.getInstance().getUtenti().put("luigi", u1);
//	
//		u1.setRuolo(new Progettista(u1));
//		u1.insertName("Luigi DiMaio");
//		try {
//			DBManager.getInstance().insertUtente(u1, "pass");
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			System.out.println("Username ESISTENTE");
//		}
//		p1 = new Progetto(u1);
//		p1.setTitolo("Progetto Governo 5 Stelle");
//		p1.setSpecifiche("Ricerco ministri per un nuovo Governo del Movimento 5 Stelle");
//		Set<String> competenze = new HashSet<String>();
//		try {
//			competenze.add(SystemUtilities.getInstance().getCompetenze(2));
//			competenze.add(SystemUtilities.getInstance().getCompetenze(3));
//		} catch (Exception e1) {
//			e1.getMessage();
//		}
//		p1.setCompetenzeProgettisti(competenze);
//		p1.setStato(StatiProgetto.PENDING);
//		
//		try {
//			DBManager.getInstance().insertProgetto(p1);
//		} catch (SQLException e) {
//			e.getMessage();
//		}
//		try {
//			List<Progetto> l = DBManager.getInstance().listaProgetti();
//			for (Progetto progetto : l) {
//				System.out.println(progetto.toString());
//			}
//			assertTrue(l.contains(p1));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	@Test
	void testGetCompetenze() {
		fail("Not yet implemented");
	}
	
	@Test
	void insertCompetenze() {
//		Set<String> competenze = new HashSet<String>();
//		try {
//			competenze.add(SystemUtilities.getInstance().getCompetenze(2));
//			competenze.add(SystemUtilities.getInstance().getCompetenze(3));
//		} catch (Exception e1) {
//			e1.getMessage();
//		}
//		try {
//			DBManager.getInstance().insertCompetenzeRichieste(p1, competenze );
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	@Test
	void partecipazioni() {
//		try {
//			Bacheca.getInstance().getCatalogoUtenti().addAll(DBManager.getInstance().getListaUtenti());
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		Utente u = IGuest.logIn();
//		try {
//			Progetto p = DBManager.getInstance().listaProgetti().get(0);
//			Partecipazione par = new Partecipazione(u, p);
//			DBManager.getInstance().insertPartecipazione(par);
//			Collection<Partecipazione> s = p.getPartecipazioni();
//			assertFalse(s.isEmpty());
//		} catch (SQLException e) {
//			e.getMessage();
//		}
		
		
	}

}
