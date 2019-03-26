package violet.labo.module.fatcalc;

public class FatCalc {
	private int _radix;
	private int _basement;

	public FatCalc() {
		this(10);
	}

	public FatCalc(int radix) {
		this(radix, 0);
	}

	public FatCalc(int radix, int basement) {
		if(radix < FatConsts.RADIX_MIN || FatConsts.RADIX_MAX < radix) {
			throw new IllegalArgumentException("Bad radix: " + radix);
		}
		if(basement < 0 || FatConsts.BASEMENT_MAX < basement) {
			throw new IllegalArgumentException("Bad basement: " + basement);
		}
		_radix = radix;
		_basement = basement;
	}

	public String calc(String leftOperandString, String operator, String rightOperandString) {
		FatConv operand = new FatConv(_radix);

		FatFloat leftOperand = operand.fromString(leftOperandString);
		FatFloat rightOperand = operand.fromString(rightOperandString);

		FatFloat answer = calc(leftOperand, operator, rightOperand);

		String answerString = operand.getString(answer);

		return answerString;
	}

	private FatFloat calc(FatFloat leftOperand, String operator, FatFloat rightOperand) {
		if("+".equals(operator)) {
			leftOperand.add(rightOperand);
			return leftOperand;
		}
		if("-".equals(operator)) {
			leftOperand.sub(rightOperand);
			return leftOperand;
		}
		if("*".equals(operator)) {
			return leftOperand.mul(rightOperand);
		}
		if("/".equals(operator)) {
			return leftOperand.div(rightOperand, _basement);
		}
		throw new IllegalArgumentException("Bad operator: " + operator);
	}
}
