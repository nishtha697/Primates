import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import monkeyAttributes.FavoriteFood;
import monkeyAttributes.HealthStatus;
import monkeyAttributes.MonkeySize;
import monkeyAttributes.Sex;
import monkeyAttributes.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link sanctuary.Primate}.
 */
public class PrimateTest {

  Sanctuary jungle;
  Primate monkey;

  @Before
  public void setUp(){
    jungle = new JungleFriendsSanctuary(1, 1, new int[]{10});
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Peter", MonkeySize.LARGE,
            23.8, 10,
            Species.MARMOSET, Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY,
            null);
    monkey = monkeyWithLocation.keySet().iterator().next();
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
}