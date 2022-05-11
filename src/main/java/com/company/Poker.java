// Name: Poker.java
// Description: Play against an AI in a game of poker.
// Initials:			Date:			Description:
// DVM                  12/20/21        Just started the program and made some constructors
// DVM                  12/21/21        Dealt two cards to each player successfully.
// DVM                  12/22/21        Added an ArrayList in order to keep track of used cards.
//                                      Added questions that asked the user for game rules.
// DVM                  12/22/21        Added a while loop for when the game is running.
//                                      Completed the way blinds are done. (IF SOMEONE DOES NOT HAVE ENOUGH MONEY
//                                      TO PUT IN BLINDS, THEN I ACT ACCORDINGLY) REMEMBER!!!
// DVM                  12/23/21        Started the game where the player decides their first turn.
//                                      Added addMoney variable in order to do raises properly
//                                      Figure out how to use the boolean from the choices method in order to run each round
// DVM                  12/31/21        Started setting up the format for how the game is going to run, with cards being
//                                      revealed at appropriate times
// DVM                  12/31/21        Made a loop to run each round. Each round can now be completed
//                                      Game can declare a winner now; however, if there is a tie for any reason,
//                                      then I need to be able to judge accordingly.
//                                      I also need to make a loop to run multiple games.
//                                      Added time-spacing for when the AI has to make a decision for suspense
//                                      If the winner has a high card, then there might be a bug. CHECK!
// DVM                  1/2/22          Ties should be handled now. Fixed high card bug above.
//                                      NOTE!!!: Lots of testing needs to be done for ties to make sure it is working
// DVM                  1/6/22          Debugging. Check notepad for list of bugs that need to be fixed
//                                      Added multiple rounds so you can play for as long as you would like
//                                      Changing the way rounds are done so a turn order is present which will be more
//                                      friendly for when I add more players.
// DVM                  1/7/22          Fixed multiple bugs. Check notepad for which ones.
//                                      Found other bugs in the process
//                                      Started implementing multiple players. Done everything except for determining
//                                      the winner when there are more than two players. After that, everything needs
//                                      to be tested. It's going to be buggy as hell
//                                      See in the future if there is any way to condense the switch statements.
// DVM                  1/8/22          Started briefly working on how to resolve the winner with multiple people
// DVM                  1/14/22         Changed how the questions are formatted so we can get the number of players
//                                      earlier which will then help us set up the game. Change formatting later so it
//                                      is easier to read.
//                                      Started implementing new ArrayList system to record hands. Right now the game is
//                                      not working as I am not done with that. Just change every method where a
//                                      constructor was used.
//                                      FOR THE FUTURE! Change the way the cards are dealt by keeping track of the
//                                      number of cards that are dealt instead of what cards are dealt. Then, just go
//                                      through the deck. It will make things easier to read.
// DVM                  1/15/22         Continued from yesterday. Stopped at the point where we used the choices method.
//                                      Current usage of the choices method is currently commented out.
// DVM                  1/29/22         Continued from last time. I need to redo some of it
// DVM                  2/7/22          Rounds are in motion. They break once someone folds. playerCounter goes up twice
//                                      when someone folds, and I'm not sure what is causing that. Sometimes certain
//                                      player turns get skipped after someone fold (in addition to the folded player).
//                                      Consult notepad for new bugs found. Fix above before working on declaring the
//                                      winner
// DVM                  2/14/22         Fixed bug where folding would skip players. The first round should be working
//                                      Started determining the winner, but haven't done anything to display it yet.
//                                      I also need to shift money.
// DVM                  2/22/22         The winner should be displayed; however, it has not been tested once yet
// DVM                  2/23/22         Fixed bugs. Check notepad to see new bugs. Above still needs some testing
// DVM                  2/23/22         Now money is added or subtracted after each round.
//                                      Everyone's cash amount is now displayed
// DVM                  3/19/22         Fixed some bugs. Now the game runs somewhat smoothly.
// DVM                  3/20/22         Fixing more bugs. Cleared out old comments and debris
// DVM                  3/21/22         Debugging. AI can still bet $0. If everyone folds, money is not distributed
//                                      properly. Eventually, I want to implement a way to request the name of the
//                                      winning hand. Additionally, find a way to make sure AI behavior is not impacted
//                                      by pairs that are given to everyone (from house). If someone bets a high amount
//                                      AI behavior should be altered as well
// DVM                  3/22/22         When an AI runs out of money, I started a process that removes them entirely

