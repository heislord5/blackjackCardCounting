/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class Player extends HandHolder {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Player.class.getName());

    private float bankRoll;
    private final BettingStrategy bettingStrategy;
    private boolean isActiveThisDeal;

    /**
     *
     * @param name
     * @param strategy
     * @param PlayingStratLocation
     * @param bankRoll
     */
    public Player(String name, BettingStrategy strategy, String PlayingStratLocation, float bankRoll) {
        super(name, new PlayingStrategy(PlayingStratLocation));
        this.bettingStrategy = strategy;
        this.bankRoll = bankRoll;
    }

    /**
     * @return the bankRoll
     */
    public float getBankRoll() {
        return bankRoll;
    }

    void BetHand(Hand hand) {
        float bet = bettingStrategy.getBetAmount(hand.getSeat().getTable());
        if (bet == 0 || getBankRoll() < hand.getSeat().getTable().getMinimumBet()) {
            //Leave Table until shoe is re-shuffled.
            hand.getSeat().getTable().getWaitingPlayers().add(this);
            hand.getSeat().setHandHolder(null);
            this.setHands(null);
        } else if (this.bankRoll >= bet) {
            this.bankRoll -= bet;
            hand.setCurrentBet(bet);
        } else {
            hand.setCurrentBet(this.bankRoll);
            this.bankRoll = 0;
        }
    }

    public void TransferCash(float cash) {
        bankRoll += cash;
    }

    @Override
    public boolean isActiveThisDeal() {
        return isActiveThisDeal;
    }

    /**
     * @param isActiveThisDeal the isActiveThisDeal to set
     */
    public void setIsActiveThisDeal(boolean isActiveThisDeal) {
        this.isActiveThisDeal = isActiveThisDeal;
    }

    void Double(Hand hand) {
        final float incomingBetLevel = hand.getCurrentBet();
        bankRoll -= incomingBetLevel;
        hand.setCurrentBet(incomingBetLevel * 2);
    }

}
