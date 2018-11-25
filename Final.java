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
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
	
	static ObservableList<String> names = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		
//		// Stage
//		
//		primaryStage.setTitle("Stage");
//		primaryStage.show();
		
//		// Stage and Scene
		
		primaryStage.setTitle("Stage and Scene");
		BorderPane bPane = new BorderPane();
		bPane.setPrefSize(2000, 1000);
		
		BorderPane top = new BorderPane();
		top.setPrefSize(2000,150);

		Rectangle rectangle = new Rectangle(2000,150,Color.rgb(245, 163, 173));
	    rectangle.relocate(0,0);
	    top.getChildren().addAll(rectangle);
	    
	    //TITLE
		Text t2 = newText("FOOD LIST", 160, 255, 226, 206);
		t2.setX(20.0f);
		t2.setY(140.0f);
		t2.setCache(true);
		
		top.getChildren().add(t2);
		StackPane.setAlignment(t2, Pos.TOP_LEFT);
	    
		// MEAL
		
		bPane.setTop(top);
		bPane.setRight(setRight());
		bPane.setCenter(setLeft());
		
		Scene test = new Scene(bPane, 1600, 900, Color.rgb(158, 53, 74));
		
		Rectangle r = new Rectangle(25,25,250,250);
		r.setFill(Color.BLUE);
		

		
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
		
		Button buttonAddFilter = newButton("Add Filter",300, 20);		
		Button buttonCurrent = newButton("Delete Filter",300, 20);	
		Button buttonClear = newButton("Clear Filters",300, 20);
	
		TextField input1 = new TextField();
		input1.setMaxHeight(20); input1.setMaxWidth(300);
		input1.setPrefSize(300, 20);
		input1.setPromptText("Add String Filter");
		input1.setFocusTraversable(false);
		
		TextField input2 = new TextField();
		input2.setMaxHeight(20); input1.setMaxWidth(300);
		input2.setPrefSize(300, 20);
		input2.setPromptText("Add Calorie Filter");
		input2.setFocusTraversable(false);
		
		Button buttonViewFilter = newButton("View All Filters",300, 20);
		
		hbox.getChildren().addAll(buttonAddFilter,buttonClear,buttonCurrent, input1, input2, buttonViewFilter);		
		//top.setCenter(hbox);
		toReturn.setTop(hbox);
	
		HBox hbox2 = addHBox();
		//hbox.setPrefSize(1000,50);
		
		Button buttonSaveFoodList = newButton("Save Food List",200, 20);	
		Button buttonNewFoodList = newButton("New Food List",200, 20);		
		Button buttonNewFoodItem = newButton("New Food Item",200, 20);
		
		hbox2.getChildren().addAll(buttonSaveFoodList,buttonNewFoodList,buttonNewFoodItem);
		//bottom.setBottom(hbox2);
		toReturn.setBottom(hbox2);
		//toReturn.setCenter(newScrollPane("fx-background: rgb(158, 53, 74)"));
		
		BorderPane rightPane = new BorderPane();
    	rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(158, 53, 74), null, new Insets(0))));
    	//rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(252, 228, 231), null, new Insets(0))));
    	ScrollBar bar = new ScrollBar();
    	bar.setStyle("-fx-background-color: seashell");
    	bar.setOrientation(Orientation.VERTICAL);
    	bar.snappedRightInset();
    	rightPane.setRight(bar);

		toReturn.setCenter(rightPane);
		
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
		    
			Text t3 = newText("MEAL", 60, 105, 10, 21);
			//t3.setX(225.0f);
			//t3.setY(60.0f);
			t3.setTextAlignment(TextAlignment.CENTER);
			t3.setCache(true);
			BorderPane top = new BorderPane();
			top.setPrefHeight(75);
			//top.getChildren().add(t3);
			top.setCenter(t3);
			right.setTop(top);
			
			BorderPane rightBottom = new BorderPane();
			//rightBottom.setPrefSize(600, 50);
			rightBottom.setPrefHeight(50);
			HBox toAdd = addHBox();
			toAdd.setPrefHeight(50);
			toAdd.getChildren().add(newButton("View Meal Summary",300, 20));
			rightBottom.setCenter(toAdd);
			right.setBottom(rightBottom);
			
			BorderPane rightPane = new BorderPane();
	    	rightPane.setBackground(new Background(new BackgroundFill(Color.rgb(249, 201, 207), null, new Insets(0))));
	    	ScrollBar bar = new ScrollBar();
	    	bar.setStyle("-fx-background-color: seashell");
	    	bar.setOrientation(Orientation.VERTICAL);
	    	bar.snappedRightInset();
	    	rightPane.setRight(bar);

			right.setCenter(rightPane);
			
			return right;
		}
		
		private ScrollPane newScrollPane() {
			ScrollPane rightPane = new ScrollPane();
            rightPane.setStyle("-fx-alignment: top-right");
            rightPane.setStyle("-fx-border-color: blue");
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
			//hbox.setStyle("-fx-backgroundcolor: A33643;");
			return hbox;
		}
	
		private Text newText(String text, int size, int r, int g, int b) {
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
		
		launch(args);
	}
}
