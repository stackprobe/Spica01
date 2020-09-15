package charlotte.tools;

public class D2Size {
	public double w;
	public double h;

	public D2Size() {
		// noop
	}

	public D2Size(double w, double h) {
		this.w = w;
		this.h = h;
	}

	public I2Size ToI2Size() {
		return new I2Size(
				DoubleTools.toInt(this.w),
				DoubleTools.toInt(this.h)
				);
	}
}
