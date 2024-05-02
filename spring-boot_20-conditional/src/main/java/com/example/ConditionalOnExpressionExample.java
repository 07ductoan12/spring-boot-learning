package com.example;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;

/** ConditionalOnExpressionExample */
@Configuration
@ConditionalOnExpression("${toan.expression1.enable:true} and ${toan.expression2.enable:true}")
public class ConditionalOnExpressionExample {}
