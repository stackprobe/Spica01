package violet.labo.module.fatcalc_v1;

public class FatFloat {
	private FatUFloat _figures;
	private int _sign;

	public FatFloat(FatUFloat figures, int sign) {
		if(figures == null) {
			throw new IllegalArgumentException("Bad figures: " + figures);
		}
		if(sign != 1 && sign != -1) {
			throw new IllegalArgumentException("Bad sign: " + sign);
		}
		_figures = figures;
		_sign = sign;
	}

	public FatUFloat figures() {
		return _figures;
	}

	public int sign() {
		return _sign;
	}
}
