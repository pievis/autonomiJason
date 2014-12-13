// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;
import it.unibo.scopone.structs.Seed;

import java.util.List;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class trustDenari extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
		ts.getAg().getLogger()
				.info("executing internal action 'playerLib.trustSettebello'");
		if (true) { // just to show how to throw another kind of exception
			throw new JasonException("not implemented!");
		}

		// everything ok, so returns true
		return true;
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
