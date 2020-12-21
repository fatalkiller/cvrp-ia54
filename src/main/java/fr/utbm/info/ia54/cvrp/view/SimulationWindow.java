package fr.utbm.info.ia54.cvrp.view;

import java.math.BigDecimal;
import java.util.UUID;

import fr.utbm.info.ia54.cvrp.agents.DeathAgent;
import fr.utbm.info.ia54.cvrp.agents.MainAgent;
import fr.utbm.info.ia54.cvrp.model.Environment;
import fr.utbm.info.ia54.cvrp.model.Metrics;
import fr.utbm.info.ia54.cvrp.model.SimuParameters;
import fr.utbm.info.ia54.cvrp.tools.NumBox;
import io.sarl.bootstrap.SRE;
import io.sarl.bootstrap.SREBootstrap;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SimulationWindow {

	private Environment environment;
	private Metrics metrics;
	private boolean debug;

	private Pane graphPane;
	private VBox vboxmetrics;

	private NumBox nbAgents;
	private NumBox nbEvaporationProportion;
	private NumBox nbEvaporationConstant;
	private NumBox nbPheromoneInfluence;
	private NumBox nbDistanceInfluence;
	
	private Button btnStartSimulation;
	private Button btnUpdateSimulation;
	private Button btnClearSimulation;
	private boolean simulationStarted = false;

	private String type;
	private String map;
	private boolean isDebugMode;

	public SimulationWindow(String type, String map, Boolean isDebugMode) {
		this.type = type;
		this.map = map;
		this.debug = isDebugMode;

		this.environment = new Environment(type, map);
		this.metrics = new Metrics(type, this.environment);
		displaySimulationFrame();
	}

	public void clearSimulation() {
		this.environment = null;
		this.metrics = null;
		this.environment = new Environment(type, map);
		this.metrics = new Metrics(type, this.environment);

		refreshSimulationFrame();
	}

	public void startSimulation() {
		// Lock/Unlock agent number
		nbAgents.setDisable(!simulationStarted);
		
		// Lock/Unlock clear simulation
		btnClearSimulation.setDisable(!simulationStarted);

		// Start simulation
		if (!simulationStarted) {
			System.out.println("START SIMULATION");
			
			btnStartSimulation.setText("Stop simulation");
			try {
				// Apply simulation parameters
				updateSimuParams();
				
				// Clear simulation first
				clearSimulation();
				
				// Start simulation
				SREBootstrap bootstrap = SRE.getBootstrap();
				bootstrap.startAgentWithID(MainAgent.class, UUID.randomUUID(), this.type, this.map, this.isDebugMode,
						this.environment, this.metrics, this);

				simulationStarted = true;

			} catch (Exception e) {
				e.printStackTrace();
			}
			// Stop simulation
		} else {
			System.out.println("STOP SIMULATION");
			
			btnStartSimulation.setText("Start simulation");

			// Launch death agent
			try {
				// Stop simulation
				SREBootstrap bootstrap = SRE.getBootstrap();
				bootstrap.startAgent(DeathAgent.class);

				simulationStarted = false;		
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateSimuParams() {
		if (!simulationStarted) {
			SimuParameters.agentNumber = nbAgents.getNumber().intValue();
		}
		SimuParameters.evaporationProportion = nbEvaporationProportion.getNumber().floatValue();
		SimuParameters.evaporationConstant = nbEvaporationConstant.getNumber().floatValue();
		SimuParameters.pheromoneInfluence = nbPheromoneInfluence.getNumber().floatValue();
		SimuParameters.distanceInfluence = nbDistanceInfluence.getNumber().floatValue();
	}

	public void refreshSimulationFrame() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Clear view panes
				graphPane.getChildren().clear();
				vboxmetrics.getChildren().clear();

				// Refresh graph
				graphPane.getChildren().addAll(environment.getCitiesRepresentation());
				if (debug) {
					graphPane.getChildren().addAll(environment.getCitiesNames());
					// No need to display all possible roads, itd just look cluttered
					graphPane.getChildren().addAll(environment.getRoadsRepresentation());
				}
				graphPane.getChildren().addAll(metrics.getFastestRoadsRepresentation());

				if (debug) {
					graphPane.getChildren().addAll(environment.getRoadsWeights());
				}

				// Add metrics
				vboxmetrics.getChildren().addAll(metrics.getMetrics());
			}
		});
	}

	public void displaySimulationFrame() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				/////////// NEW VIEW WITH GRAPH //////////////////////////////////////
//				final NumberAxis xAxis = new NumberAxis();
//				final NumberAxis yAxis = new NumberAxis();
//				xAxis.setLabel("X");
//				yAxis.setLabel("Y");
//				
//				graphPane = new Pane();
//				ScrollPane graphPaneScroll = new ScrollPane();
//				graphPaneScroll.setContent(graphPane);

				// Show just cities ////////////////
//				final ScatterChart<Number, Number> lineChart = new ScatterChart<Number, Number>(xAxis, yAxis);
//				List<City> cities = environment.getCities();
//				XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
//				for (City c : cities) {
//					serie.getData().add(new XYChart.Data<Number, Number>(c.getX(), c.getY()));
//				}
//				lineChart.getData().add(serie);
//
//				// Add graph node to a pane
//				graphPane.getChildren().add(lineChart);
//
//				// Make lineChart node size follow graphPane size
//				lineChart.prefWidthProperty().bind(graphPane.widthProperty());
//				lineChart.prefHeightProperty().bind(graphPane.heightProperty().subtract(100));

				// Show cities and routes /////////////
