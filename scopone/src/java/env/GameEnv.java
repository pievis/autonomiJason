package env;

import it.unibo.scopone.impl.Deck;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

public class GameEnv extends Environment {
	//LITERALS
	public static final Literal shuffleLit = Literal.parseLiteral("shuffle_deck");
	public static final String deliberateFunc = "deliberate";
	////////////////////////////////
	
	//ENV variables
	Deck deck;
	
	@Override
	public void init(String[] args) {
		deck = new Deck();
	}
	
	//Percepisce i cambiamenti e aggiorna i belief degli agenti
	@Override
	public void addPercept(Literal per) {
		
	}
	
	//Viene richiamato dall'agente per eseguire azioni esterne
	//EG: gioca una carta, mischia il mazzo...
	@Override
	public boolean executeAction(String agName, Structure act) {
		//System.out.println("[" + ag + "] doing: " + action);
		boolean result = false;
		String functor = act.getFunctor();
		if (act.equals(shuffleLit)) {
			//This is just for testing
			result = true;
			System.out.println("Mischio il mazzo " + agName);
		}
		if(functor.equals(deliberateFunc)){
			result = true;
			Term term = act.getTerm(0);
			System.out.println(term);
		}
		return result;
	}
}
