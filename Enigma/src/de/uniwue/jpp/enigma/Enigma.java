package de.uniwue.jpp.enigma;

import de.uniwue.jpp.encoder.*;
import java.io.InputStream;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Enigma {

	Encoder head;


	public Enigma(FixedEncoder first) throws EnigmaCreationException {
		if (first == null) {
			throw new EnigmaCreationException();
		}
		this.head = first;
	}

	public Enigma(InputStream is) throws EnigmaCreationException {
		if (is == null) {
			throw new EnigmaCreationException();
		}
		Scanner scanner = new Scanner(is);
		if (!scanner.hasNextLine()) {
			throw new EnigmaCreationException();
		}
		List<EncoderConfig> blocks = new ArrayList<>();

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.isEmpty()) continue;
			if (line.startsWith("#")) {
				String header = line.substring(1).trim();
				String[] parts = header.split("\\s+");
				String type = parts[0].toLowerCase();
				int rotations = 0;
				if (type.equals("rotating")) {
					if (parts.length >= 2) {
						try {
							rotations = Integer.parseInt(parts[1]);
						} catch (NumberFormatException e) {
							throw new EnigmaCreationException();
						}
					}
				} else {
					if (parts.length != 1) {
						throw new EnigmaCreationException();
					}
				}

				Map<Character, Character> map = new HashMap<>();
				int count = 0;
				while (count < 26 && scanner.hasNextLine()) {
					String mappingLine = scanner.nextLine().trim();
					if (mappingLine.isEmpty()) continue;
					String[] mappingParts = mappingLine.split("->");
					if (mappingParts.length != 2) {
						throw new EnigmaCreationException();
					}
					char key = mappingParts[0].trim().charAt(0);
					char value = mappingParts[1].trim().charAt(0);
					map.put(key, value);
					count++;
				}
				if (count != 26) {
					throw new EnigmaCreationException();
				}
				blocks.add(new EncoderConfig(type, rotations, map));
			}
		}

		if (blocks.size() < 2) {
			throw new EnigmaCreationException();
		}
		if (!blocks.get(0).type.equals("fixed")) {
			throw new EnigmaCreationException();
		}

		for (int i = 1; i < blocks.size(); i++) {
			if (blocks.get(i).type.equals(blocks.get(i - 1).type)) {
				throw new EnigmaCreationException();
			}
		}

		int reflectingCount = 0;
		for (EncoderConfig config : blocks) {
			if (config.type.equals("reflecting")) {
				reflectingCount++;
			}
		}
		if (reflectingCount > 1) {
			throw new EnigmaCreationException();
		}

		Encoder chain = null;
		for (int i = blocks.size() - 1; i >= 0; i--) {
			EncoderConfig config = blocks.get(i);
			String type = config.type;
			Map<Character, Character> map = config.mapping;
			switch (type) {
				case "reflecting":
					try {
						chain = new Reflector(new HashMap<>(map));
					} catch (EncoderCreationException e) {
						throw new EnigmaCreationException();
					}
					break;
				case "fixed":
					if (chain == null) {
						throw new EnigmaCreationException();
					}
					try {
						chain = new FixedEncoder(chain, new HashMap<>(map));
					} catch (EncoderCreationException e) {
						throw new EnigmaCreationException();
					}
					break;
				case "rotating":
					if (chain == null) {
						throw new EnigmaCreationException();
					}
					try {
						chain = new RotatingEncoder(chain, new HashMap<>(map), config.rotations);
					} catch (EncoderCreationException e) {
						throw new EnigmaCreationException();
					}
					break;
				default:
					throw new EnigmaCreationException();
			}
		}
		if (!(chain instanceof FixedEncoder)) {
			throw new EnigmaCreationException();
		}
		this.head = chain;
	}

	public String encrypt(InputStream is) throws EnigmaEncryptionException {
		if (is == null) {
			throw new EnigmaEncryptionException();
		}
		Scanner scanner = new Scanner(is);
		StringBuilder inputText = new StringBuilder();
		while (scanner.hasNextLine()) {
			inputText.append(scanner.nextLine());
			if (scanner.hasNextLine()) {
				inputText.append("\n");
			}
		}
		String text = inputText.toString();
		if (text.isEmpty()) {
			throw new EnigmaEncryptionException();
		}
		for (char c : text.toCharArray()) {
			if (Character.isUpperCase(c)) {
				throw new EnigmaEncryptionException();
			}
		}
		StringBuilder result = new StringBuilder();
		for (char c : text.toCharArray()) {
			if (c >= 'a' && c <= 'z') {
				try {
					char encoded = head.encode(c);
					result.append(encoded);
					head.rotate(true);
				} catch (EncoderInputException e) {
					throw new EnigmaEncryptionException();
				}
			} else if (c == ' ' || c == '\n') {
				result.append(c);
			} else {
				throw new EnigmaEncryptionException();
			}
		}
		return result.toString();
	}

	@Override
	public String toString() {
		List<Encoder> encoders = new ArrayList<>();
		Encoder current = head;
		while (true) {
			encoders.add(current);
			if (current instanceof AbstractChainableEncoder) {
				current = ((AbstractChainableEncoder) current).getDelegate();
			} else {
				break;
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Encoder encoder : encoders) {
			String type = encoder.getType();
			switch (type) {
				case "fixed" -> sb.append("# fixed\n");
				case "rotating" -> sb.append("# rotating\n");
				case "reflecting" -> sb.append("# reflecting\n");
			}
			sb.append(encoder.toString());
			sb.append("\n");
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}

	private static class EncoderConfig {
		String type;
		int rotations;
		Map<Character, Character> mapping;

		EncoderConfig(String type, int rotations, Map<Character, Character> mapping) {
			this.type = type;
			this.rotations = rotations;
			this.mapping = mapping;
		}
	}
}
