package com.kromatik.dasshy.sdk;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for plugin attributes
 */
public class AttributeUtils
{

	/**
	 * Default private constructor
	 */
	private AttributeUtils()
	{
		// no-op
	}

	/**
	 * Validate stage configuration
	 *
	 * @param stage stage
	 * @param config configuration
	 */
	public static void validateConfiguration(final IStage stage, final Map<String, String> config) throws StageException
	{
		final String stageName = stage.getClass().getSimpleName();
		final List<String> errors = new ArrayList<>();

		for (final StageAttribute definition : stage.getAttributeDefinitions())
		{
			final String name = definition.getName();
			final String value = config.get(name);

			// validate required attributes
			if (definition.isRequired() && (value == null || "".equals(value.trim())))
			{
				errors.add(stageName + "," + name + ",validation.error.is.required");
			}

			// validate attribute types
			try
			{
				switch (definition.getType())
				{
				case STRING:
					break;
				case BOOLEAN:
					parseBoolean(value);
					break;
				case INTEGER:
					parseInteger(value);
					break;
				case DECIMAL:
					parseDecimal(value);
					break;
				default:
					break;
				}
			}
			catch (final StageException validationException)
			{
				errors.add(stageName + "," + name + "," + validationException.getMessage());
			}
		}

		// throw new validation exception with complete error list
		if (!errors.isEmpty())
		{
			throw new StageException(stageName + " attributes failed validation: " +
							Arrays.toString(errors.toArray(new String[errors.size()])));
		}
	}

	/**
	 *
	 * @param value string value
	 *
	 * @return integer value
	 *
	 * @throws StageException if conversion fails
	 */
	public static Integer parseInteger(final String value) throws StageException
	{
		if (value != null)
		{
			try
			{
				if (isBlank(value))
				{
					return null;
				}
				else
				{
					return Integer.valueOf(value);
				}
			}
			catch (final NumberFormatException e)
			{
				throw new StageException("Cannot parse attribute value '" + value + "' as an integer.", e);
			}
		}
		return null;
	}

	/**
	 *
	 * @param value string value
	 *
	 * @return decimal value
	 *
	 * @throws StageException if conversion fails
	 */
	public static BigDecimal parseDecimal(final String value) throws StageException
	{
		if (value != null)
		{
			try
			{
				if (isBlank(value))
				{
					return null;
				}
				else
				{
					return new BigDecimal(value);
				}
			}
			catch (NumberFormatException e)
			{
				throw new StageException("Cannot parse attribute value '" + value + "' as a decimal number.", e);
			}
		}
		return null;
	}

	/**
	 *
	 * @param value string value
	 *
	 * @return boolean value
	 *
	 * @throws StageException if conversion fails
	 */
	public static Boolean parseBoolean(final String value) throws StageException
	{
		if (value != null)
		{
			try
			{
				if (isBlank(value))
				{
					return false;
				}
				else
				{
					return Boolean.parseBoolean(value);
				}
			}
			catch (NumberFormatException e)
			{
				throw new StageException("Cannot parse attribute value '" + value + "' as a decimal number.", e);
			}
		}
		return false;
	}

	/**
	 * Is Blank String check
	 *
	 * @param str string to check
	 *
	 * @return true, is string is blank; false otherwise
	 */
	public static boolean isBlank(final String str)
	{
		int strLen;
		if (str != null && (strLen = str.length()) != 0)
		{
			for (int i = 0; i < strLen; ++i)
			{
				if (!Character.isWhitespace(str.charAt(i)))
				{
					return false;
				}
			}

			return true;
		}
		else
		{
			return true;
		}
	}
}
