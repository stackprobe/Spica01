package wb.t20190311;

import java.util.ArrayList;
import java.util.List;

import charlotte.tools.CsvFileWriter;
import charlotte.tools.FileTools;
import charlotte.tools.ListTools;
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

	private String W_DIR = "C:/temp/SBTest";

	private int TICK_PER_SECOND = 10;
	private double MAN_PER_MINUTE = 10.0;
	private double HANDICAP_RATE = 0.01;
	private int SB_MIN_SEC = 25;
	private int SB_MAX_SEC = 35;
	private int SBK_NUM = 5; // last one is for handicappers

	private void test01() throws Exception {
		FileTools.delete(W_DIR);
		FileTools.createDir(W_DIR);

		for(SBK_NUM = 2; SBK_NUM <= 5; SBK_NUM++) {
			try(CsvFileWriter writer = new CsvFileWriter(FileTools.combine(W_DIR, SBK_NUM + ".csv"))) {
				for(int c = 10; c <= 110; c += 1) {
					MAN_PER_MINUTE = c / 10.0;

					System.out.print(SBK_NUM + ", " + MAN_PER_MINUTE + " --> ");

					try {
						test01_Main();

						double mr = mattaRate(_mattaSeconds);
						double msa = mattaSecondAvg(_mattaSeconds);
						double hmr = mattaRate(_handicapMattaSeconds);
						double hmsa = mattaSecondAvg(_handicapMattaSeconds);

						System.out.println(String.format(
								"%.3f, %.3f, %.3f, %.3f --> %.3f, %.3f, %.3f"
								,mr
								,msa
								,hmr
								,hmsa
								,hmr / (mr == 0.0 ? -1.0 : mr)
								,hmsa / (msa == 0.0 ? -1.0 : msa)
								,hmsa - msa
								));

						writer.writeRow(new String[] {
								"" + SBK_NUM,
								"" + MAN_PER_MINUTE,
								"" + mr,
								"" + msa,
								"" + hmr,
								"" + hmsa,
						});
					}
					catch(MachiSecondExplosion e) {
						System.out.println("EXPLOSION");

						writer.writeRow(new String[] {
								"" + SBK_NUM,
								"" + MAN_PER_MINUTE,
						});
					}
				}
			}
		}
	}

	private double mattaRate(List<Integer> mattaSeconds) {
		long mattaTotal = 0L;

		for(int index = 1; index < mattaSeconds.size(); index++) {
			mattaTotal += mattaSeconds.get(index);
		}
		return mattaTotal * 1.0 / (mattaSeconds.get(0) + mattaTotal);
	}

	private double mattaSecondAvg(List<Integer> mattaSeconds) {
		long numer = 0L;
		long denom = 0L;

		for(int index = 1; index < mattaSeconds.size(); index++) {
			numer += mattaSeconds.get(index) * index;
			denom += mattaSeconds.get(index);
		}
		if(denom == 0L) {
			return -1.0;
		}
		return numer * 1.0 / denom;
	}

	private class Machi {
		public boolean handicap;
		public long startedTick;
	}

	private class SBChuu {
		public long endTick;
	}

	private SBChuu[] _sbks;
	private List<Machi> _machiList = new ArrayList<Machi>();
	private List<SBChuu> _sbChuuList = new ArrayList<SBChuu>();
	private List<Integer> _mattaSeconds = new ArrayList<Integer>();
	private List<Integer> _handicapMattaSeconds = new ArrayList<Integer>();

	private void test01_Main() throws MachiSecondExplosion {
		_sbks = new SBChuu[SBK_NUM];
		_machiList.clear();
		_sbChuuList.clear();
		_mattaSeconds.clear();
		_handicapMattaSeconds.clear();

		for(int tick = 0; tick < 7 * 86400 * TICK_PER_SECOND; tick++) {
			if(SecurityTools.cRandom.getReal2() < MAN_PER_MINUTE / (60.0 * TICK_PER_SECOND)) {
				Machi machi = new Machi();

				machi.handicap = SecurityTools.cRandom.getReal2() < HANDICAP_RATE;
				machi.startedTick = tick;

				_machiList.add(machi);
			}
			for(int index = 0; index < SBK_NUM; index++) {
				if(_sbks[index] != null && _sbks[index].endTick <= tick) {
					_sbks[index] = null;
				}
			}
			for(; ; ) {
				//int[] pair = machiSbkPairing_v3();
				//int[] pair = machiSbkPairing_v2();
				int[] pair = machiSbkPairing();
				//int[] pair = machiSbkPairing_vOkk();

				if(pair == null) {
					break;
				}
				int machiIndex = pair[0];
				int sbkIndex = pair[1];

				// ----

				Machi machi = _machiList.remove(machiIndex);

				if(machi.handicap) {
					increment(_handicapMattaSeconds, tick - machi.startedTick);
				}
				else {
					increment(_mattaSeconds, tick - machi.startedTick);
				}
				SBChuu sbChuu = new SBChuu();

				sbChuu.endTick = tick + SecurityTools.cRandom.getInt(SB_MIN_SEC * TICK_PER_SECOND, SB_MAX_SEC * TICK_PER_SECOND);

				_sbks[sbkIndex] = sbChuu;
			}
		}
	}

	private void increment(List<Integer> mattaSeconds, long mattaTick) throws MachiSecondExplosion {
		long lMattaSecond = (mattaTick + TICK_PER_SECOND / 2) / TICK_PER_SECOND;

		if(600 < lMattaSecond) {
			throw new MachiSecondExplosion();
		}
		int mattaSecond = (int)lMattaSecond;

		while(mattaSeconds.size() <= mattaSecond) {
			mattaSeconds.add(0);
		}
		mattaSeconds.set(
				mattaSecond,
				mattaSeconds.get(mattaSecond) + 1
				);
	}

	private class MachiSecondExplosion extends Exception {
		// none
	}

	private int[] machiSbkPairing_vOkk() {
		int akiSbkIndexHandicap = -1;
		int akiSbkIndex = -1;

		if(_sbks[SBK_NUM - 1] == null) {
			akiSbkIndexHandicap = SBK_NUM - 1;
		}
		for(int index = 0; index < SBK_NUM; index++) {
			if(_sbks[index] == null) {
				akiSbkIndex = index;
				break;
			}
		}

		if(1 <= _machiList.size()) {
			Machi machi = _machiList.get(0);

			if(machi.handicap) {
				if(akiSbkIndexHandicap != -1) {
					return new int[] { 0, akiSbkIndexHandicap };
				}
			}
			else {
				if(akiSbkIndex != -1) {
					return new int[] { 0, akiSbkIndex };
				}
			}
		}
		return null;
	}

	private int[] machiSbkPairing() {
		int akiSbkIndexHandicap = -1;
		int akiSbkIndex = -1;

		if(_sbks[SBK_NUM - 1] == null) {
			akiSbkIndexHandicap = SBK_NUM - 1;
		}
		for(int index = 0; index < SBK_NUM; index++) {
			if(_sbks[index] == null) {
				akiSbkIndex = index;
				break;
			}
		}

		if(akiSbkIndexHandicap != -1 && akiSbkIndex != -1) {
			if(1 <= _machiList.size()) {
				Machi machi = _machiList.get(0);

				if(machi.handicap) {
					return new int[] { 0, akiSbkIndexHandicap };
				}
				else {
					return new int[] { 0, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex != -1) {
			for(int index = 0; index < _machiList.size(); index++) {
				Machi machi = _machiList.get(index);

				if(machi.handicap == false) {
					return new int[] { index, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex == -1) {
			// noop
		}
		else {
			throw null; // never
		}
		return null;
	}

	private int[] machiSbkPairing_v2() {
		int akiSbkIndexHandicap = -1;
		int akiSbkIndex = -1;

		if(_sbks[SBK_NUM - 1] == null) {
			akiSbkIndexHandicap = SBK_NUM - 1;
		}
		for(int index = 0; index < SBK_NUM; index++) {
			if(_sbks[index] == null) {
				akiSbkIndex = index;
				break;
			}
		}

		if(akiSbkIndexHandicap != -1 && akiSbkIndex != -1) {
			if(1 <= _machiList.size()) {
				Machi machi = _machiList.get(0);

				if(machi.handicap) {
					return new int[] { 0, akiSbkIndexHandicap };
				}
				else {
					if(akiSbkIndexHandicap == akiSbkIndex && 2 <= _machiList.size() && _machiList.get(1).handicap) {
						return new int[] { 1, akiSbkIndexHandicap };
					}
					return new int[] { 0, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex != -1) {
			for(int index = 0; index < _machiList.size(); index++) {
				Machi machi = _machiList.get(index);

				if(machi.handicap == false) {
					return new int[] { index, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex == -1) {
			// noop
		}
		else {
			throw null; // never
		}
		return null;
	}

	private int[] machiSbkPairing_v3() {
		int akiSbkIndexHandicap = -1;
		int akiSbkIndex = -1;

		if(_sbks[SBK_NUM - 1] == null) {
			akiSbkIndexHandicap = SBK_NUM - 1;
		}
		for(int index = 0; index < SBK_NUM; index++) {
			if(_sbks[index] == null) {
				akiSbkIndex = index;
				break;
			}
		}

		if(akiSbkIndexHandicap != -1 && akiSbkIndex != -1) {
			if(1 <= _machiList.size()) {
				Machi machi = _machiList.get(0);

				if(machi.handicap) {
					return new int[] { 0, akiSbkIndexHandicap };
				}
				else {
					if(akiSbkIndexHandicap == akiSbkIndex) {
						int index = ListTools.indexOf(_machiList, m -> m.handicap);

						if(index != -1) {
							return new int[] { index, akiSbkIndexHandicap };
						}
					}
					return new int[] { 0, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex != -1) {
			for(int index = 0; index < _machiList.size(); index++) {
				Machi machi = _machiList.get(index);

				if(machi.handicap == false) {
					return new int[] { index, akiSbkIndex };
				}
			}
		}
		else if(akiSbkIndexHandicap == -1 && akiSbkIndex == -1) {
			// noop
		}
		else {
			throw null; // never
		}
		return null;
	}
}
