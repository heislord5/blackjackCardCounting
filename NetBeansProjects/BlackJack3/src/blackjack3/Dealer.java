/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import static java.util.logging.Level.FINE;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Brad
 */
public class Dealer extends HandHolder {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(Dealer.class.getName());
    private final static String DEALER_HITS_SOFT_17 = "C:\\temp\\dealerHitSoft.xlsx";
    private final static String DEALER_STAYS_SOFT_17 = "C:\\temp\\dealerStaySoft.xlsx";

    protected final Table table;

    public Dealer(String name, Table table) {
        super(name, null);
        String dealerHitStrategy;
        this.table = table;
        dealerHitStrategy = table.isHitSoft17() ? DEALER_HITS_SOFT_17 : DEALER_STAYS_SOFT_17;
        this.setPlayingStrategy(new PlayingStrategy(dealerHitStrategy));
        table.setDealer(this);
        this.setIsDealer(true);
    }

    /**
     * @return the hand
     */
    public Hand getHand() {
        Hand returnValue;
        if (this.getHands() == null) {
            returnValue = null;
        } else {
            returnValue = this.getHands().get(0);
        }
        return returnValue;
    }

    /**
     * @param hand the hand to set
     */
    public void setHand(Hand hand) {
        if (this.getHands() == null) {
            List<Hand> hands = new ArrayList<>();
            this.setHands(hands);
            hands.add(null);
        } else if (this.getHands().isEmpty()) {
            this.getHands().add(null);
        }
        this.getHands().set(0, hand);
    }

    public void DealFirstTwoCards(Table table) {
        if (Table.getLOGGER().isLoggable(Level.FINEST)) {
            Table.getLOGGER().finest("Beginning of Hand.  Do all Handholders have an empty hand?:");
            Table.getLOGGER().log(Level.FINEST, "Seat Count: {0}", table.getSeats().size());
            Table.getLOGGER().finest(System.lineSeparator());
            table.getSeats().stream().map((Seat seat) -> {
                Table.getLOGGER().finest(seat.toString());
                return seat;
            }).filter((Seat s) -> s.getHandHolder() != null && s.getHandHolder().isActiveThisDeal()).forEach((Seat seat) -> {
                List<Hand> hands = seat.getHandHolder().getHands();
                if (hands == null || hands.isEmpty()) {
                    Table.getLOGGER().finest("HANDS ARE EMPTY AS EXPECTED");
                } else {
                    Table.getLOGGER().log(Level.FINEST, "There appears to be hands{0}", hands);
                }
            });
        }
        table.getDealer().setHand(new Hand(table.getUniqueHandIdentifierIndex(), table.getDealer(), table.getSeats().get(table.getNumberOfSeats()), 0));
        for (int cardLooper = 0; cardLooper < 2; cardLooper++) {
            table.getSeats().stream().filter((Seat s) -> s.getHandHolder() != null).forEachOrdered((Seat s) -> {
                s.getHandHolder().getHands().stream().filter((Hand h) -> h.getSeat() == s).forEachOrdered((Hand h) -> h.getCardsInHand().add(table.getShoe().GetACard()));
            });
        }
        if (Table.getLOGGER().isLoggable(Level.FINEST)) {
            StringBuilder sb = new StringBuilder();
            sb.append(System.lineSeparator()).append("shoe remaining cards:").append(table.getShoe().getShoeCards().size() - table.getShoe().getCurrentIndex()).append("current hands: ").append(System.lineSeparator()).append(table.getSeats()).append(System.lineSeparator()).append("CurrentHiLo").append(table.getShoe().getHiLoCount()).append(" : Real HiLo").append(table.getShoe().getRealHiLoCount());
            Table.getLOGGER().finest(sb.toString());
        }
    }

    void Prepare4NewShoe(Table table) {
        table.getShoe().Shuffle();
        table.reseatWaitingPlayers();
    }

    void FinishDealingHand(Table table) {
        if (!table.getDealer().getHand().isBlackJack()) {
            table.getSeats().stream().filter((Seat s) -> s.getHandHolder() != null).forEachOrdered((Seat s) -> {
                HandHolder handHolder = s.getHandHolder();
                List<Hand> hands = handHolder.getHands();
                Optional<Hand> hand = hands.stream().filter(h -> !h.isDelt() && h.getSeat().equals(s)).findFirst();
                while (hand.isPresent()) {
                    handHolder.PlayHand(hand.get(), table);
                    hand = handHolder.getHands().stream().filter(h -> !h.isDelt() && h.getSeat().equals(s)).findFirst();
                }
            });
        }
    }

    void ClearPlayerHands(Table table) {
        table.getSeats().stream().filter((Seat s) -> s.getHandHolder() != null).forEach((Seat s) -> {
            s.getHandHolder().setHands(null);
        });
    }

    public ShoeCard GetDealerFaceCard() {
        return getHand().getCardsInHand().get(0);
    }

    public ShoeCard DealCard() {
        return this.getTable().getShoe().GetACard();
    }

    /**
     * @return the table
     */
    private Table getTable() {
        return table;
    }

    void PayOutWinsAndLosses(Table theTable, Sheet sheet) {
        table.getSeats().stream().filter((Seat s) -> s.getHandHolder() != null && !s.getHandHolder().isDealer()).forEachOrdered((Seat s) -> {
            Player player = (Player) s.getHandHolder();
            Hand dealerHand = s.getTable().getDealer().getHand();
            List<Hand> hands = player.getHands();
            hands.stream().filter((hand) -> (hand.getSeat().equals(s))).forEach((hand) -> {
                if (!hand.isBusted()) {
                    if (!dealerHand.isBlackJack()) {
                        if (hand.isBlackJack()) {
                            player.TransferCash(hand.getCurrentBet() * 5 / 2);
                        } else if (dealerHand.isBusted() || hand.value() > dealerHand.value()) {
                            player.TransferCash(hand.getCurrentBet() * 2);
                        } else if (hand.value() == dealerHand.value()) {
                            player.TransferCash(hand.getCurrentBet());
                        }
                    } else if (hand.isBlackJack() /*&& Dealer hand is blackJack*/) {
                        player.TransferCash(hand.getCurrentBet());
                    }
                }
                if (LOGGER.isLoggable(FINE)) {
                    Row row = sheet.createRow(hand.getRootHandNumber());
                    row.createCell(0).setCellValue(table.getShoe().getShoeCount());
                    row.createCell(1).setCellValue(table.getRound());
                    row.createCell(2).setCellValue(player.getName());
                    row.createCell(3).setCellValue(hand.toShortString());
                    row.createCell(4).setCellValue(table.getDealer().getHand().toShortString());
                    row.createCell(5).setCellValue(hand.getCurrentBet());
                    row.createCell(6).setCellValue(player.getBankRoll());
                    row.createCell(7).setCellValue(table.getShoe().getPreviousRealHiLoCount());
                }
            });
        });
    }
}
