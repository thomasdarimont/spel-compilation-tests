package spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.CompiledExpression;

import java.time.LocalDate;

public class Ex2
        extends CompiledExpression {
    public Object getValue(Object paramObject, EvaluationContext paramEvaluationContext)
            throws EvaluationException {
        return Integer.valueOf(((LocalDate) paramObject).getYear() - ((LocalDate) paramEvaluationContext.lookupVariable("dobTesla")).getYear());
    }
}
