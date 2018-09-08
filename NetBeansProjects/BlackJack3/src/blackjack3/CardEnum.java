/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Brad
 */
public enum CardEnum {

    ACE("A", 11, 1),
    KING("K", 10),
    QUEEN("Q", 10),
    JACK("J", 10),
    TEN("10", 10),
    NINE("9", 9),
    EIGHT("8", 8),
    SEVEN("7", 7),
    SIX("6", 6),
    FIVE("5", 5),
    FOUR("4", 4),
    THREE("3", 3),
    TWO("2", 2);

    
    

    private List<Integer> values = new ArrayList<>();
    private final String stringRepresentation;
    private static final Map<String, CardEnum> lookupByString
            = new HashMap<>();
    private static final Map<Integer, CardEnum> lookupByValue
            = new HashMap<>();
    
    /**
     * @return the lookupByString
     */
    private static Map<String, CardEnum> getLookupByString() {
        return lookupByString;
    }

    /**
     * @return the lookupByValue
     */
    private static Map<Integer, CardEnum> getLookupByValue() {
        return lookupByValue;
    }

    static {
        EnumSet.allOf(CardEnum.class).stream().forEach((card) -> {
            getLookupByString().put(card.getStringRepresentation(), card);
        });

        EnumSet.allOf(CardEnum.class).stream().forEach((card) -> {
            getLookupByValue().put(card.getPrimaryValue(), card);
        });
        getLookupByValue().put(ACE.getAlternateValue(), ACE);
    }

    private CardEnum(String stringRepresentation, Integer... values) {
        this.stringRepresentation = stringRepresentation;
        this.values = Arrays.asList(values);
    }

    public List<Integer> getValues() {
        return values;
    }

    public int getPrimaryValue() {
        return getValues().get(0);
    }

    public boolean hasAlternateValue() {
        return getValues().size() == 2;
    }

    public int getAlternateValue() {
        if(hasAlternateValue()) {
        return getValues().get(1);
        } else {
            return getPrimaryValue();
        }
    }

    /**
     * @return the stringRepresentation
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }
    
    public static CardEnum get(String stringRepresentation) {
        return lookupByString.get(stringRepresentation);
    }
    
    /**
     *
     * @param value
     * @return
     */
    public static CardEnum get(Integer value) {
        return lookupByValue.get(value);
    }
}
