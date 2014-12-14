// Internal action code for project scopone

package playerLib;

import it.unibo.scopone.interfaces.ICard;

import java.util.ArrayList;
import java.util.List;

import jason.*;
import jason.asSemantics.*;
import jason.asSyntax.*;

public class getWinner extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        // execute the internal action
        //ts.getAg().getLogger().info("executing internal action 'playerLib.getWinner'");
    	if (!args[0].isList() || !args[1].isVar()) {
			throw new JasonException("check arguments");
		}
        
    	int maxScore = -1;
    	List<String> winners = new ArrayList<String>();
    	ListTerm scoreValues = ListTermImpl.parseList(args[0] + "");
        for(Term scoreTerm : scoreValues){
        	Structure scoreStruct  = Structure.parse(scoreTerm + "");
        	int value = Integer.parseInt(scoreStruct.getTerm(1)+"");
        	if(value > maxScore){
        		//winner = scoreStruct.getTerm(0)+"";
        		maxScore = value;
        		}
        }
        for(Term scoreTerm : scoreValues){
        	Structure scoreStruct  = Structure.parse(scoreTerm + "");
        	int value = Integer.parseInt(scoreStruct.getTerm(1)+"");
        	if(value == maxScore){
        		winners.add(scoreStruct.getTerm(0)+"");
        		}
        }
        String winnerStr = "winners(";
        if(winners.size() == 1)
        	winnerStr = winners.get(0);
        else{
        	for(int i = 0; i < winners.size()-1; i++)
        		winnerStr += winners.get(i) +",";
        	
        	winnerStr += winners.get(winners.size()-1)+")";
        }
        return un.unifies(args[1], Literal.parse(winnerStr));
    }
}
