import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.HousingType;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class IsolationTest {

  Sanctuary jungle;
  Primate monkey;
  Housing isolation;

  @Before
  public void setUp(){
    jungle = new JungleFriendsSanctuary(2, 1, new int[]{15});
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
    isolation = monkeyWithLocation.get(monkey);
  }

  @Test
  public void getId() {
    assertNotNull(isolation.getId());
  }

  @Test
  public void isLocationAvailable() {
    assertFalse(isolation.isLocationAvailable(monkey));
  }

  @Test
  public void isLocationAvailableEmptyIsolation() {
    jungle.removeMonkey(monkey);
    assertTrue(isolation.isLocationAvailable(monkey));
  }

  @Test
  public void getResidents() {
    assertEquals(Collections.singletonList(monkey), isolation.getResidents());
  }

  @Test
  public void getResidentsForEmptyIsolation() {
    jungle.removeMonkey(monkey);
    assertEquals(Collections.EMPTY_LIST, isolation.getResidents());
  }

  @Test
  public void getHousingType() {
    assertEquals(HousingType.ISOLATION, isolation.getHousingType());
  }

  @Test
  public void getSpecies() {
    assertEquals(Species.MARMOSET, isolation.getSpecies());

  }

  @Test
  public void getSpeciesForEmptyIsolation() {
    jungle.removeMonkey(monkey);
    assertNull(isolation.getSpecies());

  }
}