/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.examples.taskforms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.Task;
import org.activiti.test.ActivitiTestCase;
import org.activiti.test.ProcessDeclared;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TaskFormsTest extends ActivitiTestCase {

  @Before
  public void setUp() throws Exception {
    processEngineBuilder.getIdentityService().saveUser(processEngineBuilder.getIdentityService().newUser("fozzie"));
    processEngineBuilder.getIdentityService().saveGroup(processEngineBuilder.getIdentityService().newGroup("management"));
    processEngineBuilder.getIdentityService().createMembership("fozzie", "management");
  }

  @After
  public void tearDown() throws Exception {
    processEngineBuilder.getIdentityService().deleteGroup("management");
    processEngineBuilder.getIdentityService().deleteUser("fozzie");
  }

  @Test
  @ProcessDeclared(resources = { "VacationRequest.bpmn20.xml", "approve.form", "request.form", "adjustRequest.form" })
  public void testTaskFormsWithVacationRequestProcess() {

    // Get start form
    Object startForm = processEngineBuilder.getProcessService().getStartFormByKey("vacationRequest");
    assertNotNull(startForm);

    // Define variables that would be filled in through the form
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("employeeName", "kermit");
    parameters.put("numberOfDays", "4");
    parameters.put("vacationMotivation", "I'm tired");
    processEngineBuilder.getProcessService().startProcessInstanceByKey("vacationRequest", parameters);

    // Management should now have a task assigned to them
    Task task = processEngineBuilder.getTaskService().createTaskQuery().candidateGroup("management").singleResult();
    assertEquals("Vacation request by kermit", task.getDescription());
    Object taskForm = processEngineBuilder.getTaskService().getTaskForm(task.getId());
    assertNotNull(taskForm);

  }

  @Test
  @ProcessDeclared
  public void testTaskFormUnavailable() {
    assertNull(processEngineBuilder.getProcessService().getStartFormByKey("noStartOrTaskForm"));

    processEngineBuilder.getProcessService().startProcessInstanceByKey("noStartOrTaskForm");
    Task task = processEngineBuilder.getTaskService().createTaskQuery().singleResult();
    assertNull(processEngineBuilder.getTaskService().getTaskForm(task.getId()));
  }

}
