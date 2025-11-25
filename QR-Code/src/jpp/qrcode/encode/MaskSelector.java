package jpp.qrcode.encode;

import jpp.qrcode.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MaskSelector {
	public static void placeFormatInformation(boolean[][] res, int formatInformation) {//not yet
		int temp = formatInformation;
		for (int i = 0; i < 9; i++) {
			if (i == 6) {
			}
			else {
				res[i][8] = (temp & 1) == 1;
				temp = temp >> 1;
			}
		}
		for (int i = 7; i >= 0; i--) {
			if (i == 6) {
			}
			else {
				res[8][i] = (temp & 1) == 1;
				temp = temp >> 1;
			}
		}
		temp = formatInformation;
		for (int i = res.length - 1; i >= res.length - 8; i--) {
			res[8][i] = (temp & 1) == 1;
			temp = temp >> 1;
		}
		for (int i = res.length - 7; i < res.length; i++) {
			res[i][8] = (temp & 1) == 1;
			temp = temp >> 1;
		}
	}


	public static int calculatePenaltySameColored(boolean[][] data) {
		int penalty = 0;
		int size = data.length;

		for (int i = 0; i < size; i++) {
			int sameColorCount = 1;
			for (int j = 1; j < size; j++) {
				if (data[i][j] == data[i][j - 1]) {
					sameColorCount++;
				} else {
					if (sameColorCount >= 5) {
						penalty += 3 + (sameColorCount - 5);
					}
					sameColorCount = 1;
				}
			}
			if (sameColorCount >= 5) {
				penalty += 3 + (sameColorCount - 5);
			}
		}

		for (int j = 0; j < size; j++) {
			int sameColorCount = 1;
			for (int i = 1; i < size; i++) {
				if (data[i][j] == data[i - 1][j]) {
					sameColorCount++;
				} else {
					if (sameColorCount >= 5) {
						penalty += 3 + (sameColorCount - 5);
					}
					sameColorCount = 1;
				}
			}
			if (sameColorCount >= 5) {
				penalty += 3 + (sameColorCount - 5);
			}
		}

		return penalty;
	}

	public static int calculatePenalty2x2(boolean[][] data) {
		int penalty = 0;
		int size = data.length;

		for (int i = 0; i < size - 1; i++) {
			for (int j = 0; j < size - 1; j++) {
				boolean color = data[i][j];
				if (color == data[i + 1][j] && color == data[i][j + 1] && color == data[i + 1][j + 1]) {
					penalty += 3;
				}
			}
		}

		return penalty;
	}

	public static int calculatePenaltyPattern(boolean[][] data) {
		int length = data.length;
		int[] pattern = {1, 0, 1, 1, 1, 0, 1};


		int horizontalPenalty = IntStream.range(0, length).parallel()
				.map(i -> IntStream.range(0, length - 6)
						.map(j -> {
							boolean match = IntStream.range(0, 7).allMatch(k -> (data[i][j + k] ? 1 : 0) == pattern[k]);
							if (match) {
								long whiteCountLeft = IntStream.range(1, 5).filter(k -> j - k >= 0 && !data[i][j - k]).count();
								long whiteCountRight = IntStream.range(7, 11).filter(k -> j + k < length && !data[i][j + k]).count();
								if (whiteCountLeft >= 4 || whiteCountRight >= 4) {
									return 40;
								}
							}
							return 0;
						}).sum()
				).sum();


		int verticalPenalty = IntStream.range(0, length - 6).parallel()
				.map(i -> IntStream.range(0, length)
						.map(j -> {
							boolean match = IntStream.range(0, 7).allMatch(k -> (data[i + k][j] ? 1 : 0) == pattern[k]);
							if (match) {
								long whiteCountTop = IntStream.range(1, 5).filter(k -> i - k >= 0 && !data[i - k][j]).count();
								long whiteCountBottom = IntStream.range(7, 11).filter(k -> i + k < length && !data[i + k][j]).count();
								if (whiteCountTop >= 4 || whiteCountBottom >= 4) {
									return 40;
								}
							}
							return 0;
						}).sum()
				).sum();

		return horizontalPenalty + verticalPenalty;
	}


	public static int calculatePenaltyBlackWhite(boolean[][] data) {
		int penalty = 0;
		int size = data.length;
		int totalModules = size * size;
		int blackModules = 0;

		for (boolean[] datum : data) {
			for (int j = 0; j < size; j++) {
				if (datum[j]) {
					blackModules++;
				}
			}
		}

		int percentage = (blackModules * 100) / totalModules;
		int lowerMultipleOf5 = (percentage / 5) * 5;
		int higherMultipleOf5 = lowerMultipleOf5 + 5;

		penalty += Math.min(Math.abs(lowerMultipleOf5 - 50) / 5, Math.abs(higherMultipleOf5 - 50) / 5) * 10;

		return penalty;
	}

	public static int calculatePenaltyFor(boolean[][] data) {
		return calculatePenaltySameColored(data)
				+ calculatePenalty2x2(data)
				+ calculatePenaltyPattern(data)
				+ calculatePenaltyBlackWhite(data);
	}


	public static MaskPattern maskWithBestMask(boolean[][] data, ErrorCorrection correction, ReservedModulesMask modulesMask) {
		if (data.length != modulesMask.size()) {
			throw new IllegalArgumentException();
		}

		boolean[][] tempData = new boolean[data.length][data[0].length];
		IntStream.range(0, data.length).forEach(i -> System.arraycopy(data[i], 0, tempData[i], 0, data[i].length));

		Optional<MaskPattern> bestMask;
		bestMask = Stream.of(MaskPattern.values())
				.map(maskPattern -> {
					IntStream.range(0, data.length).forEach(i -> System.arraycopy(data[i], 0, tempData[i], 0, data[i].length));

					IntStream.range(0, data.length).forEach(i ->
							IntStream.range(0, data[i].length).forEach(j -> {
								if (!modulesMask.isReserved(i, j)) {
									tempData[i][j] ^= maskPattern.maskFunction().mask(i, j);
								}
							})
					);

					int formatBits = FormatInformation.get(correction, maskPattern).formatInfo();
					placeFormatInformation(tempData, formatBits);

					int penalty = calculatePenaltyFor(tempData);

					return new Object() {
						final MaskPattern pattern = maskPattern;
						final int penaltyValue = penalty;
						final FormatInformation formatInfo = FormatInformation.get(correction, maskPattern);
					};
				})
				.min(Comparator.comparingInt(a -> a.penaltyValue))
				.map(best -> {
					IntStream.range(0, data.length).forEach(i ->
							IntStream.range(0, data[i].length).forEach(j -> {
								if (!modulesMask.isReserved(i, j)) {
									data[i][j] ^= best.pattern.maskFunction().mask(i, j);
								}
							})
					);

					placeFormatInformation(data, best.formatInfo.formatInfo());

					return best.pattern;
				});

		return bestMask.orElseThrow(IllegalStateException::new);
	}


}
