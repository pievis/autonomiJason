package env;

import java.util.logging.Logger;

import utils.PrologUtils;

import com.sun.corba.se.spi.ior.IdentifiableBase;

import it.unibo.scopone.impl.Deck;
import it.unibo.scopone.impl.Table;
import it.unibo.scopone.impl.agents.BasicPlayer;
import it.unibo.scopone.interfaces.IPlayerAgent;
import it.unibo.scopone.interfaces.ITable;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.Environment;

public class GameEnv extends Environment {
	//LITERALS, FUNCTORS, ETC
	public static final Literal shuffleLit = Literal.parseLiteral("shuffle_deck");
	public static final String deliberateFunc = "deliberate";
	public static final String palyCardFunc = "playCard";
	////////////////////////////////
	
	static Logger logger = Logger.getLogger(GameEnv.class.getName());
	
	//ENV variables
	GameModel gameModel;
	boolean gameStarted = false;
	
	@Override
	public void init(String[] args) {
		System.out.println("Env started");
		initPercept();
	}
	
	void initPercept(){
		clearPercepts();
		gameModel = new GameModel(); //inizializza il gioco, smista le carte
		setupNextPlayer();
		updateCardsOnHandPercept();
		updateCardsOnTablePercept();
		gameStarted = true;
	}
	
	void setupNextPlayer(){
		for(int i = 1; i <=4; i++){
			BasicPlayer p = (BasicPlayer) gameModel.getPlayer(i);
			String litString = "nextPlayer("+p.getNextPlayer().getName()+")";
			Literal nextPlayerLit = Literal.parseLiteral(litString);
			addPercept(p.getName(),nextPlayerLit);
		}
	}
	
	void updatePercept(){
		clearPercepts(); //gli agenti scordano quanto percepito in precedenza
		if(!gameStarted){
			initPercept();
		}
	}
	
	void updateCardsOnHandPercept(){
		for(int i = 1; i <=4; i++){
			BasicPlayer p = (BasicPlayer) gameModel.getPlayer(i);
			addPercept(p.getName(),p.getCardsOnHandLiteral());
		}
	}
	
	void updateCardsOnTablePercept(){
		//ogni giocatore percepisce le carte che sono sul tavolo
		ITable table = Table.getInstance();
		String cardsOnTableStr = "cardsOnTable("+PrologUtils.cardListToStrRapp(table.cardsOnTable())+")";
		Literal cardsOnTableLit = Literal.parseLiteral(cardsOnTableStr);
		addPercept(cardsOnTableLit); 
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
			log(term+"");
		}
		return result;
	}
	
	
	///////////////////////////////////////////////////
	private void log(String text){
		logger.info(text);
		//System.out.println(text);
	}
}
