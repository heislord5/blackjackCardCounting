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
public class Seat {
    
    private HandHolder handHolder;
    private final int seatPosition;
    private final Table table;
    
    public Seat (int position, Table table) {
        this.handHolder = null;
        this.seatPosition = position;
        this.table = table;
    }
    
    @Override
    public String toString()
    {
      return getHandHolder() == null ? "HandHolder is null" : getHandHolder().toString();
    }

    /**
     * @return the handHolder
     */
    public HandHolder getHandHolder() {
        return handHolder;
    }

    /**
     * @param handHolder the handHolder to set
     */
    public void setHandHolder(HandHolder handHolder) {
        this.handHolder = handHolder;
    }

    /**
     * @return the seatPosition
     */
    public int getSeatPosition() {
        return seatPosition;
    }

    /**
     * @return the table
     */
    public Table getTable() {
        return table;
    }
}
