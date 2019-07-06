package tc.brace;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to expand bash/csh brace-option expressions.
 */
public class BraceExpander {
    /**
     * Expands PATTERN into a list of strings.
     * @param pattern A string containing {} options.
     * @return A list of strings.
     */
    public static List<String> expand(String pattern) {
        LinkedList<ParseNode> parseNodes = ParseNode.parse(pattern);
        List<String> result = new LinkedList<>();
        expandHelper(parseNodes, "", result);
        return result;
    }

    private static void expandHelper(LinkedList<ParseNode> inputNodes, String accum, List<String> result) {
        if (inputNodes.size() == 0) {
            result.add(accum);
        } else {
            LinkedList<ParseNode> nodes = new LinkedList<>();
            nodes.addAll(inputNodes);
            ParseNode first = nodes.removeFirst();
            for (String var : variations(first)) {
                 expandHelper(nodes, accum + var, result);
            }
        }
    }

    private static List<String> variations(ParseNode node) {
        List<String> result = new LinkedList<>();
        if (node.isAtom()) {
            result.add(node.getAtom().toString());
        } else {
            for (LinkedList<ParseNode> choice: node.getChoices()) {
                expandHelper(choice, "", result);
            }
        }
        return result;
    }
}
