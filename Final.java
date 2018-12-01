package application;

import java.io.File;
import java.util.Observable;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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


public class Final extends Application {
	
	static ObservableList<String> foodList = FXCollections.observableArrayList();
	static ObservableList<String> mealList = FXCollections.observableArrayList();
	static ObservableList<String> appliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> unappliedFilterList = FXCollections.observableArrayList();
	static ObservableList<String> allFilterList = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		// Stage and Scene
		
		primaryStage.setTitle("Stage and Scene");
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
		bPane.setRight(setRight());
		bPane.setCenter(setLeft());
		
		Scene test = new Scene(bPane, 1600, 900, Color.rgb(158, 53, 74));
		
		primaryStage.setScene(test);
		primaryStage.show();   
		
	}
	
	private BorderPane setLeft() {
		// MEAL
		BorderPane toReturn = new BorderPane();
		toReturn.setPrefSize(1325,850);
		toReturn.autosize();
		toReturn.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		//toReturn.setBackground(new Background("fx-background: rgb(249, 201, 207)"));
//		Rectangle rectangle2 = new Rectangle(1000,900,Color.rgb(158, 53, 74));
//		rectangle2.relocate(0,0);
//	   	rectangle2.autosize();
//	    toReturn.getChildren().addAll(rectangle2);
		
		BorderPane top = new BorderPane();
		top.setPrefHeight(80);
		HBox hbox = addHBox();
		hbox.setPrefHeight(80);
		
		//Button buttonAddFilter = newButton("Add Filter",300, 20);		
		Button buttonDelete = newButton("Delete A Filter",300, 20);	
		buttonDelete.setOnMouseReleased(e -> deleteAFilterPopUp());
		Button buttonClear = newButton("Clear All Filters",300, 20);
	
		TextField input1 = new TextField();
		input1.setMaxHeight(20); input1.setMaxWidth(300);
		input1.setPrefSize(300, 20);
		input1.setPromptText("Add Name-based Filter");
		input1.setFocusTraversable(false);
		input1.setTooltip(new Tooltip("ex: app"));
		
		TextField input2 = new TextField();
		input2.setMaxHeight(20); input2.setMaxWidth(300);
		input2.setPrefSize(300, 20);
		input2.setPromptText("Add Greater than Calorie Filter");
		input2.setFocusTraversable(false);
		input2.setTooltip(new Tooltip("ex: 500"));
		
		TextField input3 = new TextField();
		input3.setMaxHeight(20); input3.setMaxWidth(300);
		input3.setPrefSize(300, 20);
		input3.setPromptText("Add Less than Calorie Filter");
		input3.setFocusTraversable(false);
		
		Button buttonViewFilter = newButton("View All Filters",300, 20);

		buttonViewFilter.setOnMouseReleased(e -> filterPopUp());
		

		hbox.getChildren().addAll(buttonClear,buttonDelete, input1, input2, input3, buttonViewFilter);
		toReturn.setTop(hbox);
	
		
		BorderPane bottom = new BorderPane();
		bottom.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		bottom.setPrefHeight(50);
		HBox hbox2 = addHBox();
		//hbox.setPrefSize(1000,50);
		
		Button buttonSaveFoodList = newButton("Save Food List to File",200, 20);	
		Button buttonNewFoodList = newButton("Load New Food List",200, 20);		
		Button buttonNewFoodItem = newButton("Add New Food Item",200, 20);
		
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
		
		BorderPane leftCenter = new BorderPane();
		leftCenter.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
    	
		BorderPane leftLeft = new BorderPane();
		leftLeft.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		leftLeft.setPrefWidth(50);
		BorderPane leftRight = new BorderPane();
		leftRight.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
		leftRight.setPrefWidth(100);
		
		// add and remove
		VBox leftRightVbox = addVBox();
		leftRightVbox.setPrefWidth(75);
		Button add = newButton("  Add    --->", 75, 50);
		add.setTextAlignment(TextAlignment.CENTER);
		Button remove =  newButton("<---  Remove", 75, 50);
		remove.setTextAlignment(TextAlignment.CENTER);
		leftRightVbox.getChildren().addAll(remove, add);
		leftRight.setCenter(leftRightVbox);
		
		ListView<String> foodListPane = new ListView<>();
		foodListPane.setItems(foodList);
		foodListPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
		foodListPane.setPrefWidth(900);
		foodListPane.autosize();
    	
    	ScrollPane scrollFoodList = newScrollPane();
    	scrollFoodList.setContent(foodListPane);
    	
    	leftCenter.setCenter(foodListPane);
    	leftCenter.setLeft(leftLeft);
    	leftCenter.setRight(leftRight);

		toReturn.setCenter(leftCenter);
		
		return toReturn;
	}
	
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
			
			//Scroll
			BorderPane rightPane = new BorderPane();
	    	rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
	    	rightPane.setPadding(new Insets (0,15,0,15));
	    	
	    	rightPane.setCenter(mealListPane);

			right.setCenter(rightPane);
			
			return right;
		}
		
		private void deleteAFilterPopUp() {
			Stage deleteStage = new Stage();
			deleteStage.setTitle("Delete a Filter");
			
			BorderPane popUp = new BorderPane();
			popUp.setPrefSize(300,300);
			popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
			
			Text t4 = newHeaderText("FILTERS", 30, 105, 10, 21);
			t4.setTextAlignment(TextAlignment.CENTER);
			t4.setCache(true);
			
			BorderPane toAdd = new BorderPane();
			toAdd.setPrefHeight(50);
			toAdd.setCenter(t4);
			popUp.setTop(toAdd);
			
			ListView<String> filterListPane = new ListView<>();
			filterListPane.setItems(allFilterList);
			filterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
			filterListPane.setPrefWidth(300);
			filterListPane.autosize();
			popUp.setCenter(filterListPane);
			
			BorderPane toAdd2 = new BorderPane();
			toAdd2.setPrefHeight(50);
			Button buttonDeleteFilter = newButton("Delete Filter", 200, 20);
			buttonDeleteFilter.setAlignment(Pos.BASELINE_CENTER);
			toAdd2.setCenter(buttonDeleteFilter);
			popUp.setBottom(toAdd2);
			
			Scene scene = new Scene(popUp, 300, 300, Color.rgb(255, 239, 229));
			deleteStage.setScene(scene);
			deleteStage.show();
		}
		
		private void mealPopUp() {
			Stage mealStage = new Stage();
			mealStage.setTitle("Meal Nutritional Summary");
			
			BorderPane popUp = new BorderPane();
			popUp.setPrefSize(300,600);
			popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
			
			Text t4 = newHeaderText("NUTRITIONAL SUMMARY", 30, 105, 10, 21);
			t4.setTextAlignment(TextAlignment.CENTER);
			t4.setCache(true);
			
			BorderPane toAdd = new BorderPane();
			toAdd.setPrefHeight(50);
			toAdd.setCenter(t4);
			popUp.setTop(toAdd);
			
			Scene scene = new Scene(popUp, 600, 300, Color.rgb(255, 239, 229));
			mealStage.setScene(scene);
			mealStage.show();
		}
		
		private void filterPopUp() {
			Stage filterStage = new Stage();
			filterStage.setTitle("View All Filters");
			
			BorderPane popUp = new BorderPane();
			popUp.setPrefSize(300,300);
			popUp.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(0))));
			
			Text t4 = newHeaderText("FILTERS", 30, 105, 10, 21);
			t4.setTextAlignment(TextAlignment.CENTER);
			t4.setCache(true);
			
			BorderPane toAdd = new BorderPane();
			toAdd.setPrefHeight(50);
			toAdd.setCenter(t4);
			popUp.setTop(toAdd);
			
			BorderPane lists = new BorderPane();
			
			ListView<String> filterListPane = new ListView<>();
			filterListPane.setItems(appliedFilterList);
			filterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
			filterListPane.setPrefWidth(200);
			filterListPane.autosize();
			lists.setLeft(filterListPane);
			
			ListView<String> unappliedFilterListPane = new ListView<>();
			unappliedFilterListPane.setItems(unappliedFilterList);
			unappliedFilterListPane.setBackground(new Background(new BackgroundFill(Color.rgb(255, 239, 229), null, new Insets(20,20,20,20))));
			unappliedFilterListPane.setPrefWidth(200);
			unappliedFilterListPane.autosize();
			lists.setRight(unappliedFilterListPane);
			
			VBox leftRightVbox = addVBox();
			leftRightVbox.setPrefWidth(75);
			Button add = newButton("Unapply --->", 75, 50);
			add.setTextAlignment(TextAlignment.CENTER);
			Button remove =  newButton("<--- Apply", 75, 50);
			remove.setTextAlignment(TextAlignment.CENTER);
			leftRightVbox.getChildren().addAll(remove, add);
			lists.setCenter(leftRightVbox);
			
			popUp.setCenter(lists);
			
			BorderPane toAdd2 = new BorderPane();
			toAdd2.setPrefHeight(50);
			Button buttonDeleteFilter = newButton("Delete Filter", 200, 20);
			buttonDeleteFilter.setAlignment(Pos.BASELINE_CENTER);
			toAdd2.setCenter(buttonDeleteFilter);
			popUp.setBottom(toAdd2);
			
			Scene scene = new Scene(popUp, 500, 500, Color.rgb(255, 239, 229));
			filterStage.setScene(scene);
			filterStage.show();
		}
		
		private ScrollPane newScrollPane() {
			ScrollPane rightPane = new ScrollPane();
            rightPane.setStyle("-fx-alignment: top-right");
            rightPane.setStyle("-fx-border-color: pink");
            rightPane.setStyle("-fx-hbar-policy: always");

            rightPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);

            rightPane.fitToHeightProperty();
            return rightPane;
		}
		
		private Button newButton(String text, int width, int height) {
			Button toReturn = new Button(text);
			toReturn.setPrefSize(width, height);
			toReturn.setAlignment(Pos.CENTER);
			toReturn.setWrapText(true);
			return toReturn;
		}
	
		private HBox addHBox() {
			HBox hbox = new HBox();
			hbox.setPadding(new Insets (15,12,15,12));
			hbox.setSpacing(10);
			return hbox;
		}
		
		private VBox addVBox() {
			VBox vbox = new VBox();
			vbox.setPadding(new Insets (140,12,150,12));
			vbox.setSpacing(10);		
			return vbox;
		}
	
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
		
		
		// http://paletton.com/palette.php?uid=50p0u0kllllaFw0g0qFqFg0w0aF
		// http://paletton.com/palette.php?uid=3300u0kllll8uyieCrlrLfDBA9U
		// http://paletton.com/palette.php?uid=50f0h0kllll2LnubDmuuCkhSJjb
		// purple/maroon 158, 53, 74
		//brownish 170, 126, 57

		
//		// ListView
//		primaryStage.setTitle("ListView");
//		StackPane sPane = new StackPane();
//		Scene scene = new Scene(sPane, 1600, 900, Color.DARKGRAY);
//		
//		ListView<String> nameList = new ListView<>();
//		nameList.setItems(names);
//		sPane.getChildren().add(nameList);
//		primaryStage.setScene(scene);
//		primaryStage.show();
		
	
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
		
		for (int i = 0; i <150; i++){
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
