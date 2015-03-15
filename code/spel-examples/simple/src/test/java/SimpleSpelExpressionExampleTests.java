import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SimpleSpelExpressionExampleTests {

    /**
     * @see EXAMPLE-0
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

    /**
     * @see EXAMPLE-1
     */
    @Test
    public void parseAndExecuteSpelExpressionWithVariables() {

        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, getClass().getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("dobTesla", LocalDate.of(1856, 6, 10));

        Expression expression = parser.parseExpression("getYear() - #dobTesla.getYear()");

        Integer result = expression.getValue(context, LocalDate.of(2015, 3, 15), Integer.class);

        //trigger compilation
        result = expression.getValue(context, LocalDate.of(2015, 3, 15), Integer.class);
        assertThat(result, is(159));
    }
}
