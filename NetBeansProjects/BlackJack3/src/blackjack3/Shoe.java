/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class Shoe {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Shoe.class.getName());

    private final List<ShoeCard> shoeCards = new ArrayList<>();
    private final float allowedPenetration;
    private int currentIndex;
    private float hiLoCount; //Raw before accounting for penetration.
    private float previousRealHiLoCount;
    private int shoeCount;

    public Shoe(int numberOfDecks, float percentPenetrationBeforeShuffle) {
        for (int decLooper = 0; decLooper < numberOfDecks; decLooper++) {
            shoeCards.addAll(new Deck().getCards());
        }
        allowedPenetration = percentPenetrationBeforeShuffle;
        previousRealHiLoCount = 0;
        shoeCount=0;
        Shuffle();
    }

    public final void Shuffle() {
        Collections.shuffle(getShoeCards(), new Random(System.nanoTime()));
        this.currentIndex = 0;
        this.hiLoCount = 0;
        this.shoeCount++;
        this.getShoeCards().stream().forEach((c) -> c.setUsed(Boolean.FALSE));
    }

    /**
     * @return the shoeCards
     */
    public List<ShoeCard> getShoeCards() {
        return shoeCards;
    }

    /**
     * @return the allowedPenetration
     */
    public float getAllowedPenetration() {
        return allowedPenetration;
    }

    /**
     * @return the currentIndex
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    public ShoeCard GetACard() {
        ShoeCard returnCard = this.getShoeCards().get(currentIndex);
        returnCard.setUsed(Boolean.TRUE);
        currentIndex++;
        if (returnCard.getCard().getPrimaryValue() >= 10) {
            this.hiLoCount -= 1;
        } else if (returnCard.getCard().getPrimaryValue() <= 6) {
            this.hiLoCount += 1;
        }
        getRealHiLoCount();
        return returnCard;
    }

    public boolean canStartNewHandWithCurrentShoe() {
        float value = (float) currentIndex - 1;
        float shoeSize = this.getShoeCards().size();
        return (value / shoeSize <= allowedPenetration);
    }

    /**
     * @return the hiLoCount
     */
    public float getHiLoCount() {
        return hiLoCount;
    }

    /**
     * @return the RealHiLoCount
     *
     * Accounts for the number of decks remaining in the shoe.
     */
    public float getRealHiLoCount() {
        float numUnusedCards;
        numUnusedCards = this.shoeCards.stream().filter(c -> c.getUsed() == false).count();
        return hiLoCount / (numUnusedCards / Deck.getSize());
    }

    boolean hasExceededAllowedPenetration() {
        float shoeSize = this.getShoeCards().size();
        float percentUtilization = this.getCurrentIndex() / shoeSize;
        return percentUtilization >= getAllowedPenetration();
    }
    
    /**
     * @return the previousRealHiLoCount
     */
    public float getPreviousRealHiLoCount() {
        return previousRealHiLoCount;
    }

    /**
     * @param previousRealHiLoCount the previousRealHiLoCount to set
     */
    public void setPreviousRealHiLoCount() {
        this.previousRealHiLoCount = getRealHiLoCount();
    }

    /**
     * @return the shoeCount
     */
    public int getShoeCount() {
        return shoeCount;
    }
}
