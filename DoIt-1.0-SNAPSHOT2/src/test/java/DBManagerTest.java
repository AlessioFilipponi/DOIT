import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;

class DBManagerTest {

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
		Utente u1 = new Utente("pippo");
		SystemUtilities.getInstance().getPassword().put("pippo", "pass".hashCode());
		SystemUtilities.getInstance().getUtenti().put("pippo", u1);
	
		u1.setRuolo(new Progettista(u1));
		u1.insertName("Pippo Pippo");
		try {
			DBManager.getInstance().insertUtente(u1, "pass");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Progetto p1 = new Progetto(u1);
		p1.setTitolo("Progetto ASD");
		p1.setSpecifiche("Progetto corso di Algoritmi e Strutture Dati");
		
		try {
			DBManager.getInstance().insertProgetto(p1);
		} catch (SQLException e) {
			e.getMessage();
		}
		try {
			List<Progetto> l = DBManager.getInstance().listaProgetti();
			assertTrue(l.contains(p1));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Test
	void testGetCompetenze() {
		fail("Not yet implemented");
	}

}
