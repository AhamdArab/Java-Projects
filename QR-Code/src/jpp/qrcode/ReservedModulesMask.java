package jpp.qrcode;

import java.util.Arrays;

public class ReservedModulesMask {
	/*private final boolean[][] mask;

	public ReservedModulesMask(boolean[][] mask) {
		this.mask = mask;
	}

	public boolean isReserved(int i, int j) {
		return mask[i][j];
	}

	public int size() {
		return mask.length;
	}

	public static ReservedModulesMask forVersion(Version version) {
		int size = version.size();
		boolean[][] mask = new boolean[size][size];

		// Reserve the finder patterns and separators
		reserveFinderPatterns(mask, size);

		// Reserve the alignment patterns
		reserveAlignmentPatterns(mask, version.alignmentPositions());

		// Reserve the timing patterns
		reserveTimingPatterns(mask, size);

		// Reserve the dark module (always at (8, size - 8))
		mask[8][size - 8] = true;

		// Reserve the format information areas
		reserveFormatInformationAreas(mask, size);

		// Reserve the version information areas (if version >= 7)
		if (version.number() >= 7) {
			reserveVersionInformationAreas(mask, size);
		}

		return new ReservedModulesMask(mask);
	}

	private static void reserveFinderPatterns(boolean[][] mask, int size) {
		reserveSquare(mask, 0, 0, 9);  // Top-left finder pattern + separator
		reserveSquare(mask, size - 8, 0, 8);  // Top-right finder pattern + separator
		reserveSquare(mask, 0, size - 8, 8);  // Bottom-left finder pattern + separator
	}

	private static void reserveSquare(boolean[][] mask, int startX, int startY, int size) {
		for (int i = startX; i < startX + size && i < mask.length; i++) {
			for (int j = startY; j < startY + size && j < mask[i].length; j++) {
				mask[i][j] = true;
			}
		}
	}

	private static void reserveAlignmentPatterns(boolean[][] mask, int[] positions) {
		for (int i = 0; i < positions.length; i++) {
			for (int j = 0; j < positions.length; j++) {
				if ((i != 0 || j != 0) && (i != 0 || j != positions.length - 1) && (i != positions.length - 1 || j != 0)) {
					reserveSquare(mask, positions[i] - 2, positions[j] - 2, 5);
				}
			}
		}
	}

	private static void reserveTimingPatterns(boolean[][] mask, int size) {
		for (int i = 8; i < size - 8; i++) {
			mask[i][6] = true;
			mask[6][i] = true;
		}
	}

	private static void reserveFormatInformationAreas(boolean[][] mask, int size) {
		// Top-left and horizontal part of top-right
		for (int i = 0; i <= 8; i++) {
			mask[i][8] = true;
			mask[8][i] = true;
		}
		for (int i = size - 8; i < size; i++) {
			mask[8][i] = true;
		}

		// Vertical part of bottom-left
		for (int i = 0; i < 8; i++) {
			mask[size - 1 - i][8] = true;
			mask[8][size - 1 - i] = true;
		}
	}

	private static void reserveVersionInformationAreas(boolean[][] mask, int size) {
		for (int i = 0; i < 6; i++) {
			for (int j = size - 11; j < size - 8; j++) {
				mask[i][j] = true;
				mask[j][i] = true;
			}
		}
	}*/
	private final boolean[][] mask;

	// Constructor
	public ReservedModulesMask(boolean[][] mask) {
		this.mask = mask;
	}

	// Method to check if a module at position (i, j) is reserved
	public boolean isReserved(int i, int j) {
		return mask[i][j];
	}

	// Method to get the size of the mask (in one dimension)
	public int size() {
		return mask.length;
	}

	// Static method to create a ReservedModulesMask for a given version
	public static ReservedModulesMask forVersion(Version version) {
		int size = version.size();
		boolean[][] mask = new boolean[size][size];

		// Mark the orientation patterns and separators
		markOrientationPatterns(mask, size);
		markTimingPatterns(mask, size);
		markDarkModule(mask, size);
		markAlignmentPatterns(mask, version);
		markFormatInformation(mask, size);
		if (version.number() > 6) {
			markVersionInformation(mask, size);
		}

		return new ReservedModulesMask(mask);
	}

	private static void markOrientationPatterns(boolean[][] mask, int size) {
		markSquare(mask, 0, 0, 9);      // Top-left orientation pattern and separator
		markSquare(mask, 0, size - 8, 8); // Top-right orientation pattern and separator
		markSquare(mask, size - 8, 0, 8); // Bottom-left orientation pattern and separator
	}

	private static void markTimingPatterns(boolean[][] mask, int size) {
		for (int i = 8; i < size - 8; i++) {
			mask[6][i] = true; // Horizontal timing pattern
			mask[i][6] = true; // Vertical timing pattern
		}
	}

	private static void markDarkModule(boolean[][] mask, int size) {
		mask[size - 8][8] = true;
	}

	private static void markAlignmentPatterns(boolean[][] mask, Version version) {
		int[] positions = version.alignmentPositions();
		for (int row : positions) {
			for (int col : positions) {
				if ((row == 6 && col == 6) || (row == 6 && col == mask.length - 7) || (row == mask.length - 7 && col == 6)) {
					continue;
				}
				markSquare(mask, row - 2, col - 2, 5);
			}
		}
	}

	private static void markFormatInformation(boolean[][] mask, int size) {
		for (int i = 0; i < 8; i++) {
			if (i != 6) {
				mask[8][i] = true;
				mask[i][8] = true;
			}
			mask[8][size - 1 - i] = true;
			mask[size - 1 - i][8] = true;
		}

		mask[8][8] = true;
		mask[8][size - 8] = true;
		mask[size - 8][8] = true;
	}

	private static void markVersionInformation(boolean[][] mask, int size) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 3; j++) {
				mask[size - 11 + j][i] = true;
				mask[i][size - 11 + j] = true;
			}
		}
	}

	private static void markSquare(boolean[][] mask, int row, int col, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				mask[row + i][col + j] = true;
			}
		}
	}
}
