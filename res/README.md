# Jungle Friends Primate Sanctuary System

## About/Overview

The Jungle Friends Primate Sanctuary provides a permanent home and high-quality sanctuary care for
New World primates who have been cast-off from the pet trade, retired from research, or confiscated
by authorities. They are seeking to replace all of their paper records with computer records where
they can keep track of the individual animals that are in their care.

This system lets the user create a sanctuary system with initial enclosures and isolations and also
gives them the ability to expand their sanctuary based on their need.

The user can add primates, update their information, move them around the sanctuary and also remove
them from sanctuary(alumni monkeys)
in case they die or are moved to another sanctuary.

## List of features

1. Create a Sanctuary with initial number of Isolations and Enclosures. Each isolation and enclosure
   have unique ids. The user can create enclosures of any capacity you want.
2. The user can extend the sanctuary on need basis(increase number of isolations and enclosures).
3. The user can add a primate/monkey to the Sanctuary by providing its basic information and a valid
   isolation id. The monkey will be added to the given isolation id.
4. The user can also add the monkey without providing initial isolation id and in that case the new
   monkey is added to any available isolation cage.
5. The user can move a monkey to and from Isolations and Enclosures in two ways -
    1. By providing a location(isolation/enclosure) id. In this case, the monkey is added to the
       provided location.
    2. Without providing a location id. In this case, the monkey will be moved to any available
       Isolation or Enclosure (The user can choose if the monkey needs to be moved to Isolation or
       Enclosure).
6. The user can update a monkey's information like age, weight, size, health status and favorite
   food.
7. The system lets the user know if a monkey cannot be added to the isolation or enclosure based on
   the capacity.
8. An unhealthy monkey cannot be added to an Enclosure as it risks the lives of other monkeys.
9. If a healthy monkey's health status who is residing in an enclosure is updated to unhealthy then
   the monkey is automatically moved to an available isolation cage. The system lets the user know
   if no isolation cages are available but the monkey is still removed from the enclosure.
10. If a monkey's size is changed and the if the monkey is residing in an enclosure and if the
    enclosure cannot house the monkey anymore due to its updated size then the monkey will be moved
    to another available enclosure. If none enclosures are available then the user will be reported.
11. The user can remove the monkey from the sanctuary in case it dies or is moved someplace else.
    The user can get a list of alumni monkeys.
12. The user can also get a list of all housings and residents.
13. The user has the ability to expand the sanctuary.
14. The system has the ability to report the species that are currently being housed in alphabetical
    order including both isolations and enclosures.
15. The system has the ability to look up where a particular species is currently housed. It also
    reports if none of a particular species is currently being housed.
16. The system has the ability to create a sign for a given enclosure that lists each individual
    monkey that is currently housed there. For each individual monkey, the sign should include their
    name, sex, and favorite food.
17. The system has the ability to create an alphabetical list (by name) of all the monkeys housed in
    the Sanctuary with locations.
18. The system has the ability to produce a shopping list of the favorite foods of the inhabitants
    of the Sanctuary.

## How to Run

java -jar ./res/Primates.jar or <your-directory>

## Description of Examples

##### Primates_Sample_run_1.txt

1. Creates a sanctuary with 8 isolations and 5 enclosures.
2. Tries to add a monkey to sanctuary with location id that does not exist but fails.
3. Tries to add a monkey to sanctuary with enclosure id but fails.
4. Adds a few monkey successfully.
5. Tries to add a monkey with an isolation id that is already occupied but fails.
6. Adds another monkey to sanctuary.
7. Now all isolations are full, and it tries to add another monkey but fails.
8. Moves monkeys to enclosures.
9. Prints a list of all monkeys housed in the sanctuary.
10. Prints an enclosure sign.
11. Tries to get an enclosure sign for invalid enclosure but fails.
12. Prints list of all species with locations.
13. Print list of all locations a particular species is located.
14. Prints shopping list for all favorite food.

##### Primates_Sample_run_2.txt

1. Creates a sanctuary with 5 isolations and 2 enclosures.
2. Adds a few monkey successfully.
3. Moves monkeys to enclosures.
4. Tries to move another monkey to enclosure but there isn't any more space to house this monkey.
5. Adds another monkey to sanctuary(Caddy) who is unhealthy.
6. Tries to move Caddy to enclosure but fails.
7. Updates Caddy's health status to Healthy.
8. Moves Caddy to enclosure successfully.

## Design/Model Changes

1. In version 1, I had an abstract class that was implemented by both isolation and Enclosure, but
   later I realized I had no need for it as all the methods had different implementation.
2. In version 1, my Monkey, Isolation and Enclosure classes had public constructor but in version 2,
   I made them package private, so they can be used inside the package by the model but the users
   cannot construct their own objects. The only way of constructing these objects now is through the
   Sanctuary. This is done to control the creation of objects and provide only a single way through
   sanctuary with proper validation.
3. In version 1, Housing interface had public methods to add and remove a monkey but in version 2, I
   made Enclosure and Isolation have package private methods instead so that the user cannot
   directly add or remove a monkey to Enclosure or Isolation but again the user will have to go
   through the sanctuary to do such operations in order to maintain proper state.
4. In version 1, the Monkey class had public setters for age, weight, size, health status but in
   version 2, I changed these methods to package private so that user cannot access and mutate the
   state of a monkey directly but in order to achieve any changes for a particular monkey, the user
   will have to go through Sanctuary and proper validation and therefore the state remains
   consistent.
5. In version 2, I have also added a functionality to remove a monkey in case it dies or moved to
   some place else.
6. In version 2, I researched more about New World monkeys and have added the all the new world
   monkey high level species classification to the species enum. The species DRILL, MANGABEY and
   GUEREZA are removed as they are Old World monkeys.

## Assumptions

1. The Species enum is created on the basis on current new world monkey high level classification. (
   Link added in citation).
2. Once a monkey is created, it's created with the right data and fields like age, size can only be
   increased(As they increase with time) but not decrease. Name and species cannot be modified.

## Limitations

The project covers all the functionalities given in the project description.

## Citations

1. Classification of new world monkeys
    - https://www.newworldencyclopedia.org/entry/New_World_monkey#Classification
2. Old World monkeys -
3. Old world
    1. Drill - https://en.wikipedia.org/wiki/Drill_(animal)
    2. Guereza - https://en.wikipedia.org/wiki/Mantled_guereza
    3. Mangabey - https://www.newworldencyclopedia.org/entry/Mangabey






