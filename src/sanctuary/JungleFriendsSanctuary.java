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
import java.util.logging.Logger;
import java.util.stream.Collectors;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.HousingType;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

public class JungleFriendsSanctuary implements Sanctuary {
  private static final int DEFAULT_ZERO = 0;
  private final int[] sizeOfEnclosures;
  private final List<Housing> locations;
  private final List<Primate> monkeys;
  private final List<Primate> alumniMonkeys;
  private int numOfIsolationCages;
  private int numOfEnclosures;

  Logger logger = Logger.getLogger(JungleFriendsSanctuary.class.getName());

  public JungleFriendsSanctuary(int numOfIsolationCages, int numOfEnclosures,
                                int[] sizeOfEnclosures) {
    if (numOfEnclosures <= 0 || numOfIsolationCages <= 0){
      throw new IllegalArgumentException("Number of isolation cages or enclosures cannot be less "
              + "than or equal to 0");
    }
    if(sizeOfEnclosures.length != numOfEnclosures || Arrays.stream(sizeOfEnclosures).anyMatch(
            value -> value == 0)){
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
    return this.locations;
  }

  @Override
  public Set<UUID> getHousingIds() {
    Set<UUID> housingIds = new HashSet<>();
    this.locations.forEach(housing -> housingIds.add(housing.getId()));
    return housingIds;
  }

  @Override
  public void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures,
                          int[] sizeOfEnclosures) {
    this.locations.addAll(createLocations(numOfNewEnclosures, this.sizeOfEnclosures,
            numOfNewIsolationCages, this.numOfEnclosures, this.numOfIsolationCages));
    this.numOfIsolationCages += numOfNewIsolationCages;
    this.numOfEnclosures += numOfNewEnclosures;
  }

  @Override
  public Map<Primate, Housing> addMonkey(String name, MonkeySize size, double weight, int age, Species species,
                                         Sex sex, FavoriteFood favoriteFood, HealthStatus healthStatus,
                                         UUID monkeyLocation) throws IllegalStateException {
    Primate newMonkey;
    Housing isolation;
    if (isIsolationCageAvailable(null) != null) {
      newMonkey = new Monkey(name, size, weight, age, species, sex, favoriteFood, healthStatus);
      this.monkeys.add(newMonkey);
      isolation = this.getHousings().stream().filter(location ->
              location.getId() == monkeyLocation).findFirst().orElse(null);
      if (monkeyLocation != null && isolation != null && isolation.getHousingType() == HousingType.ISOLATION){

          ((Isolation)isolation).addMonkey(newMonkey);
      } else {
        isolation = moveMonkeyToIsolation(newMonkey);
        if(monkeyLocation != null) {
          logger.warning("Either the housing id " + monkeyLocation
                  + " is not a valid isolation cage id or is already occupied. The new monkey " + newMonkey.getName()
                  + ":" + newMonkey.getId()
                  + " is added to another available Isolation cage : " + isolation.getId());
        }
      }
    } else throw new IllegalStateException("No more isolation cages available. New monkey cannot "
            + "be added to the sanctuary. Please use exchange agreements to find a suitable home "
            + "for this monkey.");
    return Collections.singletonMap(newMonkey, isolation);
  }

