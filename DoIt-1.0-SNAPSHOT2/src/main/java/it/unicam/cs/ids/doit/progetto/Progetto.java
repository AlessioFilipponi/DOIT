package it.unicam.cs.ids.doit.progetto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.StatiRichieste;
import it.unicam.cs.ids.doit.ui.Named;
import it.unicam.cs.ids.doit.user.Utente;

public class Progetto implements Named {

	private String creatorID;
	private long ID;
	private StatiProgetto stato;
	private String specifiche;
	private String titolo;
	private int numPartecipanti;
	private Date data;
	private Collection<String> competenzeNecessarie;
	private String IDSelezionatore;
	private Set<Partecipazione> partecipazioni;
	private Collection<Valutazione> valutazioni;
	private Utente proponente;

	/**
	 * Crea un nuovo it.unicam.cs.DOIT.progetto.Progetto
	 * @param creatorID ID dell'utente che lo ha creato
	 */
	public Progetto(String creatorID) {
		/*
		Inizializza e variabili
		 */
		this.IDSelezionatore = creatorID;
		this.creatorID=creatorID;
		ID = (long)(Math.random()*1000000+Math.random()*100000+Math.random()*10000+(Math.random()*1000+Math.random()*100+Math.random()*10)); //genera un id Random
		stato=StatiProgetto.PENDING;//Il progetto viene messo in stato di pending
		numPartecipanti = -1;
		//Vengono inizializzate le liste
		partecipazioni=new HashSet<>();
		competenzeNecessarie=new HashSet<>();
		valutazioni=new HashSet<>();
		partecipazioni=new HashSet<>();
		this.data= new Date();
	}

	public Progetto(Utente proponente) {
		this(proponente.getID());
		this.proponente = proponente;
	}
	public Collection<Valutazione> getValutazioni() {
		return valutazioni;
	}

	/**
	 * Restituisce le specifiche del progetto
	 * @return
	 */
	public String getSpecifiche() {
		return specifiche;
	}



	/**
	 * Restituisce una stringa che descrive il progetto
	 * @return stringa
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();//Creo uno stringbuilder pper costruire la stringa del progetto
		String creatore = "N.D.";
		double valutazione = 0.0;
		StringBuilder competenze = new StringBuilder("Competenze Richieste: \n"); //Creo uno Stringbuilder per le competenze richieste
		if(stato==StatiProgetto.PENDING) //Se il progetto è in stato di pending (Altrimenti non stampo le competenze)
			for(String competenza:competenzeNecessarie)//Aggiungo tutte le competenze allo stringbuilder
				competenze.append("		"+competenza+", \n");
		else
			competenze=new StringBuilder("");
		if(!valutazioni.isEmpty())//Se ci sono delle valutazioni
			for(Valutazione v: valutazioni) //faccio la somma dei voti
				valutazione+=v.getVoto();
		valutazione=valutazione/valutazioni.size();//E lo divido per il totale, così quando andrò a inserire nella stringa mi viene la media
//		for(Utente u: Bacheca.getInstance().getCatalogoUtenti())//Cerco il nome del creatore nel catalogo utenti
//			if(u.getID().equals(creatorID))
//				creatore=u.getNome();
		creatore = proponente.getName();
		//Costruisco la stringa da ritornare mediante una serie di append allo stringbuilder
		return	s.append(titolo.toUpperCase()).append("\n\n")
				.append("Pubblicazione: " + DateFormat.getDateInstance().format(data)).append("\n")
				.append(specifiche).append("\n")
				.append("Partecipanti: ").append(getPartecipanti().size()).append("/").append(getNumPartecipanti()).append("\n")
				.append("Creato da: ").append(creatore).append("\n")
//				.append("Voto complessivo: ").append(valutazione).append("\n")
				.append("Voto: ").append(mediaVoti()).append("\n")
				.append("Stato: ").append(getStato().toString()).append("\n")
				.append(competenze).toString();//<------ Questo toString() completa la funzione trasformando in stringa lo StringBuilder fino ad ora generato
	}

	/**
	 * Restituisce le competenze necessarie
	 * @return competenze
	 */
	public Collection<String> getCompetenzeNecessarie() {
		return competenzeNecessarie;
	}

