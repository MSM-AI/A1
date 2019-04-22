package ru.msmai.a1.test.util;

import static org.junit.Assert.assertEquals;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import ru.msmai.a1.util.UtilFile;
import ru.msmai.a1.util.UtilStr;

public class TestFile {
	@Test
	public void testReadByLine() {
		AtomicInteger count = new AtomicInteger(0);
		UtilFile.readByLine("ALIK.txt", (line) -> {
			UtilStr.justSplit(line);
			count.incrementAndGet();
			return true;
		});

		assertEquals(65474, count.get());
	}
}
