// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;

import java.util.List;

import utils.PrologUtils;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class trustPrimiera extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
		ts.getAg().getLogger()
				.info("executing internal action 'playerLib.trustPrimiera'");
		if (!args[0].isGround() || !args[1].isList() || !args[2].isVar()) {
			throw new JasonException("check arguments");
		}
		ListTerm cardsOnTableTerm = ListTermImpl.parseList(args[1] + "");
		Term cardTerm = args[0];
		ICard card = PrologUtils.parseCard(cardTerm + "");
		List<ICard> cardsOnTable = PrologUtils.parseCardList(cardsOnTableTerm);
		double trust = primiera(card, cardsOnTable);
		boolean result = un.unifies(args[2], new NumberTermImpl(trust));
		return result;
	}

	public static double primiera(ICard card, List<ICard> cardsOnTable) {
		if (card.getNumber() == 7) {
			if (Rules.existPresa(card, cardsOnTable)) {
				return 0.3;
			}
		}
		for (ICard tcard : cardsOnTable) {
			// c'e' un 7 tra le carte in gioco sul tavolo
			if (tcard.getNumber() == 7) {
				List<List<ICard>> prese = Rules.getPrese(card, cardsOnTable);
				for (List<ICard> presa : prese) {
					if (presa.contains(tcard))
						return 0.3;
				}
			}
		}
		return 0.0;
	}
}
