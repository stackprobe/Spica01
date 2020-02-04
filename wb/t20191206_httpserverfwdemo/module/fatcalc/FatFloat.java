package wb.t20191206_httpserverfwdemo.module.fatcalc;

public class FatFloat {
	public FatUFloat inner;
	public int sign;

	public FatFloat(FatUFloat inner, int sign) {
		this.inner = inner;
		this.sign = sign;
	}

	public void normalize() {
		inner.normalize();

		if(inner.inner.figures.length == 0) {
			sign = 1;
		}
	}
}
