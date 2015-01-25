package org.beryl.util;

import java.util.Locale;

/** Helper class for dealing with memory related functions. */
public class Memory {

	public static final long KB_IN_BYTES = 1024;
	public static final long MB_IN_BYTES = 1024 * KB_IN_BYTES;
	public static final long GB_IN_BYTES = 1024 * MB_IN_BYTES;
	public static final long TB_IN_BYTES = 1024 * GB_IN_BYTES;

	public static final long FOUR_MB_IN_BYTES = 4 * MB_IN_BYTES;

	final static long [] sizeArray = { TB_IN_BYTES, GB_IN_BYTES, MB_IN_BYTES, KB_IN_BYTES, 1 };
	final static String [] sizeUnitsArray = { "TB", "GB", "MB", "KB", "bytes" };

	/** Reasonable amount of memory to leave available when loading large objects. (4 MB) */
	public static long getReasonableMemoryCushion() {
		return FOUR_MB_IN_BYTES;
	}

	public static long bytesToKilobytes(long bytes) {
		return bytes / KB_IN_BYTES;
	}

	public static long bytesToMegabytes(long bytes) {
		return bytes / MB_IN_BYTES;
	}

	public static long bytesToGigabytes(long bytes) {
		return bytes / GB_IN_BYTES;
	}

	public static long bytesToTerabytes(long bytes) {
		return bytes / TB_IN_BYTES;
	}

	public static long freeMemory() {
		final Runtime rt = Runtime.getRuntime();
		return rt.freeMemory();
	}

	/** The maximum amount of memory available to this VM instance. */
	public static long memoryLimit() {
		final Runtime rt = Runtime.getRuntime();
		return rt.maxMemory();
	}

	public static double consumptionRatio() {
		return freeMemory() / (double)memoryLimit();
	}

	/** Converts number of bytes into a download size remaining style size string. */
	public static String toString(long bytes) {
		String byteSizeString = "<unknown> bytes";
		final int length = sizeArray.length;
		double size;
		double dBytes = (double) bytes;

		for(int i = 0; i < length; i++) {
			size = dBytes / sizeArray[i];

			if(size >= 1.0) {
				byteSizeString = String.format(Locale.ENGLISH, "%.4f %s", size, sizeUnitsArray[i]);
				break;
			}
		}

		return byteSizeString;
	}
	
	/** Calculates a reasonable maximum size limit based on the following memory constraints. */
	public static long getSizeLimit(long numExpectedInstances) {
		return getSizeLimit(64 * KB_IN_BYTES, memoryLimit(), getReasonableMemoryCushion(), numExpectedInstances);
	}
	
	/** Calculates a reasonable maximum size limit based on the following memory constraints. */
	public static long getSizeLimit(long minSize, long maxSize, long cushionSize, long numExpectedInstances) {
		return Math.max(minSize,
				Math.min(maxSize,
						(Memory.memoryLimit() - cushionSize) / numExpectedInstances
				));
	}
}
