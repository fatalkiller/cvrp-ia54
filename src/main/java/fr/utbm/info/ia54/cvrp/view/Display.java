package fr.utbm.info.ia54.cvrp.view;

import fr.utbm.info.ia54.cvrp.model.Environment;
import fr.utbm.info.ia54.cvrp.model.Metrics;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Display {

	private Environment environment;
	private Metrics metrics;
	private boolean debug;

	private Pane graphPane;
	private VBox vboxmetrics;

	public void changeDisplayFrameTitle(String title) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StartWindow.getStage().setTitle(title);
			}
		});
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

				// Also display some metrics & stats
				vboxmetrics = new VBox();
				vboxmetrics.getChildren().addAll(metrics.getMetrics());

				ScrollPane metricsPane = new ScrollPane();
				metricsPane.setContent(vboxmetrics);

				// Main hbox
				HBox mainPane = new HBox();

				// Add all panes to main scene
				mainPane.getChildren().addAll(graphPaneScroll, metricsPane);

				Scene scene = new Scene(mainPane, 1400, 800);

				// Dynamically ajust graphPane size
				graphPane.prefWidthProperty().bind(Bindings.multiply(0.75, mainPane.widthProperty()));

				// Dynamically ajust metrics size
				vboxmetrics.prefWidthProperty().bind(Bindings.multiply(0.25, mainPane.widthProperty()));

				StartWindow.getStage().setScene(scene);
				// Fix maximized
				StartWindow.getStage().setMaximized(false);
				StartWindow.getStage().setMaximized(true);
			}
		});
	}

	public Display(Environment environment, Metrics metrics, Boolean isDebugMode) {
		this.environment = environment;
		this.metrics = metrics;
		this.debug = isDebugMode;
	}
}