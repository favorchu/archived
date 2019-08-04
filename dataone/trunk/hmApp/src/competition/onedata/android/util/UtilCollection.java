package competition.onedata.android.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class UtilCollection {
	private static final int BYTE_BUFFER_SIZE = 1024 * 4;

	public static void close(Closeable is) {
		if (is != null) {
			try {
				is.close();
			} catch (Exception e) {
				;
			}
		}
	}

	public static byte[] toBytes(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buf = null;
		byte[] output = null;
		try {
			buf = new byte[BYTE_BUFFER_SIZE];
			int len;
			while ((len = is.read(buf, 0, BYTE_BUFFER_SIZE)) != -1)
				bos.write(buf, 0, len);
			bos.flush();
			output = bos.toByteArray();
		} finally {
			close(bos);
			close(is);
		}
		return output;
	}
}
