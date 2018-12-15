/**
 * Filename:   Main.java
 * Project:    Team Project
 * Authors:    Abby Fry, CS400 @ Epic
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:  12/2 midnight
 * Version:    1.0
 * 
 * Credits:   Mary Alice Holmes, Kelly East, Amanda Weppler
 * 
 * Bugs:      no known bugs
 */
package application;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;
import java.util.HashMap;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollBar;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.beans.binding.Bindings;


public class Main extends Application {

	protected static final FoodData foodData = new FoodData();
	static ObservableList<String> foodList = FXCollections.observableArrayList();
	static ObservableList<String> mealList = FXCollections.observableArrayList();
	static ObservableList<String> appliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> unappliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> allFilterList = FXCollections.observableArrayList();
	Label foodNumber = new Label();
	
	String selectedMealItem = null;
	String stringFilter = null;
	
	// Map to store food names with corresponding items
	static private HashMap<String,FoodItem> nameMap = new HashMap<String,FoodItem>();

	/**This method launches the main window and 
	 * calls other methods which launch sections of 
	 * the window
	 * 
	 * @param the main stage
	 */
	@Override
	public void start(Stage primaryStage) {
		// Stage and Scene

		primaryStage.setTitle("Meal Planner");
		BorderPane bPane = new BorderPane();
		bPane.setPrefSize(2000, 1000);

		//testing
		
		//TOP
		BorderPane top = new BorderPane();
		top.setPrefSize(2000,150);

		Rectangle rectangle = new Rectangle(2000,150,Color.rgb(245, 163, 173));
		rectangle.relocate(0,0);
		top.getChildren().addAll(rectangle);

		//TOP TITLE
		Text t2 = newHeaderText("FOOD LIST", 160, 255, 226, 206);
		t2.setX(20.0f);
		t2.setY(140.0f);
		t2.setCache(true);

		top.getChildren().add(t2);
		StackPane.setAlignment(t2, Pos.TOP_LEFT);

		// MEAL & FOOD LIST
		bPane.setTop(top);
		bPane.setRight(setRight()); //set up Meal section
		bPane.setCenter(setLeft()); //set up Food List section

		Scene mainScene = new Scene(bPane, 1600, 900, Color.rgb(158, 53, 74));
		
		primaryStage.setScene(mainScene);
		primaryStage.show();   

	}

