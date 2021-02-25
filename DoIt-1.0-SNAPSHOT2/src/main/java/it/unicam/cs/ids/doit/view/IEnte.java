package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;
import java.util.Collection;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;

/**
 * Classe per la gestione dell'interfaccia utente, con un utente con il ruolo 
 * di Ente
 * 
 *
 */
public class IEnte implements UserInterface{
    private Utente utente;
    
    /**
     * Crea una nuova interfaccia ente
     * @param user		utente a cui associare l'interfaccia
     */
    public IEnte(Utente user) {
		utente = user;
	}
    
	@Override
    public Utente getUtente() {
        return utente;
    }
	
	/**
	 * Metodo per invitare un Collaboratore
	 */
    public void InvitaCollaboratore(){
    	Collection<Utente> progettisti = Bacheca.getInstance().getCatalogoUtenti();
    	progettisti.remove(getUtente());
        Collection<Utente> progettistiInvitati =UserCommunicator.selectMultipleElements(progettisti,"Seleziona gli utenti che vuoi aggiungere");
        
        for(Utente p:progettistiInvitati)
        {
            Invito i =new Invito(getUtente(),p);
            try {
				DBManager.getInstance().insertInvito(i);
			} catch (SQLException e) {
				UserCommunicator.print(UserCommunicator.ERROR_INSERT);
			}
        }


    }

}
