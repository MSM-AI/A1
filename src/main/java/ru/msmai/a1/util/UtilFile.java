package ru.msmai.a1.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
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

}
