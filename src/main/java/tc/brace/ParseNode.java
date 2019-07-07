package tc.brace;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A node in the parse tree for a brace expansion pattern.
 */
class ParseNode {
    /**
     * Parses the PATTERN into a collection of CharSequences,
     * @param pattern a string to be expanded.
     * @return the parsed sequence.
     */
    static Sequence parse(CharSequence pattern) throws RuntimeException {
        Sequence result = new Sequence();
        int L = pattern.length();
        if (L > 0) {
            Deque<StackNode> stack = new LinkedList<>();
            Sequence currentSequence = result;
            ParseNode currentChoice = null;
            boolean literal = false;
            for (int i = 0; i < L; ++i) {
                Character ch = pattern.charAt(i);
                if (literal) {
                    currentSequence.addChar(ch);
                    literal = false;
                    continue;
                }
                switch (ch) {
                    case '{':
                        stack.push(new StackNode(currentSequence, currentChoice));
                        currentChoice = new ParseNode();
                        currentSequence.addChoice(currentChoice);
                        // Start a new sequence one level down.
                        currentSequence = new Sequence();
                        currentChoice.addSequence(currentSequence);
                        break;
                    case '}':
                        if (stack.isEmpty()) {
                            throw new RuntimeException("invalid close-brace");
                        }
                        StackNode n = stack.pop();
                        currentSequence = n.getSequence();
                        currentChoice = n.getChoice();
                        break;
                    case ',':
                        if (currentChoice == null) {
                            currentSequence.addChar(ch);
                        } else {
                            currentSequence = new Sequence();
                            currentChoice.addSequence(currentSequence);
                        }
                        break;
                    case '\\':
                        literal = true;
                        continue;
                    default:
                        currentSequence.addChar(ch);
                        break;
                }
            }
            if (literal) {
                throw new RuntimeException("character required after escape");
            }
            if (!stack.isEmpty()) {
                throw new RuntimeException("unbalanced braces");
            }
        }
        return result;
    }
    /**
     * Returns the choices represented by this ParseNode.
     * @return a List of LinkedList of ParsxeNodes.
     */
    List<Sequence> getChoices() {
        return choices;
    }

    /**
     * Returns the atom value of this ParseNOde
     * @return returns the atom StringBuffer.
     */
    StringBuffer getAtom() {
        return atom;
    }

    /**
     * Tests if this ParseNode is an Atom and not a Choice.
     * @return true if the ParseNode is an Atom.
     */
    boolean isAtom() {
        return choices == null;
    }

    /**
     * Adds a character to the atom value of this ParseNode.
     * @param ch the character to add to the ParseNode.
     */
    void addChar(Character ch) {
        if (atom == null) {
            atom = new StringBuffer();
        }
        atom.append(ch);
    }

    /**
     * Adds a sequence to this ParseNode.
     * @param seq the sequence to be added.
     */
    private void addSequence(Sequence seq) {
        assert atom == null;
        if (choices == null) {
            choices = new LinkedList<>();
        }
        choices.add(seq);
    }

    private List<Sequence> choices = null;
    private StringBuffer atom = null;
}
