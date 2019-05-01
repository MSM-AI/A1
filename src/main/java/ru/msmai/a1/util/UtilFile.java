package ru.msmai.a1.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UtilFile implements Iterator<String>{

	private LinkedBlockingQueue<String> queue;
	
	public UtilFile(String fileName) {
		this(fileName, Runnable::run, new LinkedBlockingQueue<String>());
	}

	public UtilFile(String fileName, Predicate<String> consumer) {
		this(fileName, Runnable::run, consumer);
	}

	public UtilFile(String fileName, Executor exec, Predicate<String> consumer) {
		exec.execute(()->UtilFile.readByLine(fileName, consumer));
	}

	public UtilFile(String fileName, Executor exec, LinkedBlockingQueue<String> queue) {
		this(fileName, exec, queue::add);
		this.queue = queue;
	}

	public static void readByLine(String fileName, Predicate<String> consumer) {
		BufferedReader reader=null;
		try{
			reader = Files.newBufferedReader(Paths.get(fileName));
			for(String line=reader.readLine(); line != null; line=reader.readLine()){
				if( !consumer.test(line)){
					return;
				}
			}
		} catch(Exception e){
			throw new RuntimeException("Can't read: "+fileName);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException("Can't close: "+fileName);
			}
		}
	}


	public static void writeByLine(String fileName, Supplier<String> supplier) {
		BufferedWriter writer=null;
		try{
			writer = Files.newBufferedWriter(Paths.get(fileName), Charset.forName("UTF-8"));
			for(String line=supplier.get(); line != null; line=supplier.get()) {
				writer.write(line);
				writer.write("\n");
			}
		} catch(Exception e){
			throw new RuntimeException("Can't write: "+fileName);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException("Can't close: "+fileName);
			}
		}
	}

	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	@Override
	public String next() {
		return queue.poll();
	}

	public static List<String> readAll(String fileName) {
		List<String> list = new ArrayList<>(100000);
		readByLine(fileName, (line)->{
			list.add(line);
			return true;
		});
		return list;
	}

	public static void writeAll(String fileName,  List<String> list) {
		Iterator<String> iter = list.iterator();
		writeByLine(fileName, ()->{
			return iter.hasNext()? iter.next(): null;
		});
	}


	public static int size(String fileName) {
		AtomicInteger counter = new AtomicInteger(0);
		readByLine(fileName, (line)->{
			counter.incrementAndGet();
			return true;
		});
		return counter.get();
	}

	public static boolean isEqual(String file1, String file2) {
		BufferedReader reader1=null, reader2=null;
		try{
			reader1 = Files.newBufferedReader(Paths.get(file1));
			reader2 = Files.newBufferedReader(Paths.get(file2));
			for(String line1=reader1.readLine(),line2=reader2.readLine(); line1!=null&&line2!=null; line1=reader1.readLine(),line2=reader2.readLine()){
				if( !line1.equals(line2)){
					return false;
				}
			}
		} catch(Exception e){
			throw new RuntimeException("Can't read: "+file1+" or "+file2);
		} finally {
			try {
				reader1.close();
				reader2.close();
			} catch (IOException e) {
				throw new RuntimeException("Can't close: "+file1+" or "+file2);
			}
		}
		return true;
	}
}
