package com.techelevator;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class VendingMachineTest {
    VendingMachine vendingMachineTest = new VendingMachine();

    @Test
    public void listOfItemsShouldBeGeneratedInVendingMachine() {
        assertTrue(vendingMachineTest.getItems().size() != 0);
        assertNotNull(vendingMachineTest.getItems().get(0).getType());
        assertNotNull(vendingMachineTest.getItemSelector());
    }

    @Test
    public void usersShouldBeAbleToAddMoneyToTheirBalanceInMachine() {
        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(5));
        BigDecimal userBalance = vendingMachineTest.getUserBalance();
        assertEquals(new BigDecimal("5.00"), userBalance);
    }

    @Test
    public void machineShouldBeAbleToDockUserBalance() {
        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(3));
        vendingMachineTest.dockMoneyOnMachine(BigDecimal.valueOf(1));
        BigDecimal userBalance = vendingMachineTest.getUserBalance();
        assertEquals(new BigDecimal("2.00"), userBalance);
    }

    @Test
    public void itemAtPositionExistWillFindWhetherItemExists() {
        // A1 is an item that exists in vendingmachine.csv
        assertTrue(vendingMachineTest.itemAtPositionExists("A1"));

        // A100 does not exist in vendingmachine.csv
        assertTrue(!vendingMachineTest.itemAtPositionExists("A100"));
    }

    @Test
    public void getItemStockShouldReturnTheQuantityOfTheItemOrNegativeOne() {
        // A1 is an item that exists in vendingmachine.csv,
        // and stock is always initialized to 5
        assertEquals(5, vendingMachineTest.getItemStock("A1"));

        // A100 does not exist in vendingmachine.csv
        assertEquals(-1, vendingMachineTest.getItemStock("A100"));
    }

    @Test
    public void decrementItemAtPositionShouldReduceItsStockIfFoundOrHasStock() {
        // A1 is an item that exists in vendingmachine.csv,
        // and stock is always initialized to 5
        vendingMachineTest.decrementItemAtPosition("A1");
        assertEquals(4, vendingMachineTest.getItemStock("A1"));
    }

    @Test
    public void getChangeShouldReturnTheCorrectAmountOfChange() {
        assertEquals("You have spent all your money or have not added money yet. "
                        + "Thanks for shopping!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(3.05));
        assertEquals("Here is your change: 12 quarters and 1 nickel. Have a great day!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(1.45));
        assertEquals("Here is your change: 5 quarters and 2 dimes. Have a great day!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(0.55));
        assertEquals("Here is your change: 2 quarters and 1 nickel. Have a great day!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(0.15));
        assertEquals("Here is your change: 1 dime and 1 nickel. Have a great day!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(0.75));
        assertEquals("Here is your change: 3 quarters. Have a great day!",
                vendingMachineTest.getChangeFromMachine());

        vendingMachineTest.addMoneyToMachine(BigDecimal.valueOf(0.00));
        assertEquals("You have spent all your money or have not added money yet. "
                        + "Thanks for shopping!",
                vendingMachineTest.getChangeFromMachine());
    }
}

