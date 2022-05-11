// Name: PokerTest.java
// Description: Assist Poker.java with basic functions
// Initials:			Date:			Description:
// DVM                  12/20/21        Made shuffle function and ArrayList deck
// DVM                  12/21/21        Made methods to deal cards to each player and methods to call
//                                      the names and value of the cards given.
// DVM                  12/22/21        Changed deal method to keep track of cards that need to be removed
//                                      Added update method to remove used cards for other profiles
//                                      Added methods to keep track of money and stakes
// DVM                  12/23/21        Started choices function that will determine every single turn. Blinds should run smoothly
// DVM                  12/24/21        Started working on the value method to determine the value of certain hands
//                                      Only found pairs, but still need to work on determining which pairs are better
//                                      Built maxValue and minValue methods
// DVM                  12/24/21        Finished the part of the value method that determines pairs, full houses, and
//                                      other hands that have to do with like values.
//                                      Made a section to determine flushes
//                                      TO DO: Straights, straight flushes, high card.
//                                      TO DO: The value method will be used when the round is not done yet, which means
//                                      that I have to make sure that I can use the result of the method in order to help
//                                      the AI determine the best move to make.
// DVM                  12/25/21        Added a way to find straights. Straight flushes needed to be found
//                                      Did a lot of debugging for making sure that the value method was properly
//                                      identifying points of interest
// DVM                  12/26/21        Simplified code. Detected straight flushes and royal flushes
//                                      TO DO: Start figuring out a point system that determines how likely
//                                      an AI is to do something. That and how much money the bot needs to put in
//                                      together should determine how the AI acts.
// DVM                  12/27/21        Started working on how to determine the value of a certain given hand
// DVM                  12/28/21        Determined the value of hands. I just need to figure out how to implement it
//                                      so it determines how the AI behaves.
// DVM                  12/31/21        Started the decision-making process for the AI. I need to work on raises and
//                                      decision-making for when all the cards are revealed.
// DVM                  12/31/21        Finished being able to process how the AI behaves when all cards are revealed.
//                                      Added a way to record what final type (string) of hand the player or AI has at
//                                      the end of the game.
//                                      Added addTime() method
//                                      I still need to add an option to raise the bet for the AI
// DVM                  1/2/22          Added on the value method in order to help determine ties.
//                                      Added argument (highCardValue) to value method in order to do the above
//                                      NOTE!!!: Lots of testing needs to be done for ties to make sure it is working
// DVM                  1/6/22          Changed method name setBet to addBet to avoid confusion
// DVM                  1/7/22          Added variable name to PokerTest constructor so multiple players can be named
// DVM                  1/11/22         Added lombok
// DVM                  1/13/22         Added methods that I will use to replace current methods in order to
//                                      change how hands and money are recorded.
// DVM                  1/14/22         More of the above.
// DVM                  1/15/22         Changed choices method in order to use new getter and setter methods that
//                                      incorporate the new ArrayList
//                                      Remove last few methods if they are useless?
// DVM                  2/23/22         Added changeMoney method
// DVM                  3/19/22         Started to make sure that people have enough money to do certain actions.
//                                      Look at AI actions to make sure they are not doing the above too
// DVM                  3/20/22         Debugging. Added more measures to make sure that the player of anyone has
//                                      enough money. Currently, people can spend more more money than they have
//                                      if they raise. FIX!!!
// DVM                  3/22/22         Changed way names are set and recieved

package com.company;

import lombok.Data;
import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.concurrent.TimeUnit;

@Data
public class PokerTest {

    // ArrayList<Integer> nullList = new ArrayList<>();

    // Constructors for basic functions
    private final Random random = new Random();
    private final Scanner input = new Scanner(System.in);

