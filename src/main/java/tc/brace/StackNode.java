package tc.brace;

import java.util.LinkedList;

/**
 * An item in the parsing stack.
 */
public class StackNode {
    /**
     * Constructof for the stack item
     * @param seq a LinkedList of ParseNodes to be pushed on the stack.
     * @param alt a ParseNode to be pushed on the stack.
     */
    public StackNode(LinkedList<ParseNode> seq, ParseNode alt) {
        sequence = seq;
        choice = alt;
    }
    public ParseNode getChoice() {
        return choice;
    }
    public LinkedList<ParseNode> getSequence() {
        return sequence;
    }
    private LinkedList<ParseNode> sequence = null;
    private ParseNode choice = null;
}
