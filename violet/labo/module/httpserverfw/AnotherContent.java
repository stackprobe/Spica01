package violet.labo.module.httpserverfw;

public abstract class AnotherContent extends RuntimeException {
	public abstract String getContentType();
	public abstract Iterable<byte[]> getResBody();

	@Override
	public String toString() {
		return "Content-Type: " + getContentType() + ", hashCode: " + hashCode();
	}
}
