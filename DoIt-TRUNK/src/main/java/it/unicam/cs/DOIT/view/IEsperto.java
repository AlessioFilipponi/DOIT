package it.unicam.cs.DOIT.view;

import it.unicam.cs.DOIT.UI.UserCommunicator;
import it.unicam.cs.DOIT.business.Progetto;
import it.unicam.cs.DOIT.business.RichiestaValutazione;
import it.unicam.cs.DOIT.business.Utente;
import it.unicam.cs.DOIT.business.Valutazione;

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
        p.getValutazioni().add(new Valutazione(utente, UserCommunicator.insertInteger("Inserire il voto"), UserCommunicator.insertString("Inserire un commento")));
    }

    public void valutaPropostaProgetto(RichiestaValutazione richiestaValutazione){
        UserCommunicator.print(richiestaValutazione.getProgetto().toString());
        if(UserCommunicator.select("Questo progetto secondo te è realizzabile?"))
            richiestaValutazione.conferma();
        else
            richiestaValutazione.rifiuta(UserCommunicator.insertString("Per quale motivo?"));

    }
}
