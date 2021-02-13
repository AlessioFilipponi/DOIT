package unicam.cs.ids.doit;
import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

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

	@Test
	void test() throws SQLException {
		fail("Not implemented yet");
		
	}
	
	/**
	 * CANCELLA TUTTO IL CONTENUTO DELLE TABELLE NEL DATABASE
	 * Rispettando l'ordine sequenziale per non causare conflitti
	 * con le chiavi esterne durante la rimozione.
	 * @throws SQLException 
	 */
	private static void dbclear() throws SQLException {

		DBManager.getInstance().getConnection().prepareStatement(""
				+ "DELETE FROM doit.Collaborazione;"
				+ "DELETE FROM doit.Competenze_RichiesteProgetto;"
				+ "DELETE FROM doit.Competenze_Utente;"
				+ "DELETE FROM doit.Competenze;"
				+ "DELETE FROM doit.CategorieCompetenze;"
				+ "DELETE FROM doit.Partecipazioni;"
				+ "DELETE FROM doit.Valutazioni_Progetti;"
				+ "DELETE FROM doit.Notifiche;"
				+ "DELETE FROM doit.Progetti;"
				+ "DELETE FROM doit.Enti;"
				+ "DELETE FROM doit.Utenti;"
				+ "").executeQuery();
	}
	
	/**
	 * Ripristina il database allo stato del dump
	 * @throws SQLException
	 */
	private static void dumpRecover() throws SQLException {
		while(Utenti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Utenti VALUES ('"+Utenti.getString(1)+"',	'"+Utenti.getString(2)+"',	'"+Utenti.getString(3)+"',	'"+Utenti.getString(4)+"',	'"+Utenti.getString(5)+"');").executeQuery();
		while(Enti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Enti VALUES ('"+Enti.getString(1)+"',	'"+Enti.getString(2)+"',	'"+Enti.getString(3)+"',	'"+Enti.getString(4)+"');").executeQuery();
		while(Progetti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Progetti VALUES ('"+Progetti.getString(1)+"',	'"+Progetti.getString(2)+"',	'"+Progetti.getString(3)+"',	'"+Progetti.getString(4)+"',	'"+Progetti.getString(5)+"',	'"+Progetti.getString(6)+"',	'"+Progetti.getString(7)+"',	'"+Progetti.getString(8)+"');").executeQuery();
		while(Notifiche.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Notifiche VALUES ('"+Notifiche.getString(1)+"',	'"+Notifiche.getString(2)+"',	'"+Notifiche.getString(3)+"',	'"+Notifiche.getString(4)+"',	'"+Notifiche.getString(5)+"');").executeQuery();
		while(Valutazioni_Progetti.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Valutazioni_Progetti VALUES ('"+Valutazioni_Progetti.getString(1)+"',	'"+Valutazioni_Progetti.getString(2)+"',	'"+Valutazioni_Progetti.getString(3)+"',	'"+Valutazioni_Progetti.getString(4)+"',	'"+Valutazioni_Progetti.getString(5)+"');").executeQuery();
		while(Partecipazioni.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Partecipazioni VALUES ('"+Partecipazioni.getString(1)+"',	'"+Partecipazioni.getString(2)+"',	'"+Partecipazioni.getString(3)+"',	'"+Partecipazioni.getString(4)+"',	'"+Partecipazioni.getString(5)+"');").executeQuery();
		while(CategorieCompetenze.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.CategorieCompetenze VALUES ('"+CategorieCompetenze.getString(1)+");").executeQuery();
		while(Competenze.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze VALUES ('"+Competenze.getString(1)+"',	'"+Competenze.getString(2)+"',	'"+Competenze.getString(3)+"',	'"+Competenze.getString(4)+"');").executeQuery();
		while(CompetenzeUtente.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze_Utente VALUES ('"+CompetenzeUtente.getString(1)+"',	'"+CompetenzeUtente.getString(2)+"');").executeQuery();
		while(CompetenzeRichiesteProgetto.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Competenze_RichiesteProgetto VALUES ('"+CompetenzeRichiesteProgetto.getString(1)+"',	'"+CompetenzeRichiesteProgetto.getString(2)+"');").executeQuery();
		while(Collaborazione.next())
			DBManager.getInstance().getConnection().prepareStatement
			("INSERT INTO doit.Collaborazione VALUES ('"+Collaborazione.getString(1)+"',	'"+Collaborazione.getString(2)+"',	'"+Collaborazione.getString(3)+"',	'"+Collaborazione.getString(4)+"');").executeQuery();
		
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

