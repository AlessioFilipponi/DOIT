package it.unicam.cs.ids.doit.user;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Observer;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.Subject;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.Valutazione;
import it.unicam.cs.ids.doit.ui.Named;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
public class Utente implements Named, Observer {

	private String username;
	private String email;
	private String ID;
    private boolean stato;
	private Set<Subject> notifiche; //deve essere un set
	private Ruolo ruolo;
	/**
	 * Crea un utente
	 * @param username nome
	 * @param cognome cognome
	 */
	public Utente(String username) {
		/*
		Imposta l'username
		 */
		this.username = username;
		
		/*
		Inizializza automaticamente tutti i campi
		 */
		ID=(int)(Math.random()*10000+Math.random()*1000+Math.random()*100+Math.random()*10)+"";

		this.notifiche = new HashSet<Subject>();
//		Bacheca.getInstance().getCatalogoUtenti().add(this);
		//aggiunge il ruolo di default: Progettista 
		this.ruolo = new Progettista(this);

	}

	public void insertName(String nome) {
		ruolo.setName(nome);
	}
	/**
	 * Restituisce l'username dell'utente
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Restituisce l'ID
	 * @return ID
	 */
	public String getID() {
		return ID;
	}


	/**
	 * Restituisce la lista delle partecipazioni di questo utente ad altri progetti
	 * @return partecipazioni
	 */
	public Set<Partecipazione> getPartecipazioni() {
		return getRole().getListaPartecipazioni();
	}

	/**
	 * Restituisce le competenze possedute da questo utente
	 * @return competenze
	 */
	public Set<String> getCompetenze() {
		return getRole().getcompetenze();
	}
 
    /**
     * Imposta il ruolo dell'utente
     * @param il nuovo ruolo
     * 
     */
	public void setRuolo(Ruolo r) {
		this.ruolo = r;
	}
	/**
	 * Restituisce il ruolo dell'utente
	 * @return ruolo
	 */
	public Ruolo getRole() {
		return ruolo;
	}

    /**
     * @return il nome dell'utente
     * 
     */
	@Override
	public String getName() {
		return ruolo.getNome();
	}


	/**
	 * @return il set di notifiche
	 *
	 */

	public Set<Subject> getNotifiche(){
		return this.notifiche;
	}

	/**
	 * metodo che viene chiamato dalla notify delle varie subject
	 */
	@Override
	public void update() {
		stato = true;

	}

//	public Set<Progetto> getCurriculum(){
//		Set<Progetto>myProjects=new HashSet<>();
//		for(Progetto p: Bacheca.getInstance().getCatalogoProgetti())
//			if(p.getPartecipanti().contains(this))
//				myProjects.add(p);
//		return myProjects;
//	}

	@Override
	public void addNotifica(Subject s) {
		this.notifiche.add(s);
	}
	
	public String toString() {
		return getName();
	}
	
	public boolean insertEmail(String email) {
		return true;
	} 
	
	public Curriculum getCurriculum() {
		return ruolo.getCurriculum();
	}

	public boolean getStato() {
		return stato;
	}
}