	/**This helper method builds the Food List
	 * side of the main window.
	 * @return the left side BorderPane
	 */
	private BorderPane setLeft() {
		//MEAL
		BorderPane toReturn = new BorderPane();
		toReturn.setPrefSize(1325,850);
		toReturn.autosize();
		toReturn.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));

		// START add Filter Buttons and call pop-up helper methods
		BorderPane top = new BorderPane();
		top.setPrefHeight(80);
		HBox hbox = addHBox();
		hbox.setPrefHeight(80);

		Button buttonDelete = newButton("Delete A Filter",300, 20);	
		buttonDelete.setOnMouseReleased(e -> deleteAFilterPopUp());
		Button buttonClear = newButton("Clear All Filters",300, 20);
		buttonClear.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent e) {
				appliedFilterList.clear();
				unappliedFilterList.clear();
				allFilterList.clear();
				getFoodNames(foodData.filterByNutrients(appliedFilterList));
			}
		});

		Button buttonFoodNameFilter = newButton("Add Food Name Filter",300, 20);	
		buttonFoodNameFilter.setOnMouseReleased(e -> foodNameFilterPopUp());
		Button buttonNutritionalFilter = newButton("Add Nutritional Filter",300, 20);	
		buttonNutritionalFilter.setOnMouseReleased(e -> nutritionalFilterPopUp());
		Button buttonViewFilter = newButton("View All Filters",300, 20);
		buttonViewFilter.setOnMouseReleased(e -> filterPopUp());

		hbox.getChildren().addAll(buttonClear,buttonDelete, buttonFoodNameFilter, buttonNutritionalFilter, buttonViewFilter);
		toReturn.setTop(hbox);
		// END add Filter Buttons

		// START add Food List Buttons and call pop-up helper methods
		BorderPane bottom = new BorderPane();
		bottom.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		bottom.setPrefHeight(50);
		HBox hbox2 = addHBox();

		Button buttonSaveFoodList = newButton("Save Food List to File",200, 20);
		buttonSaveFoodList.setOnMouseReleased(e -> saveToFilePopUp());
		Button buttonNewFoodList = newButton("Load New Food List",200, 20);
		buttonNewFoodList.setOnMouseReleased(e -> loadFromFilePopUp());
		Button buttonNewFoodItem = newButton("Add New Food Item",200, 20);
		buttonNewFoodItem.setOnMouseReleased(e -> newFoodItemPopUp());

		hbox2.getChildren().addAll(buttonSaveFoodList,buttonNewFoodList,buttonNewFoodItem);
		bottom.setLeft(hbox2);

		//number of food items label
		foodNumber.setText(0 + " food(s)");
		if (!foodList.isEmpty()) {
			foodNumber.textProperty().bind(Bindings.size(foodList).asString("%s food(s)"));
		}
		foodNumber.setAlignment(Pos.CENTER_LEFT);
		foodNumber.setTextFill(Color.rgb(255, 226, 206));
		BorderPane bottomRight = new BorderPane();
		bottomRight.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(15,12,15,12))));
		bottomRight.setPrefSize(200,50);
		bottomRight.setLeft(foodNumber);
		bottom.setRight(bottomRight);

		toReturn.setBottom(bottom);
		// END add food List Buttons

		// START create food list
		BorderPane leftCenter = new BorderPane();
		leftCenter.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));

		// margins
		BorderPane leftLeft = new BorderPane();
		leftLeft.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		leftLeft.setPrefWidth(50);
		BorderPane leftRight = new BorderPane();
		leftRight.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		leftRight.setPrefWidth(100);

		// add and remove buttons in right margin
		VBox leftRightVbox = addVBox();
		leftRightVbox.setPrefWidth(75);
		Button add = newButton("  Add    --->", 75, 50);
		add.setTextAlignment(TextAlignment.CENTER);
		Button remove =  newButton("<---  Remove", 75, 50);
		remove.setTextAlignment(TextAlignment.CENTER);
		Button clearMeal =  newButton("Clear Meal", 75, 50);
		clearMeal.setTextAlignment(TextAlignment.CENTER);
		leftRightVbox.getChildren().addAll(add, remove, clearMeal);
		leftRight.setCenter(leftRightVbox);

		//creating food list view
		ListView<String> foodListPane = new ListView<>();
		foodListPane.setItems(foodList);
		foodListPane.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		foodListPane.setPrefWidth(900);
		foodListPane.autosize();

		// creating actions for Add
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String foodToAdd = null;

				if (foodListPane.getSelectionModel().getSelectedItem() != null) {
					foodToAdd = foodListPane.getSelectionModel().getSelectedItem().toString();
					foodListPane.getSelectionModel().clearSelection();
				}

				if (foodToAdd != null) {
					mealList.add(foodToAdd);
					foodToAdd = null;
				}
			}
		});

		// creating actions for Remove
		remove.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if (selectedMealItem != null) {
					mealList.remove(selectedMealItem);
					selectedMealItem = null;
				}
			}
		});

		// creating actions for Clear Meal
		clearMeal.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				mealList.clear();
			}
		});

		leftCenter.setCenter(foodListPane);
		leftCenter.setLeft(leftLeft);
		leftCenter.setRight(leftRight);

		toReturn.setCenter(leftCenter);

		return toReturn;
	}
	/**This helper method builds the Meal List
	 * side of the main window.
	 * @return the right side BorderPane
	 */
	private BorderPane setRight() {
		// MEAL
		BorderPane right = new BorderPane();
		right.setPrefSize(600,850);
		right.autosize();
		Rectangle rectangle2 = new Rectangle(1000,900,Color.rgb(249, 201, 207));
		rectangle2.relocate(0,0);
		rectangle2.autosize();
		right.getChildren().addAll(rectangle2);

		// Header
		Text t3 = newHeaderText("MEAL", 60, 105, 10, 21);
		t3.setTextAlignment(TextAlignment.CENTER);
		t3.setCache(true);
		BorderPane top = new BorderPane();
		top.setPrefHeight(75);
		top.setCenter(t3);
		right.setTop(top);

		// View Meal Summary
		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		Button buttonViewMeal = newButton("View Meal Info", 300, 20);
		buttonViewMeal.setAlignment(Pos.BASELINE_CENTER);
		buttonViewMeal.setOnMouseReleased(e -> mealPopUp());
		toAdd.setCenter(buttonViewMeal);
		right.setBottom(toAdd);

		// Meal List
		ListView<String> mealListPane = new ListView<>();
		mealListPane.setItems(mealList);
		mealListPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
		mealListPane.setPrefWidth(600);
		mealListPane.autosize();

		// set class variable to access the Selected Meal Item elsewhere
		mealListPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				if (mealListPane.getSelectionModel().getSelectedItem() != null) {
					selectedMealItem = mealListPane.getSelectionModel().getSelectedItem().toString();
				}

			}
		});

		BorderPane rightPane = new BorderPane();
		rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
		rightPane.setPadding(new Insets (0,15,0,15));

		rightPane.setCenter(mealListPane);

		right.setCenter(rightPane);

		return right;
	}

	/**This helper method builds the Delete A 
	 * Filter Pop-Up window which is called
	 * by clicking the Delete button on the Food 
	 * List side of the screen
	 * 
	 */
	private void deleteAFilterPopUp() {
		Stage deleteStage = new Stage();
		deleteStage.setTitle("Delete a Filter");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,300);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
		
		//TITLE
		Text t4 = newHeaderText("FILTERS", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);
		
		// START View all created filters list
		ListView<String> filterListPane = new ListView<>();
		filterListPane.setItems(allFilterList);
		filterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
		filterListPane.setPrefWidth(300);
		filterListPane.autosize();
		popUp.setCenter(filterListPane);
		//END
		
		// START create buttons and assign actions
		HBox toAdd2 = addHBox();
		toAdd2.setPrefHeight(50);
		toAdd2.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
		toAdd2.alignmentProperty().set(Pos.CENTER);
		Button buttonDeleteFilter = newButton("Delete Filter", 200, 20);

		buttonDeleteFilter.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				String filterToDelete = null;

				if (filterListPane.getSelectionModel().getSelectedItem() != null) {
					filterToDelete = filterListPane.getSelectionModel().getSelectedItem().toString();
					filterListPane.getSelectionModel().clearSelection();
				}

				if (filterToDelete != null) {
					allFilterList.remove(filterToDelete);
					unappliedFilterList.remove(filterToDelete);
					appliedFilterList.remove(filterToDelete);
					System.out.println(appliedFilterList.size());
					getFoodNames(foodData.filterByNutrients(appliedFilterList)); 
					filterToDelete = null;
				}
			}
		});

		Button buttonClose = newButton("Close",200,20);
		buttonClose.setOnMouseReleased(e -> deleteStage.close());
		// END create buttons and assign actions
		
		toAdd2.getChildren().addAll(buttonDeleteFilter,buttonClose);
		popUp.setBottom(toAdd2);

		Scene scene = new Scene(popUp, 300, 300, Color.rgb(255, 239, 229));
		deleteStage.setScene(scene);
		deleteStage.show();
	}

	/**This helper method builds the Nutritional
	 * Info window for the meal window.
	 * 
	 * It opens an empty window if the mealList is
	 * empty.
	 * 
	 */
	private void mealPopUp() {
		Stage mealStage = new Stage();
		mealStage.setTitle("Meal Nutritional Summary");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,600);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));

		//TITLE
		Text t4 = newHeaderText("NUTRITIONAL SUMMARY", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		if (!mealList.isEmpty()) {
			// Call the nutritional summary methods to print to screen
		
			double [] totalValues = analyzeMeal(mealList);
			double totalCalories = totalValues[0];
			double totalFat = totalValues[1];
			double totalCarbs = totalValues[2];
			double totalFiber = totalValues[3];
			double totalProtein = totalValues[4];		
			
			BorderPane summary = new BorderPane();
			BorderPane top = new BorderPane();
			top.setPrefHeight(50);
			BorderPane bottom = new BorderPane();
			bottom.setPrefHeight(50);
			BorderPane middle = new BorderPane();
			BorderPane s1 = new BorderPane();
			BorderPane s2 = new BorderPane();
			
			Text text1 = new Text();
			text1.setText("Calories: " + totalCalories + "          Fat: " + totalFat + "           Carbs: " + totalCarbs);
			text1.setTextAlignment(TextAlignment.CENTER);
			s1.setCenter(text1);
			Text text2 = new Text();
			text2.setText("Fiber: " + totalFiber + "          Protein: " + totalProtein);
			text2.setTextAlignment(TextAlignment.CENTER);
			s2.setCenter(text2);
			
			middle.setTop(s1);
			middle.setBottom(s2);
			summary.setTop(top);
			summary.setCenter(middle);
			summary.setBottom(bottom);
			popUp.setCenter(summary);
			
		}

		BorderPane toAdd2 = new BorderPane();
		toAdd2.setPrefHeight(50);
		Button buttonAccept = newButton("Accept", 200, 20);
		buttonAccept.setOnMouseReleased(e -> mealStage.close());
		buttonAccept.setAlignment(Pos.BASELINE_CENTER);
		toAdd2.setCenter(buttonAccept);
		popUp.setBottom(toAdd2);

		Scene scene = new Scene(popUp, 600, 300, Color.rgb(255, 239, 229));
		mealStage.setScene(scene);
		mealStage.show();
	}

	/**This helper method builds the View
	 * All Filters Pop-Up which is 
	 * in the Food List side of the window
	 * 
	 */
	private void filterPopUp() {
		Stage filterStage = new Stage();
		filterStage.setTitle("View All Filters");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,300);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));

		// TITLE
		Text t4 = newHeaderText("FILTERS", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		//START Listviews
		BorderPane lists = new BorderPane();

		ListView<String> unappliedFilterListPane = new ListView<>();
		unappliedFilterListPane.setItems(unappliedFilterList);
		unappliedFilterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
		unappliedFilterListPane.setPrefWidth(200);
		unappliedFilterListPane.autosize();
		lists.setLeft(unappliedFilterListPane);

		ListView<String> filterListPane = new ListView<>();
		filterListPane.setItems(appliedFilterList);
		filterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
		filterListPane.setPrefWidth(200);
		filterListPane.autosize();
		lists.setRight(filterListPane);
		//END ListViews

		//Start APPLY/UNAPPLY
		VBox leftRightVbox = addVBox();
		leftRightVbox.setPrefWidth(75);
		Button add = newButton("Apply --->", 75, 50);
		add.setTextAlignment(TextAlignment.CENTER);

		Button remove =  newButton("Unapply <---", 75, 50);
		remove.setTextAlignment(TextAlignment.CENTER);
		leftRightVbox.getChildren().addAll(add,remove);
		lists.setCenter(leftRightVbox);
		//END APPLY/UNAPPLY

		//START add and remove actions
		add.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String filterToAdd = null;

				if (unappliedFilterListPane.getSelectionModel().getSelectedItem() != null) {
					filterToAdd = unappliedFilterListPane.getSelectionModel().getSelectedItem().toString();
				}

				if (filterToAdd != null) {
					appliedFilterList.add(filterToAdd);
					unappliedFilterList.remove(filterToAdd);
				}
			}
		});

		remove.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String filterToAdd = null;

				if (filterListPane.getSelectionModel().getSelectedItem() != null) {
					filterToAdd = filterListPane.getSelectionModel().getSelectedItem().toString();
				}

				if (filterToAdd != null) {
					unappliedFilterList.add(filterToAdd);
					appliedFilterList.remove(filterToAdd);
				}
			}
		});
		//END add and remove actions

		popUp.setCenter(lists);

		// START Create Accept/Apply and Assign Actions
		BorderPane toAdd2 = new BorderPane();
		toAdd2.setPrefHeight(50);
		Button buttonAcceptApply = newButton("Accept & Apply Filters", 200, 20);
		buttonAcceptApply.setAlignment(Pos.BASELINE_CENTER);
		
		buttonAcceptApply.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				// call apply filters
				getFoodNames(foodData.filterByNutrients(appliedFilterList));
				
				filterStage.close();
			}
		});
		
		toAdd2.setCenter(buttonAcceptApply);
		popUp.setBottom(toAdd2);
		// END Create Accept/Apply and Assign Actions

		Scene scene = new Scene(popUp, 500, 500, Color.rgb(255, 239, 229));
		filterStage.setScene(scene);
		filterStage.show();
	}

	/**This helper method builds the save
	 * to File pop-up where an end-user
	 * can input a file path
	 * 
	 */
	private void loadFromFilePopUp() {
		Stage fileStage = new Stage();
		fileStage.setTitle("Load Foods from File");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,600);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(15,15,15,15))));

		//TITLE
		Text t4 = newHeaderText("LOAD FOODS FROM FILE", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		BorderPane toAdd2 = new BorderPane();

		// START Create Text Fields
		HBox toAdd3 = addHBox();
		Label enterFile = new Label("Enter File Name");
		TextField enterFileField = new TextField();
		enterFileField.setPromptText("Ex: foods.txt");
		toAdd3.getChildren().addAll(enterFile,enterFileField);
		toAdd2.setCenter(toAdd3);
		// END Create Text Fields

		popUp.setCenter(toAdd2);

		// START create and assign actions to buttons
		BorderPane bottom = new BorderPane();
		bottom.setPrefHeight(50);
		Button buttonLOAD= newButton("Load", 200, 20);
		buttonLOAD.setAlignment(Pos.BASELINE_CENTER);
		bottom.setCenter(buttonLOAD);
				
		buttonLOAD.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String filepath = null;
				
				filepath  = enterFileField.getText();
				
				if (!enterFileField.getText().isEmpty()) {
					// call load from file
					foodData.loadFoodItems(filepath);
					foodList.clear();
					mealList.clear();
					for (int i = 0; i < appliedFilterList.size(); i++) {
						unappliedFilterList.add(appliedFilterList.get(i));
						appliedFilterList.remove(i);
					}
					getFoodNames(foodData.getAllFoodItems());
				}
				
				fileStage.close();
			}
			
		});
		popUp.setBottom(bottom);
		// END create and assign actions to buttons

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		fileStage.setScene(scene);
		fileStage.show();
	}

	/**This helper method builds the SAVE
	 * to File pop-up where an end-user
	 * can input a file path
	 * 
	 */
	private void saveToFilePopUp() {
		Stage fileStage = new Stage();
		fileStage.setTitle("Save Foods to File");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,600);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(15,15,15,15))));

		//TITLE
		Text t4 = newHeaderText("SAVE FOODS TO FILE", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		BorderPane toAdd2 = new BorderPane();

		// START Create Text Fields
		HBox toAdd3 = addHBox();
		Label enterFile = new Label("Enter File Name");
		TextField enterFileField = new TextField();
		enterFileField.setPromptText("Ex: foods.txt");
		toAdd3.getChildren().addAll(enterFile,enterFileField);
		toAdd2.setCenter(toAdd3);
		// END Create Text Fields

		popUp.setCenter(toAdd2);

		// START create and assign actions to buttons
		BorderPane bottom = new BorderPane();
		bottom.setPrefHeight(50);
		Button buttonSAVE= newButton("Save", 200, 20);
		buttonSAVE.setAlignment(Pos.BASELINE_CENTER);
		bottom.setCenter(buttonSAVE);
		
		buttonSAVE.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String filepath = null;
				
				filepath  = enterFileField.getText();
				
				if (filepath != null) {
					// call save to file
					foodData.saveFoodItems(filepath);

				}
				
				fileStage.close();
			}
		});
		
		popUp.setBottom(bottom);
		// END create and assign actions to buttons

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		fileStage.setScene(scene);
		fileStage.show();
	}

	/**This helper method builds the Create
	 * New Food Name Based Filter Pop-Up
	 * where an end-user can enter a string to
	 * filter the names on
	 */
	private void foodNameFilterPopUp() {
		Stage mealStage = new Stage();
		mealStage.setTitle("Create New Name Based Filter");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,600);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(15,15,15,15))));

		//TITLE
		Text t4 = newHeaderText("ADD FOOD NAME FILTER", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		BorderPane toAdd2 = new BorderPane();

		//START Create Text Field
		HBox toAdd3 = addHBox();
		Label enterString = new Label("Value to filter on: ");
		TextField enterStringField = new TextField();
		enterStringField.setTooltip(new Tooltip("Enter text"));
		enterStringField.setPromptText("Ex: apple");
		toAdd3.getChildren().addAll(enterString,enterStringField);
		toAdd2.setCenter(toAdd3);
		popUp.setCenter(toAdd2);
		//END Create Text Field

		//START Create buttons and assign actions 
		BorderPane bottom = new BorderPane();
		bottom.setPrefHeight(50);
		Button buttonCreateFilter = newButton("Create & Apply", 200, 20);
		buttonCreateFilter.setAlignment(Pos.BASELINE_CENTER);
		bottom.setCenter(buttonCreateFilter);
		popUp.setBottom(bottom);
		
		buttonCreateFilter.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				if (enterStringField.getText().isEmpty()) {
					mealStage.close();
				} else {
					if (stringFilter != null) {
						appliedFilterList.remove("Name Filter: " + stringFilter);
						unappliedFilterList.remove("Name Filter: " + stringFilter);
						allFilterList.remove("Name Filter: " + stringFilter);
					}
					
					stringFilter  = enterStringField.getText();
					appliedFilterList.add("Name Filter: " + stringFilter);
					allFilterList.add("Name Filter: " + stringFilter);
					
					getFoodNames(foodData.filterByNutrients(appliedFilterList));  // updates food list
					
					mealStage.close();
				}
			}
		});
		//END Create buttons and assign actions 

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		mealStage.setScene(scene);
		mealStage.show();

	}//name filter

	/**This helper method builds the Create
	 * New Nutritional Based Filter Pop-Up
	 * where an end-user can enter a info to
	 * filter the names on
	 */
	private void nutritionalFilterPopUp() {
		Stage mealStage = new Stage();
		mealStage.setTitle("Create New Nutritional Filter");

		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(300,600);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(15,15,15,15))));

		//TITLE
		Text t4 = newHeaderText("ADD NUTRITIONAL FILTER", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);

		BorderPane toAdd = new BorderPane();
		toAdd.setPrefHeight(50);
		toAdd.setCenter(t4);
		popUp.setTop(toAdd);

		BorderPane toAdd2 = new BorderPane();

		// START Create Nutritional Radiobuttons
		HBox toAdd3 = addHBox();
		final ToggleGroup group1 = new ToggleGroup();

		RadioButton rb1 = new RadioButton("Fat");
		rb1.setToggleGroup(group1);
		rb1.setSelected(true);
		RadioButton rb2 = new RadioButton("Carb");
		rb2.setToggleGroup(group1);
		RadioButton rb3 = new RadioButton("Fiber");
		rb3.setToggleGroup(group1);
		RadioButton rb4 = new RadioButton("Calories");
		rb4.setToggleGroup(group1);
		RadioButton rb5 = new RadioButton("Protein");
		rb5.setToggleGroup(group1);

		toAdd3.getChildren().addAll(rb1,rb2,rb3,rb4,rb5);
		toAdd2.setTop(toAdd3);
		// END Create Nutritional Radiobuttons

		// START Create Operator RadioButtons
		BorderPane neater = new BorderPane();
		HBox toAdd4 = addHBox();
		final ToggleGroup group2 = new ToggleGroup();

		RadioButton rb6 = new RadioButton("Greater than or equal to");
		rb6.setToggleGroup(group2);
		rb6.setSelected(true);
		RadioButton rb7 = new RadioButton("Equals");
		rb7.setToggleGroup(group2);
		RadioButton rb8 = new RadioButton("Less than or equal to");
		rb8.setToggleGroup(group2);
		toAdd4.getChildren().addAll(rb8,rb7,rb6);
		neater.setTop(toAdd4);
		// END Create Operator RadioButtons

		// START Create number field to filter on
		HBox toAdd5 = addHBox();
		Label enterNumber = new Label("Value to filter on: ");
		TextField enterNumberField = new TextField();
		enterNumberField.setTooltip(new Tooltip("Enter number with decminal place"));
		enterNumberField.setPromptText("Ex: ##.##");
		toAdd5.getChildren().addAll(enterNumber,enterNumberField);
		neater.setCenter(toAdd5);
		toAdd2.setCenter(neater);
		// END  Create number field to filter on
		
		popUp.setCenter(toAdd2);

		// Start Create Buttons and Assign Actions
		BorderPane bottom = new BorderPane();
		bottom.setPrefHeight(50);
		Button buttonCreateFilter = newButton("Create & Apply", 200, 20);
		buttonCreateFilter.setAlignment(Pos.BASELINE_CENTER);
		bottom.setCenter(buttonCreateFilter);
		popUp.setBottom(bottom);
		
		buttonCreateFilter.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String nutritionalGroupFilter = null;
				String operatorFilter = null;
				String nutritionalFilter = null;
				boolean formatCheck = true;
				
				if (group2.getSelectedToggle() == rb6) {operatorFilter = ">=";} if (group2.getSelectedToggle() == rb7) {operatorFilter = "==";}
					if (group2.getSelectedToggle() == rb8) {operatorFilter = "<=";}	
				
				if (group1.getSelectedToggle() == rb1) {nutritionalGroupFilter = "fat";}  if (group1.getSelectedToggle() == rb2) {nutritionalGroupFilter = "carbohydrates";}
				if (group1.getSelectedToggle() == rb3) {nutritionalGroupFilter = "fiber";} if (group1.getSelectedToggle() == rb4) {nutritionalGroupFilter = "calories";}
				if (group1.getSelectedToggle() == rb5) {nutritionalGroupFilter = "protein";}
				
				if (enterNumberField.getText().isEmpty()) {
					errorPopup("You must add a nutritional value to create a filter.");
					return;
				}
				if (!enterNumberField.getText().isEmpty()) {
					nutritionalFilter  = enterNumberField.getText();
				}
				formatCheck = negativeValidation(nutritionalFilter);
				if (!formatCheck) {
					errorPopup("Your nutiritional input cannot be negative.");
					formatCheck = true;
					return;
				} 
				formatCheck = doubleValidation(nutritionalFilter);
				if (!formatCheck) {
					errorPopup("ERROR: Your nutiritional input '" + nutritionalFilter + "' was invalid. " 
							+ System.lineSeparator() + System.lineSeparator()
							+ "This needs to be in a double format." + System.lineSeparator() 
							+ System.lineSeparator() + "ie:  1.0 or 22.35 or ##.## or ##");
					formatCheck = true;
					return;
				} else { 
				
				System.out.println(nutritionalGroupFilter);
				System.out.println(operatorFilter);
				System.out.println(nutritionalFilter);
				
				// START Add to Lists
				appliedFilterList.add(nutritionalGroupFilter  + " " + operatorFilter 
						+ " " + nutritionalFilter);
				allFilterList.add(nutritionalGroupFilter  + " " + operatorFilter 
						+ " " + nutritionalFilter);
				//END
				
				//Call filter method
				getFoodNames(foodData.filterByNutrients(appliedFilterList)); 
				//
				
				mealStage.close();
				}
			}
		});
		// END Create Buttons and Assign Actions

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		mealStage.setScene(scene);
		mealStage.show();
	}

	/**This helper method builds the Create
	 * New Food Item Pop-Up 
	 * Where a user can create a new food item
	 */
		private void newFoodItemPopUp() {
		BorderPane screen = new BorderPane();
		Stage popup = new Stage();
		popup.setTitle("Add New Food");
		Text t4 = newHeaderText("ADD NEW FOOD", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);

		Label nutritionalErrorMessage = new Label("");

		BorderPane top = new BorderPane();
		top.setCenter(t4);
		top.setBottom(nutritionalErrorMessage);
		top.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
		
		//create grid with labels and text fields
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));

		Label foodID = new Label("ID: ");
		grid.add(foodID, 0, 1);
		TextField IDField = new TextField();
		IDField.setPromptText("Enter an alphanumeric ID");
		grid.add(IDField, 1, 1);
		
		Label foodName = new Label("Food Name: ");
		grid.add(foodName, 0, 2);
		TextField nameField = new TextField();
		nameField.setPromptText("Enter Food Name");
		grid.add(nameField, 1, 2);

		Label fat = new Label("Fat: ");
		grid.add(fat, 0, 3);
		TextField fatField = new TextField();
		fatField.setTooltip(new Tooltip("Enter Fat Content"));
		fatField.setPromptText("Enter Fat content");
		grid.add(fatField, 1, 3);

		Label carbohydrate = new Label("Carbohydrates: ");
		grid.add(carbohydrate, 0, 4);
		TextField carbohydrateField = new TextField();
		carbohydrateField.setTooltip(new Tooltip("Enter Carbohydrates"));
		carbohydrateField.setPromptText("Enter Carbohydrates");
		grid.add(carbohydrateField, 1, 4);

		Label fiber = new Label("Fiber: ");
		grid.add(fiber, 0, 5);
		TextField fiberField = new TextField();
		fiberField.setTooltip(new Tooltip("Enter Fiber"));
		fiberField.setPromptText("Enter Fiber");
		grid.add(fiberField, 1, 5);

		Label protein = new Label("Protein: ");
		grid.add(protein, 0, 6);
		TextField proteinField = new TextField();
		proteinField.setTooltip(new Tooltip("Enter Protein"));
		proteinField.setPromptText("Enter Protein");
		grid.add(proteinField, 1, 6);
		
		Label calories = new Label("Calories: ");
		grid.add(calories, 0, 7);
		TextField calorieField = new TextField();
		calorieField.setTooltip(new Tooltip("Enter Calorie Count"));
		calorieField.setPromptText("Enter Calorie Count");
		grid.add(calorieField, 1, 7);

		//create buttons
		Button saveBtn = new Button();
		saveBtn.setText("Save");
		saveBtn.alignmentProperty().set(Pos.CENTER_LEFT);

		Button cancelBtn = new Button();
		cancelBtn.setText("Cancel");
		cancelBtn.alignmentProperty().set(Pos.BASELINE_RIGHT);	
		cancelBtn.setOnMouseReleased(e -> popup.close());

		HBox bottomButtons = addHBox();
		bottomButtons.getChildren().addAll(saveBtn,cancelBtn);
		bottomButtons.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
		bottomButtons.alignmentProperty().set(Pos.CENTER);
			
		//when the user tries to save, do validation before trying to create a food item
		saveBtn.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
			String fiberInput = fiberField.getText();
			String proteinInput = proteinField.getText();
			String carbohydrateInput = carbohydrateField.getText();
			String fatInput = fatField.getText();
			String caloriesInput = calorieField.getText();
			String nameInput = nameField.getText();
			String IDinput = IDField.getText();
			System.out.println(doubleValidation(fiberInput));
			if (((doubleValidation(fiberInput) == true) && (fiberInput != null)) &&
			((doubleValidation(proteinInput) == true) && (proteinInput != null)) &&
			((doubleValidation(carbohydrateInput) == true) && (carbohydrateInput != null)) &&
			((doubleValidation(fatInput) == true) && (fatInput != null)) &&
			((doubleValidation(caloriesInput) == true) && (caloriesInput != null)) && 
			(nameInput != null) && (IDinput != null))
			{
				FoodItem newFood = new FoodItem(IDinput, nameInput);
				newFood.addNutrient("fiber", Double.valueOf(fiberInput));
				newFood.addNutrient("protein", Double.valueOf(proteinInput));
				newFood.addNutrient("carbohydrate", Double.valueOf(carbohydrateInput));
				newFood.addNutrient("fat", Double.valueOf(fatInput));
				newFood.addNutrient("calories", Double.valueOf(caloriesInput));
				foodData.addFoodItem(newFood);
				
				foodList.clear();  // clear existing list
				getFoodNames(foodData.filterByNutrients(appliedFilterList)); // load list with new item
				
				saveBtn.setOnMouseReleased(q -> popup.close());
			}
			else {
				errorPopup("Error: Please fill out all the fields. "
						+ System.lineSeparator()
						+ "Values must be in a double format. "
						+ System.lineSeparator()
						+ "Values must be positive." + System.lineSeparator() 
				+ System.lineSeparator() + "ie:  1.0 or 22.35 or ##.## or ##");			
		}}});//End of save button action
		screen.setTop(top);
		screen.setCenter(grid);
		screen.setBottom(bottomButtons);

		Scene popupAddFood = new Scene(screen, 300, 350);
		popup.setScene(popupAddFood);
		popup.show();
	}
		
	/**This helper method creates a new button
	 * this makes our buttons consistent
	 * @param text that displays on the button
	 * @param button width
	 * @param button height
	 */
	private Button newButton(String text, int width, int height) {
		Button toReturn = new Button(text);
		toReturn.setPrefSize(width, height);
		toReturn.setAlignment(Pos.CENTER);
		toReturn.setWrapText(true);
		return toReturn;
	}

	/**This helper method creates a new horizontal
	 * Box
	 * this makes our HBoxes consistent
	 * return hbox
	 */
	private HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets (15,12,15,12));
		hbox.setSpacing(10);
		return hbox;
	}

	/**This helper method creates a new vertical
	 * Box
	 * this makes our VBoxes consistent
	 * @return vbox
	 */
	private VBox addVBox() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets (140,12,150,12));
		vbox.setSpacing(10);		
		return vbox;
	}

	/**This helper method creates new Header
	 * Text with the appropriate formatting.
	 * @param text
	 * @param size
	 * @param r is the red color value
	 * @param g is the green color value
	 * @param b is the blue color value
	 * @returns the text
	 */
	private Text newHeaderText(String text, int size, int r, int g, int b) {
		Text newText = new Text();
		newText.setX(0.0f);
		newText.setY(0.0f);
		newText.setCache(true);
		newText.setText(text);
		newText.setFont(Font.font(null, FontWeight.BOLD, size));
		newText.setFill(Color.rgb(r, g, b));
		return newText;
	}


	/**This helper method checks whether the 
	 * string can be parsed into a double
	 * and whether the input is negative
	 * @param  stringInput  String input
	 * @returns boolean 
	 * returns true if the string can be converted into a double
	 * returns false if the number contains a '-'
	 * returns false if an exception is thrown
	 */
	private boolean doubleValidation (String stringInput) {
        try {
        	Double.valueOf(stringInput);
        	if (stringInput.contains("-")) {
        		return false;
        	}
        } catch (NumberFormatException e) {
        	return false;
        }
        catch (Exception e) {
        	return false;
        }
		return true;
	}
	
	/**This helper method checks whether the 
	 * input string contains a '-' to
	 * prevent negative value inputs
	 * @param  stringInput  String input
	 * @returns boolean 
	 * returns false if the string contains a '-'
	 */
	private boolean negativeValidation (String stringInput) {
		if (stringInput.contains("-")) {
			return false;
		}
		return true;
	}
	
	/**This helper method creates an error message
	 * popup with the given text
	 * @param  errorMessage the string that displays in the error popup
	 */
	private void errorPopup (String errorMessage) {

		Stage errorStage = new Stage();
		errorStage.setTitle("Error");
		
		BorderPane popUp = new BorderPane();
		popUp.setPrefSize(200,200);
		popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(15,15,15,15))));

		//TITLE
		BorderPane top = new BorderPane();
		top.setPrefHeight(50);
		Text t4 = newHeaderText("ERROR", 30, 105, 10, 21);
		t4.setTextAlignment(TextAlignment.CENTER);
		t4.setCache(true);
		top.setCenter(t4);
		popUp.setTop(top);
		
		Text t2 = new Text();
		t2.setText(errorMessage);
		popUp.setCenter(t2);
		
		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		errorStage.setScene(scene);
		errorStage.show();

	}
	
	/*
	 * Updates this class's String FoodList to have a new set of values from
	 * a List<FoodItem>.  Sorts as well.
	 * Saves item name and item to HashMap nameMap
	 * @param fooditemList comes from FoodData class
	 */
	private void getFoodNames(List<FoodItem> foodItemList) {
		if (foodItemList.isEmpty()) {
			foodList.clear();
		}
		foodList.clear();
		nameMap.clear();  //test
		
		for (int i = 0; i < foodItemList.size(); ++i) {
			// add item to ObservableList of food  
			foodList.add(foodItemList.get(i).getName());
			// add name and food item to nameMap if not already present
			//if (!nameMap.containsKey(foodItemList.get(i).getName())) {
			nameMap.put(foodItemList.get(i).getName(), foodItemList.get(i));
			//}
		   }
		   Collections.sort(foodList, String.CASE_INSENSITIVE_ORDER);
		   foodNumber.textProperty().bind(Bindings.size(foodList).asString("%s food(s)"));
	   }
	/**
	 * Provides the sum of nutrient values for a selected meal
	 * Saves item name and item to HashMap nameMap
	 * @param analyzeMealList is the mealList selected to be analyzed
	 * returns double array with values of each nutrient sum
	 */
	private static double [] analyzeMeal(ObservableList<String> analyzeMealList) {
		double [] totalValues = new double [5]; // store nutrient values
		
		double totalCalories = 0; // store sum of calories
		double totalFat = 0; // store sum of fat
		double totalCarbs = 0; // store sum of carbs
		double totalProtein = 0; // store sum of protein
		double totalFiber = 0; // store sum of fiber
		
		
		if (analyzeMealList.size() > 0) { // if meal list is not empty
		
		// traverse meal list
		for (int f = 0; f < analyzeMealList.size(); ++f) {
			
			// find FoodItem corresponding to food name
			FoodItem food = nameMap.get(analyzeMealList.get(f));
			
			// add nutrient values for item to running total
			totalCalories = totalCalories + food.getNutrientValue("calories");
			totalFat = totalFat + food.getNutrientValue("fat");
			totalCarbs = totalCarbs + food.getNutrientValue("carbohydrate");
			totalFiber = totalFiber + food.getNutrientValue("fiber");
			totalProtein = totalProtein + food.getNutrientValue("protein");
		}
			
		}
		
		// store totals in array
		totalValues[0] = totalCalories;
		totalValues[1] = totalFat;
		totalValues[2] = totalCarbs;
		totalValues[3] = totalFiber;
		totalValues[4] = totalProtein;
		
		return totalValues;
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}
