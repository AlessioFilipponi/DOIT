package it.unicam.cs.ids.doit.cataloghi;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;

import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;


public class Bacheca {
	private static Catalogo<Utente> catalogoUtenti;
	private static Catalogo<Progetto> catalogoProgetti;
	private static Bacheca instance;

	
	private Bacheca() {
		getCatalogoProgetti();
		getCatalogoProgettisti();
	}
	
	public static Bacheca getInstance() {
		if(instance ==null)
			instance = new Bacheca();
		return instance;
	}

	/**
	 * Restituisce il catalogo dei progetti
	 * @return catalogo progetti
	*/
	
	public  Catalogo<Progetto> getCatalogoProgetti(){
		if (catalogoProgetti==null) {
		try {
			catalogoProgetti = new Catalogo<Progetto>();
			catalogoProgetti.catalogo = DBManager.getInstance().listaProgetti();
		} catch (SQLException e) {
            UserCommunicator.print(UserCommunicator.ERROR_INSERT);
		}
	}
		return catalogoProgetti;
	}

	/**
	 * Restituisce il catalog dei progettisti
	 * @return catalogo progettisti
	 */
	public Collection<Utente> getCatalogoUtenti(){
		if (catalogoUtenti==null) {
			try { 
				catalogoUtenti = new Catalogo<Utente>();
				catalogoUtenti.catalogo = DBManager.getInstance().getListaUtenti();
			} catch (SQLException e) {
	            UserCommunicator.print(UserCommunicator.ERROR_INSERT);
			}
		}
			return catalogoUtenti;
		}
	

	/**
	 * Restituisce il catalogo degli esperti
	 * @return catalogo Esperti
	 */
	public Collection<Utente> getCatalogoEsperti(){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return catalogoUtenti.search(p-> p.getRole().isExpert());
	}
	/**
	 *  @return catalogo dei Progettisti
	 * 
	 */

	public Collection<Utente> getCatalogoProgettisti(){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return catalogoUtenti.search(p-> !p.getRole().isExpert() && !p.getRole().isEnte());
	}
	/**
	 * Restituisce la lista dei progetti proposti da un utente
	 * @param userID l'id dell'utente di cui si vogliono ottenere i progetti
	 * @return lista dei progetti dell'utente
	 */
	public  Collection<Progetto> getListaMieiProgetti(String userID) {
		if (catalogoProgetti==null) getCatalogoProgetti();
		return catalogoProgetti.search(p->p.getCreatorID().equals(userID) || p.getIDSelezionatore().equals(userID));
	}
	/** 
	 * @param l'oggetto utente
	 * @return la lista dei progetti dell'utente
	 */
	public Collection<Progetto> getListaMieiProgetti(Utente u) {
		if (catalogoProgetti==null) getCatalogoProgetti();
		return catalogoProgetti.search(p->p.getProponente().equals(u));
	}
	
	/**
	 * @param collection cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getEspertiCompetenti(String competenza){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return Bacheca.catalogoUtenti.search(p-> p.getCompetenze().contains(competenza) && p.getRole().isExpert());
	}
	/**
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getEspertiCompetenti(Collection<String> competenze){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return Bacheca.catalogoUtenti.search(p-> p.getCompetenze().containsAll(competenze) && p.getRole().isExpert());
	}
	/**
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getProgettistiCompetenti(String competenze){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return Bacheca.catalogoUtenti.search(p-> p.getCompetenze().contains(competenze) && (!p.getRole().isExpert()));
	}
	/**
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getProgettistiCompetenti(Collection<String> competenze){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return Bacheca.catalogoUtenti.search(p-> p.getCompetenze().containsAll(competenze));
	}
	
	
	public Collection<Utente> getCatalogoEnti() {
		if(catalogoUtenti==null) getCatalogoUtenti();
		return catalogoUtenti.search(p-> p.getRole().isEnte());
	}



}