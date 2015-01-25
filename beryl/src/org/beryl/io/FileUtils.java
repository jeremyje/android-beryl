package org.beryl.io;

import java.io.File;

import org.beryl.diagnostics.ExceptionReporter;

public class FileUtils {

	public static boolean delete(String path) {
		final File deleteTarget = new File(path);
		return delete(deleteTarget);
	}

	public static boolean delete(File targetFile) {
		boolean deleted = true;
		try {
			deleted = targetFile.delete();
			if(!deleted && ! targetFile.isFile()) {
				throw new Exception(String.format("File could not be deleted. Path: %s", targetFile.getAbsolutePath()));
			}
			deleted = true;
		} catch (Exception e) {
			deleted = false;
			ExceptionReporter.report(e);
		}

		return deleted;
	}

	public static boolean createDirectory(File directory) {
		boolean created = true;
		try {
			created = directory.mkdirs();
			if(!created && ! directory.isDirectory()) {
				throw new Exception(String.format("Directory could not be created. Path: %s", directory.getAbsolutePath()));
			}
			created = true;
		} catch (Exception e) {
			created = false;
			ExceptionReporter.report(e);
		}

		return created;
	}
}
