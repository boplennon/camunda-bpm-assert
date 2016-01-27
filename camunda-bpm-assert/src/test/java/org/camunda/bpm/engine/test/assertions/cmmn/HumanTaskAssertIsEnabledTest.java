package org.camunda.bpm.engine.test.assertions.cmmn;

import static org.camunda.bpm.engine.test.assertions.cmmn.CmmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.cmmn.CmmnAwareTests.caseExecution;
import static org.camunda.bpm.engine.test.assertions.cmmn.CmmnAwareTests.caseService;
import static org.camunda.bpm.engine.test.assertions.cmmn.CmmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.cmmn.CmmnAwareTests.disable;

import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.helpers.Failure;
import org.camunda.bpm.engine.test.assertions.helpers.ProcessAssertTestCase;
import org.junit.Rule;
import org.junit.Test;

public class HumanTaskAssertIsEnabledTest extends ProcessAssertTestCase {

  public static final String TASK_A = "PI_TaskA";
  public static final String TASK_B = "PI_TaskB";
  public static final String CASE_KEY = "Case_HumanTaskAssertIsEnabledTest";

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();

  @Test
  @Deployment(resources = { "cmmn/HumanTaskAssertIsEnabledTest.cmmn" })
  public void testIsEnabled_Success() {
    // Given
    CaseInstance caseInstance = givenCaseIsCreated();
    // When
    complete(caseExecution(TASK_A, caseInstance));
    // Then
    assertThat(caseInstance).humanTask(TASK_B).isEnabled();
  }

  @Test
  @Deployment(resources = { "cmmn/HumanTaskAssertIsEnabledTest.cmmn" })
  public void testIsEnabled_Failure() {
    // Given
    final CaseInstance caseInstance = givenCaseIsCreated();
    // When
    complete(caseExecution(TASK_A, caseInstance));
    disable(caseExecution(TASK_B, caseInstance));
    // Then
    expect(new Failure() {
      @Override
      public void when() {
        assertThat(caseInstance).humanTask(TASK_B).isEnabled();
      }
    });
  }

  private CaseInstance givenCaseIsCreated() {
    return caseService().createCaseInstanceByKey(CASE_KEY);
  }
}