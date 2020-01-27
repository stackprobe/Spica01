package violet.gbcTunnels.utils;

public class PumpHTTPClient {
	public String proxyDomain = null;
	public int proxyPortNo = -1;

	// <---- prm

	public byte[] resBody;

	// <---- res

	public void get(String url) {
		String domainPath = url.substring(7);
		String domain;
		String path;

		{
			int index = domainPath.indexOf('/');

			if(index == -1) {
				domain = domainPath;
				path = "/";
			}
			else {
				domain = domainPath.substring(0, index);
				path = domainPath.substring(index);
			}
		}

		throw null; // TODO
	}
}
