package org.example.java_project_web;

import javax.xml.bind.annotation.XmlElement;


public class MathExample {
    private String expression;

    @XmlElement(name = "expression")
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}