  @Override
  public void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    boolean shouldMonkeyMoveToIsolation = false;
    Housing currentHousing = null;
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    ((Monkey) monkey).updateHealthStatus(updatedHealthStatus);
    if (monkey.getHealthStatus() == HealthStatus.UNHEALTHY) {
      for (Housing housing : this.getHousings()) {

        if (housing.getHousingType() == HousingType.ENCLOSURE && housing.getResidents() != null
                && !housing.getResidents().isEmpty()) {
          for (Primate resident : housing.getResidents()) {
            if (resident.getId().equals(monkey.getId())) {
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
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    ((Monkey) monkey).updateSize(updatedSize);
    this.getHousings().forEach(housing -> {
      if (housing.getHousingType() == HousingType.ENCLOSURE) {
        if (((Enclosure) housing).getAvailableCapacity() < (updatedSize.getSpace()
                - monkey.getSize().getSpace())) {
          try {
            this.moveMonkeyToEnclosure(monkey);
          } catch (IllegalStateException e) {
            if (e.getMessage().equals("No space left in Enclosures. Cannot house anymore "
                    + "monkeys.")) {
              this.removeMonkeyFromCurrentLocation(monkey);
              throw new IllegalStateException("Monkey size increased and no more space left for "
                      + "monkey in any Enclosures. Please find a suitable place for monkey "
                      + monkey.getName() + "(" + monkey.getId() + ")using exchange agreement");
            } else throw e;
          }
        }
      }
    });
  }

  @Override
  public void updateMonkeyWeight(double updatedWeight, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    if (monkey == null) {
      throw new IllegalArgumentException(" Monkey cannot be null.");
    }
    ((Monkey) monkey).updateWeight(updatedWeight);
  }

  @Override
  public void updateMonkeyAge(int updatedAge, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    if (monkey == null) {
      throw new IllegalArgumentException(" Monkey cannot be null.");
    }
    ((Monkey) monkey).updateAge(updatedAge);
  }

  @Override
  public void updateFavoriteFood(FavoriteFood favoriteFood, Primate monkey) {
    if (monkey == null) {
      throw new IllegalArgumentException(" Monkey cannot be null.");
    }
    ((Monkey) monkey).updateFavoriteFood(favoriteFood);
  }

  @Override
  public void removeMonkey(Primate monkey) {
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    if (this.getMonkeys().stream()
            .noneMatch(mon -> mon.getId().equals(monkey.getId()))) {
      throw new IllegalArgumentException("Monkey " + monkey.getName() + "(" + monkey.getId() +
              ") does not exist in sanctuary.");
    }
    this.removeMonkeyFromCurrentLocation(monkey);
    this.getMonkeys().removeIf(mon -> mon.getId().equals(monkey.getId()));
    this.alumniMonkeys.add(monkey);
  }

  @Override
  public List<Primate> getMonkeys() {
    return this.monkeys;
  }

  @Override
  public List<UUID> getMonkeyIds() {
    List<UUID> monkeyIds = new ArrayList<>();
    this.monkeys.forEach(monkey -> monkeyIds.add(monkey.getId()));
    return monkeyIds;
  }

  @Override
  public List<Primate> getAlumniMonkeys() {
    return this.alumniMonkeys;
  }

  @Override
  public void moveMonkey(UUID housingId, Primate monkey) throws IllegalStateException,
          IllegalArgumentException {
    if(housingId == null) {
      throw new IllegalArgumentException("Housing id cannot be null.");
    }
    if(monkey == null){
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    Housing housing = this.locations.stream().filter(house -> house.getId().equals(housingId))
            .findFirst().orElse(null);
    if (housing == null) {
      throw new IllegalArgumentException(housingId + " : Location does not exist. Move monkey " +
              monkey.getName() + "(" + monkey.getId() + ") to valid location.");
    }
    if (housing.isLocationAvailable(monkey)) {
      if (housing.getHousingType() == HousingType.ENCLOSURE
              && monkey.getHealthStatus() == HealthStatus.UNHEALTHY) {
        throw new IllegalStateException("Only healthy monkeys can be added to Enclosures. Monkey " +
                monkey.getName() + "(" + monkey.getId() + ") is UNHEALTHY.");
      }
      if (!this.removeMonkeyFromCurrentLocation(monkey)
              && housing.getHousingType() == HousingType.ENCLOSURE) {
        throw new IllegalStateException("New monkey cannot be directly added to " +
                "Enclosure. Move monkey " + monkey.getName() + "(" + monkey.getId() +
                ") to Isolation cage first.");
      }
      if (housing.getHousingType() == HousingType.ENCLOSURE) {
        ((Enclosure) housing).addMonkey(monkey);
      } else if (housing.getHousingType() == HousingType.ISOLATION) {
        ((Isolation) housing).addMonkey(monkey);
      }
    } else
      throw new IllegalStateException("The location " + housingId + " is not available for " +
              monkey.getName() + "(" + monkey.getId() + "). Try another location.");
  }

  @Override
  public Housing moveMonkeyToIsolation(Primate monkey) throws IllegalArgumentException,
          IllegalStateException {
    if(monkey == null){
      throw new IllegalArgumentException("Monkey cannot be null");
    }
    Isolation isolation = isIsolationCageAvailable(monkey);
    if (isolation != null) {
      this.removeMonkeyFromCurrentLocation(monkey);
      isolation.addMonkey(monkey);
    } else {
      throw new IllegalStateException("No more Isolation cages left. Cannot house anymore " +
              "monkeys.");
    }
    return isolation;
  }

  @Override
  public Housing moveMonkeyToEnclosure(Primate monkey) throws IllegalArgumentException,
          IllegalStateException {
    if(monkey == null){
      throw new IllegalArgumentException("Monkey cannot be null");
    }
    Enclosure enclosure = isEnclosureAvailable(monkey);
    if (enclosure != null) {
      if (monkey.getHealthStatus() == HealthStatus.UNHEALTHY) {
        throw new IllegalStateException("Only healthy monkeys can be added to Enclosures. Monkey " +
                monkey.getName() + "(" + monkey.getId() + ") is UNHEALTHY.");
      }
      if (!this.removeMonkeyFromCurrentLocation(monkey)) {
        throw new IllegalStateException("New monkey cannot be directly added to " +
                "Enclosure. Move monkey " + monkey.getName() + "(" + monkey.getId() +
                ") to Isolation cage first.");
      }
      enclosure.addMonkey(monkey);
    } else {
      throw new IllegalStateException("No space left in Enclosures. Cannot house anymore monkeys.");
    }
    return enclosure;
  }

  @Override
  public Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(UUID enclosureId) {
    if (enclosureId == null) {
      throw new IllegalArgumentException("Enclosure id cannot be null.");
    }
    boolean validEnclosureId = false;
    Map<String, Map<Sex, FavoriteFood>> enclosureSign = new HashMap<>();
    for (Housing location : this.getHousings()) {
      if (location.getId().equals(enclosureId) && location.getHousingType() ==
              HousingType.ENCLOSURE) {
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
      if (location.getResidents() != null && !location.getResidents().isEmpty()
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
  public Map<Species, Map<HousingType, UUID>> getSpeciesWithLocations() {
    Map<Species, Map<HousingType, UUID>> speciesWithLocations = new TreeMap<>(Comparator.comparing(Enum::toString));
    Map<HousingType, UUID> marmosetLocations = new HashMap<>();
    Map<HousingType, UUID> capuchinLocations = new HashMap<>();
    Map<HousingType, UUID> howlerLocation = new HashMap<>();
    Map<HousingType, UUID> nightLocations = new HashMap<>();
    Map<HousingType, UUID> sakiLocations = new HashMap<>();
    Map<HousingType, UUID> spiderLocations = new HashMap<>();
    Map<HousingType, UUID> squirrelLocations = new HashMap<>();
    Map<HousingType, UUID> tamarinLocations = new HashMap<>();
    Map<HousingType, UUID> titiLocations = new HashMap<>();
    Map<HousingType, UUID> uakarisLocations = new HashMap<>();
    Map<HousingType, UUID> woollySpiderLocations = new HashMap<>();
    Map<HousingType, UUID> woollyLocations = new HashMap<>();

    for (Housing location : this.locations) {
      if (location.getResidents() != null && !location.getResidents().isEmpty()
              && location.getResidents().get(0) != null) {
        switch (location.getSpecies()) {
          case MARMOSET:
            marmosetLocations.put(location.getHousingType(), location.getId());
            break;
          case CAPUCHIN:
            capuchinLocations.put(location.getHousingType(), location.getId());
            break;
          case SAKI:
            sakiLocations.put(location.getHousingType(), location.getId());
            break;
          case HOWLER:
            howlerLocation.put(location.getHousingType(), location.getId());
            break;
          case NIGHT:
            nightLocations.put(location.getHousingType(), location.getId());
            break;
          case SPIDER:
            spiderLocations.put(location.getHousingType(), location.getId());
            break;
          case SQUIRREL:
            squirrelLocations.put(location.getHousingType(), location.getId());
            break;
          case TAMARIN:
            tamarinLocations.put(location.getHousingType(), location.getId());
            break;
          case TITI:
            titiLocations.put(location.getHousingType(), location.getId());
            break;
          case UAKARIS:
            uakarisLocations.put(location.getHousingType(), location.getId());
            break;
          case WOOLLY:
            woollyLocations.put(location.getHousingType(), location.getId());
            break;
          case WOOLLY_SPIDER:
            woollySpiderLocations.put(location.getHousingType(), location.getId());
            break;
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
  public Map<HousingType, UUID> getLocationsForASpecies(Species species)
          throws IllegalArgumentException{
    if(species == null){
      throw new IllegalArgumentException("Species cannot be null.");
    }
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
      }
    }
    return favFoodShoppingList;
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
    for (int i = existingNumOfEnclosures; i < numOfEnclosures; i++) {
      Housing enclosure = new Enclosure(sizeOfEnclosures[i]);
      locations.add(enclosure);
    }
    for (int i = existingNumOfIsolationCages; i < numOfIsolationCages; i++) {
      Housing isolation = new Isolation();
      locations.add(isolation);
    }
    return locations;
  }

  private boolean removeMonkeyFromCurrentLocation(Primate monkey) {
    List<Housing> locations = this.locations.stream()
            .filter(location -> location.getResidents() != null
                    && !location.getResidents().isEmpty() && location.getResidents().get(0) != null
                    && location.getResidents().stream()
                    .anyMatch(mon -> mon.getId().equals(monkey.getId())))
            .collect(Collectors.toList());

    if (locations.size() > 1) {
      throw new IllegalStateException("Monkey " + monkey.getName() + "(" + monkey.getId() +
              ") exists at more than one place");
    }
    Housing currentLocation = locations.stream().findFirst().orElse(null);

    if (currentLocation != null) {
      if (currentLocation.getHousingType() == HousingType.ISOLATION) {
        ((Isolation) currentLocation).removeMonkey(monkey);
      } else if (currentLocation.getHousingType() == HousingType.ENCLOSURE) {
        ((Enclosure) currentLocation).removeMonkey(monkey);
      }
      return true;
    }
    return false;
  }
}
