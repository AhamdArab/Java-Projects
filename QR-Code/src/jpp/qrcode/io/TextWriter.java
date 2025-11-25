package jpp.qrcode.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class TextWriter {
	public static void write(OutputStream out, boolean[][] data) throws IOException {
		PrintWriter writer = new PrintWriter(out);

		for (boolean[] row : data) {
			for (boolean value : row) {
				writer.print(value ? '1' : '0');
			}
			writer.println();
		}

		writer.flush();
	}
}
