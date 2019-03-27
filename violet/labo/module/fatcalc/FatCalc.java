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
		if(radix < FatConsts.RADIX_MIN || FatConsts.RADIX_MAX < radix) {
			throw new IllegalArgumentException("Bad radix: " + radix);
		}
		if(basement < -IntTools.IMAX || IntTools.IMAX < basement) {
			throw new IllegalArgumentException("Bad basement: " + basement);
		}
		_radix = radix;
		_basement = basement;
	}

	public String calc(String leftOperandString, String operator, String rightOperandString) {
		FatConverter conv = new FatConverter(_radix);

		FatFloat leftOperand = conv.getFloat(leftOperandString);
		FatFloat rightOperand = conv.getFloat(rightOperandString);

		FatFloatPair operands = new FatFloatPair(leftOperand, rightOperand);

		FatFloat answer = calc(operands, operator);

		String answerString = conv.getString(answer);

		return answerString;
	}

	private FatFloat calc(FatFloatPair operands, String operator) {
		if("+".equals(operator)) {
			operands.add();
			return operands.answer();
		}
		if("-".equals(operator)) {
			operands.sub();
			return operands.answer();
		}
		if("*".equals(operator)) {
			operands.mul();
			return operands.answer();
		}
		if("/".equals(operator)) {
			operands.div(_basement);
			return operands.answer();
		}
		throw new IllegalArgumentException("Bad operator: " + operator);
	}
}
