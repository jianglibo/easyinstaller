package com.jianglibo.vaadin.dashboard.formatter;

import java.text.DecimalFormat;

public class FileLengthFormat {
	
	private static long KILO = 1024L;
	private static long MEGA = KILO*1024L;
	private static long GIGA = MEGA*1024L;
	
	

	public static String format(long length) {
		DecimalFormat df = new DecimalFormat("#.00");
		if (length >= GIGA) {
			return df.format(((double)length)/GIGA) + "G";
		} else if (length >= MEGA) {
			return df.format(((double)length)/MEGA) + "M";
		} else if (length >= KILO) {
			return df.format(((double)length)/KILO) + "K";
		} else {
			return length + "B";
		}
	}
}
