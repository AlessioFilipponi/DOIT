package it.unicam.cs.DOIT.view;

import it.unicam.cs.DOIT.business.*;
import it.unicam.cs.DOIT.UI.UserCommunicator;

import java.util.*;

public class IProponenteProgetto  implements UserInterface{

	public IProponenteProgetto(Utente utente) {
		this.utente = utente;
	}
Utente utente;
	public void createProject() {
		Progetto p = new Progetto(getUtente().getID());//Crea un progetto associato all'utente che ha chiamato questo caso d'uso
		/*
		Chiede all'utente di inserire tutte le specifiche
		 */
		p.setTitolo(UserCommunicator.insertString("Inserire Titolo"));
		p.setSpecifiche(UserCommunicator.insertString("Inserire Specifiche"));
		p.setNumPartecipanti(UserCommunicator.insertInteger("Inserire Numero Partecipanti"));
		String competenze = UserCommunicator.insertString("Inserire le competenze necessarie (separate da una virgola (\",\")");
		if(!competenze.equals(""))
			p.setCompetenzeProgettisti(competenze);
		if(!UserCommunicator.select("Vuoi pubblicare il progetto?"))//Do la possibilità all'utente di pubblicare il progetto
			return;//Se non lo vuole pubblicare do' tutto quello che è stato appena fatto in pasto al garbage collector
		Bacheca.getCatalogoProgetti().add(p); //Se invece lo vuole pubblicare aggiungo il progetto al catalogo
		if(UserCommunicator.select("Vuoi invitare progettisti?")) //Dopo la pubblicazione do all'utente la possibilità di invitare progettisti
			invitaProgettista(p);
	}


	/**
	 * 
	 * @param progetto
	 */
	public void invitaProgettista(Progetto progetto) {
		if(progetto.getStato()!= StatiProgetto.PENDING)
			return; //Se il progetto non è in stato di Pending annullo l'operazione
		if(Bacheca.getCatalogoUtenti().isEmpty()) //Controllo che ci siano utenti nel sistema
		{
			UserCommunicator.print("Non esistono ancora progettisti in DOIT!"); //informo l'utente nel caso essi non esistano
			return;
		}
		Collection<Utente> progettisti= new HashSet<>(); //Lista dei progettisti competenti
		for(Utente u:Bacheca.getCatalogoUtenti()) //Per ogni progettista che ha tra le competenze tutte quelle richieste nel progetto
			if(u.getCompetenze().containsAll(progetto.getCompetenzeNecessarie()))
				progettisti.add(u); //Aggiungo il progettista alla lista dei competenti
		int progeressivo=1;
		if(progettisti.isEmpty())
			UserCommunicator.print("Non ci sono progettisti competenti!");
		Collection<Utente> progs= UserCommunicator.selectMultipleElements(progettisti,"Aggiungi progettista n°");
		for(Utente p:progs)//Per ognuno dei selezionati
		{
			Partecipazione part= new Partecipazione(p,progetto); //Creo una partecipazione tra il progetto e il progettista
			progetto.getPartecipazioni().add(part);//Aggiungo la partecipazione al progetto
			p.getPartecipazioni().add(part); //Aggiungo la partecipazione al progettista
		}
		UserCommunicator.print("Partecipazioni inviate");
	}


	@Override
	public Utente getUtente() {
		return utente;
	}

	public void richiediValutazionePropostaProgetto(){
		Progetto progetto=new IUtente(utente).selezionaProgetto(Bacheca.getListaMieiProgetti(getUtente().getID()));
		Collection<Utente> esperti=new HashSet<>();
		for (Utente u:Bacheca.getCatalogoEsperti())
			if(u.getCompetenze().containsAll(progetto.getCompetenzeNecessarie()))
				esperti.add(u);
		if(esperti.isEmpty())
		{
			UserCommunicator.print("Nessun esperto disponibile!");
			return;
		}
		UserCommunicator.print("**LISTA ESPERTI**");
		Utente esperto =UserCommunicator.selectElement(esperti,"Seleziona un esperto");
		RichiestaValutazione rv=new RichiestaValutazione(esperto,progetto);
		//TODO Che ci devo fare con sta richiesta??
	}
}