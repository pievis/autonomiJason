package env;

import it.unibo.scopone.impl.Deck;
import it.unibo.scopone.impl.Table;
import it.unibo.scopone.impl.agents.JasonPlayer;
import it.unibo.scopone.impl.agents.BasicPlayer;
import it.unibo.scopone.impl.agents.JasonPlayer;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IDeck;
import it.unibo.scopone.interfaces.IPlayerAgent;
import it.unibo.scopone.interfaces.ITable;
import it.unibo.scopone.structs.Action;

import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

import utils.PrologUtils;

public class GameModel{
	private JasonPlayer p1, p2, p3, p4;
	public ITable table;
	
	public GameModel() {
		init();
	}
	 
	public void init() {
		Deck deck = new Deck();
		table = Table.getInstance();
		initPlayers(table, deck);
		// All setup, deck should be empty
		System.out.println("Carte sul tavolo:");
		table.printTableCards();
		//p1.notifyStartTurn(); // Inizia a giocare, seguiranno tutti gli altri
	}
	
	private  void initPlayers(ITable table, IDeck deck){
		p4 = new JasonPlayer("p4");
		p3 = new JasonPlayer("p3", p4);
		p2 = new JasonPlayer("p2", p3);
		p1 = new JasonPlayer("p1", p2);
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
	
	public IPlayerAgent getPlayer(int i){
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
	
	public boolean PlayAction(Term actionTerm, String playerStr){
		JasonPlayer p = (JasonPlayer) getPlayerByName(playerStr);
		Action action = PrologUtils.parseActionTerm(actionTerm + "");
		boolean result = false;
		result = p.playAction(action);
		return result;
	}
	
	public JasonPlayer getPlayerByName(String agName){
		JasonPlayer jp = null;
		if(agName.equals(p1.getName()))
			return p1;
		if(agName.equals(p2.getName()))
			return p2;
		if(agName.equals(p3.getName()))
			return p3;
		if(agName.equals(p4.getName()))
			return p4;
		return jp;
	}
}
