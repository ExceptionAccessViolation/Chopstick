public class Hand {
    boolean isAlive = !(this.value == -1);

    Allegiance allegiance;
    Direction direction;

    private int value = 1;

    public int getValue() {
        return this.value;
    }

    public void setValue(int i) {
        this.value = i;
    }

    public void transfer(Hand receiver) {
        int receivingValue = this.value + receiver.value;
        if (receivingValue == 5)
            receiver.suicide();
        else {
            if (receivingValue > 5)
                receivingValue -= 5;
            receiver.value = receivingValue;
        }
    }

    public void suicide() {
        this.value = -1;
        isAlive = false;
    }

    public static void divide(Hand aliveHand, Hand deadHand) {
        aliveHand.value /= 2;
        deadHand.value = aliveHand.value;
        deadHand.isAlive = true;
    }

    public static void oddDivide(Hand aliveHand, Hand deadHand) { // to divide hand with value 3
        aliveHand.setValue(2);
        deadHand.setValue(1);
        deadHand.isAlive = true;
    }

    public int combine(Hand hand) {
        if ((this.value + hand.value) >= 5) {
            return -1;
        }
        int handVal = hand.value;
        hand.suicide();
        this.value += handVal;
        return 0;
    }

    public static void attack(Hand attacker, Hand attacked) {
        int attackedValue = attacker.value + attacked.value;
        if (attackedValue == 5)
            attacked.suicide();
        else if (attackedValue > 5) {
            attackedValue -= 5;
//            attacked.value = attackedValue;
        }
        attacked.value = attackedValue;
    }

    public static void rearrange(Hand hand1, Hand hand2) {
        int og1 = hand1.value;
        int og2 = hand2.value;
        if (hand1.value > hand2.value) {
            hand1.value -= 1;
            hand2.value += 1;
            if (og1 == hand2.value || og2 == hand1.value) {
                hand1.value -= 1;
                hand2.value += 1;
            }
        } else {
            hand1.value += 1;
            hand2.value -= 1;
            if (og1 == hand2.value || og2 == hand1.value) {
                hand1.value += 1;
                hand2.value -= 1;
            }
        }
        if (hand1.value == 0) {
            hand1.value = -1;
            hand1.isAlive = false;
        }
        if (hand2.value == 0) {
            hand2.value = -1;
            hand2.isAlive = false;
        }
    }

    public void dead() {
        if (!this.isAlive)
            this.value = -1;
    }

    public void printDeadString() {
        switch (this.allegiance) {
            case OPPONENT -> {
                switch (this.direction) {
                    case LEFT -> System.out.println("Player 2's left hand died," +
                            " since it reached a value of 5.");
                    case RIGHT -> System.out.println("Player 2's right hand died," +
                            " since it reached a value of 5.");
                }
            }
            case PLAYER -> {
                switch (this.direction) {
                    case LEFT -> System.out.println("Player 1's left hand died," +
                            " since it reached a value of 5.");
                    case RIGHT -> System.out.println("Player 2's right hand died," +
                            " since it reached a value of 5.");
                }
            }
        }
    }
}
