public class SkipCard extends Card {
    SkipCard(int colour){
        this.colour = colour;
        this.value = Value.SKIP.ordinal();
    }

    /**
     * Play card given current round state
     * @param state
     */
    public void playCard(RoundState state){
        if(state.isDark() == 1){
            this.playDark(state);
        }
        else{
            this.playLight(state);
        }
    }

    /**
     * Play light card given round state
     * @param state
     */
    private void playLight(RoundState state){
        state.setGameCard(this);
        state.setTurnStatus(-1);
    }

    /**
     * Play dark card based on current state
     * @param state
     */
    private void playDark(RoundState state){
        state.setGameCard(this);
        state.setTurnStatus(-1 * (state.getPlayers().size() - 1));
    }

    /**
     * Get card score
     * @param dark
     * @return
     */
    public int getScore(int dark) {return 20 + (10 * dark);}
}
