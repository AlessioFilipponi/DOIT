package it.unicam.cs.ids.doit.utilities;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.ids.doit.user.Utente;

public class SystemUtilities {
	private Map<String,Utente> utenti;
	private Map<String,Integer> password;
	private static SystemUtilities instance;
	private Map<String, Integer> competenze;
	
	public void insertUtente(String username, String u) {
		password.put(username, u.hashCode());
			try { 
				DBManager.getInstance().insertUtente(getUtente(username), u);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Utente getUtente(String username) {
		return utenti.get(username);
	}
	public boolean exist(String u) {
		return password.containsKey(u);
	}
	
	private SystemUtilities(){
		utenti = new HashMap<String, Utente>();
		password = new HashMap<String, Integer>();
		try {
			competenze = DBManager.getInstance().getCompetenze();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//				new HashSet<String>();
//		competenze.add("Ingegnere");
//		competenze.add("Informatico");
//		competenze.add("Architetto");
//		competenze.add("Sarta");
		
	}
	
	public static SystemUtilities getInstance(){
		if (instance==null)
			instance = new SystemUtilities();
		return instance;
	}

	public Map<String, Utente> getUtenti() {
		return utenti;
	}

	public Map<String, Integer> getPassword() {
		return password;
	}

	public Set<String> getCompetenze() {
		return competenze.keySet();
	}
	
	public Map<String, Integer> getMapCompetenze(){
		return competenze;
	}
	
	public String getCompetenze(int i) throws Exception {
		for(String c : getCompetenze()) {
			if (competenze.get(c)== i) return c;
		}
		throw new NullPointerException("Non esiste nessuna competenza all'indice" + i);
	}
	

}
