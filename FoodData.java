package application;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu), Amanda Weppler Ansel, and Kelly East
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    // Trees to store each nutrient value with the FoodItem
    private BPTree<Double, FoodItem> caloriesTree;
    private BPTree<Double, FoodItem> fatTree;
    private BPTree<Double, FoodItem> carbohydratesTree;
    private BPTree<Double, FoodItem> fiberTree;
    private BPTree<Double, FoodItem> proteinTree;
    
    /**
     * Public constructor
     */
    public FoodData() {
	
	// List of the food items
    	List<FoodItem> foodItemList = new ArrayList<FoodItem>();
    	this.foodItemList = foodItemList;
    	
	// Map of nutrients and corresponding index
    	HashMap<String, BPTree <Double, FoodItem>> indexes = new HashMap<String, BPTree <Double, FoodItem>>();
    	this.indexes = indexes;
    	
	// Trees to store nutrients and FoodItems
    	BPTree<Double, FoodItem> caloriesTree = new BPTree<Double, FoodItem>(3);
    	this.caloriesTree = caloriesTree;
        BPTree<Double, FoodItem> fatTree = new BPTree<Double, FoodItem>(3);
        this.fatTree = fatTree;
        BPTree<Double, FoodItem> carbohydratesTree = new BPTree<Double, FoodItem>(3);
        this.carbohydratesTree = carbohydratesTree;
        BPTree<Double, FoodItem> fiberTree = new BPTree<Double, FoodItem>(3);
        this.fiberTree = fiberTree;
        BPTree<Double, FoodItem> proteinTree = new BPTree<Double, FoodItem>(3);
        this.proteinTree = proteinTree;
        
	// Put nutrient trees into the Map when creating FoodData object
        indexes.put("calories", caloriesTree);
        indexes.put("fat", fatTree);
        indexes.put("carbohydrate", carbohydratesTree);
        indexes.put("fiber", fiberTree);
        indexes.put("protein", proteinTree);
    }
    
    
    /**
     * Loads data from the specified csv file
     * Constructs food items from parsed data
     * Adds food items to foodItemList
     * @param filepath is the user-entered filepath 
     * or the file to display on initial load
     * 
     */
    @Override
    public void loadFoodItems(String filePath) { //me
    	File inputFile = null; // File to read data from
		Scanner input = null; // Scanner to read from file
		String oneLineOfData = null; // next line of data from the file
		
		try {
			// Create a File and Scanner objects
			inputFile = new File(filePath);
			input = new Scanner(inputFile);
			
			if (input != null) {  // if file is not null
			foodItemList.clear(); //clears the list to start from scratch	
				
				//read data from the file
				while (input.hasNextLine()) { // move through file while there are additional lines
					oneLineOfData = input.nextLine();
					
					String id = null;  // ID for foodItem
					String name = null; // name for foodItem
					String calories = null; // Nutrient label "calories"
					Double calorieCount = 0.0; // value of calories nutrient
					String fat = null; // Nutrient label "fat"
					Double fatGrams = 0.0; // value of fat nutrient
					String carbohydrate = null; // Nutrient label "carbohydrate"
					Double carbGrams = 0.0; // value of carbohydrate nutrient
					String fiber = null; // Nutrient label "fiber"
					Double fiberGrams = 0.0; // value of fiber nutrient
					String protein = null; // Nutrient label of "protein"
					Double proteinGrams = 0.0; // value of protein nutrient
					
				
					if (oneLineOfData.length() == 0) { // if line in file is blank, move to next one
						continue;
					} // END if length = 0
					try {
						String[] commaSplit = oneLineOfData.split(",");
						id = commaSplit[0]; // food item id is first element
						
						name = commaSplit[1].trim(); // food item name is second element
						
						calories = commaSplit[2]; // calories label is third element
						// calorie value is fourth element; convert calories from String to Double
						calorieCount = Double.parseDouble(commaSplit[3]); 
						
						fat = commaSplit[4]; // fat label is fifth element
						// fat value is sixth element; convert fat grams value from String to Double
						fatGrams = Double.parseDouble(commaSplit[5]); 
						
						carbohydrate = commaSplit[6]; // carbohydrate label is seventh element
						// carbohydrate value is eighth element; convert value from String to Double
						carbGrams = Double.parseDouble(commaSplit[7]); 
						
						fiber = commaSplit[8]; // fiber label is ninth element
						// fiber value is tenth element; convert value from String to Double
						fiberGrams = Double.parseDouble(commaSplit[9]); 
						
						protein = commaSplit[10]; // protein label is eleventh element
						// protein value is twelfth element; convert value from String to Double
						proteinGrams = Double.parseDouble(commaSplit[11]); 
					
					} // END inner TRY block
					
						// check if lines are badly formatted
					catch (NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println("WARNING: Found incorrectly formatted line in file: " + oneLineOfData);
						continue;
					} // END inner CATCH block
					
				// check for duplicate names; if duplicate append "*" to distinguish names
					for (int n = 0; n < foodItemList.size(); ++n) {
						if (foodItemList.get(n).getName().equals(name) && 
								!foodItemList.get(n).getID().equals(id)) {
							name = name + "*";
						}
					}

						FoodItem foodItem = new FoodItem(id, name);		// create new food item 
						System.out.println("Food item " + name + " created");
						
						foodItem.addNutrient(calories, calorieCount); // add calories to HashMap
						foodItem.addNutrient(fat, fatGrams); // add fat to HashMap
						foodItem.addNutrient(carbohydrate, carbGrams); // add carbs to HashMap
						foodItem.addNutrient(fiber, fiberGrams); // add fiber to HashMap
						foodItem.addNutrient(protein, proteinGrams); // add protein to HashMap
						
						foodItemList.add(foodItem);  // add new Item to list
						
						// Add FoodItem and nutrient value to corresponding nutrient trees
						caloriesTree.insert(calorieCount, foodItem);
						fatTree.insert(fatGrams, foodItem);
						carbohydratesTree.insert(carbGrams, foodItem);
						fiberTree.insert(fiberGrams, foodItem);
						proteinTree.insert(proteinGrams, foodItem);
	
					
			} // END while loop
			} // END if statement
		} // END outer TRY block
			
				catch (IOException  e) {
		   			System.out.println("WARNING: Could not load food from file "  + filePath + ".");
		   			}	// End CATCH block
		           
		           finally { // no matter what
		        	   if (input != null){
		   				input.close();
		   				}	
		   		   } // End FINALLY block			
    }

    /**
     * Searches food list for provided substring
     * @param substring is provided by user
     * @return new list nameFilter with matching food items
     * if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByName(String substring) { 
        List<FoodItem> nameFilter = new ArrayList<>(); // will hold filtered list
    	
        if (substring != null) { // if substring has value
        
	// search through food list for provided string 
        for (int i = 0; i < foodItemList.size(); i++) {
        	if (foodItemList.get(i).getName().toLowerCase().contains(substring.toLowerCase())) {
			// if found, add food to name filtered list
        		nameFilter.add(foodItemList.get(i));
      		} // END IF
        } // END FOR
        
        } // END IF !null

        return nameFilter;
    }

    /**
     * Gets all the food items that fulfill ALL the provided rules
     *
     * Format of a rule:
     *     "<nutrient> <comparator> <value>"
     * 
     * Definition of a rule:
     *     A rule is a string which has three parts separated by a space:
     *         1. <nutrient>: Name of one of the 5 nutrients [CASE-INSENSITIVE]
     *         2. <comparator>: One of the following comparison operators: <=, >=, ==
     *         3. <value>: a double value
     * 
     * Note:
     *     1. Multiple rules can contain the same nutrient.
     *         E.g. ["calories >= 50.0", "calories <= 200.0", "fiber == 2.5"]
     *     2. A FoodItemADT object MUST satisfy ALL the provided rules i
     *        to be returned in the filtered list.
     *
     * @param rules list of rules
     * @return list of filtered food items; if no food item matched, return empty list
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) { 
    	// handle an empty food list
    	if (foodItemList.size() == 0) {
    		return null;
    	}
	
	// handle an empty rule list
	if (rules.size() == 0) {
    		return getAllFoodItems();
    	} 
    	
    	// master food list
    	ArrayList<FoodItem> masterFoodList = new ArrayList<FoodItem>();
    	
    	// list of filteredFoodLists
    	ArrayList<ArrayList<FoodItem>> filteredFoodLists = new ArrayList<ArrayList<FoodItem>>();
    	
    	// setup delimiter
    	String delims = "[ ]";
    	
    	// start looping through list
    	for (int i = 0; i < rules.size(); i ++) {
    		
    		// parse this rule
    		String ruleForEval = rules.get(i);
    		String[] filterElements = ruleForEval.split(delims);
    		
    		// handle name filter
    		if (filterElements[0].equals("Name")) {
    			if (filterElements.length > 3) {
    				return null;
    			}
    			String nameFilter = filterElements[2];
    			filteredFoodLists.add((ArrayList<FoodItem>) filterByName(nameFilter));
    		}
		
		else {
    		
    		// break out the nutrition filter elements
    		String nutrient = filterElements[0].toLowerCase();
    		String comparator = filterElements[1];
    		double key = Double.parseDouble(filterElements[2]);
    		
    		// create the nutrition filter
    		BPTree<Double, FoodItem> tree = indexes.get(nutrient);
    		ArrayList<FoodItem> filteredFoods = (ArrayList<FoodItem>) tree.rangeSearch(key, comparator);
    		filteredFoodLists.add(filteredFoods);
		}
    	}
    	
    	for (FoodItem i : filteredFoodLists.get(0)) {
    		boolean quit = false;
    		for (int j = 1; j < filteredFoodLists.size() && !quit; j++) {
    			if (!filteredFoodLists.get(j).contains(i)) {
    				quit = true;
    			}
    		}
    		
    		if (!quit) {
    			masterFoodList.add(i);
    		}
    	}
    	
        return masterFoodList;
    }


    /**
     * Adds provided food item to foodItemList
     * @param foodItem the food item instance to be added
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
 
    	// Add item to food list
    	foodItemList.add(foodItem);
        
	// Add nutrients from item to nutrient trees
        caloriesTree.insert(foodItem.getNutrientValue("calories"), foodItem);
	fatTree.insert(foodItem.getNutrientValue("fat"), foodItem);
	carbohydratesTree.insert(foodItem.getNutrientValue("carbohydrate"), foodItem);
	fiberTree.insert(foodItem.getNutrientValue("fiber"), foodItem);
	proteinTree.insert(foodItem.getNutrientValue("protein"), foodItem);
        
    }

    /**
     * Gets list of all food items
     * @returns foodItemList
     */
    @Override
    public List<FoodItem> getAllFoodItems() { 
        
    	return foodItemList;
    }


    /**
     * Save the list of food items in ascending order by name
     * @param filename name of the file where the data needs to be saved 
     */
	@Override
	public void saveFoodItems(String filename) { 
		
		File newFoodFile = null; // file for saving food list to
		PrintStream writer = null; // used to write food list to file
		
			try {
				newFoodFile = new File(filename); 
				writer = new PrintStream(newFoodFile); 
			
				for (int f = 0; f < foodItemList.size(); ++f) {
					writer.print(foodItemList.get(f).getID()); // write id to file
					writer.print(", "); // separator
					writer.print(foodItemList.get(f).getName()); // write name to file
					writer.print(", calories, "); // write calories label to file
					writer.print(foodItemList.get(f).getNutrientValue("calories")); // calorie value
					writer.print(", fat, "); // write fat label to file
					writer.print(foodItemList.get(f).getNutrientValue("fat")); // fat value
					writer.print(", carbohydrate, "); // write carbohydrate label to file
					writer.print(foodItemList.get(f).getNutrientValue("carbohydrate")); // carb value
					writer.print(", fiber, "); // write fiber label to file
					writer.print(foodItemList.get(f).getNutrientValue("fiber")); // fiber value
					writer.print(", protein, "); // write protein label to file
					writer.println(foodItemList.get(f).getNutrientValue("protein")); // protein value
				
				} // END FOR block
			}// END TRY block
			
		catch (java.io.FileNotFoundException f) {  // catch file problems
			System.out.println("WARNING: Could not save food items to file" + filename);
		} finally {
			if (writer != null) // if statement checks for null pointer
				writer.close();  // close the file
			} // END FINALLY block
}
	

	// public static void main(String [] args){
	     
	//    }
	 
	    
}
