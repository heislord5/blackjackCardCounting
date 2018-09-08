/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class HandHolder {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(HandHolder.class.getName());

    private List<Hand> hands = new ArrayList<>();
    protected final String name;
    private boolean isDealer = false;
    PlayingStrategy playingStrategy;

    HandHolder(String name, PlayingStrategy playingStrategy) {
        this.name = name;
        this.playingStrategy = playingStrategy;
    }

    /**
     * @return the hands
     */
    public List<Hand> getHands() {
        if (hands == null) {
            hands = new ArrayList<>();
        }
        return hands;
    }

    /**
     * @param hands the hands to set
     */
    public void setHands(List<Hand> hands) {
        this.hands = hands;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("HandHolder::: ");
        sb.append("name=").append(this.name).append(":Hands=");
        this.getHands().stream().forEach((h) -> {
            sb.append(h).append(",");
        });
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public boolean isActiveThisDeal() {
        return true;
    }

    /**
     * @return the isDealer
     */
    public boolean isDealer() {
        return isDealer;
    }

    /**
     * @return the isDealer
     */
    void setIsDealer(boolean isDealer) {
        this.isDealer = isDealer;
    }

    void PlayHand(Hand hand, Table table) {

        Player thePlayer = null;
        if (this instanceof Player) {
            thePlayer = (Player) this;
        }

        while (!hand.isDelt()) {
            HandTypeEnum handType = hand.GetHandType(table);
            PlayEnum play = this.getPlayingStrategy().getTableHonoringPlay(hand, handType, table);
            if (play == PlayEnum.SPLIT) {
                SplitHand(hand, table);
                hand.setDelt(true);
            } else if (play == PlayEnum.HIT) {
                hand.getCardsInHand().add(table.getDealer().DealCard());
            } else if (play == PlayEnum.SURRENDER) {
                float surrender = hand.getCurrentBet() / 2;
                thePlayer.TransferCash(surrender);
                hand.setDelt(true);
            } else if (play == PlayEnum.STAND) {
                hand.setDelt(true);
            } else if (play == PlayEnum.DOUBLE) {
                thePlayer.Double(hand);
                hand.getCardsInHand().add(table.getDealer().DealCard());
                hand.setDelt(true);
            } else if (play == null) {
                throw new NullPointerException("Play did not receive a valid value.");
            } else {
                throw new RuntimeException("Valid play not matched");
            }
        }
    }

    private void SplitHand(Hand hand, Table table) {
        //Split Hand, inactive hand while creating two new hands to be processed.
        hand.setDelt(true);
        Hand newHand1 = new Hand(hand.getRootHandNumber(), hand.getHandHolder(), hand.getSeat(), hand.getSplitCount() + 1);
        newHand1.setCurrentBet(hand.getCurrentBet());
        Hand newHand2 = new Hand(hand.getRootHandNumber(), hand.getHandHolder(), hand.getSeat(), hand.getSplitCount() + 1);
        ((Player)this).TransferCash(hand.getCurrentBet()*-1);
        newHand2.setCurrentBet(hand.getCurrentBet());
        newHand1.getCardsInHand().add(hand.getCardsInHand().get(0));
        newHand2.getCardsInHand().add(hand.getCardsInHand().get(1));
        int index = hands.indexOf(hand);
        getHands().remove(index);
        getHands().add(index, newHand2);
        getHands().add(index, newHand1);
        newHand1.getCardsInHand().add(table.getDealer().DealCard());
        newHand2.getCardsInHand().add(table.getDealer().DealCard());
    }

    /**
     * @return the strategy
     */
    PlayingStrategy getPlayingStrategy() {
        return playingStrategy;
    }

    /**
     * @param playingStrategy
     */
    void setPlayingStrategy(PlayingStrategy playingStrategy) {
        this.playingStrategy = playingStrategy;
    }

}
