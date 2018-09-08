/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blackjack3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Brad
 */
public class BlackJack3 {

    // assumes the current class is called logger
    private final static Logger LOGGER = Logger.getLogger(BlackJack3.class.getName());
    public static int bustRow = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlackJack3 bj = new BlackJack3();
        setupLogging();
        Map<Integer, List<Object>> bustOutputRowsMap = new ConcurrentHashMap<>(10000);
        try (FileOutputStream out = new FileOutputStream("C:\\temp\\report.xlsx")) {
            try (FileOutputStream bustOut = new FileOutputStream("C:\\temp\\bust.xlsx")) {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet();
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("Shoe #");
                row.createCell(1).setCellValue("Round");
                row.createCell(2).setCellValue("Name");
                row.createCell(3).setCellValue("Hand");
                row.createCell(4).setCellValue("Dealer Hand");
                row.createCell(5).setCellValue("Bet");
                row.createCell(6).setCellValue("BankRoll");
                row.createCell(7).setCellValue("HiLo Count");
                Workbook bwb = new XSSFWorkbook();
                Sheet bsheet = bwb.createSheet();
                Row brow = bsheet.createRow(0);
                brow.createCell(0).setCellValue("Player");
                brow.createCell(1).setCellValue("Bankroll");
                bustRow++;
                IntStream.range(1, 1001).parallel().forEach(i -> {
                    Table theTable = bj.InitializeGame();
                    while (theTable.getRound() < 300000) {
                        theTable.PlayARoundOfHands(sheet);
                    }

                    theTable.getSeats().stream().filter(seat -> seat.getHandHolder() instanceof Player).forEach(seat -> {
                        Player p = (Player) seat.getHandHolder();
                        List<Object> loopList = new ArrayList<>();
                        loopList.add(p.name);
                        loopList.add(p.getBankRoll());
                        bustOutputRowsMap.put(bustRow, loopList);
                        bustRow++;
                    });
                    theTable.getWaitingPlayers().forEach(p -> {
                        List<Object> loopList = new ArrayList<>();
                        loopList.add(p.name);
                        loopList.add(p.getBankRoll());
                        bustOutputRowsMap.put(bustRow, loopList);
                        bustRow++;
                    });
                });
                bustOutputRowsMap.keySet().stream().forEach((key) -> {
                    Row loopRow = bsheet.createRow(key);
                    List<Object> mapRow = bustOutputRowsMap.get(key);
                    loopRow.createCell(0).setCellValue((String) mapRow.get(0));
                    loopRow.createCell(1).setCellValue((Float) mapRow.get(1));
                });
                bwb.write(bustOut);
                bustOut.close();
                wb.write(out);
                out.close();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BlackJack3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlackJack3.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void setupLogging() throws SecurityException {
        final InputStream inputStream = BlackJack3.class.getResourceAsStream("logging.properties");

        try {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    private Table InitializeGame() {
        Table theTable = new Table(7, true, false, false, 4, true, 3 / 2, false, 15, 500, new Shoe(8, new Float("0.8")));
        Dealer dealer = new Dealer("Jasmine", theTable);
        int bankroll = 45000;
        theTable.setDealer(dealer);
        theTable.getSeats().get(0).setHandHolder(new Player("Brad", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(1).setHandHolder(new Player("Skip", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(2).setHandHolder(new Player("Jason", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(3).setHandHolder(new Player("Robin", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(4).setHandHolder(new Player("Vadim", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(5).setHandHolder(new Player("Glen", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        theTable.getSeats().get(6).setHandHolder(new Player("Craig", new BettingStrategy(-1, 2, 6, 30, 60), "C:\\temp\\Brad.xlsx", bankroll));
        return theTable;
    }
}
