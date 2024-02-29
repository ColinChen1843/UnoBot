import javax.swing.*;

public class WildDrawTwoCard extends Card {
    WildDrawTwoCard(){
        this.colour = Colour.WILD.ordinal();
        this.value = Value.WILD.ordinal();
    }

    /**
     * Set card colour
     * @param colour
     */
    public void setColour(Colour colour){
        this.colour = colour.ordinal();
    }

    /**
     * Plays card
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
     * Plays light card
     * @param state
     */
    private void playLight(RoundState state) {
        String input = chooseColour(state.isDark());
        Colour newColour = Colour.valueOf(input);
        this.setColour(newColour);
        Card prevCard = state.getGameCard();
        state.setGameCard(this);
        Player currentPlayer = state.getCurrentPlayer();

        // Show the challenge dialog
        int response = JOptionPane.NO_OPTION;
        if(!(state.getNextPlayer() instanceof Bot)) {
            response = JOptionPane.showConfirmDialog(null,
                    "Do you want to challenge the Wild Draw Two?",
                    "Challenge",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        }

        if (response == JOptionPane.YES_OPTION) {
            if(currentPlayer.hasMatchingColour(prevCard.getColour())){
                for(int i = 0; i < 2; i++){
                    Card card = state.drawCard();
                    state.getCurrentPlayer().addCard(card);
                }
                JOptionPane.showMessageDialog(null,
                        "Challenge Success! " + state.getCurrentPlayer().getName() + " drew 2 cards",
                        "Challenge Success!", JOptionPane.INFORMATION_MESSAGE);
                state.setTurnStatus(0);
            }
            else{
                for(int i = 0; i < 4; i++){
                    Card card = state.drawCard();
                    state.getNextPlayer().addCard(card);
                }
                JOptionPane.showMessageDialog(null,
                        "Challenge Failed! " + state.getNextPlayer().getName() + " drew 4 cards",
                        "Challenge Failed!", JOptionPane.INFORMATION_MESSAGE);
                state.setTurnStatus(4);
            }
        }
        else{
            for(int i = 0; i < 2; i++){
                Card card = state.drawCard();
                state.getNextPlayer().addCard(card);
            }
            state.setTurnStatus(2);
        }
    }

    /**
     * Plays dark card
     * @param state
     */
    private void playDark(RoundState state) {
        String input = chooseColour(state.isDark());
        Colour newColour = Colour.valueOf(input);
        newColour = Colour.values()[newColour.ordinal() - 1];
        this.setColour(newColour);
        Card prevCard = state.getGameCard();
        state.setGameCard(this);
        Player currentPlayer = state.getCurrentPlayer();

        // Show the challenge dialog
        int response = JOptionPane.NO_OPTION;
        if(!(state.getNextPlayer() instanceof Bot)) {
            response = JOptionPane.showConfirmDialog(null,
                    "Do you want to challenge the Wild Draw Colour?",
                    "Challenge",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        }

        if (response == JOptionPane.YES_OPTION) {
            if(currentPlayer.hasMatchingColour(prevCard.getColour())){
                int drawCount = 1;
                Card card = state.drawCard();
                state.getCurrentPlayer().addCard(card);
                while(card.getColour() != this.getColour()){
                    card = state.drawCard();
                    state.getCurrentPlayer().addCard(card);
                    drawCount++;
                }

                JOptionPane.showMessageDialog(null,
                        "Challenge Success! " + state.getCurrentPlayer().getName() + " drew " + drawCount + " cards",
                        "Challenge Success!", JOptionPane.INFORMATION_MESSAGE);
                state.setTurnStatus(0);
            }
            else{
                int drawCount = 1;
                Card card = state.drawCard();
                state.getNextPlayer().addCard(card);
                while(card.getColour()!=this.getColour()){
                    card = state.drawCard();
                    state.getNextPlayer().addCard(card);
                    drawCount++;
                }
                // Draw two extra cards
                card = state.drawCard();
                state.getNextPlayer().addCard(card);
                card = state.drawCard();
                state.getNextPlayer().addCard(card);
                state.setTurnStatus(drawCount + 2);
                JOptionPane.showMessageDialog(null,
                        "Challenge Failed! " + state.getNextPlayer().getName() + " drew " + drawCount + " cards",
                        "Challenge Failed!", JOptionPane.INFORMATION_MESSAGE);
                state.setTurnStatus(drawCount);
            }
        }
        else{
            int drawCount = 1;
            Card card = state.drawCard();
            state.getNextPlayer().addCard(card);
            while(card.getColour()!=this.getColour()){
                card = state.drawCard();
                state.getNextPlayer().addCard(card);
                drawCount++;
            }
            state.setTurnStatus(drawCount);
        }
    }

    /**
     * Plays card for the bot
     * @param state
     */
    public void botPlay(RoundState state){
        if(state.isDark() == 0){
            this.setColour(mostProminentColour(state));
            state.setGameCard(this);
            for(int i = 0; i < 2; i++){
                Card card = state.drawCard();
                state.getNextPlayer().addCard(card);
            }
            state.setTurnStatus(2);
        }
        else{
            boolean colourCount[] = {false, false, false, false, true};
            int count = 0, drawCount = 1;
            Colour newColour = mostProminentColour(state);
            Card card = state.drawCard();
            state.getNextPlayer().addCard(card);
            while(card.getColour() != newColour){
                drawCount++;
                card = state.drawCard();
                state.getNextPlayer().addCard(card);
            }
            this.setColour(newColour);
            state.setGameCard(this);
            state.setTurnStatus(drawCount);
        }
    }

    /**
     * Finds most prominent colour
     * @param state
     * @return most prominent colour
     */
    private Card.Colour mostProminentColour(RoundState state){
        int count[] = {0, 0, 0, 0, 0};
        for(Card card : state.getCurrentPlayer().getHand()){
            count[card.getColour().ordinal()/2]++;
        }

        int maxIdx = 0;
        for(int i = 1; i < 4; i++){
            if(count[i] > count[maxIdx]){
                maxIdx = i;
            }
        }

        return Card.Colour.values()[maxIdx * 2];
    }

    /**
     * Returns dark score
     * @param dark
     * @return int score
     */
    public int getScore(int dark) {return 50 + (10 * dark);}
    @Override
    public String getName(int dark){
        if(dark == 1){
            return "WILDCARD_DRAW_COLOUR";
        }
        return "WILDCARD_DRAW_TWO";
    }

    /**
     * Chooses colour
     * @param dark
     * @return colour String colour chosen
     */
    public String chooseColour(int dark){

        String[] colours = {"RED", "YELLOW", "GREEN", "BLUE"};
        if(dark==1){
            colours = new String[]{"PURPLE", "ORANGE", "PINK", "TEAL"};
        }
        String colour = (String) JOptionPane.showInputDialog(
                null,
                "Choose colour:",
                "Colour",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                colours,
                colours[0]

        );
        return colour;
    }
}
