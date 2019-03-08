package violet.labo.module.httpserverfw.html.tag;

import java.util.Map;

public abstract class AbstractTag implements ITag {
	public Map<String, String> attributes;

	@Override
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
