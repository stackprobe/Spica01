package violet.labo.module.httpserverfw;

import java.io.File;

import charlotte.tools.HTTPServer;
import charlotte.tools.HTTPServerChannel;

public class Server extends HTTPServer {
	private ContentPageInfo _root;

	public Server() {
		portNo = Config.i().PORT_NO;

		_root = new ContentPageInfo(new File(Config.i().DOR_ROOT_DIR));
	}

	@Override
	public void httpConnected(HTTPServerChannel hsChannel) throws Exception {
		hsChannel.resServer = "httpserverfw";

		String path = hsChannel.path;
		String query;

		{
			int i = path.indexOf('?');

			if(i != -1) {
				query = path.substring(i + 1);
				path = path.substring(0, i);
			}
			else {
				query = "";
			}
		}

		{
			int i = path.indexOf("://");

			if(i != -1) {
				i = path.indexOf('/', i + 3);

				if(i != -1) {
					path = path.substring(i);
				}
			}
		}

		ContentPageInfo currPage = _root;

		{
			String[] pTkns = path.split("[/]");

			for(String pTkn : pTkns) {
				if(pTkn.isEmpty() == false) {
					currPage = currPage.get(pTkn);

					if(currPage == null) {
						hsChannel.resStatus = 404;
						return;
					}
				}
			}
		}

		if(currPage.isDirectory()) {
			if(path.endsWith("/") == false) {
				hsChannel.resStatus = 301;
				hsChannel.resHeaderPairs.add(new String[] { "Location", path + "/" });
				return;
			}
			currPage = currPage.getIndexPage();

			if(currPage == null) {
				hsChannel.resStatus = 404;
				return;
			}
		}

		{
			ContextInfo context = new ContextInfo();

			context.hsChannel = hsChannel;
			context.path = path;
			context.query = query;

			try {
				//hsChannel.resContentType = "text/html; charset=UTF-8"; // moved -> ContentPageInfo.getBody()
				hsChannel.resBody = currPage.getBody(context);
			}
			catch(Throwable e) {
				e.printStackTrace();

				hsChannel.resStatus = 500;
				hsChannel.resContentType = null;
				hsChannel.resHeaderPairs.clear();
				hsChannel.resBody = null;
				return;
			}
		}
	}
}
