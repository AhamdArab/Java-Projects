package jpp.qrcode;

public class MaskApplier {
	/*public static void applyTo(boolean[][] matrix, MaskFunction mask, ReservedModulesMask reserved) {
		int size = matrix.length;

		// Check if the size of the matrix matches the size of the ReservedModulesMask
		if (size != reserved.size()) {
			throw new IllegalArgumentException("Matrix size does not match ReservedModulesMask size");
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// Check if the current position is not reserved and the mask function returns true
				if (!reserved.isReserved(i, j) && mask.mask(i, j)) {
					// Invert the value at the current position
					matrix[i][j] = !matrix[i][j];
				}
			}
		}
	}*/
	public static void applyTo(boolean[][] matrix, MaskFunction mask, ReservedModulesMask reserved) {
		if (matrix.length != reserved.size()) {
			throw new IllegalArgumentException("Matrix size does not match the size of the ReservedModulesMask.");
		}

		int size = matrix.length;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (!reserved.isReserved(i, j) && mask.mask(i, j)) {
					matrix[i][j] = !matrix[i][j];
				}
			}
		}
	}
}
