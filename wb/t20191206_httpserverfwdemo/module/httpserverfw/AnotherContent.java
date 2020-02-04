package wb.t20191206_httpserverfwdemo.module.httpserverfw;

public abstract class AnotherContent extends RuntimeException {
	public abstract String getContentType();
	public abstract Iterable<byte[]> getResBody();
}
