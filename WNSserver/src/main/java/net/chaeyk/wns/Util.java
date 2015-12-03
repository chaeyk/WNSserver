package net.chaeyk.wns;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

public class Util {
	
	public static String streamToString(InputStream in, String encoding) throws IOException {
		try {
			InputStreamReader isr = new InputStreamReader(in, encoding);
			CharBuffer buffer = CharBuffer.allocate(1024);
			String str = "";
			while (isr.read(buffer) >= 0) {
				buffer.flip();
				str += buffer.toString();
				buffer.clear();
			}
			return str;
		} finally {
			in.close();
		}
	}

	public static InputStream stringToStream(String str, String encoding) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(str.getBytes(encoding));
	}

}
