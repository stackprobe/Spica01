package wb.t20190311;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.SecurityTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			new Test0001().test01_caller();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private int TICK_PER_SECOND = 1000;
	private double MAN_PER_MINUTE = 10.0;
	private int SB_MIN_SEC = 25;
	private int SB_MAX_SEC = 35;
	private int SBK_NUM = 5;

	private class Machi {
		public int startedTick;
	}

	private class SBChuu {
		public int endTick;
	}

	private List<Machi> _machiList = new ArrayList<Machi>();
	private List<SBChuu> _sbChuuList = new ArrayList<SBChuu>();
	private List<Integer> _mattaSeconds = new ArrayList<Integer>();

	private void test01_caller() {
		for(int c = 500; c <= 1500; c += 25) {
			MAN_PER_MINUTE = c / 100.0;

			test01();
		}
	}

	private void test01() {
		_machiList.clear();
		_sbChuuList.clear();
		_mattaSeconds.clear();

		try {
			for(int tick = 0; tick < 5 * 3600 * TICK_PER_SECOND; tick++) {
				if(SecurityTools.cRandom.getReal2() < MAN_PER_MINUTE / (60 * TICK_PER_SECOND)) {
					Machi machi = new Machi();
					machi.startedTick = tick;
					_machiList.add(machi);
				}
				while(1 <= _machiList.size() && _sbChuuList.size() < SBK_NUM) {
					int machiStartedTick = _machiList.remove(0).startedTick;
					int mattaTick = tick - machiStartedTick;
					int mattaSecond = (mattaTick + TICK_PER_SECOND / 2) / TICK_PER_SECOND;

					if(600 < mattaSecond) {
						throw new MachiSecondExplosion();
					}
					while(_mattaSeconds.size() <= mattaSecond) {
						_mattaSeconds.add(0);
					}
					_mattaSeconds.set(mattaSecond, _mattaSeconds.get(mattaSecond) + 1);

					SBChuu sbChuu = new SBChuu();
					//sbChuu.endTick = tick + SecurityTools.cRandom.getInt(SB_MIN_SEC, SB_MAX_SEC) * TICK_PER_SECOND;
					sbChuu.endTick = tick + SecurityTools.cRandom.getRangeInt(SB_MIN_SEC * TICK_PER_SECOND, SB_MAX_SEC * TICK_PER_SECOND);
					_sbChuuList.add(sbChuu);
				}
				for(int index = _sbChuuList.size() - 1; 0 <= index; index--) {
					if(_sbChuuList.get(index).endTick <= tick) {
						_sbChuuList.remove(index);
					}
				}
			}
			System.out.println(MAN_PER_MINUTE + ", " + SBK_NUM + " --> " + matanaiRate() + ", " + mattaSecondAvg());
		}
		catch(MachiSecondExplosion e) {
			System.out.println(MAN_PER_MINUTE + ", " + SBK_NUM + " --> EXPLOSION");
		}
	}

	private class MachiSecondExplosion extends Exception {
		// none
	}

	private double matanaiRate() {
		long mattaTotal = 0L;

		for(int index = 1; index < _mattaSeconds.size(); index++) {
			mattaTotal += _mattaSeconds.get(index);
		}
		return _mattaSeconds.get(0) * 1.0 / (_mattaSeconds.get(0) + mattaTotal);
	}

	private double mattaSecondAvg() {
		long numer = 0L;
		long denom = 0L;

		for(int index = 1; index < _mattaSeconds.size(); index++) {
			numer += _mattaSeconds.get(index) * index;
			denom += _mattaSeconds.get(index);
		}
		return numer * 1.0 / denom;
	}
}
