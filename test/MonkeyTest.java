import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import monkey.attributes.FavoriteFood;
import monkey.attributes.HealthStatus;
import monkey.attributes.MonkeySize;
import monkey.attributes.Sex;
import monkey.attributes.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link sanctuary.Monkey}.
 */
public class MonkeyTest {

  Sanctuary jungle;
  Primate monkey;
  Primate monkey2;
  Primate monkey3;

  @Before
  public void setUp() {
    jungle = new JungleFriendsSanctuary(3, 1, new int[]{10});
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();

    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey2 = monkey2WithLocation.keySet().iterator().next();

    monkey3 = monkey;
  }

  @Test
  public void getId() {
    assertNotNull(monkey.getId());
  }

  @Test
  public void getName() {
    assertEquals("Peter", monkey.getName());
  }

  @Test
  public void getSize() {
    assertEquals(MonkeySize.LARGE, monkey.getSize());

  }

  @Test
  public void getWeight() {
    assertEquals(23.8, monkey.getWeight(), 0.001);
  }

  @Test
  public void getAge() {
    assertEquals(10, monkey.getAge());
  }

  @Test
  public void getSpecies() {
    assertEquals(Species.MARMOSET, monkey.getSpecies());
  }

  @Test
  public void getSex() {
    assertEquals(Sex.MALE, monkey.getSex());
  }

  @Test
  public void getHealthStatus() {
    assertEquals(HealthStatus.UNHEALTHY, monkey.getHealthStatus());
  }

  @Test
  public void getFavoriteFood() {
    assertEquals(FavoriteFood.SEEDS, monkey.getFavoriteFood());
  }

  @Test
  public void testEquals() {
    assertNotEquals(monkey, monkey2);
    assertNotEquals(monkey2, monkey3);
    assertEquals(monkey, monkey3);
  }

  @Test
  public void testHashCode() {
    assertNotEquals(monkey.hashCode(), monkey2.hashCode());
    assertEquals(monkey.hashCode(), monkey3.hashCode());
  }
}