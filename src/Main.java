
public class Main {
    /*
    * When you declare static variable you should use only
    * uppercase letters
    */
    private static final int FIELD_SIZE = 10;
    /*
    * No need to add "new String[]" or "new int[]" when you
    * initialize array on declaration
    */
    private static final String[] SHIPS_NAMES = {
            "Aircraft Carrier",
            "Battleship",
            "Submarine",
            "Cruiser",
            "Destroyer"
    };
    private static final int[] SHIPS_SIZES = {5, 4, 3, 3, 2};

    /*
    * Remember about access modifiers like private, public,
    * protected - it makes code more readable
    */


    public static void main(String[] args) {
        Battlefield battlefield = new Battlefield(FIELD_SIZE, SHIPS_NAMES, SHIPS_SIZES);
        battlefield.StartGame();
    }
}
