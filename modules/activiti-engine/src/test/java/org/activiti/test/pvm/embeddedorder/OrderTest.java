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
package org.activiti.test.pvm.embeddedorder;

import junit.framework.TestCase;


/**
 * @author Tom Baeyens
 */
public class OrderTest extends TestCase {

  public void testOrder() {
    Order order = new Order("someOrderInitializationParameter");
    assertEquals("verification", order.getState());
    order.verificationComplete();
    assertEquals("inProcess", order.getState());
    order.deliveryAcknowledged();
    assertEquals("archived", order.getState());
  }
}
