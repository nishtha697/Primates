package sanctuary;

import java.util.List;
import java.util.UUID;

import enums.HousingType;
import enums.Species;

/**
 * A sanctuary.Housing represents a housing in the sanctuary.
 */
public interface Housing{

  public UUID getId();

  public List<Primate> getResidents();

  public Species getSpecies();

  public HousingType getHousingType();

  public boolean isLocationAvailable(Primate monkey);
}
