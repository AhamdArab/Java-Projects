package jpp.qrcode.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextReader {
	/*public static boolean[][] read(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		int rowCount = 0;
		int colCount = -1;
		StringBuilder sb = new StringBuilder();


		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			for (char c : line.toCharArray()) {
				if (c == '1' || c == '0') {
					sb.append(c);
				} else if (!Character.isWhitespace(c)) {
					throw new IOException("Invalid character found: " + c);
				}
			}
			if (colCount == -1) {
				colCount = sb.length();
			} else if (sb.length() % colCount != 0) {
				throw new IOException("Inconsistent row length.");
			}
			rowCount++;
		}

		if (rowCount == 0 || colCount == -1) {
			throw new IOException("No valid data found.");
		}

		boolean[][] result = new boolean[rowCount][colCount];
		char[] chars = sb.toString().toCharArray();
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				result[i][j] = chars[i * colCount + j] == '1';
			}
		}

		return result;
	}*/
	public static boolean[][] read(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		StringBuilder sb = new StringBuilder();
		int matrixSize = -1;

		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			for (char c : line.toCharArray()) {
				if (c != '0' && c != '1' && !Character.isWhitespace(c)) {
					throw new IOException("Invalid character found: " + c);
				}
			}
			line = line.replaceAll("\\s", ""); // Remove all whitespaces
			if (matrixSize == -1) {
				matrixSize = line.length();
			} else if (line.length() != matrixSize) {
				throw new IOException("Inconsistent row lengths found.");
			}
			sb.append(line);
		}

		if (matrixSize == -1 || sb.length() % matrixSize != 0) {
			throw new IOException("Invalid or empty input stream.");
		}

		int size = sb.length() / matrixSize;
		boolean[][] matrix = new boolean[size][matrixSize];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < matrixSize; j++) {
				matrix[i][j] = sb.charAt(i * matrixSize + j) == '1';
			}
		}

		return matrix;
	}
}
