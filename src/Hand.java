public class Hand {
    boolean isAlive = !(this.value == 5);

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
            attacked.value = attackedValue;
        }
        attacked.value = attackedValue;
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