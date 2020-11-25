package fr.utbm.info.ia54.cvrp.view;

import java.util.UUID;

import fr.utbm.info.ia54.cvrp.agents.DeathAgent;
import fr.utbm.info.ia54.cvrp.agents.MainAgent;
import io.sarl.bootstrap.SRE;
import io.sarl.bootstrap.SREBootstrap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StartWindow extends Application{
	
	static private Stage stage;
	
	//Items to put on the screen
	private Scene s;
	private VBox layout;
	private Button startButton;
	private CheckBox debugModeCheckBox;
	private ToggleGroup mapGroup;
	private ToggleGroup typeGroup;
	
	public static void main(String[]args) {
		Application.launch(args);
	}
	
    public static void spawnMainAgent(String type, String map, boolean isDebugMode) throws Exception 
    {
        SREBootstrap bootstrap = SRE.getBootstrap();
        bootstrap.startAgentWithID(MainAgent.class, UUID.randomUUID(), type, map, isDebugMode);
    }
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		StartWindow.stage=primaryStage;

		Rectangle2D	screenBounds = Screen.getPrimary().getBounds();
		primaryStage.setWidth(screenBounds.getMaxX());
		primaryStage.setHeight(screenBounds.getMaxY());
		
		primaryStage.setTitle("Launching options");
		
		layout = new VBox();
		layout.setSpacing(20);
		
		startButton = new Button("Start Colony");
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) 
            {
            	try {

            		String type = typeGroup.getSelectedToggle().getUserData().toString();
            		String map = mapGroup.getSelectedToggle().getUserData().toString();
            		boolean isDebugMode = debugModeCheckBox.isSelected();
            		//Pass stuff here as well
					StartWindow.spawnMainAgent(type, map, isDebugMode);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
		
		typeGroup = new ToggleGroup();
		mapGroup = new ToggleGroup();
		
		RadioButton TSPButton = new RadioButton("TSP");
		TSPButton.setUserData("TSP");
		TSPButton.setSelected(false);
		TSPButton.setToggleGroup(typeGroup);
		
		
		RadioButton defaultMapButton = new RadioButton("5 French Cities");
		defaultMapButton.setUserData("Default Map");
		defaultMapButton.setSelected(false);
		defaultMapButton.setToggleGroup(mapGroup);
		defaultMapButton.setVisible(false);
		
		RadioButton usCapitals = new RadioButton("48 US Capitals");
		usCapitals.setUserData("USCapitals");
		usCapitals.setSelected(false);
		usCapitals.setToggleGroup(mapGroup);
		usCapitals.setVisible(false);
		
		RadioButton randomCities = new RadioButton("532 Random Cities");
		randomCities.setUserData("RandomCities");
		randomCities.setSelected(false);
		randomCities.setToggleGroup(mapGroup);
		randomCities.setVisible(false);
		
		RadioButton usCities = new RadioButton("13509 US Cities");
		usCities.setUserData("USCities");
		usCities.setSelected(false);
		usCities.setToggleGroup(mapGroup);
		usCities.setVisible(false);
		
		
		
		RadioButton CRVPButton = new RadioButton("CRVP");
		CRVPButton.setUserData("CRVP");
		CRVPButton.setSelected(true);
		CRVPButton.setToggleGroup(typeGroup);
		
		RadioButton CRVPBenchmark1 = new RadioButton("CRVPBenchmark1");
		CRVPBenchmark1.setUserData("CRVPBenchmark1");
		CRVPBenchmark1.setSelected(true);
		CRVPBenchmark1.setToggleGroup(mapGroup);
		CRVPBenchmark1.setVisible(true);
		
		RadioButton CRVPBenchmark2 = new RadioButton("CRVPBenchmark2");
		CRVPBenchmark2.setUserData("CRVPBenchmark2");
		CRVPBenchmark2.setSelected(false);
		CRVPBenchmark2.setToggleGroup(mapGroup);
		CRVPBenchmark2.setVisible(true);
		
		debugModeCheckBox = new CheckBox("Debug Mode");
		debugModeCheckBox.setSelected(false);

		layout.getChildren().add(TSPButton);
		layout.getChildren().add(CRVPButton);
		layout.getChildren().add(defaultMapButton);
		layout.getChildren().add(usCapitals);
		layout.getChildren().add(randomCities);
		layout.getChildren().add(usCities);
		layout.getChildren().add(CRVPBenchmark1);
		layout.getChildren().add(CRVPBenchmark2);
		
		layout.getChildren().add(debugModeCheckBox);
		layout.getChildren().add(startButton);
		layout.setAlignment(Pos.CENTER); 
		

		TSPButton.setOnAction( __ ->
		{
			defaultMapButton.setVisible(true);
			defaultMapButton.setSelected(true);
			usCapitals.setVisible(true);
			randomCities.setVisible(true);
			usCities.setVisible(true);

			CRVPBenchmark1.setVisible(false);
			CRVPBenchmark2.setVisible(false);
		});
		
		CRVPButton.setOnAction( __ ->
		{
			CRVPBenchmark1.setVisible(true);
			CRVPBenchmark1.setSelected(true);
			CRVPBenchmark2.setVisible(true);

			defaultMapButton.setVisible(false);
			usCapitals.setVisible(false);
			randomCities.setVisible(false);
			usCities.setVisible(false);
		});
		
		s = new Scene(layout, 600, 600);
		primaryStage.setScene(s);
		
		primaryStage.show();
	}

	public static Stage getStage() {
		return stage;
	}
	
	@Override
	public void stop()throws Exception {
	    System.out.println("Closing application");
	    
	    SREBootstrap bootstrap = SRE.getBootstrap();
		bootstrap.startAgent(DeathAgent.class);
	}
}
