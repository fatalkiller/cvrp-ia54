package fr.utbm.info.ia54.cvrp.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class Metrics {
	private Text display;

	private String type;
	private Calendar startTime;
	private String fastestTime;
	private String fastestPath;
	private List<Road> fastestPathObj; // Use this to know which path to highlight
	private AtomicInteger roundsElapsed = new AtomicInteger();
	private AtomicInteger totalCities = new AtomicInteger();
	private AtomicInteger activeAgents = new AtomicInteger();

	public Metrics(String type, Environment env, Integer simultaneousAgents) {
		this.type = type;
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		display = new Text();
		display.setX(screenBounds.getMaxX() * 0.75);
		display.setY(25);

		startTime = Calendar.getInstance();
		fastestTime = "TBD";
		fastestPath = "TBD";
		fastestPathObj = new ArrayList<Road>();
		roundsElapsed.set(0);
		if(type.equals("TSP"))
		{
			totalCities.set(env.cities.size());
		}
		else if(type.equals("CVRP"))
		{
			totalCities.set(simultaneousAgents);
		}
		activeAgents.set(0);
	}

	public Text getMetrics() {
		updateDisplay();
		return this.display;
	}

	public void updateDisplay() {

		display.setText("\n\n" + "Metrics :\n\n" + "Elapsed time : "
				+ (Calendar.getInstance().getTime().getTime() - startTime.getTime().getTime()) / 1000 + " s\n"
				+ "Elapsed rounds : " + roundsElapsed + "\n" + "Active agents : " + activeAgents + "/ " + totalCities
				+ "\n" + "Fastest time : " + fastestTime + " Arbitrary Units\n" + "Fastest path : " + fastestPath
				+ "\n");
	}

	public void increaseElapsedRounds() {
		this.roundsElapsed.incrementAndGet();
	}

	public void setFormattedFastestPath(List<Road> pathTaken) {
		this.fastestPathObj = pathTaken;
		String fastestPathString = "\n";
		City currentCity = null;
		Long capacity = new Long(0);
		Long maxCapacity;

		// Determining the first city making the assumption that the agent didnt take
		// the same road twice in a row
		// Since only the fastest of times get processed here, this is a fair assumption
		// Altho it might cause some problems on some maps
		
		
		
		if (pathTaken.get(1).getCities().contains(pathTaken.get(0).getCity1())) {
			currentCity = pathTaken.get(0).getCity2();
		} else {
			currentCity = pathTaken.get(0).getCity1();
		}
		
		maxCapacity = currentCity.getCapacity();
		
		if (capacity + currentCity.getCapacity() >= 0) {
			capacity = Math.min(capacity + currentCity.getCapacity(), maxCapacity);			
		}
		
		fastestPathString += currentCity.getName() + " ; Charge : " + capacity;
		
		for (Road road : pathTaken) {
			if (currentCity == road.getCity1()) {
				currentCity = road.getCity2();
			} else {
				currentCity = road.getCity1();
			}
			if (capacity + currentCity.getCapacity() >= 0) {
				capacity = Math.min(capacity + currentCity.getCapacity(), maxCapacity);				
			}
			fastestPathString += "\n-> " + currentCity.getName() + " ; Charge : " + capacity;
		}
		this.fastestPath = fastestPathString;
	}

	public List<Line> getFastestRoadsRepresentation() {
		List<Line> roadsRep = new ArrayList<Line>();
		if (type.equals("TSP")) {
			for (Road road : fastestPathObj) {
				roadsRep.add(road.getHighlightedLine());
			}
		} else if (type.equals("CVRP")) {
			List<Color> colorList = new ArrayList<Color>();
			int colorIndex = 0;
			colorList.add(Color.RED);
			colorList.add(Color.BLUE);
			colorList.add(Color.GREEN);
			colorList.add(Color.YELLOW);
			colorList.add(Color.PURPLE);
			colorList.add(Color.AQUA);
			colorList.add(Color.BLUEVIOLET);
			colorList.add(Color.BROWN);
			colorList.add(Color.CORAL);
			colorList.add(Color.DARKCYAN);
			colorList.add(Color.GOLDENROD);
			colorList.add(Color.ROYALBLUE);
			// Hopefully enough

			int i;
			for (i = 0; i < fastestPathObj.size(); i++) {
				Line roadLine = new Line();
				// Assuming the depot is city 1, they should always be city1 in roads because of
				// how roads are generated.
				if (i > 0 && fastestPathObj.get(i).getCity1().getCapacity() > 0
						&& fastestPathObj.get(i - 1).getCity1().getCapacity() > 0) {
					colorIndex++;
					if (colorIndex > colorList.size() - 1)
						colorIndex = 0;
				}

				roadLine.setStroke(colorList.get(colorIndex));
				roadLine.setStartX(fastestPathObj.get(i).getCity1().getX());
				roadLine.setStartY(fastestPathObj.get(i).getCity1().getY());
				roadLine.setEndX(fastestPathObj.get(i).getCity2().getX());
				roadLine.setEndY(fastestPathObj.get(i).getCity2().getY());

				roadsRep.add(roadLine);
			}
		}
		return roadsRep;
	}
	
	// NOT USED => use if graph created in view
	public List<List<Road>> getFastestRoads() {
		List<List<Road>> listRoutes = new ArrayList<List<Road>>();
		// Not implemented for simple TSP
		if (type.equals("CVRP")) {
			int i;
			
			// Initialize first route
			int routeIndex = 0;
			listRoutes.add(new ArrayList<Road>());
			
			for (i = 0; i < fastestPathObj.size(); i++) {
				// Assuming the depot is city 1, they should always be city1 in roads because of
				// how roads are generated.
				if (i > 0 && fastestPathObj.get(i).getCity1().getCapacity() > 0
						&& fastestPathObj.get(i - 1).getCity1().getCapacity() > 0) {
					routeIndex++;
					listRoutes.add(new ArrayList<Road>());
				}

				// Add current road to corresponding route
				listRoutes.get(routeIndex).add(fastestPathObj.get(i));
			}
		}
		return listRoutes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Text getDisplay() {
		return display;
	}

	public void setDisplay(Text display) {
		this.display = display;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public String getFastestTime() {
		return fastestTime;
	}

	public void setFastestTime(String fastestTime) {
		this.fastestTime = fastestTime;
	}

	public String getFastestPath() {
		return fastestPath;
	}

	public void setFastestPath(String fastestPath) {
		this.fastestPath = fastestPath;
	}

	public List<Road> getFastestPathObj() {
		return fastestPathObj;
	}

	public void setFastestPathObj(List<Road> fastestPathObj) {
		this.fastestPathObj = fastestPathObj;
	}

	public AtomicInteger getRoundsElapsed() {
		return roundsElapsed;
	}

	public void setRoundsElapsed(Integer roundsElapsed) {
		this.roundsElapsed.set(roundsElapsed);
	}

	public AtomicInteger getTotalCities() {
		return totalCities;
	}

	public void setTotalCities(Integer totalCities) {
		this.totalCities.set(totalCities);
	}

	public AtomicInteger getActiveAgents() {
		return activeAgents;
	}

	public void setActiveAgents(Integer activeAgents) {
		this.activeAgents.set(activeAgents);
	}

	public void increaseActiveAgents() {
		this.activeAgents.incrementAndGet();
	}

	public void decreaseActiveAgents() {
		this.activeAgents.decrementAndGet();
	}
}
