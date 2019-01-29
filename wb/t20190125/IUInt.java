package wb.t20190125;

public interface IUInt {
	void set(String str);
	String get();

	IUInt invert();
	IUInt one();
	IUInt[] add(IUInt prm);
	IUInt sub(IUInt prm);
	IUInt[] mul(IUInt prm);
	IUInt div(IUInt prm);
	IUInt mod(IUInt prm);

	// divSubで使う...
	boolean isZero();
	boolean isFill();
	int compareTo(IUInt prm);
	IUInt fill();
}
