package charlotte.tools;

public class HandleDam {
	/**
	 * 終了時に hDam に add した全てのハンドルを close する。 // orig: * \u7d42\u4e86\u6642\u306b hDam \u306b add \u3057\u305f\u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002
	 * @param routine hDame を渡して実行する routine // orig: * @param routine hDame \u3092\u6e21\u3057\u3066\u5b9f\u884c\u3059\u308b routine
	 * @return routine の戻り値 // orig: * @return routine \u306e\u623b\u308a\u5024
	 * @throws Exception
	 */
	public static <T> T section_get(FunctionEx<HandleDam, T> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			return routine.apply(hDam);
		}
		finally {
			hDam.burst();
		}
	}

	/**
	 * 終了時に hDam に add した全てのハンドルを close する。 // orig: * \u7d42\u4e86\u6642\u306b hDam \u306b add \u3057\u305f\u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002
	 * @param routine hDame を渡して実行する処理 // orig: * @param routine hDame \u3092\u6e21\u3057\u3066\u5b9f\u884c\u3059\u308b\u51e6\u7406
	 * @throws Exception
	 */
	public static void section(ConsumerEx<HandleDam> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			routine.accept(hDam);
		}
		finally {
			hDam.burst();
		}
	}

	/**
	 * 例外発生時のみ hDam に add した全てのハンドルを close する。 // orig: * \u4f8b\u5916\u767a\u751f\u6642\u306e\u307f hDam \u306b add \u3057\u305f\u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002
	 * @param routine >hDam を渡して実行する routine // orig: * @param routine >hDam \u3092\u6e21\u3057\u3066\u5b9f\u884c\u3059\u308b routine
	 * @return routine の戻り値 // orig: * @return routine \u306e\u623b\u308a\u5024
	 * @throws Exception
	 */
	public static <T> T transaction_get(FunctionEx<HandleDam, T> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			return routine.apply(hDam);
		}
		catch(Throwable e) {
			hDam.burst(e);
			throw null; // never
		}
	}

	/**
	 * 例外発生時のみ hDam に add した全てのハンドルを close する。 // orig: * \u4f8b\u5916\u767a\u751f\u6642\u306e\u307f hDam \u306b add \u3057\u305f\u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002
	 * @param routine hDam を渡して実行する処理 // orig: * @param routine hDam \u3092\u6e21\u3057\u3066\u5b9f\u884c\u3059\u308b\u51e6\u7406
	 * @throws Exception
	 */
	public static void transaction(ConsumerEx<HandleDam> routine) throws Exception {
		HandleDam hDam = new HandleDam();
		try {
			routine.accept(hDam);
		}
		catch(Throwable e) {
			hDam.burst(e);
			//throw null; // never
		}
	}

	private IStack<AutoCloseable> _handles = new StackUnit<AutoCloseable>();

	/**
	 * ハンドルを追加する。 // orig: * \u30cf\u30f3\u30c9\u30eb\u3092\u8ffd\u52a0\u3059\u308b\u3002
	 * @param handle 追加するハンドル // orig: * @param handle \u8ffd\u52a0\u3059\u308b\u30cf\u30f3\u30c9\u30eb
	 * @return 追加したハンドル // orig: * @return \u8ffd\u52a0\u3057\u305f\u30cf\u30f3\u30c9\u30eb
	 */
	public <T extends AutoCloseable> T add(T handle) {
		_handles.push(handle);
		return handle;
	}

	/**
	 * 全てのハンドルを close する。<br> // orig: * \u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002<br>
	 * 1つ以上の close が例外を投げた場合、全ての close を実行し終えた後で RTError にまとめて投げる。 // orig: * 1\u3064\u4ee5\u4e0a\u306e close \u304c\u4f8b\u5916\u3092\u6295\u3052\u305f\u5834\u5408\u3001\u5168\u3066\u306e close \u3092\u5b9f\u884c\u3057\u7d42\u3048\u305f\u5f8c\u3067 RTError \u306b\u307e\u3068\u3081\u3066\u6295\u3052\u308b\u3002
	 * @throws Exception
	 */
	public void burst() throws Exception {
		ExceptionDam.section(eDam -> burst(eDam));
	}

	/**
	 * 全てのハンドルを close する。<br> // orig: * \u5168\u3066\u306e\u30cf\u30f3\u30c9\u30eb\u3092 close \u3059\u308b\u3002<br>
	 * 全ての close を実行し終えた後で e と close が投げた例外を RTError にまとめて投げる。 // orig: * \u5168\u3066\u306e close \u3092\u5b9f\u884c\u3057\u7d42\u3048\u305f\u5f8c\u3067 e \u3068 close \u304c\u6295\u3052\u305f\u4f8b\u5916\u3092 RTError \u306b\u307e\u3068\u3081\u3066\u6295\u3052\u308b\u3002
	 * @throws Exception
	 */
	public void burst(Throwable cause) throws Exception {
		ExceptionDam.section(cause, eDam -> burst(eDam));
	}

	private void burst(ExceptionDam eDam) {
		for(AutoCloseable handle : IteratorTools.once(IQueues.iterator(_handles))) {
			eDam.invoke(() -> handle.close());
		}
	}
}
