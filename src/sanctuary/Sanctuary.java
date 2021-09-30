package sanctuary;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.HousingType;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

public interface Sanctuary {

  public int getTotalNumOfIsolationCages();

  public int getTotalNumOfEnclosures();

  public List<Housing> getHousings();

  public Set<UUID> getHousingIds();

  public void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures,
                          int[] sizeOfEnclosures);

  public Map<Primate, Housing> addMonkey(String name, MonkeySize size, double weight, int age, Species species,
                                         Sex sex, FavoriteFood favoriteFood, HealthStatus healthStatus,
                                         UUID monkeyLocation);

  public void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey);

  public void updateMonkeySize(MonkeySize updatedSize, Primate monkey);

  public void updateMonkeyWeight(double updatedWeight, Primate monkey);

  public void updateMonkeyAge(int updatedAge, Primate monkey);

  public void updateFavoriteFood(FavoriteFood favoriteFood, Primate monkey);

  public void removeMonkey(Primate monkey);

  public List<Primate> getMonkeys();

  public List<UUID> getMonkeyIds();

  public List<Primate> getAlumniMonkeys();

  public void moveMonkey(UUID housingId, Primate monkey);

  public Housing moveMonkeyToIsolation(Primate monkey) throws UnsupportedOperationException;

  public Housing moveMonkeyToEnclosure(Primate monkey) throws UnsupportedOperationException;

  public Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(UUID enclosureId);

  public Map<String, Map<HousingType, UUID>> getAllMonkeysWithLocations();

  public Map<Species, Map<HousingType, UUID>> getSpeciesWithLocations();

  public Map<HousingType, UUID> getLocationsForASpecies(Species species);

  public Map<FavoriteFood, Integer> getFavFoodShoppingList();
}
