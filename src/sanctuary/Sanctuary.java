package sanctuary;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import monkeyAttributes.FavoriteFood;
import monkeyAttributes.HealthStatus;
import housingAttributes.HousingType;
import monkeyAttributes.MonkeySize;
import monkeyAttributes.Sex;
import monkeyAttributes.Species;

/**
 * The sanctuary.Sanctuary represents data for various sanctuaries that provide care for primates.
 */
public interface Sanctuary {

  /**
   * The total number of isolation cages in the sanctuary. It will never be zero or negative.
   *
   * @return the total number of isolation cages.
   */
  int getTotalNumOfIsolationCages();

  /**
   * The total number of enclosures in the sanctuary. It will never be zero or negative.
   *
   * @return the total number of enclosures.
   */
  int getTotalNumOfEnclosures();

  /**
   * The {@link List} of all {@link Housing}s that are a part of the sanctuary. This will ever be
   * {@code null} or empty.
   *
   * @return the list of all housings.
   */
  List<Housing> getHousings();

  /**
   * The {@link Set} of all {@link UUID}s for {@link Housing}s that are a part of the sanctuary.
   * This will ever be {@code null} or empty.
   *
   * @return the set of all housing ids.
   */
  Set<UUID> getHousingIds();

  /**
   * Adds new isolation cages and enclosures to the sanctuary.
   *
   * @param numOfNewIsolationCages number of new isolation cages to be added.
   * @param numOfNewEnclosures     number of new enclosures to be added.
   * @param sizeOfEnclosures       the array where each element represents the size of each new
   *                               enclosure.
   * @throws IllegalArgumentException <ul><li>if {@code numOfNewIsolationCages} is negative.</li>
   *                                  <li>if {@code numOfNewEnclosures} is negative.</li>
   *                                  <li>if the length of {@code sizeOfEnclosures} is not equal to size
   *                                  of {@code numOfNewEnclosures}.</li>
   *                                  <li>if any element of {@code sizeOfEnclosures} is zero or
   *                                  negative.</li></ul>
   */
  void addCapacity(int numOfNewIsolationCages, int numOfNewEnclosures,
                   int[] sizeOfEnclosures);

  /**
   * Adds a {@link Primate} monkey to the sanctuary.
   *
   * @param name         the name of the monkey. This cannot be {@code null} or empty.
   * @param size         the {@link MonkeySize} of the monkey. This cannot be {@code null}.
   * @param weight       the weight(in pounds) of the monkey. This cannot be zero or negative.
   * @param age          the age(in years) of the monkey. This cannot be negative.
   * @param species      the {@link Species} of the monkey. This cannot be {@code null}.
   * @param sex          the {@link Sex} of the monkey. This cannot be {@code null}.
   * @param favoriteFood the {@link FavoriteFood} of the monkey. This cannot be {@code null}.
   * @param healthStatus the {@link HealthStatus} of the monkey. This cannot be {@code null}.
   * @param isolationId  the {@link UUID} for isolation cage the new monkey need to be housed in.
   *                     If {@code null}, monkey will be housed to any available isolation cage.
   *                     If it is not a valid isolation cage id or if the location is already
   *                     occupied, monkey will be housed to any available isolation cage.
   * @return a {@link Map} of {@link Primate}(representing the new monkey) and {@link Housing}(the
   * location where monkey is housed). This returns an empty map in case of an exception.
   * @throws IllegalArgumentException <ul><li>if {@code name} is {@code null} or empty.</li>
   *                                  <li>if {@code size} is {@code null}.</li>
   *                                  <li>if {@code weight} is zero or negative.</li>
   *                                  <li>if {@code age} is negative.</li>
   *                                  <li>if {@code species} is {@code null}.</li>
   *                                  <li>if {@code sex} is {@code null}.</li>
   *                                  <li>if {@code favoriteFood} is {@code null}.</li>
   *                                  <li>if {@code healthStatus} is {@code null}.</li>
   *                                  <li>if {@code isolationId} does not exist.</li> </ul>
   * @throws IllegalStateException    <ul><li>if {@code isolationId} is already occupied.</li>
   *                                  <li>if all isolation cages are occupied.</li></ul>
   */
  Map<Primate, Housing> addMonkey(String name, MonkeySize size, double weight, int age, Species species,
                                  Sex sex, FavoriteFood favoriteFood, HealthStatus healthStatus,
                                  UUID isolationId);

