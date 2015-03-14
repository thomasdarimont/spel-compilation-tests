import com.google.common.base.Stopwatch;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Thomas Darimont
 */
public class PlainBenchmark {

    public static void main(String[] args) {

        int batchSize = (int) 1e6;

        System.out.printf("Creating %s records%n", batchSize);

        Record[] records = new Record[batchSize];
        for (int i = 0; i < records.length; i++) {
            records[i] = new Record();
        }

        System.out.printf("Done.%n");

        String expression = "uuid.leastSignificantBits > uuid.mostSignificantBits";

        //Change to SpelCompilerMode.OFF to SpelCompilerMode.IMMEDIATE
        evaluate(records, expression, SpelCompilerMode.OFF);

    }

    private static void evaluate(Record[] records, String expressionString, SpelCompilerMode compilerMode) {

        SpelParserConfiguration config = new SpelParserConfiguration(compilerMode, PlainBenchmark.class.getClassLoader());
        ExpressionParser parser = new SpelExpressionParser(config);

        StandardEvaluationContext context = new StandardEvaluationContext();
        Expression expression = parser.parseExpression(expressionString);

        Stopwatch watch = Stopwatch.createUnstarted();

        System.out.printf("Evaluating %s expressions.%n", records.length);

        System.out.println("Evaluating expressions...");

        List<Object> results = new ArrayList<>(records.length);

        watch.start();
        for (int i = 0; i < records.length; i++) {
            results.add(expression.getValue(context, records[i]));
        }
        watch.stop();

        System.out.printf("Done in %sms%n", watch.elapsed(TimeUnit.MILLISECONDS));
    }
}
