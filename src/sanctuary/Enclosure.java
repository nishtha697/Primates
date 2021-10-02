package sanctuary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import housing.attributes.HousingType;
import monkey.attributes.FavoriteFood;
import monkey.attributes.Sex;
import monkey.attributes.Species;

/**
 * Implements {@link Housing} and represents an enclosure.
 */
public class Enclosure implements Housing {

  private final int capacity;
  private final UUID id;
  private final List<Primate> troop;


  Enclosure(int capacity) {
    this.id = UUID.randomUUID();
    this.troop = new ArrayList<>();
    this.capacity = capacity;
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public List<Primate> getResidents() {
    return troop;
  }

  @Override
  public boolean isLocationAvailable(Primate monkey) {
    if (monkey.getSize().getSpace() <= this.getAvailableCapacity() && this.getSpecies() == monkey
            .getSpecies()) {
      return true;
    } else {
      return this.getAvailableCapacity() == this.capacity;
    }
  }

  @Override
  public HousingType getHousingType() {
    return HousingType.ENCLOSURE;
  }

  @Override
  public Species getSpecies() {
    if (this.getResidents() == null || this.getResidents().isEmpty()) {
      return null;
    }
    return getResidents().get(0).getSpecies();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Enclosure)) {
      return false;
    }
    Enclosure enclosure = (Enclosure) o;
    return id.toString().equals(enclosure.id.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  void addMonkey(Primate monkey) {
    this.troop.add(monkey);
  }

  void removeMonkey(Primate monkey) {
    troop.remove(monkey);
  }

  int getAvailableCapacity() {
    int usedCapacity = 0;
    if (this.troop == null || this.troop.isEmpty()) {
      return this.capacity;
    }
    for (Primate monkey : this.troop) {
      usedCapacity += monkey.getSize().getSpace();
    }
    return this.capacity - usedCapacity;
  }

  Map<String, Map<Sex, FavoriteFood>> getEnclosureSign() {
    Map<String, Map<Sex, FavoriteFood>> sign = new HashMap<>();
    if (!this.getResidents().isEmpty()) {
      for (Primate monkey : this.getResidents()) {
        sign.put(monkey.getName(), Collections.singletonMap(monkey.getSex(),
                monkey.getFavoriteFood()));
      }
    }
    return sign;
  }
}
