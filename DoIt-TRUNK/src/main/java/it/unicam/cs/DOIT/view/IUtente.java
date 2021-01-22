package it.unicam.cs.DOIT.view;

import it.unicam.cs.DOIT.business.*;
import it.unicam.cs.DOIT.UI.UserCommunicator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Questo è il core del sistema di interfacciamento tra l'utente generico e DOIT
 */
public class IUtente implements UserInterface {
Utente utente;

	public IUtente(Utente utente) {
		this.utente = utente;
	}


	public void visualizzaProgetti()
	{
		Progetto p = selezionaProgetto(Bacheca.getCatalogoProgetti());//Chiedo all'utente di selezionare un progetto tra tutti quelli presenti nel sistema
		if(p==null) //Se non ne ha selezionato nessuno o il progetto selezionato non esiste
			return; //Annullo la procedura
		visualizzaDettagliProgetto(p); //Altrimenti visualizzo i dettagli del progetto
	}

	private void valutaPartecipazioni(Progetto proj){
		if(proj==null)
			return;//Se lo ha selezionato
		if(proj.getPartecipazioni().isEmpty()) //Controllo che la sua lista di partecipazioni non sia vuota
		{
			UserCommunicator.print("Nessun candidato si è proposto per partecipare al progetto");
			return;
		}
		//Se non lo è
		for(Partecipazione p:proj.getPartecipazioni()) //Scorro le partecipazioni una per una
			if(p.getStato()== StatiPartecipazione.NON_CONFERMATO) //Se la partecipazione si trova nello stato "Non Confermato"
			{
				UserCommunicator.print(p.getProgettista().getNome()+" "+p.getProgettista().getCognome());//Stampo nome e cognome del progettista a cui è riferita
				if(proj.getStato()== StatiProgetto.PENDING)//Se il progetto è in stato di PENDING
				{
					if(UserCommunicator.select("Accettare questa richiesta di partecipazione?")) //Chiedo all'utente se vuole accettare o rifiutare la partecipazione
						p.accetta();
					else
						p.rifiuta();
				}
				else
				{
					UserCommunicator.print("Il progetto è già stato confermato!");//Altrimenti lo informo che il progetto è già confermato
					break;
				}
			}
	}
	public void valutaPartecipazioni()
	{
		Progetto proj= selezionaProgetto(Bacheca.getListaMieiProgetti(getUtente().getID())); //Chiedo all'utente di selezionare un progetto tra quelli creati da lui
		valutaPartecipazioni(proj);
	}


	/**
	 * 
	 * @param p
	 */
	public void visualizzaDettagliProgetto(Progetto p) {
		UserCommunicator.print(p.toString());//stampa i dettagli del progetto
		if(p.getIDSelezionatore().equals(getUtente().getID())) //Se l'utente che ha chiamato questo caso d'uso è il selezionatore del progetto
		{
			if(UserCommunicator.select("Valutare le partecipazioni per questo progetto?")) //gli do la possibilità di valutare le partecipazioni
				valutaPartecipazioni(p);
		}
		else //Altrimenti
		{
			if(UserCommunicator.select("Vuoi partecipare al progetto?")) //Se le ha gli do la possibilità di partecipare al progetto
			{//Nel caso in cui volesse partecipare
				Partecipazione part = new Partecipazione(getUtente(),p); //Creo una partecipazione tra l'utente e il progetto
				getUtente().getPartecipazioni().add(part); //la aggiungo all'utente
				p.getPartecipazioni().add(part); //e la aggiungo al progetto
			}
			if(utente.getRole()==Ruoli.ESPERTO)
				if(UserCommunicator.select("Vuoi valutare questo progetto?"))
					new IEsperto(utente).valutaProgetto(p);
		}
	}

	public Progetto selezionaProgetto(Collection<Progetto> projects){
		return UserCommunicator.selectElement(projects,"Seleziona un progetto");
	}

	public void visualizzaNotifiche(){
		if(getUtente().getNotifiche().isEmpty())
			return;
		UserCommunicator.print("***LE TUE NOTIFICHE***");
		Subject notifica =UserCommunicator.selectElement(getUtente().getNotifiche(),"Seleziona una notifica");
		if(notifica instanceof RichiestaValutazione)
			new IEsperto(getUtente()).valutaPropostaProgetto(((RichiestaValutazione)notifica));
	}
	@Override
	public Utente getUtente() {
		return utente;
	}
}