package sanctuary;

import enums.FavoriteFood;
import enums.HousingType;
import enums.Sex;
import enums.Species;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Enclosure implements Housing{

  private List<Primate> troop;
  private final int capacity;
  private static int enclosureNumber;
  private final String id;


  public Enclosure(int capacity) {
    this.enclosureNumber += 1;
    this.id = "ENC" + this.enclosureNumber;
    this.troop = new ArrayList<>();
    this.capacity = capacity;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public List<Primate> getResidents()
  {
    return troop;
  }

   void removeMonkey(Primate monkey) {
    troop.remove(monkey);
  }

  @Override
  public boolean isLocationAvailable(Primate monkey) {
    if(monkey.getSize().getSpace() <= this.getAvailableCapacity() && this.getSpecies() == monkey.getSpecies())
    {
      return true;
    } else if (this.getAvailableCapacity() == this.capacity)
    {
      return true;
    }
    return false;
  }

  @Override
  public HousingType getHousingType() {
    return HousingType.ENCLOSURE;
  }

  @Override
  public Species getSpecies()
  {
    if(this.getResidents() == null || this.getResidents().isEmpty()){
      return null;
    }
    return getResidents().get(0).getSpecies();
  }

  void addMonkey(Primate monkey) {
    this.troop.add(monkey);
  }

  int getAvailableCapacity() {
    int usedCapacity = 0;
    if(this.troop == null || this.troop.isEmpty())
    {
      return this.capacity;
    }
    for (Primate monkey : this.troop) {
      usedCapacity += monkey.getSize().getSpace();
    }
    return this.capacity - usedCapacity;
  }

  Map<String, Map<Sex, FavoriteFood>> getEnclosureSign() {
    Map<String, Map<Sex, FavoriteFood>> sign = new HashMap<>();
    if(!this.getResidents().isEmpty()) {
      for (Primate monkey : this.getResidents()) {
        sign.put(monkey.getName(), Collections.singletonMap(monkey.getSex(),
                monkey.getFavoriteFood()));
      }
    }
    return sign;
  }
}
