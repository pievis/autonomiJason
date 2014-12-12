package it.unibo.scopone.impl;

import java.util.List;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.ITable;
import it.unibo.scopone.structs.Rules;

public class Table implements ITable {
	// Vediamolo come ambiente, implementiamo con un singleton
	static ITable table = null;

	public Table() {
	}

	public static synchronized ITable getInstance() {
		if (table == null)
			table = new Table();
		return table;
	}

	//

	List<ICard> cardsOnTable;

	@Override
	public List<ICard> cardsOnTable() {
		return cardsOnTable;
	}

	@Override
	public void setCardsOnTable(List<ICard> birthCard) {
		cardsOnTable = birthCard;
	}

	@Override
	public boolean action(ICard card, List<ICard> taking) {
		if (Rules.azionePossibile(card, cardsOnTable, taking)) {
			cardsOnTable.add(card);
			if (taking.size() != 0) {
				// Presa effettuata, raccolgono carte dal tavolo
				cardsOnTable.removeAll(taking);
			}
			updateTableView();
			return true;
		} else
			return false;
	}
	
	private void updateTableView(){
		printTableCards();
	}

	@Override
	public void printTableCards() {
		System.out.println("\n---TABLE CARDS---\n");
		for (ICard c : cardsOnTable)
			System.out.println(c.getCardStr());
		System.out.println("------\n");
	}
}
