package fr.utbm.info.ia54.cvrp.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import fr.utbm.info.ia54.cvrp.model.Environment;
import fr.utbm.info.ia54.cvrp.model.Metrics;
import javafx.application.Platform;

public class Display {

	private Environment environment;
	private Metrics metrics;
	private boolean debug;

	public void changeDisplayFrameTitle(String title) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StartWindow.getStage().setTitle(title);
			}
		});
	}

	public void changeDisplayFrameSize(Integer width, Integer height) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				StartWindow.getStage().setWidth(width);
				StartWindow.getStage().setHeight(height);
				StartWindow.getStage().centerOnScreen();
			}
		});
	}

	public void displaySimulationFrame() {

		// I dont get it but somehow if I dont include the group stuff here I get errors
		// in the console
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				Group g = new Group();

				// Things to display

				g.getChildren().addAll(environment.getCitiesRepresentation());
				if (debug) {
					g.getChildren().addAll(environment.getCitiesNames());
					// No need to display all possible roads, itd just look cluttered
					g.getChildren().addAll(environment.getRoadsRepresentation());
				}
				g.getChildren().addAll(metrics.getFastestRoadsRepresentation());

				if (debug) {
					g.getChildren().addAll(environment.getRoadsWeights());
				}

				// Also display some metrics & stats
				g.getChildren().addAll(metrics.getMetrics());

				// Assuming stuff is already being displayed
				// stage.show();

				Scene scene = new Scene(g, Screen.getPrimary().getBounds().getWidth() * 3 / 4,
						Screen.getPrimary().getBounds().getHeight() * 3 / 4);

				StartWindow.getStage().setScene(scene);
			}
		});
	}

	public Display(Environment environment, Metrics metrics, Boolean isDebugMode) {
		this.environment = environment;
		this.metrics = metrics;
		this.debug = isDebugMode;
	}
}