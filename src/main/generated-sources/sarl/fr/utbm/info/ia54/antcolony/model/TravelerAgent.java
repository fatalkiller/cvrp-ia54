package fr.utbm.info.ia54.antcolony.model;

import com.google.common.base.Objects;
import fr.utbm.info.ia54.antcolony.model.City;
import fr.utbm.info.ia54.antcolony.model.Environment;
import fr.utbm.info.ia54.antcolony.model.ExitApplicationEvent;
import fr.utbm.info.ia54.antcolony.model.Metrics;
import fr.utbm.info.ia54.antcolony.model.Road;
import fr.utbm.info.ia54.antcolony.model.TravelsFinished;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.core.Destroy;
import io.sarl.core.Initialize;
import io.sarl.core.Lifecycle;
import io.sarl.core.Logging;
import io.sarl.lang.annotation.ImportedCapacityFeature;
import io.sarl.lang.annotation.PerceptGuardEvaluator;
import io.sarl.lang.annotation.SarlElementType;
import io.sarl.lang.annotation.SarlSpecification;
import io.sarl.lang.annotation.SyntheticMember;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.BuiltinCapacitiesProvider;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Skill;
import io.sarl.lang.util.ClearableReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author ma-pe
 */
@SuppressWarnings("potential_field_synchronization_problem")
@SarlSpecification("0.10")
@SarlElementType(19)
public class TravelerAgent extends Agent {
  private Environment env;
  
  private Metrics met;
  
  private City startingCity;
  
  private City currentCity;
  
  private Set<City> visitedCities;
  
  private List<Road> travelledRoads;
  
  private Long travelTime;
  
  private Boolean isDebugMode;
  
  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    Object _get = occurrence.parameters[0];
    this.env = ((Environment) _get);
    Object _get_1 = occurrence.parameters[1];
    this.met = ((Metrics) _get_1);
    Object _get_2 = occurrence.parameters[2];
    this.startingCity = ((City) _get_2);
    Object _get_3 = occurrence.parameters[3];
    this.isDebugMode = ((Boolean) _get_3);
    this.currentCity = this.startingCity;
    HashSet<City> _hashSet = new HashSet<City>();
    this.visitedCities = _hashSet;
    this.visitedCities.add(this.startingCity);
    ArrayList<Road> _arrayList = new ArrayList<Road>();
    this.travelledRoads = _arrayList;
    Long _long = new Long(0);
    this.travelTime = _long;
    Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$castSkill(Logging.class, (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = this.$getSkill(Logging.class)) : this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
    Integer _roundsElapsed = this.met.getRoundsElapsed();
    String _name = this.startingCity.getName();
    _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.setLoggingName(((("Traveler Agent - Round " + _roundsElapsed) + " - Starting city ") + _name));
    if (((this.isDebugMode) == null ? false : (this.isDebugMode).booleanValue())) {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1 = this.$castSkill(Logging.class, (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = this.$getSkill(Logging.class)) : this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER_1.info("Traveler agent spawned !");
    }
    this.travel();
  }
  
  protected void travel() {
    Road road = null;
    while (((this.visitedCities.size() != this.env.cities.size()) || (!Objects.equal(this.currentCity, this.startingCity)))) {
      {
        road = this.pickRoad();
        Long _timeTaken = road.getTimeTaken();
        this.travelTime = Long.valueOf((((this.travelTime) == null ? 0 : (this.travelTime).longValue()) + ((_timeTaken) == null ? 0 : (_timeTaken).longValue())));
        this.travelledRoads.add(road);
        this.visitedCities.addAll(road.getCities());
        this.currentCity = road.travel(this.currentCity);
      }
    }
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$castSkill(DefaultContextInteractions.class, (this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS = this.$getSkill(DefaultContextInteractions.class)) : this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS);
    TravelsFinished _travelsFinished = new TravelsFinished(this.travelledRoads, this.travelTime);
    _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_travelsFinished);
    Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$castSkill(Lifecycle.class, (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = this.$getSkill(Lifecycle.class)) : this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE);
    _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
  }
  
