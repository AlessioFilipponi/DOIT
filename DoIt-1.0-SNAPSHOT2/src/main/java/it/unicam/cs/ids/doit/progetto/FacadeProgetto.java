package it.unicam.cs.ids.doit.progetto;

import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;

/**
 * Classe di facciata per la gestione del progetto
 *
 */
public class FacadeProgetto {
    Progetto p;

    /**
     * 
     * @param p		progetto su cui creare la Facade
     */
    public FacadeProgetto(Progetto p) {
        this.p = p;
    }

    /**
     * Metodo per effetuare la valutazione di un progetto
     * @param esperto	esperto che effettua la valutazione
     */
    public void valutaProgetto(Utente esperto) {
        p.getValutazioni().add(new Valutazione(esperto, inserimentoVoto(), UserCommunicator.insertString("Inserire un commento")));
    }
    
    /**
     * Metodo per inserire il voto
     * @return	voto inserito 
     */
    private int inserimentoVoto(){
        int votoInserito=0;
        while(votoInserito<1||votoInserito>5)
            votoInserito=UserCommunicator.insertInteger("Inserire il voto compreso tra 1 e 5");
        return votoInserito;
    }
    
    /**
     * Metodo per richiedere la partecipazione ad un progetto
     * @param progettista	progettista che richiede la partecipazione
     * @return partecipazione
     */
    public Partecipazione richiediPartecipazione(Utente progettista) {
    	if(p.getStato()== StatiProgetto.PENDING)//Se il progetto è in stato PENDING
		{
			Partecipazione part = new Partecipazione(progettista,p); //Creo una nuova partecipazione correlata all'utente e al progetto
			p.getPartecipazioni().add(part); //La aggiungo al progetto
			
		    return part;
		}
    	return null;
    	
    }
}
