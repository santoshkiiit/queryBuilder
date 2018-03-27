package com.employee.search;


import java.io.Serializable;

public class ExpressionNode implements Serializable
{
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ExpressionNode getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(ExpressionNode leftOperand) {
        this.leftOperand = leftOperand;
    }

    public ExpressionNode getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(ExpressionNode rightOperand) {
        this.rightOperand = rightOperand;
    }

    private Object value;
    private ExpressionNode leftOperand;
    private ExpressionNode rightOperand;


//    public ExpressionNode(Object value, ExpressionNode leftOperand, ExpressionNode rightOperand)
//    {
//        this.value = value;
//        this.leftOperand = leftOperand;
//        this.rightOperand = rightOperand;
//    }

    public ExpressionNode(Object value){
        this.value = value;
    }

    public ExpressionNode(){

    }




    public ExpressionNode(String operator, Object operand1, Object operand2) {
        this.value=operator;
        this.leftOperand = new ExpressionNode(operand1);
        this.rightOperand = new ExpressionNode(operand2);
    }




}