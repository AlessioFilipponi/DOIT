package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.notifiche.Observer;
import it.unicam.cs.ids.doit.notifiche.Subject;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.Named;
public class Utente implements Named, Observer<Utente> {

	private String username;
	private String ID;
	private Set<Subject<Utente>> notifiche; //deve essere un set
	private Ruolo ruolo;
	private String email;
	private boolean newMessage;
	/**
	 * Crea un utente
	 * @param username username
	 */
	public Utente(String username) {
		/* Imposta l'username */
		this.username = username;
		
		/*
		Inizializza automaticamente tutti i campi
		 */
		ID = username.hashCode() + "";
//		ID=(int)(Math.random()*10000+Math.random()*1000+Math.random()*100+Math.random()*10)+"";
		this.notifiche = new HashSet<Subject<Utente>>();


	}

	public void insertName(String nome) {
		ruolo.setName(nome);
	}
	/**
	 * Restituisce l'username dell'utente
	 * @return nome
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
	 * Restituisce la lista dei progetti a cui l'utente si è candidato
	 * @return List lista di progetti
	 */
	public List<Progetto> getPartecipazioni() {
		return getRole().getPartecipazioni();
	}

	/**
	 * Restituisce le competenze possedute da questo utente
	 * @return competenze
	 */
	public Set<String> getCompetenze() {
		return getRole().getCompetenze();
	}


  /**
   * Metodo per impostare il ruolo dell'Utente
   * @param r nuovo ruolo 
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
     * Metodo che restiuisce il nome dell'utente
     * @return nome dell'utente
     */

	@Override
	public String getName() {
		return ruolo.getName();
	}


	/**
	 * @return il set di notifiche
	 *
	 */

	public Set<Subject<Utente>> getNotifiche(){
		return this.notifiche;
	}

	/*
	 * metodo che viene chiamato dalla notify delle varie subject
	 * */
	@Override
	public boolean update() {
		newMessage = true;
		return newMessage;

	}
	/**
	 * Metodo che ritorna il messaggio delle notifiche
	 * @return newMessage messaggio di notifica 
	 */
	public boolean getMessage() {
		return newMessage;
	}
	/**
	 * Metodo per impostare il messaggio delle notifiche
	 * @param m nuovo messaggio
	 */
	public void setMessage(boolean m) {
		this.newMessage = m;
	}
	/**
	 * Metodo per aggiungere una notifica
	 * @param Subject s notifica osservata dall'Utente
	 */
	@Override
	public void addNotifica(Subject<Utente> s) {
		this.notifiche.add(s);
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * Ritorna il curriculum dell'Utente
	 * @return curriculum dell'Utente
	 */
	public Curriculum getCurriculum() {
		return ruolo.getCurriculum();
	}
	/**
	 * Metodo per ottenere l'intero associato al ruolo
	 * @return int associato al ruolo 
	 */
	public int getRuolo() {
		return ruolo.getRoleNumber();
	}
	/**
	 * Metodo che restituisce l'email dell'Utente
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * Metodo per settare l'email dell'utente
	 * @param email 	nuova email da inserire
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Utente getObserver() {
		return this;
	}


}