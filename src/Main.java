public class Main {

    private static final int FIELD_SIZE = 10;

    private static final String[] SHIPS_NAMES = {
            "Aircraft Carrier",
            "Battleship",
            "Submarine",
            "Cruiser",
            "Destroyer"
    };
    private static final int[] SHIPS_SIZES = {5, 4, 3, 3, 2};

    public static void main(String[] args) {
        Battlefield battlefield = new Battlefield(FIELD_SIZE, SHIPS_NAMES, SHIPS_SIZES);
        battlefield.StartGame();
    }
}