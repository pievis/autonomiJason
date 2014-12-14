// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Rules;

import java.util.List;

import utils.PrologUtils;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class trustScopa extends DefaultInternalAction {

	//sintassi: playerLib.trustScopa(card(5,bastoni),Xt,Trust)
	//Xt = lista carte sul tavolo
	//Trust = variabile da unificare con il valore di fiducia di quella mossa
	//ad esempio: 0.4
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
//        ts.getAg().getLogger().info("executing internal action 'playerLib.trustScopa'");
        if (!args[0].isGround() || !args[1].isList() || !args[2].isVar())
        	throw new JasonException("check arguments");
        ListTerm cardsOnTableTerm = ListTermImpl.parseList(args[1]+"");
        Term cardTerm = args[0];
        ICard card = PrologUtils.parseCard(cardTerm+"");
        List<ICard> cardsOnTable = PrologUtils.parseCardList(cardsOnTableTerm);
        double trust = scopa(card, cardsOnTable);
        boolean result = un.unifies(args[2], new NumberTermImpl(trust));
        return result;
    }
    
    /**
	 * Ragiona e ritorna una fiducia relativa al compiere 
	 * un punto facendo scopa.
	 * 
	 * @param card
	 *            = carta in esame
	 * @return fiducia
	 */
	public static double scopa(ICard card, List<ICard> cardsOnTable) {
		List<List<ICard>> prese = Rules.getPrese(card, cardsOnTable);
		for(List<ICard> presa : prese)
			if(presa.size() == cardsOnTable.size())
				return 1.0; //Faccio una scopa;
		return 0.0;
	}
}
