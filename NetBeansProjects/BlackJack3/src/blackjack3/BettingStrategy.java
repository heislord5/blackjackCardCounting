/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

/**
 *
 * @author Brad
 */
public class BettingStrategy {

    private final float walkAwayCount;
    private final float minScalingCount;
    private final float maxScalingCount;
    private final float maxScalingSpread;
    private final float maxSpreadNonScaling;

    public BettingStrategy(float walkAwayCount, float minScalingCount, float maxScalingCount, float maxScalingSpread, float maxSpreadNonScaling) {
        this.walkAwayCount = walkAwayCount;
        this.minScalingCount = minScalingCount;
        this.maxScalingCount = maxScalingCount;
        this.maxScalingSpread = maxScalingSpread;
        this.maxSpreadNonScaling = maxSpreadNonScaling;
    }

    /**
     * @return the walkAwayCount
     */
    public float getWalkAwayCount() {
        return walkAwayCount;
    }

    /**
     * @return the minScalingCount
     */
    public float getMinScalingCount() {
        return minScalingCount;
    }

    /**
     * @return the maxScalingCount
     */
    public float getMaxScalingCount() {
        return maxScalingCount;
    }

    /**
     * @return the maxScalingSpread
     */
    public float getMaxScalingSpread() {
        return maxScalingSpread;
    }

    /**
     * @return the maxSpreadNonScaling
     */
    public float getMaxSpreadNonScaling() {
        return maxSpreadNonScaling;
    }

    public float getBetAmount(Table table) {
        float count = table.getShoe().getRealHiLoCount();
        float bet;
        if (count < getWalkAwayCount()) {
            bet = 0;
        } else if (count < getMinScalingCount()) {
            bet = table.getMinimumBet();
        } else if (count < getMaxScalingCount()) {
            bet = table.getMinimumBet() + (getMaxScalingSpread() * table.getMinimumBet() - table.getMinimumBet()) / (getMaxScalingCount() - getMinScalingCount());
        } else {
            bet = table.getMinimumBet() * getMaxSpreadNonScaling();
        }
        return bet;
    }
}
