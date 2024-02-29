import javax.swing.*;

public class WildCard extends Card {
    WildCard(){
        this.colour = Colour.WILD.ordinal();
        this.value = Value.WILD.ordinal();
    }

     /**
     * Sets current colour
     * @param colour
     */
    public void setColour(Colour colour){
        this.colour = colour.ordinal();
    }

    /**
     * Plays wildcard
     * @param state
     */
    public void playCard(RoundState state){
        String input = chooseColour(state.isDark());
        Colour newColour = Colour.valueOf(input);
        newColour = Colour.values()[newColour.ordinal() - state.isDark()];
        this.setColour(newColour);
        state.setGameCard(this);
    }

    /**
     * Makes bot play card
     * @param state
     */
    public void botPlay(RoundState state){
        this.setColour(mostProminentColour(state));
        state.setGameCard(this);
    }

    /**
     * Finds the most prominent colour for the bot to use
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
     * Get score
     * @param dark
     * @return score in dark
     */
    public int getScore(int dark) {return 40;}

    /**
     * Get name for dark wildcard
     * @param dark
     * @return name
     */
    @Override
    public String getName(int dark){
        if(dark == 1){
            return "DARK_WILDCARD";
        }
        return "WILDCARD";
    }

    /**
     * Choose dark wildcard colour
     * @param dark
     * @return colour string colour
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
