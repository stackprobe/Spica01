package violet.labo.module.fatcalc;

public class FatUIntCalc {
	public int radix;

	public FatUIntCalc(int radix) {
		this.radix = radix;
	}

	public void add(FatUInt a, FatUInt b) {
		a.resize(Math.max(a.figures.length, b.figures.length) + 1);

		int carry = 0;

		for(int index = 0; index < b.figures.length || carry == 1; index++) {
			int value = a.figures[index] + b.get(index) + carry;
			carry = value / radix;
			a.figures[index] = value % radix;
		}
	}

	public int sub(FatUInt a, FatUInt b) {
		a.resize(Math.max(a.figures.length, b.figures.length) + 1);

		int carry = 0;

		for(int index = 0; (index < b.figures.length || carry == -1) && index < a.figures.length; index++) {
			int value = a.figures[index] - b.get(index) + carry + radix;
			carry = value / radix - 1;
			a.figures[index] = value % radix;
		}
		if(carry == -1) {
			a.inverse(radix - 1);
			a.add(0, 1L, radix);
		}
		return carry * 2 + 1;
	}

	public FatUInt mul(FatUInt a, FatUInt b) {
		FatUInt answer = new FatUInt();

		answer.resize(a.figures.length + b.figures.length);

		for(int index = 0; index < a.figures.length; index++) {
			for(int ndx = 0; ndx < b.figures.length; ndx++) {
				answer.add(index + ndx, (long)a.figures[index] * b.figures[ndx], radix);
			}
		}
		return answer;
	}

	public FatUInt div(FatUInt a, FatUInt b) {
		return new FatUIntCalcDiv(this).div(new FatUIntDiv(a), new FatUIntDiv(b));
	}
}
