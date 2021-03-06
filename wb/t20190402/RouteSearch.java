package wb.t20190402;

import java.util.ArrayList;
import java.util.List;

public abstract class RouteSearch<T> {
	protected List<T> route = new ArrayList<T>();

	protected abstract T first();
	protected abstract T next(T value);

	public void perform() {
		for(; ; ) {
			for(; ; ) {
				T node = first();

				if(node == null) {
					break;
				}
				route.add(node);
			}
			for(; ; ) {
				if(route.isEmpty()) {
					return;
				}
				T node = next(route.get(route.size() - 1));

				if(node != null) {
					route.set(route.size() - 1, node);
					break;
				}
				route.remove(route.size() - 1);
			}
		}
	}
}
