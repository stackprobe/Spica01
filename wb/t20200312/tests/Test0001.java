package wb.t20200312.tests;

import java.util.List;
import java.util.Map;

import charlotte.tools.ReflectTools;

public class Test0001 {
	public static void main(String[] args) {
		try {
			test01();

			System.out.println("OK!");
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	public boolean z;
	public byte b;
	public int i;
	public char h;
	public short r;
	public long l;
	public float f;
	public double d;

	public boolean[] za;
	public byte[] ba;
	public int[] ia;
	public char[] ha;
	public short[] ra;
	public long[] la;
	public float[] fa;
	public double[] da;

	public boolean[][] zaa;
	public byte[][] baa;
	public int[][] iaa;
	public char[][] haa;
	public short[][] raa;
	public long[][] laa;
	public float[][] faa;
	public double[][] daa;

	public Boolean zc;
	public Byte bc;
	public Integer ic;
	public Character ih;
	public Short ir;
	public Long lc;
	public Float fc;
	public Double dc;
	public Object oc;
	public String sc;

	public Boolean[] zca;
	public Byte[] bca;
	public Integer[] ica;
	public Character[] hca;
	public Short[] rca;
	public Long[] lca;
	public Float[] fca;
	public Double[] dca;
	public Object[] oca;
	public String[] sca;

	public Boolean[][] zcaa;
	public Byte[][] bcaa;
	public Integer[][] icaa;
	public Character[][] hcaa;
	public Short[][] rcaa;
	public Long[][] lcaa;
	public Float[][] fcaa;
	public Double[][] dcaa;
	public Object[][] ocaa;
	public String[][] scaa;

	public List<Boolean> zl;
	public List<Byte> bl;
	public List<Integer> il;
	public List<Character> hl;
	public List<Short> rl;
	public List<Long> ll;
	public List<Float> fl;
	public List<Double> dl;
	public List<Object> ol;
	public List<String> sl;

	public List<boolean[]> zal;
	public List<byte[]> bal;
	public List<int[]> ial;
	public List<char[]> hal;
	public List<short[]> ral;
	public List<long[]> lal;
	public List<float[]> fal;
	public List<double[]> dal;
	public List<Object[]> oal;
	public List<String[]> sal;

	public Map<String, String> ssm;
	public Map<String, Integer> sim;
	public Map<Integer, String> ism;
	public Map<Integer, Integer> iim;

	private static void test01() {
		for(ReflectTools.FieldUnit field : ReflectTools.getFields(Test0001.class)) {
			String fieldName = field.inner.getName();
			String fieldType = field.inner.getType().getName();

			System.out.println(fieldName + " ---> " + fieldType);
		}
	}
}
