// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;
import it.unibo.scopone.structs.Seed;

import java.util.List;

import utils.PrologUtils;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class trustDenari extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
//				ts.getAg().getLogger()
//						.info("executing internal action 'playerLib.trustDenari'");
				if (!args[0].isGround() || !args[1].isList() || !args[2].isVar()) {
					throw new JasonException("check arguments");
				}
				ListTerm cardsOnTableTerm = ListTermImpl.parseList(args[1] + "");
				Term cardTerm = args[0];
				ICard card = PrologUtils.parseCard(cardTerm + "");
				List<ICard> cardsOnTable = PrologUtils.parseCardList(cardsOnTableTerm);
				double trust = denari(card, cardsOnTable);
				boolean result = un.unifies(args[2], new NumberTermImpl(trust));
				return result;
	}

	public static double denari(ICard card, List<ICard> cardsOnTable) {
		// Una presa contiene anche la carta in esame
		List<List<ICard>> prese = Rules.getPrese(card, cardsOnTable);
		if (prese.size() > 0) {
			double fp = 0.0;
			for (List<ICard> presa : prese)
				for (ICard c : presa)
					if (c.getSeed() == Seed.DENARI)
						fp += 0.3;
			return fp;
		}
		return 0.0;
	}
}
