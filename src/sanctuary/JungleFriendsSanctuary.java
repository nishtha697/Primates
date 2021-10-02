package sanctuary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import housing.attributes.HousingType;
import monkey.attributes.FavoriteFood;
import monkey.attributes.HealthStatus;
import monkey.attributes.MonkeySize;
import monkey.attributes.Sex;
import monkey.attributes.Species;

/**
 * This class implements the interface {@link Sanctuary} and represents a sanctuary that provides
 * care for New World primates.
 */
public class JungleFriendsSanctuary implements Sanctuary {
  private static final int DEFAULT_ZERO = 0;
  private final int[] sizeOfEnclosures;
  private final List<Housing> locations;
  private final List<Primate> monkeys;
  private final List<Primate> alumniMonkeys;
  private int numOfIsolationCages;
  private int numOfEnclosures;

  /**
   * Constructs the JungleFriendsSanctuary.
   *
   * @param numOfIsolationCages number of isolation cages. This cannot be zero or negative.
   * @param numOfEnclosures     number of enclosures. This cannot be zero or negative.
   * @param sizeOfEnclosures    size of enclosures. This should be an array of size
   *                            {@code numberOfEnclosures} and all the values must be greater than
   *                            0.
   */
  public JungleFriendsSanctuary(int numOfIsolationCages, int numOfEnclosures,
                                int[] sizeOfEnclosures) {
    if (numOfEnclosures <= 0 || numOfIsolationCages <= 0) {
      throw new IllegalArgumentException("Number of isolation cages or enclosures cannot be less "
              + "than or equal to 0");
    }
    if (sizeOfEnclosures.length != numOfEnclosures || Arrays.stream(sizeOfEnclosures)
            .anyMatch(value -> value <= 0)) {
      throw new IllegalArgumentException("Size of enclosures should be an array of size "
              + numOfEnclosures + " and all the values must be greater than 0.");
    }
    this.numOfIsolationCages = numOfIsolationCages;
    this.numOfEnclosures = numOfEnclosures;
    this.sizeOfEnclosures = sizeOfEnclosures;
    this.locations = createLocations(this.numOfEnclosures, this.sizeOfEnclosures,
            this.numOfIsolationCages, DEFAULT_ZERO, DEFAULT_ZERO);
    this.monkeys = new ArrayList<>();
    this.alumniMonkeys = new ArrayList<>();
  }

  @Override
  public int getTotalNumOfIsolationCages() {
    return this.numOfIsolationCages;
  }

  @Override
  public int getTotalNumOfEnclosures() {
    return this.numOfEnclosures;
  }

  @Override
  public List<Housing> getHousings() {
    return new ArrayList<>(this.locations);
  }

  @Override
  public Set<UUID> getHousingIds() {
    Set<UUID> housingIds = new HashSet<>();
    this.locations.forEach(housing -> housingIds.add(housing.getId()));
    return housingIds;
  }

