package tc.brace;

import java.util.LinkedList;

public class StackNode {
    LinkedList<ParseNode> sequence = null;
    ParseNode choice = null;
    StackNode(LinkedList<ParseNode> seq, ParseNode alt) {
        sequence = seq;
        choice = alt;
    }
}
