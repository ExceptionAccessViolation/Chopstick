import java.util.Scanner;

public class Chopstick {

    public static Hand hand1 = new Hand(); // left
    public static Hand hand2 = new Hand(); // right

    // opponent
    public static Hand hand3 = new Hand(); // right
    public static Hand hand4 = new Hand(); // left

    public static void init() {
        System.out.println("Welcome to Chopstick! \nChopstick is a 1v1 chopsticks game.");
    }

    public static void printGame(Turn turn) {
        if (turn == Turn.PLAYER) {
            System.out.println(hand3.getValue() + "  -  " + hand4.getValue()
                    + "\n" + hand1.getValue() + "  -  " + hand2.getValue());
        } else {
            System.out.println(hand2.getValue() + "  -  " + hand1.getValue()
                    + "\n" + hand4.getValue() + "  -  " + hand3.getValue());
        }
    }

    public static void main(String[] args) {
        Integer[] game = {hand1.getValue(), hand2.getValue(), hand3.getValue(), hand4.getValue()};

        boolean loop = true;
        Scanner s = new Scanner(System.in);

        init();

        String help = """
                Enter what operation you want to do (transfer, attack, combine, divide or rearrange),
                or if you want to exit or print the current situation, use "exit" and "print" respectively.
                Use "help" for help.
                If a hand's value is -1, it implies that it is dead.
                                
                Rules for the game can be found on "https://en.wikipedia.org/wiki/Chopsticks_(game)".
                The variation being played is Misère.
                
                Player 1 is the player who played first, and Player 2 is the other player.""";

        String left = "left";
        String right = "right";

        hand1.allegiance = Allegiance.PLAYER;
        hand2.allegiance = Allegiance.PLAYER;
        hand3.allegiance = Allegiance.OPPONENT;
        hand4.allegiance = Allegiance.OPPONENT;

        hand1.direction = Direction.LEFT;
        hand2.direction = Direction.RIGHT;
        hand3.direction = Direction.RIGHT;
        hand4.direction = Direction.LEFT;

        Turn turn = Turn.PLAYER;

        System.out.println(help);
        String input;

        while (loop) {

            if (turn == Turn.PLAYER)
                System.out.println("Player 1's turn.");
            else
                System.out.println("Player 2's turn.");

            System.out.print(">>> ");
            input = s.nextLine().toLowerCase().trim();

            boolean repeat = false;

            if (turn == Turn.PLAYER) {

                switch (input) {
                    case "exit" -> {
                        System.out.println("Thanks for playing!");
                        loop = false;
                    }
                    case "transfer" -> {
                        String hand;
                        if (!hand1.isAlive || !hand2.isAlive) {
                            System.out.println("You cannot transfer since one of your hands is dead.");
                            repeat = true;
                            break;
                        }
                        while (true) {
                            System.out.print("To which hand?: ");
                            hand = s.nextLine().toLowerCase().trim();
                            if (hand.equals(left) || hand.equals(right))
                                    break;
                            System.out.println("Please type left or right.");
                        }
                        if (hand.equals(left)) {
                            hand2.transfer(hand1);
                            System.out.println("You added your right hand to your left hand, " +
                                    "resulting in it having a value of " + hand1.getValue() + ".");
                        } else {
                            hand1.transfer(hand2);
                            System.out.println("You added your left hand to your right hand, " +
                                    "resulting in it having a value of " + hand2.getValue() + ".");
                        }
                    }
                    case "divide" -> {
                        String dividingHand;
                        if (!hand1.isAlive || !hand2.isAlive) {
                            if (!hand2.isAlive)
                                dividingHand = left;
                            else
                                dividingHand = right;

                            if ((dividingHand.equals(left) && hand1.getValue() == 1) || (dividingHand.equals(right) && hand2.getValue() == 1)) {
                                System.out.println("You cannot divide your " + dividingHand + " hand as its value is 1.");
                                repeat = true;
                                break;
                            }

                            if (dividingHand.equals(left) && hand1.getValue() == 3) {
                                Hand.oddDivide(hand1, hand2);
                                System.out.println("You divided your " + dividingHand + " hand, resulting in your " + dividingHand +
                                        " having a value of " + hand1.getValue() + " and your left hand having a value of " + hand2.getValue() + ".");
                                break;
                            }
                            if (dividingHand.equals(right) && hand2.getValue() == 3) {
                                Hand.oddDivide(hand2, hand1);
                                System.out.println("You divided your " + dividingHand + " hand, resulting in your " + dividingHand +
                                        " having a value of " + hand2.getValue() + " and your left hand having a value of " + hand1.getValue() + ".");
                                break;
                            }

                            if (dividingHand.equals(right))
                                Hand.divide(hand2, hand1);
                            else
                                Hand.divide(hand1, hand2);
                            System.out.println("You divided your " + dividingHand +
                                    " hand, resulting in both your hands having the value of " + hand1.getValue());
                        } else {
                            System.out.println("You cannot divide, since both of your hands are already alive.");
                            repeat = true;
                        }
                    }
                    case "combine" -> {
                        if (!hand3.isAlive || !hand4.isAlive) {
                            System.out.println("You cannot combine since one of your hands is dead.");
                            repeat = true;
                            break;
                        }
                        int hand1val = hand1.combine(hand2);
                        if (hand1val == -1) {
                            System.out.println("You cannot combine your hands' " +
                                    "values as they exceed or result in a value equal to 5.");
                            repeat = true;
                            break;
                        }
                        System.out.println("You combined your hands' values, " +
                                "resulting in your left hand having a value of " + hand1.getValue());
                    }
                    case "attack" -> {
                        String attackedHand, hand;
                        while (true) {
                            System.out.print("With which hand of yours do you want to attack?: ");
                            hand = s.nextLine().toLowerCase().trim();
                            if (!(hand.equals(left) || hand.equals(right)))
                                System.out.println("Please enter either left or right.");
                            else if (hand.equals(left) && !hand1.isAlive)
                                System.out.println("You cannot attack with your " + hand + " hand since it is dead.");
                            else if (hand.equals(right) && !hand2.isAlive)
                                System.out.println("You cannot attack with your " + hand + " hand since it is dead.");
                            else {
                                while (true) {
                                    System.out.print("Which of your opponent's hands do you want to attack?: ");
                                    attackedHand = s.nextLine().toLowerCase().trim();
                                    if (!(attackedHand.equals(left) || attackedHand.equals(right)))
                                        System.out.println("Please enter either left or right.");
                                    else if (attackedHand.equals(left) && !hand4.isAlive)
                                        System.out.println("You cannot attack your opponent's " + hand + " hand since it is dead.");
                                    else if (attackedHand.equals(right) && !hand3.isAlive)
                                        System.out.println("You cannot attack your opponent's " + hand + " hand since it is dead.");
                                    else
                                        break;
                                }
                                break;
                            }
                        }
                        if (attackedHand.equals(left)) {
                            if (hand.equals(left)) {
                                Hand.attack(hand1, hand4);
                                System.out.println("You attacked your opponent's left hand with your left hand, " +
                                        "resulting in their hand having a value of " + hand4.getValue());
                            }
                            if (hand.equals(right)) {
                                Hand.attack(hand2, hand4);
                                System.out.println("You attacked your opponent's left hand with your right hand, " +
                                        "resulting in their hand having a value of " + hand4.getValue());
                            }
                        } else {
                            if (hand.equals(left)) {
                                Hand.attack(hand1, hand3);
                                System.out.println("You attacked your opponent's right hand with your left hand, " +
                                        "resulting in their hand having a value of " + hand3.getValue());
                            }
                            if (hand.equals(right)) {
                                Hand.attack(hand2, hand3);
                                System.out.println("You attacked your opponent's right hand with your right hand, " +
                                        "resulting in their hand having a value of " + hand3.getValue());
                            }
                        }
                    }
                    case "rearrange" -> {
                        if (!hand1.isAlive || !hand2.isAlive) {
                            System.out.println("You cannot rearrange your hands' values since one of them is dead.");
                            repeat = true;
                            break;
                        }
                        if (hand1.getValue() == 4 && hand2.getValue() == 4) {
                            System.out.println("You cannot rearrange your hands' values since they are too large.");
                            repeat = true;
                            break;
                        }
                        if ((hand1.getValue() == 1 && hand2.getValue() == 1) || (hand1.getValue() == 1 &&
                                hand2.getValue() == 0) || (hand1.getValue() == 0 && hand2.getValue() == 1)) {
                            System.out.println("You cannot rearrange your hands' values since they are too small.");
                            repeat = true;
                            break;
                        }
                        Hand.rearrange(hand1, hand2);
                        System.out.println("You rearranged your hands' values, resulting in your " +
                                hand1.direction.toString().toLowerCase() + " hand having a value of " + hand1.getValue() +
                                " and your " + hand2.direction.toString().toLowerCase() + " hand having a value of " +
                                hand2.getValue() + ".");
                    }
                    case "print" -> {
                        printGame(turn);
                        repeat = true;
                    }
                    case "help" -> {
                        System.out.println(help);
                        repeat = true;
                    }
                    case "reset" -> {
                        reset();
                        repeat = true;
                    }
                    default -> {
                        System.out.println("Please enter a valid value. For possible values, type help.");
                        repeat = true;
                        input = "";
                    }
                }

                if (!repeat)
                    turn = Turn.OPPONENT;
            }

            else {

                switch (input) {
                    case "exit" -> {
                        System.out.println("Thanks for playing!");
                        loop = false;
                    }
                    case "transfer" -> {
                        String hand;
                        if (!hand3.isAlive || !hand4.isAlive) {
                            System.out.println("You cannot transfer since one of your hands is dead.");
                            repeat = true;
                            break;
                        }
                        while (true) {
                            System.out.print("To which hand?: ");
                            hand = s.nextLine().toLowerCase().trim();
                            if (hand.equals(left) || hand.equals(right))
                                break;
                            System.out.println("Please type left or right.");
                        }
                        if (hand.equals(right)) {
                            hand4.transfer(hand3);
                            System.out.println("You added your left hand to your right hand, " +
                                    "resulting in it having a value of " + hand3.getValue() + ".");
                        } else {
                            hand3.transfer(hand4);
                            System.out.println("You added your right hand to your left hand, " +
                                    "resulting in it having a value of " + hand4.getValue() + ".");
                        }
                    }
                    case "divide" -> {
                        String dividingHand;
                        if (!hand3.isAlive || !hand4.isAlive) {
                            if (!hand3.isAlive)
                                dividingHand = left;
                            else
                                dividingHand = right;

                            if ((dividingHand.equals(left) && hand4.getValue() == 1) || (dividingHand.equals(right) && hand3.getValue() == 1)) {
                                System.out.println("You cannot divide your " + dividingHand + " hand as its value is 1.");
                                repeat = true;
                                break;
                            }

                            if (dividingHand.equals(left) && hand4.getValue() == 3) {
                                Hand.oddDivide(hand4, hand3);
                                System.out.println("You divided your " + dividingHand + " hand, resulting in your " + dividingHand +
                                        " having a value of " + hand4.getValue() + " and your right hand having a value of " + hand3.getValue() + ".");
                                break;
                            }
                            if (dividingHand.equals(right) && hand3.getValue() == 3) {
                                Hand.oddDivide(hand3, hand4);
                                System.out.println("You divided your " + dividingHand + " hand, resulting in your " + dividingHand +
                                        " having a value of " + hand3.getValue() + " and your left hand having a value of " + hand4.getValue() + ".");
                                break;
                            }

                            if (dividingHand.equals(right))
                                Hand.divide(hand3, hand4);
                            else
                                Hand.divide(hand4, hand3);
                            System.out.println("You divided your " + dividingHand +
                                    " hand, resulting in both your hands having the value of " + hand3.getValue());
                        } else {
                            System.out.println("You cannot divide, since both of your hands are already alive.");
                            repeat = true;
                        }
                    }
                    case "combine" -> {
                        if (!hand3.isAlive || !hand4.isAlive) {
                            System.out.println("You cannot combine since one of your hands is dead.");
                            repeat = true;
                            break;
                        }
                        int hand3val = hand4.combine(hand3);
                        if (hand3val == -1) {
                            System.out.println("You cannot combine your hands' " +
                                    "values as they exceed or result in a value equal to 5.");
                            repeat = true;
                            break;
                        }
                        System.out.println("You combined your hands' values, " +
                                "resulting in your left hand having a value of " + hand4.getValue());
                    }
                    case "attack" -> {
                        String attackedHand, hand;
                        while (true) {
                            System.out.print("With which hand of yours do you want to attack?: ");
                            hand = s.nextLine().toLowerCase().trim();
                            if (!(hand.equals(left) || hand.equals(right)))
                                System.out.println("Please enter either left or right.");
                            else if (hand.equals(left) && !hand4.isAlive)
                                System.out.println("You cannot attack with your " + hand + " hand since it is dead.");
                            else if (hand.equals(right) && !hand3.isAlive)
                                System.out.println("You cannot attack with your " + hand + " hand since it is dead.");
                            else {
                                while (true) {
                                    System.out.print("Which of your opponent's hands do you want to attack?: ");
                                    attackedHand = s.nextLine().toLowerCase().trim();
                                    if (!(attackedHand.equals(left) || attackedHand.equals(right)))
                                        System.out.println("Please enter either left or right.");
                                    else if (attackedHand.equals(left) && !hand1.isAlive)
                                        System.out.println("You cannot attack your opponent's " + hand + " hand since it is dead.");
                                    else if (attackedHand.equals(right) && !hand2.isAlive)
                                        System.out.println("You cannot attack your opponent's " + hand + " hand since it is dead.");
                                    else
                                        break;
                                }
                                break;
                            }
                        }
                        if (attackedHand.equals(left)) {
                            if (hand.equals(left)) {
                                Hand.attack(hand4, hand1);
                                System.out.println("You attacked your opponent's left hand with your left hand, " +
                                        "resulting in their hand having a value of " + hand1.getValue());
                            }
                            if (hand.equals(right)) {
                                Hand.attack(hand3, hand1);
                                System.out.println("You attacked your opponent's left hand with your right hand, " +
                                        "resulting in their hand having a value of " + hand1.getValue());
                            }
                        } else {
                            if (hand.equals(left)) {
                                Hand.attack(hand4, hand2);
                                System.out.println("You attacked your opponent's right hand with your left hand, " +
                                        "resulting in their hand having a value of " + hand2.getValue());
                            }
                            if (hand.equals(right)) {
                                Hand.attack(hand3, hand2);
                                System.out.println("You attacked your opponent's right hand with your right hand, " +
                                        "resulting in their hand having a value of " + hand2.getValue());
                            }
                        }
                    }
                    case "rearrange" -> {
                        if (!hand3.isAlive || !hand4.isAlive) {
                            System.out.println("You cannot rearrange your hands' values since one of them is dead.");
                            repeat = true;
                            break;
                        }
                        if (hand3.getValue() == 4 && hand4.getValue() == 4) {
                            System.out.println("You cannot rearrange your hands' values since they are too large.");
                            repeat = true;
                            break;
                        }
                        if ((hand3.getValue() == 1 && hand4.getValue() == 1) || (hand3.getValue() == 1 &&
                                hand4.getValue() == 0) || (hand4.getValue() == 0 && hand3.getValue() == 1)) {
                            System.out.println("You cannot rearrange your hands' values since they are too small.");
                            repeat = true;
                            break;
                        }
                        Hand.rearrange(hand3, hand4);
                        System.out.println("You rearranged your hands' values, resulting in your " +
                                hand4.direction.toString().toLowerCase() + " hand having a value of " + hand4.getValue() +
                                " and your " + hand3.direction.toString().toLowerCase() + " hand having a value of " +
                                hand3.getValue() + ".");
                    }
                    case "print" -> {
                        printGame(turn);
                        repeat = true;
                    }
                    case "help" -> {
                        System.out.println(help);
                        repeat = true;
                    }
                    case "reset" -> {
                        reset();
                        repeat = true;
                    }
                    default -> {
                        System.out.println("Please enter a valid value. For possible values, type help.");
                        repeat = true;
                        input = "";
                    }
                }
                if (!repeat)
                    turn = Turn.PLAYER;
            }

            if (hand1.getValue() >= 5) {
                hand1.suicide();
                hand1.printDeadString();
            }
            if (hand2.getValue() >= 5) {
                hand2.suicide();
                hand2.printDeadString();
            }
            if (hand3.getValue() >= 5) {
                hand3.suicide();
                hand3.printDeadString();
            }
            if (hand4.getValue() >= 5) {
                hand4.suicide();
                hand4.printDeadString();
            }

            if (!hand1.isAlive && !hand2.isAlive) {
                System.out.println("""
                        \n
                        **************************************************
                        Player 2 wins since both of player 1's hands died!
                        **************************************************
                        """);
                loop = false;
            }
            if (!hand3.isAlive && !hand4.isAlive) {
                System.out.println("""
                        \n
                        **************************************************
                        Player 1 wins since both of player 2's hands died!
                        **************************************************
                        """);
                loop = false;
            }

            input = "";
        }
    }

    public static void reset() {
        hand1.setValue(1);
        hand1.isAlive = true;
        hand2.setValue(1);
        hand2.isAlive = true;
        hand3.setValue(1);
        hand3.isAlive = true;
        hand4.setValue(1);
        hand4.isAlive = true;
    }
}
