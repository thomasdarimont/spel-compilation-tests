import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleSpelExpressionExampleTests {

    /**
     * @see EXAMPLE-1
     */
    @Test
    public void parseAndExecuteSimpleSpelExpression() {

        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, SimpleSpelExpressionExampleTests.class.getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("toUpperCase()");

        //Expression will be compiled on next execution
        expression.getValue("Nikola Tesla", String.class);

        String result = expression.getValue("Nikola Tesla", String.class);

        assertThat(result, is("NIKOLA TESLA"));
    }
}
