package it.unicam.cs.ids.doit;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.cataloghi.Catalogo;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.StatiRichieste;
import it.unicam.cs.ids.doit.notifiche.Subject;
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
	    //Carico il contenuto del db
		Runnable load = () ->{
			Bacheca.getInstance().getCatalogoUtenti();
			Bacheca.getInstance().getCatalogoProgetti();
		};
	    //faccio partire il sistema
		Runnable doit = ()->{
			InteractionManager.start();
		};
		
		ExecutorService executor = Executors.newCachedThreadPool(); 
		executor.execute(load);
		executor.execute(doit);
		executor.shutdown();
		
	}
	}

