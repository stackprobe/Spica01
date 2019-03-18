package wb.t20190311;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.SecurityTools;

public class Test0002 {
	public static void main(String[] args) {
		try {
			new Test0002().test01();
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}

	private int TICK_PER_SECOND = 10;
	private double MAN_PER_MINUTE = 10.0;
	private double HANDICAP_RATE = 0.01;
	private int SB_MIN_SEC = 25;
	private int SB_MAX_SEC = 35;
	private int SBK_NUM = 5; // last one is for handicappers

	private void test01() {
		for(SBK_NUM = 2; SBK_NUM <= 5; SBK_NUM++) {
			for(int c = 30; c <= 110; c += 2) {
				MAN_PER_MINUTE = c / 10.0;

				test01_Main();
			}
		}
	}

	private class Machi {
		public boolean handicap;
		public long startedTick;
	}

	private class SBChuu {
		public long endTick;
	}

	private SBChuu[] _sbs;
	private List<Machi> _machiList = new ArrayList<Machi>();
	private List<SBChuu> _sbChuuList = new ArrayList<SBChuu>();
	private List<Integer> _mattaSeconds = new ArrayList<Integer>();
	private List<Integer> _handicapMattaSeconds = new ArrayList<Integer>();

	private void test01_Main() {
		_sbs = new SBChuu[SBK_NUM];
		_machiList.clear();
		_sbChuuList.clear();
		_mattaSeconds.clear();
		_handicapMattaSeconds.clear();

		for(int tick = 0; tick < 86400 * TICK_PER_SECOND; tick++) {
			if(SecurityTools.cRandom.getReal2() < MAN_PER_MINUTE / (60.0 * TICK_PER_SECOND)) {
				Machi machi = new Machi();

				machi.handicap = SecurityTools.cRandom.getReal2() < HANDICAP_RATE;
				machi.startedTick = tick;

				_machiList.add(machi);
			}
			for(int index = 0; index < SBK_NUM; index++) {
				if(_sbs[index] != null && _sbs[index].endTick <= tick) {
					_sbs[index] = null;
				}
			}
			for(int index = 0; index < SBK_NUM; index++) {
				if(_sbs[index] == null) {
					int i;
					for(i = 0; i < _machiList.size(); i++) {
						Machi machi = _machiList.get(i);
					}
				}
			}

			for(int index = 0; index < _machiList.size(); index++) {
				Machi machi = _machiList.get(index);
			}
			for(int index = 0; index < SBK_NUM; index++) {
				boolean forHandicap = index == SBK_NUM - 1;

				if(_sbs[index] != null && _sbs[index].endTick <= tick) {
					_sbs[index] = null;
				}
			}
			for(int index = 0; index < _machiList.size(); index++) {
				Machi machi = _machiList.get(index);
			}
		}
	}
}
