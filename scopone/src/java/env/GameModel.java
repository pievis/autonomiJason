package env;

import it.unibo.scopone.impl.Deck;
import it.unibo.scopone.impl.Table;
import it.unibo.scopone.impl.agents.AgentPlayer;
import it.unibo.scopone.impl.agents.BasicPlayer;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IDeck;
import it.unibo.scopone.interfaces.ITable;

import java.util.ArrayList;
import java.util.List;

public class GameModel{
	static BasicPlayer p1, p2, p3, p4;
	
	public static void init() {
		Deck deck = new Deck();
		ITable table = Table.getInstance();
		initPlayers(table, deck);
		// All setup, deck should be empty
		System.out.println("Carte sul tavolo:");
		table.printTableCards();
		p1.notifyStartTurn(); // Inizia a giocare, seguiranno tutti gli altri
	}
	
	private static void initPlayers(ITable table, IDeck deck){
		p4 = new AgentPlayer("p4");
		p3 = new AgentPlayer("p3", p4);
		p2 = new AgentPlayer("p2", p3);
		p1 = new AgentPlayer("p1", p2);
		p4.setNextPlayer(p1);
		List<ICard> p1c = new ArrayList<ICard>();
		List<ICard> p2c = new ArrayList<ICard>();
		List<ICard> p3c = new ArrayList<ICard>();
		List<ICard> p4c = new ArrayList<ICard>();
		List<ICard> tc = new ArrayList<ICard>();
		for (int i = 0; i < 9; i++) { // carte ai giocatori
			p1c.add(deck.dealCard());
			p2c.add(deck.dealCard());
			p3c.add(deck.dealCard());
			p4c.add(deck.dealCard());
		}
		for (int i = 0; i < 4; i++) { // carte sul tavolo
			tc.add(deck.dealCard());
		}
		table.setCardsOnTable(tc);
		p1.setCardsOnHand(p1c);
		p2.setCardsOnHand(p2c);
		p3.setCardsOnHand(p3c);
		p4.setCardsOnHand(p4c);
	}
	
	public BasicPlayer getPlayer(int i){
		if(i == 1)
			return p1;
		if(i == 2)
			return p2;
		if(i == 3)
			return p3;
		if(i == 4)
			return p4;
		return null;
	}
}
