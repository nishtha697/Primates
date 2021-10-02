import java.util.List;
import java.util.Map;
import java.util.UUID;

import housing.attributes.HousingType;
import monkey.attributes.FavoriteFood;
import monkey.attributes.HealthStatus;
import monkey.attributes.MonkeySize;
import monkey.attributes.Sex;
import monkey.attributes.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

/**
 * Driver class.
 */
public class Driver {

  /** Driver main().
   *
   * @param args arguments.
   */
  public static void main(String[] args) {

    Sanctuary jungle = new JungleFriendsSanctuary(8, 5,
            new int[]{10, 20, 15, 60, 80});

    System.out.println("Jungle Friends Primate Sanctuary created.");

    System.out.println("\nTotal Enclosures: " + jungle.getTotalNumOfEnclosures() + " Total "
            + "Isolations: " + jungle.getTotalNumOfIsolationCages());

    //adds monkey with location id that does not exist throws exception
    System.out.println("\nAdding monkey with location id that does not exist");
    try {
      jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10, Species.MARMOSET,
              Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, UUID.randomUUID());
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    //adds monkey with enclosure location id throws exception
    System.out.println("\nAdding monkey with with enclosure location id");
    try {
      jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10, Species.MARMOSET,
              Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, jungle.getHousings().get(0)
                      .getId());
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    // add monkey
    System.out.println("\nAdding monkeys");
    Map<Primate, Housing> monkeyWithLocation = jungle.addMonkey("Bruce", MonkeySize.LARGE,
            45, 11, Species.MARMOSET, Sex.MALE, FavoriteFood.FRUITS,
            HealthStatus.HEALTHY, jungle.getHousings().get(5).getId());
    Primate primate1 = monkeyWithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate1.getName() + "(" + primate1.getId() + ") is added to "
            + "the sanctuary at isolation " + monkeyWithLocation.get(primate1).getId());


