package sanctuary;

import java.util.List;
import java.util.UUID;

import housingAttributes.HousingType;
import monkeyAttributes.Species;

/**
 * A sanctuary.Housing represents a housing in the sanctuary.
 */
public interface Housing{

  /**
   * Returns unique identifier for the hosuing. It will never be {@code null} or empty.
   *
   * @return id
   */
  public UUID getId();

  /**
   * Gets the {@link List} of all {@link Primate}s living in the housing.
   * @return list of primates.
   */
  public List<Primate> getResidents();

  /**
   * Gets the {@link Species} of the {@link Primate}(s) living in the housing.
   * @return the species.
   */
  public Species getSpecies();

  /**
   * Gets the {@link HousingType} hot the housing.
   * @return the housing type.
   */
  public HousingType getHousingType();

  /**
   * Returns if the housing is available to accommodate the {@code monkey}.
   * @param monkey the monkey
   * @return {@code true} if location is available otherwise {@code false}.
   */
  public boolean isLocationAvailable(Primate monkey);
}
