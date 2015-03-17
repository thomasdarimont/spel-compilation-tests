package spel.bench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.script.*;

/**
 * Build project:
 * <pre>mvn package</pre>
 * <p/>
 * Run benchmark:
 * <pre>java -jar target\benchmarks.jar -f 1 -i 10 -wi 10 .*InstanceCreation.*</pre>
 *
 * @author Thomas Darimont
 */
@State(Scope.Benchmark)
public class InstanceCreationExample {

    private static final Object[] NO_ARGS = new Object[0];
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private static final SpelExpressionParser COMPILING_PARSER = new SpelExpressionParser(new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, InstanceCreationExample.class.getClassLoader()));

    private static final Class<?> TEST_OBJECT_CLASS = TestObjects.Foo.class;
    private static final String SPEL_EXPRESSION_STRING = "new " + TEST_OBJECT_CLASS.getName() + "()";
    private static final Expression SPEL_NEW_FOO = PARSER.parseExpression(SPEL_EXPRESSION_STRING);
    private static final Expression SPEL_NEW_FOO_COMPILED = COMPILING_PARSER.parseExpression(SPEL_EXPRESSION_STRING);

    private static final Invocable NASHORN_NEW_FOO_INVOCABLE;
    private static final CompiledScript NASHORN_NEW_FOO_COMPILED;

    static {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

        try {
            engine.eval("var Obj = Java.type(\"" + TEST_OBJECT_CLASS.getName() + "\");");
            engine.eval("function create() { return new Obj();}");

            NASHORN_NEW_FOO_INVOCABLE = (Invocable) engine;
            NASHORN_NEW_FOO_COMPILED = (CompiledScript) ((Compilable) engine).compile("new Obj();");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void run_1_newInstanceViaPlainJava(Blackhole bh) {
        bh.consume(new TestObjects.Foo());
    }

    @Benchmark
    public void run_2_newInstanceViaReflection(Blackhole bh) throws Exception {
        bh.consume(TEST_OBJECT_CLASS.newInstance());
    }

    @Benchmark
    public void run_3_newInstanceViaSpelInterpreted(Blackhole bh) throws Exception {
        bh.consume(SPEL_NEW_FOO.getValue());
    }

    @Benchmark
    public void run_4_newInstanceViaSpelCompiled(Blackhole bh) throws Exception {
        bh.consume(SPEL_NEW_FOO_COMPILED.getValue());
    }

    @Benchmark
    public void run_5_newInstanceViaNashornInvocable(Blackhole bh) throws Exception {
        bh.consume(NASHORN_NEW_FOO_INVOCABLE.invokeFunction("create", NO_ARGS));
    }

    @Benchmark
    public void run_6_newInstanceViaNashornCompiledScript(Blackhole bh) throws Exception {
        bh.consume(NASHORN_NEW_FOO_COMPILED.eval());
    }
}
