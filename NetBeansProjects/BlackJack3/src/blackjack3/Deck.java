/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class Deck {

    private List<ShoeCard> cards = new ArrayList<>();

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Deck.class.getName());

    public Deck() {
        for (CardEnum card : CardEnum.values()) {
            cards.add(new ShoeCard(card));
            cards.add(new ShoeCard(card));
            cards.add(new ShoeCard(card));
            cards.add(new ShoeCard(card));
        }
        cards = Collections.unmodifiableList(cards);
    }

    /**
     * @return the size
     */
    public static int getSize() {
        return 52;
    }

    /**
     * @return the cards
     */
    public List<ShoeCard> getCards() {
        return cards;
    }
}
