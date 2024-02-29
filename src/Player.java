import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int score;
    public Player(String name){
        this.name = name;
        hand = new ArrayList<>();
        score = 0;
    }
    public Player(Player player){
        this.name = player.name;
        this.hand = new ArrayList<Card>(player.hand);
        this.score = player.score;
    }

    /**
     * Get card with index
     * @param card index
     * @return Card card requested
     */
    public Card getCard(int card){
        return hand.get(card);
    }

    /**
     * Add card to hand
     * @param card
     */
    public void addCard(Card card){
        hand.add(card);
    }

    /**
     * Discard card given the card index
     * @param cardIdx int index
     */
    public void discardCard(int cardIdx){hand.remove(cardIdx);}

    /**
     * Get number of cards in hand
     * @return int number of cards held
     */
    public int getNumCards(){return hand.size();}

    /**
     * Get name of player
     * @return name String
     */
    public String getName(){
        return name;
    }

    /**
     * Set name of player
     * @param name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Get current score
     * @return int score
     */
    public int getScore(){ return score; }

    /**
     * Add score of player
     * @param score
     */
    public void addScore(int score){ this.score += score; }

    /**
     * Get current hand
     * @return ArrayList<Card> hand.
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Check if there is a matching colour in the player hand
     * @param colour
     * @return true if colour is found, else false
     */
    public boolean hasMatchingColour(Card.Colour colour) {
        for (Card card : this.getHand()) {
            if (card.getColour(0) == colour && card.getValue(0) != Card.Value.WILD) {
                return true;
            }
        }
        return false;
    }
}
