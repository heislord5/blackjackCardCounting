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
public class ShoeCard {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(ShoeCard.class.getName());

    private final CardEnum card;
    private Boolean used;

    public ShoeCard(CardEnum card) {
        this.card = card;
        used = false;
    }

    /**
     * @return the card
     */
    public CardEnum getCard() {
        return card;
    }

    /**
     * @return the used
     */
    public Boolean getUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(Boolean used) {
        this.used = used;
    }

    @Override
    public String toString(){
        String sb = "ShoeCard:::" + this.card + "&used=" + this.used;
        return sb;
    }
}