    // Variables
    private final int NUM_CARDS = 52, // Number of cards in a deck
        NUM_RANDOM = 500,   // Number of times to shuffle the deck
        NUM_SUIT = 13, // Number of cards in a suit
        SUIT = 4,   // Number of suits
        HIGH_CARD = 14, // Value of Ace
        NUM_HAND = 2, // Number of cards in a player hand
        NUM_HOUSE = 5, // Number of cards on the table
        NUM_FLUSH = 5, // Number of cards for a flush
        RAND = 100; // Determines the likelihood that the AI will do something. This number can change for balance
    private final String SUITS[] = {"Hearts", "Spades", "Diamonds", "Clubs"}; // Name of all suits
    private final String VALUES[] = {"King", "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen"};
    private ArrayList<Integer> deck = new ArrayList<>(); // Deck of cards
    public ArrayList<Integer> hand = new ArrayList<>(); // The hands of each player
    public String name[] = {"Player", "AI One", "AI Two", "AI Three", "AI Four", "AI Five", "AI Six", "AI Seven"};
    private ArrayList<ArrayList<Integer>> hands = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> playerMoney = new ArrayList<>();
    private int money, // Amount of money each player has
        bet, // The amount of money that each player has put in
        stakes; // Determines blinds
    public String valueName; // Name of the best value of a hand

    public PokerTest() {

    }

    // NEEDED???
    public PokerTest(int money, int bet) {

    }

    public PokerTest(int money, int stakes, int bet) {
        this.money = money;
        this.stakes = stakes;
        this.bet = bet;
    }

    public String getName(int player) {
        return name[player];
    }

    public void setName(int player, String name) {
        this.name[player] = name;
    }

    public void setMoney(int player, int money) {

        // Variable that will be used to set the money
        ArrayList<Integer> amount = new ArrayList<>();

        // Multiple try and catch statements in order to set the money.
        // This is so we can normally set the money or add on to the variable if
        // there isn't already space
        try {
            amount = playerMoney.get(player); // Assigns the previous value if there is one
        } catch (IndexOutOfBoundsException e) {}

        // Sets the money
        try {
            amount.set(0, money);
        } catch (IndexOutOfBoundsException e) {
            amount.add(money);
        }
        try {
            playerMoney.set(player, amount);
        } catch (IndexOutOfBoundsException e) {
            playerMoney.add(amount);
        }
    }

    public int getMoney(int player) {
        ArrayList<Integer> placeholder = playerMoney.get(player);
        return placeholder.get(0);
    }

    public void setBet(int player, int bet) {
        ArrayList<Integer> placeholder = playerMoney.get(player);
        try {
            placeholder.set(1, 0);
        } catch (IndexOutOfBoundsException e) {
            placeholder.add(0);
        }
        playerMoney.set(player, placeholder);
    }

    public int getBet(int player) {
        ArrayList<Integer> placeholder = playerMoney.get(player);
        return placeholder.get(1);
    }

    public void addBet(int bet) {
        this.bet += bet;
    }

    public void addBet(int player, int bet) {
        ArrayList<Integer> placeholder = playerMoney.get(player);
        int previousBet;
        try {
            previousBet = placeholder.get(1);
        } catch (IndexOutOfBoundsException e) {
            placeholder.add(0);
            previousBet = 0;
        }
        placeholder.set(1, previousBet + bet);
    }

    public int getCard(int index) {
        return hand.get(index);
    }

    public int getCard(int player, int index) {
        ArrayList<Integer> hand = hands.get(player);
        return hand.get(index);
    }

    public String getCardName(int card) throws Exception {
        return (getValue(card) + " of " + getSuit(card));
    }

    // Gets the string of the value of the card
    public String getValue(int card) {

        // Gets the number
        int value = (card % NUM_SUIT);

        // Returns the value of the card as a string
        return VALUES[value];
    }

    // Gets the name of the suit
    public String getSuit(int card) throws Exception {
        int suit = NUM_SUIT;

        // Every 13 cards equal another suit, this loops to determine with suit it is and returns the name
        for (int i = 0; i < SUIT; i++) {
            if (card <= suit) {
                return SUITS[i];
            } else suit += NUM_SUIT;
        }

        // If this is returned, then there is a problem
        throw new Exception("Null Suit Caught");
    }

    public void shuffle() {

        // Makes a new deck
        deck.clear();
        for (int i = 0; i < NUM_CARDS; i++) deck.add(i, (i + 1));

        // Switches two cards a bunch of times
        for (int j = 0; j < NUM_RANDOM; j++) {
            int cardOne = random.nextInt(NUM_CARDS);
            int cardTwo = random.nextInt(NUM_CARDS);
            int placeholder = deck.get(cardOne);
            deck.set(cardOne, deck.get(cardTwo));
            deck.set(cardTwo, placeholder);
        }

    }

