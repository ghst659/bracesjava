package tc.brace;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A node in the parse tree for a brace expansion pattern.
 */
public class ParseNode {
    /**
     * Parses the PATTERN into a collection of CharSequences,
     * @param pattern
     * @return
     */
    public static LinkedList<ParseNode> parse(CharSequence pattern) throws RuntimeException {
        LinkedList<ParseNode> result = new LinkedList<>();
        int L = pattern.length();
        if (L > 0) {
            Deque<StackNode> stack = new LinkedList<>();
            LinkedList<ParseNode> currentSequence = result;
            ParseNode currentChoice = null;
            for (int i = 0; i < L; ++i) {
                Character ch = pattern.charAt(i);
                switch (ch) {
                    case '{':
                        stack.push(new StackNode(currentSequence, currentChoice));
                        currentChoice = new ParseNode();
                        currentSequence.add(currentChoice);
                        // Start a new sequence one level down.
                        currentSequence = new LinkedList<>();
                        currentChoice.addSequence(currentSequence);
                        break;
                    case '}':
                        if (stack.size() < 0) {
                            throw new RuntimeException("invalid close-brace");
                        }
                        StackNode n = stack.pop();
                        currentSequence = n.getSequence();
                        currentChoice = n.getChoice();
                        break;
                    case ',':
                        if (currentChoice == null) {
                            addCharToSequence(currentSequence, ch);
                        } else {
                            currentSequence = new LinkedList<>();
                            currentChoice.addSequence(currentSequence);
                        }
                        break;
                    default:
                        addCharToSequence(currentSequence, ch);
                        break;
                }
            }
        }
        return result;
    }
    private static void addCharToSequence(LinkedList<ParseNode> sequence, Character ch) {
        ParseNode token = null;
        if (sequence.size() == 0) {
            token = new ParseNode();
            sequence.add(token);
        } else {
            token = sequence.getLast();
            if (! token.isAtom()) {
                token = new ParseNode();
                sequence.add(token);
            }
        }
        token.addChar(ch);
    }

    /**
     * Returns the choices represented by this ParseNode.
     * @return a List of LinkedList of ParsxeNodes.
     */
    public List<LinkedList<ParseNode>> getChoices() {
        return choices;
    }

    /**
     * Returns the atom value of this ParseNOde
     * @return returns the atom StringBuffer.
     */
    public StringBuffer getAtom() {
        return atom;
    }

    /**
     * Tests if this ParseNode is an Atom and not a Choice.
     * @return true if the ParseNode is an Atom.
     */
    public boolean isAtom() {
        return choices == null;
    }

    /**
     * Adds a character to the atom value of this ParseNode.
     * @param ch the character to add to the ParseNode.
     */
    public void addChar(Character ch) {
        if (atom == null) {
            atom = new StringBuffer();
        }
        atom.append(ch);
    }

    /**
     * Adds a sequence to this PraseNode.
     * @param seq the sequence to be added.
     */
    public void addSequence(LinkedList<ParseNode> seq) {
        assert atom == null;
        if (choices == null) {
            choices = new LinkedList<>();
        }
        choices.add(seq);
    }

    /**
     * The string representation fo this ParseNode.
     * @return the string representation of the ParseNode.
     */
    @Override
    public String toString() {
        return String.format("%s|%s", atom == null ? "-" : atom,
                choices == null ? "-" : choices);
    }
    private List<LinkedList<ParseNode>> choices = null;
    private StringBuffer atom = null;
}
