package wb.t20200312;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import charlotte.tools.JsonTools;
import charlotte.tools.ObjectList;
import charlotte.tools.ObjectMap;
import charlotte.tools.ObjectTree;
import charlotte.tools.ReflectTools;

/**
 * untested
 *
 */
public class Deserializer {
	private Object _target = null;

	public void setJsonString(String source) throws Exception {
		setJsonObject(JsonTools.decode(source));
	}

	@SuppressWarnings("unchecked")
	public void setJsonObject(Object source) throws Exception {
		ObjectTree ot = new ObjectTree(source);

		String className = ot.get("type").stringValue();
		Object valueObject = ot.get("value");

		if(className.isEmpty()) {
			_target = null;
		}
		else if(className.charAt(0) == '[') {
			ObjectList ol = (ObjectList)valueObject;

			_target = Array.newInstance(Class.forName(className.substring(1)), ol.size());

			for(int index = 0; index < ol.size(); index++) {
				Array.set(_target, index, ol.get(index));
			}
		}
		else if(className.equals("java.util.List")) {
			ObjectList ol = (ObjectList)valueObject;

			_target = new ArrayList<Object>();

			for(int index = 0; index < ol.size(); index++) {
				((List<Object>)_target).add(ol.get(index));
			}
		}
		else if(className.equals("java.util.Map")) {
			ObjectMap om = (ObjectMap)valueObject;

			_target = new TreeMap<Object, Object>();

			for(String key : om.keys()) {
				((Map<Object, Object>)_target).put(key, om.get(key));
			}
		}
		else if(className.equals("boolean")) {
			_target = new Boolean(valueObject.toString().equals("true"));
		}
		else if(className.equals("byte")) {
			_target = new Byte(Byte.parseByte(valueObject.toString()));
		}
		else if(className.equals("int")) {
			_target = new Integer(Integer.parseInt(valueObject.toString()));
		}
		else if(className.equals("char")) {
			_target = new Character((char)Integer.parseInt(valueObject.toString()));
		}
		else if(className.equals("short")) {
			_target = new Short(Short.parseShort(valueObject.toString()));
		}
		else if(className.equals("long")) {
			_target = new Long(Long.parseLong(valueObject.toString()));
		}
		else if(className.equals("float")) {
			_target = new Float(Float.parseFloat(valueObject.toString()));
		}
		else if(className.equals("double")) {
			_target = new Double(Double.parseDouble(valueObject.toString()));
		}
		else {
			valueObject = GeneralSerializer.getTrueObject(valueObject, className);

			if(valueObject == null) {
				ObjectMap om = ObjectMap.create();

				_target = ReflectTools.getConstructor(Class.forName(className)).invoke(new Object[0]);

				for(ReflectTools.FieldUnit field : ReflectTools.getFields(Class.forName(className))) {
					Deserializer d = new Deserializer();

					d.setJsonObject(om.get(field.inner.getName()));

					field.setValue(_target, d.getObject());
				}
			}
		}
	}

	public Object getObject() {
		return _target;
	}
}
