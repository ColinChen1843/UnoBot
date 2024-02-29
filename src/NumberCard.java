public class NumberCard extends Card{
    public NumberCard(int colour, int value){
        this.colour = colour;
        this.value = value;
    }

    /**
     * Play number card
     * @param state
     */
    public void playCard(RoundState state){ state.setGameCard(this); }

    /**
     * Get score
     * @param dark
     * @return int score
     */
    public int getScore(int dark) {return value + 1;}
}