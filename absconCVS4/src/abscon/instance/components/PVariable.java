package abscon.instance.components;

/**
 * Author: Tomo Bessho Course: CSCE 421 Date: 2/1/2020
 */

public class PVariable {
	private String name;

	private PDomain domain;

	public String getName() {
		return name;
	}

	public PDomain getDomain() {
		return domain;
	}

	public PVariable(String name, PDomain domain) {
		this.name = name;
		this.domain = domain;
	}

	public String toString() {
		return "  variable " + name + " with associated domain " + domain.getName();
	}
}
