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
	public IUInt sub(IUInt prm) {
		UDec b = (UDec)prm;
		UDec r = new UDec();

		r.value = (value + 10 - b.value) % 10;

		return r;
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

	@Override
	public IUInt mod(IUInt prm) {
		UDec b = (UDec)prm;
		UDec r = new UDec();

		r.value = value % b.value;

		return r;
	}

	@Override
	public boolean isZero() {
		return value == 0;
	}

	@Override
	public boolean isFill() {
		return value == 9;
	}

	@Override
	public int compareTo(IUInt prm) {
		UDec b = (UDec)prm;

		return value - b.value;
	}

	@Override
	public IUInt fill() {
		UDec r = new UDec();

		r.value = 9;

		return r;
	}
}
