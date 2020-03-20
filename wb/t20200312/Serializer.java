package wb.t20200312;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

import charlotte.tools.JsonTools;
import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;
import charlotte.tools.ReflectTools;

/**
 * untested
 *
 */
public class Serializer {
	private Object _target;

	public Serializer(Object target) {
		_target = target;
	}

	public Object getJsonObject() throws Exception {
		String className;
		Object valueObject;

		if(_target == null) {
			className = "";
			valueObject = "";
		}
		else {
			className = _target.getClass().getName();

			if(className.charAt(0) == '[') {
				ObjectList ol = new ObjectList(Array.getLength(_target));

				for(int index = 0; index < Array.getLength(_target); index++) {
					ol.add(new Serializer(Array.get(_target, index)).getJsonObject());
				}
				valueObject = ol;
			}
			else if(className.equals("java.util.List")) {
				ObjectList ol = new ObjectList(((List<?>)_target).size());

				for(Object element : (List<?>)_target) {
					ol.add(new Serializer(element).getJsonObject());
				}
				valueObject = ol;
			}
			else if(className.equals("java.util.Map")) {
				ObjectMap om = ObjectMap.create();

				for(Map.Entry<?, ?> entry : ((Map<?, ?>)_target).entrySet()) {
					om.put(entry.getKey(), new Serializer(entry.getValue()).getJsonObject());
				}
				valueObject = om;
			}
			else if(className.equals("boolean")) {
				valueObject = new JsonTools.Word(((Boolean)_target).booleanValue() ? "true" : "false");
			}
			else if(className.equals("byte")) {
				valueObject = new JsonTools.Word("" + (((Byte)_target).byteValue()));
			}
			else if(className.equals("int")) {
				valueObject = new JsonTools.Word("" + (((Integer)_target).intValue()));
			}
			else if(className.equals("char")) {
				valueObject = new JsonTools.Word("" + (((Character)_target).charValue()));
			}
			else if(className.equals("short")) {
				valueObject = new JsonTools.Word("" + (((Short)_target).shortValue()));
			}
			else if(className.equals("long")) {
				valueObject = new JsonTools.Word("" + (((Long)_target).longValue()));
			}
			else if(className.equals("float")) {
				valueObject = new JsonTools.Word("" + (((Float)_target).floatValue()));
			}
			else if(className.equals("double")) {
				valueObject = new JsonTools.Word("" + (((Double)_target).doubleValue()));
			}
			else {
				valueObject = GeneralSerializer.getJsonObject(_target, className);

				if(valueObject == null) {
					ObjectMap om = ObjectMap.create();

					for(ReflectTools.FieldUnit field : ReflectTools.getFields(Class.forName(className))) {
						om.put(field.inner.getName(), new Serializer(field.getValue(_target)).getJsonObject());
					}
					valueObject = om;
				}
			}
		}

		{
			ObjectMap ret = ObjectMap.create();

			ret.put("type", className);
			ret.put("value", valueObject);

			return ret;
		}
	}

	public String getJsonString() throws Exception {
		return JsonTools.encode(getJsonObject());
	}
}
