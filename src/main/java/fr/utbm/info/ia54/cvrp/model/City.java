package fr.utbm.info.ia54.cvrp.model;

import java.util.UUID;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class City {
	// An precise identifier in case two cities have the same name
	private UUID uuid;

	// What the city is called
	private String name;

	// Where the city will appear on the map
	private Double x;
	private Double y;

	// How much does the city cost to deliver ?
	// Positive value counts as a refill station
	// We assume all refill stations will refill the same value which is the car's
	// max capacity
	// (It should still work otherwise though)
	// Negative value counts as a delivery cost
	private Long capacity;

	public Circle getCircle() {
		Circle cityDisplay = new Circle();

		Color cityColor = Color.GRAY;
		// To distinguish the starting city
		if (this.capacity > 0) {
			cityColor = Color.GREENYELLOW;			
		}
		Integer cityRadius = 5;

		cityDisplay.setCenterX(x);
		cityDisplay.setCenterY(y);
		cityDisplay.setRadius(cityRadius);
		cityDisplay.setFill(cityColor);

		return cityDisplay;
	}

	public Text getCityText() {
		Text cityText = new Text();
		Double textHeight = new Double(20);

		cityText.setText(this.name);
		cityText.setX(this.x);
		cityText.setY(this.y - textHeight);

		return cityText;
	}

	public City() {
		super();
		this.uuid = null;
		this.name = null;
		this.x = null;
		this.y = null;
		// Gotta put this at 0 so TSP still works
		this.capacity = new Long(0);
	}

	public City(UUID uuid, String name, Double x, Double y, Long capacity) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.x = x;
		this.y = y;
		this.capacity = capacity;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}
}
