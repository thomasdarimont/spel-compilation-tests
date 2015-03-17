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
 * <pre>java -jar target\benchmarks.jar -f 1 -i 10 -wi 10 .*Full.*</pre>
 *
 * @author Thomas Darimont
 */
@State(Scope.Benchmark)
public class FullSpelBenchmark {

    private static final A a = new A();

    private static final String expression = "b.c.d.someMethod()";

    private static final Expression EXPRESSION_1_SPEL_COMPILATION_OFF = parse(expression, SpelCompilerMode.OFF);
    private static final Expression EXPRESSION_1_SPEL_COMPILATION_MIXED = parse(expression, SpelCompilerMode.MIXED);
    private static final Expression EXPRESSION_1_SPEL_COMPILATION_FULL = parse(expression, SpelCompilerMode.IMMEDIATE);

    @Benchmark
    public void expression1_1_plainJava(Blackhole bh) {
        bh.consume(a.getB().getC().getD().someMethod());
    }

    @Benchmark
    public void expression1_2_spelCompilationOff(Blackhole bh) {
        bh.consume(EXPRESSION_1_SPEL_COMPILATION_OFF.getValue(a));
    }

    @Benchmark
    public void expression1_3_spelCompilation_mixed(Blackhole bh) {
        bh.consume(EXPRESSION_1_SPEL_COMPILATION_MIXED.getValue(a));
    }

    @Benchmark
    public void expression1_4_spelCompilation_full(Blackhole bh) {
        bh.consume(EXPRESSION_1_SPEL_COMPILATION_FULL.getValue(a));
    }

    private static Expression parse(String expression, SpelCompilerMode compilerMode) {
        SpelParserConfiguration config = new SpelParserConfiguration(compilerMode, FullSpelBenchmark.class.getClassLoader());
        SpelExpressionParser parser = new SpelExpressionParser(config);
        return parser.parseExpression(expression);
    }
}
