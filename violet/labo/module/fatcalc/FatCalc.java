package violet.labo.module.fatcalc;

import charlotte.tools.IntTools;

public class FatCalc {
	private int _radix;
	private int _basement;

	public FatCalc() {
		this(10);
	}

	public FatCalc(int radix) {
		this(radix, 30);
	}

	public FatCalc(int radix, int basement) {
		if(radix < 2 || IntTools.IMAX < radix) {
			throw new IllegalArgumentException("Bad radix: " + radix);
		}
		if(basement < 0 || IntTools.IMAX < basement) {
			throw new IllegalArgumentException("Bad basement: " + basement);
		}
		_radix = radix;
		_basement = basement;
	}

	public String calc(String leftOperand, String operator, String rightOperand) {
		int divBasement = 0;

		if("/".equals(operator)) {
			divBasement = _basement;
		}
		FatConverter conv = new FatConverter(_radix);

		conv.setString(leftOperand);
		FatFloat a = conv.getFloat();
		conv.setString(rightOperand);
		conv.exponent -= divBasement;
		FatFloat b = conv.getFloat();

		FatFloat ans = calcMain(a, operator, b, conv.rdx);

		conv.setFloat(ans);
		conv.exponent -= divBasement;
		String answer = conv.getString(_basement);

		return answer;
	}

	private FatFloat calcMain(FatFloat a, String operator, FatFloat b, int radix) {
		if("+".equals(operator)) {
			new FatFloatCalc(radix).add(a, b);
			return a;
		}
		if("-".equals(operator)) {
			new FatFloatCalc(radix).sub(a, b);
			return a;
		}
		if("*".equals(operator)) {
			return new FatFloatCalc(radix).mul(a, b);
		}
		if("/".equals(operator)) {
			return new FatFloatCalc(radix).div(a, b);
		}
		throw new IllegalArgumentException("Bad operator: " + operator);
	}

	public String power(String operand, int exponent) {
		if(exponent < 0) {
			throw new IllegalArgumentException("Bad exponent: " + exponent);
		}
		FatConverter conv = new FatConverter(_radix);

		conv.setString(operand);
		FatFloat a = conv.getFloat();

		FatFloat ans = powerMain(a, exponent, conv.rdx);

		conv.setFloat(ans);
		String answer = conv.getString(-1);

		return answer;
	}

	private FatFloat powerMain(FatFloat a, int exponent, int radix) {
		if(exponent == 0) {
			return new FatFloat(new FatUFloat(new FatUInt(new int[] { 1 }), 0), 1);
		}
		if(exponent == 1) {
			return a;
		}
		if(exponent == 2) {
			return new FatFloatCalc(radix).mul(a, a);
		}

		{
			FatFloat ans = powerMain(a, exponent / 2, radix);

			if(exponent % 2 == 1) {
				ans = new FatFloatCalc(radix).mul(ans, new FatFloatCalc(radix).mul(ans, a));
			}
			else {
				ans = new FatFloatCalc(radix).mul(ans, ans);
			}
			return ans;
		}
	}
}
