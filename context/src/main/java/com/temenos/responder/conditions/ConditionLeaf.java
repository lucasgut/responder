package com.temenos.responder.conditions;

/**
 * Created by aburgos on 05/01/2017.
 */
public class ConditionLeaf<T> implements Condition {

    private BooleanBinaryOperator<T> operator;
    private T leftHand;
    private T rightHand;

    public ConditionLeaf(T leftHand, BooleanBinaryOperator<T> operator, T rightHand) {
        this.leftHand = leftHand;
        this.operator = operator;
        this.rightHand = rightHand;
    }

    @Override
    public boolean evaluate() {
        return operator.apply(leftHand, rightHand);
    }
}
