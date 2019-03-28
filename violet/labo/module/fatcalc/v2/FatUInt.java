package violet.labo.module.fatcalc.v2;

import java.util.Arrays;

public class FatUInt {
	public int[] figures;
	public boolean remained = false;

	public FatUInt() {
		this(new int[0]);
	}

	public FatUInt(int[] figures) {
		this.figures = figures;
	}

	public int get(int index) {
		return index < figures.length ? figures[index] : 0;
	}

	public void resize(int size) {
		figures = Arrays.copyOf(figures, size);
	}

	public void inverse(int fill) {
		for(int index = 0; index < figures.length; index++) {
			figures[index] = fill - figures[index];
		}
	}

	public void add(int index, long value, int radix) {
		while(0 < value) {
			value += figures[index];
			figures[index] = (int)(value % radix);
			value /= radix;
			index++;
		}
	}
}
