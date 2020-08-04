package charlotte.tools;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *	String -> Class
 *		Class.forName("charlotte.tools.ReflectTools")
 *
 */
public class ReflectTools {
	public static class FieldUnit {
		public Field inner;

		public FieldUnit(Field value) {
			toAccessible(value);
			this.inner = value;
		}

		/**
		 * get from static field
		 * @return
		 * @throws Exception
		 */
		public Object getValue() throws Exception {
			return inner.get(null);
		}

		/**
		 * get from instance field
		 * @param instance
		 * @return
		 * @throws Exception
		 */
		public Object getValue(Object instance) throws Exception {
			return inner.get(instance);
		}

		/**
		 * set to static field
		 * @param value
		 * @throws Exception
		 */
		public void setValue(Object value) throws Exception {
			inner.set(null, value);
		}

		/**
		 * set to instance field
		 * @param instance
		 * @param value
		 * @throws Exception
		 */
		public void setValue(Object instance, Object value) throws Exception {
			inner.set(instance, value);
		}
	}

	public static class MethodUnit {
		public Method inner;

		public MethodUnit(Method value) {
			toAccessible(value);
			this.inner = value;
		}

		/*
		public Class<?>[] getParameterTypes() {
			return this.inner.getParameterTypes();
		}
		*/

		/**
		 * invoke static method
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object[] prms) throws Exception {
			return this.inner.invoke(null, prms);
		}

		/**
		 * invoke instance method
		 * @param instance
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object instance, Object[] prms) throws Exception {
			return this.inner.invoke(instance, prms);
		}
	}

	public static class ConstructorUnit {
		public Constructor<?> inner;

		public ConstructorUnit(Constructor<?> value) {
			toAccessible(value);
			this.inner = value;
		}

		/**
		 * invoke constructor
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object[] prms) throws Exception {
			return this.inner.newInstance(prms);
		}
	}

	private static void toAccessible(AccessibleObject value) {
		if(value.isAccessible() == false) {
			value.setAccessible(true);
		}
	}

	public static List<FieldUnit> getFieldsByInstance(Object instance) {
		return getFields(instance.getClass());
	}

	public static FieldUnit getFieldByInstance(Object instance, String name) {
		return getField(instance.getClass(), name);
	}

	public static List<FieldUnit> getFields(Class<?> classObj) {
		List<FieldUnit> dest = new ArrayList<FieldUnit>();

		while(classObj != null) {
			for(Field fieldObj : classObj.getDeclaredFields()) {
				dest.add(new FieldUnit(fieldObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest;
	}

	public static FieldUnit getField(Class<?> classObj, String name) {
		while(classObj != null) {
			for(Field fieldObj : classObj.getDeclaredFields()) {
				if(name.equals(fieldObj.getName())) {
					return new FieldUnit(fieldObj);
				}
			}
			classObj = classObj.getSuperclass();
		}
		return null;
	}

	public static boolean equals(FieldUnit a, Class<?> b) {
		return equals(a.inner.getType(), b);
	}

	public static boolean equals(Class<?> a, Class<?> b) {
		return a.getName().equals(b.getName());
	}

	public static boolean equalsOrBase(FieldUnit a, Class<?> b) {
		return equalsOrBase(a.inner.getType(), b);
	}

	public static boolean equalsOrBase(Class<?> a, Class<?> b) { // ret: ? a == b || a (extends | implements) b
		while(a != null) {
			if(equals(a, b)) {
				return true;
			}
			for(Class<?> interfaceObj : getInterfaces(a)) {
				if(equals(interfaceObj, b)) {
					return true;
				}
			}
			a = a.getSuperclass();
		}
		return false;
	}

	private static List<Class<?>> getInterfaces(Class<?> classObj) {
		List<Class<?>> dest = new ArrayList<Class<?>>();

		dest.add(classObj);

		for(int index = 0; index < dest.size(); index++) {
			for(Class<?> instanceObj : dest.get(index).getInterfaces()) {
				dest.add(instanceObj);
			}
		}
		dest.remove(0);
		return dest;
	}

	// moved -> FieldUnit
	/*
	public static Object getValue(FieldUnit field, Object instance) throws Exception {
		return field.value.get(instance);
	}
	*/

	// moved -> FieldUnit
	/*
	public static void setValue(FieldUnit field, Object instance, Object value) throws Exception {
		field.value.set(instance, value);
	}
	*/

	public static List<MethodUnit> getMethodsByInstance(Object instance) {
		return getMethods(instance.getClass());
	}

	public static List<MethodUnit> getMethods(Class<?> classObj) {
		List<MethodUnit> dest = new ArrayList<MethodUnit>();

		while(classObj != null) {
			for(Method methodObj : classObj.getDeclaredMethods()) {
				dest.add(new MethodUnit(methodObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest;
	}

	public static List<ConstructorUnit> getConstructorsByInstance(Object instance) {
		return getConstructors(instance.getClass());
	}

	public static List<ConstructorUnit> getConstructors(Class<?> classObj) {
		List<ConstructorUnit> dest = new ArrayList<ConstructorUnit>();

		while(classObj != null) {
			for(Constructor<?> ctorObj : classObj.getDeclaredConstructors()) {
				dest.add(new ConstructorUnit(ctorObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest;
	}

	public static MethodUnit getMethod(Class<?> classObj, String name) {
		return getMethod(classObj, name, null);
	}

	public static MethodUnit getMethod(Class<?> classObj, String name, Object[] parameters) {
		while(classObj != null) {
			for(Method methodObj : classObj.getDeclaredMethods()) {
				if(name.equals(methodObj.getName()) && (parameters == null || checkParameters(parameters, methodObj.getParameterTypes()))) {
					return new MethodUnit(methodObj);
				}
			}
			classObj = classObj.getSuperclass();
		}
		return null;
	}

	public static ConstructorUnit getConstructor(Class<?> classObj) {
		return getConstructors(classObj).get(0);
	}

	public static ConstructorUnit getConstructor(Class<?> classObj, Object[] parameters) {
		while(classObj != null) {
			for(Constructor<?> ctorObj : classObj.getDeclaredConstructors()) {
				if(checkParameters(parameters, ctorObj.getParameterTypes())) {
					return new ConstructorUnit(ctorObj);
				}
			}
			classObj = classObj.getSuperclass();
		}
		return null;
	}

	private static boolean checkParameters(Object[] parameters, Class<?>[] parameterTypes) {
		return parameters.length == parameterTypes.length; // HACK
	}
}
