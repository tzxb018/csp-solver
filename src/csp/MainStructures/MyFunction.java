package csp.MainStructures;

import abscon.instance.components.PFunction;

/**
 * 
 * @author Tomo Bessho
 * @version HW 2
 * @since 2/11/2020
 */

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