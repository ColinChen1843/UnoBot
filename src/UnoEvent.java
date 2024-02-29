import java.util.ArrayList;
import java.util.EventObject;

public class UnoEvent extends EventObject {
    private RoundState roundState;
    private ArrayList<Player> playersList;
    private boolean playable;
    private boolean invalidCard;
    private boolean win;
    private int turnStatus;

    public UnoEvent(UnoModel model, RoundState roundState, ArrayList<Player> playersList, boolean playable, boolean invalidCard, boolean win, int turnStatus){
        super(model);
        this.roundState = roundState;
        this.playersList = playersList;
        this.playable = playable;
        this.invalidCard = invalidCard;
        this.win = win;
        this.turnStatus = turnStatus;
    }

    /**
     * Get list of players
     * @return playersList list of players
     */
    public ArrayList<Player> getPlayersList(){
        return playersList;
    }

    /**
     * Gets round state
     * @return roundState current round state
     */
    public RoundState getRoundState() {
        return roundState;
    }

    /**
     * Returns if the player is allowed to play or not
     * @return true if playable
     */
    public boolean getPlayable(){
        return playable;
    }

    /**
     * Returns invalid card
     * @return true if invalid
     */
    public boolean getInvalidCard(){ return invalidCard; }

    /**
     * Returns if a player won
     * @return true if player won
     */
    public boolean getWin() { return win; }

    /**
     * Returns current turn status
     * @return int status
     */
    public int getTurnStatus() { return turnStatus; }

}
