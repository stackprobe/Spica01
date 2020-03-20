package wb.t20200312;

import charlotte.tools.JsonTools;

public class Serializer {
	private Object _target;

	public Serializer(Object target) {
		_target = target;
	}

	public Object getObject() {
		throw null; //ReflectTools.getFields(_target); // TODO
	}

	public String getJsonString() {
		return JsonTools.encode(getObject());
	}

	// TODO
	// TODO
	// TODO
}
