package wb.t20200312;

import charlotte.tools.JsonTools;
import charlotte.tools.ReflectTools;

public class Serializer {
	private Object _target;

	public Serializer(Object target) {
		_target = target;
	}

	public Object getObject() {
		ReflectTools.getFields(_target);
	}

	public String getJsonString() {
		return JsonTools.encode(getObject());
	}
}
