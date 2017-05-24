package com.fa.gateway.cucumber.stepdefs;

import com.fa.gateway.FoodAppetencyApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = FoodAppetencyApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
