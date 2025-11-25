package jpp.qrcode.encode;

import jpp.qrcode.*;

public class Encoder {
	public static QRCode createFromString(String msg, ErrorCorrection correction) {

		DataEncoderResult encodedResult = DataEncoder.encodeForCorrectionLevel(msg, correction);


		byte[] structuredData = DataStructurer.structure(encodedResult.bytes(),
				encodedResult.version().correctionInformationFor(correction));


		Version version = encodedResult.version();
		boolean[][] qrMatrix = PatternPlacer.createBlankForVersion(version);


		ReservedModulesMask reservedModulesMask = ReservedModulesMask.forVersion(version);


		DataInserter.insert(qrMatrix, reservedModulesMask, structuredData);


		MaskPattern bestMask = MaskSelector.maskWithBestMask(qrMatrix, correction, reservedModulesMask);


		return new QRCode(qrMatrix, version, bestMask, correction);
	}
}
