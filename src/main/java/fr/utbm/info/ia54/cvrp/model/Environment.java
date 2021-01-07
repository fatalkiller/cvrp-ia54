package fr.utbm.info.ia54.cvrp.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class Environment {

	public List<City> cities;
	public List<Road> roads;

	// this belongs in metrics and not in environment since its essentially just
	// information and not physical objects
	// However we're already scanning the bench data here so I guess we're storing
	// this here
	private String benchType;
	private String benchName;
	private String benchBestCost;

	public Environment(String type, String map) {
		this.benchType = type;
		this.benchName = map;
		this.benchBestCost = " - ";
		cities = new ArrayList<City>();
		roads = new ArrayList<Road>();
		if (this.benchName.equals("Default Map")) {
			makeDefaultMap();
		} else {
			loadBenchmarkFile();

			if (this.benchType.equals("CVRP")) {
				loadBenchmarkSolBestCost();
			}
		}
	}

	private void loadBenchmarkFile() {
		final boolean isCVRP = this.benchType.equals("CVRP");

		Path filePath = Paths.get("src/main/resources/fr/utbm/info/ia54/cvrp/benchmark", benchName + (isCVRP ? ".vrp" : ".tsp"));
		// FOR JAR FILE COMPILED
//		Path filePath = Paths.get(System.getProperty("user.dir"), "benchmark", benchName + (isCVRP ? ".vrp" : ".tsp"));

		File mapFile = new File(filePath.toString());

		Scanner scanner = null;
		String line = null;
		String data[] = null;
		boolean isDemandSection = false;
		boolean isDepotSection = false;
		Long capacity = new Long(0);
		Float maxX = new Float(0);
		Float maxY = new Float(0);
		City city;
		try {
			scanner = new Scanner(mapFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error reading map file.");
		}
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line != null && !line.isEmpty()) {
				data = line.split(" ");
				if (isCVRP) {
					if (data.length == 3 && data[0].equals("CAPACITY")) {
						capacity = new Long(data[2]);
					}
					if (line.equals("DEMAND_SECTION")) {
						isDemandSection = true;
					}

					if (line.equals("DEPOT_SECTION")) {
						isDepotSection = true;
					}
				}

				if (Character.isDigit(line.charAt(0))) {
					if (!isDemandSection) {
						// Import city information
						city = new City();
						city.setName("City " + data[0]);
						city.setX(Float.parseFloat(data[1]));
						city.setY(Float.parseFloat(data[2]));
						cities.add(city);

						maxX = Math.max(Float.parseFloat(data[1]), maxX);
						maxY = Math.max(Float.parseFloat(data[2]), maxY);

					} else {
						// Import capacity information
						city = cities.get(Integer.parseInt(data[0]) - 1);
						city.setCapacity(isDepotSection ? capacity : new Long("-" + data[1]));
					}
				}
			}

		}
		autoGenerateRoads();
		resizeMap(maxX, maxY);
	}

	private void loadBenchmarkSolBestCost() {
		Path filePath = Paths.get("src/main/resources/fr/utbm/info/ia54/cvrp/benchmark", benchName + ".sol");
		// FOR JAR FILE COMPILED
//		Path filePath = Paths.get(System.getProperty("user.dir"), "benchmark", benchName + ".sol");

		File mapSolFile = new File(filePath.toString());

		Scanner scanner = null;
		String line = null;
		String data[] = null;
		try {
			scanner = new Scanner(mapSolFile);
		} catch (FileNotFoundException e) {
			System.out.println("Error reading map file.");
		}

		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line != null && !line.isEmpty()) {
				data = line.split(" ");
				if (data[0].equals("Cost")) {
					this.benchBestCost = data[1];
				}
			}
		}
	}

	// Generates roads with timeTaken based on distance
	public void autoGenerateRoads() {
		Road road;
		Float dist;
		int i, j;

		for (i = 0; i < this.cities.size(); i++) {
			for (j = i + 1; j < this.cities.size(); j++) {
				road = new Road();
				road.setCity1(getCityByName(this.cities.get(i).getName()));
				road.setCity2(getCityByName(this.cities.get(j).getName()));
				dist = new Float(Math.sqrt(Math.pow(this.cities.get(i).getX() - this.cities.get(j).getX(), 2)
						+ Math.pow(this.cities.get(i).getY() - this.cities.get(j).getY(), 2)));
				road.setTimeTaken(new Float(dist));
				roads.add(road);
			}
		}
	}

	public void resizeMap(Float maxX, Float maxY) {
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		Float scale = new Float((Math.min(maxX / screenBounds.getMaxX(), maxY / screenBounds.getMaxY()) * 2));
		for (City city : cities) {
			city.setX(city.getX() / scale);
			city.setY(city.getY() / scale);
		}
	}

	public void makeDefaultMap() {
		City city;

		city = new City();
		city.setName("Strasbourg");
		city.setX(new Float(800));
		city.setY(new Float(200));
		cities.add(city);

		city = new City();
		city.setName("Paris");
		city.setX(new Float(400));
		city.setY(new Float(300));
		cities.add(city);

		city = new City();
		city.setName("Belfort");
		city.setX(new Float(700));
		city.setY(new Float(400));
		cities.add(city);

		city = new City();
		city.setName("Toulouse");
		city.setX(new Float(300));
		city.setY(new Float(600));
		cities.add(city);

		city = new City();
		city.setName("Marseille");
		city.setX(new Float(650));
		city.setY(new Float(700));
		cities.add(city);

		Road road;

		road = new Road();
		road.setCity1(getCityByName("Strasbourg"));
		road.setCity2(getCityByName("Paris"));
		road.setTimeTaken(new Float(270));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Strasbourg"));
		road.setCity2(getCityByName("Belfort"));
		road.setTimeTaken(new Float(105));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Strasbourg"));
		road.setCity2(getCityByName("Toulouse"));
		road.setTimeTaken(new Float(600));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Strasbourg"));
		road.setCity2(getCityByName("Marseille"));
		road.setTimeTaken(new Float(480));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Paris"));
		road.setCity2(getCityByName("Belfort"));
		road.setTimeTaken(new Float(300));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Paris"));
		road.setCity2(getCityByName("Toulouse"));
		road.setTimeTaken(new Float(420));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Paris"));
		road.setCity2(getCityByName("Marseille"));
		road.setTimeTaken(new Float(390));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Belfort"));
		road.setCity2(getCityByName("Toulouse"));
		road.setTimeTaken(new Float(480));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Belfort"));
		road.setCity2(getCityByName("Marseille"));
		road.setTimeTaken(new Float(420));
		roads.add(road);

		road = new Road();
		road.setCity1(getCityByName("Toulouse"));
		road.setCity2(getCityByName("Marseille"));
		road.setTimeTaken(new Float(240));
		roads.add(road);

	}

	public City getCityByID(UUID id) {
		City resultCity = null;

		for (City city : cities) {
			if (city.getUuid().equals(id)) {
				resultCity = city;
			}
		}

		return resultCity;
	}

	public City getCityByName(String name) {
		City resultCity = null;

		for (City city : cities) {
			if (city.getName().equals(name)) {
				resultCity = city;
			}
		}

		return resultCity;
	}

	public List<Road> getAdjacentRoads(City city) {
		List<Road> roads = new ArrayList<Road>();

		for (Road road : this.roads) {
			if (road.getCity1() == city || road.getCity2() == city) {
				roads.add(road);
			}
		}

		return roads;
	}

	public Road getRoadBetweenTwoCities(City city1, City city2) {
		Road resRoad = null;

		for (Road road : this.roads) {
			if (road.getCity1() == city1 && road.getCity2() == city2) {
				resRoad = road;
			} else if (road.getCity1() == city2 && road.getCity2() == city1) {
				resRoad = road;
			}
		}

		return resRoad;
	}

	public void updateWeights() {
		for (Road road : roads) {

			// Only include the future weight if this road was actually visited.
			// This prevents the division by 0 into infinity issue
			// According to the wiki we should still evaporate, though
			// This is a huge improvement as not having to set futureweight to 1 allows for
			// proportional control of the weights
			// I think having regular weights set to 1 should still be fine as its only a
			// starting variable and doesnt recur
			if (road.getFutureWeight() != 0) {
				road.setWeight(((1 - SimuParameters.evaporationProportion) * road.getWeight())
						+ (SimuParameters.evaporationConstant / road.getFutureWeight()));
			} else {
				road.setWeight((1 - SimuParameters.evaporationProportion) * road.getWeight());
			}

			road.setFutureWeight(new Float(0));
		}
	}

	// Display stuff

	public List<Circle> getCitiesRepresentation() {
		List<Circle> citiesRep = new ArrayList<Circle>();

		for (City city : cities) {
			citiesRep.add(city.getCircle());
		}

		return citiesRep;
	}

	public List<Line> getRoadsRepresentation() {
		List<Line> roadsRep = new ArrayList<Line>();

		for (Road road : roads) {
			roadsRep.add(road.getLine());
		}

		return roadsRep;
	}

	public List<Text> getCitiesNames() {
		List<Text> citiesNames = new ArrayList<Text>();

		for (City city : cities) {
			citiesNames.add(city.getCityText());
		}

		return citiesNames;
	}

	public List<Text> getRoadsWeights() {
		List<Text> roadsWeights = new ArrayList<Text>();

		for (Road road : roads) {
			roadsWeights.add(road.getRoadText());
		}

		return roadsWeights;
	}

	// Autogen

	public List<City> getCities() {
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<Road> getRoads() {
		return roads;
	}

	public void setRoads(List<Road> roads) {
		this.roads = roads;
	}

	public String getBenchType() {
		return benchType;
	}

	public String getBenchName() {
		return benchName;
	}

	public String getBenchBestCost() {
		return benchBestCost;
	}
}