    public void clearHand() {
        hand.clear();
    }
    public void clearHand(int player) {
        ArrayList<Integer> hand = new ArrayList<>();
        try {
            hands.set(player, hand);
        } catch (IndexOutOfBoundsException e) {} // If the hand is already cleared
    }

    public ArrayList<Integer> getHand(int player) {
        return hands.get(player);
    }

    // This will deal a hand to each player
    public int deal() {

        hand.add(deck.get(hand.size()));

        return hand.get(hand.size() - 1);

    }
    public int deal(int player) {

        // Gets current hand
        ArrayList<Integer> hand = new ArrayList<>();
        try {
            hand = hands.get(player);
        } catch (IndexOutOfBoundsException e) {}

        // Deals a card
        hand.add(deck.get(hand.size()));

        // Sets the new hand
        try {
            hands.set(player, hand);
        } catch (IndexOutOfBoundsException e) {
            hands.add(hand);
        }

        // Returns value so it can't be used again
        return hand.get(hand.size() - 1);
    }

    public void update(ArrayList cards) {
        for (int i = 0; i < cards.size(); i++){
            deck.remove(Integer.valueOf((int) cards.get(i)));
        }
    }

    public boolean choices(int player, ArrayList<Integer> house, int houseBet, int knownCards, int stakes) throws Exception {

        // Two variables to store inputs
        String response;
        int raise = -1;

        // How much money the people need to add to pot
        int moneyNeeded = (houseBet - getBet(player));

        // If player money is limited
        if (moneyNeeded > getMoney(player)) {
            moneyNeeded = getMoney(player);
        }

        // houseBet is going down every time someone folds

        // TEST TEST TEST
        System.out.print("\nTest " + houseBet + " " + getBet(player) + "\n");

        // Lists through the choices for the player
        if ((getBet(player) - getMoney(player)) == 0) {
            System.out.printf("%s waits.",
                    getName(player));
        } else if ((getBet(player) - getMoney(player) == 0)) {
            throw new Exception("Someone has negative money. Player: " + player + " Money: " + getMoney());
        } else {
            if (player == 0) {
                if (getBet(player) != houseBet) {

                    // Offers choices
                    System.out.printf("%nWould you like to call $%,d, raise the bet, or fold: ",
                            moneyNeeded);

                    // This responds accordingly to what the user inputs
                    do {
                        response = input.nextLine();
                        if (response.toLowerCase().equals("call")) {

                            // The player calls and the money that the player offers is recorded
                            addBet(player, moneyNeeded);
                            System.out.printf("%nYou called $%,d.",
                                    moneyNeeded);
                            return true;
                        } else if (response.toLowerCase().equals("raise")) {

                            // Asks the player how much they want to raise
                            System.out.printf("%nHow much would you like to raise: ");
                            do {

                                // This makes sure that the input is valid
                                try {
                                    raise = (int) input.nextFloat();
                                } catch (InputMismatchException e) {
                                    System.out.printf("%nInput was not an integer.");
                                    raise = -1;
                                }

                                // If player put a negative number
                                if (raise < 0) {
                                    System.out.printf("%nPlease try again: ");

                                    // If player requests to put down more money than they have
                                } else if (raise > (getMoney(player) - houseBet)) {
                                    System.out.printf("%nYou do not have enough money to perform that action, please try again: ");
                                    raise = -1;

                                    // To cancel raise
                                } else if (raise == 0) {
                                    System.out.printf("%nCancelling raise.");
                                    choices(player, house, houseBet, knownCards, stakes);
                                    return true;
                                }
                                input.nextLine(); // Debugging
                            } while (raise < 0);

                            // If raise is not cancelled
                            if (raise != 0) {
                                // Records how much the bet was raised by
                                Poker.addMoney += raise;

                                // Records how much the player has put into the pot
                                addBet(player, (moneyNeeded + raise));
                                System.out.printf("%nYou raised the bet from $%,d to $%,d.",
                                        moneyNeeded,
                                        (moneyNeeded + raise));
                            }

                            return true;
                        } else if (response.equalsIgnoreCase("fold")) {

                            // Ends the players run for this round
                            System.out.printf("%nYou folded.");
                            Poker.addMoney = 0;
                            return false;
                        } else {

                            // If response is invalid
                            response = "";
                            System.out.printf("%nPlease try again: ");
                        }
                    } while (response == "");
                } else {

                    // The following code process if there is no bet to be called
                    System.out.printf("%nWould you like to bet, check, or fold: ");
                    do {
                        response = input.nextLine();

                        // Goes through each possible response
                        if (response.equalsIgnoreCase("bet")) {

                            // Asks the user how much they want to bet
                            System.out.printf("%nHow much would you like to put in: ");

                            // Checks that it is a valid number
                            do {
                                try {
                                    raise = (int) input.nextFloat();
                                } catch (InputMismatchException e) {
                                    System.out.printf("%nInput was not an integer.");
                                    raise = -1;
                                }
                                if (raise == 0) {
                                    System.out.printf("%nCancelling bet.");
                                    choices(player, house, houseBet, knownCards, stakes);
                                    return true;
                                } else if (raise < 0) {
                                    System.out.printf("%nPlease try again: ");
                                } else if (raise > (getMoney(player) - getBet(player))) {
                                    System.out.printf("%nYou do not have enough money to perform that action. Please try again: ");
                                    raise = -1;
                                }
                            } while (raise < 0);

                            // Records the bet
                            addBet(player, raise);
                            Poker.addMoney += raise;

                            return true;

                        } else if (response.equalsIgnoreCase("check")) {

                            System.out.printf("%nYou checked.");
                            return true;
                        } else if (response.equalsIgnoreCase("fold")) {

                            System.out.printf("%nYou folded.");
                            Poker.addMoney = 0;
                            return false;
                        } else {

                            // If the response was not any of the above
                            response = "";
                            System.out.printf("%nPlease try again: ");
                        }
                    } while (response == "");
                }
            } else {

                // Gets the total value of their cards when there are no cards visible
                int value = 0;
                if (knownCards == 0) {
                    for (int i = 0; i < NUM_HAND; i++) {
                        int num = getCard(player, i) % NUM_SUIT;
                        if (num == 0) {
                            value += 13;
                        } else value += num;
                    }

                    // If the AI does not have to put in money, it decides like this
                    if (houseBet == getBet(player)) {
                        System.out.printf("%n%s checks.",
                                getName(player));
                        return true;
                    } else {

                        // Calculates the chance to fold if the computer has bad cards and depending on the money needed to give
                        int chance = 0;
                        int moneyPlaceholder = 0;
                        while (moneyNeeded > moneyPlaceholder) {
                            chance += 5;
                            moneyPlaceholder += (getMoney(player) / 40);
                        }

                        // If the computer has low cards, then there is a chance that they fold unless they are the same suit or value
                        if (value < chance && (getSuit(getCard(player, 0)) != getSuit(getCard(player, 1))) &&
                                (getValue(getCard(player, 0)) != getValue(getCard(player, 1)))) {
                            int fold = (random.nextInt(100) + 1);

                            if (fold <= chance) {
                                System.out.printf("%n%s folds.",
                                        getName(player));
                                Poker.addMoney = 0;
                                return false;
                            } else {
                                addBet(player, moneyNeeded);
                                System.out.printf("%n%s calls $%,d.",
                                        getName(player),
                                        moneyNeeded);
                                return true;
                            }
                        } else {
                            System.out.printf("%n%s calls $%,d.",
                                    getName(player),
                                    moneyNeeded);
                            addBet(player, moneyNeeded);
                            return true;
                        }
                    }

                } else {

                    // The following determines the chance that the bot will do something other than checking or calling
                    int currentValue = value(getHand(player), house, knownCards, true, 1);
                    int outs = value(getHand(player), house, knownCards, false, 1);
                    int standard = (currentValue / 100) * outs;
                    int chance = random.nextInt(RAND);

                    // This changes how probability when all cards are known
                    if (knownCards == 5) {
                        standard = 0;
                    } else currentValue = 0;

                    // Player placed bet or raised bet
                    if (getBet(player) != houseBet) {
                        if (chance < standard || chance < (currentValue / 10)) {

                            // The AI will call here if both their cards and chance allows them.
                            System.out.printf("%n%s calls $%,d.",
                                    getName(player),
                                    moneyNeeded);
                            addBet(player, moneyNeeded);
                            return true;
                        } else if ((chance * 2) < standard) {

                            // RAISE
                            System.out.printf("%nTest.");

                        } else {

                            // The AI folds
                            System.out.printf("%n%s folds.",
                                    getName(player));
                            Poker.addMoney = 0; // Ends the round
                            return false;
                        }
                    } else {

                        // The AI will place a bet
                        if (chance < standard || chance < (currentValue / 10)) {
                            int maxBet = random.nextInt(10) + 1; // The max amount of money that the AI will put in
                            int moneyToAdd = 0; // The amount of money that the AI will put in

                            // This determines how much money to put in
                            for (int i = 0; i < maxBet; i++) {
                                if (chance < standard && (getBet(player) + stakes) <= getMoney(player)) {

                                    // Checks if AI has enough money
                                    if ((moneyToAdd + stakes) > getMoney(player)) {
                                        moneyToAdd = getMoney(player);
                                    } else {

                                        // If AI has plenty of money
                                        moneyToAdd += stakes;
                                        chance = random.nextInt(RAND);
                                    }
                                } else {
                                    break;
                                }
                            }

                            System.out.printf("%n%s bets $%,d",
                                    getName(player),
                                    moneyToAdd);
                            addBet(player, moneyToAdd);
                            Poker.addMoney += moneyToAdd;
                            return true;
                        } else {
                            System.out.printf("%n%s checks.",
                                    getName(player));
                            return true;
                        }
                    }


                }

            }
        }
        return true; // Default
    }

