/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import static java.util.logging.Level.FINER;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author Brad
 */
public class Table {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Table.class.getName());

    /**
     * @return the LOGGER
     */
    public static Logger getLOGGER() {
        return LOGGER;
    }
    private int uniqueHandIdentifierIndex = 0;

    /**
     * @return the uniqueHandIdentifierIndex
     */
    public int getUniqueHandIdentifierIndex() {
        return ++uniqueHandIdentifierIndex;
    }

    protected Dealer dealer;
    private List<Seat> seats;
    private final int numberOfSeats;
    private final boolean hitSoft17;
    private final boolean hitSplitAces;
    private final boolean multipleSplitAces;
    private final int numberOfSplitsAllowed;
    private final boolean doubleAfterSplit;
    private final float blackjackPayout;
    private final boolean surrenderAllowed;
    private final int maximumBet;
    private final int minimumBet;
    private final Shoe shoe;
    private List<Player> waitingPlayers;
    private int round;

    public Table(int numberOfSeats, boolean hitSoft17, boolean hitSplitAces,
            boolean multipleSplitAces, int numberOfSplitsAllowed, boolean doubleAfterSplit,
            float blackjackPayout, boolean surrenderAllowed,
            int minimumBet, int maximumBet, Shoe shoe) {
        this.seats = new ArrayList<>();

        this.hitSoft17 = hitSoft17;
        this.hitSplitAces = hitSplitAces;
        this.multipleSplitAces = multipleSplitAces;
        this.numberOfSplitsAllowed = numberOfSplitsAllowed;
        this.doubleAfterSplit = doubleAfterSplit;
        this.blackjackPayout = blackjackPayout;
        this.minimumBet = minimumBet;
        this.maximumBet = maximumBet;
        this.surrenderAllowed = surrenderAllowed;
        this.shoe = shoe;
        this.waitingPlayers = new ArrayList<>();
        this.round = 0;

        for (int loop = 0; loop < numberOfSeats; loop++) {
            seats.add(new Seat(loop, this));
        }
        //Add theDealer seat.
        seats.add(new Seat(numberOfSeats, this));
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * @return the handHolderInSeat
     */
    public List<Seat> getSeats() {
        return seats;
    }

    /**
     * @param seats the handHolderInSeat to set
     */
    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public void reseatWaitingPlayers() {
        getLOGGER().log(FINER, "Reseating Players: {0}", getWaitingPlayers());
        List<Seat> emptySeats = this.getSeats().stream().filter(s -> s.getHandHolder() == null).collect(Collectors.toList());

        if (!emptySeats.isEmpty()) {
            List<Player> playersWithMoney2BSeated = getWaitingPlayers().stream().filter(p -> p.getBankRoll() > 0).collect(Collectors.toList());
            for (int count = 0; count < playersWithMoney2BSeated.size(); count++) {
                emptySeats.get(count).setHandHolder(playersWithMoney2BSeated.get(count));
                getWaitingPlayers().remove(playersWithMoney2BSeated.get(count));
            }
        }
    }

    // There is still just one hand per seat.  So we know the hand is index 0.
    void AccumulateInitialBetsFromPlayers() {
        seats.stream().filter(seat -> seat.getHandHolder() != null && seat.getSeatPosition() != getDealerSeatNumber()).forEach((Seat seat) -> {
            Player player;
            player = (Player) seat.getHandHolder();

            Hand hand = new Hand(getUniqueHandIdentifierIndex(), player, seat, 0);
            player.getHands().add(hand);
            player.BetHand(hand);
        });
    }

    private Seat GetDealerSeat() {
        return seats.get(this.getNumberOfSeats());
    }

    /**
     * @return the numberOfSeats
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    /**
     * @return the numberOfSeats
     */
    public int getDealerSeatNumber() {
        return numberOfSeats;
    }

    /**
     * @return the shoe
     */
    public Shoe getShoe() {
        return shoe;
    }

    /**
     * @return the theDealer
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * @return the hitSoft17
     */
    public boolean isHitSoft17() {
        return hitSoft17;
    }

    /**
     * @return the hitSplitAces
     */
    public boolean isHitSplitAces() {
        return hitSplitAces;
    }

    /**
     * @return the multipleSplitAces
     */
    public boolean isMultipleSplitAces() {
        return multipleSplitAces;
    }

    /**
     * @return the numberOfSplitsAllowed
     */
    public int getNumberOfSplitsAllowed() {
        return numberOfSplitsAllowed;
    }

    /**
     * @return the doubleAfterSplit
     */
    public boolean isDoubleAfterSplitAllowed() {
        return doubleAfterSplit;
    }

    /**
     * @return the blackjackPayout
     */
    public float getBlackjackPayout() {
        return blackjackPayout;
    }

    /**
     * @return the maximumBet
     */
    public int getMaximumBet() {
        return maximumBet;
    }

    /**
     * @return the minimumBet
     */
    public int getMinimumBet() {
        return minimumBet;
    }

    /**
     * @return the waitingPlayers
     */
    public List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * @param waitingPlayers the waitingPlayers to set
     */
    public void setWaitingPlayers(List<Player> waitingPlayers) {
        this.waitingPlayers = waitingPlayers;
    }

    /**
     * @param dealer the theDealer to set
     */
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
        if (this.GetDealerSeat().getHandHolder() == null) {
            this.GetDealerSeat().setHandHolder(dealer);
        }
    }

    /**
     * @return the surrenderAllowed
     */
    public boolean isSurrenderAllowed() {
        return surrenderAllowed;
    }

    boolean doubleAllowed(Hand hand) {
        if (hand.getCardsInHand().size() == 2 && (hand.splitCount == 0 || this.isDoubleAfterSplitAllowed())) {
            return true;
        } else {
            return false;
        }
    }

    void PlayARoundOfHands(Sheet sheet) {
        Table.LOGGER.log(Level.FINEST, "PlayARound");
        this.setRound(this.getRound() + 1);
        this.getShoe().setPreviousRealHiLoCount();
        int handHolderCount = this.getSeats().stream().filter((Seat seat) -> seat.getHandHolder() != null).collect(Collectors.toList()).size();
        Dealer theDealer = this.getDealer();
        if (handHolderCount < 2 || this.getShoe().hasExceededAllowedPenetration()) {
            theDealer.Prepare4NewShoe(this);
        }
        this.AccumulateInitialBetsFromPlayers();
        theDealer.DealFirstTwoCards(this);
        theDealer.FinishDealingHand(this);
        theDealer.PayOutWinsAndLosses(this, sheet);
        theDealer.ClearPlayerHands(this);
    }

    /**
     * @return the round
     */
    public int getRound() {
        return round;
    }

    /**
     * @param round the round to set
     */
    public void setRound(int round) {
        this.round = round;
    }
}
