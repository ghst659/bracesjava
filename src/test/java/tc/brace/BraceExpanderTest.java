package tc.brace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class BraceExpanderTest {

    @Test public void testExcept() {
        Assertions.assertThrows(java.lang.RuntimeException.class,
                () -> {
                    List<String> notUsed = BraceExpander.expand("foo}");
                });
    }

    public static Stream<Arguments> dataProvider() {
        return Stream.of(
                arguments("", List.of("")),
                arguments("abc", List.of("abc")),
                arguments("a,c", List.of("a,c")),
                arguments("{xy}", List.of("xy")),
                arguments("{x,y}", List.of("x", "y")),
                arguments("x{y,z}", List.of("xy", "xz")),
                arguments("{px,qy,rz}a", List.of("pxa", "qya", "rza")),
                arguments("{x,y}v{p,q}", List.of("xvp", "xvq", "yvp", "yvq")),
                arguments("p{q{r,s},t}", List.of("pqr","pqs", "pt")),
                arguments("{{r,s}{v,w},t}a", List.of("rva","rwa","sva","swa","ta"))
                );
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void check(String pattern, List<String> expected) {
        List<String> found = BraceExpander.expand(pattern);
        String msg = String.format("Pattern: \"%s\"", pattern);
        Assertions.assertEquals(expected, found, msg);
    }
}
