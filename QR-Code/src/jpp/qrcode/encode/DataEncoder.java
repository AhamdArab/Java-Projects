package jpp.qrcode.encode;

import jpp.qrcode.ErrorCorrection;
import jpp.qrcode.Version;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class DataEncoder {
	public static DataEncoderResult encodeForCorrectionLevel(String str, ErrorCorrection level) {
		List<Integer> dataBits = new ArrayList<>();

		byte[] inputBytes = str.getBytes(StandardCharsets.ISO_8859_1);

		addBits(dataBits, 0x4, 4);

		Version version = null;
		for (int ver = 1; ver <= 40; ver++) {
			version = Version.fromNumber(ver);
			int charCountBits = getCharacterCountBits(ver);
			int totalDataBits = 4 + charCountBits + (8 * inputBytes.length) + 4;
			if (totalDataBits <= version.correctionInformationFor(level).totalDataByteCount() * 8) {
				break;
			}
		}

		if (!canFitInVersion(inputBytes.length, version, level)) {
			throw new IllegalArgumentException();
		}

		int charCountBits = getCharacterCountBits(version.number());
		addBits(dataBits, inputBytes.length, charCountBits);

		for (byte b : inputBytes) {
			addBits(dataBits, b & 0xFF, 8);
		}

		addBits(dataBits, 0, 4);


		while (dataBits.size() % 8 != 0) {
			dataBits.add(0);
		}

		int totalDataBits = version.correctionInformationFor(level).totalDataByteCount() * 8;
		byte[] paddingBytes = {(byte) 0xEC, (byte) 0x11};
		int padIndex = 0;
		while (dataBits.size() < totalDataBits) {
			addBits(dataBits, paddingBytes[padIndex] & 0xFF, 8);
			padIndex = (padIndex + 1) % 2;
		}

		byte[] dataBytes = bitsToBytes(dataBits);

		return new DataEncoderResult(dataBytes, version);
	}

	private static boolean canFitInVersion(int inputLength, Version version, ErrorCorrection level) {
		int charCountBits = getCharacterCountBits(version.number());
		int totalDataBits = 4 + charCountBits + (8 * inputLength) + 4;
		return totalDataBits <= version.correctionInformationFor(level).totalDataByteCount() * 8;
	}

	private static void addBits(List<Integer> bits, int value, int length) {
		for (int i = length - 1; i >= 0; i--) {
			bits.add((value >> i) & 1);
		}
	}

	private static byte[] bitsToBytes(List<Integer> bits) {
		byte[] bytes = new byte[bits.size() / 8];
		for (int i = 0; i < bytes.length; i++) {
			for (int j = 0; j < 8; j++) {
				bytes[i] = (byte) ((bytes[i] << 1) | bits.get(i * 8 + j));
			}
		}
		return bytes;
	}

	private static int getCharacterCountBits(int version) {
		if (version >= 1 && version <= 9) {
			return 8;
		} else if (version >= 10 && version <= 26) {
			return 16;
		} else {
			return 16;
		}
	}

}
