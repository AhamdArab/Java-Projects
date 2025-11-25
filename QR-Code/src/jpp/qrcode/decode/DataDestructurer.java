package jpp.qrcode.decode;

import jpp.qrcode.DataBlock;
import jpp.qrcode.ErrorCorrectionInformation;
import jpp.qrcode.reedsolomon.ReedSolomon;
import jpp.qrcode.reedsolomon.ReedSolomonException;

public class DataDestructurer {
	public static byte[] join(DataBlock[] blocks, ErrorCorrectionInformation correctionInformation) {
		byte[] result = new byte[correctionInformation.totalDataByteCount()];
		int index = 0;

		for (DataBlock block : blocks) {
			byte[] dataBytes = block.dataBytes();
			byte[] correctionBytes = block.correctionBytes();

			try {
				ReedSolomon.correct(dataBytes, correctionBytes);
			} catch (ReedSolomonException e) {
				throw new QRDecodeException();
			}

			System.arraycopy(dataBytes, 0, result, index, dataBytes.length);
			index += dataBytes.length;
		}

		return result;
	}

	public static DataBlock[] deinterleave(byte[] data, ErrorCorrectionInformation correctionInformation) {
		int totalBlocks = correctionInformation.totalBlockCount();
		DataBlock[] blocks = new DataBlock[totalBlocks];

		int lowerDataByteCount = correctionInformation.lowerDataByteCount();
		int correctionBytesPerBlock = correctionInformation.correctionBytesPerBlock();
		int upperDataByteCount = lowerDataByteCount + 1;

		int numLowerDataByteBlocks = totalBlocks - (correctionInformation.totalDataByteCount() % totalBlocks);
		int pos = 0;

		// Create data blocks and fill data bytes
		for (int i = 0; i < totalBlocks; i++) {
			int dataByteCount = (i < numLowerDataByteBlocks) ? lowerDataByteCount : upperDataByteCount;
			byte[] dataBytes = new byte[dataByteCount];
			byte[] correctionBytes = new byte[correctionBytesPerBlock];

			blocks[i] = new DataBlock(dataBytes, correctionBytes);
		}

		// Fill data bytes in zigzag order
		for (int i = 0; i < lowerDataByteCount; i++) {
			for (int j = 0; j < totalBlocks; j++) {
				if (blocks[j].dataBytes().length > i) {
					blocks[j].dataBytes()[i] = data[pos++];
				}
			}
		}

		// Fill upper data bytes if present
		for (int j = 0; j < totalBlocks; j++) {
			if (blocks[j].dataBytes().length > lowerDataByteCount) {
				blocks[j].dataBytes()[lowerDataByteCount] = data[pos++];
			}
		}

		// Fill correction bytes
		for (int i = 0; i < correctionBytesPerBlock; i++) {
			for (int j = 0; j < totalBlocks; j++) {
				blocks[j].correctionBytes()[i] = data[pos++];
			}
		}

		return blocks;
	}

	public static byte[] destructure(byte[] data, ErrorCorrectionInformation correctionInformation) {
		if (data.length != correctionInformation.totalByteCount()) {
			throw new IllegalArgumentException("Data length does not match correction information");
		}

		DataBlock[] blocks = deinterleave(data, correctionInformation);
		return join(blocks, correctionInformation);
	}
}
