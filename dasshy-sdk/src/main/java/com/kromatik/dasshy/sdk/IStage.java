/**
 * Dasshy - Real time and Batch Analytics Open Source System
 * Copyright (C) 2016 Kromatik Solutions (http://kromatiksolutions.com)
 *
 * This file is part of Dasshy
 *
 * Dasshy is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Dasshy is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Dasshy.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.kromatik.dasshy.sdk;

import java.util.List;

/**
 * Stage of the ETL execution
 */
public interface IStage
{
	/**
	 * Initializes the stage at the beginning of its execution
	 *
	 * @param runtimeContext running context
	 * @param configuration stage configuration
	 */
	void init(final RuntimeContext runtimeContext, final StageConfiguration configuration);

	/**
	 * Cleans the stage at the end of its execution
	 *
	 * @param runtimeContext context
	 */
	void clean(final RuntimeContext runtimeContext);

	/**
	 * Returns a list of attribute definitions for the stage
	 *
	 * @return attribute definitions. empty list if none.
	 */
	List<StageAttribute> getAttributeDefinitions();
}
