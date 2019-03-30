package charlotte.tools;

import java.util.ArrayList;
import java.util.List;

public class ExceptionDam {
	public static void section(Throwable cause, ConsumerEx<ExceptionDam> routine) throws Exception {
		section(eDam -> {
			eDam.add(cause);
			routine.accept(eDam);
		});
	}

	public static void section(ConsumerEx<ExceptionDam> routine) throws Exception {
		ExceptionDam eDam = new ExceptionDam();
		try {
			routine.accept(eDam);
		}
		catch(Throwable e) {
			eDam.add(e);
		}
		eDam.burst();
	}

	private List<Throwable> _es = new ArrayList<Throwable>();

	public void add(Throwable e) {
		_es.add(e);
	}

	public void invoke(RunnableEx routine) {
		try {
			routine.run();
		}
		catch(Throwable e) {
			add(e);
		}
	}

	public void burst() {
		if(1 <= _es.size()) {
			Throwable[] es = _es.toArray(new Throwable[_es.size()]);

			_es.clear();

			throw RTError.re(es);
		}
	}
}
