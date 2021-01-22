package it.unicam.cs.DOIT.business;
import javax.naming.Name;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
public class Utente implements Ruolo, Named, Observer {

	Ruoli ruolo = Ruoli.PROGETTISTA;
	private String nome;
	private String cognome;
	private String ID;
	private Collection<String> competenze;
	private Collection<Partecipazione> partecipazioni;
	private Collection<Valutazione> valutazioniRilasciate; //non è dell'utente
	private Set<Subject> notifiche; //deve essere un set 
	
	

	/**
	 * Restituisce il nome
	 * @return nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Restituisce il cognome
	 * @return cognome
	 */
	public String getCognome() {
		return cognome;
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
	public Collection<Partecipazione> getPartecipazioni() {
		return partecipazioni;
	}

	/**
	 * Restituisce le competenze possedute da questo utente
	 * @return competenze
	 */
	public Collection<String> getCompetenze() {
		return competenze;
	}

	/**
	 * Crea un utente
	 * @param nome nome
	 * @param cognome cognome
	 */
	public Utente(String nome, String cognome) {
		/*
		Imposta nome e cognome
		 */
		this.nome = nome;
		this.cognome = cognome;
		/*
		Inizializza automaticamente tutti i campi
		 */
		ID=(int)(Math.random()*10000+Math.random()*1000+Math.random()*100+Math.random()*10)+"";
		competenze=new HashSet<>();
		partecipazioni=new HashSet<>();
		valutazioniRilasciate=new HashSet<>();
		this.notifiche = new HashSet<Subject>();
	}

	/**
	 * Restituisce il ruolo dell'utente
	 * @return ruolo
	 */
	@Override
	public Ruoli getRole() {
		return ruolo;
	}



	@Override
	public String getName() {
		return getNome() + " " + getCognome();
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
	public void update() {
		System.out.println("Nuova notifica");
		//qui il metodo dovrebbe chiamare un sistem.out ma della classe che interagisce con l'utente
		
	}

	@Override
	public void addNotifica(Subject s) {
		this.notifiche.add(s);
		//qui aggiunge una notifica al set
		
	}
}