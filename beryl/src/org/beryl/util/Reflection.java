package org.beryl.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Reflection {

	/**
	 * Gets a list of all interfaces implemented by the class. This includes super-classes.
	 */
	public static Class<?> [] getAllInterfaces(final Class<?> clazz) {
		List<Class<?>> interfaces = new ArrayList<Class<?>>();
		Class<?> cur = clazz;
		while(cur != Object.class && cur != null) {
			final Class<?>[] ifaces = cur.getInterfaces();
			final int size = ifaces.length;
			for(int i = 0; i < size; i++) {
				interfaces.add(ifaces[i]);
			}
			cur = cur.getSuperclass();
		}
		
		final int size = interfaces.size();
		Class<?> [] result = new Class<?>[size];
		for(int i = 0; i < size; i++) {
			result[i] = interfaces.get(i);
		}
		
		return result;
	}
	
	public static final ArrayList<Class<?>> ZeroParameters = new ArrayList<Class<?>>();
	/**
	 * Basically Class.getMethod but allows for a list of parameter types instead of a variable arguments list.
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	public static Method findDeclaredMethod(final Class<?> clazz, final String methodName, List<Class<?>> paramTypes) throws SecurityException, NoSuchMethodException {
		Method method = null;
		Method [] methods = clazz.getDeclaredMethods();

		if(paramTypes == null) {
			paramTypes = ZeroParameters;
		}

		for(Method candidate : methods) {
			if(candidate.getName().equals(methodName)) {

				Class<?>[] candidateParamTypes = candidate.getParameterTypes();
				if(candidateParamTypes.length == paramTypes.size()) {
					final int length = candidateParamTypes.length;
					boolean parametersMatch = true;
					for(int i = 0; i < length && parametersMatch; i++) {
						if(! candidateParamTypes[i].isAssignableFrom(paramTypes.get(i))) {
							parametersMatch = false;
						}
					}
					if(parametersMatch) {
						method = candidate;
						method.setAccessible(true);
						break;
					}
				}
			}
		}

		/* Try this to:
		method = clazz.getDeclaredMethod(methodName, (Class<?>[]) paramTypes.toArray());
		if(method != null) {
			method.setAccessible(true);
		}
		*/
		return method;
	}
}
