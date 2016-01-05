/**
 * 
 * @author saurabh
 *
 */
public class TicTacToeConfig {
    private short config;
    private byte turns;

    public static final char USER_SYMBOL = 'O';
    public static final char AI_SYMBOL = 'X';
    public static final byte USER_PLAYER = 1;
    public static final byte AI_PLAYER = 2;
    public static final short GRID_SIZE = 9;

    /**
     * 
     */
    public TicTacToeConfig() {
        config = 0;
        turns = 0;
    }

    /**
     * 
     * @param config
     */
    public TicTacToeConfig(short config) {
        this.config = config;
        calculateTurns();
    }

    /**
     * 
     * @param configS
     */
    public TicTacToeConfig(char[] configS) {
        config = configStringToNum(configS);
        calculateTurns();
    }

    /**
     * 
     * @param configS
     * @return
     */
    public static short configStringToNum(char[] configS) {
        short config = 0;

        for(short i = 0; i < GRID_SIZE && i < configS.length; i++) {
            if(configS[i] == USER_SYMBOL)
                config += Math.pow(3, i);
            else if(configS[i] == AI_SYMBOL)
                config += Math.pow(3, i) * AI_PLAYER;
            else if(configS[i] != ' ') {
                config = 0;
                break;
            }
        }

        return config;
    }

    /**
     * 
     * @param config
     * @return
     */
    public static char[] configNumToString(short config) {
        char[] configS = new char[GRID_SIZE];
        short num = config;

        for(short i = GRID_SIZE - 1; i >= 0; i--) {
            if(num >= AI_PLAYER * Math.pow(3, i)) {
                configS[i] = AI_SYMBOL;
                num -= AI_PLAYER * Math.pow(3, i);
            }
            else if(num >= Math.pow(3, i)) {
                configS[i] = USER_SYMBOL;
                num -= Math.pow(3, i);
            }
            else
                configS[i] = ' ';
        }

        if(num != 0)
            return null;

        return configS;
    }

    /**
     * 
     */
    public void calculateTurns() {
        this.turns = 0;

        if(this.config != 0) {
            char[] configS = configNumToString(this.config);
            for(short i = 0; i < GRID_SIZE && i < configS.length; i++) {
                if(configS[i] == USER_SYMBOL 
                    || configS[i] == AI_SYMBOL)
                    turns++;
            }
        }
    }

    /**
     * 
     * @return
     */
    public short getConfig() {
        return config;
    }

    /**
     * 
     * @return
     */
    public byte getTurns() {
        return turns;
    }

    /**
     * 
     */
    public String toString() {
        return "Config:" + 
            (new String(configNumToString(config))) + 
            ", Turns:" + turns;
    }

    /**
     * 
     * @param config2
     * @return
     */
    public short findDifference(TicTacToeConfig config2) {
        char[] configS1 = configNumToString(this.config);
        char[] configS2 = configNumToString(config2.getConfig());
        short returnValue = -1;

        for(short i = 0; i < GRID_SIZE && i < configS1.length; i++) {
            if(configS1[i] != configS2[i]) {
                returnValue = i;
                break;
            }
        }

        return returnValue;
    }

    /**
     * 
     * @return
     */
    public byte getWinner() {
        char winner = ' ';
        char[] configS = configNumToString(config);

        if(configS[0] != ' '
            && ((configS[0] == configS[1] && configS[0] == configS[2]) 
                || (configS[0] == configS[3] && configS[0] == configS[6])
                || (configS[0] == configS[4] && configS[0] == configS[8])))
            winner = configS[0];
        else if(configS[1] != ' '
            && (configS[1] == configS[4] && configS[1] == configS[7]))
            winner = configS[1];
        else if(configS[2] != ' '
            && ((configS[2] == configS[4] && configS[2] == configS[6]) 
                || (configS[2] == configS[5] && configS[2] == configS[8])))
            winner = configS[2];
        else if(configS[3] != ' '
            && (configS[3] == configS[4] && configS[3] == configS[5]))
            winner = configS[3];
        else if(configS[6] != ' '
            && (configS[6] == configS[7] && configS[6] == configS[8]))
            winner = configS[6];

        if(winner == AI_SYMBOL)
            return AI_PLAYER;
        else if(winner == USER_SYMBOL)
            return USER_PLAYER;
        else
            return 0;
    }
}