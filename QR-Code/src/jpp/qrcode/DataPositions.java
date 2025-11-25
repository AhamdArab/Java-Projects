package jpp.qrcode;

public class DataPositions {
	ReservedModulesMask mask;
	int size;
	int index;
	int offset;
	boolean topToDown;
	boolean change;

	public DataPositions(ReservedModulesMask mask) {// not yet
		this.mask = mask;
		this.size = mask.size();
		this.index = size * 2 - 1;
		this.offset = size - 2;
		this.topToDown = false;
		this.change = false;
	}

	public int i() {
		return topToDown ? Math.floorDiv((size * 2 - 1) - index, 2) : Math.floorDiv(index, 2);
	}

	public int j() {
		return topToDown ? (Math.floorMod((size * 2 - 1) - index, 2) ^ 1) + offset : Math.floorMod(index, 2) + offset;
	}

	public boolean next() {
		if (i() == size - 1 && j() == 0) {
			return false;
		}

		index--;
		if (index == -1) {
			index = size * 2 - 1;
			offset -= 2;
			topToDown = !topToDown;
		}

		if (j() < 6 && !change) {
			offset--;
			change = true;
		}

		if (mask.isReserved(i(), j())) {
			return next();
		}

		return i() != size - 1 || j() != 0;
	}

}
