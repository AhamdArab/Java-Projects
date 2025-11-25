package de.uniwue.jpp.encoder;

import java.util.*;

public class RotatingEncoder extends AbstractChainableEncoder {

	Map<Character, Character> mapping;
	int rotations;

	public RotatingEncoder(Encoder delegate) throws EncoderCreationException {
		super(delegate);
		mapping = new HashMap<>();
		for (char ch = 'a'; ch <= 'z'; ch++) {
			mapping.put(ch, ch);
		}
		rotations = 0;
	}

	public RotatingEncoder(Encoder delegate, HashMap<Character, Character> map, int rotations) throws EncoderCreationException {
		super(delegate);
		if (map == null) {
			throw new EncoderCreationException();
		}
		if (rotations < 0 || rotations > 25) {
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
		Set<Character> values = new HashMap<>(map).values() instanceof Set<?> ? (Set<Character>) new HashMap<>(map).values() : null;
		if (new HashMap<>(map).values().stream().distinct().count() != 26) {
			throw new EncoderCreationException();
		}
		if (new java.util.HashSet<>(map.values()).size() != 26) {
			throw new EncoderCreationException();
		}
		this.mapping = new HashMap<>(map);
		this.rotations = rotations;
	}

	@Override
	public char encode(char c) throws EncoderInputException {
		if (c < 'a' || c > 'z') {
			throw new EncoderInputException();
		}
		char mapped = mapping.get(c);
		char result;
		if (getDelegate() != null) {
			result = getDelegate().encode(mapped);
			return reverseLookup(result);
		} else {
			return mapped;
		}
	}

	private char reverseLookup(char value) {
		for (Map.Entry<Character, Character> entry : mapping.entrySet()) {
			if (entry.getValue() == value) {
				return entry.getKey();
			}
		}
		return value;
	}

	@Override
	public void rotate(boolean carry) {
		if (!carry) return;
		char firstValue = mapping.get('a');

		for (char c = 'a'; c < 'z'; c++) {
			mapping.put(c, mapping.get((char) (c + 1)));
		}
		mapping.put('z', firstValue);

		rotations = (rotations + 1) % 26;

		if (rotations == 0 && getDelegate() != null) {
			getDelegate().rotate(true);
		}
	}

	@Override
	public String getType() {
		return "rotating";
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RotatingEncoder other)) return false;
		return this.mapping.equals(other.mapping);
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