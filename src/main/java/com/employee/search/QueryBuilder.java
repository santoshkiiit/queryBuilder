package com.employee.search;



import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Created by santoshk on 3/19/18.
 */
@Component
public class QueryBuilder {


    Stack expressionStack = new Stack();

    static final String AND_JOIN ="AND";

    static final String OR_JOIN ="OR";

    static final String IN_JOIN ="IN";

    static final String startTag ="(";


    static final String endTag =")";

    int bracketsCount =0;

    Logger logger = Logger.getLogger(QueryBuilder.class.getName());

    String resultMessage = "";



    public Map<String, Object> getExpressionTree(String query){
        Map<String, Object> validationResult = new HashMap<>();


        if(validateAndBuildExpressionTree(query)){
            ExpressionNode expressionNode = (ExpressionNode)expressionStack.pop();
            validationResult.put("resultStatus","Success");
            validationResult.put("expressionNode",expressionNode);
        }else{
            validationResult.put("resultStatus",resultMessage);

        }

        return validationResult;
    }

    private boolean validateAndBuildExpressionTree(String query) {

        if(query==null || query.isEmpty()){
            logger.warning("query is empty");
            resultMessage ="query is empty";
            return false;
        }

        String token="";
        for(int i=0;i < query.length();i++){
            char nextChar = query.charAt(i);
            if(nextChar==' '){
                if(!token.isEmpty()){
                    //to handle extra spaces in query

                    if(!validateAndInsertToken(token)){
                        return false;
                    }
                    token ="";
                }
            }else{
                token = token+query.charAt(i);
            }
        }

        if(!validateAndInsertToken(token)){
            return false;
        }
        if(bracketsCount > 0){
            logger.warning("invalid query, opening tag not closed");
            resultMessage ="invalid query, opening tag not close";
            return false;
        }
        if(expressionStack.size()>2){
            evaluateExpressionStack();
        }
        if(expressionStack.size()==1){
            // if expression has only only operand (A)
            if(!(expressionStack.peek() instanceof ExpressionNode)){
                ExpressionNode rootNode = new ExpressionNode(expressionStack.pop());
                expressionStack.push(rootNode);

            }
        }
        if(expressionStack.size()==0){

        }
        return true;
    }

    private boolean validateAndInsertToken(String token) {


        if(isOperator(token)) {
            return insertOperator(token);
            }
         else if(isOperand(token)){
            return insertOperand(token);
            }
        else  if(token.equals(startTag)){
            return insertStartTag(token);
        }
        else if(token.equals(endTag)){
            bracketsCount--;
            if(bracketsCount<0){
                logger.warning("invalid query,closing tag at incorrect location");
                resultMessage ="invalid query,closing tag at incorrect location";
                return false;
            }
            if(isOperator((String) expressionStack.peek())){
                logger.warning("invalid query,close tag after operator");
                resultMessage ="invalid query,close tag after operator";

                return false;
            }
          return insertExpression();
        }

        return true;
    }

    private boolean insertStartTag(String token) {
        bracketsCount ++;
        if(expressionStack.size()==0){
            expressionStack.push(token);
        }else{
            Object topElement = expressionStack.peek();
            if(topElement instanceof ExpressionNode || isOperand((String)topElement)){
                logger.warning("Invalid query, no connecting word between inner queries");
                resultMessage ="Invalid query, no connecting word between inner queries";
                return false;
            }else{
                expressionStack.push(token);
            }
        }
        return true;
    }

    private boolean insertOperand(String token) {

        if (expressionStack.size() > 0 && isOperand((String) expressionStack.peek())) {
            logger.warning("Invalid query, two operands in sequence");
            resultMessage = "Invalid query, two operands in sequence";
            return false;
        } else {
            expressionStack.push(token);
        }
        return true;
    }

    private boolean insertOperator(String token) {

            if(expressionStack.size()==0){
                logger.warning("Invalid query, can't start query with an operator");
                resultMessage = "Invalid query, can't start query with an operator";
                return false;

            }else{
                Object topElement = (Object) expressionStack.peek();
                if(topElement instanceof ExpressionNode){
                    //element on top of stack is a query expression
                    expressionStack.push(token);
                }
                else if(isOperator((String) topElement)){
                    logger.warning("Invalid query, two operators in sequence");
                    resultMessage = "Invalid query, two operators in sequence";
                    return false;
                }else{
                    expressionStack.push(token);
                }
            }
        return true;
    }

    private  boolean insertExpression() {

        if(expressionStack.size()==0){
            logger.warning("Invalid query, cannot start query with end tag");
            resultMessage="Invalid query, cannot start query with end tag";
            return false;

        }

        if (expressionStack.size() > 2) {
            Object operand1 = expressionStack.pop();
            String operator = (String) expressionStack.pop();
            Object operand2 = expressionStack.pop();
            ExpressionNode rootNode = new ExpressionNode(operator, operand2, operand1);

            if (!expressionStack.peek().equals(startTag)) {
                expressionStack.push(rootNode);
                if(expressionStack.size()>2){
                    insertExpression();
                    //inner query has more conditions to evaluate
                }
            }else{
                expressionStack.pop();
                expressionStack.push(rootNode);

            }
        }
        return true;
    }

    private ExpressionNode evaluateExpressionStack(){
        Object operand1 = expressionStack.pop();
        String operator = (String) expressionStack.pop();
        Object operand2 = expressionStack.pop();
        ExpressionNode rootNode = new ExpressionNode(operator, operand2, operand1);
        expressionStack.push(rootNode);
        if(expressionStack.size()>2){
            evaluateExpressionStack();
        }
        return rootNode;


    }

    private boolean isOperand(String token) {
        if(isOperator(token)){
            return false;
        }
        if(token.equals(startTag)||token.equals(endTag)){
            return false;

        }
        return true;
    }

    private boolean isOperator(String token) {

        if(token.equals(AND_JOIN) || token.equals(OR_JOIN) || token.equals(IN_JOIN)){
            return true;
        }
        return false;
    }



}
