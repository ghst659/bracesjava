package tc.brace;

import java.util.LinkedList;

/**
 * An immutable item in the parsing stack.
 */
class StackNode {
    /**
     * Constructof for the stack item
     * @param seq a LinkedList of ParseNodes to be pushed on the stack.
     * @param alt a ParseNode to be pushed on the stack.
     */
    StackNode(Sequence seq, ParseNode alt) {
        sequence = seq;
        choice = alt;
    }

    /**
     * Gets the sequence item.
     * @return the sequence member.
     */
    Sequence getSequence() {
        return sequence;
    }

    /**
     * Gets the choice member.
     * @return the choice member.
     */
    ParseNode getChoice() {
        return choice;
    }

    private Sequence sequence;
    private ParseNode choice;
}
