* Filename:   Main.java
 * Project:    Team Project, Milestone2, Java FX GUI interface at start
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
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;


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


public class Main extends Application {

	static ObservableList<String> foodList = FXCollections.observableArrayList();
	static ObservableList<String> mealList = FXCollections.observableArrayList();
	static ObservableList<String> appliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> unappliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> allFilterList = FXCollections.observableArrayList();

	String selectedMealItem = null;

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
	 * 
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
		Label foodNumber = new Label();
		foodNumber.setText(foodList.size() + " food(s)");
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
		foodListPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
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
	/**This helper method builds the Food List
	 * side of the main window.
	 * 
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
				
				if (filepath != null) {
					// call load from file
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
				String stringFilter = null;
				
				stringFilter  = enterStringField.getText();
				
				if (stringFilter != null) {
					// call create name filter
				}
				
				mealStage.close();
			}
		});
		//END Create buttons and assign actions 

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		mealStage.setScene(scene);
		mealStage.show();

	}

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
		buttonCreateFilter.setOnMouseReleased(e -> mealStage.close());
		buttonCreateFilter.setAlignment(Pos.BASELINE_CENTER);
		bottom.setCenter(buttonCreateFilter);
		popUp.setBottom(bottom);
		
		buttonCreateFilter.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent e) {
				String nutritionalGroupFilter = null;
				String operatorFilter = null;
				String nutritionalFilter = "0.0";
				
				if (group2.getSelectedToggle() == rb6) {operatorFilter = ">=";} if (group2.getSelectedToggle() == rb7) {operatorFilter = "=";}
					if (group2.getSelectedToggle() == rb8) {operatorFilter = "<=";}	
				
				if (group1.getSelectedToggle() == rb1) {nutritionalGroupFilter = "fat";}  if (group1.getSelectedToggle() == rb2) {nutritionalGroupFilter = "carb";}
				if (group1.getSelectedToggle() == rb3) {nutritionalGroupFilter = "fiber";} if (group1.getSelectedToggle() == rb4) {nutritionalGroupFilter = "calories";}
				if (group1.getSelectedToggle() == rb5) {nutritionalGroupFilter = "protein";}
				
				if (!enterNumberField.getText().isEmpty()) {
					nutritionalFilter  = enterNumberField.getText();
				}
				
				System.out.println(nutritionalGroupFilter);
				System.out.println(operatorFilter);
				System.out.println(nutritionalFilter);
				
				// call create nutritional filter
				
				mealStage.close();
			}
		});
		// END Create Buttons and Assign Actions

		Scene scene = new Scene(popUp, 400, 300, Color.rgb(255, 239, 229));
		mealStage.setScene(scene);
		mealStage.show();
	}

	/**This helper method builds the Create
	 * New Food Item Pop-Up
	 * where an end-user can enter a info to
	 * filter the names on
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


		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));

		Label foodName = new Label("Food Name: ");
		grid.add(foodName, 0, 1);
		TextField nameField = new TextField();
		nameField.setPromptText("Enter Food Name");
		grid.add(nameField, 1, 1);

		Label calories = new Label("Calories: ");
		grid.add(calories, 0, 2);
		TextField calorieField = new TextField();
		calorieField.setTooltip(new Tooltip("Enter Calorie Count"));
		calorieField.setPromptText("Enter Calorie Count");
		grid.add(calorieField, 1, 2);

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

		Button saveBtn = new Button();
		saveBtn.setText("Save");
		saveBtn.alignmentProperty().set(Pos.CENTER_LEFT);
		saveBtn.setOnMouseReleased(e -> popup.close());

		Button cancelBtn = new Button();
		cancelBtn.setText("Cancel");
		cancelBtn.alignmentProperty().set(Pos.BASELINE_RIGHT);	
		cancelBtn.setOnMouseReleased(e -> popup.close());

		HBox bottomButtons = addHBox();
		bottomButtons.getChildren().addAll(saveBtn,cancelBtn);
		bottomButtons.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
		bottomButtons.alignmentProperty().set(Pos.CENTER);

		// Mary Alice to add save things

		screen.setTop(top);
		screen.setCenter(grid);
		screen.setBottom(bottomButtons);

		Scene popupAddFood = new Scene(screen, 300, 350);
		popup.setScene(popupAddFood);
		popup.show();
	}

	/**This helper method creates a new button
	 * this makes our buttons consistent
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
	 */
	private VBox addVBox() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets (140,12,150,12));
		vbox.setSpacing(10);		
		return vbox;
	}

	/**This helper method creates new Header
	 * Text with the appropriate formatting.
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

	public static void main(String[] args) {

		//		String fileName = "file.txt";
		//		File inputFile = null;
		//		Scanner sc = null;
		//		
		//		try {
		//			inputFile = new File(fileName);
		//			sc = new Scanner(inputFile);
		//			while(sc.hasNextLine()) {
		//				String name = sc.nextLine();
		//				names.add(name);
		//			}
		//			sc.close();
		//		} catch (Exception ex) {
		//			ex.printStackTrace();
		//			System.exit(-1);
		//		}

		for (int i = 0; i <150; i++){
			foodList.add("food #" + i);
		}

		foodList.add("food test");
		foodList.add("food test2");
		foodList.add("food test6");
		foodList.add("food test4");

		for (int i = 0; i < 20; i++){
			mealList.add("meal item #" + i);
		}

		mealList.add("test");
		mealList.add("test2");
		mealList.add("test6");
		mealList.add("test4");

		appliedFilterList.add("Filter1");
		appliedFilterList.add("Filter3");
		appliedFilterList.add("Filter4");

		unappliedFilterList.add("Filter2");

		allFilterList.add("Filter1");
		allFilterList.add("Filter3");
		allFilterList.add("Filter4");

		allFilterList.add("Filter2");

		launch(args);
	}
}
