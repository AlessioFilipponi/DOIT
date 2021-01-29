package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;

public class Ente extends Ruolo{
    private String descrizione;
    private List<Utente> collaboratori;
    private Set<Partecipazione> partecipazioni;
 
    public Ente(Utente u) {
    	super(u);
    	this.partecipazioni = new HashSet<Partecipazione>();
    }
    
    public Ruoli getRole() {
        return Ruoli.ENTE;
    }

    public Set<String> getcompetenze() {
        Set<String> competenze=new HashSet<>();
        for(Utente u:collaboratori)
            competenze.addAll(u.getCompetenze());
        return competenze;
    }

    @Override
    public Set<Partecipazione> getPartecipazioni() {
        return partecipazioni;
    }

    @Override
    public boolean isExpert() {
        return false;
    }

    public void addCollaboratore(Utente c){
        collaboratori.add(c);
    }

    public String getDescrizione() {
        return descrizione;
    }

	@Override
	public void addToCatalogo() {
		Bacheca.getCatalogoEnti().add(this);
		
	}

	@Override
	public Set<Partecipazione> getListaPartecipazioni() {
		return partecipazioni;
	}
}
