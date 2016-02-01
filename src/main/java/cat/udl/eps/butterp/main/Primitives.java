package cat.udl.eps.butterp.main;

import cat.udl.eps.butterp.data.*;
import cat.udl.eps.butterp.data.Integer;
import cat.udl.eps.butterp.environment.Environment;

public class Primitives {

    public static void loadPrimitives(Environment env) {
        env.bindGlobal(Symbol.NIL, Symbol.NIL);
        env.bindGlobal(Symbol.TRUE, Symbol.TRUE);

        env.bindGlobal(new Symbol("add"), new Function() {
            @Override
            public SExpression apply(SExpression evargs, Environment env) {
                if (evargs.equals(Symbol.NIL)) {
                    return new Integer(0);
                }

                ConsCell consCell = (ConsCell) evargs;
                SExpression value = consCell.car;
                return sum(value, apply(consCell.cdr, env), env);
            }

            private SExpression sum(SExpression x, SExpression y, Environment env) {
                int xInt = castInteger(x, env);
                int yInt = castInteger(y, env);
                return new Integer(xInt + yInt);
            }

            private int castInteger(SExpression sExpression, Environment env) {
                if (!(sExpression instanceof Integer)) {
                    throw new EvaluationError("NotInteger");
                }
                return ((Integer) sExpression).value;
            }
        });

        env.bindGlobal(new Symbol("mult"), new Function() {
            @Override
            public SExpression apply(SExpression evargs, Environment env) {
                if (evargs.equals(Symbol.NIL)) {
                    return new Integer(1);
                }

                ConsCell consCell = (ConsCell) evargs;
                SExpression value = consCell.car;
                return mult(value, apply(consCell.cdr, env), env);
            }

            private SExpression mult(SExpression x, SExpression y, Environment env) {
                int xInt = castInteger(x, env);
                int yInt = castInteger(y, env);
                return new Integer(xInt * yInt);
            }

            private int castInteger(SExpression sExpression, Environment env) {
                if (!(sExpression instanceof Integer)) {
                    throw new EvaluationError("NotInteger");
                }
                return ((Integer) sExpression).value;
            }
        });

        env.bindGlobal(new Symbol("define"), new Special() {
            @Override
            public SExpression applySpecial(SExpression evargs, Environment env) {
                if (ListOps.length(evargs) != 2) {
                    throw new EvaluationError("WrongNumberOfArguments");
                }

                ConsCell consCell = (ConsCell) evargs;
                Symbol key = castSymbol(consCell.car);
                SExpression value = ((ConsCell)consCell.cdr).car.eval(env);

                //save definition
                env.bindGlobal(key, value);

                return Symbol.NIL;
            }

            private Symbol castSymbol(SExpression sExpression) {
                if (!(sExpression instanceof Symbol)) {
                    throw new EvaluationError("NotSymbol");
                }

                return (Symbol) sExpression;
            }
        });

        /*

        An example of a predefined Function:

        env.bindGlobal(new Symbol("function"), new Function() {
            @Override
            public SExpression apply(SExpression evargs, Environment env) {
                throw new UnsupportedOperationException("not implemented yet");
            }
        });

        */

        /*

        An example of a predefined Special:

        env.bindGlobal(new Symbol("special"), new Special() {
            @Override
            public SExpression applySpecial(SExpression args, Environment env) {
                throw new UnsupportedOperationException("not implemented yet");
            }
        });

        */

    }
}
