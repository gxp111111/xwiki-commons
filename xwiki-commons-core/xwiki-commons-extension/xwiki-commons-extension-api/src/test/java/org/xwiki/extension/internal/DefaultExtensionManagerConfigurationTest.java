/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.extension.internal;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Assert;

import org.jmock.Expectations;
import org.junit.Test;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.environment.Environment;
import org.xwiki.extension.ExtensionManagerConfiguration;
import org.xwiki.extension.repository.DefaultExtensionRepositoryDescriptor;
import org.xwiki.extension.repository.ExtensionRepositoryDescriptor;
import org.xwiki.logging.LoggerManager;
import org.xwiki.test.AbstractComponentTestCase;

public class DefaultExtensionManagerConfigurationTest extends AbstractComponentTestCase
{
    private ExtensionManagerConfiguration configuration;

    @Override
    public void setUp() throws Exception
    {
        super.setUp();

        // Register a Mocked Environment since we need to provide one.
        final Environment environment = registerMockComponent(Environment.class);

        getMockery().checking(new Expectations()
        {
            {
                allowing(environment).getPermanentDirectory();
                will(returnValue(null));
                allowing(environment).getResourceAsStream(with(any(String.class)));
                will(returnValue(null));
            }
        });

        this.configuration = getComponentManager().getInstance(ExtensionManagerConfiguration.class);
    }

    @Test
    public void testGetRepositoriesWithInvalid() throws ComponentLookupException, Exception
    {
        // Expect warnings to be logged
        LoggerManager logManager = getComponentManager().getInstance(LoggerManager.class);
        logManager.pushLogListener(null);

        getConfigurationSource().setProperty("extension.repositories", Arrays.asList("id:type:http://url", "invalid"));

        Assert.assertEquals(
            Arrays.asList(new DefaultExtensionRepositoryDescriptor("id", "type", new URI("http://url"))),
            new ArrayList<ExtensionRepositoryDescriptor>(this.configuration.getExtensionRepositoryDescriptors()));
    }

    @Test
    public void testGetExtensionRepositoryDescriptorsEmpty()
    {
        Assert.assertEquals(null, this.configuration.getExtensionRepositoryDescriptors());
    }
}