//				final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
//				lineChart.setTitle("CVRP Routes");
//				// Get different routes (one route, one color)
//				List<List<Road>> listRoutes = metrics.getFastestRoads();
//				// For each route, assign a serie (with a different color)
//				int routeIndex = 1;
//				for (List<Road> listRoads : listRoutes) {
//					XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
//					serie.setName("Route " + routeIndex);
//
//					for (Road road : listRoads) {
//						serie.getData().add(new XYChart.Data<Number, Number>(road.getCity1().getX(),
//								road.getCity1().getY()));
//						serie.getData().add(new XYChart.Data<Number, Number>(road.getCity2().getX(),
//								road.getCity2().getY()));
//					}
//					// Add route to graph
//					lineChart.getData().add(serie);
//					routeIndex++;
//				}
//
//				// Add graph node to a pane
//				graphPane.getChildren().add(lineChart);
//
//				// Make lineChart node size follow graphPane size
//				lineChart.prefWidthProperty().bind(graphPane.widthProperty());
//				lineChart.prefHeightProperty().bind(graphPane.heightProperty().subtract(100));
				//////////////////////////////////////////////////////////////////////////

				/////////////// GRAPH WITH CIRLCES /////////////////////////////////////////
				graphPane = new Pane();
				ScrollPane graphPaneScroll = new ScrollPane();
				graphPaneScroll.setContent(graphPane);

				// Things to display
				graphPane.getChildren().addAll(environment.getCitiesRepresentation());
				if (debug) {
					graphPane.getChildren().addAll(environment.getCitiesNames());
					// No need to display all possible roads, itd just look cluttered
					graphPane.getChildren().addAll(environment.getRoadsRepresentation());
				}
				graphPane.getChildren().addAll(metrics.getFastestRoadsRepresentation());

				if (debug) {
					graphPane.getChildren().addAll(environment.getRoadsWeights());
				}

				// Add control panel
				VBox vboxControl = new VBox();
				vboxControl.setSpacing(10);
				
				Label lbAgents = new Label("Number of CVRP agents to run simultaneously :");
				nbAgents = new NumBox(new BigDecimal(SimuParameters.agentNumber), new BigDecimal(1));

				Label lbEvapProp = new Label("Evaporation proportion (positive) :");
				nbEvaporationProportion = new NumBox(new BigDecimal(SimuParameters.evaporationProportion),
						new BigDecimal(0.1));

				Label lbEvapConst = new Label("Evaporation constant (positive) :");
				nbEvaporationConstant = new NumBox(new BigDecimal(SimuParameters.evaporationConstant),
						new BigDecimal(1));

				Label lbPheroInf = new Label("Pheromone influence (positive) :");
				nbPheromoneInfluence = new NumBox(new BigDecimal(SimuParameters.pheromoneInfluence),
						new BigDecimal(0.1));

				Label lbDistInf = new Label("Distance influence (positive) :");
				nbDistanceInfluence = new NumBox(new BigDecimal(SimuParameters.distanceInfluence),
						new BigDecimal(0.1));

				btnStartSimulation = new Button("Start simulation");
				btnStartSimulation.setOnAction(e -> startSimulation());

				btnClearSimulation = new Button("Clear simulation");
				btnClearSimulation.setOnAction(e -> clearSimulation());

				btnUpdateSimulation = new Button("Update parameters");
				btnUpdateSimulation.setOnAction(e -> updateSimuParams());

				vboxControl.getChildren().addAll(lbAgents, nbAgents, lbEvapProp, nbEvaporationProportion, lbEvapConst,
						nbEvaporationConstant, lbPheroInf, nbPheromoneInfluence, lbDistInf, nbDistanceInfluence,
						btnUpdateSimulation, btnClearSimulation, btnStartSimulation);

				// Info benchmark
				VBox vboxInfoBench = new VBox();
				vboxInfoBench.setSpacing(5);
				vboxInfoBench.setPadding(new Insets(0, 0, 20, 0));

				Label lbBenchName = new Label("Benchmark name: " + environment.getBenchName());
				Label lbBenchType = new Label("Benchmark name: " + environment.getBenchType());

				vboxInfoBench.getChildren().addAll(lbBenchType, lbBenchName);

				// Also display some metrics & stats
				vboxmetrics = new VBox();
				vboxmetrics.getChildren().addAll(metrics.getMetrics());

				// Right panel with controls and metrics
				VBox rightPanel = new VBox();
				rightPanel.setPadding(new Insets(10, 0, 0, 10));

				rightPanel.getChildren().addAll(vboxInfoBench, vboxControl, vboxmetrics);

				ScrollPane rightPaneScroll = new ScrollPane();
				rightPaneScroll.setContent(rightPanel);

				// Main hbox
				HBox mainPane = new HBox();

				// Add all panes to main scene
				mainPane.getChildren().addAll(graphPaneScroll, rightPaneScroll);

				Scene scene = new Scene(mainPane, 1400, 800);

				// Dynamically ajust graphPane size
				graphPane.prefWidthProperty().bind(Bindings.multiply(0.75, mainPane.widthProperty()));

				// Dynamically ajust metrics size
				rightPanel.prefWidthProperty().bind(Bindings.multiply(0.25, mainPane.widthProperty()));

				StartWindow.getStage().setTitle("ACO - TSP - Simulation");
				StartWindow.getStage().setScene(scene);
				// Fix maximized
				StartWindow.getStage().setMaximized(false);
				StartWindow.getStage().setMaximized(true);
			}
		});
	}
}