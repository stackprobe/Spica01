package charlotte.tools;

import java.util.List;

public class DebugTools {
	public static Object toListOrMap(Object instance) throws Exception {
		return toListOrMap(instance, 3);
	}

	public static Object toListOrMap(Object instance, int depth) throws Exception {
		if(instance == null) {
			return null;
		}
		if(depth <= 0) {
			return instance;
		}
		Class<?> classObj = instance.getClass();

		if(classObj.isPrimitive()) {
			return instance;
		}
		if(classObj.isArray()) {
			ObjectList dest = new ObjectList();

			for(Object element : (Object[])instance) {
				dest.add(toListOrMap(element, depth - 1));
			}
			return dest;
		}
		if(ReflectTools.equalsOrBase(classObj, String.class)) {
			return instance;
		}
		if(ReflectTools.equalsOrBase(classObj, List.class)) {
			ObjectList dest = new ObjectList();

			for(Object element : (List<?>)instance) {
				dest.add(toListOrMap(element, depth - 1));
			}
			return dest;
		}

		{
			ObjectMap dest = ObjectMap.create();

			for(ReflectTools.FieldUnit field : ReflectTools.getFieldsByInstance(instance)) {
				dest.put(field.inner.getName(), toListOrMap(field.getValue(instance), depth - 1));
			}
			return dest;
		}
	}

	public static void mustThrow(RunnableEx routine) {
		try {
			routine.run();
		}
		catch(Throwable e) {
			e.printStackTrace(System.out);
			return;
		}
		throw new RTError();
	}

	public static String toString(Object value) {
		return toString(value, "<NULL>");
	}

	public static String toString(Object value, String nullVal) {
		return value == null ? nullVal : value.toString();
	}
}
