package com.jianglibo.vaadin.dashboard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SoftwarePackUtil {

	public static void pack(final Path srcFolder, final Path zipFilePath) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			Files.walkFileTree(srcFolder, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(srcFolder.relativize(file).toString()));
					Files.copy(file, zos);
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					zos.putNextEntry(new ZipEntry(srcFolder.relativize(dir).toString() + "/"));
					zos.closeEntry();
					return FileVisitResult.CONTINUE;
				}
			});
		}
	}

	public static Path unpack(Path zipFile) {
		return unpack(zipFile, null);
	}

	public static Path unpack(Path zipFile, Path outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			if (outputFolder == null) {
				outputFolder = Files.createTempDirectory(SoftwarePackUtil.class.getName());
			}
			
			if (!Files.exists(outputFolder)) {
				Files.createDirectories(outputFolder);
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile));

			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				
				if (ze.isDirectory()) {
					if (!"/".equals(fileName)) {
						new File(outputFolder + File.separator + fileName).mkdirs();
					}
				} else {
					File newFile = new File(outputFolder + File.separator + fileName);
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
			return outputFolder;

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

}
