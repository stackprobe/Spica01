package wb.t20190402;

import java.util.ArrayList;
import java.util.List;

public abstract class RouteSearch<T> {
	protected List<T> route;

	protected abstract T first();
	protected abstract T next(T value);

	public void perform() {
		route = new ArrayList<T>();

		boolean ahead = true;

		do {
			if(ahead) {
				T node = first();

				if(node == null) {
					ahead = false;
				}
				else {
					route.add(node);
				}
			}
			else {
				T node = next(route.get(route.size() - 1));

				if(node == null) {
					route.remove(route.size() - 1);
				}
				else {
					ahead = true;
					route.set(route.size() - 1, node);
				}
			}
		}
		while(1 <= route.size());

		route = null;
	}
}
