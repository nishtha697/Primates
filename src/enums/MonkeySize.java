package enums;

public enum MonkeySize {

  SMALL(1, 100), MEDIUM(5, 250), LARGE(10, 500);

  private final int space;
  private final int foodRequired;

  MonkeySize(int space, int foodRequired)
  {
    this.space = space;
    this.foodRequired = foodRequired;
  }

  /**
   * in square meters.
   * @return space required.
   */
  public int getSpace()
  {
    return this.space;
  }

  public int getFoodRequired(){
    return this.foodRequired;
  }
}
