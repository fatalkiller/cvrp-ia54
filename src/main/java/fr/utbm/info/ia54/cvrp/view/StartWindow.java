package fr.utbm.info.ia54.cvrp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fr.utbm.info.ia54.cvrp.agents.DeathAgent;
import fr.utbm.info.ia54.cvrp.agents.MainAgent;
import io.sarl.bootstrap.SRE;
import io.sarl.bootstrap.SREBootstrap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartWindow extends Application {
//TODO : I have no idea where the application constants were moved, but we should still continue to improve them to find better default values
	static private Stage stage;

	// Items to put on the screen
	private Scene s;
	private VBox layout;
	private Button startButton;
	private CheckBox debugModeCheckBox;
	private ToggleGroup mapGroup;
	private ToggleGroup typeGroup;

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void spawnMainAgent(String type, String map, boolean isDebugMode) throws Exception {
		SREBootstrap bootstrap = SRE.getBootstrap();
		bootstrap.startAgentWithID(MainAgent.class, UUID.randomUUID(), type, map, isDebugMode);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		StartWindow.stage = primaryStage;
		primaryStage.setTitle("Launching options");

		layout = new VBox();
		layout.setSpacing(5);

		startButton = new Button("Go to simulation");

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					String type = typeGroup.getSelectedToggle().getUserData().toString();
					String map = mapGroup.getSelectedToggle().getUserData().toString();
					boolean isDebugMode = debugModeCheckBox.isSelected();
					new SimulationWindow(type, map, isDebugMode);
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

		RadioButton CVRPButton = new RadioButton("CVRP");
		CVRPButton.setUserData("CVRP");
		CVRPButton.setSelected(true);
		CVRPButton.setToggleGroup(typeGroup);

		RadioButton CVRPBenchmark1 = new RadioButton("CMT1");
		CVRPBenchmark1.setUserData("CMT1");
		CVRPBenchmark1.setSelected(true);
		CVRPBenchmark1.setToggleGroup(mapGroup);
		CVRPBenchmark1.setVisible(true);

		RadioButton CVRPBenchmarki;
		List<RadioButton> otherButtons = new ArrayList<RadioButton>();

		for (int i = 2; i < 15; i++) {
			CVRPBenchmarki = new RadioButton("CMT" + i);
			CVRPBenchmarki.setUserData("CMT" + i);
			CVRPBenchmarki.setSelected(false);
			CVRPBenchmarki.setToggleGroup(mapGroup);
			CVRPBenchmarki.setVisible(true);
			otherButtons.add(CVRPBenchmarki);
		}

		debugModeCheckBox = new CheckBox("Debug Mode");
		debugModeCheckBox.setSelected(false);

		layout.getChildren().add(TSPButton);
		layout.getChildren().add(CVRPButton);
		layout.getChildren().add(defaultMapButton);
		layout.getChildren().add(usCapitals);
		layout.getChildren().add(randomCities);
		layout.getChildren().add(usCities);
		layout.getChildren().add(CVRPBenchmark1);

		for (RadioButton button : otherButtons)
			layout.getChildren().add(button);

		layout.getChildren().add(debugModeCheckBox);
		layout.getChildren().add(startButton);
		layout.setAlignment(Pos.CENTER);

		TSPButton.setOnAction(__ -> {
			defaultMapButton.setVisible(true);
			defaultMapButton.setSelected(true);
			usCapitals.setVisible(true);
			randomCities.setVisible(true);
			usCities.setVisible(true);

			CVRPBenchmark1.setVisible(false);
			for (RadioButton button : otherButtons)
				button.setVisible(false);
		});

		CVRPButton.setOnAction(__ -> {
			CVRPBenchmark1.setVisible(true);
			CVRPBenchmark1.setSelected(true);
			for (RadioButton button : otherButtons)
				button.setVisible(true);

			defaultMapButton.setVisible(false);
			usCapitals.setVisible(false);
			randomCities.setVisible(false);
			usCities.setVisible(false);
		});

		s = new Scene(layout, 400, 600);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static Stage getStage() {
		return stage;
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Closing application");

		SREBootstrap bootstrap = SRE.getBootstrap();
		bootstrap.startAgent(DeathAgent.class);
	}
}