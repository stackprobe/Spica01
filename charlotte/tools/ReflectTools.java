package charlotte.tools;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectTools {
	public static class FieldUnit {
		public Field field;

		public FieldUnit(Field field) {
			this.field = field;
		}
	}

	public static class MethodUnit {
		public Method method;

		public MethodUnit(Method method) {
			this.method = method;
		}

		public Class<?>[] getParameters() {
			return this.method.getParameterTypes();
		}

		/**
		 * invoke static method
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object[] prms) throws Exception {
			return this.method.invoke(null, prms);
		}

		/**
		 * invoke instance method
		 * @param instance
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object instance, Object[] prms) throws Exception {
			return this.method.invoke(instance, prms);
		}
	}

	public static class ConstructorUnit {
		public Constructor<?> ctor;

		public ConstructorUnit(Constructor<?> ctor) {
			this.ctor = ctor;
		}

		/**
		 * invoke constructor
		 * @param prms
		 * @return
		 * @throws Exception
		 */
		public Object invoke(Object[] prms) throws Exception {
			return this.ctor.newInstance(prms);
		}
	}

	public static FieldUnit[] getFieldsByInstance(Object instance) {
		return getFields(instance.getClass());
	}

	public static FieldUnit getFieldByInstance(Object instance, String name) {
		return getField(instance.getClass(), name);
	}

	public static FieldUnit[] getFields(Class<?> classObj) {
		List<FieldUnit> dest = new ArrayList<FieldUnit>();

		while(classObj != null) {
			for(Field fieldObj : classObj.getDeclaredFields()) {
				dest.add(new FieldUnit(fieldObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest.toArray(new FieldUnit[dest.size()]);
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
		return equals(a.field.getType(), b);
	}

	public static boolean equals(Class<?> a, Class<?> b) {
		return a.getName().equals(b.getName());
	}

	public static boolean equalsOrBase(FieldUnit a, Class<?> b) {
		return equalsOrBase(a.field.getType(), b);
	}

	public static boolean equalsOrBase(Class<?> a, Class<?> b) { // ert: ? a == b || a (extends | implements) b
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

	private static Class<?>[] getInterfaces(Class<?> classObj) {
		List<Class<?>> dest = new ArrayList<Class<?>>();

		dest.add(classObj);

		for(int index = 0; index < dest.size(); index++) {
			for(Class<?> instanceObj : dest.get(index).getInterfaces()) {
				dest.add(instanceObj);
			}
		}
		dest.remove(0);
		return dest.toArray(new Class<?>[dest.size()]);
	}

	public static Object getValue(FieldUnit field, Object instance) throws Exception {
		return field.field.get(instance);
	}

	public static void setValue(FieldUnit field, Object instance, Object value) throws Exception {
		field.field.set(instance, value);
	}

	public static MethodUnit[] getMethodsByInstance(Object instance) {
		return getMethods(instance.getClass());
	}

	public static MethodUnit[] getMethods(Class<?> classObj) {
		List<MethodUnit> dest = new ArrayList<MethodUnit>();

		while(classObj != null) {
			for(Method methodObj : classObj.getDeclaredMethods()) {
				dest.add(new MethodUnit(methodObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest.toArray(new MethodUnit[dest.size()]);
	}

	public static ConstructorUnit[] getConstructorsByInstance(Object instance) {
		return getConstructors(instance.getClass());
	}

	public static ConstructorUnit[] getConstructors(Class<?> classObj) {
		List<ConstructorUnit> dest = new ArrayList<ConstructorUnit>();

		while(classObj != null) {
			for(Constructor<?> ctorObj : classObj.getDeclaredConstructors()) {
				dest.add(new ConstructorUnit(ctorObj));
			}
			classObj = classObj.getSuperclass();
		}
		return dest.toArray(new ConstructorUnit[dest.size()]);
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
		return getConstructors(classObj)[0];
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
		return parameters.length == parameterTypes.length; // XXX
	}
}
