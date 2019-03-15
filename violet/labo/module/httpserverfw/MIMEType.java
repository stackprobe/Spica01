package violet.labo.module.httpserverfw;

import java.util.List;
import java.util.Map;

import charlotte.tools.CsvFileReader;
import charlotte.tools.MapTools;
import charlotte.tools.RTError;

/**
 * MIME_TYPE_FILE == kore to onaji format -> https://github.com/stackprobe/HTT/blob/master/doc/MimeType.tsv
 *
 */
public class MIMEType {
	private static MIMEType _i = null;

	public static MIMEType i() {
		if(_i == null) {
			_i = RTError.get(() -> new MIMEType());
		}
		return _i;
	}

	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";

	private Map<String, String> _extension2MIMEType = MapTools.<String>createIgnoreCase();

	private MIMEType() throws Exception {
		try(CsvFileReader reader = new CsvFileReader(Config.i().MIME_TYPE_FILE)) {
			reader.DELIMITER = '\t';

			for(; ; ) {
				List<String> row = reader.readRow();

				if(row == null) {
					break;
				}
				String extensionWithoutDot = row.get(0);
				String mimeType = row.get(1);

				String extension = "." + extensionWithoutDot;

				_extension2MIMEType.put(extension, mimeType);
			}
		}
	}

	public String getMIMEType(String extension) {
		String mimeType = _extension2MIMEType.get(extension);

		if(mimeType == null) {
			mimeType = DEFAULT_MIME_TYPE;
		}
		return mimeType;
	}
}
