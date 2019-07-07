package tc.brace;

import java.util.LinkedList;

/**
 * A sequence of substrings
 */
class Sequence extends LinkedList<ParseNode> {
    /**
     * Adds a character to the sequence.
     * @param ch the character to be added.
     */
    void addChar(Character ch) {
        ParseNode token;
        if (this.size() == 0) {
            token = new ParseNode();
            this.add(token);
        } else {
            token = this.getLast();
            if (! token.isAtom()) {
                token = new ParseNode();
                this.add(token);
            }
        }
        token.addChar(ch);
    }

    /**
     * Adds a choice to the sequence.
     * @param choice the choice to be added.
     */
    void addChoice(ParseNode choice) {
        this.add(choice);
    }
}