  @Pure
  protected Road pickRoad() {
    Road chosenRoad = null;
    List<Road> availableRoads = new ArrayList<Road>();
    Float reverseWeights = new Float(0);
    Float NormalizedReverseWeights = new Float(0);
    Float cumulativeNormalizedReverseWeights = new Float(0);
    Float totalWeights = new Float(0);
    Float totalReverseWeights = new Float(0);
    Float rng = Float.valueOf(new Random().nextFloat());
    availableRoads = this.env.getAdjacentRoads(this.currentCity);
    Road.sortRoadsByWeights(availableRoads);
    for (final Road road : availableRoads) {
      Long _weight = road.getWeight();
      totalWeights = Float.valueOf((((totalWeights) == null ? 0 : (totalWeights).floatValue()) + ((_weight) == null ? 0 : (_weight).longValue())));
    }
    for (final Road road_1 : availableRoads) {
      Long _weight_1 = road_1.getWeight();
      totalReverseWeights = Float.valueOf(((((totalReverseWeights) == null ? 0 : (totalReverseWeights).floatValue()) + ((totalWeights) == null ? 0 : (totalWeights).floatValue())) - ((_weight_1) == null ? 0 : (_weight_1).longValue())));
    }
    for (final Road road_2 : availableRoads) {
      if ((chosenRoad == null)) {
        if (((totalWeights != null && (totalWeights.floatValue() == 0)) || (totalReverseWeights != null && (totalReverseWeights.floatValue() == 0)))) {
          int _size = availableRoads.size();
          cumulativeNormalizedReverseWeights = Float.valueOf((((cumulativeNormalizedReverseWeights) == null ? 0 : (cumulativeNormalizedReverseWeights).floatValue()) + 
            (1f / _size)));
          if ((rng.floatValue() < cumulativeNormalizedReverseWeights.doubleValue())) {
            chosenRoad = road_2;
          }
        } else {
          Long _weight_2 = road_2.getWeight();
          reverseWeights = Float.valueOf((((totalWeights) == null ? 0 : (totalWeights).floatValue()) - ((_weight_2) == null ? 0 : (_weight_2).longValue())));
          NormalizedReverseWeights = Float.valueOf((((reverseWeights) == null ? 0 : (reverseWeights).floatValue()) / ((totalReverseWeights) == null ? 0 : (totalReverseWeights).floatValue())));
          cumulativeNormalizedReverseWeights = Float.valueOf((((cumulativeNormalizedReverseWeights) == null ? 0 : (cumulativeNormalizedReverseWeights).floatValue()) + ((NormalizedReverseWeights) == null ? 0 : (NormalizedReverseWeights).floatValue())));
          if ((rng.floatValue() < cumulativeNormalizedReverseWeights.doubleValue())) {
            chosenRoad = road_2;
          }
        }
      }
    }
    return chosenRoad;
  }
  
  private void $behaviorUnit$ExitApplicationEvent$1(final ExitApplicationEvent occurrence) {
    Lifecycle _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER = this.$castSkill(Lifecycle.class, (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = this.$getSkill(Lifecycle.class)) : this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE);
    _$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER.killMe();
  }
  
  private void $behaviorUnit$Destroy$2(final Destroy occurrence) {
    if (((this.isDebugMode) == null ? false : (this.isDebugMode).booleanValue())) {
      Logging _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER = this.$castSkill(Logging.class, (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) ? (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = this.$getSkill(Logging.class)) : this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
      _$CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER.info("The traveler agent was stopped.");
    }
  }
  
  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient ClearableReference<Skill> $CAPACITY_USE$IO_SARL_CORE_LOGGING;
  
  @SyntheticMember
  @Pure
  @Inline(value = "$castSkill(Logging.class, ($0$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || $0$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) ? ($0$CAPACITY_USE$IO_SARL_CORE_LOGGING = $0$getSkill(Logging.class)) : $0$CAPACITY_USE$IO_SARL_CORE_LOGGING)", imported = Logging.class)
  private Logging $CAPACITY_USE$IO_SARL_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_CORE_LOGGING);
  }
  
  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient ClearableReference<Skill> $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE;
  
  @SyntheticMember
  @Pure
  @Inline(value = "$castSkill(Lifecycle.class, ($0$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || $0$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) ? ($0$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = $0$getSkill(Lifecycle.class)) : $0$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE)", imported = Lifecycle.class)
  private Lifecycle $CAPACITY_USE$IO_SARL_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_CORE_LIFECYCLE);
  }
  
  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient ClearableReference<Skill> $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS;
  
  @SyntheticMember
  @Pure
  @Inline(value = "$castSkill(DefaultContextInteractions.class, ($0$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS == null || $0$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) ? ($0$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS = $0$getSkill(DefaultContextInteractions.class)) : $0$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS)", imported = DefaultContextInteractions.class)
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS);
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$2(occurrence));
  }
  
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ExitApplicationEvent(final ExitApplicationEvent occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ExitApplicationEvent$1(occurrence));
  }
  
  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TravelerAgent other = (TravelerAgent) obj;
    if (other.travelTime != this.travelTime)
      return false;
    if (other.isDebugMode != this.isDebugMode)
      return false;
    return super.equals(obj);
  }
  
  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + (int) (this.travelTime ^ (this.travelTime >>> 32));
    result = prime * result + (this.isDebugMode ? 1231 : 1237);
    return result;
  }
  
  @SyntheticMember
  public TravelerAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  @Deprecated
  public TravelerAgent(final BuiltinCapacitiesProvider provider, final UUID parentID, final UUID agentID) {
    super(provider, parentID, agentID);
  }
  
  @SyntheticMember
  @Inject
  public TravelerAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