  /**
   * Updates the {@link Primate}'s {@link HealthStatus}.
   * If the {@code updatedHealthStatus} is equal to {@code HealthStatus.UNHEALTHY} and if the
   * {@code monkey} is currently residing in an enclosure then the {@code monkey} will be
   * automatically moved to an available isolation cage.
   *
   * @param updatedHealthStatus the new health status to be updated. This cannot be {@code null}.
   * @param monkey              the monkey whose health status need to be updated. This cannot be {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code updatedHealthStatus} is {@code null}.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li></ul>
   * @throws IllegalStateException    If no more isolation cages are available to house the updated
   *                                  unhealthy monkey currently residing in enclosure.
   */
  void updateMonkeyHealthStatus(HealthStatus updatedHealthStatus, Primate monkey);

  /**
   * Updates the {@link Primate}'s {@link MonkeySize}.
   * If the current enclosure housing the monkey cannot accommodate the monkey after updating the
   * monkey size, then the monkey is automatically moved to another available enclosure.
   *
   * @param updatedSize the new size to be updated. This cannot be {@code null}.
   * @param monkey      the monkey whose size need to be updated. This cannot be
   *                    {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code updatedSize} is {@code null}.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li></ul>
   * @throws IllegalStateException    If no enclosures are available to house the {@code monkey}
   *                                  after updating it's size.
   */
  void updateMonkeySize(MonkeySize updatedSize, Primate monkey);

  /**
   * Updates the {@link Primate}'s weight.
   *
   * @param updatedWeight the new weight to be updated. This cannot be zero or negative.
   * @param monkey        the monkey whose weight(in pounds) need to be updated. This cannot be
   *                      {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code updatedWeight} is zero or negative.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li></ul>
   */
  void updateMonkeyWeight(double updatedWeight, Primate monkey);

  /**
   * Updates the {@link Primate}'s age.
   *
   * @param updatedAge the new age(in years) to be updated. This cannot be less than the current age.
   * @param monkey     the monkey whose age need to be updated. This cannot be
   *                   {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code updatedAge} is less than current age.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li></ul>
   */
  void updateMonkeyAge(int updatedAge, Primate monkey);

  /**
   * Updates the {@link Primate}'s {@link FavoriteFood}.
   *
   * @param favoriteFood the new favorite food to be updated. This cannot be {@code null}.
   * @param monkey       the monkey whose favorite food need to be updated. This cannot be
   *                     {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code favoriteFood} is {@code null}.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li></ul>
   */
  void updateFavoriteFood(FavoriteFood favoriteFood, Primate monkey);

  /**
   * Removes the {@link Primate}'s from sanctuary.
   * This is used in case a monkey is dead or is moved to another sanctuary.
   *
   * @param monkey the monkey that needs to be removed. This cannot be
   *               {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code monkey} is {@code null}.</li>
   *                                  <li>if {@code monkey} does not exist in the sanctuary.</li></ul>
   */
  void removeMonkey(Primate monkey);

  /**
   * Gets the {@link List} of all {@link Primate}s (monkeys) living in the sanctuary.
   *
   * @return the list of monkeys. This can be empty but will never be {@code null}.
   */
  List<Primate> getMonkeys();

  /**
   * Gets the {@link List} of all {@link UUID}s of {@link Primate}s (monkeys) living in the
   * sanctuary.
   *
   * @return the list of monkey ids. This can be empty but will never be {@code null}.
   */
  List<UUID> getMonkeyIds();

  /**
   * Gets the {@link List} of all {@link Primate}s (monkeys) who lived in the sanctuary. The monkeys
   * that are either dead or that don't live in the sanctuary anymore.
   *
   * @return the list of alumni monkeys. This can be empty but will never be {@code null}.
   */
  List<Primate> getAlumniMonkeys();

