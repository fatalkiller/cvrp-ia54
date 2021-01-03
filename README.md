# Ant Colony Algorithm for Capacitated Vehicle Routing Problems
#### (ACO for CVRP)

The VRP concerns the service of a delivery company. How things are delivered from one or more depots which has a given set of home vehicles and operated by a set of drivers who can move on a given road network to a set of customers. It asks for a determination of a set of routes, S, (one route for each vehicle that must start and finish at its own depot) such that all customers' requirements and operational constraints are satisfied and the global transportation cost is minimized. This cost may be monetary, distance or otherwise.

The road network can be described using a graph where the arcs are roads and vertices are junctions between them. The arcs may be directed or undirected due to the possible presence of one way streets or different costs in each direction. Each arc has an associated cost which is generally its length or travel time which may be dependent on vehicle type

For the CVRP problems, the vehicles have a limited carrying capacity of the goods that must be delivered.

(from https://en.wikipedia.org/wiki/Vehicle_routing_problem)

Our program is a simulator to find the best path to deliver every cities.

### Tech

Our program uses different frameworks in Java : 

* [JavaFX](https://www.oracle.com/fr/java/technologies/javase/javafx-overview.html) -  creating and delivering desktop applications
* [Sarl](http://www.sarl.io/) - General-purpose Agent-Oriented Programming Language.

### Installation 

On windows : 
- Install Java 8 JDK https://www.oracle.com/fr/java/technologies/javase/javase-jdk8-downloads.html
- Download and extract the last release archive with the executable and the benchmark files
- Run the last release file CVRP_IA54.jar





License
----
MIT

