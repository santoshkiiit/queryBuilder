# queryBuilder
A simple Rest Api to validate  sql query and return tree representation of the query.

Implemented using

Java 1.8 & Spring boot




 URL : /query/builder/validate   , requestType: GET
 Returns validation message and expressiontree if input is a valid query.
 
 
   
1)Input :{ "query":"( (A) OR (B) )"}

Output : 
{"resultStatus":"Success",
"expressionNode":{"value":"OR","leftOperand":{"value":"\"A","leftOperand":null,"rightOperand":null},
"rightOperand":{"value":"B\"","leftOperand":null,"rightOperand":null}}}



2)Input :{ "query":"( (A) OR (B) ) )"}
Output : {"resultStatus":"invalid query,closing tag at incorrect location"}


3)Input : { "query":"( (A) (B) ) )"}
Output : {"resultStatus":"Invalid query, two operands in sequence"}


