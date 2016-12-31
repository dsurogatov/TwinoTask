/**
 * 
 */
package org.dsu;

import org.dsu.domain.Person;

/** Helps to create objects.
 *
 * @author nescafe
 */
public final class TestObjectHelper {

	public static final Page PAGE_DEFAULT = new Page(0, 20, null);
	public static final Person PERSON_DEFAULT = new Person();
	
	static {
		PERSON_DEFAULT.setFirstName("firstName");
		PERSON_DEFAULT.setSurName("surName");
	}
}
