import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UnoController implements ActionListener {
    UnoModel model;
    public UnoController(UnoModel model){
        this.model = model;
    }

    /**
     * Handle the action performed by user
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String event = e.getActionCommand();
        if(event.equals("draw")){
            model.playDraw();
        }

        else if(event.equals("next")){
            model.playNext();
        }
        else if (event.equals("reset")){
            model.reset(0);
        }
        else{
            int cardIdx = Integer.parseInt(event);
            model.playCard(cardIdx);
        }
    }

    /**
     * Reset round
     * @param score
     */
    public void reset(int score){
        model.reset(score);
    }
}
