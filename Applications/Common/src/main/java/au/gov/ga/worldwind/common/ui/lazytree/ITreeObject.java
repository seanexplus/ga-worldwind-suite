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
package au.gov.ga.worldwind.common.ui.lazytree;

import javax.swing.tree.MutableTreeNode;

/**
 * Represents any object that can be displayed in the {@link LazyTree}.
 * 
 * @author Michael de Hoog (michael.dehoog@ga.gov.au)
 */
public interface ITreeObject
{
	/**
	 * @param model
	 * @return The list of child tree nodes of this object.
	 */
	public MutableTreeNode[] getChildren(LazyTreeModel model);
}
