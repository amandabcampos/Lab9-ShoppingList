import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
 * 
 * @author amandabcampos
 *
 */

public class ShoppingList {
	
	private static List<String> orderKeys = new ArrayList<>();
	private static List<Double> orderValues = new ArrayList<>();
	private static List<Integer> orderQuantities = new ArrayList<>();
	
	public static void main(String[] args) {
		
		Scanner scnr = new Scanner(System.in);	
		
		Map<String, Double> foodItems = new HashMap<>();
		
		foodItems.put("apple", 0.99);	
		foodItems.put("banana", 0.59);	
		foodItems.put("cauliflower", 1.59);	
		foodItems.put("dragonfruit", 2.19);	
		foodItems.put("elderberry", 1.79);	
		foodItems.put("figs", 2.09);	
		foodItems.put("grapefruit", 1.99);	
		foodItems.put("honeydew", 3.49);	
		
		System.out.println("Welcome to Guenther's Market!");
		
		boolean looping = true;
		while (looping) {
			
			printFoodItems(foodItems);
			
			System.out.println("What item would you like to order? (enter name or number)");
			
			String itemName = scnr.nextLine().toLowerCase();
				
			System.out.println("How many " + itemName + "s?");
			try {	
				Integer itemQuantity = scnr.nextInt();
				scnr.nextLine();
				
				if (addItemToOrder(foodItems, itemName, itemQuantity, orderQuantities)) {	
					System.out.println("Would you like to order anything else (y/n)?");
					if (scnr.nextLine().toLowerCase().startsWith("n")) {
						looping = false;
					}
				}
			} catch(InputMismatchException e) {
				System.out.println("Please try again");
				scnr.nextLine();
			}
		}
			
		printOrder(orderKeys, orderValues, orderQuantities);
		printAveragePricePerItem(orderValues, orderQuantities);
		
		System.out.println("Your most expensive item is the " + orderKeys.get(getHighestCost(orderValues)) + " at $" + orderValues.get(getHighestCost(orderValues)));
		System.out.println("Your least expensive item is the " + orderKeys.get(getLowestCost(orderValues)) + " at $" + orderValues.get(getLowestCost(orderValues)));
		
		System.out.print("Thanks for your order!");
		
		scnr.close();
		
	}
	
	public static void printFoodItems(Map<String,Double> fruitsList) { 
		System.out.println("--------------- MENU -----------------");
		System.out.printf("%16s", "Item");
		System.out.printf("%12s %n", "Price");
		System.out.printf("%24s %n", "____________");
		int i=1;													
		for (String fruitsName: fruitsList.keySet()) {
			System.out.printf("%1s", i);
			System.out.printf("%15s", fruitsName);    
			System.out.printf("%11s %n", fruitsList.get(fruitsName));
			i++;
		}
		System.out.println("--------------------------------------");
	}
	
	public static boolean addItemToOrder(Map<String,Double> itemsMap, String itemName, Integer itemQuantity, List<Integer> orderQuantities) {
			
		String realItemName = "";
		
		if (isInteger(itemName)) {
			
			if (Integer.decode(itemName) > itemsMap.keySet().size() || Integer.decode(itemName) < 1) {
				System.out.println("Sorry, we don't have those/Invalid amount.  Please try again.");
				return false;
			} else {
			
				int i=0;
				for (String items : itemsMap.keySet()) {
					realItemName = items;
					i++;
					if (i == Integer.decode(itemName)){
						break;
					}
				}
				itemName = realItemName;	
			}	
		}
		
		if (itemsMap.containsKey(itemName) && itemQuantity >0) {
			if (isInOrderList(orderKeys, itemName)<0) {
				orderKeys.add(itemName);
				orderValues.add(itemsMap.get(itemName));
				orderQuantities.add(itemQuantity);
			} else {
				int index = isInOrderList(orderKeys, itemName);
				int oldQuantity = orderQuantities.get(index);
				int newQuantity = oldQuantity + itemQuantity;
				orderQuantities.remove(index);
				orderQuantities.add(index, newQuantity);
			}
			System.out.println("Adding " + itemQuantity + " " + itemName + "s to cart at $" + itemsMap.get(itemName) + " each");
			return true;
		}
		System.out.println("Sorry, we don't have those/Invalid amount.  Please try again.");
		return false;
	}
	
	public static int isInOrderList(List<String> orderKeys, String itemName) {
		for (int i=0; i<orderKeys.size(); i++) {
			if (orderKeys.get(i).equals(itemName)) {
				return i;
			}
		}
		return -1;
	}
	
	public static void printOrder(List<String> orderKeys, List<Double> orderValues, List<Integer> orderQuantities) { 
		System.out.println("----------------- YOUR ORDER -------------------");
		System.out.printf("%15s", "*Item*");
		System.out.printf("%15s", "*Quantity*");
		System.out.printf("%15s %n", "*Price Each*");
		
		for (int i=0; i<orderKeys.size(); i++) {
			
			System.out.printf("%15s", orderKeys.get(i)); 
			System.out.printf("%15s", orderQuantities.get(i));
			System.out.printf("%15s %n", "$" + orderValues.get(i)); 
			
		}
		System.out.println("------------------------------------------------");
	}
	
	public static void printAveragePricePerItem(List<Double> orderValues, List<Integer> orderQuantities) {
		double sum = 0;
		int amountItems = 0;

		for (int i=0; i<orderValues.size(); i++) {
			sum += (orderValues.get(i) * orderQuantities.get(i));
			amountItems += orderQuantities.get(i);
			
		}
		System.out.print("Your total is: $");
		System.out.printf("%.2f\n", sum);
		System.out.print("Average price per item in order was $");	
		System.out.printf("%.2f\n", sum/amountItems);

	}
	
	public static int getHighestCost(List<Double> orderValues) {
		double highest = Double.MIN_VALUE;
		int indexHighest = 0;
		for (int i=0; i<orderValues.size(); i++) {
			if (orderValues.get(i) > highest) {
				highest = orderValues.get(i);
				indexHighest = i;
			}
		}
		return indexHighest;
	}
	
	public static int getLowestCost(List<Double> orderValues) {
		double lowest = Double.MAX_VALUE;
		int indexLowest = 0;
		for (int i=0; i<orderValues.size(); i++) {
			if (orderValues.get(i) < lowest) {
				lowest = orderValues.get(i);
				indexLowest = i;
			}
		}
		return indexLowest;
	}
	
	public static boolean isInteger(String input) { 
		if (input.matches("\\d+")) {
			return true;
		}
		return false;
	}
	
	
}