package com.company;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Poker {

    // This will store the money that is added from raises
    public static int addMoney = 0;

    Poker() {

    }

    public static void main(String[] args) throws Exception {

        final int NUM_HOUSE_CARDS = 5, // The five cards that will open up as the game progresses
                NUMBER_OF_QUESTIONS = 3, // When asking for game setting information. Increase number to add a question
                DEFAULT_MONEY = 5000, // Default setting
                DEFAULT_STAKES = 100, // Default setting
                DEFAULT_PLAYERS = 6, // Default setting
                MAX_PLAYERS = 8, // Most amount of players
                NUM_ROUNDS = 4; // Number of times cards flip per round

        Scanner input = new Scanner(System.in);

        // This stores cards that need to be removed
        ArrayList<Integer> removable = new ArrayList<>();

        // The profiles of each player including the house
        PokerTest house = new PokerTest(0, 0, 0);
        PokerTest player = new PokerTest();

        // Introduction
        System.out.print("This is a game of poker\n");

        int money = -1;

        // Number of players
        int numPlayers = 0;

        // This is a loop for when it comes to input because the process is very similar.
        for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {

            // Resets the variable after the first iteration
            money = -1;

            // Asks for number of players
            if (i == 0) {
                System.out.print("\nHow many players should there be: ");
            }

            // Asks for the amount of money to play
            if (i == 1) {
                System.out.print("\nHow much money would you like to put in: ");
            }

            // This asks what the stakes are
            if (i == 2) {
                System.out.print("\nWhat should the stakes be: ");
            }

            do {
                try {

                    // This gets rid of any decimals
                    money = (int) input.nextFloat();
                } catch (InputMismatchException e) {

                    // The below code runs if the user does not enter an integer
                    System.out.print("Input was not an integer. ");
                    if (i == 0) {

                        // The default number of players
                        money = DEFAULT_PLAYERS;

                        // Displays the default number of players
                        System.out.printf("Setting number of players to %d.%n",
                                DEFAULT_PLAYERS);

                    } else if (i == 2) {

                        // This code runs if the question was asking for stakes
                        money = DEFAULT_STAKES;

                        // If the default stakes are too high, then this will automatically reconfigure it
                        while (money >= (player.getMoney() / 2)) money--;

                        // Displays the default stakes
                        System.out.printf("Setting stakes to $%,d.%n", money);
                    } else {

                        // This code runs if the question was asking for money
                        System.out.printf("Setting money to $%,d.%n", DEFAULT_MONEY);
                        money = DEFAULT_MONEY;

                    }
                }

                if (money <= 0) {

                    // If the user enters a negative number or 0, then they have to try again to exit the loop
                    System.out.print("\nPlease try again: ");
                } else if (i == 2 && money >= (player.getMoney(0) / 2)) {

                    // If the user inputs a value that is considered to be too high, then they have to try again.
                    System.out.print("\nThe stakes are too high for the amount of money you plan to use.\nPlease try again: ");
                    money = -1; // Reset
                } else if (i == 0 && money < 2) {

                    // If the user wants to play by them self
                    System.out.print("\nThere must be at least two players.\nPlease try again: ");
                    money = -1; // Reset
                } else if (i == 0 && money > 8) {

                    // In case the user wants to play against too many people
                    System.out.printf("%nThe most amount of players we can accommodate is %d players. Please try again: ",
                            MAX_PLAYERS);
                    money = -1; // Reset
                }
            } while (money <= 0);

            if (i == 0) {

                numPlayers = money;
            } else if (i == 2) {

                // Sets the stakes up
                house.setStakes(money);
            } else if (i == 1) {

                // Gives each player the declared amount of money
                for (int j = 0; j < numPlayers; j++) {
                    player.setMoney(j, money);
                }

            }
        }

        boolean game = true; // Determines if the game is running or not
        int round = 1; // Number of rounds that past

        // Game start message
        System.out.print("\nThe game starts. ");

        while (game) {

            // Shuffles the deck
            player.shuffle();

            // Creates each hand for each player
            for (int i = 0; i < numPlayers; i++) {
                player.clearHand(i);
                for (int j = 0; j < 2; j++) {
                    player.update(removable);
                    removable.add(player.deal(i));
                }
            }

            // Resets the pot
            for (int i = 0; i < numPlayers; i++) {
                player.setBet(i, 0);
            }

            // Creates the five cards that the house flips over
            house.shuffle();
            house.update(removable);
            for (int i = 0; i < NUM_HOUSE_CARDS; i++) {
                house.deal();
            }

            // Beginning of round message
            System.out.printf("Round %d:%n", round);

            // Determines who places the small blind first
            String whoSmall = null, whoBig = null; // Name of players who are going to placing blinds
            int blind = (round % numPlayers),
                    turnOrder = 0;  // Determines the turn order

            for (int i = 0; i < numPlayers; i++) {
                if (blind == i) {

                    // Determines who plays the small blind
                    whoSmall = player.getName(i);
                    player.addBet(i, house.getStakes() / 2);

                    // This identifies who does the big blind.
                    try {
                        whoBig = player.getName(i + 1);
                        player.addBet(i + 1, house.getStakes());
                    } catch (IndexOutOfBoundsException e) {

                        // If the small blind goes to the last player, then it rotates
                        whoBig = player.getName(0);
                        player.addBet(0, house.getStakes());
                    }

                    // This identifies the next person to go
                    turnOrder = i + 2;

                    // The below recognizes if the players need to be rotated.
                    int counter = -1;
                    while ((turnOrder + 1) > numPlayers) {
                        turnOrder--;
                        counter++;
                    }
                    if (counter != -1) turnOrder = counter;

                    // For when there are two players, the turnOrder variable needs to be moved again
                    if ((turnOrder + 1) > numPlayers) {
                        turnOrder--;
                    }

                }
            }

            // In case there is an error and neither of the required variables were assigned properly
            if (whoSmall == null || whoBig == null) {
                throw new Exception("Blinds couldn't be processed: " + whoSmall + " " + whoBig);
            }

            // Amount of money that everyone needs to put in
            house.addBet(house.getStakes());

            // This displays your cards
            System.out.printf("%nYour cards are:%n%s and %s%n",
                    player.getCardName(player.getCard(0, 0)),
                    player.getCardName(player.getCard(0, 1)));

            // This displays the message for the blinds
            System.out.printf("%n%s place(s) a small blind of $%,d.%n%s place(s) a big blind of $%,d.",
                    whoSmall,
                    house.getStakes() / 2,
                    whoBig,
                    house.getStakes());

            int knownCards = 0; // The amount of cards to flip over
            boolean keepPlaying = false; // Records the fact that money was added

            // Determines who folded
            ArrayList<Boolean> fold = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                fold.add(false);
            }
            int numFolded = 0; // Number of people who folded
            boolean folded = false; // This records if the round ended because everyone folded
            int pot = 0; // Captures the pot

            // Loop for each round
            for (int i = 0; i < NUM_ROUNDS; i++) {

                // If everyone but one folds, the round is over
                if (numFolded == (numPlayers - 1)) {
                    folded = true;
                    break;
                } else {

                    // Reveals the cards that the house give
                    switch (i) {
                        case 0 -> {
                        }
                        case 1 -> {
                            knownCards += 3;
                            System.out.printf("%n%nThe house reveals three cards:%n%s, %s, and %s",
                                    house.getCardName(house.getCard(0)),
                                    house.getCardName(house.getCard(1)),
                                    house.getCardName(house.getCard(2)));
                        }
                        case 2 -> {
                            knownCards += 1;
                            System.out.printf("%n%nThe house reveals another card:%n%s",
                                    house.getCardName(house.getCard(3)));
                        }
                        case 3 -> {
                            knownCards += 1;
                            System.out.printf("%n%nThe house reveals another card: %n%s",
                                    house.getCardName(house.getCard(4)));

                        }
                        default -> throw new Exception("Error: Game cannot continue this far: " + i);

                    }

                    // States the pot
                    pot = 0;
                    for (int j = 0; j < numPlayers; j++) {
                        pot += player.getBet(j);
                    }
                    System.out.printf("%nThe pot is $%,d",
                            pot);

                    // Goes through this round
                    int playerCounter = 0;
                    do {
                        keepPlaying = true; // Reset
                        // This makes sure that everyone goes

                        // Choices
                        for (int j = 0; j < numPlayers; j++) {
                            if (j == turnOrder) {

                                // Checks if person folded
                                if (!fold.get(turnOrder)) {

                                    // Thinking time
                                    TimeUnit.SECONDS.sleep(1);

                                    // Player/AI turn
                                    keepPlaying = player.choices(turnOrder, house.hand, house.getBet(), knownCards, house.getStakes());

                                }
                                break;
                            }
                        }

                        // If somebody folds
                        if (!keepPlaying) {
                            fold.set(turnOrder, true);
                            numFolded++;

                            // Once a certain number of people fold, the round will end
                            if (!(numFolded == (numPlayers - 1))) {
                                keepPlaying = true;
                            }
                        }

                        // If someone puts in money, the turn order will move
                        if (addMoney > 0) {
                            playerCounter = 1;
                        } else {
                            playerCounter++;
                        }

                        // Everyone went, so it is the next round
                        if (playerCounter == numPlayers) {
                            keepPlaying = false;
                        }

                        // Add money to pot if the player decides to do so
                        if (addMoney != -1) {
                            house.addBet(addMoney);
                            addMoney -= addMoney;
                        }

                        // Moves the turn order accordingly
                        if ((turnOrder + 1) == numPlayers) {
                            turnOrder = 0;
                        } else turnOrder++;

                    } while (keepPlaying);

                }
            }

            // Restates the pot (Important for when everyone folds)
            pot = 0;
            for (int j = 0; j < numPlayers; j++) {
                pot += player.getBet(j);
            }

            String winner = ""; // For declaring the winner
            int winnerNumber = -1, tieNumber = 0, highestValue = 0; // For declaring the winner

            // Placeholder for finding the best hand
            int placeholder = 0, tiedPlayer = -1;
            boolean tie = false;

            // For finding the winner
            ArrayList<Integer> handValues = new ArrayList<>();
            if (folded) {
                for (int i = 0; i < fold.size(); i++) {
                    if (!fold.get(i)) {
                        winnerNumber = i; // Gets the index for the winner IS THIS NEEDED?
                        winner = player.getName(i);
                    }
                }

                // For displaying the winner
                if (winner == "" || winnerNumber == -1) {
                    throw new Exception("Winner not found: " + winner + ":" + winnerNumber);
                } else {
                    System.out.printf("%nEveryone but one player folded%n%s wins the $%d.",
                            winner,
                            pot);

                    // Gives winner pot and makes everyone lose money
                    player.changeMoney(winnerNumber, pot);

                }

            } else {
                for (int i = 0; i < numPlayers; i++) {

                    // Collects all hand values
                    if (!fold.get(i)) {
                        handValues.add(player.value(player.getHand(i), house.hand, knownCards, true, 0));
                    } else handValues.add(0);

                }

                // Goes through each hand in play to determine winner
                for (int i = 0; i < numPlayers; i++) {
                    if (handValues.get(i) > placeholder) {
                        winnerNumber = i; // Gets index for the greatest hand
                        winner = player.getName(i);
                        tie = false; // States that there is no tie
                        tiedPlayer = -1;
                        placeholder = handValues.get(i);
                    } else if (handValues.get(i) != 0 && handValues.get(i) == placeholder) {
                        tie = true;
                        tiedPlayer = i;
                    }
                }

            }

            // If there is a tie, then we need to break it
            int tiedValueOne = 0, tiedValueTwo = 0; // Gets the updated values for the tied people
            if (tie) {
                for (int i = 0; i < NUM_HOUSE_CARDS; i++) {
                    tiedValueOne = player.value(player.getHand(winnerNumber), house.hand, knownCards, true, i);
                    tiedValueTwo = player.value(player.getHand(tiedPlayer), house.hand, knownCards, true, i);

                    // Checks for ties
                    if (tiedValueOne != tiedValueTwo) {

                        // There is no tie
                        if (tiedValueTwo > tiedValueOne) winnerNumber = tiedPlayer;
                        break;

                        // There is a tie if this iterates five times
                    } else if (i == (NUM_HOUSE_CARDS - 1)) tie = true;
                }
            }

            if (!folded) {
                if (winnerNumber < 0) {
                    throw new Exception("Winner not found: " + winnerNumber);
                } else {

                    // Displays the cards of everyone who didn't fold
                    for (int i = 0; i < numPlayers; i++) {
                        if (!fold.get(i)) {
                            System.out.printf("%n%s had %s and %s.",
                                    player.getName(i),
                                    player.getCardName(player.getCard(i, 0)),
                                    player.getCardName(player.getCard(i, 1)));
                        }
                    }

                    // Displays the five cards
                    // Introduce loop here?
                    System.out.printf("%nThe five cards were %s, %s, %s, %s, and %s.",
                            house.getCardName(house.getCard(0)),
                            house.getCardName(house.getCard(1)),
                            house.getCardName(house.getCard(2)),
                            house.getCardName(house.getCard(3)),
                            house.getCardName(house.getCard(4)));

                    // Displays the winner
                    // Change the display so it names the type of winning hand it is
                    System.out.printf("%n%s wins the $%d",
                            winner,
                            pot);

                    // Gives winner pot and makes everyone lose money
                    player.changeMoney(winnerNumber, pot);

                }
            }

            // Lists what money everyone has
            for (int i = 0; i < numPlayers; i++) {
                System.out.printf("%n%s: $%d",
                        player.getName(i),
                        player.getMoney(i));
            }

            // Round counter
            round++;

            // Resets pot
            pot = 0;
            house.setBet(0);

            // Identifies players that have no money so the program can act accordingly without altering the above code
            // THIS NEEDS MORE TESTING!!!
            int currentPlayers = numPlayers;
            for (int i = 0; i < currentPlayers; i++) {
                if (player.getMoney(i) == 0 && player.getMoney(0) != 0) {
                    numPlayers--; // Reduces the number of players

                    // Moves everyone down if someone in the middle is eliminated.
                    for (int j = 0; j < currentPlayers; j++) {
                        if ((i + j + 1) != currentPlayers) {
                            player.setMoney(i + j, player.getMoney(i + j + 1));
                            player.setName(i + j, player.getName(i + j + 1));
                        }
                    }

                    // Redoes loop
                    i--;
                }
            }

            // Ends game if player has no more money
            if (player.getMoney(0) == 0) {
                System.out.print("\nYou have no more money. GAME OVER!!!");
                game = false;

                // If all the AI are eliminated
            } else if (numPlayers == 1) {
                System.out.print("\nThere are no more players left! YOU HAVE WON!!!");
                game = false;
            } else {

                // Asks the player if they want to continue
                String response = "";
                System.out.printf("%nWould you like to continue? (Y/N) ");
                do {
                    response = input.nextLine();
                    if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
                        continue; // Continues the game
                    } else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
                        game = false; // Ends the game
                    } else {
                        response = "";
                        System.out.printf("%nPlease try again: "); // If the user did not put in the right input
                    }
                } while (response == "");

            }
        }
    }
}
