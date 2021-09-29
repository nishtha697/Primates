package sanctuary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.HousingType;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

public class JungleFriendsSanctuary implements Sanctuary {

  private final int[] sizeOfEnclosures;
  private final List<Housing> locations;
  private final List<Primate> monkeys;
  private int numOfIsolationCages;
  private int numOfEnclosures;
  private final List<Primate> alumniMonkeys;

  public JungleFriendsSanctuary(int numOfIsolationCages, int numOfEnclosures, int[] sizeOfEnclosures) {
    this.numOfIsolationCages = numOfIsolationCages;
    this.numOfEnclosures = numOfEnclosures;
    this.sizeOfEnclosures = sizeOfEnclosures;
    this.locations = createLocations(this.numOfEnclosures, this.sizeOfEnclosures, this.numOfIsolationCages, 0, 0);
    this.monkeys = new ArrayList<>();
    this.alumniMonkeys = new ArrayList<>();
  }

  @Override
  public void addMonkey(String name, MonkeySize size, float weight, int age, Species species, Sex sex,
                        FavoriteFood favoriteFood, HealthStatus healthStatus,
                        String monkeyLocation) throws IllegalStateException {
    Primate newMonkey;
    if (isIsolationCageAvailable(null) != null) {
      newMonkey = new Monkey(name, size, weight, age, species, sex, favoriteFood, healthStatus);
      this.monkeys.add(newMonkey);
      if (monkeyLocation != null) {
        moveMonkey(monkeyLocation, newMonkey);
      } else {
        moveMonkeyToIsolation(newMonkey);
      }
    } else throw new IllegalStateException("No more isolation cages available. New monkey cannot " +
            "be added to the sanctuary");
  }

  @Override
  public List<Primate> getMonkeys() {
    return this.monkeys;
  }

  @Override
  public void moveMonkey(String housingId, Primate monkey) throws IllegalStateException,
          IllegalArgumentException {
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
                monkey.getName() + "(" +  monkey.getId() + ") is UNHEALTHY.");
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
  public void moveMonkeyToEnclosure(Primate monkey) throws IllegalStateException {
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
  }

  @Override
  public void moveMonkeyToIsolation(Primate monkey) throws UnsupportedOperationException {
    Isolation isolation = isIsolationCageAvailable(monkey);
    if (isolation != null) {
      this.removeMonkeyFromCurrentLocation(monkey);
      isolation.addMonkey(monkey);
    } else {
      throw new IllegalStateException("No more Isolation cages left. Cannot house anymore " +
              "monkeys.");
    }
  }

  @Override
  public List<Housing> getHousings() {
    return this.locations;
  }

