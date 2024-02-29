import java.util.ArrayList;
import java.util.Collections;

public class RoundState {
    private int currentPlayerIdx;
    private int dark;
    private ArrayList<Player> players;
    private int direction;
    private ArrayList<Card> deck;
    private Card gameCard;
    private int turnStatus;

    public RoundState(ArrayList<Player> players){
        currentPlayerIdx = 0;
        dark = 0;
        direction = 1;
        turnStatus = 0;
        deck = new ArrayList<Card>();
        this.players = new ArrayList<Player>(players);
        buildDeck();
        gameCard = drawCard();
    }
    public RoundState(RoundState state){
        this.currentPlayerIdx = state.currentPlayerIdx;
        this.dark = state.dark;
        this.players = new ArrayList<Player>();
        // deep copy
        for(Player player : state.getPlayers()){
            this.players.add(new Player(player));
        }
        this.direction = state.direction;
        this.turnStatus = state.turnStatus;
        this.deck = new ArrayList<>(state.deck);
        this.gameCard = state.gameCard;
    }

    /**
     * Gets current player ID
     * @return playerID
     */
    public int getCurrentPlayerIdx(){ return currentPlayerIdx; }

    /**
     * Sets current player ID
     * @param currentPlayerIdx
     */
    public void setCurrentPlayerIdx(int currentPlayerIdx){ this.currentPlayerIdx = currentPlayerIdx; }

    /**
     * Return dark value
     * @return dark
     */
    public int isDark(){ return dark; }

    /**
     * Sets dark value
     * @param dark
     */
    public void setDark(int dark){ this.dark = dark; }

    /**
     * Gets current direction
     * @return direction
     */
    public int getDirection(){ return direction; }

    /**
     * Sets direction
     * @param direction
     */
    public void setDirection(int direction){ this.direction = direction; }

    /**
     * Gets turn status
     * @return turnStatus
     */
    public int getTurnStatus(){ return turnStatus; }

    /**
     * Sets turn status
     * @param turnStatus
     */
    public void setTurnStatus(int turnStatus){ this.turnStatus = turnStatus; }

    /**
     * Gets top card
     * @return Card top card
     */
    public Card getGameCard(){ return gameCard; }

    /**
     * Sets top card
     * @param newGameCard
     */
    public void setGameCard(Card newGameCard){ this.gameCard = newGameCard; }

    /**
     * Draws card from deck
     * @return Card from deck
     */
    public Card drawCard(){
        if(deck.size() == 0){
            buildDeck();
        }
        return deck.remove(0);
    }
    /**
     * Gets players
     * @return ArrayList<Player> players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }
    /**
     * Gets current player
     * @return Player current player
     */
    public Player getCurrentPlayer(){
        return players.get(currentPlayerIdx);
    }

    /**
     * Gets next player
     * @return Player next player
     */
    public Player getNextPlayer(){
        int nextPlayerIndex = (currentPlayerIdx + direction) % players.size();
        if (nextPlayerIndex < 0) {
            nextPlayerIndex += players.size();
        }
        return players.get(nextPlayerIndex);
    }

    /**
     * Gets score for current player
     * @return score
     */
    public int getScore(){
        int score = 0;
        for(Player player : this.getPlayers()){
            for(Card card : player.getHand()){
                score += card.getScore(this.isDark());
            }
        }
        return score;
    }

    /**
     * Resets the game and updates the current score
     * @param score
     */
    public void reset(int score){
        this.getCurrentPlayer().addScore(score);
        for(Player player : this.getPlayers()){
            player.getHand().clear();
        }
        currentPlayerIdx = 0;
        dark = 0;
        direction = 1;
        turnStatus = 0;
        buildDeck();
        gameCard = drawCard();
    }

    /**
     * Builds the deck for the entire game
     */
    private void buildDeck(){
        deck.clear();
        // generate number cards
        for(int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 9; k++) {
                    Card card = new NumberCard(i, k);
                    deck.add(card);
                }
            }
        }

        // generate draw one cards
        for(int i = 0; i < 8; i += 2){
            for(int j = 0; j < 2; j++){
                Card card = new DrawOneCard(i);
                deck.add(card);
            }
        }

        // generate reverse cards
        for(int i = 0; i < 8; i += 2){
            for(int j = 0; j < 2; j++){
                Card card = new ReverseCard(i);
                deck.add(card);
            }
        }

        // generate skip cards
        for(int i = 0; i < 8; i += 2){
            for(int j = 0; j < 2; j++){
                Card card = new SkipCard(i);
                deck.add(card);
            }
        }

        // generate flip cards
        for(int i = 0; i < 8; i += 2){
            for(int j = 0; j < 2; j++){
                Card card = new FlipCard(i);
                deck.add(card);
            }
        }

        //generate wild cards
        for(int i = 0; i < 4; i++){
            Card card = new WildCard();
            deck.add(card);
        }

        //generate wild draw two cards
        for(int i = 0; i < 4; i++){
            Card card = new WildDrawTwoCard();
            deck.add(card);
        }

        // shuffle deck
        Collections.shuffle(deck);
    }

}