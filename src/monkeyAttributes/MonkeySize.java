package monkeyAttributes;

/**
 * Enum for monkey's size. Small monkeys (smaller than 10 cm) , medium monkeys (10 to 20 cm), and
 * large monkeys (larger than 20 cm).
 */
public enum MonkeySize {

  SMALL(1, 100), MEDIUM(5, 250), LARGE(10, 500);

  private final int space;
  private final int foodRequired;

  /**
   * Represents a monkey size.
   * @param space the space required by monkey in meter square.
   * @param foodRequired the food required by monkey in grams.
   */
  MonkeySize(int space, int foodRequired)
  {
    this.space = space;
    this.foodRequired = foodRequired;
  }

  /**
   * Gets the space required by monkey in square meters. 1 square meter for SMALL. 5 square meter
   * for MEDIUM. 10 square meter for LARGE.
   * @return space required.
   */
  public int getSpace()
  {
    return this.space;
  }

  /**
   * Gets the food required by monkey in grams. 100 grams for SMALL. 250 grams for MEDIUM. 500 grams
   * for LARGE.
   * @return the food required.
   */
  public int getFoodRequired(){
    return this.foodRequired;
  }
}
