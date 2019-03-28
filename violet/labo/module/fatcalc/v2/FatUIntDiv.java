package violet.labo.module.fatcalc.v2;

import java.util.Arrays;

public class FatUIntDiv {
	public int[] figures;
	public int end;

	public FatUIntDiv(FatUInt a) {
		this(a.figures);
	}

	public FatUIntDiv(int[] figures) {
		this.figures = figures;
		this.end = figures.length;

		trim();
	}

	public void trim() {
		while(0 < end && figures[end - 1] == 0) {
			end--;
		}
	}

	public int get(int index) {
		return index < figures.length ? figures[index] : 0;
	}

	public FatUInt getUInt() {
		return new FatUInt(figures.length == end ? figures : Arrays.copyOf(figures, end));
	}
}
