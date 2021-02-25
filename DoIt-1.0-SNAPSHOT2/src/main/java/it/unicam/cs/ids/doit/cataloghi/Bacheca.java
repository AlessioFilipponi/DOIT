package it.unicam.cs.ids.doit.cataloghi;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unicam.cs.ids.doit.progetto.Progetto;
import it.unicam.cs.ids.doit.ui.UserCommunicator;
import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;

import it.unicam.cs.ids.doit.user.Utente;
import it.unicam.cs.ids.doit.utilities.DBManager;
/**
 * La Bacheca è una classe Singleton che contiene il Catalogo Progetti e il 
 * Catalogo Utenti. E' usata in maniera analoga al Proxy per la lettura dal DB.
 *
 */

public class Bacheca {
	private static Catalogo<Utente> catalogoUtenti;
	private static Catalogo<Progetto> catalogoProgetti;
	private static Bacheca instance;

	/**
	 * Costruttore privato della Bacheca
	 */
	private Bacheca() {
	}
	
	/**
	 * Metodo per accedere ai metodi della Bacheca
	 * @return instance della Bacheca
	 */
	public static Bacheca getInstance() {
		if(instance ==null)
			instance = new Bacheca();
		return instance;
	}

	/**
	 * Restituisce il catalogo dei progetti
	 * @return catalogoProgetti 	Catalogo Progetti è un oggetto di tipo Catalogo
	 *                              contenente tutti i Progetti. lla prima chiamata del
	 *                              metodo accede al DB per caricare tutti i Progetti
	 *                              in locale. 
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
	 * Restituisce il catalogo degli Utenti
	 * @return catalogoUtenti  		Catalogo Utenti è un oggetto di tipo Catalogo
	 *                              contenente tutti gli Utenti. Alla prima chiamata del
	 *                              metodo accede al DB per caricare tutti gli Utenti
	 *                              in locale. 		
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
	 *  Restituisce il Catalogo dei Progettisti
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
	 * Restituisce la lista dei progetti proposti da un utente
	 * @param u l'oggetto utente
	 * @return la lista dei progetti dell'utente
	 */
	public Collection<Progetto> getListaMieiProgetti(Utente u) {
		if (catalogoProgetti==null) getCatalogoProgetti();
		return catalogoProgetti.search(p->p.getProponente().equals(u) || p.getSelezionatore().equals(u));
	}
	
	/**
	 * Restituisce la lista degli esperti con la competenza cercata 
	 * @param competenza collection cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getEspertiCompetenti(String competenza){
		if(catalogoUtenti==null) getCatalogoUtenti();
		return Bacheca.catalogoUtenti.search(p-> p.getCompetenze().contains(competenza) && p.getRole().isExpert());
	}
	/**
	 * Restituisce la lista degli Esperti che contengono una delle competenze passate
	 * @param competenze cercate
	 * @return la lista degli Esperti che hanno tra le competenze la competenza passata
	 */
	public Collection<Utente> getEspertiCompetenti(Collection<String> competenze){
		if(catalogoUtenti==null) getCatalogoUtenti();
	    Set<Utente> esperti = new HashSet<Utente>();
		for (String competenza : competenze) {
			esperti.addAll(getEspertiCompetenti(competenza));
		
		}
		return esperti;
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
		Set<Utente> progettisti = new HashSet<Utente>();
		for (String competenza : competenze) {
			progettisti.addAll(getProgettistiCompetenti(competenza));
		
		}
		return progettisti;
	}
	
	/**
	 * @return lista degli Enti 
	 */
	public Collection<Utente> getCatalogoEnti() {
		if(catalogoUtenti==null) getCatalogoUtenti();
		return catalogoUtenti.search(p-> p.getRole().isEnte());
	}



}