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
package org.xwiki.extension.jar;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.classloader.ClassLoaderManager;
import org.xwiki.classloader.NamespaceURLClassLoader;
import org.xwiki.component.annotation.Component;
import org.xwiki.context.ExecutionContext;
import org.xwiki.context.ExecutionContextException;
import org.xwiki.context.ExecutionContextInitializer;

@Component
@Singleton
@Named("jarextension")
public class TestExecutionContextInitializer implements ExecutionContextInitializer
{
    public static String currentNamespace;

    @Inject
    private ClassLoaderManager jarExtensionClassLoader;

    @Override
    public void initialize(ExecutionContext context) throws ExecutionContextException
    {
        NamespaceURLClassLoader extensionClassLoader =
            this.jarExtensionClassLoader.getURLClassLoader(currentNamespace != null ? currentNamespace : null, false);

        if (extensionClassLoader != null) {
            Thread.currentThread().setContextClassLoader(extensionClassLoader);
        }
    }
}
