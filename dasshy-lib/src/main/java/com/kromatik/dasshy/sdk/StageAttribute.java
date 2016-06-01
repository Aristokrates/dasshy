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