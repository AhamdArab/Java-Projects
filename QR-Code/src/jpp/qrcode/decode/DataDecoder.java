package jpp.qrcode.decode;

import jpp.qrcode.Encoding;
import jpp.qrcode.ErrorCorrection;
import jpp.qrcode.ErrorCorrectionInformation;
import jpp.qrcode.Version;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

public class DataDecoder {
	public static Encoding readEncoding(byte[] bytes) { //not yet
		int encodingBits = (bytes[0] >> 4) & 0x0F;
		return Encoding.fromBits(encodingBits);
	}

	public static int readCharacterCount(byte[] bytes, int bitCount) {
		int charCount = 0;
		int startBit = 4;

		for (int i = 0; i < bitCount; i++) {
			int byteIndex = (startBit + i) / 8;
			int bitIndex = 7 - ((startBit + i) % 8);
			charCount = (charCount << 1) | ((bytes[byteIndex] >> bitIndex) & 1);
		}

		return charCount;
	}

    public static String decodeToString(byte[] bytes, Version version, ErrorCorrection errorCorrection) {

        ErrorCorrectionInformation info = version.correctionInformationFor(errorCorrection);
        if (bytes.length != info.totalDataByteCount()) {
            throw new IllegalArgumentException();
        }


        Encoding encoding = readEncoding(bytes);
        if (!encoding.equals(Encoding.BYTE)) {
            throw new QRDecodeException();
        }


        int versionIndex = version.number() - 1;
        if (versionIndex < 0 || versionIndex >= BIT_COUNTS.length) {
            throw new IllegalArgumentException();
        }
        int bitCount = BIT_COUNTS[versionIndex];


        int startBit = 4;
        int endBit = startBit + bitCount;

        int characterCount = IntStream.range(startBit, endBit)
                .map(i -> {
                    int byteIndex = i / 8;
                    int bitIndex = 7 - (i % 8);
                    return (bytes[byteIndex] >> bitIndex) & 1;
                })
                .reduce(0, (acc, bit) -> (acc << 1) | bit);


        int dataOffset = DATA_OFFSETS[versionIndex];

        if (characterCount > bytes.length - dataOffset) {
            throw new QRDecodeException();
        }


        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        byte[] data = new byte[characterCount];
        for (int i = 0; i < characterCount; i++) {
            int index = dataOffset - 1 + i;
            if (index + 1 < buffer.limit()) {
                int combined = buffer.getShort(index) & 0x0FF0;
                data[i] = (byte) ((combined >> 4) & 0xFF);
            }
        }

        return new String(data, StandardCharsets.ISO_8859_1);
    }

    private static final int[] BIT_COUNTS = {
            8, 8, 8, 8, 8, 8, 8, 8, 8, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
            16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16,
            16, 16, 16, 16
    };

    private static final int[] DATA_OFFSETS = {
            2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3
    };

}
