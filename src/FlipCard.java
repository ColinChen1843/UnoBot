public class FlipCard extends Card{
    public FlipCard(int colour){
        this.colour = colour;
        this.value = Value.FLIP.ordinal();
    }

    /**
     * Play flip card
     * @param state
     */
    public void playCard(RoundState state){
        // 1 - dark is complement
        state.setDark(1 - state.isDark());
        state.setGameCard(this);
    }

    /**
     * Get score of card
     * @param dark
     * @return int score
     */
    public int getScore(int dark) {return 20;}
}