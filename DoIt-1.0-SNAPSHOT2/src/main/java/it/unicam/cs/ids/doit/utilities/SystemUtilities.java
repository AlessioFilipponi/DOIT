package it.unicam.cs.ids.doit.utilities;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Utente;

/**
 * Classe per le Utilità di Sistema
 * 
 *
 */
public class SystemUtilities {
	private Map<String,Utente> utenti;
	private Map<String,Integer> password;
	private static SystemUtilities instance;
	private Map<String, Integer> competenze;
	
	
	/**
	 * Costruttore privato per creare il SystemUtilities
	 */
	private SystemUtilities(){
		utenti = new HashMap<String, Utente>();
		password = new HashMap<String, Integer>();
		try {
			competenze = DBManager.getInstance().getCompetenze();
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}

		
	}
	
	/**
	 * Metodo per accedere ai metodi del SystemUtilities garantendo che esista una sola
	 * instanza
	 * @return instance		instanza della classe SystemUtilities
	 */
	public static SystemUtilities getInstance(){
		if (instance==null)
			instance = new SystemUtilities();
		return instance;
	}
	
	/**
	 * Metodo per inserire un Utente nel sistema
	 * @param username	username dell'utente da inserire
	 * @param u 	password
	 */
	public void insertUtente(String username, String u) {
	
		password.put(username, u.hashCode());
			try { 
				DBManager.getInstance().insertUtente(getUtente(username), u);
				if (getUtente(username).getRole().isEnte()) DBManager.getInstance().insertEnte((Ente)getUtente(username).getRole());
		} catch (SQLException e) {
			UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
	}
	
	/**
	 * Metodo per ottenere un oggetto Utente dall'username
	 * @param username 	username dell'utente da cercare
	 * @return Utente 	utente con associato l'username
	 */
	public Utente getUtente(String username) {
		return utenti.get(username);
	}
	
	/**
	 * Metodo per verificare l'esistenza di un utente con l'username u
	 * @param u		username 
	 * @return		true se l'uente con l'username u esiste, false altrimenti
	 */
	public boolean exist(String u) {
		return password.containsKey(u);
	}
	
	
	/**
	 * Metodo che restituisce la mappa contenente l'username e gli utenti associati
	 * @return	utenti mappa degli utenti
	 */
	public Map<String, Utente> getUtenti() {
		return utenti;
	}

	/**
	 * Metodo che restituisce la mappa contenente l'username e la password associata
	 * @return	password mappa degli utenti e password
	 */
	public Map<String, Integer> getPassword() {
		return password;
	}

	/**
	 * Metodo che ritorna il Set delle competenze presenti nel sistema
	 * @return competenze	l'insieme delle competenze disponibili nel sistema
	 */
	public Set<String> getCompetenze() {
		return competenze.keySet();
	}
	
	/**
	 * Metodo che ritorna le competenze con il rispettivo id associato
	 * @return competenze	
	 */
	public Map<String, Integer> getMapCompetenze(){
		return competenze;
	}
	
	/**
	 * Metodo che ritorna la competenza associata ad i
	 * @param i			indice della competenza cercata
	 * @return competenza	com
	 * @throws Exception 	NullPointerException se non esiste nessuna competenza 
	 * 						all'indice i
	 */
	public String getCompetenze(int i) throws Exception {
		for(String c : getCompetenze()) {
			if (competenze.get(c)== i) return c;
		}
		throw new NullPointerException("Non esiste nessuna competenza all'indice" + i);
	}
	

}
