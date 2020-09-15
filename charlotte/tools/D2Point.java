package charlotte.tools;

public class D2Point {
	public double x;
	public double y;

	public D2Point() {
		// noop
	}

	public D2Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public I2Point ToI2Point() {
		return new I2Point(
				DoubleTools.toInt(this.x),
				DoubleTools.toInt(this.y)
				);
	}
}
