package sanctuary;

import java.util.List;

import enums.HousingType;
import enums.Species;

/**
 * 
 */
public interface Housing {

  public String getId();

  public List<Primate> getResidents();

  public Species getSpecies();

  public HousingType getHousingType();

  public boolean isLocationAvailable(Primate monkey);
}
