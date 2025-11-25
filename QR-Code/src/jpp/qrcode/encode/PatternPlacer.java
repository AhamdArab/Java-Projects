package jpp.qrcode.encode;

import jpp.qrcode.Version;
import jpp.qrcode.VersionInformation;

import java.util.stream.IntStream;

public class PatternPlacer {
	public static void placeOrientation(boolean[][] res, Version version) {//not yet
		int[][] orientationPattern = {
				{1, 1, 1, 1, 1, 1, 1},
				{1, 0, 0, 0, 0, 0, 1},
				{1, 0, 1, 1, 1, 0, 1},
				{1, 0, 1, 1, 1, 0, 1},
				{1, 0, 1, 1, 1, 0, 1},
				{1, 0, 0, 0, 0, 0, 1},
				{1, 1, 1, 1, 1, 1, 1}
		};

		int n = res.length;

		// Helper method to place orientation pattern
		IntStream.range(0, 7).forEach(i ->
				IntStream.range(0, 7).forEach(j -> res[i][j] = orientationPattern[i][j] == 1)
		);

		IntStream.range(0, 7).forEach(i ->
				IntStream.range(0, 7).forEach(j -> res[n - 7 + i][j] = orientationPattern[i][j] == 1)
		);

		IntStream.range(0, 7).forEach(i ->
				IntStream.range(0, 7).forEach(j -> res[i][n - 7 + j] = orientationPattern[i][j] == 1)
		);
	}


	public static void placeTiming(boolean[][] res, Version version) {
		int size = version.size();

		for (int i = 8; i < size - 8; i++) {
			res[6][i] = i % 2 == 0;
			res[i][6] = i % 2 == 0;
		}
	}

	public static void placeAlignment(boolean[][] res, Version version) {//not yet
		int[] alignmentPositions = version.alignmentPositions();
		int n = res.length;

		int[][] alignmentPattern = {
				{1, 1, 1, 1, 1},
				{1, 0, 0, 0, 1},
				{1, 0, 1, 0, 1},
				{1, 0, 0, 0, 1},
				{1, 1, 1, 1, 1}
		};

		IntStream.range(0, alignmentPositions.length).forEach(i ->
				IntStream.range(0, alignmentPositions.length).forEach(j -> {
					int row = alignmentPositions[i];
					int col = alignmentPositions[j];

					if ((row == 6 && col == 6) ||
							(row == n - 7 && col == 6) ||
							(row == 6 && col == n - 7)) {
						return;
					}

					IntStream.range(-2, 3).forEach(k ->
							IntStream.range(-2, 3).forEach(l -> {
								res[row + k][col + l] = alignmentPattern[k + 2][l + 2] == 1;
							})
					);
				})
		);
	}

	public static void placeVersionInformation(boolean[][] res, int versionInformation) {
		int size = res.length;

		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				boolean bit = ((versionInformation >> (i * 3 + j)) & 1) == 1;
				res[size - 11 + j][i] = bit;
				res[i][size - 11 + j] = bit;
			}
		}
	}

	public static boolean[][] createBlankForVersion(Version version) {//not yet
		int size = version.size();
		boolean[][] data = new boolean[size][size];

		placeOrientation(data, version);

		placeTiming(data, version);

		placeAlignment(data, version);

		IntStream.range(0, 1).forEach(i -> data[size - 8][8] = true);

		if (version.number() > 6) {
			int versionInfo = VersionInformation.forVersion(version);
			IntStream.range(0, 6).forEach(i ->
					IntStream.range(0, 3).forEach(j -> {
						boolean bit = ((versionInfo >> (i * 3 + j)) & 1) == 1;
						data[size - 11 + j][i] = bit;
						data[i][size - 11 + j] = bit;
					})
			);
		}

		return data;

	}
}
