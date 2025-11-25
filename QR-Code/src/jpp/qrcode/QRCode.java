package jpp.qrcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class QRCode {
	boolean[][] validatedQrCode;
	Version version;
	MaskPattern pattern;
	ErrorCorrection correction;

	public QRCode(boolean[][] validatedQrCode, Version version, MaskPattern pattern, ErrorCorrection correction) {
		this.validatedQrCode = validatedQrCode;
		this.version = version;
		this.pattern = pattern;
		this.correction = correction;
	}

	public boolean[][] data() {
		return validatedQrCode;
	}

	public Version version() {
		return version;
	}

	public MaskPattern maskPattern() {
		return pattern;
	}

	public ErrorCorrection errorCorrection() {
		return correction;
	}

	public String matrixToString() {
		/*StringBuilder builder = new StringBuilder();
		for (boolean[] row : validatedQrCode) {
			for (boolean cell : row) {
				builder.append(cell ? "\u2588\u2588" : "\u2591\u2591");
			}
			builder.append("\n");
		}
		return builder.toString();*/

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < validatedQrCode.length; i++) {
			boolean[] row = validatedQrCode[i];
			for (boolean cell : row) {
				if (cell) {
					builder.append((char) 0x2588).append((char) 0x2588);
				} else {
					builder.append((char) 0x2591).append((char) 0x2591);
				}
			}
			if (i < validatedQrCode.length - 1) {
				builder.append('\n');
			}
		}
		return builder.toString();
	}

	public static QRCode createValidatedFromBooleans(boolean[][] data) throws InvalidQRCodeException {
		if (data == null)
			throw new InvalidQRCodeException("Matrix of data is null!");
		if (data.length == 0)
			throw new InvalidQRCodeException("Matrix of data is empty!");
		int numberOfRows = data.length;
		for (boolean[] datum : data) {
			if (datum.length != numberOfRows)
				throw new InvalidQRCodeException("Matrix of data is not square!");
		}

		int N = data.length;
		if (N >= 21) {
			if ((N - 17) % 4 != 0)
				throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");
			if ((N - 17) / 4 > 40)
				throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");
		} else
			throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");

		List<Integer> expectedPattern = Arrays.asList(
				0b1111111, 0b1000001, 0b1011101, 0b1011101, 0b1011101, 0b1000001, 0b1111111
		);

		if (IntStream.range(0, 7).anyMatch(rowIndex -> {
			int currentRowPattern = IntStream.range(0, 7).reduce(0, (pattern, colIndex) ->
					(pattern << 1) | (data[rowIndex][colIndex] ? 1 : 0)
			);
			return !expectedPattern.get(rowIndex).equals(currentRowPattern);
		}))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (0 , 0)");

		if (IntStream.range(0, 7).anyMatch(rowIndex -> {
			int currentRowPattern = IntStream.range(0, 7).reduce(0, (pattern, colIndex) ->
					(pattern << 1) | (data[rowIndex][data.length - 7 + colIndex] ? 1 : 0)
			);
			return !expectedPattern.get(rowIndex).equals(currentRowPattern);
		}))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (0 , " + (data.length - 7) + ")");

		if (IntStream.range(0, 7).anyMatch(rowIndex -> {
			int currentRowPattern = IntStream.range(0, 7).reduce(0, (pattern, colIndex) ->
					(pattern << 1) | (data[data.length - 7 + rowIndex][colIndex] ? 1 : 0)
			);
			return !expectedPattern.get(rowIndex).equals(currentRowPattern);
		}))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (" + (data.length - 7) + " , 0)");

		if (!IntStream.range(0, 8).allMatch(index ->
				!data[index][7] && !data[7][index] &&
						!data[index][data.length - 8] && !data[7][data.length - 1 - index] &&
						!data[data.length - 8][index] && !data[data.length - 1 - index][7]
		))
			throw new InvalidQRCodeException("No Separators Found");

		if (IntStream.range(0, data.length).noneMatch(row ->
				row == data.length - 8 && data[row][8]
		))
			throw new InvalidQRCodeException("No Dark Module Found");

		if (!IntStream.range(8, data.length - 8).allMatch(i ->
				(data[6][i] == (i % 2 == 0)) && (data[i][6] == (i % 2 == 0))
		))
			throw new InvalidQRCodeException("No Timing Pattern Found");

		int v = (N - 17) / 4;
		Version version = Version.fromNumber(v);
		int[] alignmentPositions = version.alignmentPositions();

		List<List<Boolean>> alignmentPattern = Arrays.asList(
				Arrays.asList(true, true, true, true, true),
				Arrays.asList(true, false, false, false, true),
				Arrays.asList(true, false, true, false, true),
				Arrays.asList(true, false, false, false, true),
				Arrays.asList(true, true, true, true, true)
		);

		for (int alignmentPosition : alignmentPositions) {
			for (int position : alignmentPositions) {
				if ((alignmentPosition == 6 && position == 6) ||
						(alignmentPosition == data.length - 7 && position == 6) ||
						(alignmentPosition == 6 && position == data.length - 7)) {
					continue;
				}
				if (!IntStream.range(-2, 3).allMatch(rowOffset ->
						IntStream.range(-2, 3).allMatch(colOffset ->
								data[alignmentPosition + rowOffset][position + colOffset] ==
										alignmentPattern.get(rowOffset + 2).get(colOffset + 2)
						)
				))
					throw new InvalidQRCodeException("Error in alignment Position : (" + alignmentPosition + ", " + position + ")");
			}
		}

		int firstFormat = 0;
		for (int col = 0; col <= 8; col++) {
			if (col == 6)
				continue;
			firstFormat = (firstFormat << 1) + (data[8][col] ? 1 : 0);
		}

		for (int row = 7; row >= 0; row--) {
			if (row == 6)
				continue;
			firstFormat = (firstFormat << 1) + (data[row][8] ? 1 : 0);
		}

		int lastFormat = 0;
		for (int row = data.length - 1; row > data.length - 8; row--)
			lastFormat = (lastFormat << 1) + (data[row][8] ? 1 : 0);

		for (int col = data.length - 8; col < data.length; col++)
			lastFormat = (lastFormat << 1) + (data[8][col] ? 1 : 0);

		int[] formats = new int[]{firstFormat, lastFormat};

		if (FormatInformation.fromBits(formats[0]) == null && FormatInformation.fromBits(formats[1]) == null)
			throw new InvalidQRCodeException("damaged format Information Twice");

		int formatInformationValue;
		if (FormatInformation.fromBits(formats[0]) != null)
			formatInformationValue = formats[0];
		else
			formatInformationValue = formats[1];

		FormatInformation formatInformation = FormatInformation.fromBits(formatInformationValue);

		if (v > 6) {
			List<Integer> upperRightVersionBits = new ArrayList<>();
			List<Integer> lowerLeftVersionBits = new ArrayList<>();

			for (int row = 5; row >= 0; row--) {
				for (int col = data.length - 9; col >= data.length - 11; col--) {
					upperRightVersionBits.add(data[row][col] ? 1 : 0);
				}
			}

			for (int col = 5; col >= 0; col--) {
				for (int row = data.length - 9; row >= data.length - 11; row--) {
					lowerLeftVersionBits.add(data[row][col] ? 1 : 0);
				}
			}

			int upperRightVersionValue = upperRightVersionBits.stream()
					.reduce(0, (accumulator, bit) -> (accumulator << 1) | bit);

			int lowerLeftVersionValue = lowerLeftVersionBits.stream()
					.reduce(0, (accumulator, bit) -> (accumulator << 1) | bit);

			int[] versions = new int[]{upperRightVersionValue, lowerLeftVersionValue};

			if (VersionInformation.fromBits(versions[0]) == null && VersionInformation.fromBits(versions[1]) == null)
				throw new InvalidQRCodeException("damaged version Information Twice");
			if (VersionInformation.fromBits(versions[0]) != null)
				version = VersionInformation.fromBits(versions[0]);
			else
				version = VersionInformation.fromBits(versions[1]);
		}

		ErrorCorrection errorCorrection = formatInformation.errorCorrection();
		MaskPattern maskPattern = formatInformation.maskPattern();
		return new QRCode(data, version, maskPattern, errorCorrection);
	}

	/*public static QRCode createValidatedFromBooleans(boolean[][] data) throws InvalidQRCodeException {
		if (data == null)
			throw new InvalidQRCodeException("Matrix of data is null!");
		if (data.length == 0)
			throw new InvalidQRCodeException("Matrix of data is empty!");
		int numberOfRows = data.length;
		for (boolean[] datum : data) {
			if (datum.length != numberOfRows)
				throw new InvalidQRCodeException("Matrix of data is not square!");
		}

		int N = data.length;
		if (N >= 21) {
			if ((N - 17) % 4 != 0)
				throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");
			if ((N - 17) / 4 > 40)
				throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");

		} else
			throw new InvalidQRCodeException("The size of the matrix not fits a valid QR code version.");

		if (isOrientationPatternBits(data, 0, 0))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (" + 0 + " , " + 0 + ")");

		if (isOrientationPatternBits(data, 0, data.length - 7))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (" + 0 + " , " + (data.length - 7) + ")");

		if (isOrientationPatternBits(data, data.length - 7, 0))
			throw new InvalidQRCodeException("No Orientation Pattern Found in: (" + (data.length - 7) + " , " + 0 + ")");

		if (!isSeparatorsFound(data))
			throw new InvalidQRCodeException("No Separators Found");

		if (!isDarkModule(data))
			throw new InvalidQRCodeException("No Dark Module Found");

		if (!isTimingPatterns(data))
			throw new InvalidQRCodeException("No Timing Pattern Found");

		int v = (N - 17) / 4;
		Version version = Version.fromNumber(v);
		int[] alignmentPositions = version.alignmentPositions();

		for (int alignmentPosition : alignmentPositions) {
			for (int position : alignmentPositions) {
				if (!isAlignmentPosition(data, alignmentPosition, position))
					throw new InvalidQRCodeException("Error in alignment Position : (" + alignmentPosition + ", " + position + ")");
			}
		}

		int[] formats = getFormatInformationValue(data);
		if (FormatInformation.fromBits(formats[0]) == null && FormatInformation.fromBits(formats[1]) == null)
			throw new InvalidQRCodeException("damaged format Information Twice");

		int formatInformationValue;
		if (FormatInformation.fromBits(formats[0]) != null)
			formatInformationValue = formats[0];
		else
			formatInformationValue = formats[1];

		FormatInformation formatInformation = FormatInformation.fromBits(formatInformationValue);

		if (v > 6) {


			int[] versions = getVersionValue(data);
			if (VersionInformation.fromBits(versions[0]) == null && VersionInformation.fromBits(versions[1]) == null)
				throw new InvalidQRCodeException("damaged version Information Twice");
			if (VersionInformation.fromBits(versions[0]) != null)
				version = VersionInformation.fromBits(versions[0]);
			else
				version = VersionInformation.fromBits(versions[1]);


		}

		ErrorCorrection errorCorrection = formatInformation.errorCorrection();
		MaskPattern maskPattern = formatInformation.maskPattern();
		return new QRCode(data, version, maskPattern, errorCorrection);

	}


	private static boolean isOrientationPatternBits(boolean[][] matrix, int startX, int startY) {
		List<Integer> expectedPattern = Arrays.asList(
				0b1111111, 0b1000001, 0b1011101, 0b1011101, 0b1011101, 0b1000001, 0b1111111
		);

		return IntStream.range(0, 7).anyMatch(rowIndex -> {
			int currentRowPattern = IntStream.range(0, 7).reduce(0, (pattern, colIndex) ->
					(pattern << 1) | (matrix[startX + rowIndex][startY + colIndex] ? 1 : 0)
			);
			return !expectedPattern.get(rowIndex).equals(currentRowPattern);
		});
	}// done

	private static boolean isSeparatorsFound(boolean[][] matrix) {
		int size = matrix.length;
		return IntStream.range(0, 8).allMatch(index ->
				!matrix[index][7] && !matrix[7][index] &&
						!matrix[index][size - 8] && !matrix[7][size - 1 - index] &&
						!matrix[size - 8][index] && !matrix[size - 1 - index][7]
		);
	} // done

	private static boolean isDarkModule(boolean[][] matrix) {
		int numRows = matrix.length;
		int numCols = matrix[0].length;
		int darkModulePositionRow = numRows - 8;
		int darkModulePositionCol = 8;

		return IntStream.range(0, numRows)
				.anyMatch(row -> row == darkModulePositionRow && matrix[row][darkModulePositionCol]);
	}

	private static boolean isTimingPatterns(boolean[][] matrix) {
		*//*int size = matrix.length;

		boolean isHorizontalPatternCorrect = IntStream.range(8, size - 8)
				.allMatch(col -> matrix[6][col] == (col % 2 == 0));

		boolean isVerticalPatternCorrect = IntStream.range(8, size - 8)
				.allMatch(row -> matrix[row][6] == (row % 2 == 0));

		return isHorizontalPatternCorrect && isVerticalPatternCorrect;*//*
		int size = matrix.length;

		return IntStream.range(8, size - 8).allMatch(i ->
				(matrix[6][i] == (i % 2 == 0)) && (matrix[i][6] == (i % 2 == 0))
		);
	}

	private static boolean isAlignmentPosition(boolean[][] matrix, int centerX, int centerY) {
		*//*if ((centerX == 6 && centerY == 6) ||
				(centerX == matrix.length - 7 && centerY == 6) ||
				(centerX == 6 && centerY == matrix.length - 7)) {
			return true;
		}

		List<Integer> expectedPattern = Arrays.asList(
				0b11111,
				0b10001,
				0b10101,
				0b10001,
				0b11111
		);

		return IntStream.range(0, 5).allMatch(rowIndex -> {
			int currentRowPattern = IntStream.range(0, 5).reduce(0, (pattern, colIndex) ->
					(pattern << 1) | (matrix[centerX - 2 + rowIndex][centerY - 2 + colIndex] ? 1 : 0)
			);
			return expectedPattern.get(rowIndex).equals(currentRowPattern);
		});*//*

		if ((centerX == 6 && centerY == 6) ||
				(centerX == matrix.length - 7 && centerY == 6) ||
				(centerX == 6 && centerY == matrix.length - 7)) {
			return true;
		}

		// Definieren des erwarteten Musters für die Ausrichtung als Liste von Listen
		List<List<Boolean>> expectedPattern = Arrays.asList(
				Arrays.asList(true, true, true, true, true),
				Arrays.asList(true, false, false, false, true),
				Arrays.asList(true, false, true, false, true),
				Arrays.asList(true, false, false, false, true),
				Arrays.asList(true, true, true, true, true)
		);

		// Überprüfen des Musters in der Matrix
		return IntStream.range(-2, 3).allMatch(rowOffset ->
				IntStream.range(-2, 3).allMatch(colOffset ->
						matrix[centerX + rowOffset][centerY + colOffset] ==
								expectedPattern.get(rowOffset + 2).get(colOffset + 2)
				)
		);
	}
	private static int[] getFormatInformationValue(boolean[][] data) {//not yet
		int firstFormat = 0;
		for (int col = 0; col <= 8; col++) {
			if (col == 6)
				continue;
			firstFormat = (firstFormat << 1) + (data[8][col] ? 1 : 0);
		}

		for (int row = 7; row >= 0; row--) {
			if (row == 6)
				continue;
			firstFormat = (firstFormat << 1) + (data[row][8] ? 1 : 0);
		}

		int lastFormat = 0;
		for (int row = data.length - 1; row > data.length - 8; row--)
			lastFormat = (lastFormat << 1) + (data[row][8] ? 1 : 0);

		for (int col = data.length - 8; col < data.length; col++)
			lastFormat = (lastFormat << 1) + (data[8][col] ? 1 : 0);


		return new int[] { firstFormat, lastFormat };
	}

	private static int[] getVersionValue(boolean[][] matrix) {
		int matrixSize = matrix.length;

		List<Integer> upperRightVersionBits = new ArrayList<>();
		List<Integer> lowerLeftVersionBits = new ArrayList<>();

		for (int row = 5; row >= 0; row--) {
			for (int col = matrixSize - 9; col >= matrixSize - 11; col--) {
				upperRightVersionBits.add(matrix[row][col] ? 1 : 0);
			}
		}

		for (int col = 5; col >= 0; col--) {
			for (int row = matrixSize - 9; row >= matrixSize - 11; row--) {
				lowerLeftVersionBits.add(matrix[row][col] ? 1 : 0);
			}
		}

		int upperRightVersionValue = upperRightVersionBits.stream()
				.reduce(0, (accumulator, bit) -> (accumulator << 1) | bit);

		int lowerLeftVersionValue = lowerLeftVersionBits.stream()
				.reduce(0, (accumulator, bit) -> (accumulator << 1) | bit);

		return new int[] { upperRightVersionValue, lowerLeftVersionValue };
	}*/

}