  @Override
  public void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures,
                          int[] sizeOfEnclosures) throws IllegalArgumentException {
    if (numOfNewIsolationCages < 0 || numOfNewEnclosures < 0) {
      throw new IllegalArgumentException("Number of new isolation cages or number of new enclosures"
              + " cannot be less than 0.");
    }
    if (sizeOfEnclosures.length != numOfNewEnclosures || Arrays.stream(sizeOfEnclosures)
            .anyMatch(value -> value <= 0)) {
      throw new IllegalArgumentException("Size of enclosures should be an array of size "
              + numOfNewEnclosures + " and all the values must be greater than 0.");
    }
    this.locations.addAll(createLocations(numOfNewEnclosures, this.sizeOfEnclosures,
            numOfNewIsolationCages, this.numOfEnclosures, this.numOfIsolationCages));
    this.numOfIsolationCages += numOfNewIsolationCages;
    this.numOfEnclosures += numOfNewEnclosures;
  }

  @Override
  public Map<Primate, Housing> addMonkey(String name, MonkeySize size, double weight, int age,
                                         Species species, Sex sex, FavoriteFood favoriteFood,
                                         HealthStatus healthStatus, UUID isolationId)
          throws IllegalArgumentException, IllegalStateException {
    Primate newMonkey;
    Housing isolation;

    //checking if any isolation cages are available
    if (isIsolationCageAvailable(null) != null) {
      if (isolationId != null) {
        isolation = this.getHousings().stream().filter(location ->
                        location.getId().toString().equals(isolationId.toString())).findFirst()
                .orElse(null);
        if (isolation == null || isolation.getHousingType() != HousingType.ISOLATION) {
          throw new IllegalArgumentException("Monkey not added to the sanctuary. The housing id "
                  + isolationId + "is not a valid isolation cage id.");
        } else if (!isolation.getResidents().isEmpty()) {
          throw new IllegalStateException("Monkey not added to the sanctuary. The isolation cage "
                  + isolationId + " is occupied. Try again with another isolation cage.");
        }
        newMonkey = new Monkey(name, size, weight, age, species, sex, favoriteFood, healthStatus);
        this.monkeys.add(newMonkey);
        ((Isolation) isolation).addMonkey(newMonkey);
      } else {
        newMonkey = new Monkey(name, size, weight, age, species, sex, favoriteFood, healthStatus);
        this.monkeys.add(newMonkey);
        isolation = moveMonkeyToIsolation(newMonkey);
      }
    } else {
      throw new IllegalStateException("No more isolation cages available. New monkey cannot "
              + "be added to the sanctuary. Please use exchange agreements to find a suitable home "
              + "for this monkey.");
    }
    return Collections.singletonMap(newMonkey, isolation);
  }

  @Override
  public void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    boolean shouldMonkeyMoveToIsolation = false;
    Housing currentHousing = null;
    nullCheck(monkey, "Monkey");
    ((Monkey) monkey).updateHealthStatus(updatedHealthStatus);
    if (monkey.getHealthStatus() == HealthStatus.UNHEALTHY) {
      for (Housing housing : this.getHousings()) {
        if (housing.getHousingType() == HousingType.ENCLOSURE
                && !housing.getResidents().isEmpty()) {
          for (Primate resident : housing.getResidents()) {
            if (resident.equals(monkey)) {
              shouldMonkeyMoveToIsolation = true;
              currentHousing = housing;
            }
          }
        }
      }
    }
    if (shouldMonkeyMoveToIsolation) {
      try {
        this.moveMonkeyToIsolation(monkey);
      } catch (IllegalStateException e) {
        this.removeMonkeyFromCurrentLocation(monkey);
        throw new IllegalStateException("Unhealthy monkey " + monkey.getName() + "("
                + monkey.getId() + ") cannot stay in Enclosure. Monkey is removed from enclosure "
                + currentHousing.getId() + " but no more Isolation cages are left. Please move "
                + "monkey to a suitable place.");
      }
    }
  }

  @Override
  public void updateMonkeySize(MonkeySize updatedSize, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    this.nullCheck(monkey, "Monkey");
    ((Monkey) monkey).updateSize(updatedSize);
    this.getHousings().forEach(housing -> {
      if (housing.getHousingType() == HousingType.ENCLOSURE) {
        if (((Enclosure) housing).getAvailableCapacity() < (updatedSize.getSpace()
                - monkey.getSize().getSpace())) {
          try {
            this.moveMonkeyToEnclosure(monkey);
          } catch (IllegalStateException e) {
            if (e.getMessage().equals("No space left in Enclosures for " + monkey.getId()
            )) {
              this.removeMonkeyFromCurrentLocation(monkey);
              throw new IllegalStateException("Monkey size increased and no more space left for "
                      + "monkey in any Enclosures. Please find a suitable place for monkey "
                      + monkey.getName() + "(" + monkey.getId() + ")using exchange agreement");
            } else {
              throw e;
            }
          }
        }
      }
    });
  }

  @Override
  public void updateMonkeyWeight(double updatedWeight, Primate monkey)
          throws IllegalArgumentException {
    this.nullCheck(monkey, "Monkey");
    ((Monkey) monkey).updateWeight(updatedWeight);
  }

  @Override
  public void updateMonkeyAge(int updatedAge, Primate monkey)
          throws IllegalArgumentException {
    this.nullCheck(monkey, "Monkey");
    ((Monkey) monkey).updateAge(updatedAge);
  }

  @Override
  public void updateFavoriteFood(FavoriteFood favoriteFood, Primate monkey)
          throws IllegalArgumentException {
    this.nullCheck(monkey, "Monkey");
    ((Monkey) monkey).updateFavoriteFood(favoriteFood);
  }

  @Override
  public void removeMonkey(Primate monkey) throws IllegalArgumentException {
    this.nullCheck(monkey, "Monkey");
    if (this.getMonkeys().stream()
            .noneMatch(mon -> mon.equals(monkey))) {
      throw new IllegalArgumentException("Monkey " + monkey.getName() + "(" + monkey.getId()
              + ") does not exist in sanctuary.");
    }
    this.removeMonkeyFromCurrentLocation(monkey);
    this.monkeys.removeIf(mon -> mon.equals(monkey));
    this.alumniMonkeys.add(monkey);
  }

  @Override
  public List<Primate> getMonkeys() {
    return new ArrayList<>(this.monkeys);
  }

  @Override
  public List<UUID> getMonkeyIds() {
    List<UUID> monkeyIds = new ArrayList<>();
    this.monkeys.forEach(monkey -> monkeyIds.add(monkey.getId()));
    return monkeyIds;
  }

  @Override
  public List<Primate> getAlumniMonkeys() {
    return new ArrayList<>(this.alumniMonkeys);
  }

  @Override
  public void moveMonkey(UUID housingId, Primate monkey) throws IllegalStateException,
          IllegalArgumentException {
    this.nullCheck(housingId, "Housing id");
    this.nullCheck(monkey, "Monkey");
    Housing housing = this.locations.stream().filter(house -> house.getId().toString()
            .equals(housingId.toString())).findFirst().orElse(null);
    if (housing == null) {
      throw new IllegalArgumentException(housingId + " : Location does not exist. Move monkey "
              + monkey.getName() + "(" + monkey.getId() + ") to valid location.");
    }
    if (housing.isLocationAvailable(monkey)) {
      this.checkIfUnhealthyMonkeyBeingMovedToEnclosure(housing, monkey);
      this.checkIfMonkeyExitsInSanctuary(monkey);
      this.removeMonkeyFromCurrentLocation(monkey);
      if (housing.getHousingType() == HousingType.ENCLOSURE) {
        ((Enclosure) housing).addMonkey(monkey);
      } else if (housing.getHousingType() == HousingType.ISOLATION) {
        ((Isolation) housing).addMonkey(monkey);
      }
    } else {
      throw new IllegalStateException("The location " + housingId + " is not available for "
              + monkey.getName() + "(" + monkey.getId() + "). Try another location.");
    }
  }

  @Override
  public Housing moveMonkeyToIsolation(Primate monkey) throws IllegalArgumentException,
          IllegalStateException {
    this.nullCheck(monkey, "Monkey");
    this.checkIfMonkeyExitsInSanctuary(monkey);
    Isolation isolation = isIsolationCageAvailable(monkey);
    if (isolation != null) {
      this.removeMonkeyFromCurrentLocation(monkey);
      isolation.addMonkey(monkey);
    } else {
      throw new IllegalStateException("No more Isolation cages left. Cannot house "
              + monkey.getId());
    }
    return isolation;
  }

  @Override
  public Housing moveMonkeyToEnclosure(Primate monkey) throws IllegalArgumentException,
          IllegalStateException {
    this.nullCheck(monkey, "Monkey");
    Enclosure enclosure = isEnclosureAvailable(monkey);
    if (enclosure != null) {
      checkIfUnhealthyMonkeyBeingMovedToEnclosure(enclosure, monkey);
      this.checkIfMonkeyExitsInSanctuary(monkey);
      this.removeMonkeyFromCurrentLocation(monkey);
      enclosure.addMonkey(monkey);
    } else {
      throw new IllegalStateException("No space left in Enclosures for "
              + monkey.getId());
    }
    return enclosure;
  }

  @Override
  public Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(UUID enclosureId)
          throws IllegalArgumentException {
    this.nullCheck(enclosureId, "Enclosure id");
    boolean validEnclosureId = false;
    Map<String, Map<Sex, FavoriteFood>> enclosureSign = new HashMap<>();
    for (Housing location : this.getHousings()) {
      if (location.getId().toString().equals(enclosureId.toString()) && location.getHousingType()
              == HousingType.ENCLOSURE) {
        enclosureSign = ((Enclosure) location).getEnclosureSign();
        validEnclosureId = true;
      }
    }
    if (!validEnclosureId) {
      throw new IllegalArgumentException(enclosureId + ": Enclosure id does not exist.");
    }
    return enclosureSign;
  }

  @Override
  public Map<String, Map<HousingType, UUID>> getAllMonkeysWithLocations() {
    Map<String, Map<HousingType, UUID>> monkeysWithLocations = new TreeMap<>();
    for (Housing location : this.locations) {
      if (!location.getResidents().isEmpty()
              && location.getResidents().get(0) != null) {
        if (location.getHousingType() == HousingType.ISOLATION) {
          monkeysWithLocations.put(location.getResidents().get(0).getName(),
                  Collections.singletonMap(HousingType.ISOLATION, location.getId()));
        } else if (location.getHousingType() == HousingType.ENCLOSURE) {
          for (Primate monkey : location.getResidents()) {
            monkeysWithLocations.put(monkey.getName(),
                    Collections.singletonMap(HousingType.ENCLOSURE, location.getId()));
          }
        }
      }
    }
    return monkeysWithLocations;
  }

  @Override
  public Map<Species, Map<HousingType, List<UUID>>> getSpeciesWithLocations() {
    Map<Species, Map<HousingType, List<UUID>>> speciesWithLocations = new TreeMap<>(Comparator
            .comparing(Enum::toString));
    Map<HousingType, List<UUID>> marmosetLocations = new HashMap<>();
    Map<HousingType, List<UUID>> capuchinLocations = new HashMap<>();
    Map<HousingType, List<UUID>> howlerLocation = new HashMap<>();
    Map<HousingType, List<UUID>> nightLocations = new HashMap<>();
    Map<HousingType, List<UUID>> sakiLocations = new HashMap<>();
    Map<HousingType, List<UUID>> spiderLocations = new HashMap<>();
    Map<HousingType, List<UUID>> squirrelLocations = new HashMap<>();
    Map<HousingType, List<UUID>> tamarinLocations = new HashMap<>();
    Map<HousingType, List<UUID>> titiLocations = new HashMap<>();
    Map<HousingType, List<UUID>> uakarisLocations = new HashMap<>();
    Map<HousingType, List<UUID>> woollySpiderLocations = new HashMap<>();
    Map<HousingType, List<UUID>> woollyLocations = new HashMap<>();

    List<UUID> marmosetIsolationList = new ArrayList<>();
    List<UUID> marmosetEnclosureList = new ArrayList<>();
    List<UUID> capuchinIsolationList = new ArrayList<>();
    List<UUID> capuchinEnclosureList = new ArrayList<>();
    List<UUID> howlerIsolationList = new ArrayList<>();
    List<UUID> howlerEnclosureList = new ArrayList<>();
    List<UUID> nightIsolationList = new ArrayList<>();
    List<UUID> nightEnclosureList = new ArrayList<>();
    List<UUID> sakiIsolationList = new ArrayList<>();
    List<UUID> sakiEnclosureList = new ArrayList<>();
    List<UUID> spiderIsolationList = new ArrayList<>();
    List<UUID> spiderEnclosureList = new ArrayList<>();
    List<UUID> squirrelIsolationList = new ArrayList<>();
    List<UUID> squirrelEnclosureList = new ArrayList<>();
    List<UUID> tamarinIsolationList = new ArrayList<>();
    List<UUID> tamarinEnclosureList = new ArrayList<>();
    List<UUID> titiIsolationList = new ArrayList<>();
    List<UUID> titiEnclosureList = new ArrayList<>();
    List<UUID> uakarisIsolationList = new ArrayList<>();
    List<UUID> uakarisEnclosureList = new ArrayList<>();
    List<UUID> woollyIsolationList = new ArrayList<>();
    List<UUID> woollyEnclosureList = new ArrayList<>();
    List<UUID> woollySpiderIsolationList = new ArrayList<>();
    List<UUID> woollySpiderEnclosureList = new ArrayList<>();

    for (Housing location : this.locations) {
      if (!location.getResidents().isEmpty()
              && location.getResidents().get(0) != null) {
        switch (location.getSpecies()) {
          case MARMOSET:
            addSpeciesLocationsToMap(location, marmosetIsolationList, marmosetEnclosureList,
                    marmosetLocations);
            break;
          case CAPUCHIN:
            addSpeciesLocationsToMap(location, capuchinIsolationList, capuchinEnclosureList,
                    capuchinLocations);
            break;
          case SAKI:
            addSpeciesLocationsToMap(location, sakiIsolationList, sakiEnclosureList,
                    sakiLocations);
            break;
          case HOWLER:
            addSpeciesLocationsToMap(location, howlerIsolationList, howlerEnclosureList,
                    howlerLocation);
            break;
          case NIGHT:
            addSpeciesLocationsToMap(location, nightIsolationList, nightEnclosureList,
                    nightLocations);
            break;
          case SPIDER:
            addSpeciesLocationsToMap(location, spiderIsolationList, spiderEnclosureList,
                    spiderLocations);
            break;
          case SQUIRREL:
            addSpeciesLocationsToMap(location, squirrelIsolationList, squirrelEnclosureList,
                    squirrelLocations);
            break;
          case TAMARIN:
            addSpeciesLocationsToMap(location, tamarinIsolationList, tamarinEnclosureList,
                    tamarinLocations);
            break;
          case TITI:
            addSpeciesLocationsToMap(location, titiIsolationList, titiEnclosureList,
                    titiLocations);
            break;
          case UAKARIS:
            addSpeciesLocationsToMap(location, uakarisIsolationList, uakarisEnclosureList,
                    uakarisLocations);
            break;
          case WOOLLY:
            addSpeciesLocationsToMap(location, woollyIsolationList, woollyEnclosureList,
                    woollyLocations);
            break;
          case WOOLLY_SPIDER:
            addSpeciesLocationsToMap(location, woollySpiderIsolationList, woollySpiderEnclosureList,
                    woollySpiderLocations);
            break;

          default: //No default case.
        }
      }
      speciesWithLocations.put(Species.MARMOSET, marmosetLocations);
      speciesWithLocations.put(Species.SAKI, sakiLocations);
      speciesWithLocations.put(Species.SPIDER, spiderLocations);
      speciesWithLocations.put(Species.CAPUCHIN, capuchinLocations);
      speciesWithLocations.put(Species.NIGHT, nightLocations);
      speciesWithLocations.put(Species.HOWLER, howlerLocation);
      speciesWithLocations.put(Species.TAMARIN, tamarinLocations);
      speciesWithLocations.put(Species.TITI, titiLocations);
      speciesWithLocations.put(Species.UAKARIS, uakarisLocations);
      speciesWithLocations.put(Species.WOOLLY, woollyLocations);
      speciesWithLocations.put(Species.WOOLLY_SPIDER, woollySpiderLocations);
      speciesWithLocations.put(Species.SQUIRREL, squirrelLocations);
    }
    return speciesWithLocations;
  }

  @Override
  public Map<HousingType, List<UUID>> getLocationsForASpecies(Species species)
          throws IllegalArgumentException {
    this.nullCheck(species, "Species");
    return this.getSpeciesWithLocations().get(species);
  }

  @Override
  public Map<FavoriteFood, Integer> getFavFoodShoppingList() {
    Map<FavoriteFood, Integer> favFoodShoppingList = new HashMap<>();
    int nuts = DEFAULT_ZERO;
    int fruits = DEFAULT_ZERO;
    int seeds = DEFAULT_ZERO;
    int eggs = DEFAULT_ZERO;
    int leaves = DEFAULT_ZERO;
    int treeSap = DEFAULT_ZERO;
    int insects = DEFAULT_ZERO;
    for (Primate monkey : this.getMonkeys()) {
      switch (monkey.getFavoriteFood()) {
        case NUTS:
          favFoodShoppingList.put(FavoriteFood.NUTS, nuts += monkey.getSize().getFoodRequired());
          break;
        case FRUITS:
          favFoodShoppingList.put(FavoriteFood.FRUITS, fruits += monkey.getSize()
                  .getFoodRequired());
          break;
        case EGGS:
          favFoodShoppingList.put(FavoriteFood.EGGS, eggs += monkey.getSize().getFoodRequired());
          break;
        case SEEDS:
          favFoodShoppingList.put(FavoriteFood.SEEDS, seeds += monkey.getSize().getFoodRequired());
          break;
        case LEAVES:
          favFoodShoppingList.put(FavoriteFood.LEAVES, leaves += monkey.getSize()
                  .getFoodRequired());
          break;
        case TREESAP:
          favFoodShoppingList.put(FavoriteFood.TREESAP, treeSap += monkey.getSize()
                  .getFoodRequired());
          break;
        case INSECTS:
          favFoodShoppingList.put(FavoriteFood.INSECTS, insects += monkey.getSize()
                  .getFoodRequired());
          break;

        default: //No default case.
      }
    }
    return favFoodShoppingList;
  }


  private void addSpeciesLocationsToMap(Housing location, List<UUID> speciesIsolationList,
                                        List<UUID> speciesEnclosureList, Map<HousingType,
          List<UUID>> speciesLocationMap) {
    if (location.getHousingType() == HousingType.ISOLATION) {
      speciesIsolationList.add(location.getId());
      speciesLocationMap.put(location.getHousingType(), speciesIsolationList);
    } else if (location.getHousingType() == HousingType.ENCLOSURE) {
      speciesEnclosureList.add(location.getId());
      speciesLocationMap.put(location.getHousingType(), speciesEnclosureList);
    }
  }

  private Isolation isIsolationCageAvailable(Primate monkey) {
    List<Housing> isolations = locations.stream()
            .filter(location -> location.getHousingType() == HousingType.ISOLATION)
            .collect(Collectors.toList());
    for (Housing isolation : isolations) {
      if (isolation.isLocationAvailable(monkey)) {
        return (Isolation) isolation;
      }
    }
    return null;
  }

  private Enclosure isEnclosureAvailable(Primate monkey) {
    List<Housing> enclosures = locations.stream()
            .filter(location -> location.getHousingType() == HousingType.ENCLOSURE)
            .collect(Collectors.toList());
    for (Housing enclosure : enclosures) {
      if (enclosure.isLocationAvailable(monkey)) {
        return (Enclosure) enclosure;
      }
    }
    return null;
  }

  private List<Housing> createLocations(int numOfEnclosures, int[] sizeOfEnclosures,
                                        int numOfIsolationCages, int existingNumOfEnclosures,
                                        int existingNumOfIsolationCages) {
    List<Housing> locations = new ArrayList<>();
    for (int i = existingNumOfEnclosures; i < existingNumOfEnclosures + numOfEnclosures; i++) {
      Housing enclosure = new Enclosure(sizeOfEnclosures[i - existingNumOfEnclosures]);
      locations.add(enclosure);
    }
    for (int i = existingNumOfIsolationCages; i < existingNumOfIsolationCages + numOfIsolationCages;
         i++) {
      Housing isolation = new Isolation();
      locations.add(isolation);
    }
    return locations;
  }

  private void removeMonkeyFromCurrentLocation(Primate monkey) {
    List<Housing> locations = this.locations.stream()
            .filter(location -> !location.getResidents().isEmpty() && location.getResidents().get(0)
                    != null && location.getResidents().stream()
                    .anyMatch(mon -> mon.equals(monkey)))
            .collect(Collectors.toList());

    if (locations.size() > 1) {
      throw new IllegalStateException("Monkey " + monkey.getName() + "(" + monkey.getId()
              + ") exists at more than one place");
    }
    Housing currentLocation = locations.stream().findFirst().orElse(null);

    if (currentLocation != null) {
      if (currentLocation.getHousingType() == HousingType.ISOLATION) {
        ((Isolation) currentLocation).removeMonkey(monkey);
      } else if (currentLocation.getHousingType() == HousingType.ENCLOSURE) {
        ((Enclosure) currentLocation).removeMonkey(monkey);
      }
    }
  }

  private void checkIfUnhealthyMonkeyBeingMovedToEnclosure(Housing housing, Primate monkey)
          throws IllegalStateException {
    if (housing.getHousingType() == HousingType.ENCLOSURE
            && monkey.getHealthStatus() == HealthStatus.UNHEALTHY) {
      throw new IllegalStateException("Only healthy monkeys can be added to Enclosures. Monkey "
              + monkey.getName() + "(" + monkey.getId() + ") is UNHEALTHY.");
    }
  }

  private void checkIfMonkeyExitsInSanctuary(Primate monkey) throws IllegalArgumentException {
    if (!this.getMonkeys().contains(monkey)) {
      throw new IllegalArgumentException("Monkey " + monkey.getName() + "(" + monkey.getId()
              + ")does not exist in sanctuary");
    }
  }

  private void nullCheck(Object object, String value) throws IllegalArgumentException {
    if (object == null) {
      throw new IllegalArgumentException(value + " cannot be null.");
    }
  }
}
