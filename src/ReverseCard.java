public class ReverseCard extends Card {
    ReverseCard(int colour){
        this.colour = colour;
        this.value = Value.REVERSE.ordinal();
    }

    /**
     * Play card, given round state
     * @param state
     */
    public void playCard(RoundState state){
        int currentDirection = -1 * state.getDirection();
        state.setDirection(currentDirection);
        state.setGameCard(this);
    }

    /**
     * Get current score
     * @param dark
     * @return int score
     */
    public int getScore(int dark) {return 20;}
}
