package violet.labo.module.fatcalc;

import charlotte.tools.RTError;

public class FatUIntCalcDiv {
	public FatUIntCalc owner;

	public FatUIntCalcDiv(FatUIntCalc owner) {
		this.owner = owner;
	}

	public FatUInt div(FatUIntDiv a, FatUIntDiv b) {
		if(b.end == 0) {
			throw new RTError("Zero divide");
		}

		FatUInt answer = new FatUInt(new int[a.end]);

		{
			int numer = owner.radix;
			int denom = b.figures[b.end - 1] + 1;
			int d = numer / denom;

			FatUInt dd = new FatUInt(new int[] { d });

			a = new FatUIntDiv(owner.mul(a.getUInt(), dd));
			b = new FatUIntDiv(owner.mul(b.getUInt(), dd));
		}

		while(b.figures[b.start] == 0) {
			b.start++;
		}

		for(; ; ) {
			if(a.end < b.end) {
				break;
			}
			if(a.end == b.end) {
				int numer = a.figures[a.end - 1];
				int denom = b.figures[b.end - 1] + 1;
				int d = numer / denom;

				if(d == 0) {
					break;
				}
				answer.add(0, (long)d, owner.radix);

				sub(a, 0, mul(b, d));
			}
			else {
				long numer = a.figures[a.end - 2] + (long)owner.radix * a.figures[a.end - 1];
				long denom = b.figures[b.end - 1] + 1L;
				long d = numer / denom;
				int di = a.end - b.end - 1;

				answer.add(di, (long)d, owner.radix);

				sub(a, di, mul(b, (int)(d % owner.radix), (int)(d / owner.radix)));
			}
			a.trim();
		}
		if(a.end != 0) {
			FatUInt ta = a.getUInt();

			if(owner.sub(ta, b.getUInt()) == 1) {
				answer.add(0, 1L, owner.radix);
				a = new FatUIntDiv(ta);
				answer.remained = a.end != 0;
			}
			else {
				answer.remained = true;
			}
		}
		return answer;
	}

	private void sub(FatUIntDiv a, int offset, FatUIntDiv b) {
		int carry = 0;

		for(int index = 0; index < b.end || carry == -1; index++) {
			int value = a.figures[offset + index] - b.get(index) + carry + owner.radix;
			carry = value / owner.radix - 1;
			a.figures[offset + index] = value % owner.radix;
		}
	}

	private FatUIntDiv mul(FatUIntDiv a, int b) {
		FatUInt answer = new FatUInt(new int[a.end + 1]);

		for(int index = a.start; index < a.end; index++) {
			answer.add(index, (long)a.figures[index] * b, owner.radix);
		}
		return new FatUIntDiv(answer);
	}

	private FatUIntDiv mul(FatUIntDiv a, int b, int b2) {
		FatUInt answer = new FatUInt(new int[a.end + 2]);

		for(int index = a.start; index < a.end; index++) {
			answer.add(index + 0, (long)a.figures[index] * b, owner.radix);
			answer.add(index + 1, (long)a.figures[index] * b2, owner.radix);
		}
		return new FatUIntDiv(answer);
	}
}
