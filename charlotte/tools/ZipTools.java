package charlotte.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTools {
	public static byte[] compress(byte[] src) throws Exception {
		try(
				ByteArrayInputStream reader = new ByteArrayInputStream(src);
				ByteArrayOutputStream writer = new ByteArrayOutputStream();
				) {
			compress(reader, writer);
			return writer.toByteArray();
		}
	}

	public static byte[] decompress(byte[] src) throws Exception {
		try(
				ByteArrayInputStream reader = new ByteArrayInputStream(src);
				ByteArrayOutputStream writer = new ByteArrayOutputStream();
				) {
			decompress(reader, writer);
			return writer.toByteArray();
		}
	}

	public static void compress(String rFile, String wFile) throws Exception {
		try(
				FileInputStream reader = new FileInputStream(rFile);
				FileOutputStream writer = new FileOutputStream(wFile);
				) {
			compress(reader, writer);
		}
	}

	public static void decompress(String rFile, String wFile) throws Exception {
		try(
				FileInputStream reader = new FileInputStream(rFile);
				FileOutputStream writer = new FileOutputStream(wFile);
				) {
			decompress(reader, writer);
		}
	}

	public static void compress(InputStream reader, OutputStream writer) throws Exception {
		try(GZIPOutputStream gz = new GZIPOutputStream(writer)) {
			FileTools.readToEnd(reader, gz);
		}
	}

	public static void decompress(InputStream reader, OutputStream writer) throws Exception {
		try(GZIPInputStream gz = new GZIPInputStream(reader)) {
			FileTools.readToEnd(gz, writer);
		}
	}

	public static void pack(String rDir, String wFile) throws Exception {
		pack(rDir, wFile, "");
		//pack(rDir, wFile, FileTools.getFileName(rDir));
	}

	public static void pack(String rDir, String wFile, String wDir) throws Exception {
		try(FileOutputStream writer = new FileOutputStream(wFile)) {
			pack(rDir, writer, wDir);
		}
	}

	public static void pack(String rDir, OutputStream writer, String wDir) throws Exception {
		try(ZipOutputStream zip = new ZipOutputStream(writer)) {
			pack(rDir, zip, wDir);
		}
	}

	public static void pack(String rDir, ZipOutputStream writer, String wDir) throws Exception {
		for(File f : new File(rDir).listFiles()) {
			String wPath = FileTools.combine(wDir, f.getName());

			if(f.isDirectory()) {
				writer.putNextEntry(new ZipEntry(wPath + "/"));
				writer.closeEntry();

				pack(f.getCanonicalPath(), writer, wPath);
			}
			else {
				writer.putNextEntry(new ZipEntry(wPath));

				try(FileInputStream reader = new FileInputStream(f)) {
					FileTools.readToEnd(reader, writer);
				}
				writer.closeEntry();
			}
		}
	}

	public static void extract(String rFile, String wDir) throws Exception {
		extract(rFile, (entry, reader) -> writeToDir(wDir, entry, reader));
	}

	public static void extract(String rFile, IWriter writer) throws Exception {
		try(FileInputStream reader = new FileInputStream(rFile)) {
			extract(reader, writer);
		}
	}

	public static void extract(InputStream reader, IWriter writer) throws Exception {
		try(ZipInputStream zip = new ZipInputStream(reader)) {
			extract(zip, writer);
		}
	}

	public static void extract(ZipInputStream reader, IWriter writer) throws Exception {
		for(; ; ) {
			ZipEntry entry = reader.getNextEntry();

			if(entry == null) {
				break;
			}
			writer.write(entry, reader);
		}
	}

	public interface IWriter {
		void write(ZipEntry entry, ZipInputStream reader) throws Exception;
	}

	public static void writeToDir(String wDir, ZipEntry entry, ZipInputStream reader) throws Exception {
		if(entry.isDirectory()) {
			String relDir = entry.getName();
			String dir = FileTools.combine(wDir, relDir);

			FileTools.createDir(dir);
		}
		else {
			String relFile = entry.getName();
			String file = FileTools.combine(wDir, relFile);
			String dir = FileTools.getDirectoryName(file);

			FileTools.createDir(dir);

			try(FileOutputStream writer = new FileOutputStream(file)) {
				FileTools.readToEnd(reader, writer);
			}
		}
	}
}
