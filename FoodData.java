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
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    
    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    private BPTree<Double, FoodItem> caloriesTree;
    private BPTree<Double, FoodItem> fatTree;
    private BPTree<Double, FoodItem> carbohydratesTree;
    private BPTree<Double, FoodItem> fiberTree;
    private BPTree<Double, FoodItem> proteinTree;
    
    /**
     * Public constructor
     */
    public FoodData() {
    	List<FoodItem> foodItemList = new ArrayList<FoodItem>();
    	this.foodItemList = foodItemList;
    	
    	HashMap<String, BPTree <Double, FoodItem>> indexes = new HashMap<String, BPTree <Double, FoodItem>>();
    	this.indexes = indexes;
    }
    
    
    /**
     * Loads data from the specified csv file
     * Constructs food items from parsed data
     * Adds food items to foodItemList
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
				
				//read data from the file
				while (input.hasNextLine()) { // move through file while there are additional lines
					oneLineOfData = input.nextLine();
					
					String id = null;
					String name = null;
					String calories = null;
					Double calorieCount = 0.0;
					String fat = null;
					Double fatGrams = 0.0;
					String carbohydrate = null;
					Double carbGrams = 0.0;
					String fiber = null;
					Double fiberGrams = 0.0;
					String protein = null;
					Double proteinGrams = 0.0;
					
				
					if (oneLineOfData.length() == 0) { // if line in file is blank, move to next one
						continue;
					} // END if length = 0
					try {
						String[] commaSplit = oneLineOfData.split(",");
						id = commaSplit[0]; // food item id is first element
						System.out.println("id is " + id);
						name = commaSplit[1]; // food item name is second element
						System.out.println("name is " + name);
						
						calories = commaSplit[2]; // calories label is third element
						System.out.println("calories is " + calories);
						calorieCount = Double.parseDouble(commaSplit[3]); // convert calories from String to Double
						System.out.println("calorie count is" + calorieCount);
						
						fat = commaSplit[4]; // fat label is fifth element
						System.out.println("fat is " + fat);
						fatGrams = Double.parseDouble(commaSplit[5]); // convert fat grams value from String to Double
						System.out.println("fat grams are" + fatGrams);
						
						carbohydrate = commaSplit[6]; // carbohydrate label is seventh element
						System.out.println("carbs are " + carbohydrate);
						carbGrams = Double.parseDouble(commaSplit[7]); // convert from String to Double
						System.out.println("carb grams are " + carbGrams);
						
						fiber = commaSplit[8]; // fiber label is ninth element
						System.out.println("fiber is " + fiber);
						fiberGrams = Double.parseDouble(commaSplit[9]); // convert from String to Double
						System.out.println("fiber grams are" + fiberGrams);
						
						protein = commaSplit[10]; // protein label is eleventh element
						System.out.println("protein is " + protein);
						proteinGrams = Double.parseDouble(commaSplit[11]); //convert from String to Double
						System.out.println("protein grams are " + proteinGrams);
					
					} // END inner TRY block
					
						// check if lines are badly formatted
					catch (NullPointerException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println("WARNING: Found incorrectly formatted line in file: " + oneLineOfData);
						continue;
					} // END inner CATCH block
					
					
						FoodItem foodItem = new FoodItem(id, name);		// create new food item 
						System.out.println("Food item " + name + " created");
						
						foodItem.addNutrient(calories, calorieCount); // add calories to HashMap
						foodItem.addNutrient(fat, fatGrams); // add fat to HashMap
						foodItem.addNutrient(carbohydrate, carbGrams); // add carbs to HashMap
						foodItem.addNutrient(fiber, fiberGrams); // add fiber to HashMap
						foodItem.addNutrient(protein, proteinGrams); // add protein to HashMap
						
						foodItemList.add(foodItem);  // add new Item to list
						
						caloriesTree.insert(calorieCount, foodItem);
						fatTree.insert(fatGrams, foodItem);
						carbohydratesTree.insert(carbGrams, foodItem);
						fiberTree.insert(fiberGrams, foodItem);
						proteinTree.insert(proteinGrams, foodItem);
					
			} // END while loop
			} // END if statement
		} // END outer TRY block
			
				catch (IOException  e) {
		   			System.out.println("WARNING: Could not load room contents from file "  + filePath + ".");
		   			}	// End CATCH block
		           
		           finally { // no matter what
		        	   if (input != null){
		   				input.close();
		   				}	
		   		   } // End FINALLY block			
    }

    /**
     * Searches food list for provided substring
     * @return new list nameFilter with matching food items
     */
    @Override
    public List<FoodItem> filterByName(String substring) { //me
        List<FoodItem> nameFilter = new ArrayList<>(); // will hold filtered list
    	
        if (substring != null) {
        
        for (int i = 0; i < foodItemList.size(); i++) {
        	if (foodItemList.get(i).getName().toLowerCase().contains(substring.toLowerCase())) {
        		nameFilter.add(foodItemList.get(i));
        		System.out.println("food item " + foodItemList.get(i).getName() + " added to filtered list");
        	}
        }
        
        }
        if (nameFilter.size() == 0) {
        	System.out.println("No foods found");
        }
        //System.out.println("Item 1 is " + nameFilter.get(0).getName());
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
    		
    		// break out the filter elements
    		String nutrient = filterElements[0].toLowerCase();
    		String comparator = filterElements[1];
    		double key = Double.parseDouble(filterElements[2]);
    		
    		// create the filter
    		BPTree<Double, FoodItem> tree = indexes.get(nutrient);
    		ArrayList<FoodItem> filteredFoods = (ArrayList<FoodItem>) tree.rangeSearch(key, comparator);
    		filteredFoodLists.add(filteredFoods);
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
     */
    @Override
    public void addFoodItem(FoodItem foodItem) { //me
        foodItemList.add(foodItem);
	
	caloriesTree.insert(foodItem.getNutrientValue("calories"), foodItem);
	fatTree.insert(foodItem.getNutrientValue("fat"), foodItem);
	carbohydratesTree.insert(foodItem.getNutrientValue("carbohydrates"), foodItem);
	fiberTree.insert(foodItem.getNutrientValue("fiber"), foodItem);
	proteinTree.insert(foodItem.getNutrientValue("protein"), foodItem);
    }

    /**
     * Gets list of all food items
     * @returns foodItemList
     */
    @Override
    public List<FoodItem> getAllFoodItems() { //me
        
    	return foodItemList;
    }
    
   

    /**
     * Save the list of food items in ascending order by name
     * 
     * @param filename name of the file where the data needs to be saved 
     */
	@Override
	public void saveFoodItems(String filename) { //me
		
		File newFoodFile = null; //file for saving food list to
		PrintStream writer = null; //used to write food list to file
		
			try {
				newFoodFile = new File(filename); 
				writer = new PrintStream(newFoodFile); 
			
				for (int f = 0; f < foodItemList.size(); ++f) {
					writer.print(foodItemList.get(f).getID()); // write id to file
					writer.print(", "); // separator
					writer.print(foodItemList.get(f).getName()); // write name to file
					writer.print(", calories, "); // write calories to file
					writer.print(foodItemList.get(f).getNutrientValue("calories")); // calorie value
					writer.print(", fat, ");
					writer.print(foodItemList.get(f).getNutrientValue("fat")); // fat value
					writer.print(", carbohydrate, ");
					writer.print(foodItemList.get(f).getNutrientValue("carbohydrate")); // carb value
					writer.print(", fiber, ");
					writer.print(foodItemList.get(f).getNutrientValue("fiber")); // fiber value
					writer.print(", protein, ");
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
	

	 public static void main(String [] args){
	     FoodData test = new FoodData();
		 
		 test.loadFoodItems("test.csv"); 
	     //System.out.println(test.foodItemList.get(0).getName());
	     //System.out.println(test.foodItemList.get(1).getName());
		 //test.filterByName(null);
		 
		// System.out.println("testing getAllFoodITems " + test.getAllFoodItems().get(0).getName());
		 
		 FoodItem chocolate = new FoodItem("chocolate");
		 test.addFoodItem(chocolate);
		 
		 System.out.println("testing add " + test.getAllFoodItems().get(2).getName());
		 
		 //test.getAllFoodItems().get(1)
		 
		 System.out.println(test.filterByName("soy").get(0).getName());
		 
		 
		 
		 System.out.println(test.getFoodNames(test.getAllFoodItems()) + "get food test");
		 
		// test.saveFoodItems("writetest.ddd");
	    }
}

