package it.unicam.cs.ids.doit.view;

import java.sql.SQLException;

import it.unicam.cs.ids.doit.notifiche.RichiestaValutazione;
import it.unicam.cs.ids.doit.progetto.FacadeProgetto;
import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;

public class IEsperto implements UserInterface{
    private Utente utente;
    @Override
    public Utente getUtente() {
        return null;
    }

    public IEsperto(Utente utente) {
        this.utente = utente;
    }

    public void valutaProgetto(Progetto p)
    {
        new FacadeProgetto(p).valutaProgetto(getUtente());
    }

    public void valutaPropostaProgetto(RichiestaValutazione richiestaValutazione){
        UserCommunicator.print(richiestaValutazione.getProgetto().toString());
        if(UserCommunicator.select("Questo progetto secondo te è realizzabile?"))
            richiestaValutazione.conferma();
        else
            richiestaValutazione.rifiuta(UserCommunicator.insertString("Per quale motivo?"));
        try {
			DBManager.getInstance().updaterichiestaValutazione(richiestaValutazione);
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
    }
}
