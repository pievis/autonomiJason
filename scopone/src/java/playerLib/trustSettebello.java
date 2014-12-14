// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;

import java.util.List;

import utils.PrologUtils;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class trustSettebello extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
		ts.getAg().getLogger()
				.info("executing internal action 'playerLib.trustSettebello'");
		if (!args[0].isGround() || !args[1].isList() || !args[2].isVar()) {
			throw new JasonException("check arguments");
		}
		ListTerm cardsOnTableTerm = ListTermImpl.parseList(args[1] + "");
		Term cardTerm = args[0];
		ICard card = PrologUtils.parseCard(cardTerm + "");
		List<ICard> cardsOnTable = PrologUtils.parseCardList(cardsOnTableTerm);
		double trust = setteBello(card, cardsOnTable);
		boolean result = un.unifies(args[2], new NumberTermImpl(trust));
		return result;
	}

	/**
	 * Ragiona e ritorna una fiducia relativa al compiere un punto facendo
	 * Settebello.
	 * 
	 * @param card
	 *            = carta in esame
	 * @return fiducia
	 */
	public static double setteBello(ICard card, List<ICard> cardsOnTable) {
		// La carta che esamino è il sette bello
		if (Rules.isSetteBello(card)) {
			if (Rules.existPresa(card, cardsOnTable))
				;
			return 1.0;
		}
		for (ICard tcard : cardsOnTable) {
			// Il settebello è tra le carte in gioco (sul tavolo)
			if (Rules.isSetteBello(tcard)) {
				List<List<ICard>> prese = Rules.getPrese(card, cardsOnTable);
				for (List<ICard> presa : prese) {
					// Se almeno una presa contiene il sette bello allora ho più
					// fiducia
					if (presa.contains(tcard))
						return 1.0;
				}
			}
		}
		return 0.0;
	}
}
