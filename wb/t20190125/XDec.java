package wb.t20190125;

import java.util.ArrayList;
import java.util.List;

public class XDec implements IUInt {
	public IUInt vL;
	public IUInt vH;
	public int depth;
	public int rank;

	public XDec(int depth) {
		if(depth < 1) {
			throw new IllegalArgumentException();
		}
		if(depth == 1) {
			vL = new UDec();
			vH = new UDec();
		}
		else {
			vL = new XDec(depth - 1);
			vH = new XDec(depth - 1);
		}
		this.depth = depth;
		this.rank = 1 << depth;
	}

	@Override
	public void set(String str) {
		if(rank < str.length()) {
			throw new IllegalArgumentException();
		}
		str = zPad(str, rank);
		vL.set(str.substring(rank / 2));
		vH.set(str.substring(0, rank / 2));
	}

	private static String zPad(String str, int minlen) {
		while(str.length() < minlen) {
			str = "0" + str;
		}
		return str;
	}

	@Override
	public String get() {
		return unZPad(vH.get() + zPad(vL.get(), rank / 2));
	}

	private static String unZPad(String str) {
		while(str.startsWith("0")) {
			str = str.substring(1);
		}
		return str;
	}

	@Override
	public IUInt invert() {
		XDec r = new XDec(depth);

		r.vL = vL.invert();
		r.vH = vH.invert();

		return r;
	}

	@Override
	public IUInt one() {
		XDec r = new XDec(depth);

		r.vL = vL.one();

		return r;
	}

	private XDec toB(IUInt prm) {
		XDec b = (XDec)prm;

		if(depth != b.depth) {
			throw new IllegalArgumentException();
		}
		return b;
	}

	@Override
	public IUInt[] add(IUInt prm) {
		XDec b = toB(prm);

		IUInt[] c = vL.add(b.vL);
		IUInt[] d = vH.add(b.vH);
		IUInt[] e = d[0].add(c[1]);
		IUInt[] f = d[1].add(e[1]);

		XDec rL = new XDec(depth);
		XDec rH = new XDec(depth);

		rL.vL = c[0];
		rL.vH = e[0];
		rH.vL = f[0];

		return new IUInt[] { rL, rH };
	}

	@Override
	public IUInt[] sub(IUInt prm) {
		IUInt b = toB(prm);

		b = b.invert();
		b = b.add(b.one())[0];

		return add(b);
	}

	@Override
	public IUInt[] mul(IUInt prm) {
		XDec b = toB(prm);

		IUInt[] c = vL.mul(b.vL);
		IUInt[] d = vL.mul(b.vH);
		IUInt[] e = vH.mul(b.vL);
		IUInt[] f = vH.mul(b.vH);

		List<List<IUInt>> m = new ArrayList<List<IUInt>>();

		m.add(new ArrayList<IUInt>());
		m.add(new ArrayList<IUInt>());
		m.add(new ArrayList<IUInt>());
		m.add(new ArrayList<IUInt>());
		m.add(new ArrayList<IUInt>());

		m.get(0).add(c[0]);
		m.get(1).add(c[1]);
		m.get(1).add(d[0]);
		m.get(2).add(d[1]);
		m.get(1).add(e[0]);
		m.get(2).add(e[1]);
		m.get(2).add(f[0]);
		m.get(3).add(f[1]);

		for(int i = 0; i < 4; i++) {
			List<IUInt> mi = m.get(i);

			while(2 <= mi.size()) {
				IUInt t = mi.remove(mi.size() - 1);
				IUInt u = mi.remove(mi.size() - 1);
				IUInt[] v = t.add(u);

				mi.add(v[0]);
				m.get(i + 1).add(v[1]);
			}
		}

		XDec rL = new XDec(depth);
		XDec rH = new XDec(depth);

		rL.vL = m.get(0).get(0);
		rL.vH = m.get(1).get(0);
		rH.vL = m.get(2).get(0);
		rH.vH = m.get(3).get(0);

		return new IUInt[] { rL, rH };
	}

	@Override
	public IUInt div(IUInt prm) {
		return null; // TODO
	}
}