  /**
   * Moves the {@link Primate} to the mentioned location.
   *
   * @param housingId the id of the location. This cannot be {@code null}.
   * @param monkey    the monkey to be moved. This cannot be {@code null}.
   * @throws IllegalArgumentException <ul><li>if {@code housingId} is {@code null}.</li>
   *                                  <li>if {@code monkey} is {@code null}.</li>
   *                                  <li>if {@code housingId} does not exist in sanctuary.</li>
   *                                  <li>if {@code monkey} does not exist in sanctuary.</li></ul>
   * @throws IllegalStateException    <ul><li>if unhealthy monkey is tried to move to enclosure.</li>
   *                                  <li>if {@code housingId} location cannot accommodate {@code monkey}.</li></ul>
   */
  void moveMonkey(UUID housingId, Primate monkey);

  /**
   * Moves the {@link Primate} to an available isolation cage.
   *
   * @param monkey the monkey to be moved. This cannot be {@code null}.
   * @return the {@link Housing} isolation the {@code monkey} is moved to. This can be {@code null}
   * if monkey is not moved to any isolation(in case of exceptions).
   * @throws IllegalArgumentException <ul><li>if {@code monkey} is {@code null}.</li>
   *                                  <li>if {@code monkey} does not exist in sanctuary.</li></ul>
   * @throws IllegalStateException    if no more isolation cages are left.
   */
  Housing moveMonkeyToIsolation(Primate monkey);

  /**
   * Moves the {@link Primate} to an available enclosure.
   *
   * @param monkey the monkey to be moved. This cannot be {@code null}.
   * @return the {@link Housing} enclosure the {@code monkey} is moved to. This can be {@code null}
   * if monkey is not moved to any enclosure(in case of exceptions).
   * @throws IllegalArgumentException <ul><li>if {@code monkey} is {@code null}.</li>
   *                                  <li>if {@code monkey} does not exist in sanctuary.</li></ul>
   * @throws IllegalStateException    <ul><li>if no enclosure that can house {@code monkey} is left.</li>
   *                                  <li>if unhealthy monkey is tried to move to enclosure.</li></ul>
   */
  Housing moveMonkeyToEnclosure(Primate monkey) throws UnsupportedOperationException;

  /**
   * Gets the enclosure sign for the given enclosure id containing a map of the name of each
   * individual {@link Primate} that is currently housed with their {@link Sex}, and
   * {@link FavoriteFood}.
   *
   * @param enclosureId the id of the enclosure. Returns an empty map in case of no primate living
   *                    there. This will never be {@code null}.
   * @return the map of primate's name with map of its sex and favorite food.
   * @throws IllegalArgumentException <ul><li>if {@code enclosureId} is {@code null}.</li>
   * <li> if {@code enclosureId} does not exist in sanctuary.</li></ul>
   */
  Map<String, Map<Sex, FavoriteFood>> getEnclosureSign(UUID enclosureId);

  /**
   * Gets an alphabetical {@link Map} (by name) of all the {@link Primate}s housed in the Sanctuary
   * with the {@link Map} of its {@link HousingType} and housing {@link UUID}.
   *
   * @return primate's name with map of its housing type and location id.Returns an empty map in
   * case of no primate living in the sanctuary. This will never be {@code null}.
   */
  Map<String, Map<HousingType, UUID>> getAllMonkeysWithLocations();

  /**
   * Gets an alphabetical {@link Map} of all the {@link Species}s housed in the Sanctuary
   * with the {@link Map} of its {@link HousingType} and housing {@link UUID}.
   *
   * @return a map of species with map of its housing type and location id. This will never be {@code null}.
   */
  Map<Species, Map<HousingType, List<UUID>>> getSpeciesWithLocations();

  /**
   * Gets a {@link Map} of all locations(housing type and id) where a particular {@link Species} is
   * currently housed. This will never be {@code null}.
   * @param species the species of primate.
   * @return a map of housing type with its id.
   */
  Map<HousingType, List<UUID>> getLocationsForASpecies(Species species);

  /**
   * Gets the shopping list of all {@link FavoriteFood}s of the inhabitants of the sanctuary with
   * its quantity in grams.
   * @return the map of favorite food and its quantity in grams. This will never be {@code null}.
   */
  Map<FavoriteFood, Integer> getFavFoodShoppingList();
}
