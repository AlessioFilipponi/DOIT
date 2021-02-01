package it.unicam.cs.ids.doit.view;

import java.util.Collection;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Invito;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;

public class IEnte implements UserInterface{
    private Utente utente;
    public IEnte(Utente user) {
		utente = user;
	}
	@Override
    public Utente getUtente() {
        return utente;
    }
    public void InvitaCollaboratore(){
        Collection<Utente> progettisti = UserCommunicator.selectMultipleElements(Bacheca.getInstance().getCatalogoUtenti(),"Seleziona gli utenti che vuoi aggiungere");
//        progettisti.remove(getUtente());
        for(Utente p:progettisti)
        {
            Invito i =new Invito(getUtente(),p);
        }


    }

}
