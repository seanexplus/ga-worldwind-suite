/*******************************************************************************
 * Copyright 2012 Geoscience Australia
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package au.gov.ga.worldwind.tiler.util;

import java.util.logging.Logger;

/**
 * Simple abstract implementation of the {@link ProgressReporter} interface
 * containing common code.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public abstract class ProgressReporterImpl implements ProgressReporter
{
	private boolean cancelled = false;
	private final Logger logger;

	public ProgressReporterImpl(Logger logger)
	{
		this.logger = logger;
	}

	public void cancel()
	{
		cancelled = true;
	}

	public Logger getLogger()
	{
		return logger;
	}

	public boolean isCancelled()
	{
		return cancelled;
	}
}
