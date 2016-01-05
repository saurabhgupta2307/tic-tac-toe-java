import java.util.Scanner;

/**
 * 
 * @author saurabh
 *
 */
public class TicTacToe {
    private static char[] config 
            = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

    /**
     * 
     * @param config
     */
    public static void printGrid (char[] config) {
    	String[] printConfig = new String[TicTacToeConfig.GRID_SIZE];
        for(int i = 0; i < TicTacToeConfig.GRID_SIZE 
                        && i < config.length; i++) {
            if(config[i] == ' ')
                printConfig[i] = "[" + (i+1) + "]";
            else if(config[i] == TicTacToeConfig.USER_SYMBOL 
                    || config[i] == TicTacToeConfig.AI_SYMBOL)
                printConfig[i] = " " + config[i] + " ";
            else {
                System.out.println("Invalid configuration! Exiting..");
                System.exit(0);
            }
        }

        System.out.println();
        System.out.println("\t     |     |     ");
        System.out.println("\t " + printConfig[0] + 
                            " | " + printConfig[1] + 
                            " | " + printConfig[2] + 
                            " ");
        System.out.println("\t_____|_____|_____");
        System.out.println("\t     |     |     ");
        System.out.println("\t " + printConfig[3] + 
                            " | " + printConfig[4] + 
                            " | " + printConfig[5] + 
                            " ");
        System.out.println("\t_____|_____|_____");
        System.out.println("\t     |     |     ");
        System.out.println("\t " + printConfig[6] + 
                            " | " + printConfig[7] + 
                            " | " + printConfig[8] + 
                            " ");
        System.out.println("\t     |     |     ");
        System.out.println();
    }

    /**
     * 
     * @param scan
     * @return
     */
    public static int getValidInput(Scanner scan) {
        int input = scan.nextInt();
        if(input < 1 || input > TicTacToeConfig.GRID_SIZE 
            || config[input-1] != ' ') {
            System.out.println("Invalid choice! Enter again..");
            input = getValidInput(scan);
        }
        return input;
    }

    /**
     * 
     * @param scan
     * @return
     */
    public static int getUserInput(Scanner scan) {
        System.out.print("Enter your choice [1-9]: ");
        int input = getValidInput(scan); 
        return input;
    }

    /**
     * 
     */
    public static void gameplay() {
        Scanner scan = new Scanner(System.in);
        int userInput = -1;
        byte winner = -1;

        printGrid(config);
        userInput = getUserInput(scan);

        config[userInput - 1] = TicTacToeConfig.USER_SYMBOL;
        TicTacToeConfig cfg = new TicTacToeConfig(config);
        TicTacToeNode node = new TicTacToeNode(cfg, null, 
                                TicTacToeConfig.USER_PLAYER);
        node.createChildren();
        node.postorderScore(TicTacToeConfig.AI_PLAYER);
        node.prune(TicTacToeConfig.AI_PLAYER);

        while(node != null 
                && node.getNode().getTurns() < TicTacToeConfig.GRID_SIZE
                && node.getNode().getWinner() == 0) {
            TicTacToeConfig moveConfig = node.getLeftChild().getNode();
            short moveIndex = cfg.findDifference(moveConfig);
            System.out.print("Player 2 selects " + (moveIndex + 1));
            config[moveIndex] = TicTacToeConfig.AI_SYMBOL;
            cfg = new TicTacToeConfig(config);
            
            printGrid(config);
            winner = cfg.getWinner();
            if(winner != 0) {
                System.out.println("Player " + winner + " wins!");
                break;
            }

            userInput = getUserInput(scan);

            config[userInput - 1] = TicTacToeConfig.USER_SYMBOL;
            cfg = new TicTacToeConfig(config);
            winner = cfg.getWinner();
            if(winner != 0) {
                printGrid(config);
                System.out.println("Player " + winner + " wins!");
                break;
            }

            node = node.findNode(config);
            if(node != null)
                node.setParent(null);
        }

        if(winner == 0) {
            printGrid(config);
            System.out.println("Draw!!!");
        }

        scan.close();
    }

    public static void main(String[] args) {
        gameplay();
    }
}