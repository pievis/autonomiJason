// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.impl.Table;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Action;
import it.unibo.scopone.structs.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.PrologUtils;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class selectCardRandom extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
        ts.getAg().getLogger().info("executing internal action 'playerLib.selectCardRandom'");
        if (!args[0].isList() || !args[1].isVar() || !args[2].isVar())
        	throw new JasonException("check arguments");
	    ListTerm cardList = ListTermImpl.parseList(args[0]+"");
	    int rand = new Random().nextInt(cardList.size());
	    Term randCardTerm = cardList.get(rand);
//	    String player = "p1";
	    ICard card = PrologUtils.parseCard(randCardTerm+"");
	    List<List<ICard>> takings = Rules.getPrese(card, Table.getInstance().cardsOnTable());
	    List<ICard> taking;
	    if(takings.size() > 0)
	    	taking = takings.get(0);
	    else
	    	taking = new ArrayList<ICard>();
	    Term takingTerm = Literal.parse(PrologUtils.cardListToStrRapp(taking));
//	    Action retAction = new Action(player, card, taking);
//	    ts.getAg().getLogger().info("ACTION : " + retAction.getRep());
//	    Term retTerm = Literal.parse(retAction.getRep());
//	    ts.getAg().getLogger().info("Unifies with: " + retTerm);
	    boolean result = un.unifies(args[1], randCardTerm)
	    	&& un.unifies(args[2], takingTerm);
	    return result;
    }
}
