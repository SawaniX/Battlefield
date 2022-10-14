import java.util.Scanner;

public class Main {
    final static int fieldSize = 10;
    final static String[] shipsNames = new String[] {"Aircraft Carrier",
            "Battleship", "Submarine", "Cruiser", "Destroyer"};
    final static int[] shipsSizes = new int[] {5, 4, 3, 3, 2};

    static Battlefield field = new Battlefield(fieldSize);


    public static void main(String[] args) {
        //Battlefield field = new Battlefield(fieldSize);
        field.initializeBattlefield();
        field.printBattlefield();

        createShips();
    }

    static void createShips() {
        for (int i = 0; i < shipsNames.length; i++) {
            boolean nextShip = false;

            while (!nextShip) {
                System.out.printf("Enter the coordinates of the %s (%d cells)",
                        shipsNames[i], shipsSizes[i]);
                System.out.println();

                Scanner scan = new Scanner(System.in);
                String word = scan.next();
                int row1 = Character.getNumericValue(word.charAt(0)) - 10;
                int column1 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                word = scan.next();
                int row2 = Character.getNumericValue(word.charAt(0)) - 10;
                int column2 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                nextShip = checkConstraints(row1, column1, row2, column2, shipsSizes[i]);

                if (nextShip) {
                    field.placeShipOnBattlefield(row1, column1, row2, column2);
                }
            }

            field.printBattlefield();
        }
    }

    static boolean checkConstraints(int row1, int column1, int row2, int column2, int shipSize) {
        boolean checkOutOfBoundaries = row1 < 0 || row2 < 0 || column1 < 0 || column2 < 0 ||
                row1 > fieldSize - 1 || row2 > fieldSize - 1 || column1 > fieldSize - 1 || column2 > fieldSize - 1;
        boolean checkShipDirection = row1 != row2 && column1 != column2;
        boolean checkShipSize = Math.abs(column2-column1)+1 != shipSize &&
                Math.abs(row1-row2)+1 != shipSize;

        if (checkOutOfBoundaries) {
            System.out.println("Error! Wrong coordinates! Try again:");
            return false;
        } else if (checkShipDirection) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        } else if (checkShipSize) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
            return false;
        } else if (!field.areShipCoordinatesCorrect(row1, column1, row2, column2)) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return false;
        } else {
            return true;
        }
    }
}
