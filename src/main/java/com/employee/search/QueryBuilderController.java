package com.employee.search;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping(value = "/query/builder")
public class QueryBuilderController{

    @Autowired
    QueryBuilder queryBuilder;


    @RequestMapping(path="/validate" ,method = RequestMethod.GET)
    public Map<String, Object > getExpressionTrue(@RequestParam(value = "query", required = true) String query){

    return queryBuilder.getExpressionTree(query);

    }






}
