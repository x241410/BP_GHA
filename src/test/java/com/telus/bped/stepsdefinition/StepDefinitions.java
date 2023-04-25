package com.telus.bped.stepsdefinition;

import org.json.JSONObject;

import com.telus.bped.utils.GenericUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.Status;
import com.test.utils.SystemProperties;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;

/**
 * ***************************************************************************
 * DESCRIPTION: This class contains the steps implementations for the BPED
 * application smoke tests AUTHOR: x241410
 * ***************************************************************************
 */

public class StepDefinitions extends BaseTest {

	String testCaseDescription = null;
	String environment = null;
	String dataFilePath = null;
	JSONObject subDataJson = null;
	JSONObject testDataJson = null;
	JSONObject commonDataJson = null;
	String BlifPhoneNumber = null;
	String vpopOrderTrackNumber = null;

	private Scenario scenario = null;

	@Before
	public void before(Scenario scenario) {
		this.scenario = scenario;
	}

	@Given("test data configuration for {string}")
	public void test_data_configuration_for(String scriptName) {

		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");

	}

	@Given("user login into {string}")
	public void user_login_into(String applicationName) {

		WebDriverSteps.getWebDriverSession().get("https://www.google.com/");
	}

}
