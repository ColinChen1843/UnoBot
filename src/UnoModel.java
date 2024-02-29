import java.util.*;

public class UnoModel {
    private ArrayList<UnoView> views;
    public int roundScore;
    private int playerNum, AINum, firstBotIdx;
    private RoundState roundState;

    public UnoModel(int playerNum, ArrayList<String> names, int AINum, ArrayList<String> AIName) {
        this.views = new ArrayList<UnoView>();
        this.AINum = AINum;
        this.playerNum = playerNum;
        this.firstBotIdx = playerNum;
        roundState = new RoundState(new ArrayList<Player>());
        addPlayers(names);
        addAIs(AIName);
        this.playerNum += AINum;
    }
    public void addUnoView(UnoView view){
        this.views.add(view);
    }
    public void removeUnoView(UnoView view){
        this.views.remove(view);
    }

    /**
     * Plays selected card
     * @param cardIdx ID of the card selected
     */
    public void playCard(int cardIdx) {
        Player currentPlayer = roundState.getPlayers().get(roundState.getCurrentPlayerIdx());
        Card card = currentPlayer.getCard(cardIdx);
        int turnStatus = 0;

        if(card.validateCard(roundState.getGameCard())){
            boolean playable = false;
            if(currentPlayer instanceof Bot){
                turnStatus = -5;
                if(card instanceof WildCard){
                    ((WildCard) card).botPlay(roundState);
                }
                else if(card instanceof WildDrawTwoCard){
                    ((WildDrawTwoCard) card).botPlay(roundState);
                }
                else{
                    card.playCard(roundState);
                }
            }
            else{
                card.playCard(roundState);
            }
            currentPlayer.discardCard(cardIdx);

            boolean win = (currentPlayer.getNumCards() == 0);

            for (UnoView view: views){
                view.handleUpdate(new UnoEvent(this, roundState, roundState.getPlayers(), playable, false, win, turnStatus));
            }
        }
        else{
            for (UnoView view: views){
                view.handleUpdate(new UnoEvent(this, roundState, roundState.getPlayers(), true, true, false, turnStatus));
            }
        }
    }

    /**
     * Draws card for current player
     */
    public void playDraw() {
        Player currentPlayer = roundState.getPlayers().get(roundState.getCurrentPlayerIdx());
        Card drawnCard = roundState.drawCard();
        currentPlayer.addCard(drawnCard);

        for (UnoView view: views){
            view.handleUpdate(new UnoEvent(this, roundState, roundState.getPlayers(), false, false, false, 1));
        }
    }

    /**
     * Sets up next play in the game
     */
    public void playNext() {
        boolean playable = true;
        int turnStatus = roundState.getTurnStatus();

        // Increment Player
        int nextPlayerIndex = (roundState.getCurrentPlayerIdx() + roundState.getDirection() + playerNum) % playerNum;
        roundState.setCurrentPlayerIdx(nextPlayerIndex);

        if(turnStatus > 0){
            playable = false;
            roundState.setTurnStatus(0);
        }
        else if(turnStatus < 0){
            playable = false;
            roundState.setTurnStatus(turnStatus + 1);
        }
        else if(roundState.getCurrentPlayer() instanceof Bot){
            System.out.println("NEW MOVE for " + roundState.getCurrentPlayer().getName());
            int totalCards = 0;
            for(Player player : roundState.getPlayers()){
                totalCards += player.getNumCards();
            }
            int depth = Integer.max(14 - (totalCards/10), 2);
            System.out.println("Depth: " + depth);
            int[] result = ((Bot)roundState.getCurrentPlayer()).play(roundState, depth, this.firstBotIdx, depth, -900001, 900001);
            int move = result[1];

            if(move == -1){
                playDraw();
            }
            else{
                playCard(move);
            }
            return;
        }

        for (UnoView view: views){
            view.handleUpdate(new UnoEvent(this, roundState, roundState.getPlayers(), playable, false, false, turnStatus));
        }
    }

    /**
     * Resets game
     * @param score
     */
    public void reset(int score){
        this.roundState.reset(score);
        dealCards();
        for (UnoView view: views){
            view.handleUpdate(new UnoEvent(this, roundState, roundState.getPlayers(), true, false, false, 0));
        }
    }

    /**
     * Adds players to the game
     * @param names names of players
     */
    private void addPlayers(ArrayList<String> names){
        for(int i = 0; i < playerNum; i++){
            Player player = new Player(names.get(i));
            roundState.getPlayers().add(player);
        }
    }

    /**
     * Adds AIs to the game to play
     * @param names names of AIs
     */
    private void addAIs(ArrayList<String> names){
        for(int i = 0; i < AINum; i++){
            Bot player = new Bot(names.get(i));
            roundState.getPlayers().add(player);
        }
    }

    /**
     * Deals cards to players in the game
     */
    private void dealCards(){
        for (Player p : roundState.getPlayers()) {
            for (int i = 1; i <= 7; i++) {
                p.addCard(roundState.drawCard());
            }
        }
    }
}
