import org.junit.Before;
import org.junit.Test;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import monkeyAttributes.FavoriteFood;
import monkeyAttributes.HealthStatus;
import housingAttributes.HousingType;
import monkeyAttributes.MonkeySize;
import monkeyAttributes.Sex;
import monkeyAttributes.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link JungleFriendsSanctuary}.
 */
public class JungleFriendsSanctuaryTest {

  Sanctuary jungle;
  int numberOfIsolation;
  int numberOfEnclosures;
  int[] sizeOfEnclosures;

  @Before
  public void setUp() {
    numberOfEnclosures = 2;
    numberOfIsolation = 5;
    sizeOfEnclosures = new int[]{15, 10};
    jungle = new JungleFriendsSanctuary(numberOfIsolation, numberOfEnclosures, sizeOfEnclosures);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuaryNegativeNumberOfIsolations() {
    new JungleFriendsSanctuary(-numberOfIsolation, numberOfEnclosures, sizeOfEnclosures);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuaryZeroNumberOfIsolations() {
    new JungleFriendsSanctuary(0, numberOfEnclosures, sizeOfEnclosures);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuaryNegativeNumberOfEnclosures() {
    new JungleFriendsSanctuary(numberOfIsolation, -numberOfEnclosures, sizeOfEnclosures);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuaryZeroNumberOfEnclosures() {
    new JungleFriendsSanctuary(numberOfIsolation, 0, sizeOfEnclosures);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuarySizeOfEnclosuresWrongLength() {
    new JungleFriendsSanctuary(numberOfIsolation, numberOfEnclosures, new int[]{8});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuarySizeOfEnclosuresZeroValues() {
    new JungleFriendsSanctuary(numberOfIsolation, numberOfEnclosures, new int[]{8, 0});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testJungleFriendsSanctuarySizeOfEnclosuresNegativeValues() {
    new JungleFriendsSanctuary(numberOfIsolation, numberOfEnclosures, new int[]{8, -10});
  }

  @Test
  public void testGetTotalNumOfIsolationCages() {
    assertEquals(5, jungle.getTotalNumOfIsolationCages());
  }

  @Test
  public void testGetTotalNumOfEnclosures() {
    assertEquals(2,
            jungle.getTotalNumOfEnclosures());
  }

  @Test
  public void testGetHousings() {
    List<Housing> actualValues = new ArrayList<>(jungle.getHousings());
    assertEquals(numberOfEnclosures + numberOfIsolation, actualValues.size());
  }

  @Test
  public void testGetHousingIds() {
    List<UUID> actualValues = new ArrayList<>(jungle.getHousingIds());
    assertEquals(numberOfEnclosures + numberOfIsolation, actualValues.size());
    for (Housing house : jungle.getHousings()) {
      assertTrue(actualValues.contains(house.getId()));
    }
  }

  @Test
  public void testAddCapacity() {
    assertEquals(7, jungle.getHousings().size());
    jungle.addCapacity(2, 1, new int[]{10});
    assertEquals(numberOfIsolation + 2, jungle.getTotalNumOfIsolationCages());
    assertEquals(numberOfEnclosures + 1, jungle.getTotalNumOfEnclosures());
    assertEquals(10, jungle.getHousings().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCapacityNegativeNumOfNewEnclosures() {
    jungle.addCapacity(2, -1, new int[]{10});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCapacityNegativeNumOfNewIsolations() {
    jungle.addCapacity(-2, 1, new int[]{10});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCapacitySizeOfEnclosuresWrongLength() {
    jungle.addCapacity(2, 2, new int[]{10, 15, 20});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCapacitySizeOfEnclosuresZeroValues() {
    jungle.addCapacity(2, 2, new int[]{10, 0});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddCapacitySizeOfEnclosuresNegativeValues() {
    jungle.addCapacity(2, 2, new int[]{-10, 20});
  }

  @Test
  public void testAddCapacityZeroValues() {
    assertEquals(7, jungle.getHousings().size());
    jungle.addCapacity(0, 0, new int[]{});
    assertEquals(numberOfIsolation, jungle.getTotalNumOfIsolationCages());
    assertEquals(numberOfEnclosures, jungle.getTotalNumOfEnclosures());
    assertEquals(7, jungle.getHousings().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullName() {
    jungle.addMonkey(null, MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithEmptyName() {
    jungle.addMonkey("", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullMonkeySize() {
    jungle.addMonkey("Peter", null,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithZeroWeight() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            0, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNegativeWeight() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            -23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullSpecies() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            12, 1,
            null, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullSex() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            12, 1,
            Species.MARMOSET, null, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullFavoriteFood() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            12, 1,
            Species.MARMOSET, Sex.MALE, null, HealthStatus.UNHEALTHY,
            null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithNullHealthStatus() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            12, 1,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, null,
            null);
  }

  @Test
  public void testAddMonkeyWithLocation() {
    UUID isolationId = jungle.getHousings().get(numberOfEnclosures + 3).getId();
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, isolationId);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertNotNull(monkey);
    assertTrue(jungle.getMonkeys().contains(monkey));
    assertTrue(jungle.getHousings().stream().filter(location ->
                    location.getId().equals(isolationId)).findFirst().orElse(null)
            .getResidents().contains(monkey));
    assertEquals(monkeyWithLocation.get(monkey).getId(), isolationId);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithEnclosureLocation() {
    UUID enclosureId = jungle.getHousings().get(0).getId();
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, enclosureId);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMonkeyWithInvalidLocation() {
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            UUID.randomUUID());
  }

  @Test(expected = IllegalStateException.class)
  public void testAddMonkeyWithOccupiedIsolationId() {
    UUID isolationId = jungle.getHousings().get(numberOfEnclosures).getId();

    //added first monkey to isolationId
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, isolationId);

    //adding another monkey to same id
    jungle.addMonkey("Sam", MonkeySize.LARGE,
            12, 5,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, isolationId);
  }

  @Test()
  public void testAddMonkeyWithZeroAge() {
    UUID isolationId = jungle.getHousings().get(numberOfEnclosures + 3).getId();
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 0,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, isolationId);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertNotNull(monkey);
    assertTrue(jungle.getMonkeys().contains(monkey));
    assertTrue(jungle.getHousings().stream().filter(location ->
                    location.getId().equals(isolationId)).findFirst().orElse(null)
            .getResidents().contains(monkey));
    assertEquals(monkeyWithLocation.get(monkey).getId(), isolationId);
  }

  @Test
  public void testAddMonkeyWithNullLocation() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    //All isolations are free and thus the first available isolation in the list is right after
    // enclosure locations.
    UUID firstIsolationId = jungle.getHousings().get(numberOfEnclosures).getId();
    assertNotNull(monkey);
    assertTrue(jungle.getMonkeys().contains(monkey));
    assertTrue(jungle.getHousings().stream().filter(location ->
                    location.getId().equals(firstIsolationId)).findFirst().orElse(null)
            .getResidents().contains(monkey));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddMonkeyIsolationsFull() {
    UUID isolationId = jungle.getHousings().get(numberOfEnclosures + 3).getId();
    jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, isolationId);
    for (int i = 0; i < numberOfIsolation; i++) {
      jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10,
              Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
              null);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyHealthStatusNullHealthStatus() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.updateMonkeyHealthStatus(null, monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyHealthStatusNullMonkey() {
    jungle.updateMonkeyHealthStatus(HealthStatus.UNHEALTHY, null);
  }

  @Test
  public void testUpdateMonkeyHealthStatusToHealthy() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(monkey.getHealthStatus(), HealthStatus.UNHEALTHY);
    jungle.updateMonkeyHealthStatus(HealthStatus.HEALTHY, monkey);
    assertEquals(monkey.getHealthStatus(), HealthStatus.HEALTHY);
  }

  @Test
  public void testUpdateMonkeyHealthStatusToUnhealthy() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(monkey.getHealthStatus(), HealthStatus.HEALTHY);
    jungle.updateMonkeyHealthStatus(HealthStatus.UNHEALTHY, monkey);
    assertEquals(monkey.getHealthStatus(), HealthStatus.UNHEALTHY);
  }

  @Test
  public void testUpdateMonkeyHealthStatusToUnhealthyMovedToIso() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.moveMonkey(jungle.getHousings().get(0).getId(), monkey);
    assertTrue(jungle.getHousings().get(0).getResidents().contains(monkey));
    assertSame(jungle.getHousings().get(0).getHousingType(), HousingType.ENCLOSURE);
    jungle.updateMonkeyHealthStatus(HealthStatus.UNHEALTHY, monkey);
    assertEquals(monkey.getHealthStatus(), HealthStatus.UNHEALTHY);
    assertFalse(jungle.getHousings().get(0).getResidents().contains(monkey));
    assertTrue(jungle.getHousings().stream().filter(location -> location.getHousingType()
            == HousingType.ISOLATION).anyMatch(location -> location.getResidents()
            .contains(monkey)));
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateMonkeyHealthStatusToUnhealthyNoMoreIsolations() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.moveMonkey(jungle.getHousings().get(0).getId(), monkey);
    assertTrue(jungle.getHousings().get(0).getResidents().contains(monkey));
    assertSame(jungle.getHousings().get(0).getHousingType(), HousingType.ENCLOSURE);
    for (int i = 0; i < numberOfIsolation; i++) {
      jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10,
              Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
              null);
    }
    jungle.updateMonkeyHealthStatus(HealthStatus.UNHEALTHY, monkey);
  }

  @Test
  public void testUpdateMonkeySize() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    assertEquals(monkey.getSize(), MonkeySize.SMALL);
    jungle.updateMonkeySize(MonkeySize.MEDIUM, monkey);
    assertEquals(monkey.getSize(), MonkeySize.MEDIUM);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeySizeToASmallerSize() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(monkey.getSize(), MonkeySize.LARGE);
    jungle.updateMonkeySize(MonkeySize.MEDIUM, monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeySizeNullMonkey() {
    jungle.updateMonkeySize(MonkeySize.MEDIUM, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeySizeNullSize() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.updateMonkeySize(null, monkey);
  }

  @Test(expected = IllegalStateException.class)
  public void testUpdateMonkeySizeToALargerSizeEnclosureFull() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Simon", MonkeySize.LARGE,
            23, 10, Species.SAKI, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();

    jungle.moveMonkeyToEnclosure(monkey);
    jungle.moveMonkeyToEnclosure(monkey2);

    Map<Primate, Housing> monkey3WithLocation = jungle.addMonkey("Simon", MonkeySize.MEDIUM,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey3 = monkey3WithLocation.keySet().iterator().next();

    jungle.moveMonkeyToEnclosure(monkey3);

    try {
      jungle.updateMonkeySize(MonkeySize.LARGE, monkey3);
    } catch (IllegalStateException re) {
      String message = "Monkey size increased and no more space left for "
              + "monkey in any Enclosures. Please find a suitable place for monkey "
              + monkey3.getName() + "(" + monkey3.getId() + ")using exchange agreement";
      assertEquals(message, re.getMessage());
      throw re;
    }
    fail("IllegalStateException for no space in enclosures is not thrown");
  }

  @Test
  public void testUpdateMonkeyWeight() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(monkey.getWeight(), 23, 0.001);
    jungle.updateMonkeyWeight(25.7, monkey);
    assertEquals(monkey.getWeight(), 25.7, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyWeightNullMonkey() {
    jungle.updateMonkeyWeight(25.7, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyWeightZeroWeight() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.updateMonkeyWeight(0, monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyWeightNegativeWeight() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.updateMonkeyWeight(-23.9, monkey);
  }

  @Test
  public void testUpdateMonkeyAge() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    assertEquals(monkey.getAge(), 10);
    jungle.updateMonkeyAge(11, monkey);
    assertEquals(monkey.getAge(), 11);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyAgeNullMonkey() {
    jungle.updateMonkeyAge(11, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateMonkeyAgeLessThanExistingAge() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    assertEquals(monkey.getAge(), 10);
    jungle.updateMonkeyAge(9, monkey);
  }

  @Test
  public void testUpdateFavoriteFood() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    assertEquals(monkey.getFavoriteFood(), FavoriteFood.SEEDS);
    jungle.updateFavoriteFood(FavoriteFood.INSECTS, monkey);
    assertEquals(monkey.getFavoriteFood(), FavoriteFood.INSECTS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateFavoriteFoodNullMonkey() {
    jungle.updateFavoriteFood(FavoriteFood.INSECTS, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdateFavoriteFoodNullFavoriteFood() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    jungle.updateFavoriteFood(null, monkey);
  }

  @Test
  public void testRemoveMonkey() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    UUID firstIsolationId = jungle.getHousings().get(numberOfEnclosures).getId();
    assertTrue(jungle.getMonkeys().contains(monkey));
    assertTrue(jungle.getHousings().stream().filter(location ->
                    location.getId().equals(firstIsolationId)).findFirst().orElse(null)
            .getResidents().contains(monkey));
    jungle.removeMonkey(monkey);
    assertFalse(jungle.getMonkeys().contains(monkey));
    assertFalse(jungle.getHousings().stream().filter(location ->
                    location.getId().equals(firstIsolationId)).findFirst().orElse(null)
            .getResidents().contains(monkey));
    assertTrue(jungle.getAlumniMonkeys().contains(monkey));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMonkeyNullMonkey() {
    jungle.removeMonkey(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemoveMonkeyNonExistingMonkey() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.removeMonkey(monkey);
    assertFalse(jungle.getMonkeys().contains(monkey));
    //monkey already removed, so doesn't exit in sanctuary anymore
    jungle.removeMonkey(monkey);
  }

  @Test
  public void testGetMonkeys() {
    assertTrue(jungle.getMonkeys().isEmpty());
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(jungle.getMonkeys().size(), 1);
    assertTrue(jungle.getMonkeys().contains(monkey));
  }

  @Test
  public void testGetMonkeyIds() {
    assertTrue(jungle.getMonkeyIds().isEmpty());
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertEquals(jungle.getMonkeyIds().size(), 1);
    assertTrue(jungle.getMonkeyIds().contains(monkey.getId()));
  }

  @Test
  public void testGetAlumniMonkeys() {
    assertTrue(jungle.getAlumniMonkeys().isEmpty());
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    assertTrue(jungle.getAlumniMonkeys().isEmpty());
    jungle.removeMonkey(monkey);
    assertEquals(jungle.getAlumniMonkeys().size(), 1);
    assertTrue(jungle.getAlumniMonkeys().contains(monkey));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyNullMonkey() {
    jungle.moveMonkey(jungle.getHousings().get(0).getId(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyNullHousingId() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    jungle.moveMonkey(null, monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyInValidHousingId() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.moveMonkey(UUID.randomUUID(), monkey);
  }

  @Test
  public void testMoveMonkey() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    UUID isolationId = jungle.getHousings().get(numberOfEnclosures + 2).getId();
    jungle.moveMonkey(isolationId, monkey);
    assertFalse(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    assertTrue(jungle.getHousings().get(numberOfEnclosures + 2).getResidents().contains(monkey));
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyUnhealthyMonkeyToEnclosure() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.UNHEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    UUID firstEnclosureId = jungle.getHousings().get(0).getId();
    jungle.moveMonkey(firstEnclosureId, monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyNotExistingInSanctuary() {
    UUID firstEnclosureId = jungle.getHousings().get(0).getId();
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    jungle.removeMonkey(monkey);
    try {
      jungle.moveMonkey(firstEnclosureId, monkey);
    } catch (IllegalArgumentException e) {
      String message = "Monkey " + monkey.getName() + "(" + monkey.getId() +
              ")does not exist in sanctuary";
      assertEquals(message, e.getMessage());
      throw e;
    }
    fail("IllegalArgumentException for non existential monkey is not thrown");
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyToAlreadyFullIsolation() {
    UUID firstIsolationId = jungle.getHousings().get(numberOfEnclosures).getId();
    jungle.addMonkey("Peter", MonkeySize.SMALL, 23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            firstIsolationId);
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    jungle.moveMonkey(firstIsolationId, monkey);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyToAlreadyFullEnclosure() {
    UUID firstEnclosureId = jungle.getHousings().get(0).getId();
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Carol", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();

    jungle.moveMonkey(firstEnclosureId, monkey);
    jungle.moveMonkey(firstEnclosureId, monkey2);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyInEnclosureOfDifferentSpecies() {
    UUID firstEnclosureId = jungle.getHousings().get(0).getId();
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.SPIDER, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();

    jungle.moveMonkey(firstEnclosureId, monkey);
    jungle.moveMonkey(firstEnclosureId, monkey2);
  }

  @Test
  public void testMoveMonkeyToIsolation() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    Housing isolation = jungle.moveMonkeyToIsolation(monkey);
    assertFalse(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    assertTrue(isolation.getResidents().contains(monkey));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyToIsolationMonkeyDoesNotExistInSanctuary() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    jungle.removeMonkey(monkey);
    Housing isolation = jungle.moveMonkeyToIsolation(monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyToIsolationNullMonkey() {
    jungle.moveMonkeyToIsolation(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyToIsolationAllIsolationsFull() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    for (int i = 0; i < numberOfIsolation - 1; i++) {
      jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10, Species.MARMOSET,
              Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY, null);
    }
    jungle.moveMonkeyToIsolation(monkey);
  }

  @Test
  public void testMoveMonkeyToEnclosure() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    Housing enclosure = jungle.moveMonkeyToEnclosure(monkey);
    assertFalse(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    assertTrue(enclosure.getResidents().contains(monkey));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyToEnclosureMonkeyDoesNotExistInSanctuary() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    jungle.removeMonkey(monkey);
    Housing isolation = jungle.moveMonkeyToEnclosure(monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMonkeyToIEnclosureNullMonkey() {
    jungle.moveMonkeyToEnclosure(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testMoveMonkeyToEnclosureUnhealthyMonkey() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    assertTrue(jungle.getHousings().get(numberOfEnclosures).getResidents().contains(monkey));
    jungle.moveMonkeyToEnclosure(monkey);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEnclosureSignNullEnclosureId() {
    jungle.getEnclosureSign(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEnclosureSignEnclosureIdDoesNotExist() {
    jungle.getEnclosureSign(UUID.randomUUID());
  }

  @Test
  public void testGetEnclosureSign() {
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();

    Housing enclosure = jungle.moveMonkeyToEnclosure(monkey);

    Map<String, Map<Sex, FavoriteFood>> actualEnclosureSign = jungle.getEnclosureSign(enclosure
            .getId());
    assertTrue(actualEnclosureSign.containsKey(monkey.getName()));
    assertTrue(actualEnclosureSign.entrySet().stream().anyMatch(e -> e.getValue().containsKey(
            monkey.getSex())));
    assertTrue(actualEnclosureSign.entrySet().stream().anyMatch(e -> e.getValue().containsValue(
            monkey.getFavoriteFood())));
  }

  @Test
  public void testGetAllMonkeysWithLocations() {
    //adding monkeys
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();
    Housing enclosure = jungle.moveMonkeyToEnclosure(monkey);

    //creating expected data
    Map<HousingType, UUID> expectedLocationMapForMonkey1 = Collections.singletonMap(
            HousingType.ENCLOSURE, enclosure.getId());
    Map<HousingType, UUID> expectedLocationMapForMonkey2 = Collections.singletonMap(
            HousingType.ISOLATION, monkey2WithLocation.get(monkey2).getId());
    Map<String, Map<HousingType, UUID>> expectedAllMonkeysMap = new HashMap<>();
    expectedAllMonkeysMap.put(monkey.getName(), expectedLocationMapForMonkey1);
    expectedAllMonkeysMap.put(monkey2.getName(), expectedLocationMapForMonkey2);

    //getting actual Map
    Map<String, Map<HousingType, UUID>> actualAllMonkeysWithLocations = jungle
            .getAllMonkeysWithLocations();

    //created ordered lists
    List<String> namesOrdered = new ArrayList<>();
    namesOrdered.add(monkey2.getName());
    namesOrdered.add(monkey.getName());

    List<Map<HousingType, UUID>> locationMapsOrdered = new ArrayList<>();
    locationMapsOrdered.add(expectedLocationMapForMonkey2);
    locationMapsOrdered.add(expectedLocationMapForMonkey1);

    //test if expected and actual are equal
    assertEquals(expectedAllMonkeysMap, actualAllMonkeysWithLocations);
    int i = 0;
    for (Map.Entry<String, Map<HousingType, UUID>> entry : actualAllMonkeysWithLocations
            .entrySet()) {
      assertEquals(entry.getKey(), namesOrdered.get(i));
      assertEquals(entry.getValue(), locationMapsOrdered.get(i));
      i++;
    }
  }

  @Test
  public void testGetSpeciesWithLocations() {
    jungle = new JungleFriendsSanctuary(12, numberOfEnclosures, sizeOfEnclosures);
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.SPIDER, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey3WithLocation = jungle.addMonkey("Bob", MonkeySize.SMALL,
            23, 10, Species.WOOLLY, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey3 = monkey3WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey4WithLocation = jungle.addMonkey("Kat", MonkeySize.SMALL,
            23, 10, Species.WOOLLY, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey4 = monkey4WithLocation.keySet().iterator().next();
    Housing enclosure = jungle.moveMonkeyToEnclosure(monkey4);
    Map<Primate, Housing> monkey5WithLocation = jungle.addMonkey("Zack", MonkeySize.SMALL,
            23, 10, Species.CAPUCHIN, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey5 = monkey5WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey6WithLocation = jungle.addMonkey("Leo", MonkeySize.SMALL,
            23, 10, Species.HOWLER, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey6 = monkey6WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey7WithLocation = jungle.addMonkey("Jimmy", MonkeySize.SMALL,
            23, 10, Species.NIGHT, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey7 = monkey7WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey8WithLocation = jungle.addMonkey("Sara", MonkeySize.SMALL,
            23, 10, Species.SAKI, Sex.FEMALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey8 = monkey8WithLocation.keySet().iterator().next();
    Housing enclosure2 = jungle.moveMonkeyToEnclosure(monkey8);
    Map<Primate, Housing> monkey9WithLocation = jungle.addMonkey("Cass", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey9 = monkey9WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey10WithLocation = jungle.addMonkey("Tom", MonkeySize.SMALL,
            23, 10, Species.TAMARIN, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey10 = monkey10WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey11WithLocation = jungle.addMonkey("Lola", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey11 = monkey11WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey12WithLocation = jungle.addMonkey("Lola", MonkeySize.SMALL,
            23, 10, Species.TITI, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey12 = monkey12WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey13WithLocation = jungle.addMonkey("Lola", MonkeySize.SMALL,
            23, 10, Species.UAKARIS, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.HEALTHY,
            null);
    Primate monkey13 = monkey13WithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey14WithLocation = jungle.addMonkey("Lola", MonkeySize.SMALL,
            23, 10, Species.WOOLLY_SPIDER, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey14 = monkey14WithLocation.keySet().iterator().next();


    //creating expected data
    Map<HousingType, List<UUID>> expectedLocationMapForMarmoset = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkeyWithLocation.get(monkey).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForSpider = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey2WithLocation.get(monkey2).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForWoolly = new HashMap<>();
    List<UUID> woollyLocationUUIDList = new ArrayList<>();
    woollyLocationUUIDList.add(monkey3WithLocation.get(monkey3).getId());
    expectedLocationMapForWoolly.put(HousingType.ISOLATION,woollyLocationUUIDList);
    expectedLocationMapForWoolly.put(HousingType.ENCLOSURE,Collections.singletonList(enclosure.getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForCapuchin = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey5WithLocation.get(monkey5).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForHowler = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey6WithLocation.get(monkey6).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForNight = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey7WithLocation.get(monkey7).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForSaki = Collections.singletonMap(
            HousingType.ENCLOSURE, Collections.singletonList(enclosure2.getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForSquirrel = new HashMap<>();
    List<UUID> squirrelLocationUUIDList = new ArrayList<>();
    squirrelLocationUUIDList.add(monkey9WithLocation.get(monkey9)
            .getId());
    squirrelLocationUUIDList.add(monkey11WithLocation.get(monkey11)
            .getId());
    expectedLocationMapForSquirrel.put(HousingType.ISOLATION, squirrelLocationUUIDList);
    Map<HousingType, List<UUID>> expectedLocationMapForTamarin = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey10WithLocation.get(monkey10).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForTiti = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey12WithLocation.get(monkey12).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForUakaris = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey13WithLocation.get(monkey13).getId()));
    Map<HousingType, List<UUID>> expectedLocationMapForWoolySpider = Collections.singletonMap(
            HousingType.ISOLATION, Collections.singletonList(monkey14WithLocation.get(monkey14).getId()));


    Map<Species, Map<HousingType, List<UUID>>> expectedAllSpeciesMap = new TreeMap<>(Comparator
            .comparing(Enum::toString));
    expectedAllSpeciesMap.put(Species.WOOLLY, expectedLocationMapForWoolly);
    expectedAllSpeciesMap.put(Species.WOOLLY_SPIDER, expectedLocationMapForWoolySpider);
    expectedAllSpeciesMap.put(Species.CAPUCHIN, expectedLocationMapForCapuchin);
    expectedAllSpeciesMap.put(Species.HOWLER, expectedLocationMapForHowler);
    expectedAllSpeciesMap.put(Species.MARMOSET, expectedLocationMapForMarmoset);
    expectedAllSpeciesMap.put(Species.NIGHT, expectedLocationMapForNight);
    expectedAllSpeciesMap.put(Species.TITI, expectedLocationMapForTiti);
    expectedAllSpeciesMap.put(Species.UAKARIS, expectedLocationMapForUakaris);
    expectedAllSpeciesMap.put(Species.SAKI, expectedLocationMapForSaki);
    expectedAllSpeciesMap.put(Species.SPIDER, expectedLocationMapForSpider);
    expectedAllSpeciesMap.put(Species.SQUIRREL, expectedLocationMapForSquirrel);
    expectedAllSpeciesMap.put(Species.TAMARIN, expectedLocationMapForTamarin);

    List<Species> speciesOrdered = new ArrayList<>();
    speciesOrdered.add(Species.CAPUCHIN);
    speciesOrdered.add(Species.HOWLER);
    speciesOrdered.add(Species.MARMOSET);
    speciesOrdered.add(Species.NIGHT);
    speciesOrdered.add(Species.SAKI);
    speciesOrdered.add(Species.SPIDER);
    speciesOrdered.add(Species.SQUIRREL);
    speciesOrdered.add(Species.TAMARIN);
    speciesOrdered.add(Species.TITI);
    speciesOrdered.add(Species.UAKARIS);
    speciesOrdered.add(Species.WOOLLY);
    speciesOrdered.add(Species.WOOLLY_SPIDER);

    List<Map<HousingType, List<UUID>>> locationMapsOrdered = new ArrayList<>();
    locationMapsOrdered.add(expectedLocationMapForCapuchin);
    locationMapsOrdered.add(expectedLocationMapForHowler);
    locationMapsOrdered.add(expectedLocationMapForMarmoset);
    locationMapsOrdered.add(expectedLocationMapForNight);
    locationMapsOrdered.add(expectedLocationMapForSaki);
    locationMapsOrdered.add(expectedLocationMapForSpider);
    locationMapsOrdered.add(expectedLocationMapForSquirrel);
    locationMapsOrdered.add(expectedLocationMapForTamarin);
    locationMapsOrdered.add(expectedLocationMapForTiti);
    locationMapsOrdered.add(expectedLocationMapForUakaris);
    locationMapsOrdered.add(expectedLocationMapForWoolly);
    locationMapsOrdered.add(expectedLocationMapForWoolySpider);

    // actual map
    Map<Species, Map<HousingType, List<UUID>>> actualAllSpeciesMap = jungle.getSpeciesWithLocations();

    //test
    assertEquals(expectedAllSpeciesMap, actualAllSpeciesMap);
    int i = 0;
    for (Map.Entry<Species, Map<HousingType, List<UUID>>> entry : actualAllSpeciesMap.entrySet()) {
      assertEquals(entry.getKey(), speciesOrdered.get(i));
      assertEquals(entry.getValue(), locationMapsOrdered.get(i));
      i++;
    }
  }

  @Test
  public void testGetSpeciesWithLocationsNoValues() {
    Map<Species, Map<HousingType, UUID>> expectedAllSpeciesMap = new HashMap<>();
    expectedAllSpeciesMap.put(Species.WOOLLY, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.WOOLLY_SPIDER, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.CAPUCHIN, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.HOWLER, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.MARMOSET, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.NIGHT, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.TITI, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.UAKARIS, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.SAKI, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.SPIDER, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.SQUIRREL, Collections.EMPTY_MAP);
    expectedAllSpeciesMap.put(Species.TAMARIN, Collections.EMPTY_MAP);
    assertEquals(jungle.getSpeciesWithLocations(), expectedAllSpeciesMap);
  }

  @Test
  public void getLocationsForSpecies() {
    //create data
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey = monkeyWithLocation.keySet().iterator().next();
    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    Primate monkey2 = monkey2WithLocation.keySet().iterator().next();
    Housing enclosure = jungle.moveMonkeyToEnclosure(monkey2);

    //expected data
    Map<HousingType, List<UUID>> expectedLocationMapForSquirrel = new HashMap<>();
    expectedLocationMapForSquirrel.put(HousingType.ISOLATION, Collections.singletonList(
            monkeyWithLocation.get(monkey)
            .getId()));
    expectedLocationMapForSquirrel.put(HousingType.ENCLOSURE, Collections.singletonList(
            enclosure.getId()));

    //actual map
    Map<HousingType, List<UUID>> actualSpeciesMapWithLocation = jungle.getLocationsForASpecies(
            Species.SQUIRREL);

    //test
    assertEquals(expectedLocationMapForSquirrel, actualSpeciesMapWithLocation);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getLocationsForSpeciesForNullSpecies() {
    jungle.getLocationsForASpecies(null);
  }

  @Test
  public void getLocationsForSpeciesNoValues() {
    assertEquals(jungle.getLocationsForASpecies(Species.SPIDER), Collections.EMPTY_MAP);
  }

  @Test
  public void getFavFoodShoppingList() {
    jungle = new JungleFriendsSanctuary(11, numberOfEnclosures, sizeOfEnclosures);
    jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Carol", MonkeySize.MEDIUM,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.NUTS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.FRUITS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Carol", MonkeySize.MEDIUM,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.INSECTS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Carol", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.SEEDS,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Carol", MonkeySize.LARGE,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.LEAVES,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Peter", MonkeySize.SMALL,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.EGGS, HealthStatus.HEALTHY,
            null);
    jungle.addMonkey("Carol", MonkeySize.MEDIUM,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.TREESAP,
            HealthStatus.HEALTHY, null);
    jungle.addMonkey("Peter", MonkeySize.LARGE,
            23, 10, Species.SQUIRREL, Sex.MALE, FavoriteFood.EGGS, HealthStatus.HEALTHY,
            null);
    jungle.addMonkey("Carol", MonkeySize.LARGE,
            23, 10, Species.SQUIRREL, Sex.FEMALE, FavoriteFood.TREESAP,
            HealthStatus.HEALTHY, null);


    //expected data
    Map<FavoriteFood, Integer> expectedFavFoodMap = new HashMap<>();
    expectedFavFoodMap.put(FavoriteFood.SEEDS, 300);
    expectedFavFoodMap.put(FavoriteFood.FRUITS, 500);
    expectedFavFoodMap.put(FavoriteFood.INSECTS, 250);
    expectedFavFoodMap.put(FavoriteFood.TREESAP, 750);
    expectedFavFoodMap.put(FavoriteFood.NUTS, 250);
    expectedFavFoodMap.put(FavoriteFood.LEAVES, 500);
    expectedFavFoodMap.put(FavoriteFood.EGGS, 600);

    //actual
    Map<FavoriteFood, Integer> actualFavFoodMap = jungle.getFavFoodShoppingList();

    //test
    assertEquals(expectedFavFoodMap, actualFavFoodMap);
  }
}