    // This will figure out how many points someone has.
    public int value(ArrayList<Integer> player, ArrayList<Integer> house, int knownCards, boolean getValue, int highCardValue) throws Exception {

        // Booleans for each type of possibility
        boolean flush = false, straight = false, straightFlush = false, royal = false, pair = false, twoPair = false, fullHouse = false, threeKind = false, fourKind = false;

        // Combines list of each hand
        ArrayList<Integer> cards = new ArrayList<>();
        for (int i = 0; i < player.size(); i++) {
            cards.add(player.get(i));
        }
        for (int i = 0; i < knownCards; i++) {
            cards.add(house.get(i));
        }

        // Collects only the values of the cards to better help determine if there is a straight or a pair
        ArrayList<Integer> cardValue = new ArrayList<>();
        for (Integer card : cards) {
            cardValue.add(correctValue(card % NUM_SUIT));
        }
        Collections.sort(cardValue);

        // Checks for a pair, two pair, three of a kind, full house, four of a kind
        int pairCounterOne = 1, pairCounterTwo = 1, pairCounterThree = 1; // Counts for pairs. Equals one for readability purposes later on
        int pairOneValue = -1, pairTwoValue = -1, pairThreeValue = -1; // Records the cards that are considered to be pairs
        int skip = 1; // This skips cards so that a three of a kind does not come up as a two pair
        for (int i = 0; i < cards.size(); i++) {

            for (int j = (i + skip); j < cards.size(); j++) {

                if (cardValue.get(i) == cardValue.get(j)) {

                    // Records the existence of a pair accordingly
                    if (!pair) {
                        pairCounterOne++;
                        pairOneValue = cardValue.get(j);
                    } else if (!twoPair) {
                        pairCounterTwo++;
                        pairTwoValue = cardValue.get(j);
                    } else {
                        pairCounterThree++;
                        pairThreeValue = cardValue.get(j);
                    }
                }
            }

            // Resets the variable in case there is a combination of a pair, three of a kind, and another pair
            if (skip > 1) skip--;

            // If a pair is found, then the loop will try to locate another pair
            if (pairCounterOne > 1) {
                pair = true;
                if (pairCounterOne == 3) skip++;
            }

            // Breaks the loop if a four of a kind is found
            if (pairCounterOne == 4 || pairCounterTwo == 4) {
                fourKind = true;
                break;
            }

            // If two pairs are found, we find a third pair.
            if (pairCounterTwo > 1) {
                twoPair = true;
                if (pairCounterTwo == 3) skip++;
            }

        }

        // This determines a three of a kind
        if (pairCounterOne == 3 && pairCounterTwo == 1) {
            threeKind = true;
        }

        // This determines a full house
        if (twoPair && pairCounterThree == 1) {
            if (pairCounterOne == 3 || pairCounterTwo == 3) {
                fullHouse = true;
            }
        }

        // This determines a flush
        int heartCounter = 0, spadeCounter = 0, diamondCounter = 0, clubCounter = 0; // Counts how many of each suit exist
        String suitFlushName = "";  // This records the name of the flush
        boolean closeToFlush = false;   // This records the fact that there was almost a flush
        for (Integer card : cards) {

            // This counts how much of each suit there is
            switch (getSuit(card)) {
                case "Hearts" -> {
                    heartCounter++;
                    if (heartCounter == (NUM_FLUSH - 1)) closeToFlush = true;
                    if (heartCounter == NUM_FLUSH) suitFlushName = SUITS[0];
                }
                case "Spades" -> {
                    spadeCounter++;
                    if (spadeCounter == (NUM_FLUSH - 1)) closeToFlush = true;
                    if (spadeCounter == NUM_FLUSH) suitFlushName = SUITS[1];
                }
                case "Diamonds" -> {
                    diamondCounter++;
                    if (diamondCounter == (NUM_FLUSH - 1)) closeToFlush = true;
                    if (diamondCounter == NUM_FLUSH) suitFlushName = SUITS[2];
                }
                case "Clubs" -> {
                    clubCounter++;
                    if (clubCounter == (NUM_FLUSH - 1)) closeToFlush = true;
                    if (clubCounter == NUM_FLUSH) suitFlushName = SUITS[3];
                }
                default -> throw new IllegalStateException("Unexpected value: " + getSuit(card));
            }

            // This determines if there is flush. If we name the below variable, then there is a flush.
            if (suitFlushName != "") {
                flush = true;
                break;
            }
        }

        // This determines if there is a straight
        int straightCounter = 1, straightHighCardIndex = 0;
        boolean closeToStraight = false; // This determines if we got close to a straight
        int shift = 1; // This makes sure that an out of bounds error does not happen when we are checking for straights
        if (getValue) shift = -1;
        for (int i = 0; i < (knownCards + shift); i++) {
            if (cardValue.get(i) == (cardValue.get(i + 1) - 1)) {

                // The ArrayList should be sorted, so if the next card is the next value, then a straight is forming
                straightCounter++;
            } else if (cardValue.get(i) == cardValue.get(i + 1)) {

                // This is so pairs don't get in the way of ruining potential straights
                continue;
            } else {

                // This runs if the cards in a row runs out.
                straightCounter = 1;
            }

            // This shows that we got close to a straight in case it stops here
            if (straightCounter == 4) {
                closeToStraight = true;
            }

            // This determines if there is a straight
            if (straightCounter == 5) {
                straight = true;
                straightHighCardIndex = (i + 1);
            }

        }

        // The below array carries all the cards of the same suit
        ArrayList<Integer> flushValues = new ArrayList<>();
        if (flush && straight) {

            // This gets all the cards that are in a flush if there is one
            for (Integer card : cards) {
                if (getSuit(card) == suitFlushName) {
                    flushValues.add(card);
                }
            }

            // Sorts them in order
            Collections.sort(flushValues);

            // This determines if there is a straight flush
            int straightFlushCounter = 1;
            for (int i = 0; i < (NUM_FLUSH - 1); i++) {
                if (cards.get(i) == cards.get(i + 1)) {
                    straightFlushCounter++;
                } else {
                    break;
                }
            }

            // If the loop goes on without breaking, then there is a straight flush
            if (straightFlushCounter == 5) {
                straightFlush = true;

                // If the high card is an ace, then it is also a royal flush
                if (cardValue.get(straightHighCardIndex) == HIGH_CARD) royal = true;
            }

        }

        int value = 0; // This variable will find the best value of the set of cards once all cards are revealed
        // Below determines the worth of someone's hand. Values are affected by high cards
        // This is used after all cards are revealed (or maybe during?)
        if (royal) {
            value = 1000;
        } else if (straightFlush) {
            value = (900 + (cardValue.get(straightHighCardIndex - highCardValue)));
        } else if (fourKind) {
            value = 800;
            if (highCardValue == 0) {
                if (twoPair) {
                    value += (pairTwoValue);
                } else value += pairOneValue;
            } else {

                // If the there is tie with the four of a kind
                for (int i = (cardValue.size() - 1); i >= 0; i--) {
                    if (twoPair) {
                        if (cardValue.get(i) != pairTwoValue) {
                            value += cardValue.get(i);
                            break;
                        }
                    } else {
                        if (cardValue.get(i) != pairOneValue) {
                            value += cardValue.get(i);
                            break;
                        }
                    }
                }
            }

        } else if (fullHouse) {
            value = 700;
            int higherPair; // For when there is a tie, this is needed

            if (pairCounterOne == 3) {
                if (highCardValue == 0) value += pairOneValue;
                higherPair = maxValue(pairTwoValue, pairThreeValue);
            } else if (pairCounterTwo == 3) {
                if (highCardValue == 0) value += pairCounterTwo;
                higherPair = maxValue(pairOneValue, pairThreeValue);
            } else {
                if (highCardValue == 0) value += pairCounterThree;
                higherPair = maxValue(pairOneValue, pairTwoValue);
            }

            // In case there is a tie and two people have the same three of a kind
            if (highCardValue > 0) {
                if (pairCounterThree > 1) {
                    value += higherPair;
                } else {
                    if (pairCounterOne == 3) {
                        value += pairTwoValue;
                    } else if (pairCounterTwo == 3) {
                        value += pairOneValue;
                    } else throw new Exception("Cannot read tiebreaker for full house");
                }
            }

        } else if (flush) {

            // Try to see if there is another way that looks cleaner
            // Without the try and catch statement, an error would occur if someone got a flush before all cards
            // were revealed
            try {
                value = (600 + (flushValues.get(NUM_FLUSH - 1 - highCardValue)));
            } catch (IndexOutOfBoundsException e) {
                value = 600;
            }
        } else if (straight) {
            value = (500 + cardValue.get(straightHighCardIndex - highCardValue));
        } else if (threeKind) {
            value = 400; // Base value
            if (highCardValue == 0) {
                value += pairOneValue;
            } else {

                // This will be the tiebreaker in case two people have the same three of a kind
                int counter = highCardValue;
                for (int i = (cardValue.size() - 1); i >= 0; i--) {
                    if (cardValue.get(i) != pairOneValue) {
                        if (counter == 1) {

                            // Gets high card
                            value += cardValue.get(i);
                            break;
                        } else {

                            // In case the tied people have the same high card by chance as well
                            counter--;
                        }
                    }
                }
            }
        } else if (twoPair) {
            int greaterPair = maxValue(pairOneValue, pairTwoValue); // The higher pair
            value = 300; // Base value
            if (highCardValue == 0) {
                value += greaterPair;
            } else if (highCardValue == 1) {

                // In case two people have the same highest pair
                if (pairOneValue == greaterPair) {
                    value += pairTwoValue;
                } else value += pairOneValue;
            } else {

                // In case two people have the same two pair. It gets the high card
                for (int i = (cardValue.size() - 1); i >= 0; i--) {
                    if (cardValue.get(i) != pairOneValue && cardValue.get(i) != pairTwoValue) {
                        value += cardValue.get(i);
                        break;
                    }
                }

            }
        } else if (pair) {
            value = 200; // Base value
            if (highCardValue == 0) {

                // The value of the pair
                value += pairOneValue;
            } else {

                // In case two people have the same pair
                int counter = highCardValue; // For ties
                for (int i = (cardValue.size() - 1); i >= 0; i--) {
                    if (cardValue.get(i) != pairOneValue) {
                        if (counter == 1) {
                            value += cardValue.get(i);
                            break;
                        } else {

                            // In case the tied people have the same high card(s)
                            counter--;
                        }
                    }
                }
            }
        } else {
            value = 100; // Base value
            int counter = highCardValue; // For ties
            for (int i = (cardValue.size() - 1); i >= 0; i--) {
                if (counter == 0) {

                    // Gets the high card value
                    value += cardValue.get(i);
                } else {

                    // In case two people have the same high card
                    counter--;
                }
            }
        }

        // This returns the value to the AI to help it think
        if (getValue) {
            if (value > 990) {
                valueName = "Royal Flush";
            } else if (value > 890) {
                valueName = "Straight FLush";
            } else if (value > 790) {
                valueName = "Four of a Kind";
            } else if (value > 690) {
                valueName = "Full House";
            } else if (value > 590) {
                valueName = "Flush";
            } else if (value > 490) {
                valueName = "Straight";
            } else if (value > 390) {
                valueName = "Three of a Kind";
            } else if (value > 290) {
                valueName = "Two Pair";
            } else if (value > 190) {
                valueName = "Pair";
            } else {
                valueName = "High Card (" + getValue(cardValue.get(cardValue.size() - 1)) + ")";
            }

            return value;
        }

        // The AI might think that points that in the five cards are of value to the AI, when it isn't. This should get
        // rid of that.
        ArrayList<Integer> blank = new ArrayList<>();
        int houseValue = value(blank, house, knownCards, true, 1);

        int outs = 0; // This variable will find the number of cards that will help the AI improve their hand
        if (knownCards != 5) {

            // The below if statements finds most cards that will help improve the bots hand
            if (!pair && houseValue < 200) outs += 6;
            if (pair && !twoPair && houseValue < 300) {

                // If you have a pair in your two cards, then a two pair will not be an improvement
                if (!(cards.get(0) == cards.get(1))) outs += 3;
            }
            if (twoPair && !fullHouse && houseValue < 700) outs += 4;
            if (pair && !threeKind && houseValue < 200) outs += 2;
            if (threeKind && !fourKind && houseValue < 400) outs += 1;
            if (!flush && closeToFlush && houseValue < 600) outs += (NUM_SUIT - 4);
            if (!straight && closeToStraight && houseValue < 500) outs += 8;

            // WHAT IF EVENTS THAT I NEED TO EVENTUALLY INCLUDE TO BETTER THE AI:
            /* If there is a straight within the five cards, it can still be bettered by having cards higher that
            continue the straight
               If the AI almost has a straight but a card in the middle is missing. How would I detect that.
             */

            // This will increase the value accordingly, so it can be used in equations to help the AI think.
            if (knownCards == 3) outs *= 4;
            if (knownCards == 4) outs *= 2;

        }


        // TEST TEST TEST
        /* for (int i = 0; i < cards.size(); i++) {
            System.out.print(cards.get(i) + "\n");
        }
        for (int i = 0; i < cards.size(); i++) {
            System.out.print(cardValue.get(i) + "\n");
        }
        System.out.print(pair + " " + flush + " " + straight + " " + fullHouse + " " + fourKind + " " + twoPair +  " " + threeKind + "\n" + pairCounterOne + " " + pairCounterTwo + " " + pairCounterThree + "\n") ;
        System.out.print(value + "\n"); */

        if (knownCards != 5) return outs;

        return 0;
    }

    // This corrects the value for kings and aces
    public int correctValue(int value) {
        if (value == 0 || value == 1) {
            value += NUM_SUIT;
        }
        return value;
    }

    // After each round, this changes the money accordingly
    public void changeMoney(int winnerNumber, int pot) throws Exception {

        // Remove money that everyone put in money
        for (int i = 0; i < playerMoney.size(); i++) {
            setMoney(i, getMoney(i) - getBet(i));
        }

        // We need the second place winner in order to complete this.
        if ((getMoney(winnerNumber) * 2) < pot) {
            throw new Exception("This part need to be completed");
        }

        // Give winner winning money
        setMoney(winnerNumber, getMoney(winnerNumber) + pot);
    }

    // This determines the higher value out of two cards
    public int maxValue(int valueOne, int valueTwo) throws Exception {

        // CHECK IF THIS IS NEEDED
        /*// This checks if there is an error
        if (valueOne == -1 || valueTwo == -1) {
            throw new Exception("Error reading cards: " + valueOne + " " + valueTwo);
        } */

        // Finds greatest value
        if (valueOne > valueTwo) {
            return valueOne;
        } return valueTwo;
    }

}
