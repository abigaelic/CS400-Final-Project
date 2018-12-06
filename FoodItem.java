import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents a food item with all its properties.
 * 
 * @author Amanda Weppler Ansel
 */
public class FoodItem {
    // The name of the food item.
    private String name;

    // The id of the food item.
    private String id;

    // Map of nutrients and value.
    private HashMap<String, Double> nutrients;
    
    /**
     * Constructor
     * @param name name of the food item
     * @param id unique id of the food item 
     */
    public FoodItem(String id, String name) {
        this.id = id;
        this.name = name;
        
        HashMap<String, Double> nutrients = new HashMap<String, Double>();
        this.nutrients = nutrients;
    }
    
    /**
     * Constructor
     * @param name name of the food item
     */
    
    public FoodItem(String name) {
    	this.name = name;
    	String id = new String();
    	this.id = id;
    	
    	HashMap<String, Double> nutrients = new HashMap<String, Double>();
        this.nutrients = nutrients;
    }
    
    /**
     * Default constructor
     */
    
    public FoodItem() {
    	String name = new String();
    	this.name = name;
    	String id = new String();
    	this.id = id;
    	
    	HashMap<String, Double> nutrients = new HashMap<String, Double>();
        this.nutrients = nutrients;
    }
    
    /**
     * Gets the name of the food item
     * 
     * @return name of the food item
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets name for FoodItem object
     * @param newName
     */
    
    public void setName(String newName) {
    	name = newName;
    }

    /**
     * Gets the unique id of the food item
     * 
     * @return id of the food item
     */
    public String getID() {
        
        return id;
    }
    
    public void setID(String newId) {
    	id = newId;
    }
    
    /**
     * Gets the nutrients of the food item
     * 
     * @return nutrients of the food item
     */
    public HashMap<String, Double> getNutrients() {
        
        return nutrients;
    }

    /**
     * Adds a nutrient and its value to this food. 
     * If nutrient already exists, updates its value.
     */
    public void addNutrient(String name, double value) {
        
    	if (name != null) { // if nutrient name is not null
    		
    		if (nutrients.containsKey(name)) { // if nutrient already present and mapped, update value
    			nutrients.replace(name, value);
    			System.out.println("nutrient values updated. Value = " + nutrients.get(name));
    		}
    		
    		else {  // add new nutrient and value
    			nutrients.put(name, value);
    			System.out.println("nutrient values added. Value = " + nutrients.get(name));
    		}

    	}
    }

    /**
     * Returns the value of the given nutrient for this food item. 
     * If not present, then returns 0.
     */
    public double getNutrientValue(String name) {
    	
    	if (nutrients.containsKey(name)) { // if nutrient is present
    		return nutrients.get(name); // return nutrient value
    		
    	}
    	else {
    		return 0;   // return 0 if not present in map
    	}
    	
    	
    	
    	
    }
    
    public static void main(String [] args){
    	FoodItem chocolate = new FoodItem("one","chocolate");
    	
    	//chocolate.addNutrient("fat", 4.5);
    	//System.out.println(chocolate.getNutrients().toString());
    	
    	//System.out.println(chocolate.getNutrientValue("fat"));
    	
    	FoodItem testOrange = new FoodItem();
    	testOrange.setName("orange");
    	
    	System.out.println(testOrange.getName());
    	
    	chocolate.setID(chocolate.toString());
    	System.out.println(chocolate.getID());
    }
}

