import java.util.List;
import java.util.Map;

import enums.FavoriteFood;
import enums.HealthStatus;
import enums.MonkeySize;
import enums.Sex;
import enums.Species;
import sanctuary.Housing;
import sanctuary.JungleFriendsSanctuary;
import sanctuary.Primate;
import sanctuary.Sanctuary;

public class Driver {

  public static void main(String[] args) {

    Sanctuary jungle = new JungleFriendsSanctuary(8,5,
            new int[]{48, 45, 56, 60, 64});

    System.out.println("Total Enclosures: " + jungle.getTotalNumOfEnclosures() + " Total Isolations: "
             + jungle.getTotalNumOfIsolationCages());

    List<Housing> allLocations = jungle.getHousings();
    //New Monkeys
    try {
      jungle.addMonkey("Jake", MonkeySize.LARGE, 19, 3, Species.MARMOSET,
              Sex.MALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, "ISO03");
    } catch(IllegalArgumentException e){
      System.out.println(e);
    }

    try {
      jungle.addMonkey("Jake", MonkeySize.LARGE, 20, 5, Species.MARMOSET,
              Sex.MALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, "ENC1");
    } catch(IllegalStateException e){
      System.out.println(e);
    }


    jungle.addMonkey("Tony", MonkeySize.MEDIUM, 14, 8, Species.NIGHT,
            Sex.MALE, FavoriteFood.INSECTS, HealthStatus.HEALTHY, "ISO6");

    try {
      jungle.addMonkey("Steve", MonkeySize.SMALL, 17, 6, Species.SPIDER,
              Sex.MALE, FavoriteFood.INSECTS, HealthStatus.HEALTHY, "ISO6");
    } catch(IllegalStateException e){
      System.out.println(e);
    }

    jungle.addMonkey("Peter", MonkeySize.LARGE, 23, 10,Species.MARMOSET,
            Sex.MALE, FavoriteFood.SEEDS, HealthStatus.UNHEALTHY, null);
    jungle.addMonkey("Bruce", MonkeySize.LARGE, 45, 11, Species.MARMOSET,
            Sex.MALE, FavoriteFood.FRUITS, HealthStatus.HEALTHY, null);
    jungle.addMonkey("Robert", MonkeySize.MEDIUM, 15, 9, Species.SAKI,
            Sex.MALE, FavoriteFood.EGGS, HealthStatus.HEALTHY, null);
    jungle.addMonkey("Natalie", MonkeySize.SMALL, 30, 17, Species.TAMARIN,
            Sex.FEMALE, FavoriteFood.TREESAP, HealthStatus.HEALTHY, null);
    jungle.addMonkey("Simon", MonkeySize.SMALL, 21, 9,Species.NIGHT,
            Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, null);
    jungle.addMonkey("Wanda", MonkeySize.MEDIUM, 22, 7, Species.HOWLER,
            Sex.FEMALE, FavoriteFood.LEAVES, HealthStatus.HEALTHY, null);


    List<Primate> monkeys = jungle.getMonkeys();

    System.out.println("\nAll Locations: ");
    for(Housing location : jungle.getHousings())
    {
      System.out.print(location.getId() + " ");
    }

    System.out.println("\n\nAll species with Locations: " + jungle.getAllMonkeysWithLocations());

    try {
      jungle.moveMonkeyToEnclosure(monkeys.get(3));
    } catch(IllegalStateException e){
      System.out.println("\n" + e);
    }

    //Adding unhealthy monkey to Enclosure.
    try {
      jungle.moveMonkeyToEnclosure(monkeys.get(4));
    } catch(IllegalStateException e){
      System.out.println("\n" + e);
    }

    try {
      jungle.moveMonkey("ENC2", monkeys.get(4));
    } catch(IllegalStateException e){
      System.out.println("\n" + e);
    }

    jungle.updateMonkeyHealthStatus(HealthStatus.HEALTHY, monkeys.get(4));
    jungle.moveMonkey("ENC2", monkeys.get(4));
    System.out.println("\nAll species with Locations: " + jungle.getAllMonkeysWithLocations());

    jungle.updateMonkeyHealthStatus(HealthStatus.UNHEALTHY, monkeys.get(4));
    System.out.println("\nAll species with Locations: " + jungle.getAllMonkeysWithLocations());

    jungle.moveMonkeyToEnclosure(monkeys.get(7));
    try {
      jungle.moveMonkey("ENC1", monkeys.get(8));
    } catch (IllegalStateException e) {
      System.out.println("\n" + e);
    }


    jungle.moveMonkeyToIsolation(monkeys.get(8));


    //No more isolation cages available
    try {
      jungle.addMonkey("Karen", MonkeySize.LARGE, 44, 11, Species.NIGHT,
              Sex.FEMALE, FavoriteFood.NUTS, HealthStatus.HEALTHY, null);
    } catch (IllegalStateException e){
      System.out.println("\n" + e);
    }

    System.out.println("\nAll species with Locations: " + jungle.getAllMonkeysWithLocations());

    System.out.println("\nAll Monkeys: ");
    for(Primate monkey : jungle.getMonkeys())
    {
      System.out.print(monkey.getId() + " ");
    }
    jungle.removeMonkey(monkeys.get(0));
    System.out.println("\nAll Monkeys: ");
    for(Primate monkey : jungle.getMonkeys())
    {
      System.out.print(monkey.getId() + " ");
    }

    //fav food shopping list
    Map<FavoriteFood, Integer> favFoodshoppingList = jungle.getFavFoodShoppingList();
    System.out.println("\n Favorite food shopping List: ");
    System.out.println(favFoodshoppingList);

    //species with locations
    System.out.println("\n Species with location list: ");
    System.out.println(jungle.getSpeciesWithLocations());

    //enclosure sign
    System.out.println("\n Enclosure sign: ");
    System.out.println(jungle.getEnclosureSign("ENC1"));
    //enclosure sign for wrong enclosure id = ISO1
    try {
      System.out.println(jungle.getEnclosureSign("ISO1"));
    } catch (IllegalArgumentException e){
      System.out.println("\n" + e);
    }

    //update monkey's size
    try {
      jungle.updateMonkeySize(MonkeySize.LARGE, monkeys.get(0));
      System.out.println("Monkey size updated to " + MonkeySize.LARGE);
    } catch (IllegalArgumentException e){
      System.out.println("\n" + e);
    }

  }
}
