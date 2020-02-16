package wb.t20200122;

import charlotte.tools.Base64Unit;
import charlotte.tools.BinTools;
import wb.t20200125_GBCTunnels.utils.cbc16.CRC16;

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

	private static Base64Unit.NoPadding _base64 = Base64Unit.createByC6364P("-_=").noPadding();
	private static CRC16 _crc16 = new CRC16();

	private static void test01() {
		test01_a("FQAAAN1nn_lo6E9NOJfqjAdRHZJGAAPOHg"); // Java 版 失敗 // orig: test01_a("FQAAAN1nn_lo6E9NOJfqjAdRHZJGAAPOHg"); // Java \u7248 \u5931\u6557
		test01_a("V07iTlxKm2QOhiuR8ah5DkYAf2I"); // C 版 失敗 // orig: test01_a("V07iTlxKm2QOhiuR8ah5DkYAf2I"); // C \u7248 \u5931\u6557
		test01_a("V07iTlxKm2QOhiuR8ah5DkIAdicgE9bFIs2TlGJgHupZ3R-0bBCTFrcYH_tp3oAkDsSe-gGlDkL8cssey3Vg0RyBJ5ax59xcZDJJdGLVDwIITNkoeUEXNBAv_-dot_g1pD43X9hhZpj9oarOa4rr3kWRNweY8DWpOim0j63lUb281zsapW0prNqbZ2mVfujtNLZcaMV1YsXyEb5fl6k5Dst9Rgv768ss4yRwXj0jYR3wrp3hHJhqzJMv5BCIGahFRyu4o6aF0Sqp799rkLEkOpRoCKOZ4_iXsBI92x7UI6G2FwWxhKvU5vVFOZMBilY_SBp7C1Z9pGLr5AVFQy31cJZLvozV7P4uCJaF4d66oNY4HIiodsiKh_aYMxOW__LrFkh2Aw2NXaMNtbPZ-p4Bdg5--7L4sIc2XPlzIKiUK-OXS3epg5bw2yZU66at2MektrxUvI0d0jS7q_CYL8knGQwnRB7AeE_hMHNi1hBQqNEmftfdjy_EWqap7dBhIKYX3qGJiKjgGRBRVWM72RbTVGO1qw-0wQ_4Yp1qHOz6ImHpgGid1LqhWLzlp4fgOKpCeDtlHf6IRj-x_gtI9jfsDkXzSFKd3Dc2kk_oi43s-OHy713QhHwa2WY_rngeBV0Lm2Rw5eYE3TY50VPTmjbkggrBH0I24DckcwU0x9owEJE5hGFCf8PkOR1lPXWc4d2hsZxJ7-U");

		test01_a("AAAAAMb95KavOgj4ktfSFUYAKO0c");

		test01_a("1cDMAOx73BtvKv8Y-ev5WEYARhcf");
		test01_a("1cDMAOx73BtvKv8Y-ev5WEIARNc7");

		test01_b("d5c0cc00ec7bdc1b6f2aff18f9ebf958" + "46" + "1f");
		test01_b("d5c0cc00ec7bdc1b6f2aff18f9ebf958" + "42" + "3b");
		test01_b("574ee24e5c4a9b640e862b91f1a8790e" + "42" + "2013d6c522cd939462601eea59dd1fb46c109316b7181ffb69de80240ec49efa01a50e42fc72cb1ecb7560d11c812796b1e7dc5c6432497462d50f02084cd92879411734102fffe768b7f835a43e375fd8616698fda1aace6b8aebde4591370798f035a93a29b48fade551bdbcd73b1aa56d29acda9b6769957ee8ed34b65c68c57562c5f211be5f97a9390ecb7d460bfbebcb2ce324705e3d23611df0ae9de11c986acc932fe4108819a845472bb8a3a685d12aa9efdf6b90b1243a946808a399e3f897b0123ddb1ed423a1b61705b184abd4e6f5453993018a563f481a7b0b567da462ebe40545432df570964bbe8cd5ecfe2e089685e1debaa0d6381c88a876c88a87f698331396fff2eb164876030d8d5da30db5b3d9fa9e01760e7efbb2f8b087365cf97320a8942be3974b77a98396f0db2654eba6add8c7a4b6bc54bc8d1dd234bbabf0982fc927190c27441ec0784fe1307362d61050a8d1267ed7dd8f2fc45aa6a9edd06120a617dea18988a8e019105155633bd916d35463b5ab0fb4c10ff8629d6a1cecfa2261e980689dd4baa158bce5a787e038aa42783b651dfe88463fb1fe0b48f637ec0e45f348529ddc3736924fe88b8decf8e1f2ef5dd0847c1ad9663fae781e055d0b9b6470e5e604dd3639d153d39a36e4820ac11f4236e03724730534c7da301091398461427fc3e4391d653d759ce1dda1b19c49efe5");
	}

	private static void test01_a(String s) {
		byte[] b = _base64.decode(s);
		String strB = BinTools.Hex.toString(b);

		System.out.println(strB);
	}

	private static void test01_b(String s) {
		byte[] b = BinTools.Hex.toBytes(s);
		int digest = _crc16.compute(b, 0, b.length);
		String strDigest = String.format("%08x", digest);

		System.out.println(strDigest);
	}
}
