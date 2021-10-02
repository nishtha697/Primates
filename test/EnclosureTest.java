import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import housing.attributes.HousingType;
import monkey.attributes.FavoriteFood;
import monkey.attributes.HealthStatus;
import monkey.attributes.MonkeySize;
import monkey.attributes.Sex;
import monkey.attributes.Species;
import sanctuary.Enclosure;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link sanctuary.Enclosure}.
 */
public class EnclosureTest {
  Sanctuary jungle;
  Primate monkey;
  Housing enclosure;

  @Before
  public void setUp() {
    jungle = new JungleFriendsSanctuary(1, 2, new int[]{15, 20});
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
    enclosure = jungle.moveMonkeyToEnclosure(monkey);
  }

  @Test
  public void getId() {
    assertNotNull(enclosure.getId());
  }

  @Test
  public void getResidents() {
    assertEquals(Collections.singletonList(monkey), enclosure.getResidents());
  }

  @Test
  public void getResidentsEmptyEnclosure() {
    jungle.removeMonkey(monkey);
    assertEquals(Collections.EMPTY_LIST, enclosure.getResidents());
  }

  @Test
  public void isLocationAvailableForSameSpeciesWithinCapacity() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
    assertTrue(enclosure.isLocationAvailable(monkey));
  }

  @Test
  public void isLocationAvailableForSameSpeciesOverCapacity() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
    assertFalse(enclosure.isLocationAvailable(monkey));
  }

  @Test
  public void isLocationAvailableForDifferentSpecies() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23.8, 10,
            Species.CAPUCHIN, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
    assertFalse(enclosure.isLocationAvailable(monkey));
  }

  @Test
  public void getHousingType() {
    assertEquals(HousingType.ENCLOSURE, enclosure.getHousingType());
  }

  @Test
  public void getSpecies() {
    assertEquals(Species.MARMOSET, enclosure.getSpecies());
  }

  @Test
  public void testEquals() {
    Housing firstEnclosure = jungle.getHousings().get(0);
    Housing secondEnclosure = jungle.getHousings().get(1);
    Housing firstEnclosureCopy = firstEnclosure;
    Housing firstIsolation = jungle.getHousings().get(jungle.getTotalNumOfEnclosures());

    assertNotEquals(firstEnclosure, secondEnclosure);
    assertNotEquals(firstEnclosure, firstIsolation);
    assertEquals(firstEnclosure, firstEnclosureCopy);
  }

  @Test
  public void testHashCode() {
    Enclosure firstEnclosure = (Enclosure) jungle.getHousings().get(0);
    Enclosure secondEnclosure = (Enclosure) jungle.getHousings().get(1);
    Enclosure firstEnclosureCopy = firstEnclosure;

    assertNotEquals(firstEnclosure.hashCode(), secondEnclosure.hashCode());
    assertEquals(firstEnclosure.hashCode(), firstEnclosureCopy.hashCode());
  }
}