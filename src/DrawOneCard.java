public class DrawOneCard extends Card {
    DrawOneCard(int colour){
        this.colour = colour;
        this.value = Value.DRAW_ONE.ordinal();
    }

    /**
     * Play draw one card
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
     * Play light draw one card
     * @param state
     */
    public void playLight(RoundState state){
        state.setGameCard(this);
        Player nextPlayer = state.getNextPlayer();
        Card card = state.drawCard();
        nextPlayer.addCard(card);
        state.setTurnStatus(1);
    }

    /**
     * Play draw one dark card
     * @param state
     */
    public void playDark(RoundState state){
        state.setGameCard(this);
        Player nextPlayer = state.getNextPlayer();
        for (int i =0;i<5;i++){
            Card card = state.drawCard();
            nextPlayer.addCard(card);

        }
        state.setTurnStatus(5);
    }

    /**
     * Get score
     * @param dark
     * @return int score
     */
    public int getScore(int dark) {return 10 + (10 * dark);}
}
