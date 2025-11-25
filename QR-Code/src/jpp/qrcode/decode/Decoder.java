package jpp.qrcode.decode;

import jpp.qrcode.*;
import jpp.qrcode.reedsolomon.ReedSolomonException;

public class Decoder {
	public static String decodeToString(QRCode qrCode) {
		// Erstellen einer Kopie der QR-Code-Matrix
		boolean[][] matrix = qrCode.data();
		boolean[][] copiedMatrix = new boolean[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			System.arraycopy(matrix[i], 0, copiedMatrix[i], 0, matrix[i].length);
		}

		// Demaskieren der Matrix
		MaskPattern maskPattern = qrCode.maskPattern();
		MaskFunction maskFunction = maskPattern.maskFunction();
		ReservedModulesMask reservedModulesMask = ReservedModulesMask.forVersion(qrCode.version());

		MaskApplier.applyTo(copiedMatrix, maskFunction, reservedModulesMask);

		// Extrahieren der Daten aus der demaskierten Matrix
		DataPositions dataPositions = new DataPositions(reservedModulesMask);
		byte[] dataBytes = new byte[qrCode.version().totalByteCount()];
		int byteIndex = 0;
		int bitIndex = 0;

		do {
			int i = dataPositions.i();
			int j = dataPositions.j();
			if (!reservedModulesMask.isReserved(i, j)) {
				if (copiedMatrix[i][j]) {
					dataBytes[byteIndex] |= (1 << (7 - bitIndex));
				}
				bitIndex++;
				if (bitIndex == 8) {
					byteIndex++;
					bitIndex = 0;
				}
			}
		} while (dataPositions.next() && byteIndex < dataBytes.length);

		// Dekodieren der extrahierten Daten
		Version version = qrCode.version();
		ErrorCorrection errorCorrection = qrCode.errorCorrection();
		ErrorCorrectionInformation errorCorrectionInformation = version.correctionInformationFor(errorCorrection);

		try {
			byte[] correctedData = DataDestructurer.destructure(dataBytes, errorCorrectionInformation);
			return DataDecoder.decodeToString(correctedData, version, errorCorrection);
		} catch (QRDecodeException e) {
			throw new QRDecodeException("Failed to decode QR code data", e);
		}
	}
}
