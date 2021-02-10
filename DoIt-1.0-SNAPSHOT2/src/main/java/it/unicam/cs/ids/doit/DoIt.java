package it.unicam.cs.ids.doit;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;

import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;
import it.unicam.cs.ids.doit.view.IProponenteProgetto;
import it.unicam.cs.ids.doit.view.IUtente;
import it.unicam.cs.ids.doit.view.InteractionManager;

public class DoIt {

	public static void main(String [] args) {
//		try {
//			System.out.println(new ConnessioneMysql().queryDB("SELECT nome FROM doit.Utenti WHERE ID = '1';").getString(0));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		//popolo il sistema
//		Utente u1 = new Utente("pippo");
//		SystemUtilities.getInstance().getPassword().put("pippo", "pass".hashCode());
//		SystemUtilities.getInstance().getUtenti().put("pippo", u1);
//		u1.setRuolo(new Progettista(u1));
//		u1.insertName("Pippo Pippo");
//		Utente u2 = new Utente("pluto");
//		SystemUtilities.getInstance().getPassword().put("pluto", "pass".hashCode());
//		SystemUtilities.getInstance().getUtenti().put("pluto", u2);
//		u2.setRuolo(new Ente(u2));
//		u2.insertName("Università di Pluto");
//		u1.setRuolo(new Progettista(u1));
//		Utente u2 = new Utente("Tiziano","Rossi");
//		u2.setRuolo(new Progettista(u2));
//		Utente u3 = new Utente("Francesco","Mari");
//		u3.setRuolo(new Progettista(u3));
//		Utente u4 = new Utente("Ottavio","Bianchi");
//		u4.setRuolo(new Progettista(u4));
//		Utente u5 = new Utente("Sara","Polli");
//		u5.setRuolo(new Progettista(u5));
//		Utente u6 = new Utente("Daniele","Assi");
//		u6.setRuolo(new Progettista(u6));
//		Utente u8 = new Utente("Università","di Camerino");
//		u8.setRuolo(new Ente(u8));
//		Progetto p = new Progetto(u1);
//		p.setTitolo("Progetto IDS");
//		p.setSpecifiche("Progetto corso di Ingegneria del Software");
//		Bacheca.getInstance().getCatalogoProgetti().add(p);
//		Progetto p1 = new Progetto(u1);
//		p1.setTitolo("Progetto ASD");
//		p1.setSpecifiche("Progetto corso di Algoritmi e Strutture Dati");
//		Bacheca.getInstance().getCatalogoProgetti().add(p1);
//		Progetto p2 = new Progetto(u1);
//		p2.setTitolo("Progetto IRS");
//		p2.setSpecifiche("Progetto corso di Internet Reti e Sicurezza");
//		Bacheca.getInstance().getCatalogoProgetti().add(p2);
//		Progetto p3 = new Progetto(u1);
//		p3.setTitolo("Progetto FCC");
//		p3.setSpecifiche("Progetto corso di Fondamenti di Cloud Computing");
//		Bacheca.getInstance().getCatalogoProgetti().add(p3);
	
//		u7.setRuolo(new Esperto(u7));
//		Utente u9 = new Utente("Olga", "Bernini");
//		u9.setRuolo(new Esperto(u9));
//		Partecipazione par = new Partecipazione(u2, p);
//		Partecipazione par1 = new Partecipazione(u3, p);
//		Partecipazione par2 = new Partecipazione(u4, p);
//       try {
//		DBManager.getInstance().getConnection();
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();}
//	}
//       try {
//		List<Progetto> l = DBManager.getInstance().listaProgetti();
//		for (Progetto progetto : l) {
//			System.out.println(progetto.toString());
//		}
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
		Set<String> c =SystemUtilities.getInstance().getCompetenze();
		for (String string : c) {
			System.out.println(string.toString() + " " +SystemUtilities.getInstance().getMapCompetenze().get(string));
//		}
//       try {
//		Bacheca.getInstance().getCatalogoProgetti().addAll(DBManager.getInstance().listaProgetti());
//		Bacheca.getInstance().getCatalogoUtenti().addAll(DBManager.getInstance().getListaUtenti());
//		
//	} catch (SQLException e) {
//		e.getMessage();
	}
		InteractionManager.start();
	}
//Prova commit Linux
}