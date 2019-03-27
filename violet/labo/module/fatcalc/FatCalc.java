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

		conv.setString(leftOperandString);
		FatFloat leftOperand = conv.getFloat();
		conv.setString(rightOperandString);
		FatFloat rightOperand = conv.getFloat();

		FatFloatPair operands = new FatFloatPair(leftOperand, rightOperand);

		FatFloat answer = calcMain(operands, operator);

		conv.setFloat(answer);
		String answerString = conv.getString(_basement);

		return answerString;
	}

	private FatFloat calcMain(FatFloatPair operands, String operator) {
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

	public String power(String operandString, int exponent) {
		if(exponent < 0 || IntTools.IMAX < exponent) {
			throw new IllegalArgumentException("Bad exponent: " + exponent);
		}
		FatConverter conv = new FatConverter(_radix);

		conv.setString(operandString);
		FatFloat operand = conv.getFloat();

		FatFloat answer = new FatFloat(powerMain(operand.figures(), exponent), exponent % 2 == 1 ? operand.sign() : 1);

		conv.setFloat(answer);
		String answerString = conv.getString(-1);

		return answerString;
	}

	private FatUFloat powerMain(FatUFloat operand, int exponent) {
		if(exponent == 0) {
			return new FatUFloat(_radix, new int[] { 1 }, 0);
		}
		if(exponent == 1) {
			return operand;
		}
		if(exponent == 2) {
			return new FatUFloatPair(operand, operand).mul();
		}

		{
			FatUFloat answer = powerMain(operand, exponent / 2);

			if(exponent % 2 == 1) {
				answer = new FatUFloatPair(answer, new FatUFloatPair(answer, operand).mul()).mul();
			}
			else {
				answer = new FatUFloatPair(answer, answer).mul();
			}
			return answer;
		}
	}
}
