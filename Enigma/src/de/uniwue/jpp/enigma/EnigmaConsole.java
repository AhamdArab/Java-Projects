package de.uniwue.jpp.enigma;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class EnigmaConsole {

	public static void main(String[] args) {
		EnigmaConsole console = new EnigmaConsole();
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		console.run(inputStream, outputStream);
	}

	public void run(InputStream is, OutputStream os) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(os), true);

		writer.println("Enigma wird gestartet.");


		de.uniwue.jpp.enigma.Enigma enigma = null;
		while (enigma == null) {
			writer.println("Bitte geben Sie den Pfad zu einer Konfiguration an:");
			String configPath = null;
			try {
				configPath = reader.readLine();
			} catch (IOException e) {
				writer.println("Fehler beim Lesen der Eingabe.");
				continue;
			}
			File configFile = new File(configPath);
			if (!configFile.exists()) {
				writer.println("Datei nicht gefunden!");
				continue;
			}
			try (FileInputStream fis = new FileInputStream(configFile)) {
				enigma = new Enigma(fis);
			} catch (FileNotFoundException e) {
				writer.println("Datei nicht gefunden!");
			} catch (EnigmaCreationException e) {
				writer.println("Fehlerhafte Enigma-Datei!");
			} catch (IOException e) {
				writer.println("Fehler beim Verarbeiten der Datei!");
			}
		}

		while (true) {
			writer.println("Geben Sie ihren Ver- oder Entschlüsselten Text ein:");
			String inputText = null;
			try {
				inputText = reader.readLine();
			} catch (IOException e) {
				writer.println("Fehler beim Lesen der Eingabe.");
				continue;
			}

			boolean valid = true;
			if (inputText.isEmpty()) {
				valid = false;
			} else {
				for (char c : inputText.toCharArray()) {
					if (!((c >= 'a' && c <= 'z') || c == ' ' || c == '\n')) {
						valid = false;
						break;
					}
				}
			}
			if (!valid) {
				writer.println("Nicht erlaubte Symbole!");
				continue;
			}

			try {
				String result = enigma.encrypt(new ByteArrayInputStream(inputText.getBytes(StandardCharsets.UTF_8)));
				writer.println("En- oder Dekodierter Text:" + result);
			} catch (EnigmaEncryptionException e) {
				writer.println("Nicht erlaubte Symbole!");
				continue;
			}
			break;
		}

		writer.println("Vielen Dank für die Nutzung der Enigma.");
	}
}
