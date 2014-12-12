package it.unibo.scopone.impl.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.unibo.scopone.impl.Deck;
import it.unibo.scopone.impl.Table;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IPlayerAgent;
import it.unibo.scopone.interfaces.ITable;
import it.unibo.scopone.structs.Action;
import it.unibo.scopone.structs.Rules;

/**
 * Si presenta come base di un player agent
 * 
 * @author Pierluigi
 */
public class BasicPlayer implements IPlayerAgent {

	String name;
	IPlayerAgent nextAgent;
	List<ICard> cardsOnHand;
	ITable table; // Riferimento al tavolo di gioco
	List<ICard> personalDeck; // mazzetto

	Action intendedAction = null;
	int scopeCount = 0;

	public BasicPlayer(String name, IPlayerAgent nextAgent) {
		this.name = name;
		this.nextAgent = nextAgent;
		init();
	}

	public BasicPlayer(String name) {
		this.name = name;
		init();
	}

	public void setNextPlayer(IPlayerAgent nextAgent) {
		this.nextAgent = nextAgent;
	}

	@Override
	public String getName() {
		return name;
	}

	private void init() {
		personalDeck = new ArrayList<ICard>();
		table = Table.getInstance();
	}

	@Override
	public void setCardsOnHand(List<ICard> cards) {
		this.cardsOnHand = cards;
	}

	void deliberate() {
		/*
		 * Processo deliberativo ignorante su cui fare override sceglie
		 * semplicemente una carta a caso dal mazzo, totalmente imprevedibile,
		 * nessuna intelligenza.
		 */
		int n = new Random().nextInt(cardsOnHand.size());
		ICard randomCard = cardsOnHand.get(n);
		List<List<ICard>> prese = Rules.getPrese(randomCard,
				table.cardsOnTable());
		if (prese.size() != 0)
			intendedAction = new Action(this, randomCard, prese.get(0));
		else
			intendedAction = new Action(this, randomCard,
					new ArrayList<ICard>()); // empty

	}

	private void playCard(ICard card, List<ICard> taking) {
		if (table.action(card, taking)) {
			log("Giocata la carta " + card.getCardStr());
			cardsOnHand.remove(card); // rimuove la carta dalla mano
			if (taking.size() != 0){
				personalDeck.addAll(taking); // Aggiungo le carte al mazzetto
				log("Presa: " + cardsListStr(taking));
				}
			if(table.cardsOnTable().size() == 0)
				scopeCount++; //Ho eseguito una scopa;
		} else {
			log("Impossibile giocare la carta " + card.getCardStr());
			throw new IllegalStateException("L'agente " + getName()
					+ " sta eseguendo un azione non valida");
		}
	}

	private void endTurn() {
		printStatus(); // TODO to comment
		log("Passa il turno al prossimo giocatore: " + nextAgent.getName());
		nextAgent.notifyStartTurn();
	}

	@Override
	public void notifyStartTurn() {
		// Starts thinking and do stuff
		log("E' il mio turno!");
		stampaCarteInMano();
		if (cardsOnHand.size() > 0) // se ho carte in mano
			execute();
	}
	
	public void stampaCarteInMano(){
		if(cardsOnHand.size() > 0){
			String str = cardsListStr(cardsOnHand);
			log("Carte in mano: \n" + str +"\n---");
		}
	}
	
	private String cardsListStr(List<ICard> list)
	{
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			str+=list.get(i).getCardStr() + " ";
		}
		return str;
	} 

	/**
	 * comportamento core
	 */
	private void execute() {
		deliberate();
		if (intendedAction != null) {
			playCard(intendedAction.playedCard, intendedAction.taking);
			endTurn(); // il tuo turno � terminato
		} else {
			log("deliberation() non ha restituito nessuna azione");
			throw new IllegalStateException(
					"Impossibile eseguire un azione non deliberata");
		}
	}

	public int evaluateGameScore() {
		int score = 0;
		// TODO implementare sistema di calcolo punteggio in Rules
		log("Il mio punteggio e'" + score);
		return score;
	}

	private void printStatus() {
		log("Carte nel mazzetto: " + personalDeck.size());
		log("Scope: " + scopeCount);
		log("Carte in mano: " + cardsOnHand.size());
	}

	protected void log(String text) {
		System.out.println(name + "] " + text);
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// MAIN FOR TESTING
	public static void main(String[] args) {
		Deck deck = new Deck();
		ITable table = Table.getInstance();
		BasicPlayer p1, p2, p3, p4;
		p4 = new BasicPlayer("p4");
		p3 = new BasicPlayer("p3", p4);
		p2 = new BasicPlayer("p2", p3);
		p1 = new BasicPlayer("p1", p2);
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
		// All setup, deck should be empty
		System.out.println("Carte sul tavolo:");
		table.printTableCards();
		p1.notifyStartTurn(); // Inizia a giocare, seguiranno tutti gli altri
	}
	// /////////////////////////////////////////////////////////////////////////////////////////
}
