import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author Thomas Darimont
 */
public class DumpGeneratedJavaClassExample {

    public static void main(String[] args) {

        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, DumpGeneratedJavaClassExample.class.getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);
        Expression expression = parser.parseExpression("toUpperCase()");

        expression.getValue("Nikola Tesla");

        /*
         * 1. Set breakpoint in org.springframework.expression.spel.standard.SpelCompiler.createExpressionClass
         *  before the return.
         *
         *  2. Issue the following statement via the debugger:
         *  dump(expressionToCompile.toStringAST(), clazzName, data);
         *
         *  3. Look for the generated class in your temp Directory.
         */
        expression.getValue("Nikola Tesla");

        System.out.println(expression);

    }
}
