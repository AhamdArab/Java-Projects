package jpp.qrcode.encode;

import jpp.qrcode.DataBlock;
import jpp.qrcode.ErrorCorrectionInformation;
import jpp.qrcode.reedsolomon.ReedSolomon;

public class DataStructurer {
	public static DataBlock[] split(byte[] data, ErrorCorrectionInformation correctionInformation) {
		int totalBlocks = correctionInformation.totalBlockCount();
		DataBlock[] blocks = new DataBlock[totalBlocks];

		int lowerDataByteCount = correctionInformation.lowerDataByteCount();
		int upperDataByteCount = lowerDataByteCount + 1;
		int numLowerDataByteBlocks = totalBlocks - (correctionInformation.totalDataByteCount() % totalBlocks);

		int dataIndex = 0;
		for (int i = 0; i < totalBlocks; i++) {
			int dataByteCount = (i < numLowerDataByteBlocks) ? lowerDataByteCount : upperDataByteCount;
			byte[] dataBytes = new byte[dataByteCount];
			System.arraycopy(data, dataIndex, dataBytes, 0, dataByteCount);
			dataIndex += dataByteCount;

			byte[] correctionBytes = ReedSolomon.calculateCorrectionBytes(dataBytes, correctionInformation.correctionBytesPerBlock());
			blocks[i] = new DataBlock(dataBytes, correctionBytes);
		}

		return blocks;
	}

	public static byte[] interleave(DataBlock[] blocks, ErrorCorrectionInformation correctionInformation) {
		int totalByteCount = correctionInformation.totalByteCount();
		byte[] result = new byte[totalByteCount];

		int maxDataByteCount = correctionInformation.lowerDataByteCount() + 1;
		int maxCorrectionByteCount = correctionInformation.correctionBytesPerBlock();

		int index = 0;

		for (int i = 0; i < maxDataByteCount; i++) {
			for (DataBlock block : blocks) {
				if (i < block.dataBytes().length) {
					result[index++] = block.dataBytes()[i];
				}
			}
		}

		for (int i = 0; i < maxCorrectionByteCount; i++) {
			for (DataBlock block : blocks) {
				if (i < block.correctionBytes().length) {
					result[index++] = block.correctionBytes()[i];
				}
			}
		}

		return result;
	}

	public static byte[] structure(byte[] data, ErrorCorrectionInformation correctionInformation) {
		if (data.length != correctionInformation.totalDataByteCount()) {
			throw new IllegalArgumentException("Data length does not match correction information");
		}

		DataBlock[] blocks = split(data, correctionInformation);
		return interleave(blocks, correctionInformation);
	}
}
