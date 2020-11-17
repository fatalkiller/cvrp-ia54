package fr.utbm.info.ia54.antcolony.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class Metrics
{
	private Text display;

	private Calendar startTime;
	private String fastestTime;
	private String fastestPath;
	//TODO : try replacing with concurrentList
	//TODO : Make it a set of lists to implement CVRP version
	private List<Road> fastestPathObj; //Use this to know which path to highlight
	private Integer roundsElapsed;
	private Integer totalCities;
	private Integer activeAgents;
	
	public Metrics(Environment env)
	{
		Rectangle2D	screenBounds = Screen.getPrimary().getBounds();
		display=new Text();
		display.setX(screenBounds.getMaxX()*0.75);
		display.setY(25);
		
		startTime = Calendar.getInstance();
		fastestTime = "TBD";
		fastestPath = "TBD";
		fastestPathObj = new ArrayList<Road>();
		roundsElapsed = 0;
		totalCities = env.cities.size();
		activeAgents = 0;
	}
	
	public Text getMetrics()
	{
		updateDisplay();
		return this.display;
	}
	
	public void updateDisplay()
	{
		
		display.setText("\n\n"
			+"Metrics :\n\n"
			+ "Elapsed time : " + (Calendar.getInstance().getTime().getTime() - startTime.getTime().getTime())/1000 + " s\n"
			+ "Elapsed rounds : " + roundsElapsed + "\n"
			+ "Active agents : " + activeAgents + "/ "+ totalCities + "\n"
			+ "Fastest time : " + fastestTime + " Arbitrary Units\n"
			+ "Fastest path : " + fastestPath + "\n"
			);
	}

	public void increaseElapsedRounds()
	{
		this.roundsElapsed++;
	}
	


	public void setFormattedFastestPath(List<Road> pathTaken) {
		this.fastestPathObj=pathTaken;
		String fastestPathString = "\n";
		City currentCity = null;

		//Determining the first city making the assumption that the agent didnt take the same road twice in a row
		//Since only the fastest of times get processed here, this is a fair assumption
		//Altho it might cause some problems on some maps
		if(pathTaken.get(1).getCities().contains(pathTaken.get(0).getCity1()))
		{
			currentCity=pathTaken.get(0).getCity2();
		}
		else
		{
			currentCity=pathTaken.get(0).getCity1();
		}
		
		fastestPathString+=currentCity.getName();
		for(Road road : pathTaken)
		{
			if(currentCity==road.getCity1())
			{
				currentCity=road.getCity2();
			}
			else
			{
				currentCity=road.getCity1();
			}
			fastestPathString+="\n-> "+currentCity.getName();
		}
		
		this.fastestPath = fastestPathString;
	}
	
	
	public List<Line> getFastestRoadsRepresentation()
	{
		List<Line> roadsRep=new ArrayList<Line>();
		
		for(Road road : fastestPathObj)
		{
			roadsRep.add(road.getHighlightedLine());
		}
		
		return roadsRep;
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

	public Integer getRoundsElapsed() {
		return roundsElapsed;
	}

	public void setRoundsElapsed(Integer roundsElapsed) {
		this.roundsElapsed = roundsElapsed;
	}

	public Integer getTotalCities() {
		return totalCities;
	}

	public void setTotalCities(Integer totalCities) {
		this.totalCities = totalCities;
	}

	public Integer getActiveAgents() {
		return activeAgents;
	}

	public void setActiveAgents(Integer activeAgents) {
		this.activeAgents = activeAgents;
	}

	public void increaseActiveAgents() {
		this.activeAgents++;
	}

	public void decreaseActiveAgents() {
		this.activeAgents--;
	}
	
}
