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
package au.gov.ga.worldwind.common.util;

import java.io.InputStream;

/**
 * Helper class that reads from an {@link InputStream} on a separate thread,
 * discarding all data.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public class InputStreamGobbler
{
	/**
	 * Create a daemon thread that reads and discards all data from the given
	 * {@link InputStream}.
	 * 
	 * @param is
	 */
	public InputStreamGobbler(final InputStream is)
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				byte[] text = new byte[1024];
				try
				{
					while (is.read(text) >= 0)
						;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}
}
