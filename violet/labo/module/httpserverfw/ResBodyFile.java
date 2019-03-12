package violet.labo.module.httpserverfw;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import charlotte.tools.RTError;

public class ResBodyFile implements Iterable<byte[]> {
	private File _f;
	private long _length;

	public ResBodyFile(File f) {
		_f = f;
		_length = f.length();
	}

	@Override
	public Iterator<byte[]> iterator() {
		return new Iterator<byte[]>() {
			private long _currPos = 0L;

			@Override
			public boolean hasNext() {
				return _currPos < _length;
			}

			@Override
			public byte[] next() {
				try(FileInputStream reader = new FileInputStream(_f)) {
					reader.skip(_currPos);
					int readSize = (int)Math.min(4L * 1024 * 1024, _length - _currPos);
					byte[] buff = new byte[readSize];

					if(reader.read(buff) != readSize) {
						throw new RTError("Bad read size: " + readSize);
					}
					_currPos += readSize;
					return buff;
				}
				catch(Throwable e) {
					throw RTError.re(e);
				}
			}
		};
	}
}
