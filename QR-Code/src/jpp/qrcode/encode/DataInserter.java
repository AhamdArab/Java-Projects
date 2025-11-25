package jpp.qrcode.encode;

import jpp.qrcode.DataPositions;
import jpp.qrcode.ReservedModulesMask;

import java.util.stream.IntStream;

public class DataInserter {
	public static void insert(boolean[][] target, ReservedModulesMask mask, byte[] data) {
		DataPositions dataPositions = new DataPositions(mask);
		int[] bitIndex = {0};
		int[] byteIndex = {0};
		boolean[] moreData = {true};// not yet

		IntStream.generate(() -> 1)
				.takeWhile(i -> moreData[0] && byteIndex[0] < data.length)
				.filter(i -> {
					while (mask.isReserved(dataPositions.i(), dataPositions.j())) {
						if (!dataPositions.next()) {
							moreData[0] = false;
							return false;
						}
					}
					return true;
				})
				.forEach(i -> {
					int row = dataPositions.i();
					int col = dataPositions.j();

					boolean bit = ((data[byteIndex[0]] >> (7 - bitIndex[0])) & 1) == 1;

					target[row][col] = bit;

					bitIndex[0]++;

					if (bitIndex[0] == 8) {
						bitIndex[0] = 0;
						byteIndex[0]++;
					}

					if (!dataPositions.next()) {
						moreData[0] = false;
					}
				});
	}

}
