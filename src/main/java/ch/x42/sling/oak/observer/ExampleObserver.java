/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ch.x42.sling.oak.observer;

import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.spi.commit.CommitInfo;
import org.apache.jackrabbit.oak.spi.commit.Observer;
import org.apache.jackrabbit.oak.spi.state.NodeState;
import org.apache.jackrabbit.oak.spi.state.NodeStateDiff;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Activating this in a Sling instance works, the Observer is called with
 *  changes to the content repository. However, only the childNodeChanged method
 *  is called when adding, modifying or removing nodes and properties using POST 
 *  requests that trigger the SlingPostServlet.
 */
@Component(service=Observer.class)
public class ExampleObserver implements Observer, NodeStateDiff {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private NodeState previous = null;
    
    @Override
    public void contentChanged(NodeState ns, CommitInfo ci) {
        if(previous != null) {
            ns.compareAgainstBaseState(previous, this);
        }
        previous = ns;
    }

    @Override
    public boolean propertyAdded(PropertyState after) {
        log.info("PROPERTY_ADDED: {}", after.getName());
        return true;
    }

    @Override
    public boolean propertyChanged(PropertyState before, PropertyState after) {
        log.info("PROPERTY CHANGED: {}", after.getName());
        return true;
    }

    @Override
    public boolean propertyDeleted(PropertyState before) {
        log.info("PROPERTY DELETED: {}", before.getName());
        return true;
    }

    @Override
    public boolean childNodeAdded(String name, NodeState after) {
        log.info("CHILD NODE ADDED: {}", name);
        return true;
    }

    @Override
    public boolean childNodeChanged(String name, NodeState before, NodeState after) {
        log.info("CHILD NODE CHANGED: {}", name);
        return true;
    }

    @Override
    public boolean childNodeDeleted(String name, NodeState before) {
        log.info("CHILD NODE DELETED: {}", name);
        return true;
    }

}
