package tc.brace;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
            for (int i = 0; i < L; i++) {
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
                        if (stack.size() > 0) {
                            StackNode n = stack.pop();
                            currentSequence = n.sequence;
                            currentChoice = n.choice;
                        } else {
                            throw new RuntimeException("invalid close-brace");
                        }
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

    public List<LinkedList<ParseNode>> getChoices() {
        return choices;
    }

    public StringBuffer getAtom() {
        return atom;
    }
    private List<LinkedList<ParseNode>> choices = null;
    private StringBuffer atom = null;
    @Override
    public String toString() {
        return String.format("%s|%s", atom == null ? "-" : atom,
                choices == null ? "-" : choices);
    }
    public boolean isAtom() {
        return choices == null;
    }
    public void addChar(Character ch) {
        if (atom == null) {
            atom = new StringBuffer();
        }
        atom.append(ch);
    }
    public void addSequence(LinkedList<ParseNode> seq) {
        assert atom == null;
        if (choices == null) {
            choices = new LinkedList<>();
        }
        choices.add(seq);
    }

    public ParseNodeVariations variations() {
        return new ParseNodeVariations();
    }

    private class ParseNodeVariations implements Iterator<String> {
        @Override
        public boolean hasNext() {
            if (ParseNode.this.isAtom()) {
                return true;
            } else {
                if (ParseNode.this.getChoices().size() > 0) {
                    return false;
                } else {
                    return false;
                }
            }
        }

        @Override
        public String next() {
            return null;
        }
    }
}
