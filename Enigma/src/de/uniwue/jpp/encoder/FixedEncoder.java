package de.uniwue.jpp.encoder;

import java.util.*;

public class FixedEncoder extends AbstractChainableEncoder {

	Map<Character, Character> mapping;
	Map<Character, Character> inverseMapping;

	public FixedEncoder(Encoder delegate, Map<Character, Character> map) throws EncoderCreationException {
		super(delegate);
		if (map == null) {
			throw new EncoderCreationException();
		}
		if (map.size() != 26) {
			throw new EncoderCreationException();
		}
		for (Map.Entry<Character, Character> entry : map.entrySet()) {
			char key = entry.getKey();
			char value = entry.getValue();
			if (key < 'a' || key > 'z') {
				throw new EncoderCreationException();
			}
			if (value < 'a' || value > 'z') {
				throw new EncoderCreationException();
			}
		}
		for (char ch = 'a'; ch <= 'z'; ch++) {
			if (!map.containsKey(ch)) {
				throw new EncoderCreationException();
			}
		}
		Set<Character> values = new HashSet<>(map.values());
		if (values.size() != 26) {
			throw new EncoderCreationException();
		}
		this.mapping = new HashMap<>(map);
		this.inverseMapping = new HashMap<>();
		for (Map.Entry<Character, Character> entry : this.mapping.entrySet()) {
			this.inverseMapping.put(entry.getValue(), entry.getKey());
		}
	}

	@Override
	public char encode(char c) throws EncoderInputException {
		if (c < 'a' || c > 'z') {
			throw new EncoderInputException();
		}
		char forward = mapping.get(c);
		char encoded;
		if (getDelegate() != null) {
			encoded = getDelegate().encode(forward);
		} else {
			encoded = forward;
		}
		return inverseMapping.get(encoded);
	}

	@Override
	public void rotate(boolean carry) {
		if (carry && getDelegate() != null) {
			getDelegate().rotate(true);
		}
	}

	@Override
	public String getType() {
		return "fixed";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FixedEncoder)) return false;
		FixedEncoder that = (FixedEncoder) o;
		return mapping.equals(that.mapping);
	}

	@Override
	public int hashCode() {
		return mapping.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Character> keys = new ArrayList<>(mapping.keySet());
		Collections.sort(keys);
		for (char key : keys) {
			sb.append(key).append("->").append(mapping.get(key)).append("\n");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
}