	/**
	 * Restituisce l'ID dell'utente che lo ha creato
	 * @return ID
	 */
	public String getCreatorID() {
		return creatorID;
	}

	/**
	 * Restituise il Titolo
	 * @return titolo
	 */
	public String getTitolo() {
		return titolo;
	}

	/**
	 * Imposta un Titolo
	 * @param titolo
	 */
	public void setTitolo(String titolo) {
		this.titolo = titolo.toUpperCase();
	}

	/**
	 * imposta le specifiche
	 * @param specifiche specifiche da impostare
	 */
	public void setSpecifiche(String specifiche) {
		this.specifiche = specifiche;
	}

	/**
	 * Imposta il numero di partecipanti ammessi
	 * @param numPartecipanti numero di partecipanti ammessi
	 */
	public void setNumPartecipanti(int numPartecipanti) {
		this.numPartecipanti = numPartecipanti;
	}

	/**
	 * Imposta le competenze necessarie per partecipare al progetto
	 * @param competenze competenze necessarie
	 */
	public void setCompetenzeProgettisti(String competenze) {
		this.competenzeNecessarie=new ArrayList<>(Arrays.asList(competenze.split(",")));
	}

	/**
	 * Imposta lo stato
	 * @param stato
	 */
	public void setStato(StatiProgetto stato) {
		this.stato = stato;
	}

	/**
	 * Restituisce lo stato
	 * @return
	 */
	public StatiProgetto getStato() {
		return this.stato;
	}

	/**
	 * Restituisce la lista delle partecipazioni correlate a questo progetto
	 * @return
	 */
	public Collection<Partecipazione> getPartecipazioni() {
		return partecipazioni;
	}

	/**
	 * Restituisce l'ID del selezionatore
	 * @return ID Selezionatore
	 */
	public String getIDSelezionatore() {
		return IDSelezionatore;
	}

	/**
	 * Restituisce la lista dei candidati per la partecipazione
	 * @return lista candidati
	 */
	public Collection getCandidati() {
		Collection<String> names=new HashSet<>();
		for (Partecipazione p : partecipazioni)
			if(p.getStato()== StatiRichieste.NON_CONFERMATO)
				names.add(p.getProgettista().getID()+"> "+p.getProgettista().getUsername() + " " +p.getProgettista().getName());
		return names;
	}

	/**
	 * Restituisce il numero di partecipanti ammessi
	 * @return numero partecipanti
	 */
	public int getNumPartecipanti() {
		return numPartecipanti;
	}

	/**
	 * Restituisce tutti i partecipanti
	 * @return partecipanti
	 */
	public Collection<Utente> getPartecipanti(){
		Collection<Utente> users=new HashSet<>();
		for (Partecipazione p : partecipazioni)
			if(p.getStato()== StatiRichieste.CONFERMATO)
				users.add(p.getProgettista());
		return users;
	}

	@Override
	public String getName() {
		String puntini ="...";
		if(getSpecifiche().length()<50)
			puntini="";
		String retn=getTitolo()+"\n"+
				"Partecipanti richiesti: "+getNumPartecipanti()+"\n"+
				getSpecifiche()+puntini;
		return getTitolo();
	}

	public Utente getProponente() {
		return proponente;
	}
	
	public double mediaVoti() {
		double v = 0;
		for (Valutazione val : valutazioni) {
			v += val.getVoto();
		}
		return v/valutazioni.size();
	}

	public void setCompetenzeProgettisti(Set<String> competenze) {
		this.competenzeNecessarie = competenze;
		
	}
}