package it.unicam.cs.ids.doit.user;

import java.util.HashSet;
import java.util.Set;

import it.unicam.cs.ids.doit.cataloghi.Bacheca;
import it.unicam.cs.ids.doit.notifiche.Partecipazione;
import it.unicam.cs.ids.doit.ui.Named;

public class Esperto extends Ruolo implements Named{
    private Set<String> competenze;
    private Utente utente;
	private String nome;
	private Curriculum curriculum;

    public Esperto(Utente u) {
        super(u);
        this.competenze = new HashSet<>();
        this.curriculum = new Curriculum(u);
    }



    @Override
    public Set<String> getCompetenze() {
        return competenze;
    }


    @Override
    public boolean isExpert() {
        return true;
    }

	@Override
	public String getName() {
		return nome;
	}
	
	public Utente getUtente() {
		return utente;
	}




	@Override
	public boolean isEnte() {
		return false;
	}

	@Override
	public void setName(String nome) {
		this.nome = nome;
		
	}



	@Override
	public Curriculum getCurriculum() {
		return curriculum;
	}



	@Override
	public int getRoleNumber() {
		return 1;
	}


}
