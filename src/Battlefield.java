import java.util.Arrays;
import java.util.Scanner;

class Battlefield {
    private static final String FOG = "~";
    private static final String OWN_SHIP = "O";
    private static final String HITTED = "X";
    private static final String MISSED = "M";
    private final String[][] field;
    private final String[][] emptyField;
    private final int fieldSize;
    private final String[] shipsNames;
    private final int[] shipsSizes;
    private int shipsNumber;


    public Battlefield(int fieldSize, String[] shipsNames, int[] shipsSizes) {
        this.fieldSize = fieldSize;
        this.field = new String[fieldSize][fieldSize];
        this.emptyField = new String[fieldSize][fieldSize];
        this.shipsNames = shipsNames;
        this.shipsSizes = shipsSizes;
        this.shipsNumber = shipsNames.length;
    }

    public void StartGame() {
        initializeBattlefield(field);
        printBattlefield(field);
        createShips();
        startShooting();
    }

    private void startShooting() {
        boolean isGameEnd = false;
        System.out.println("The game starts!");

        initializeBattlefield(emptyField);
        printBattlefield(emptyField);

        System.out.println("Take a shot!");

        shoot();

        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    private void shoot() {
        Scanner scan = new Scanner(System.in);
        while (shipsNumber > 0) {
            String word = scan.next();
            int row = Character.getNumericValue(word.charAt(0)) - 10;
            int column = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

            if (row >= 0 && row < fieldSize && column >= 0 && column < fieldSize) {
                processShot(row, column);
            } else {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
            }
        }
    }

    private void processShot(int row, int column) {
        boolean hit = field[row][column].equals(OWN_SHIP) || field[row][column].equals(HITTED);

        if (hit) {
            shipHitted(row, column);
        } else {
            shipMissed(row, column);
        }
    }

    private void shipHitted(int row, int column) {
        boolean wasShipDestroyed = isShipDestroyed(row, column, OWN_SHIP);
        emptyField[row][column] = HITTED;
        field[row][column] = HITTED;
        printBattlefield(emptyField);
        boolean isShipDestroyed = isShipDestroyed(row, column, OWN_SHIP);
        if (isShipDestroyed && !wasShipDestroyed) {
            shipsNumber -= 1;
            System.out.println("You sank a ship! Specify a new target:" + shipsNumber);
        } else {
            System.out.println("You hit a ship!");
        }
    }

    private void shipMissed(int row, int column) {
        emptyField[row][column] = MISSED;
        field[row][column] = MISSED;
        printBattlefield(emptyField);
        System.out.println("You missed!");
    }

    private void printBattlefield(String[][] battleField) {
        int[] first_row = new int[fieldSize];
        Arrays.setAll(first_row, i -> i + 1);
        char first_column = 'A';

        System.out.println();
        for (int i = 0; i < fieldSize + 1; i++) {
            for (int j = 0; j < fieldSize + 1; j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(first_row[j - 1] + " ");
                } else if (j == 0) {
                    System.out.print(Character.toString(first_column + i - 1) + " ");
                } else {
                    System.out.print(battleField[i - 1][j - 1] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void initializeBattlefield(String[][] battleField) {
        for (String[] row : battleField) {
            Arrays.fill(row, FOG);
        }
    }

    private void placeShipOnBattlefield(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                field[row1][column] = OWN_SHIP;
            }
        } else {
            int startRow = Math.min(row1, row2);
            int stopRow = Math.max(row1, row2);

            for (int row = startRow; row <= stopRow; row++) {
                field[row][column1] = OWN_SHIP;
            }
        }
    }

    private boolean areShipCoordinatesCorrect(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            int startColumn = Math.min(column1, column2);
            int stopColumn = Math.max(column1, column2);

            for (int column = startColumn; column <= stopColumn; column++) {
                if (!areNeighborsEmpty(row1, column, FOG)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    return false;
                }
            }
            return true;
        }

        int startRow = Math.min(row1, row2);
        int stopRow = Math.max(row1, row2);

        for (int row = startRow; row <= stopRow; row++) {
            if (!areNeighborsEmpty(row, column1, FOG)) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                return false;
            }
        }
        return true;
    }

    private boolean areNeighborsEmpty(int row, int column, String sign) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(row - i < 0 || row - i > fieldSize - 1 || column - j < 0 || column - j > fieldSize - 1)) {
                    if (!field[row - i][column - j].equals(sign)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isShipDestroyed(int row, int column, String sign) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(row - i < 0 || row - i > fieldSize - 1 || column - j < 0 || column - j > fieldSize - 1)) {
                    if (field[row - i][column - j].equals(sign)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void createShips() {
        for (int i = 0; i < shipsNames.length; i++) {
            boolean isNextShip = false;
            System.out.printf("Enter the coordinates of the %s (%d cells)\n",
                    shipsNames[i], shipsSizes[i]);
            System.out.println();

            while (!isNextShip) {
                Scanner scan = new Scanner(System.in);
                String word = scan.next();
                int row1 = Character.getNumericValue(word.charAt(0)) - 10;
                int column1 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                word = scan.next();
                int row2 = Character.getNumericValue(word.charAt(0)) - 10;
                int column2 = Integer.parseInt(word.replaceAll("[\\D]", "")) - 1;

                isNextShip = isShipPlaceableAtCoordinates(row1, column1, row2, column2, shipsSizes[i]);

                if (isNextShip) {
                    placeShipOnBattlefield(row1, column1, row2, column2);
                }
            }
            printBattlefield(field);
        }
    }

    private boolean isShipPlaceableAtCoordinates(int row1, int column1, int row2, int column2, int shipSize) {
        return !isOutOfBoundaries(row1, column1, row2, column2)
                && !isShipDirectionWrong(row1, column1, row2, column2)
                && !isShipSizeWrong(row1, column1, row2, column2, shipSize)
                && areShipCoordinatesCorrect(row1, column1, row2, column2);
    }

    private boolean isOutOfBoundaries(int row1, int column1, int row2, int column2) {
        boolean isOutOfBoundaries = row1 < 0
                || row2 < 0
                || column1 < 0
                || column2 < 0
                || row1 > fieldSize - 1
                || row2 > fieldSize - 1
                || column1 > fieldSize - 1
                || column2 > fieldSize - 1;
        if(isOutOfBoundaries) {
            System.out.println("Error! Wrong coordinates! Try again:");
        }
        return isOutOfBoundaries;
    }

    private boolean isShipDirectionWrong(int row1, int column1, int row2, int column2) {
        boolean isShipDirectionWrong = row1 != row2 && column1 != column2;
        if(isShipDirectionWrong) {
            System.out.println("Error! Wrong ship location! Try again:");
        }
        return isShipDirectionWrong;
    }

    private boolean isShipSizeWrong(int row1, int column1, int row2, int column2, int shipSize) {
        boolean isShipSizeWrong = Math.abs(column2 - column1) + 1 != shipSize && Math.abs(row1 - row2) + 1 != shipSize;
        if(isShipSizeWrong) {
            System.out.println("Error! Wrong length of the Submarine! Try again:");
        }
        return isShipSizeWrong;
    }
}