    //adds a few monkeys
    //add monkey with mull location adds it any available isolation cage
    Map<Primate, Housing> monkey2WithLocation = jungle.addMonkey("Robert", MonkeySize.MEDIUM,
            15, 9, Species.MARMOSET, Sex.MALE, FavoriteFood.EGGS, HealthStatus.HEALTHY,
            null);
    Primate primate2 = monkey2WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate2.getName() + "(" + primate2.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey2WithLocation.get(primate2).getId());

    Map<Primate, Housing> monkey3WithLocation = jungle.addMonkey("Natalie", MonkeySize.SMALL,
            30, 17, Species.MARMOSET, Sex.FEMALE, FavoriteFood.TREESAP,
            HealthStatus.HEALTHY, null);
    Primate primate3 = monkey3WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate3.getName() + "(" + primate3.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey3WithLocation.get(primate3).getId());

    Map<Primate, Housing> monkey4WithLocation = jungle.addMonkey("Simon", MonkeySize.SMALL,
            21, 9, Species.NIGHT, Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY,
            null);
    Primate primate4 = monkey4WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate4.getName() + "(" + primate4.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey4WithLocation.get(primate4).getId());

    Map<Primate, Housing> monkey5WithLocation = jungle.addMonkey("Calli", MonkeySize.MEDIUM,
            15, 9, Species.SAKI, Sex.MALE, FavoriteFood.EGGS, HealthStatus.HEALTHY,
            null);
    Primate primate5 = monkey5WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate5.getName() + "(" + primate5.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey5WithLocation.get(primate5).getId());

    Map<Primate, Housing> monkey6WithLocation = jungle.addMonkey("Sid", MonkeySize.SMALL,
            30, 17, Species.UAKARIS, Sex.FEMALE, FavoriteFood.TREESAP,
            HealthStatus.HEALTHY, null);
    Primate primate6 = monkey6WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate6.getName() + "(" + primate6.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey6WithLocation.get(primate6).getId());

    Map<Primate, Housing> monkey7WithLocation = jungle.addMonkey("Cass", MonkeySize.SMALL,
            21, 9, Species.NIGHT, Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY,
            null);
    Primate primate7 = monkey7WithLocation.keySet().iterator().next();
    System.out.println("Monkey " + primate7.getName() + "(" + primate7.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey7WithLocation.get(primate7).getId());

    //add monkey to a full isolation throws error
    System.out.println("\nAdding monkey to a full isolation cage");
    try {
      jungle.addMonkey("Wanda", MonkeySize.MEDIUM, 22, 7, Species.HOWLER,
              Sex.FEMALE, FavoriteFood.LEAVES, HealthStatus.HEALTHY, jungle.getHousings().get(5)
                      .getId());
    } catch (IllegalStateException e) {
      System.out.println(e);
    }

    Map<Primate, Housing> monkey8WithLocation = jungle.addMonkey("Monty", MonkeySize.SMALL,
            21, 9, Species.TAMARIN,
            Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, null);
    Primate primate8 = monkey8WithLocation.keySet().iterator().next();
    System.out.println("\nMonkey " + primate8.getName() + "(" + primate8.getId() + ") is added to "
            + "the sanctuary at isolation " + monkey8WithLocation.get(primate8).getId());


    //adding monkey when all isolation are full throws exception
    System.out.println("\nAdding monkey when all isolations are full");
    try {
      jungle.addMonkey("Carol", MonkeySize.SMALL, 21, 9, Species.SPIDER,
              Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, null);
    } catch (IllegalStateException e) {
      System.out.println(e);
    }

    //add monkeys to enclosures
    System.out.println("\nMove monkeys to Enclosures.");
    Housing enclosure = jungle.moveMonkeyToEnclosure(primate2);
    System.out.println("Monkey " + primate2.getName() + "(" + primate2.getId() + ") is moved to "
            + "enclosure " + enclosure.getId());
    Housing enclosure2 = jungle.moveMonkeyToEnclosure(primate3);
    System.out.println("Monkey " + primate3.getName() + "(" + primate3.getId() + ") is moved to "
            + "enclosure " + enclosure2.getId());
    Housing enclosure3 = jungle.moveMonkeyToEnclosure(primate4);
    System.out.println("Monkey " + primate4.getName() + "(" + primate4.getId() + ") is moved to "
            + "enclosure " + enclosure3.getId());
    Housing enclosure4 = jungle.moveMonkeyToEnclosure(primate5);
    System.out.println("Monkey " + primate5.getName() + "(" + primate5.getId() + ") is moved to "
            + "enclosure " + enclosure4.getId());

    //all monkeys with locations
    System.out.println("\nList of all monkeys with location: ");
    Map<String, Map<HousingType, UUID>> allMonkeysWithLocations = jungle
            .getAllMonkeysWithLocations();

    for (Map.Entry<String, Map<HousingType, UUID>> entry : allMonkeysWithLocations.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }

    //enclosure sign
    System.out.println("\nEnclosure sign for " + jungle.getHousings().get(0).getId());
    Map<String, Map<Sex, FavoriteFood>> enclosureSign = jungle.getEnclosureSign(jungle.getHousings()
            .get(0).getId());
    for (Map.Entry<String, Map<Sex, FavoriteFood>> entry : enclosureSign.entrySet()) {
      System.out.println("Name : " + entry.getKey());
      for (Map.Entry<Sex, FavoriteFood> entry1 : entry.getValue().entrySet()) {
        System.out.println("Sex : " + entry1.getKey());
        System.out.println("Favorite Food : " + entry1.getValue());
      }
      System.out.println();
    }

    //enclosure sign for wrong enclosure id that does not exist in sanctuary.
    UUID randomID = UUID.randomUUID();
    System.out.println("\nEnclosure sign for id that does not exist in sanctuary: " + randomID);
    try {
      System.out.println(jungle.getEnclosureSign(randomID));
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    //all species with locations
    System.out.println("\nList of all species with locations: ");
    Map<Species, Map<HousingType, List<UUID>>> allSpeciesWithLocations = jungle
            .getSpeciesWithLocations();
    for (Map.Entry<Species, Map<HousingType, List<UUID>>> entry : allSpeciesWithLocations
            .entrySet()) {
      System.out.println(entry.getKey());
      if (entry.getValue().isEmpty()) {
        System.out.println("Not currently housed in the Sanctuary.");
      }
      for (Map.Entry<HousingType, List<UUID>> entry1 : entry.getValue().entrySet()) {
        System.out.println(entry1.getKey() + " : " + entry1.getValue());
      }
      System.out.println();
    }

    System.out.println("\nLooking up particular species locations.");
    System.out.println("Locations for Capuchin: ");
    Map<HousingType, List<UUID>> capuchinLocations = jungle.getLocationsForASpecies(
            Species.CAPUCHIN);
    if (capuchinLocations.isEmpty()) {
      System.out.println(Species.CAPUCHIN + " is not currently housed in Sanctuary.");
    } else {
      for (Map.Entry<HousingType, List<UUID>> entry1 : capuchinLocations.entrySet()) {
        System.out.println(entry1.getKey() + " : " + entry1.getValue());

      }
    }

    System.out.println("\nLocations for Marmoset: ");
    Map<HousingType, List<UUID>> marmosetLocations = jungle.getLocationsForASpecies(
            Species.MARMOSET);
    if (marmosetLocations.isEmpty()) {
      System.out.println(Species.MARMOSET + " are not currently housed in Sanctuary.");
    } else {
      for (Map.Entry<HousingType, List<UUID>> entry1 : marmosetLocations.entrySet()) {
        System.out.println(entry1.getKey() + " : " + entry1.getValue());
      }
    }

    System.out.println("\nShopping list for all residents: ");
    Map<FavoriteFood, Integer> shoppingList = jungle.getFavFoodShoppingList();
    for (Map.Entry<FavoriteFood, Integer> entry1 : shoppingList.entrySet()) {
      System.out.println(entry1.getKey() + " : " + entry1.getValue() + " grams");
    }
  }
}



