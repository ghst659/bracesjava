package tc.brace;

import java.util.List;

public class Expander {
    /**
     * An Element can be either a text or a choice of Sequences.
     */
    public static class Element {
        public boolean isChoice() {
            return false;
        }
        public List<Sequence> choices() {
            return null;
        }
        public String text() {
            return null;
        }
    }
    public static class Sequence {

    }
}
