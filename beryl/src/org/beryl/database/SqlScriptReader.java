package org.beryl.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/** Buffered Reader that reads SQL statements from an input stream. */
public class SqlScriptReader {

	private final BufferedReader scriptFile;

	public SqlScriptReader(InputStream stream) {
		final InputStreamReader isr = new InputStreamReader(new BufferedInputStream(stream));
		scriptFile = new BufferedReader(isr);
	}

	public SqlScriptReader(BufferedReader reader) {
		scriptFile = reader;
	}

	/** Gets the next command in the sql file.
	 * Ignores comments by simply chomping on "--".
	 * Does not support having more than 1 statement per line.
	 * @throws IOException
	 */
	public String nextStatement() throws IOException {
		StringBuilder sb = new StringBuilder();

		boolean hasStatement = false;
		int statementBreak;
		int commentPosition;
		String line;

		while((line = scriptFile.readLine()) != null) {

			// Remove comments.
			commentPosition = line.indexOf("--");
			if(commentPosition != -1) {
				line = line.substring(0, commentPosition);
			}

			// Clean the line.
			line = line.trim();
			statementBreak = line.indexOf(";");
			if(statementBreak == -1) {
				sb.append(line);
			} else {
				sb.append(line.substring(0, statementBreak));
				hasStatement = true;
				break;
			}
		}

		if(hasStatement)
			return sb.toString();
		else
			return null;
	}
}
