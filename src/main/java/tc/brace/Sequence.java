package tc.brace;

import java.util.LinkedList;

/**
 * A sequence of substrings
 */
class Sequence {
    Sequence() {
        // do nothing
    }

    Sequence(Sequence original) {
        rep.addAll(original.rep);
    }

    ParseNode car() {
        return rep.getFirst();
    }

    Sequence cdr() {
        Sequence result = new Sequence(this);
        result.rep.removeFirst();
        return result;
    }

    int size() {
        return rep.size();
    }

    /**
     * Adds a character to the sequence.
     * @param ch the character to be added.
     */
    void addChar(Character ch) {
        ParseNode token;
        if (rep.size() == 0) {
            token = new ParseNode();
            rep.add(token);
        } else {
            token = rep.getLast();
            if (! token.isAtom()) {
                token = new ParseNode();
                rep.add(token);
            }
        }
        token.addChar(ch);
    }

    /**
     * Adds a choice to the sequence.
     * @param choice the choice to be added.
     */
    void addChoice(ParseNode choice) {
        rep.add(choice);
    }

    private LinkedList<ParseNode> rep = new LinkedList<>();
}
