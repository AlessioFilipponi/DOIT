package it.unicam.cs.ids.doit.view;

import it.unicam.cs.ids.doit.user.Ente;
import it.unicam.cs.ids.doit.user.Esperto;
import it.unicam.cs.ids.doit.user.Progettista;

public interface Menu {
	public default String menu() {
		return "\n\n\n ****> MENU PRINCIPALE <****\n" +
				"1) Visualizza progetti\n" ;

		
	}
	public default String myMenu(Ente e) {
		return menu()+
				"2) Proponi un progetto\n" +
				"3) Valuta inviti a partecipare\n"+
				"4) Richiedi Valutazione Proposta Progetto\n" +
				"5) Invita Collaboratore";
		
	}
	public default String myMenu(Esperto e) {
		return menu() +
				"2) Proponi un progetto\n" +
				"3) Valuta inviti a partecipare\n"+
				"4) Richiedi Valutazione Proposta Progetto\n" +
				"5) Valuta Progetto";
	}

}
