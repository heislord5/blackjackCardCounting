/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class Hand {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Hand.class.getName());

    /**
     * @return the globalHandCount
     */
    private static int getGlobalHandCount() {
        return globalHandCount;
    }

    /**
     * @param aGlobalHandCount the globalHandCount to set
     */
    private static void setGlobalHandCount(int aGlobalHandCount) {
        globalHandCount = aGlobalHandCount;
    }

    /**
     * @return the handCount
     */
    public int getHandIdentifier() {
        return handIdentifier;
    }

    protected final int rootHandNumber;
    protected final HandHolder handHolder;
    private final Seat seat;
    private final List<ShoeCard> cardsInHand;
    protected final int splitCount;
    private float currentBet;
    private boolean delt = false;
    private int handIdentifier;
    private static int globalHandCount = 0;
    
    public Hand(int rootHandNumber, HandHolder handHolder, Seat seat, int splitCount) {
        this.rootHandNumber = rootHandNumber;
        this.handHolder = handHolder;
        this.cardsInHand = new ArrayList<>();
        this.splitCount = splitCount;
        this.seat = seat;
        this.handIdentifier = ++Hand.globalHandCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Hand::: ");
        sb.append("current Bet:").append(currentBet).append("RootHandNumber=").append(getRootHandNumber()).
                append(":handHolder=").append(getHandHolder().name).append(":cardsInHand=").
                append(getCardsInHand()).append(":splitCount=").append(getSplitCount());
        return sb.toString();
    }
    
        public String toShortString() {
        StringJoiner sj = new StringJoiner(",", "", "");
        this.getCardsInHand().stream().forEach((card) -> {
            sj.add(card.getCard().getStringRepresentation());
        });
        return sj.toString();
    }

    /**
     * @return the seat
     */
    public Seat getSeat() {
        return seat;
    }

    /**
     * @return the cardsInHand
     */
    public List<ShoeCard> getCardsInHand() {
        return cardsInHand;
    }

    /**
     * @return the currentBet
     */
    public float getCurrentBet() {
        return currentBet;
    }

    /**
     * @param currentBet the currentBet to set
     */
    public void setCurrentBet(float currentBet) {
        this.currentBet = currentBet;
    }    

    /**
     * @return the delt
     */
    public boolean isDelt() {
        return delt;
    }

    /**
     * @param delt the delt to set
     */
    public void setDelt(boolean delt) {
        this.delt = delt;
    }

    /**
     * @return the rootHandNumber
     */
    public int getRootHandNumber() {
        return rootHandNumber;
    }

    /**
     * @return the handHolder
     */
    public HandHolder getHandHolder() {
        return handHolder;
    }

    /**
     * @return the splitCount
     */
    public int getSplitCount() {
        return splitCount;
    }
    
    int minimumValue() {
        int returnVal = 0;
        returnVal = getCardsInHand().stream().map((card) -> card.getCard().getAlternateValue()).reduce(returnVal, Integer::sum);
        return returnVal;
    }
    
    int value() {
        return hasAces() && minimumValue() < 12 ? minimumValue()+10 : minimumValue();
    }
    
    boolean hasAces() {
        boolean returnVal = false;
        for (ShoeCard card : getCardsInHand()) {
            if (card.getCard() == CardEnum.ACE)
            {
                returnVal = true;
            }
        }
        return returnVal;
    }

    HandTypeEnum GetHandType(Table table) {
        List<ShoeCard> cards = this.getCardsInHand();
        HandTypeEnum returnVal = HandTypeEnum.HARD;
        if(cards.size() == 2 
                && cards.get(0).getCard().getPrimaryValue() == cards.get(1).getCard().getPrimaryValue() 
                && this.getSplitCount() < table.getNumberOfSplitsAllowed())
        {
            returnVal = HandTypeEnum.SPLITTABLE;
        } else if (minimumValue() < 12 && hasAces()) {
            returnVal = HandTypeEnum.SOFT;
        }
        return returnVal;
    }

    boolean isBlackJack() {
        return (this.getSplitCount() == 0 && this.getCardsInHand().size() == 2 && this.value() == 21);
    }

    boolean isBusted() {
        return this.minimumValue() > 21;
    }
}