  @Override
  public void removeMonkey(Primate monkey) {
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    if (!this.getMonkeys().stream()
            .filter(mon -> mon.getId().equals(monkey.getId()))
            .findAny().isPresent()) {
      throw new IllegalArgumentException("Monkey " + monkey.getName() + "(" + monkey.getId() +
              ") does not exist in sanctuary.");
    }
    this.removeMonkeyFromCurrentLocation(monkey);
    this.getMonkeys().removeIf(mon -> mon.getId().equals(monkey.getId()));
    this.alumniMonkeys.add(monkey);
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
  public Map<Species, List<String>> getSpeciesWithLocations() {
    Map<Species, List<String>> speciesWithLocations = new TreeMap<>();
    List<String> marmosetLocations = new ArrayList<>();
    List<String> capuchinLocations = new ArrayList<>();
    List<String> howlerLocation = new ArrayList<>();
    List<String> nightLocations = new ArrayList<>();
    List<String> sakiLocations = new ArrayList<>();
    List<String> spiderLocations = new ArrayList<>();
    List<String> squirrelLocations = new ArrayList<>();
    List<String> tamarinLocations = new ArrayList<>();
    List<String> titiLocations = new ArrayList<>();
    List<String> uakarisLocations = new ArrayList<>();
    List<String> woollySpiderLocations = new ArrayList<>();
    List<String> woollyLocations = new ArrayList<>();


    for (Housing location : this.locations) {
      if(location.getResidents() != null && !location.getResidents().isEmpty()
              && location.getResidents().get(0) != null) {
        switch (location.getSpecies()) {
          case MARMOSET:
            marmosetLocations.add(location.getId());
            break;
          case CAPUCHIN:
            capuchinLocations.add(location.getId());
            break;
          case SAKI:
            sakiLocations.add(location.getId());
            break;
          case HOWLER:
            howlerLocation.add(location.getId());
            break;
          case NIGHT:
            nightLocations.add(location.getId());
            break;
          case SPIDER:
            spiderLocations.add(location.getId());
            break;
          case SQUIRREL:
            squirrelLocations.add(location.getId());
            break;
          case TAMARIN:
            tamarinLocations.add(location.getId());
            break;
          case TITI:
            titiLocations.add(location.getId());
            break;
          case UAKARIS:
            uakarisLocations.add(location.getId());
            break;
          case WOOLLY:
            woollyLocations.add(location.getId());
            break;
          case WOOLLY_SPIDER:
            woollySpiderLocations.add(location.getId());
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
      speciesWithLocations.put(Species.WOOLLY_SPIDER, woollyLocations);
      speciesWithLocations.put(Species.SQUIRREL, squirrelLocations);

    }
    return speciesWithLocations;
  }

  @Override
  public List<String> getLocationsForSpecies(Species species) {
    return this.getSpeciesWithLocations().get(species);
  }

  @Override
  public Map<String, Map<HousingType, String>> getAllMonkeysWithLocations() {
    Map<String, Map<HousingType, String>> monkeysWithLocations = new TreeMap<>();
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
  public Map<FavoriteFood, Integer> getFavFoodShoppingList() {
    Map<FavoriteFood, Integer> favFoodShoppingList = new HashMap<>();
    int nuts = 0;
    int fruits = 0;
    int seeds = 0;
    int eggs = 0;
    int leaves = 0;
    int treeSap = 0;
    int insects = 0;
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

  @Override
  public void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures,
                          int[] sizeOfEnclosures) {
    this.locations.addAll(createLocations(numOfNewEnclosures, this.sizeOfEnclosures,
            numOfNewIsolationCages, this.numOfEnclosures, this.numOfIsolationCages));
    this.numOfIsolationCages += numOfNewIsolationCages;
    this.numOfEnclosures += numOfNewEnclosures;
  }

  @Override
  public Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(String enclosureId) {
    if(enclosureId == null){
      throw new IllegalArgumentException("Enclosure id cannot be null.");
    }
    boolean validEnclosureId = false;
    Map<String, Map<Sex, FavoriteFood>> enclosureSign = new HashMap<>();
    for(Housing location : this.getHousings()) {
      if (location.getId().equals(enclosureId) && location.getHousingType() == HousingType.ENCLOSURE) {
        enclosureSign = ((Enclosure) location).getEnclosureSign();
        validEnclosureId = true;
      }
    }
    if(!validEnclosureId){

        throw new IllegalArgumentException(enclosureId + ": Enclosure id does not exist.");

    }
    return enclosureSign;
  }

  @Override
  public List<Primate> getAlumniMonkeys() {
    return this.alumniMonkeys;
  }

  @Override
  public void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    boolean shouldMonkeyMoveToIsolation = false;
    Housing currentHousing = null;
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    try{
      ((Monkey) monkey).updateHealthStatus(updatedHealthStatus);
    } catch(IllegalArgumentException e){
      throw e;
    }
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
        throw new IllegalStateException("Unhealthy monkey " + monkey.getName() + "(" +
                monkey.getId() + ") cannot stay in Enclosure. Monkey is removed from enclosure " +
                currentHousing.getId() + " but no more Isolation cages are left. Please move " +
                "monkey to a suitable place.");
      }
    }
  }

  @Override
  public void updateMonkeySize(MonkeySize updatedSize, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    if (monkey == null) {
      throw new IllegalArgumentException("Monkey cannot be null.");
    }
    try {
      ((Monkey) monkey).updateSize(updatedSize);
    } catch(IllegalArgumentException e){
      throw e;
    }
    this.getHousings().forEach(housing -> {
      if (housing.getHousingType() == HousingType.ENCLOSURE) {
        if (((Enclosure) housing).getAvailableCapacity() < (updatedSize.getSpace() - monkey.getSize().
                getSpace())) {
          try {
            this.moveMonkeyToEnclosure(monkey);
          } catch (IllegalStateException e) {
            if (e.getMessage().equals("No space left in Enclosures. Cannot house anymore monkeys."))
            {
              this.removeMonkeyFromCurrentLocation(monkey);
              throw new IllegalStateException("Monkey size increased and no more space left for " +
                      "monkey in any Enclosures. Please find a suitable place for monkey " +
                      monkey.getName() + "(" + monkey.getId() + ")using exchange agreement");
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
    try{
      ((Monkey) monkey).updateWeight(updatedWeight);
    } catch(IllegalArgumentException e){
    throw e;
  }
  }

  @Override
  public void updateMonkeyAge(int updatedAge, Primate monkey)
          throws IllegalStateException, IllegalArgumentException {
    if (monkey == null) {
      throw new IllegalArgumentException(" Monkey cannot be null.");
    }
    try{
      ((Monkey) monkey).updateAge(updatedAge);
    } catch(IllegalArgumentException e){
      throw e;
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
