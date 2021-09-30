package sanctuary;

import java.util.UUID;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

/**
 * Implements {@link Primate} and represents a monkey.
 */
public class Monkey implements Primate {

  private final UUID id;
  private final String name;
  private final Species species;
  private final Sex sex;
  private FavoriteFood favoriteFood;
  private MonkeySize size;
  private double weight;
  private int age;
  private HealthStatus healthStatus;

  /**
   * Constructs a monkey.
   *
   * @param name         the name of the monkey.
   * @param size         the size of the monkey.
   * @param weight       the weight of the monkey.
   * @param age          the age of the monkey.
   * @param species      the species of the monkey.
   * @param sex          the sex of the monkey.
   * @param favoriteFood the favorite food of the monkey.
   * @param healthStatus the health status of the monkey.
   * @throws IllegalArgumentException if {@code name} is {@code null} or empty.
   *                                  if {@code size} is {@code null}.
   *                                  if {@code weight} is negative or zero.
   *                                  if {@code age} is negative or zero.
   *                                  if {@code species} is {@code null}.
   *                                  if {@code sex} is {@code null}.
   *                                  if {@code favoriteFood} is {@code null}.
   *                                  if {@code healthStatus} is {@code null}.
   */
  Monkey(String name, MonkeySize size, double weight, int age, Species species, Sex sex,
         FavoriteFood favoriteFood, HealthStatus healthStatus) throws IllegalArgumentException {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Monkey's name cannot be null or empty.");
    }
    if (size == null) {
      throw new IllegalArgumentException("Monkey's size cannot be null.");
    }
    if (weight <= 0) {
      throw new IllegalArgumentException("Monkey's weight cannot be negative or zero.");
    }
    if (age <= 0) {
      throw new IllegalArgumentException("Monkey's age cannot be negative or zero.");
    }
    if (species == null) {
      throw new IllegalArgumentException("Monkey's species cannot be null.");
    }
    if (sex == null) {
      throw new IllegalArgumentException("Monkey's sex cannot be null.");
    }
    if (favoriteFood == null) {
      throw new IllegalArgumentException("Monkey's favorite food cannot be null.");
    }
    if (healthStatus == null) {
      throw new IllegalArgumentException("Monkey's health status cannot be null.");
    }
    this.id = UUID.randomUUID();
    this.name = name;
    this.size = size;
    this.weight = weight;
    this.species = species;
    this.sex = sex;
    this.favoriteFood = favoriteFood;
    this.healthStatus = healthStatus;
    this.age = age;
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public MonkeySize getSize() {
    return this.size;
  }

  @Override
  public double getWeight() {
    return this.weight;
  }

  @Override
  public int getAge() {
    return this.age;
  }

  @Override
  public Species getSpecies() {
    return this.species;
  }

  @Override
  public Sex getSex() {
    return this.sex;
  }

  @Override
  public HealthStatus getHealthStatus() {
    return this.healthStatus;
  }

  @Override
  public FavoriteFood getFavoriteFood() {
    return this.favoriteFood;
  }

  /**
   * Updates the age of the monkey.
   *
   * @param age updated age
   * @throws IllegalArgumentException if {@code age} is less than existing age.
   */
  void updateAge(int age) throws IllegalArgumentException {
    if (age < this.getAge()) {
      throw new IllegalArgumentException("Monkey's updated age cannot be less than existing age." +
              " Updated age: " + age + ", Existing age: " + this.getAge());
    }
    this.age = age;
  }

  /**
   * Updates the size of the monkey.
   *
   * @param size updated size
   * @throws IllegalArgumentException if {@code size} is {@code null} or smaller than existing size.
   */
  void updateSize(MonkeySize size) throws IllegalArgumentException {
    if (size == null) {
      throw new IllegalArgumentException("Monkey's updated size cannot be null.");
    }
    if (size.getSpace() < this.getSize().getSpace()) {
      throw new IllegalArgumentException("Invalid Size. Updated monkey size cannot be smaller " +
              "than existing size. Updated size: " + size + ", Existing monkey size: " +
              this.getSize());
    }
    this.size = size;
  }

  /**
   * Updates the weight of the monkey.
   *
   * @param weight updated weight
   * @throws IllegalArgumentException if {@code weight} is zero or negative.
   */
  void updateWeight(double weight) throws IllegalArgumentException {
    if (weight <= 0) {
      throw new IllegalArgumentException("Monkey's weight cannot be negative or zero.");
    }
    this.weight = weight;
  }

  /**
   * Updates the health status of the monkey.
   *
   * @param updatedHealthStatus updated health status
   * @throws IllegalArgumentException if {@code updatedHealthStatus} is {@code null}.
   */
  void updateHealthStatus(HealthStatus updatedHealthStatus) throws IllegalArgumentException {
    if (updatedHealthStatus == null) {
      throw new IllegalArgumentException("Monkey's updated health status cannot be " +
              "null.");
    }
    this.healthStatus = updatedHealthStatus;
  }

  /**
   * Updates the favorite food of the monkey.
   *
   * @param favoriteFood updated favorite food
   * @throws IllegalArgumentException if {@code favoriteFood} is {@code null}.
   */
  void updateFavoriteFood(FavoriteFood favoriteFood){
    if(favoriteFood == null){
      throw new IllegalArgumentException("Monkey's updated favorite food cannot be " +
              "null.");
    }
    this.favoriteFood = favoriteFood;
    }
}
