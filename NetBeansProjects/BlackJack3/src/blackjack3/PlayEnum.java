package blackjack3;

import java.util.HashMap;
import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Brad
 */
public enum PlayEnum {

    HIT("H"),
    DOUBLE("D"),
    DOUBLE_OR_HIT("DH"),
    DOUBLE_OR_STAND("DS"),
    SPLIT("P"),
    SPLIT_IF_DOUBLE_AFTER_SPLIT_ALLOWED_OR_HIT("PH"),
    STAND("S"),
    SURRENDER_OR_HIT("RH"),
    SURRENDER_OR_STAND("RS"),
    SURRENDER("R"),
    SURRENDER_OR_SPLIT("RP");


    private final String stringRepresentation;
    private static final Map<String, PlayEnum> lookupByString
            = new HashMap<>();

    static {
        for (PlayEnum d : PlayEnum.values()) {
            lookupByString.put(d.getStringRepresentation(), d);
        }

    }

    /**
     * @return the lookupByString
     */
    private static Map<String, PlayEnum> getLookupByString() {
        return lookupByString;
    }

    private PlayEnum(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    /**
     * @return the stringRepresentation
     */
    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public static PlayEnum get(String stringRepresentation) {
        return lookupByString.get(stringRepresentation);
    }
}
