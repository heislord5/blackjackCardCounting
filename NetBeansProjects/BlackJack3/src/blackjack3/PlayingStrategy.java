/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import static blackjack3.PlayEnum.DOUBLE;
import static blackjack3.PlayEnum.DOUBLE_OR_HIT;
import static blackjack3.PlayEnum.DOUBLE_OR_STAND;
import static blackjack3.PlayEnum.HIT;
import static blackjack3.PlayEnum.SPLIT;
import static blackjack3.PlayEnum.SPLIT_IF_DOUBLE_AFTER_SPLIT_ALLOWED_OR_HIT;
import static blackjack3.PlayEnum.STAND;
import static blackjack3.PlayEnum.SURRENDER;
import static blackjack3.PlayEnum.SURRENDER_OR_HIT;
import static blackjack3.PlayEnum.SURRENDER_OR_SPLIT;
import static blackjack3.PlayEnum.SURRENDER_OR_STAND;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Brad
 */
final class PlayingStrategy {

    protected final Map<Integer, Map<CardEnum, PlayEnum>> softMapping;
    protected final Map<Integer, Map<CardEnum, PlayEnum>> hardMapping;
    protected final Map<CardEnum, Map<CardEnum, PlayEnum>> splitableMapping;

    public PlayingStrategy(String path) {
        this.softMapping = new HashMap<>();
        this.hardMapping = new HashMap<>();
        this.splitableMapping = new HashMap<>();

        try (InputStream input = new FileInputStream(path)) {
            Workbook wb = WorkbookFactory.create(input);
            Sheet sheet = wb.getSheetAt(0);

            PopulateHardMapping(sheet);
            PopulateSoftMapping(sheet);
            PopulateSplittableMapping(sheet);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PlayingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PlayingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException | EncryptedDocumentException ex) {
            Logger.getLogger(PlayingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void PopulateHardMapping(Sheet sheet) throws NumberFormatException {
        //Read mapping for player with Hard Value
        for (int rowCounter = 1; rowCounter <= 18; rowCounter++) {
            Row row = sheet.getRow(rowCounter);

            Map<Integer, Map<CardEnum, PlayEnum>> mapping;
            mapping = this.getHardMapping();

            //Get all the columns of the mapping.
            EnumMap<CardEnum, PlayEnum> tempMap = new EnumMap<>(CardEnum.class);

            tempMap.put(CardEnum.TWO, PlayEnum.get(row.getCell(1).getStringCellValue()));
            tempMap.put(CardEnum.THREE, PlayEnum.get(row.getCell(2).getStringCellValue()));
            tempMap.put(CardEnum.FOUR, PlayEnum.get(row.getCell(3).getStringCellValue()));
            tempMap.put(CardEnum.FIVE, PlayEnum.get(row.getCell(4).getStringCellValue()));
            tempMap.put(CardEnum.SIX, PlayEnum.get(row.getCell(5).getStringCellValue()));
            tempMap.put(CardEnum.SEVEN, PlayEnum.get(row.getCell(6).getStringCellValue()));
            tempMap.put(CardEnum.EIGHT, PlayEnum.get(row.getCell(7).getStringCellValue()));
            tempMap.put(CardEnum.NINE, PlayEnum.get(row.getCell(8).getStringCellValue()));
            tempMap.put(CardEnum.TEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.JACK, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.QUEEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.KING, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.ACE, PlayEnum.get(row.getCell(10).getStringCellValue()));
            mapping.put((int) row.getCell(0).getNumericCellValue(), tempMap);
        }
    }

    private void PopulateSoftMapping(Sheet sheet) throws NumberFormatException {
        //Read mapping for player with Hard Value
        for (int rowCounter = 20; rowCounter <= 29; rowCounter++) {
            Row row = sheet.getRow(rowCounter);

            Map<Integer, Map<CardEnum, PlayEnum>> mapping;
            mapping = this.getSoftMapping();

            //Get all the columns of the mapping.
            EnumMap<CardEnum, PlayEnum> tempMap = new EnumMap<>(CardEnum.class);

            tempMap.put(CardEnum.TWO, PlayEnum.get(row.getCell(1).getStringCellValue()));
            tempMap.put(CardEnum.THREE, PlayEnum.get(row.getCell(2).getStringCellValue()));
            tempMap.put(CardEnum.FOUR, PlayEnum.get(row.getCell(3).getStringCellValue()));
            tempMap.put(CardEnum.FIVE, PlayEnum.get(row.getCell(4).getStringCellValue()));
            tempMap.put(CardEnum.SIX, PlayEnum.get(row.getCell(5).getStringCellValue()));
            tempMap.put(CardEnum.SEVEN, PlayEnum.get(row.getCell(6).getStringCellValue()));
            tempMap.put(CardEnum.EIGHT, PlayEnum.get(row.getCell(7).getStringCellValue()));
            tempMap.put(CardEnum.NINE, PlayEnum.get(row.getCell(8).getStringCellValue()));
            tempMap.put(CardEnum.TEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.JACK, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.QUEEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.KING, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.ACE, PlayEnum.get(row.getCell(10).getStringCellValue()));
            mapping.put((int) row.getCell(0).getNumericCellValue(), tempMap);
        }
    }

    private void PopulateSplittableMapping(Sheet sheet) throws NumberFormatException {
        //Read mapping for player with Hard Value
        for (int rowCounter = 31; rowCounter <= 43; rowCounter++) {
            Row row = sheet.getRow(rowCounter);

            Map<CardEnum, Map<CardEnum, PlayEnum>> mapping = this.getSplitableMapping();
            CardEnum splitCard = CardEnum.get(row.getCell(0).getStringCellValue());

            //Get all the columns of the mapping.
            Map tempMap = new EnumMap<>(CardEnum.class);
            tempMap.put(CardEnum.TWO, PlayEnum.get(row.getCell(1).getStringCellValue()));
            tempMap.put(CardEnum.THREE, PlayEnum.get(row.getCell(2).getStringCellValue()));
            tempMap.put(CardEnum.FOUR, PlayEnum.get(row.getCell(3).getStringCellValue()));
            tempMap.put(CardEnum.FIVE, PlayEnum.get(row.getCell(4).getStringCellValue()));
            tempMap.put(CardEnum.SIX, PlayEnum.get(row.getCell(5).getStringCellValue()));
            tempMap.put(CardEnum.SEVEN, PlayEnum.get(row.getCell(6).getStringCellValue()));
            tempMap.put(CardEnum.EIGHT, PlayEnum.get(row.getCell(7).getStringCellValue()));
            tempMap.put(CardEnum.NINE, PlayEnum.get(row.getCell(8).getStringCellValue()));
            tempMap.put(CardEnum.TEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.JACK, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.QUEEN, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.KING, PlayEnum.get(row.getCell(9).getStringCellValue()));
            tempMap.put(CardEnum.ACE, PlayEnum.get(row.getCell(10).getStringCellValue()));
            mapping.put(splitCard, tempMap);
        }
    }

    /**
     * @return the mapping
     */
    public Map<Integer, Map<CardEnum, PlayEnum>> getSoftMapping() {
        return softMapping;
    }

    /**
     * @return the hardMapping
     */
    public Map<Integer, Map<CardEnum, PlayEnum>> getHardMapping() {
        return hardMapping;
    }

    /**
     * @return the splitableMapping
     */
    public Map<CardEnum, Map<CardEnum, PlayEnum>> getSplitableMapping() {
        return splitableMapping;
    }

    public PlayEnum reducePlay(final PlayEnum unreducedPlay, Table table, Hand hand) {
        PlayEnum enum2Return = unreducedPlay;
        if (unreducedPlay.equals(DOUBLE_OR_HIT)) {
            if (table.doubleAllowed(hand)) {
                enum2Return = DOUBLE;
            } else {
                enum2Return = HIT;
            }
        } else if (unreducedPlay.equals(DOUBLE_OR_STAND)) {
            if (table.doubleAllowed(hand)) {
                enum2Return = DOUBLE;
            } else {
                enum2Return = STAND;
            }
        } else if (unreducedPlay.equals(SPLIT_IF_DOUBLE_AFTER_SPLIT_ALLOWED_OR_HIT)) {
            if (table.isDoubleAfterSplitAllowed()) {
                enum2Return = SPLIT;
            } else {
                enum2Return = HIT;
            }
        } else if (unreducedPlay.equals(SURRENDER_OR_HIT)) {
            if (table.isSurrenderAllowed()) {
                enum2Return = SURRENDER;
            } else {
                enum2Return = HIT;
            }
        } else if (unreducedPlay.equals(SURRENDER_OR_SPLIT)) {
            if (table.isSurrenderAllowed()) {
                enum2Return = SURRENDER;
            } else {
                enum2Return = SPLIT;
            }
        } else if (unreducedPlay.equals(SURRENDER_OR_STAND)) {
            if (table.isSurrenderAllowed()) {
                enum2Return = SURRENDER;
            } else {
                enum2Return = STAND;
            }
        }
        return enum2Return;
    }

    PlayEnum getTableHonoringPlay(Hand hand, HandTypeEnum handType, Table table) {
        PlayEnum play4hand = null;
        ShoeCard dealerFaceCard = table.getDealer().GetDealerFaceCard();
        if (hand.value() > 21) {
            play4hand = STAND;
        } else if (handType == HandTypeEnum.SPLITTABLE) {
            Map<CardEnum, PlayEnum> row = this.getSplitableMapping().get(hand.getCardsInHand().get(0).getCard());
            PlayEnum unreducedPlay = row.get(dealerFaceCard.getCard());
            play4hand = reducePlay(unreducedPlay, table, hand);
        } else if (handType == HandTypeEnum.SOFT) {
            Map<CardEnum, PlayEnum> row = this.getSoftMapping().get(hand.value());
            PlayEnum unreducedPlay = row.get(dealerFaceCard.getCard());
            play4hand = reducePlay(unreducedPlay, table, hand);
        } else if (handType == HandTypeEnum.HARD) {
            Map<CardEnum, PlayEnum> row = this.getHardMapping().get(hand.value());
            PlayEnum unreducedPlay = row.get(dealerFaceCard.getCard());
            play4hand = reducePlay(unreducedPlay, table, hand);
        }
        return play4hand;
    }
}
