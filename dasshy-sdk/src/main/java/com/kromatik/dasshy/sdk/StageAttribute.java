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

/**
 * Definition of the configuration attributes expected by a stage
 */
public class StageAttribute
{
	/**
	 * type of the attribute
	 */
	public enum Type
	{
		STRING, INTEGER, DECIMAL, BOOLEAN
	}

	/** name of the attribute */
	private String			name;

	/** attribute type */
	private Type			type 		=	Type.STRING;

	/** mark as required */
	private boolean 		required 	=	false;

	/**
	 * Default constructor
	 */
	public StageAttribute()
	{
		// no-op
	}

	/**
	 * Constructing attribute with name
	 *
	 * @param name name of the attribute
	 */
	public StageAttribute(final String name)
	{
		this.name = name;
	}

	/**
	 * Constructing attribute with name and type
	 *
	 * @param name name of the attribute
	 * @param type attribute type
	 */
	public StageAttribute(final String name, final Type type)
	{
		this.name = name;
		this.type = type;
	}

	/**
	 * Constructing mandatory/optional attribute with name, type
	 *
	 * @param name name of the attribute
	 * @param type attribute type
	 * @param required mandatory or optional attribute
	 */
	public StageAttribute(final String name, final Type type, boolean required)
	{
		this.name = name;
		this.type = type;
		this.required = required;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
	{
		this.type = type;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}
}