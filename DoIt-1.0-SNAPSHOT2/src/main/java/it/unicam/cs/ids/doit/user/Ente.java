package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.progetto.Progetto;

public class Ente extends Ruolo{
    private String descrizione;
    private List<Utente> collaboratori;
    private String name;
    private Curriculum curriculum;
 
    public Ente(Utente u) {
    	super(u);
    	this.curriculum = new Curriculum(u);
    
    }
   

    public Set<String> getcompetenze() {
        Set<String> competenze=new HashSet<>();
        for(Utente u:collaboratori)
            competenze.addAll(u.getCompetenze());
        return competenze;
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

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	@Override
	public void addToCatalogo() {
		Bacheca.getInstance().getCatalogoEnti().add(this);
		
	}



	@Override
	public boolean isEnte() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void setName(String nome) {
		this.name = nome;
		
	}

	@Override
	protected String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public Curriculum getCurriculum() {
		for (Utente utente : collaboratori) {
		     curriculum.getProgetti().addAll(utente.getCurriculum().getProgetti());
		}
		return curriculum;
	}
}
