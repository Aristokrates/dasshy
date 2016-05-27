package com.kromatik.dasshy.server.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Groups rules under a rule set having name, description and activ flag
 */
public class RuleGroup
{
	/** wheter the rule is active */
	private boolean				active;

	/** name of the rule set */
	private String				name;

	/** rule description */
	private String				description;

	/** rules composing this rule group */
	private List<Rule>			rules		=	new LinkedList<>();

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<Rule> getRules()
	{
		return rules;
	}

	public void setRules(List<Rule> rules)
	{
		this.rules = rules;
	}
}
