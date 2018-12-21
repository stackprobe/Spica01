package charlotte.tools;

public class SockChannelError extends RuntimeException {
	public SockChannelError() {
		super();
	}

	public SockChannelError(String message) {
		super(message);
	}

	public SockChannelError(String message, Throwable e) {
		super(message, e);
	}
}
