package com.techelevator;

import com.techelevator.view.Menu;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String MAIN_MENU_OPTION_HIDDEN = "";
	private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS,
			MAIN_MENU_OPTION_PURCHASE,
			MAIN_MENU_OPTION_EXIT,
			MAIN_MENU_OPTION_HIDDEN};

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY,
			PURCHASE_MENU_OPTION_SELECT_PRODUCT,
			PURCHASE_MENU_OPTION_FINISH_TRANSACTION};

	private static final BigDecimal FEED_MONEY_MENU_1_DOLLAR = new BigDecimal("1.00");
	private static final BigDecimal FEED_MONEY_MENU_2_DOLLAR = new BigDecimal("2.00");
	private static final BigDecimal FEED_MONEY_MENU_5_DOLLAR = new BigDecimal("5.00");
	private static final BigDecimal FEED_MONEY_MENU_10_DOLLAR = new BigDecimal("10.00");
	private static final BigDecimal FEED_MONEY_MENU_20_DOLLAR = new BigDecimal("20.00");
	private static final BigDecimal[] FEED_MONEY_MENU_OPTIONS = {FEED_MONEY_MENU_1_DOLLAR,
			FEED_MONEY_MENU_2_DOLLAR,
			FEED_MONEY_MENU_5_DOLLAR,
			FEED_MONEY_MENU_10_DOLLAR,
			FEED_MONEY_MENU_20_DOLLAR};

	private static final String FEED_MONEY_CONTINUE = "Add more money.";
	private static final String FEED_MONEY_EXIT = "I am done.";
	private static final String[] FEED_MONEY_EXIT_MENU_OPTIONS = {FEED_MONEY_CONTINUE,
			FEED_MONEY_EXIT};

	private Menu menu;
	private VendingMachine vendingMachine;

	public VendingMachineCLI(Menu menu, VendingMachine vendingMachine) {
		this.menu = menu;
		this.vendingMachine = vendingMachine;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				System.out.println();
				System.out.println("***All Products***");
				displayVendingMachineItems(vendingMachine, false);
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				purchaseMenuRun(menu, vendingMachine);
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Thank you for shopping, see you next time!");
				return;
			} else if (choice.equals(MAIN_MENU_OPTION_HIDDEN)) {
				boolean created = vendingMachine.hiddenSalesLog();
				if (created) {
					System.out.println("Sales report created successfully!");
				}
			}
		}
	}

	public static void purchaseMenuRun(Menu menu, VendingMachine vendingMachine) {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			if (choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				feedMoney(menu, vendingMachine);
			} else if (choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
				selectProduct(menu, vendingMachine);
			} else if (choice.equals(PURCHASE_MENU_OPTION_FINISH_TRANSACTION)) {
				finishTransaction(menu, vendingMachine);
				return;
			}
		}
	}

	public static void displayVendingMachineItems(VendingMachine vendingMachine, boolean considerStock) {
		System.out.println("Slot Location       Product Name        "
				+ "Price               Type                Stock");
		System.out.println(String.join("", Collections.nCopies(88, "=")));
		for (Item item : vendingMachine.getItems()) {
			int itemStock = item.getStock();
			String itemPosition = item.getPosition();
			String itemName = item.getName();
			String itemPrice = "$" + item.getPrice();
			String itemType = item.getType();
			String itemStockStr = itemStock > 0 ? String.valueOf(itemStock) : "SOLD OUT";

			if (!considerStock) {
				System.out.println(itemPosition + generateSpace(itemPosition) +
						itemName + generateSpace(itemName) +
						itemPrice + generateSpace(itemPrice) +
						itemType + generateSpace(itemType) +
						itemStockStr);
			} else {
				if (itemStock > 0) {
					System.out.println(itemPosition + generateSpace(itemPosition) +
							itemName + generateSpace(itemName) +
							itemPrice + generateSpace(itemPrice) +
							itemType + generateSpace(itemType) +
							itemStockStr);
				}
			}
		}
	}

	public static String generateSpace(String str) {
		int spaceLength = 20 - str.length();
		String space = "";

		for (int i = 1; i <= spaceLength; i++) {
			space += " ";
		}

		return space;
	}

	public static void feedMoney(Menu menu, VendingMachine vendingMachine) {
		System.out.println("Hi there! Please insert a bill, "
				+ "we only accept $1, $2, $5, $10, $20, thank you!");
		BigDecimal choice = (BigDecimal) menu.getChoiceFromOptions(FEED_MONEY_MENU_OPTIONS);

		if (choice.equals(FEED_MONEY_MENU_1_DOLLAR) ||
				choice.equals(FEED_MONEY_MENU_2_DOLLAR) ||
				choice.equals(FEED_MONEY_MENU_5_DOLLAR) ||
				choice.equals(FEED_MONEY_MENU_10_DOLLAR) ||
				choice.equals(FEED_MONEY_MENU_20_DOLLAR)) {
			vendingMachine.addMoneyToMachine(choice);
			System.out.println();
			System.out.println("$" + choice + " added to your balance successfully!");
		}

		System.out.println();
		System.out.println("Current Money Provided: $" + vendingMachine.getUserBalance());
		vendingMachine.log(VendingMachine.FEED_MONEY, choice);
		System.out.println();
		System.out.println("Would you like to add more money?");

		String feedMore = (String) menu.getChoiceFromOptions(FEED_MONEY_EXIT_MENU_OPTIONS);

		if (feedMore.equals(FEED_MONEY_CONTINUE)) {
			feedMoney(menu, vendingMachine);
		}

	}

	public static void selectProduct(Menu menu, VendingMachine vendingMachine) {
		System.out.println();
		System.out.println("***Available Products***");
		displayVendingMachineItems(vendingMachine, true);

		System.out.println();
		System.out.println("Please enter product code:");
		String choice = menu.collectUserInput();

		if (!vendingMachine.itemAtPositionExists(choice)) {
			System.out.println();
			System.out.println("Product does not exist.");
		} else if (vendingMachine.getItemStock(choice) == 0) {
			System.out.println();
			System.out.println("Product is sold out.");
		} else {
			if (vendingMachine.getUserBalance().compareTo(vendingMachine.getItemPrice(choice)) >= 0) {
				vendingMachine.decrementItemAtPosition(choice);
				BigDecimal itemPrice = vendingMachine.getItemPrice(choice);
				vendingMachine.dockMoneyOnMachine(itemPrice);

				String itemName = vendingMachine.getItemName(choice);
				BigDecimal userBalance = vendingMachine.getUserBalance();
				String itemMessage = vendingMachine.getItemMessage(choice);
				System.out.println();
				System.out.println("You just purchased " + itemName + " for $" + itemPrice + ".");
				System.out.println("Your current balance is $" + userBalance + ".");
				System.out.println(itemMessage);
				vendingMachine.log(choice);
			} else {
				System.out.println();
				System.out.println("Insufficient balance, please add more money.");
			}
		}
	}

	public static void finishTransaction(Menu menu, VendingMachine vendingMachine) {
		String finishMessage = vendingMachine.getChangeFromMachine();
		System.out.println(finishMessage);
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachine vendingMachine = new VendingMachine();
		VendingMachineCLI cli = new VendingMachineCLI(menu, vendingMachine);
		cli.run();
	}
}