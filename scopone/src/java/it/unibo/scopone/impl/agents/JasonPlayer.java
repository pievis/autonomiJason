package it.unibo.scopone.impl.agents;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IPlayerAgent;
import it.unibo.scopone.structs.Action;
import jason.asSyntax.Literal;

import java.util.List;

import utils.PrologUtils;

public class JasonPlayer extends BasicPlayer {

	public JasonPlayer(String name, IPlayerAgent nextAgent) {
		super(name, nextAgent);
	}
	
	public JasonPlayer(String name) {
		super(name);
	}

	// Json interfaces

	public List<ICard> getCardsOnHand() {
		return cardsOnHand;
	}

	public Literal getCardsOnHandLiteral() {
		String str = PrologUtils.cardListToStrRapp(cardsOnHand);
		Literal lit = Literal.parseLiteral("cardsOnHand(" + str + ")");
		return lit;
	}

	public IPlayerAgent getNextPlayer() {
		return nextAgent;
	}

	public List<ICard> getPersonalDeck() {
		return personalDeck;
	}

	public boolean playAction(Action action) {
		ICard card = action.playedCard;
		List<ICard> taking = action.taking;
		return playCard(card, taking);
	}

	//

}
