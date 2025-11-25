package de.uniwue.jpp.encoder;

import java.util.*;
import java.util.stream.Collectors;

public class Reflector implements Encoder {

	Map<Character, Character> mapping;

	public Reflector(HashMap<Character, Character> map) throws EncoderCreationException {
		if (map == null) {
			throw new EncoderCreationException();
		}
		if (map.size() != 26) {
			throw new EncoderCreationException();
		}

		for (char ch = 'a'; ch <= 'z'; ch++) {
			if (!map.containsKey(ch)) {
				throw new EncoderCreationException();
			}
		}

		if (new HashSet<>(map.values()).size() != 26) {
			throw new EncoderCreationException();
		}

		for (Map.Entry<Character, Character> entry : map.entrySet()) {
			char key = entry.getKey();
			char value = entry.getValue();
			if (key == value) {
				throw new EncoderCreationException();
			}
			if (!map.containsKey(value)) {
				throw new EncoderCreationException();
			}
			if (map.get(value) != key) {
				throw new EncoderCreationException();
			}
		}
		this.mapping = new HashMap<>(map);
	}


	@Override
	public char encode(char c) throws EncoderInputException {
		if (c < 'a' || c > 'z') {
			throw new EncoderInputException();
		}
		return mapping.get(c);
	}


	@Override
	public void rotate(boolean carry) {
		// Reflector rotiert nicht.
	}

	@Override
	public String getType() {
		return "reflecting";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Reflector that)) return false;
		return mapping.equals(that.mapping);
	}

	@Override
	public int hashCode() {
		return mapping.hashCode();
	}

	@Override
	public String toString() {
		List<Character> keys = new ArrayList<>(mapping.keySet());
		Collections.sort(keys);
		StringBuilder sb = new StringBuilder();
		for (Character key : keys) {
			sb.append(key)
					.append("->")
					.append(mapping.get(key))
					.append("\n");
		}
		return sb.toString().trim();
	}

}