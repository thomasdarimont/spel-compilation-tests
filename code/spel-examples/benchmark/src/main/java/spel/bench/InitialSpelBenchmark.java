package spel.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Build project:
 * <pre>mvn package</pre>
 * <p/>
 * Run benchmark:
 * <pre>java -jar target\benchmarks.jar -f 1 -i 10 -wi 10 .*Initial.*</pre>
 *
 * @author Thomas Darimont
 */
@State(Scope.Benchmark)
public class InitialSpelBenchmark {

    private static final A a = new A();

    private static final String expression = "b.c.d.someMethod()";

    private static final Expression EXPRESSION_1_SPEL_COMPILATION_OFF =
            parse(expression, SpelCompilerMode.OFF);

    @Benchmark
    public void expression1_1_plainJava(Blackhole bh) {
        bh.consume(a.getB().getC().getD().someMethod());
    }

    @Benchmark
    public void expression1_2_spelCompilationOff(Blackhole bh) {
        bh.consume(EXPRESSION_1_SPEL_COMPILATION_OFF.getValue(a));
    }

    private static Expression parse(String expression, SpelCompilerMode compilerMode) {
        SpelParserConfiguration config =
                new SpelParserConfiguration(compilerMode, InitialSpelBenchmark.class.getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);
        return parser.parseExpression(expression);
    }
}
