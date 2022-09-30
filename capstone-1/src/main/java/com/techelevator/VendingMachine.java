package com.techelevator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine {
    public static final int FEED_MONEY = 1, GIVE_CHANGE = 2;
    private static final String FEED_MONEY_STR = " FEED MONEY ", GIVE_CHANGE_STR = " GIVE CHANGE ";
    private static final String INVENTORY_FILE_NAME = "vendingmachine.csv";
    private static final String CHIP_TYPE = "Chip", CANDY_TYPE = "Candy", DRINK_TYPE = "Drink", GUM_TYPE = "Gum";
    private static final String LOG_FILE_NAME = "log.txt", PREVIOUS_SALES_MAP = "previousSales.txt";
    private final List<Item> items;
    private final Map<String, Item> itemSelector;
    private BigDecimal userBalance;
    private final List<String> salesLog;
    private final File logFile;
    private TotalSalesTracker salesTracker;

    public VendingMachine() {
        items = new ArrayList<Item>();
        itemSelector = new HashMap<String, Item>();
        userBalance = new BigDecimal("0.00");
        salesLog = new ArrayList<String>();
        logFile = new File(LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (Exception e) {
                System.out.println("File creation failure.");
            }

        }

        try {
            populateItems();
        } catch (Exception e) {
            System.out.println("Problem! vendingmachine.csv most likely not present.");
        }

        readSalesTracker();
        if (salesTracker.getTotalSales() == null) {
            salesTracker.setTotalSales(new HashMap<String, Integer>());
        }
        if (salesTracker.getTotalRevenue() == null) {
            salesTracker.setTotalRevenue(new BigDecimal("0.00"));
        }
        for (Item item : items) {
            if (salesTracker.getTotalSales().get(item.getName()) == null)
                salesTracker.getTotalSales().put(item.getName(), 0);
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public BigDecimal getUserBalance() {
        return userBalance;
    }

    public Map<String, Item> getItemSelector() {
        return itemSelector;
    }

    public List<String> getSalesLog() {
        return salesLog;
    }

    public void addMoneyToMachine(BigDecimal money) {
        userBalance = userBalance.add(money);
    }

    public void dockMoneyOnMachine(BigDecimal itemPrice) {
        userBalance = userBalance.subtract(itemPrice);
    }

    public boolean itemAtPositionExists(String position) {
        return itemSelector.get(position) != null;
    }

    /* returns -1 if there is no item at that position */
    public int getItemStock(String position) {
        Item itemAtPosition = itemSelector.get(position);

        if (itemAtPosition != null) {
            return itemAtPosition.getStock();
        }

        return -1;
    }

    /* returns null if there is no item at that position. */
    public String getItemMessage(String position) {
        return itemSelector.get(position) != null ? itemSelector.get(position).getMessage() : null;
    }

    /* returns null if there is no item at that position. */
    public String getItemType(String position) {
        return itemSelector.get(position) != null ? itemSelector.get(position).getType() : null;
    }

    /* returns null if there is no item at that position. */
    public String getItemName(String position) {
        return itemSelector.get(position) != null ? itemSelector.get(position).getName() : null;
    }

    /*
     * return -1 if there is no item at that position
     */
    public BigDecimal getItemPrice(String position) {
        return itemSelector.get(position) != null ? itemSelector.get(position).getPrice() : BigDecimal.valueOf(-1);
    }

    public boolean decrementItemAtPosition(String position) {
        boolean decrementSuccesful = false;
        Item itemAtPosition = itemSelector.get(position);
        if (itemAtPosition.getStock() > 0 && itemAtPosition != null)
            itemAtPosition.decrementStock();
        return decrementSuccesful;
    }

    public String getChangeFromMachine() {
        int balanceNoDecimal = userBalance.multiply(BigDecimal.valueOf(100)).intValue();
        int quarters = balanceNoDecimal / 25;
        int dimes = (balanceNoDecimal - (quarters * 25)) / 10;
        int nickels = (balanceNoDecimal - (quarters * 25 + dimes * 10)) / 5;

        String quarterAmount = "", dimeAmount = "", nickelAmount = "";

        if (quarters == 1) {
            quarterAmount = quarters + " quarter";
        } else if (quarters > 1) {
            quarterAmount = quarters + " quarters";
        }

        if (dimes == 1) {
            dimeAmount = quarters > 0 ? " and " + dimes + " dime" : dimes + " dime";
        } else if (dimes > 1) {
            dimeAmount = quarters > 0 ? " and " + dimes + " dimes" : dimes + " dimes";
        }

        if (nickels == 1) {
            nickelAmount = (quarters > 0 || dimes > 0) ? " and " + nickels + " nickel" : nickels + " nickel";
        }

        if ((quarterAmount + dimeAmount + nickelAmount).equals("")) {
            return "You have spent all your money or have not added money yet. " + "Thanks for shopping!";
        }

        userBalance = BigDecimal.valueOf(0);
        return "Here is your change: " + quarterAmount + dimeAmount + nickelAmount + ". Have a great day!";
    }

    private void populateItems() throws IOException {
        File file = new File(INVENTORY_FILE_NAME);
        try (Scanner reader = new Scanner(file.getAbsoluteFile())) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] itemsStrings = line.split("\\|");
                if (itemsStrings[3].equals(CHIP_TYPE)) {
                    Chips chip = new Chips(itemsStrings[0], itemsStrings[1], new BigDecimal(itemsStrings[2]));
                    items.add(chip);
                    itemSelector.put(chip.getPosition(), chip);
                } else if (itemsStrings[3].equals(CANDY_TYPE)) {
                    Candy candy = new Candy(itemsStrings[0], itemsStrings[1], new BigDecimal(itemsStrings[2]));
                    items.add(candy);
                    itemSelector.put(candy.getPosition(), candy);
                } else if (itemsStrings[3].equals(DRINK_TYPE)) {
                    Drink drink = new Drink(itemsStrings[0], itemsStrings[1], new BigDecimal(itemsStrings[2]));
                    items.add(drink);
                    itemSelector.put(drink.getPosition(), drink);
                } else if (itemsStrings[3].equals(GUM_TYPE)) {
                    Gum gum = new Gum(itemsStrings[0], itemsStrings[1], new BigDecimal(itemsStrings[2]));
                    items.add(gum);
                    itemSelector.put(gum.getPosition(), gum);
                }
            }

        } catch (Exception e) {
            System.out.println("Problem encountered. The file vendingmachine.csv is probably missing. Exiting.");
            System.exit(1);
        }
    }

    public boolean log(int transaction, BigDecimal amount) {
        String transStr = "", logStr = "";
        if (transaction == FEED_MONEY)
            transStr = FEED_MONEY_STR;
        if (transaction == GIVE_CHANGE)
            transStr = GIVE_CHANGE_STR;
        logStr = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")) + transStr + "$"
                + amount.toString() + " $" + userBalance.toString();
        salesLog.add(logStr);
        logToFile(logStr);
        return !transStr.isEmpty();
    }

    public boolean log(String position) {
        String logStr = "";
        Item itemInQuestion = itemSelector.get(position);
        if (itemInQuestion != null) {
            logStr = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a")) + " "
                    + itemInQuestion.getName() + " " + itemInQuestion.getPosition() + " $"
                    + userBalance.add(itemInQuestion.getPrice()) + " $" + userBalance.toString();
            salesLog.add(logStr);
            logToFile(logStr);
            salesTracker.addToTotalRevenue(itemInQuestion.getPrice());
            salesTracker.getTotalSales().put(itemInQuestion.getName(),
                    salesTracker.getTotalSales().get(itemInQuestion.getName()) + 1);
            writeSalesTracker();
        }
        return itemInQuestion != null;
    }

    public boolean readSalesTracker() {
        boolean readSuccessful = true;
        try (FileInputStream prevSalesTrackerFile = new FileInputStream(PREVIOUS_SALES_MAP)) {
            ObjectInputStream preSalesTrackerIn = new ObjectInputStream(prevSalesTrackerFile);
            try {
                salesTracker = (TotalSalesTracker) preSalesTrackerIn.readObject();
                preSalesTrackerIn.close();
            } catch (ClassNotFoundException e) {
                // Eclipse did not like when this wasn't caught.
                salesTracker = new TotalSalesTracker();
                readSuccessful = false;
            }

        } catch (IOException e) {
            salesTracker = new TotalSalesTracker();
            readSuccessful = false;
        }

        return readSuccessful;
    }

    public boolean logToFile(String logStr) {
        try (PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile.getAbsoluteFile(), true))) {
            logWriter.append(logStr + "\n");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean writeSalesTracker() {
        boolean writeSuccessful = true;
        try (FileOutputStream outputFile = new FileOutputStream(PREVIOUS_SALES_MAP)) {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(outputFile)) {
                outputStream.writeObject(salesTracker);
            }
        } catch (IOException e) {
            writeSuccessful = false;
        }
        return writeSuccessful;
    }

    public boolean hiddenSalesLog() {
        boolean writeSuccessful = true;
        String logFile = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy-hh.mm.ss.SS-a"));
        logFile = "HiddenSalesLog" + logFile + ".txt";
        File hiddenLog = new File(logFile);
        try {
            hiddenLog.createNewFile();
        } catch (IOException e) {
            writeSuccessful = false;
        }
        try (PrintWriter logWriter = new PrintWriter(hiddenLog.getAbsoluteFile())) {
            for (Item item : items)
                logWriter.write(item.getName() + "|" + salesTracker.getTotalSales().get(item.getName()) + "\n");
            logWriter.write("Total Revenue: $" + salesTracker.getTotalRevenue().toString());
            logWriter.flush();
        } catch (FileNotFoundException e) {
            writeSuccessful = false;
        }

        return writeSuccessful;
    }

}