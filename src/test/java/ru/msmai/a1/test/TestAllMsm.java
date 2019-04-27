package ru.msmai.a1.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ru.msmai.a1.test.util.TestFile;
import ru.msmai.a1.test.util.TestMsmCode;
import ru.msmai.a1.test.util.TestMsmFile;
import ru.msmai.a1.test.util.TestStr;

@RunWith(Suite.class)
@SuiteClasses({ TestStr.class, TestMsmCode.class, TestFile.class, TestMsmFile.class })
public class TestAllMsm {
}
