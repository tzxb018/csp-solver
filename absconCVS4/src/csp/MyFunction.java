package csp;

import java.util.ArrayList;
import java.util.Objects;

import abscon.instance.components.PFunction;

public class MyFunction {

    protected String[] formalParameters;
    protected String name;
    protected String functionalExpression;
    protected String[] universalPostFixExpression;

    protected PFunction refFunction;

    public MyFunction(PFunction refFunction) {
        this.refFunction = refFunction;

        this.name = refFunction.getName();

        // parsing the toString method of PFunction's toString() method
        functionalExpression = refFunction.toString();
        functionalExpression = functionalExpression.substring(functionalExpression.indexOf("= ") + 2,
                functionalExpression.indexOf(" and (universal)"));

        this.formalParameters = refFunction.getFormalParameters();
        this.universalPostFixExpression = refFunction.getUniversalPostfixExpression();
    }

    @Override
    public String toString() {
        String s = this.functionalExpression;

        return s;

    }

}