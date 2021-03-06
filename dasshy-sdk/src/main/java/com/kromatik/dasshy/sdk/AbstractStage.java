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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract stage
 */
public abstract class AbstractStage implements IStage
{

	/** attributes */
	private List<StageAttribute> attributeDefinitions = Collections.emptyList();

	/** attribute values */
	private Map<String, String> attributes = new HashMap<>();

	@Override
	public void init(final RuntimeContext runtimeContext, final StageConfiguration configuration)
	{
		if (configuration != null)
		{
			attributes = configuration.getValues();
		}
	}

	@Override
	public void clean(final RuntimeContext runtimeContext)
	{
		// no implementation; override if needed
	}

	@Override
	public List<StageAttribute> getAttributeDefinitions()
	{
		return attributeDefinitions;
	}

	/**
	 * Stage attribute definitions
	 *
	 * @param attributeDefinitions attribute definitions
	 */
	protected final void setAttributeDefinitions(final StageAttribute... attributeDefinitions)
	{
		this.attributeDefinitions = Collections.unmodifiableList(Arrays.asList(attributeDefinitions));
	}

	/**
	 * Return the attribute value as String if it exists, or the given default value if it doesn't
	 *
	 * @param name         attribute name
	 * @param defaultValue default value, if attribute not defined
	 * @return attribute value, or default if not defined
	 */
	protected String getAttribute(final String name, final String defaultValue)
	{
		String value = attributes.get(name);
		return !AttributeUtils.isBlank(value) ? value : defaultValue;
	}

	/**
	 * Return the attribute value as Integer if it exists, or the given default value if it doesn't
	 *
	 * @param name         attribute name
	 * @param defaultValue default value, if attribute not defined
	 * @return attribute value, or default if not defined
	 * @throws StageException thrown if attribute count not be parsed as Integer
	 */
	protected Integer getAttribute(final String name, final Integer defaultValue) throws StageException
	{
		final String value = attributes.get(name);
		final Integer intValue = AttributeUtils.parseInteger(value);
		return intValue != null ? intValue : defaultValue;
	}

	/**
	 * Return the attribute value as Boolean if it exists, or the given default value if it doesn't
	 *
	 * @param name         attribute name
	 * @param defaultValue default value, if attribute not defined
	 * @return attribute value, or default if not defined
	 * @throws StageException thrown if attribute count not be parsed as Boolean
	 */
	protected Boolean getAttribute(final String name, final Boolean defaultValue) throws StageException
	{
		final String value = attributes.get(name);
		final Boolean boolValue = AttributeUtils.parseBoolean(value);
		return boolValue != null ? boolValue : defaultValue;
	}

	/**
	 * Return the attribute value as BigDecimal if it exists, or the given default value if it doesn't
	 *
	 * @param name         attribute name
	 * @param defaultValue default value, if attribute not defined
	 * @return attribute value, or default if not defined
	 * @throws StageException thrown if attribute count not be parsed as BigDecimal
	 */
	protected BigDecimal getAttribute(final String name, final BigDecimal defaultValue) throws StageException
	{
		final String value = attributes.get(name);
		final BigDecimal decimalValue = AttributeUtils.parseDecimal(value);
		return decimalValue != null ? decimalValue : defaultValue;
	}
}
