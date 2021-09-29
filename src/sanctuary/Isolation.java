package sanctuary;

import java.util.Collections;
import java.util.List;

import enums.HousingType;
import enums.Species;

public class Isolation implements Housing{

  private final String id;
  private Primate monkey;
  private static int isolationNumber;


  public Isolation() {
    this.isolationNumber += 1;
    this.id = "ISO" + this.isolationNumber;
    this.monkey = null;
  }

  @Override
  public String getId() {
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
    return Collections.singletonList(monkey);
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
