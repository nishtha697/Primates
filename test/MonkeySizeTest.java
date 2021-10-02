import org.junit.Before;
import org.junit.Test;

import monkey.attributes.MonkeySize;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link MonkeySize}.
 */
public class MonkeySizeTest {

  MonkeySize small;
  MonkeySize medium;
  MonkeySize large;

  @Before
  public void setUp() {
    small = MonkeySize.SMALL;
    medium = MonkeySize.MEDIUM;
    large = MonkeySize.LARGE;
  }

  @Test
  public void getSpaceForSmall() {
    assertEquals(small.getSpace(), 1);
  }

  @Test
  public void getFoodRequiredForSmall() {
    assertEquals(small.getFoodRequired(), 100);
  }

  @Test
  public void getSpaceForMedium() {
    assertEquals(medium.getSpace(), 5);
  }

  @Test
  public void getFoodRequiredForMedium() {
    assertEquals(medium.getFoodRequired(), 250);

  }

  @Test
  public void getSpaceForLarge() {
    assertEquals(large.getSpace(), 10);

  }

  @Test
  public void getFoodRequiredForLarge() {
    assertEquals(large.getFoodRequired(), 500);
  }
}