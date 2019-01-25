package wb.t20190125;

import charlotte.tools.StringTools;

public class UDec implements IUInt {
	public int value;

	@Override
	public void set(String str) {
		if(str.length() != 1 || StringTools.DECIMAL.indexOf(str.charAt(0)) == -1) {
			throw new IllegalArgumentException();
		}
		value = StringTools.DECIMAL.indexOf(str.charAt(0));
	}

	@Override
	public String get() {
		return StringTools.DECIMAL.substring(value, value + 1);
	}

	@Override
	public IUInt invert() {
		UDec r = new UDec();

		r.value = 9 - value;

		return r;
	}

	@Override
	public IUInt one() {
		UDec r = new UDec();

		r.value = 1;

		return r;
	}

	@Override
	public IUInt[] add(IUInt prm) {
		UDec b = (UDec)prm;
		UDec rH = new UDec();
		UDec rL = new UDec();

		rH.value = (value + b.value) / 10;
		rL.value = (value + b.value) % 10;

		return new UDec[] { rL, rH };
	}

	@Override
	public IUInt[] sub(IUInt prm) {
		UDec b = (UDec)prm;

		if(value < b.value) {
			UDec r = new UDec();

			r.value = value + 10 - b.value;

			return new UDec[] { r, null };
		}
		else {
			UDec r = new UDec();

			r.value = value - b.value;

			return new UDec[] { r };
		}
	}

	@Override
	public IUInt[] mul(IUInt prm) {
		UDec b = (UDec)prm;
		UDec rH = new UDec();
		UDec rL = new UDec();

		rH.value = (value * b.value) / 10;
		rL.value = (value * b.value) % 10;

		return new UDec[] { rL, rH };
	}

	@Override
	public IUInt div(IUInt prm) {
		UDec b = (UDec)prm;
		UDec r = new UDec();

		r.value = value / b.value;

		return r;
	}
}
