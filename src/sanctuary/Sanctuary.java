package sanctuary;

import java.util.List;
import java.util.Map;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.HousingType;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

public interface Sanctuary {

  public void addMonkey(String name, MonkeySize size, float weight, int age, Species species, Sex sex, FavoriteFood favoriteFood,
                        HealthStatus healthStatus, String monkeyLocation);

  public List<Primate> getMonkeys();

  public void moveMonkey(String housingId, Primate monkey);

  public List<Housing> getHousings();

  public void removeMonkey(Primate monkey);

  public int getTotalNumOfIsolationCages();

  public int getTotalNumOfEnclosures();

  public void moveMonkeyToIsolation(Primate monkey) throws UnsupportedOperationException;

  public void moveMonkeyToEnclosure(Primate monkey) throws UnsupportedOperationException;

  public Map<Species, List<String>> getSpeciesWithLocations();

  public List<String> getLocationsForSpecies(Species species);

  public Map<String, Map<HousingType, String>> getAllMonkeysWithLocations();

  public Map<FavoriteFood, Integer> getFavFoodShoppingList();

  public void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures, int[] sizeOfEnclosures);

  //implementation in enclosure
  Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(String enclosureId);

  public void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey);

  public void updateMonkeySize(MonkeySize updatedSize, Primate monkey);

  public void updateMonkeyWeight(double updatedWeight, Primate monkey);

  public void updateMonkeyAge(int updatedAge, Primate monkey);

  public List<Primate> getAlumniMonkeys();
}
