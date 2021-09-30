package sanctuary;

import java.util.UUID;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;

/**
 * A sanctuary.Primate represents a new world primate that is housed in the sanctuary.
 */
public interface Primate{

  /**
   * Returns unique identifier for the primate. It will never be {@code null} or empty.
   *
   * @return id
   */
  UUID getId();

  /**
   * Returns the name for the primate. It will never be {@code null} or empty.
   *
   * @return name
   */
  String getName();

  /**
   * Returns the size of the primate in {@link MonkeySize}. It will never be {@code null}.
   *
   * @return size
   */
  MonkeySize getSize();

  /**
   * Returns the weight of the primate in pounds. It will always be a positive value.
   *
   * @return weight
   */
  double getWeight();

  /**
   * Returns the age of the primate in years. It will never be zero.
   *
   * @return age
   */
  int getAge();

  /**
   * Returns the species of the primate in {@link Species}. It will never be {@code null}.
   *
   * @return species
   */
  Species getSpecies();

  /**
   * Returns the sex of the primate in {@link Sex}. It will never be {@code null}.
   *
   * @return sex
   */
  Sex getSex();

  /**
   * Returns the health status of the primate in {@link HealthStatus}. It will never be
   * @code null}.
   *
   * @return health status
   */
  HealthStatus getHealthStatus();

  /**
   * Returns the favorite food of the primate in {@link FavoriteFood}. It will never be
   * {@code null}.
   *
   * @return favorite food
   */
  FavoriteFood getFavoriteFood();
}

