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
	 * @param username nome
	 * @param cognome cognome
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
	 * @return List<Progetto>
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
	public boolean getMessage() {
		return newMessage;
	}
	
	public void setMessage(boolean m) {
		this.newMessage = m;
	}
	@Override
	public void addNotifica(Subject<Utente> s) {
		this.notifiche.add(s);
	}
	
	public String toString() {
		return getName();
	}
	
	
	public Curriculum getCurriculum() {
		return ruolo.getCurriculum();
	}
	
	public int getRuolo() {
		return ruolo.getRoleNumber();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Utente getObserver() {
		return this;
	}


}