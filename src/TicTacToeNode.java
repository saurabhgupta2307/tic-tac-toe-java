/**
 * 
 * @author saurabh
 *
 */
public class TicTacToeNode {
    private TicTacToeConfig node;
    private byte level;
    private byte score;
    private byte player;

    private TicTacToeNode parent;
    private TicTacToeNode leftChild;
    private TicTacToeNode nextSibling;

    //private TicTacToeNode() { }

    /**
     * 
     * @param config
     * @param parent
     * @param player
     */
    public TicTacToeNode(short config, TicTacToeNode parent,
                            byte player) {
        node = new TicTacToeConfig(config);
        this.parent = parent;
        this.player = player;
        if(parent != null) {
            level = parent.getLevel();
            level++;
        }
    }

    /**
     * 
     * @param configS
     * @param parent
     * @param player
     */
    public TicTacToeNode(char[] configS, TicTacToeNode parent,
                            byte player) {
        node = new TicTacToeConfig(configS);
        this.parent = parent;
        this.player = player;
        if(parent != null) {
            level = parent.getLevel();
            level++;
        }
    }

    /**
     * 
     * @param config
     * @param parent
     * @param player
     */
    public TicTacToeNode(TicTacToeConfig config, 
                            TicTacToeNode parent,
                            byte player) {
        node = config;
        this.parent = parent;
        this.player = player;
        if(parent != null) {
            level = parent.getLevel();
            level++;
        }
    }

    /**
     * 
     */
    public void createChildren() {
        if(node.getTurns() >= TicTacToeConfig.GRID_SIZE 
            || node.getWinner() != 0) 
            return;

        byte nextPlayer = (byte)(
            (this.player == TicTacToeConfig.USER_PLAYER) ? 
                TicTacToeConfig.AI_PLAYER : 
                TicTacToeConfig.USER_PLAYER);
        char symbol = 
            (nextPlayer == TicTacToeConfig.AI_PLAYER) ? 
                TicTacToeConfig.AI_SYMBOL : 
                TicTacToeConfig.USER_SYMBOL;

        TicTacToeNode currentChild = null;
        char[] configS = TicTacToeConfig.configNumToString(node.getConfig());

        for(int i = 0; i < TicTacToeConfig.GRID_SIZE 
                        && i < configS.length; i++) {
            if(configS[i] == ' ') {
                configS[i] = symbol;
                if(leftChild == null) {
                    leftChild = new TicTacToeNode(configS, this, nextPlayer);
                    currentChild = leftChild;
                }
                else {
                    currentChild.setNextSibling(
                        new TicTacToeNode(configS, this, nextPlayer));
                    currentChild = currentChild.nextSibling;
                }
                configS[i] = ' ';
                currentChild.createChildren();
            }
        }
    }

    /**
     * 
     * @param configS
     * @return
     */
    public TicTacToeNode findNode(char[] configS) {
        //short config = TicTacToeConfig.configStringToNum(configS);
        TicTacToeNode returnNode = null;

        if(TicTacToeConfig.configStringToNum(configS) == node.getConfig())
            return this;

        if(leftChild != null)
            returnNode = leftChild.findNode(configS);

        if(returnNode == null && nextSibling != null)
            returnNode = nextSibling.findNode(configS);

        return returnNode;
    }

    /**
     * 
     * @param indent
     */
    public void preorderPrint(String indent) {
        System.out.println(indent + "Player:" + player +
                            ", Winner:" + node.getWinner() +
                            ", " + node + ", Score:" + score);

        if(leftChild != null)
            leftChild.preorderPrint(indent + "    ");
        if(nextSibling != null)
            nextSibling.preorderPrint(indent);
    }

    /**
     * 
     * @param player
     */
    public void postorderScore(byte player) {
        byte winner = node.getWinner();

        if(this.player == player)
            this.score = 1;
        else
            this.score = -1;

        if(leftChild != null)
            leftChild.postorderScore(player);
        else if(winner == player)
            this.score = 1;
        else if(winner == 0)
            this.score = 0;
        else {
            this.score = -1;
        }

        if(parent != null)
            parent.updateScore(score, player);

        if(nextSibling != null)
            nextSibling.postorderScore(player);
    }

    /**
     * 
     * @param player
     */
    public void prune(byte player) {
        //System.out.println("Pruning " + node);
        while(nextSibling != null && this.player == player
                && nextSibling.getScore() <= this.score) {
            //System.out.println("\tDeleting Sibling " + nextSibling.getNodeString());
            nextSibling = nextSibling.getNextSibling();
        }

        if(nextSibling != null)
            nextSibling.prune(player);

        if(this.score == -1)
            leftChild = null;

        while(leftChild != null && leftChild.getScore() == -1) {
            //System.out.println("\tDeleting Child " + leftChild.getNodeString());
            leftChild = leftChild.getNextSibling();
        }

        if(leftChild != null)
            leftChild.prune(player);

        if(this.player != player && leftChild != null 
            && leftChild.getNextSibling() != null
            && leftChild.getScore() < leftChild.getNextSibling().getScore())
            leftChild = leftChild.getNextSibling();
    }

    /**
     * 
     * @param score
     * @param player
     */
    public void updateScore(byte score,  byte player) {
        if((score > this.score && player != this.player) 
            || (score < this.score && player == this.player))
            this.score = score;
    }

    /**
     * 
     * @param parent
     */
    public void setParent(TicTacToeNode parent) {
        this.parent = parent;
        if(parent != null) {
            level = parent.getLevel();
            level++;
        }
        else {
            // TODO Recalculate Levels
        }
    }

    /**
     * 
     * @param childNode
     */
    public void setLeftChild(TicTacToeNode childNode) {
        leftChild = childNode;
    }

    /**
     * 
     * @param siblingNode
     */
    public void setNextSibling(TicTacToeNode siblingNode) {
        nextSibling = siblingNode;
    }

    /**
     * 
     * @return
     */
    public TicTacToeConfig getNode() {
        return node;
    }

    /**
     * 
     * @return
     */
    public TicTacToeNode getParent() {
        return parent;
    }

    /**
     * 
     * @return
     */
    public TicTacToeNode getLeftChild() {
        return leftChild;
    }

    /**
     * 
     * @return
     */
    public TicTacToeNode getNextSibling() {
        return nextSibling;
    }

    /**
     * 
     * @return
     */
    public String getNodeString() {
        return node.toString();
    }

    /**
     * 
     * @return
     */
    public String getParentString() {
        if(parent == null)
            return null;
        return parent.getNodeString();
    }

    /**
     * 
     * @return
     */
    public String getLeftChildString() {
        if(leftChild == null)
            return null;
        return leftChild.getNodeString();
    }

    /**
     * 
     * @return
     */
    public String getNextSiblingString() {
        if(nextSibling == null)
            return null;
        return nextSibling.getNodeString();
    }

    /**
     * 
     * @return
     */
    public byte getLevel() {
        return level;
    }

    /**
     * 
     * @return
     */
    public byte getScore() {
        return score;
    }

    /**
     * 
     */
    public String toString() {
        return "Node - " + getNodeString() +
                "\nLevel - " + level + 
                "\nScore - " + score +
                "\nParent - " + getParentString() +
                "\nLeft Child - " + getLeftChildString() +
                "\nNext Sibling - " + getNextSiblingString();
    }
}