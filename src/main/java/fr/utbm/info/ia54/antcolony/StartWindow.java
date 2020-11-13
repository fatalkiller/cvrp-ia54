package fr.utbm.info.ia54.antcolony;

import fr.utbm.info.ia54.antcolony.model.DeathAgent;
import fr.utbm.info.ia54.antcolony.model.MainAgent;
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
	/*
	private CheckBox benchesCheckBox;
	private CheckBox exitCheckBox;
	private Slider agentsDelaySlider;
	private Slider agentsSimultaneousSlider;
	private Slider agentsTotalSlider;
	private  Label agentsDelayLabel;
	private  Label agentsSimultaneousLabel;
	private  Label agentsTotalLabel;
	*/
	
	public static void main(String[]args) {
		Application.launch(args);
	}
	
	/*
	//Pass instructions you need to pass to the main agent like so
	public static void spawnMainAgent(boolean pillars, boolean benches, boolean randomExit, double agentsDelay, int agentsSimultaneous, int agentsTotal) throws Exception {
		SREBootstrap bootstrap = SRE.getBootstrap();
		bootstrap.startAgent(MainAgent.class, pillars, benches, randomExit, agentsDelay, agentsSimultaneous, agentsTotal);
	}
	*/
	
	public static void spawnMainAgent(String map, boolean isDebugMode) throws Exception {
		SREBootstrap bootstrap = SRE.getBootstrap();
		bootstrap.startAgent(MainAgent.class, map, isDebugMode);
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
            public void handle(ActionEvent event) {
            	try {
            		/*
            		init stuff here
            		boolean benches = benchesCheckBox.isSelected();
            		boolean randomExit = exitCheckBox.isSelected();
            		double agentsDelay =  agentsDelaySlider.getValue();
            		int agentsSimultaneous =  (int) agentsSimultaneousSlider.getValue();
            		int agentsTotal =  (int) agentsTotalSlider.getValue();
            		*/
            		
            		String map = mapGroup.getSelectedToggle().getUserData().toString();
            		boolean isDebugMode = debugModeCheckBox.isSelected();
            		//Pass stuff here as well
					StartWindow.spawnMainAgent(map, isDebugMode);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
		
		mapGroup = new ToggleGroup();
		
		RadioButton defaultMapButton = new RadioButton("5 French Cities");
		defaultMapButton.setUserData("Default Map");
		defaultMapButton.setSelected(true);
		defaultMapButton.setToggleGroup(mapGroup);
		
		RadioButton usCapitals = new RadioButton("48 US Capitals");
		usCapitals.setUserData("USCapitals");
		usCapitals.setSelected(false);
		usCapitals.setToggleGroup(mapGroup);
		
		RadioButton randomCities = new RadioButton("532 Random Cities");
		randomCities.setUserData("RandomCities");
		randomCities.setSelected(false);
		randomCities.setToggleGroup(mapGroup);
		
		RadioButton usCities = new RadioButton("13509 US Cities");
		usCities.setUserData("USCities");
		usCities.setSelected(false);
		usCities.setToggleGroup(mapGroup);
		
		debugModeCheckBox = new CheckBox("Debug Mode");
		debugModeCheckBox.setSelected(false);
		
		layout.getChildren().add(defaultMapButton);
		layout.getChildren().add(usCapitals);
		layout.getChildren().add(randomCities);
		layout.getChildren().add(usCities);
		
		layout.getChildren().add(debugModeCheckBox);
		layout.getChildren().add(startButton);
		layout.setAlignment(Pos.CENTER); 
		
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
