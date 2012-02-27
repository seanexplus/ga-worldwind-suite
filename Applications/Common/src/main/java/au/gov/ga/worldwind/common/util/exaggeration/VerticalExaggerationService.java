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
package au.gov.ga.worldwind.common.util.exaggeration;

import gov.nasa.worldwind.render.DrawContext;

/**
 * An interface for services that can apply vertical exaggeration to an elevation value
 * 
 * @author James Navin (james.navin@ga.gov.au)
 */
public interface VerticalExaggerationService
{

	/** 
	 * @return The exaggerated elevation value 
	 */
	double applyVerticalExaggeration(DrawContext dc, double elevation);
	
	/** 
	 * @return The global vertical exaggeration value 
	 */
	double getGlobalVerticalExaggeration(DrawContext dc);
}
