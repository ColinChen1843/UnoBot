public abstract class Card {
    public enum Colour {YELLOW, ORANGE, BLUE, PURPLE, RED, PINK, GREEN, TEAL, WILD, DARK_WILD}
    public enum Value {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, REVERSE, FLIP, WILD, DRAW_ONE, DRAW_FIVE, SKIP, SKIP_ALL}
    protected int colour;
    protected int value;

    /**
     * Get value of current dark card.
     * @param dark
     * @return value of dark card
     */
    public Value getValue(int dark){
        if(value >= 12){
            return Value.values()[value + dark];
        }
        return Value.values()[value];
    }

    /**
     * Gets current colour if dark.
     * @param dark
     * @return dark colour
     */
    public Colour getColour(int dark){
        return Colour.values()[colour + dark];
    }

    /**
     * Return the current colour.
     * @return card colour
     */
    public Colour getColour(){
        return Colour.values()[colour];
    }

    /**
     * This function validates whether a card can be played or not in the current sequence of the game.
     * @param gameCard
     * @return
     */
    public boolean validateCard(Card gameCard){
        if(this.getColour(0) == gameCard.getColour(0)){
            return true;
        }
        if(this.getValue(0) == gameCard.getValue(0)){
            return true;
        }
        if(this.getValue(0) == Value.WILD){
            return true;
        }
        if(gameCard.getColour(0) == Colour.WILD){
            return true;
        }
        return false;
    };

    /**
     * Get name of current player
     * @param dark
     * @return name of player
     */
    public String getName(int dark){ return getColour(dark) + " " + getValue(dark); }

    /**
     * Abstract class to play card.
     * @param state
     */
    public abstract void playCard(RoundState state);

    /**
     * Returns score of the game.
     * @param dark
     * @return current score
     */
    public abstract int getScore(int dark);
}
