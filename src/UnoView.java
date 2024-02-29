import javax.swing.*;
import java.awt.*;
import java.util.*;

public class UnoView extends JFrame {

    UnoModel model;
    private JPanel pGameBoard, pGameBoard_North, pGameBoard_South, pStatus, pPlayerHand, pDrawCard, pCardsContainer;

    private JLabel lCurrentPlayer, lTopCard, lTopCardName, lTopCardIcon, lStatus;
    private JButton bDrawCard, bNextPlayer, bCard, bReset;
    private JScrollPane spPlayerHand;
    private int numPlayers, numAI;
    private ArrayList<String> names, AINames;
    private UnoController unoController;

    public UnoView(){
        super("Uno");

        numPlayers = UnoViewNumPlayers();
        numAI = UnoViewNumAI(numPlayers);
        names = setNames(numPlayers);
        AINames = setAINames(numAI);
        model = new UnoModel(numPlayers, names, numAI, AINames);
        model.addUnoView(this);
        unoController = new UnoController(model);

        // Frame window
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource
        ("/icons/unoIcon.png")));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // panelP gameboard
        pGameBoard = new JPanel(new BorderLayout());
        pGameBoard.setPreferredSize(new Dimension(100,215));
        //pGameBoard.setBackground(Color.LIGHT_GRAY);
        this.add(pGameBoard,BorderLayout.NORTH);

        //panel pGameBoard_North setup
        pGameBoard_North = new JPanel();
        pGameBoard.add(pGameBoard_North,BorderLayout.NORTH);
        //--panel pGameBoard_North setup

        //panel pGameBoard_South setup
        pGameBoard_South = new JPanel(new BorderLayout());
        pGameBoard_South.setBorder(BorderFactory.createLineBorder(Color.black,2));
        pGameBoard.add(pGameBoard_South,BorderLayout.SOUTH);
        //--panel pGameBoard_South setup

        //panel pStatus setup
        pStatus = new JPanel(new GridLayout(3, 0));
        pStatus.setPreferredSize(new Dimension(100,100));
        pStatus.setBorder(BorderFactory.createLineBorder(Color.black,2));
        this.add(pStatus,BorderLayout.WEST);
        //--panel pStatus setup

        //panel pPlayerHand setup
        pPlayerHand = new JPanel(new BorderLayout());
        this.add(pPlayerHand,BorderLayout.CENTER);
        //--panel pPlayerHand setup

        //panel pDrawCard setup
        pDrawCard = new JPanel(new BorderLayout());
        pDrawCard.setPreferredSize(new Dimension(100,100));
        this.add(pDrawCard,BorderLayout.EAST);
        //--panel pDrawCard setup

        //label lCurrentPlayer setup
        lCurrentPlayer = new JLabel(names.get(0));
        pGameBoard_North.add(lCurrentPlayer,BorderLayout.CENTER);
        //--label lCurrentPlayer setup

        //label lTopCard setup
        lTopCard = new JLabel("Top Card");
        pGameBoard_South.add(lTopCard,BorderLayout.NORTH,SwingConstants.CENTER);
        //--label lTopCard setup

        //label lTopCardName setup
        lTopCardName = new JLabel("TopCardName", SwingConstants.CENTER);
        pGameBoard_South.add(lTopCardName,BorderLayout.CENTER);
        //--label lTopCardName setup

        //label lTopCardIcon setup
        lTopCardIcon = new JLabel("", SwingConstants.CENTER);
        pGameBoard_South.add(lTopCardIcon,BorderLayout.SOUTH);
        //--label lTopCardName setup

        //label lStatus setup
        lStatus = new JLabel("Game Status");
        pStatus.add(lStatus,BorderLayout.NORTH);
        //--label lStatus setup

        bDrawCard = new JButton("Draw Card");
        bDrawCard.addActionListener(unoController);
        bDrawCard.setActionCommand("draw");
        pDrawCard.add(bDrawCard,BorderLayout.CENTER);

        //button bReset setup
        bReset = new JButton("Reset");
        bReset.setEnabled(true);
        bReset.addActionListener(unoController);
        bReset.setActionCommand("reset");
        pStatus.add(bReset);
        //--button bReset setup

        //button bNextPlayer setup
        bNextPlayer = new JButton("Next Player");
        bNextPlayer.setEnabled(false);
        bNextPlayer.addActionListener(unoController);
        bNextPlayer.setActionCommand("next");
        pStatus.add(bNextPlayer);
        //--button bNextPlayer setup

        pCardsContainer = new JPanel(new FlowLayout());
        //--panel pCardsContainer setup
        //create a JScrollPane for contain card buttons
        spPlayerHand = new JScrollPane(pCardsContainer);
        pPlayerHand.add(spPlayerHand,BorderLayout.CENTER);
        spPlayerHand.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        this.setVisible(true);
        unoController.reset(0);
    }

    /**
     * Handles updates that occur in the game
     * @param e
     */
    public void handleUpdate(UnoEvent e){
        RoundState roundState = e.getRoundState();
        ArrayList<Player> players = e.getPlayersList();
        Player currentPlayer = players.get(roundState.getCurrentPlayerIdx());
        int dark = roundState.isDark();

        // Update current player display
        lCurrentPlayer.setText(currentPlayer.getName());

        // Update player cards display
        pCardsContainer.removeAll();
        for (int i = 0; i < currentPlayer.getNumCards(); i++) {
            Card card = currentPlayer.getCard(i);
            bCard = new JButton(card.getName(dark));

            // Set card icon
            String filepath = "/images/" + card.getName(dark) + ".png";
            if (getClass().getResource(filepath) != null) {
                ImageIcon cardIcon = new ImageIcon(getClass().getResource(filepath));
                Image cardImage = cardIcon.getImage();
                cardImage = cardImage.getScaledInstance(200 * 168/227, 200, java.awt.Image.SCALE_SMOOTH);
                cardIcon = new ImageIcon(cardImage);
                bCard.setIcon(cardIcon);
            }
            bCard.setVerticalTextPosition(SwingConstants.TOP);
            bCard.setHorizontalTextPosition(SwingConstants.CENTER);

            bCard.setOpaque(false);
            bCard.setContentAreaFilled(false);
            bCard.setBorderPainted(false);
            bCard.addActionListener(unoController);
            bCard.setActionCommand("" + i);
            bCard.setEnabled(e.getPlayable());
            pCardsContainer.add(bCard);
        }
        pCardsContainer.revalidate();
        pCardsContainer.repaint();

        // Update top card display
        Card topCard = roundState.getGameCard();
        lTopCardName.setText(topCard.getName(dark));
        String filepath = "/images/" + topCard.getName(dark) + ".png";
        ImageIcon cardIcon = new ImageIcon(getClass().getResource(filepath));
        Image cardImage = cardIcon.getImage();
        cardImage = cardImage.getScaledInstance(150 * 168/227, 150, java.awt.Image.SCALE_SMOOTH);
        cardIcon = new ImageIcon(cardImage);
        lTopCardIcon.setIcon(cardIcon);
        pGameBoard_South.add(lTopCardName,BorderLayout.CENTER);

        // Update draw card button
        bDrawCard.setEnabled(e.getPlayable());

        // Update next player button
        bNextPlayer.setEnabled(!e.getPlayable() && !e.getWin());

        // Update Status
        lTopCard.setText("Current Colour: " + topCard.getColour(dark));
        if(e.getInvalidCard()){
            lStatus.setText("Invalid Card\n");
        }
        else if(e.getWin()){
            lStatus.setText(currentPlayer.getName() + " Wins\n");
            int score = e.getRoundState().getScore();
            roundOverDialog(currentPlayer, score);
            unoController.reset(score);
        }
        else if(e.getTurnStatus() == -5){
            lStatus.setText("<html>Bot Played:<br/>" + roundState.getGameCard().getName(dark) + "</html>");
        }
        else if(e.getTurnStatus() < 0){
            lStatus.setText("Turn Skipped\n");
        }
        else if(e.getTurnStatus() > 0){
            lStatus.setText("Drew " + e.getTurnStatus() + " Cards\n");
        }
        else{
            lStatus.setText("Game Status");
        }
    }

    /**
     * Sets up number of players
     * @return playerCount number of players
     */
    public int UnoViewNumPlayers(){
        Integer[] playerNumber = {1, 2, 3, 4};
        ImageIcon playerSelectIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/playerSelectIcon.png"))).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        Integer playerCount = (Integer) JOptionPane.showInputDialog(
                null,
                "Choose number of players:",
                "Player Count",
                JOptionPane.INFORMATION_MESSAGE,
                playerSelectIcon,
                playerNumber,
                playerNumber[0]

        );
        return playerCount;
    }

    /**
     * Sets up number of AIs, based on how many players are available
     * @param players
     * @return num of AIs
     */
    public int UnoViewNumAI(int players){

        if(players == 4) return 0;
        Integer[] AINumber = {0, 1};
        if (players == 2){
            AINumber = new Integer[]{0, 1, 2};
        }
        if(players == 1){
            AINumber = new Integer[]{1, 2, 3};
        }

        ImageIcon playerSelectIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/playerSelectIcon.png"))).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        Integer AICount = (Integer) JOptionPane.showInputDialog(
                null,
                "Choose number of Bots:",
                "Bot Count",
                JOptionPane.INFORMATION_MESSAGE,
                playerSelectIcon,
                AINumber,
                AINumber[0]

        );
        return AICount;
    }

    /**
     * Sets names of the players
     * @param numPlayers
     * @return names ArrayList<String> of names
     */
    public ArrayList<String> setNames(int numPlayers){
        ArrayList<String> names = new ArrayList<String>();
        ImageIcon playerNameIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/icons/nameIcon.png"))).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        for(int i = 1; i <= numPlayers; i++){
            String playerName = (String) JOptionPane.showInputDialog(
                    null,
                    "Enter name for Player " + i + " of " + numPlayers + " players:",
                    "Player Name",
                    JOptionPane.PLAIN_MESSAGE,
                    playerNameIcon,
                    null,
                    ""
            );
            if(playerName == null || playerName.equals("")){
                playerName = "Player " + i;
            }
            names.add(playerName);
        }
        return names;
    }
    public ArrayList<String> setAINames(int numAI){
        ArrayList<String> names = new ArrayList<String>();
        for(int i = 0; i < numAI; i++){
            String AIName = "Bot " + (i + 1);
            names.add(AIName);
        }
        return names;
    }

    /**
     * Outputs who wins
     * @param winner
     * @param score
     */
    public void roundOverDialog(Player winner, int score){
        JOptionPane.showMessageDialog(this, winner.getName() + " wins!\n Scored " + score + " points");
    }
    public static void main(String[] args) {
        new UnoView();
    }
}