package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;
import java.util.*;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.notifiche.RichiestaValutazione;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.progetto.StatiProgetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
import it.unicam.cs.ids.doit.utilities.SystemUtilities;
/**
 * Classe per la gestione dell'interfaccia con l'utente che è un proponente progetto
 * ovvero che pubblica un progetto o ne ha pubblicati 
 *
 */
public class IProponenteProgetto  implements UserInterface{
	private Utente utente;
	
	/**
	 * 
	 * @param utente	utente che invoca l'interfaccia
	 */
	public IProponenteProgetto(Utente utente) {
		this.utente = utente;
	}
	
	
	/**
	 * Metodo per creare un Progetto
	 */
	public void createProject() {
		Progetto p = new Progetto(getUtente());//Crea un progetto associato all'utente che ha chiamato questo caso d'uso
		/*
		Chiede all'utente di inserire tutte le specifiche
		 */
		p.setTitolo(UserCommunicator.insertString("Inserire Titolo"));
		p.setSpecifiche(UserCommunicator.insertString("Inserire Specifiche"));
		do{try{p.setNumPartecipanti(UserCommunicator.insertInteger("Inserire Numero Partecipanti"));}
		catch (Exception e) {
			UserCommunicator.print(UserCommunicator.ERROR_INT_MESSAGE);
		}}while(p.getNumPartecipanti()<0);
		Set<String> competenze = UserCommunicator.selectMultipleElementsS(SystemUtilities.getInstance().getCompetenze(), "Seleziona le competenze");
		p.setCompetenzeProgettisti(competenze);
		if(!UserCommunicator.select("Vuoi pubblicare il progetto?")) {//Do la possibilità all'utente di pubblicare il progetto
			p = null;//annulla
			return;}//Se non lo vuole pubblicare do' tutto quello che è stato appena fatto in pasto al garbage collector
		Bacheca.getInstance().getCatalogoProgetti().add(p); //Se invece lo vuole pubblicare aggiungo il progetto al catalogo
		try {
			DBManager.getInstance().insertProgetto(p);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
		if(UserCommunicator.select("Vuoi invitare progettisti?")) //Dopo la pubblicazione do all'utente la possibilità di invitare progettisti
			invitaProgettista(p);
	}


	/**
	 * Metodo per invitare un progettista ad un Progetto
	 * @param progetto progetto selezionato
	 */
	public void invitaProgettista(Progetto progetto) {
		if(progetto.getStato()!= StatiProgetto.PENDING)
			return; //Se il progetto non è in stato di Pending annullo l'operazione
		Collection<Utente> c = Bacheca.getInstance().getCatalogoUtenti();
		c.remove(getUtente());
		if(c.isEmpty()) //Controllo che ci siano utenti nel sistema
		{
			UserCommunicator.print("Non esistono ancora progettisti in DOIT!"); //informo l'utente nel caso essi non esistano
			
		}
		else{Collection<Utente> progettisti= Bacheca.getInstance().getProgettistiCompetenti(progetto.getCompetenzeNecessarie()); //Lista dei progettisti competenti
		int progeressivo=1;
		progettisti.remove(getUtente());
		if(progettisti.isEmpty())
			UserCommunicator.print("Non ci sono progettisti competenti!");
		else {
		Collection<Utente> progs= UserCommunicator.selectMultipleElements(progettisti,"Aggiungi progettista n°");
		for(Utente p:progs)//Per ognuno dei selezionati
		{
			Partecipazione part= new Partecipazione(p,progetto); //Creo una partecipazione tra il progetto e il progettista
			progetto.getPartecipazioni().add(part);//Aggiungo la partecipazione al progetto
			p.getNotifiche().add(part);
			try {
				DBManager.getInstance().insertPartecipazione(part);
			} catch (SQLException e) {
				UserCommunicator.print(UserCommunicator.ERROR_INSERT);
			}
		}
		UserCommunicator.print("Partecipazioni inviate");}}
	}


	@Override
	public Utente getUtente() {
		return utente;
	}

	/**
	 * Metodo per richiedere la valutazione da parte di un Esperto ad un Progetto
	 * pubblicato dall'Utente
	 */
	public void richiediValutazionePropostaProgetto(){
		if(Bacheca.getInstance().getListaMieiProgetti(utente).isEmpty()) UserCommunicator.print("Non hai pubblicato progetti");
		else {Progetto progetto=new IUtente(utente).selezionaProgetto(Bacheca.getInstance().getListaMieiProgetti(getUtente().getID()));
		Collection<Utente> esperti= Bacheca.getInstance().getEspertiCompetenti(progetto.getCompetenzeNecessarie());
		if(esperti.isEmpty())
		{
			UserCommunicator.print("Nessun esperto disponibile!");
			return;
		}
		UserCommunicator.print("**LISTA ESPERTI**");
		Utente esperto =UserCommunicator.selectElement(esperti,"Seleziona un esperto");
		RichiestaValutazione rv=new RichiestaValutazione(esperto,progetto);
		try {
			DBManager.getInstance().insertRichiestaValutazioneProgetto(rv);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}}
	}

	/**
	 * Metodo per modificare un progetto pubblicato
	 * @param p progetto
	 */
	public void modificaProgetto(Progetto p){
		UserCommunicator.print("***Modifica Progetto***");
		int insert= -1;
		do {
			try {insert=UserCommunicator.insertInteger("1) Modifica Titolo\n2) Modifica Specifiche\n3) Modifica competenze\n0)Esci\n La tua scelta");
			} catch (Exception e) {
				
				UserCommunicator.print("Puoi solo inserire un numero da 0 a 3");
			}}while(insert<0 ||insert>3);
			switch(insert)
			{
				case 1:p.setTitolo(UserCommunicator.insertString("Inserire nuovo titolo"));break;
				case 2:p.setSpecifiche(UserCommunicator.insertString("Inserire nuove specifiche"));break;
				case 3:p.setCompetenzeProgettisti(UserCommunicator.selectMultipleElementsS(SystemUtilities.getInstance().getCompetenze(), "Scegli le competenze necessarie"));break;
				case 0:break;
				default:UserCommunicator.print("Scelta non valida!");
			}
		
		try {
			DBManager.getInstance().modificaProgetto(p);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
	}
}