package jpp.qrcode.decode;

import jpp.qrcode.DataPositions;
import jpp.qrcode.ReservedModulesMask;

public class DataExtractor {
	public static byte[] extract(boolean[][] matrix, ReservedModulesMask reservedModules, int byteCount) {
		if (matrix.length != reservedModules.size()) {
			throw new IllegalArgumentException("Matrix size does not match the size of the ReservedModulesMask.");
		}

		byte[] result = new byte[byteCount];
		DataPositions dataPositions = new DataPositions(reservedModules);
		int bitIndex = 0;

		while (bitIndex < byteCount * 8) {
			int i = dataPositions.i();
			int j = dataPositions.j();

			if (!reservedModules.isReserved(i, j)) {
				int byteIndex = bitIndex / 8;
				int bitPosition = 7 - (bitIndex % 8);

				if (matrix[i][j]) {
					result[byteIndex] |= (1 << bitPosition);
				}
				bitIndex++;
			}

			if (!dataPositions.next()) {
				break;
			}
		}

		return result;
	}
}
