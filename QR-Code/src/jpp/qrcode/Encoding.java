package jpp.qrcode;

public enum Encoding {
    NUMERIC(0b1), ALPHANUMERIC(0b10), BYTE(0b100),
    KANJI(0b1000), ECI(0b111), INVALID(-1);

    final int bits;

    Encoding(int bits) {
        this.bits = bits;
    }

    public static Encoding fromBits(int i) {
        if (i == 0b1)
            return Encoding.NUMERIC;
        else if (i == 0b10) {
            return Encoding.ALPHANUMERIC;
        } else if (i == 0b100) {
            return Encoding.BYTE;
        } else if (i == 0b1000) {
            return Encoding.KANJI;
        } else if (i == 0b111) {
            return Encoding.ECI;
        }
        /*return switch (i) {
            case 0b1 -> Encoding.NUMERIC;
            case 0b10 -> Encoding.ALPHANUMERIC;
            case 0b100 -> Encoding.BYTE;
            case 0b1000 -> Encoding.KANJI;
            case 0b111 -> Encoding.ECI;
            default -> Encoding.INVALID;
        };*/

        return Encoding.INVALID;
    }

    public int bits() {
        return bits;
    }
}
