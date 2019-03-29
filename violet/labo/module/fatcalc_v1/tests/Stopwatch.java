package violet.labo.module.fatcalc_v1.tests;

import java.util.ArrayList;
import java.util.List;

public class Stopwatch {
	private String _label;

	public Stopwatch(String label) {
		_label = label;
	}

	private class LapTime {
		private String _label;
		private long _startTime;
		private long _endTime = -1L;

		public LapTime(String label) {
			_label = label;
			_startTime = System.currentTimeMillis();
		}

		public void stop() {
			_endTime = System.currentTimeMillis();
		}

		public String label() {
			return _label;
		}

		public long time() {
			if(_endTime == -1L) {
				throw null; // never
			}
			return _endTime - _startTime;
		}
	}

	private List<LapTime> _lapTimes = new ArrayList<LapTime>();
	private LapTime _curr = null;

	public void clear() {
		_lapTimes.clear();
		_curr = null;
	}

	public void start(String label) {
		if(_curr != null) {
			stop();
		}
		_curr = new LapTime(label);
	}

	public void stop() {
		if(_curr == null) {
			throw null; // never
		}
		_curr.stop();
		_lapTimes.add(_curr);
		_curr = null;
	}

	public void debugPrint() {
		if(_curr != null) {
			throw null; // never
		}
		System.out.println(_label + " >");

		for(LapTime lapTime : _lapTimes) {
			System.out.println("[" + _label + "] " + lapTime.label() + ": " + lapTime.time());
		}
		System.out.println("< " + _label);
	}
}
