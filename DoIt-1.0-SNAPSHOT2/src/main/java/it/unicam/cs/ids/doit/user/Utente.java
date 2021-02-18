package it.unicam.cs.ids.doit.user;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Observer;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.Subject;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.Valutazione;
import it.unicam.cs.ids.doit.ui.Named;
public class Utente implements Named, Observer {

	private String username;

	private String ID;

	private Set<Subject> notifiche; //deve essere un set
	private Ruolo ruolo;
	private String email;
	private boolean newMessage;
	/**
	 * Crea un utente
	 * @param nome nome
	 * @param cognome cognome
	 */
	public Utente(String nome) {
		/*
		Imposta nome e cognome
		 */
		this.username = nome;
		
		/*
		Inizializza automaticamente tutti i campi
		 */
		ID=(int)(Math.random()*10000+Math.random()*1000+Math.random()*100+Math.random()*10)+"";

		this.notifiche = new HashSet<Subject>();
//		Bacheca.getInstance().getCatalogoUtenti().add(this);
//		this.ruolo = new Progettista(this);

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
	 * Restituisce la lista delle partecipazioni di questo utente ad altri progetti
	 * @return partecipazioni
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

	public Set<Subject> getNotifiche(){
		return this.notifiche;
	}

	/*
	 * metodo che viene chiamato dalla notify delle varie subject
	 * */
	@Override
	public boolean update() {
		newMessage = true;
		return newMessage;
		//qui il metodo dovrebbe chiamare un sistem.out ma della classe che interagisce con l'utente

	}
	public boolean getMessage() {
		return newMessage;
	}
	
	public void setMessage(boolean m) {
		this.newMessage = m;
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
		setEmail(email);
		return true;
	} 
	
	public Curriculum getCurriculum() {
		return ruolo.getCurriculum();
	}
	
	public int getRuolo() {
		return ruolo.getRuolo();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public Object getObserver() {
		return this;
	}
}