package sanctuary;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import enums.HousingType;
import enums.Species;

public class Isolation implements Housing{

  private final UUID id;
  private Primate monkey;


  public Isolation() {
    this.id = UUID.randomUUID();
    this.monkey = null;
  }

  @Override
  public UUID getId() {
    return id;
  }

   void addMonkey(Primate monkey) {
    this.monkey = monkey;
  }

  void removeMonkey(Primate monkey) {
    if (this.monkey.getId().equals(monkey.getId())) {
      this.monkey = null;
    }
  }

  @Override
  public boolean isLocationAvailable(Primate monkey) {

    if(this.getResidents() == null || this.getResidents().isEmpty()
            || this.getResidents().get(0) == null)
    {
      return true;
    }
    return false;
  }

  @Override
  public List<Primate> getResidents() {
    if(monkey != null){
      return Collections.singletonList(monkey);
    }
    return Collections.EMPTY_LIST;
  }

  @Override
  public HousingType getHousingType() {
    return HousingType.ISOLATION;
  }

  @Override
  public Species getSpecies(){
    if(this.monkey == null){
      return null;
    }
    return this.monkey.getSpecies();
  }
}
