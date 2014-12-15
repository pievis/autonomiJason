// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.impl.Table;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;
import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import utils.PrologUtils;

public class selectCardInput extends DefaultInternalAction {

	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		// execute the internal action
		// ts.getAg().getLogger().info("executing internal action 'playerLib.selectCardRandom'");
		if (!args[0].isList() || !args[1].isVar() || !args[2].isVar())
			throw new JasonException("check arguments");
		ListTerm cardList = ListTermImpl.parseList(args[0] + "");
		List<ICard> cardsOnTable = Table.getInstance().cardsOnTable();

		String[] possibilities = new String[cardList.size()];
		for (int i = 0; i < possibilities.length; i++) {
			possibilities[i] = cardList.get(i) + "";
		}

		String stringListCardOnTable = "Cards on the table:\n\n" + PrologUtils.cardListToStrRapp(cardsOnTable);

		String selection = (String) JOptionPane.showInputDialog(null, stringListCardOnTable,
				"Select card", JOptionPane.PLAIN_MESSAGE, null, possibilities,
				null);

		boolean result = false;
		Term selCardTerm;
		ICard card;
		if (selection == null) {
			int rand = new Random().nextInt(cardList.size());
			selCardTerm = cardList.get(rand);
			card = PrologUtils.parseCard(selCardTerm + "");
			// String player = "p1";
		} else {
			selCardTerm = Literal.parse(selection);
			card = PrologUtils.parseCard(selection);
		}
		List<List<ICard>> takings = Rules.getPrese(card, Table.getInstance()
				.cardsOnTable());
		List<ICard> taking;
		if (takings.size() > 0)
			taking = takings.get(0);
		else
			taking = new ArrayList<ICard>();
		Term takingTerm = Literal.parse(PrologUtils.cardListToStrRapp(taking));
		// Action retAction = new Action(player, card, taking);
		// ts.getAg().getLogger().info("ACTION : " + retAction.getRep());
		// Term retTerm = Literal.parse(retAction.getRep());
		// ts.getAg().getLogger().info("Unifies with: " + retTerm);

		result = un.unifies(args[1], selCardTerm)
				&& un.unifies(args[2], takingTerm);
		return result;
	}
}
