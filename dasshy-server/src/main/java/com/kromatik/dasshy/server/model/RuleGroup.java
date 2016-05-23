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
}
