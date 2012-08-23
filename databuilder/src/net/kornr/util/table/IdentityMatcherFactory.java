package net.kornr.util.table;

public class IdentityMatcherFactory {

	static public class StringNoCaseMatcher implements IIdentityMatcher {
		@Override
		public boolean matches(Object o1, Object o2) {
			return ((String)o1).trim().equalsIgnoreCase(((String)o2).trim());
		}
	}

	static public class ObjectEqualsMatcher implements IIdentityMatcher {
		@Override
		public boolean matches(Object o1, Object o2) {
			if (o1 == o2) {
				return true;
			}
			if (o1 != null) {
				return o1.equals(o2);
			} else if (o2 != null) {
				return o2.equals(o1);
			}
			return false;
		}
	}
	
}
