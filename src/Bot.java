import java.util.ArrayList;

public class Bot extends Player{
    public Bot(String name){
        super(name);
    }
  
    public int[] play(RoundState state, int depth, int botIdx, int originalDepth, int alpha, int beta){
        ArrayList<Player> players = state.getPlayers();
        if(depth == 0){
            int evalScore = 0;
            for(int i = 0; i < botIdx; i++){
                evalScore += players.get(i).getNumCards();
            }
            int botCards = state.getPlayers().get(botIdx).getNumCards();
            evalScore -= (botCards * botCards);
            int eval[] = {evalScore, -100};
            return eval;
        }

        int currentPlayerIdx = state.getCurrentPlayerIdx();
        Player currentPlayer = players.get(currentPlayerIdx);
        Card gameCard = state.getGameCard();

        // If not playable
        if(state.getTurnStatus() != 0){
            RoundState newState = new RoundState(state);
            if(state.getTurnStatus() > 0){
                newState.setTurnStatus(0);
            }
            else if(state.getTurnStatus() < 0){
                newState.setTurnStatus(state.getTurnStatus() + 1);
            }
            int nextPlayerIndex = (newState.getCurrentPlayerIdx() + newState.getDirection() + players.size()) % players.size();
            newState.setCurrentPlayerIdx(nextPlayerIndex);
            int eval[] = play(newState, depth - 1, botIdx, originalDepth, alpha, beta);
            eval[1] = -100;
            return eval;
        }

        // Check draw card
        RoundState newState = new RoundState(state);
        newState.getCurrentPlayer().addCard(newState.drawCard());
        // Increment player
        int nextPlayerIndex = (newState.getCurrentPlayerIdx() + newState.getDirection() + players.size()) % players.size();
        newState.setCurrentPlayerIdx(nextPlayerIndex);

        int[] eval = play(newState, depth - 1, botIdx, originalDepth, alpha, beta);
        eval[1] = -1;
        if(currentPlayerIdx >= botIdx){
            alpha = Integer.max(eval[0], alpha);
        }
        else{
            beta = Integer.min(eval[0], beta);
        }
        if(beta < alpha){
            return eval;
        }

        if(depth == originalDepth) System.out.println("Considering Draw 1 for a score of: " + eval[0]);
        // Check play card
        for(int i = 0; i < currentPlayer.getHand().size(); i++){
            Card card = currentPlayer.getHand().get(i);
            if(card.validateCard(gameCard)){
                newState = new RoundState(state);
                if(card instanceof WildCard){
                    ((WildCard) card).botPlay(newState);
                }
                else if(card instanceof WildDrawTwoCard){
                    ((WildDrawTwoCard) card).botPlay(newState);
                }
                else{
                    card.playCard(newState);
                }

                newState.getCurrentPlayer().discardCard(i);
                if(newState.getCurrentPlayer().getNumCards() == 0){
                    if(newState.getCurrentPlayerIdx() == botIdx){
                        eval[0] = 900000;
                    }
                    else{
                        eval[0] = -900000;
                    }
                    eval[1] = i;
                    return eval;
                }

                // Increment next player
                nextPlayerIndex = (newState.getCurrentPlayerIdx() + newState.getDirection() + players.size()) % players.size();
                newState.setCurrentPlayerIdx(nextPlayerIndex);

                int tempEval[] = play(newState, depth - 1, botIdx, originalDepth, alpha, beta);
                if(card instanceof WildCard){
                    ((WildCard)card).setColour(Card.Colour.WILD);
                }
                if(card instanceof WildDrawTwoCard){
                    ((WildDrawTwoCard)card).setColour(Card.Colour.WILD);
                }

                if(depth == originalDepth) System.out.println("Considering " + i + " for a score of: " + tempEval[0]);
                if((currentPlayerIdx >= botIdx) && (tempEval[0] >= eval[0])){
                    eval[0] = tempEval[0];
                    eval[1] = i;
                    alpha = Integer.max(eval[0], alpha);
                }
                else if(currentPlayerIdx < botIdx && (tempEval[0] <= eval[0])){
                    eval[0] = tempEval[0];
                    eval[1] = i;
                    beta = Integer.min(eval[0], beta);
                }
                if(beta < alpha){
                    break;
                }
            }
        }

        if(depth == originalDepth) System.out.println("Chose: " + eval[1]);

        return eval;
    }
}