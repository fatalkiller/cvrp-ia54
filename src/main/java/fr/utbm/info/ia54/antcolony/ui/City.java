package fr.utbm.info.ia54.antcolony.ui;

import java.util.UUID;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class City 
{
	//An precise identifier in case two cities have the same name
	private UUID uuid;
	
	//What the city is called
	private String name;
	
	//Where the city will appear on the map
	private Double x;
	private Double y;
	
	
	
	
	public Circle getCircle()
	{
		Circle cityDisplay=new Circle();
		
		Color cityColor=Color.GRAY;
		Integer cityRadius=5;
		
		cityDisplay.setCenterX(x);
		cityDisplay.setCenterY(y);
		cityDisplay.setRadius(cityRadius);
		cityDisplay.setFill(cityColor);
		
		return cityDisplay;
	}
	
	public Text getCityText()
	{
		Text cityText=new Text();
		Double textHeight =new Double(20);

		cityText.setText(this.name);
		cityText.setX(this.x);
		cityText.setY(this.y-textHeight);
		
		return cityText;
	}
	
	
	public City() {
		super();
		this.uuid = null;
		this.name = null;
		this.x = null;
		this.y = null;
	}

	public City(UUID uuid, String name, Double x, Double y) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.x = x;
		this.y = y;
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
}
