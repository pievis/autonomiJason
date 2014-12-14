// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import utils.BasicMaths;
import utils.PrologUtils;
import utils.logging.ILogger.LogLevel;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class selectCardWithThinking extends DefaultInternalAction {

	//playerLib.selectCardWithThinking(L, Card, Taking);
	//Dove L è una lista nella sintassi:
	//[trust(card(10,bastoni),carte,0.5),trust(card(7,spade),settebello,0.1),...]
	//carte,settebello,primiera,denari,scopa
	
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
        ts.getAg().getLogger().info("executing internal action 'playerLib.selectCardWithThinking'");
        if (!args[0].isList() || !args[1].isVar() || !args[2].isVar()) {
			throw new JasonException("check arguments");
		}
        
        List<ICard> selection = new ArrayList<ICard>();
        ListTerm trustedValues = ListTermImpl.parseList(args[0] + "");
        for(Term trustTerm : trustedValues ){
        	//let's store every trusted card
        	Structure trustStruct  = Structure.parse(trustTerm + "");
        	ICard card = PrologUtils.parseCard(trustStruct.getTerm(0)+""); //prendo la carta
        	//ignoro l'obbiettivo per il momento
        	
        }
        
        // everything ok, so returns true
        return true;
    }
    
    ICard getCartWithProbability(double[] trustArray, List<ICard> cardsOnHand) {
		ICard card = null;
		// Se le carte mancano di fiducia, allora ne seleziono semplicemente una
		// random
		double max = BasicMaths.arrayMax(trustArray);
		if (max <= 0.0) {
			int randomIndex = new Random().nextInt(cardsOnHand.size());
			card = cardsOnHand.get(randomIndex);
//			log("Scelta la carta " + card.getCardStr() + " con fiducia: "
//					+ trustArray[randomIndex], LogLevel.SPECIFIC);
			return card;
		}
		// Altrimenti la scelgo basandomi sulla fiducia
		// normalizzo il trust array con double da 0.0 a 1.0
		double[] probabilities = BasicMaths.normalizeArray(trustArray, 1.0);
		int[] singletons = new int[cardsOnHand.size()];
		for (int i = 0; i < cardsOnHand.size(); i++)
			singletons[i] = i;
		EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(
				singletons, probabilities);
		int randomIndex = distribution.sample(); // prende la carta in relazione
													// alla probabilitÃ  non
													// uniforme
		card = cardsOnHand.get(randomIndex);
//		log("Scelta la carta " + card.getCardStr() + " con fiducia: "
//				+ trustArray[randomIndex] + " => " + probabilities[randomIndex],
//				LogLevel.SPECIFIC);
		return card;
	}
}
