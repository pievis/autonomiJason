// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;

import java.awt.font.NumericShaper;
import java.util.List;

import utils.PrologUtils;
import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getScore extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
		ts.getAg().getLogger()
				.info("executing internal action 'playerLib.getScore'");
		if (!args[0].isList() || !args[1].isNumeric() || !args[2].isVar()) {
			throw new JasonException("check arguments");
		}
		ListTerm cardsOnPersonalDeckTerm = ListTermImpl.parseList(args[0] + "");
		int numScope = Integer.parseInt(args[1] + "");
		List<ICard> cardsOnPersonalDeck = PrologUtils
				.parseCardList(cardsOnPersonalDeckTerm);
		int newScore = calcScore(cardsOnPersonalDeck, numScope);
		boolean result = un.unifies(args[2], new NumberTermImpl(newScore));
		return result;
	}

	public static int calcScore(List<ICard> cardOnPersonalDeck, int numScope) {
		int prese = Rules.finalScore(cardOnPersonalDeck, numScope);
		return prese;
	}
}