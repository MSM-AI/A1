package ru.msmai.a1.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import ru.msmai.a1.test.util.TestFile;
import ru.msmai.a1.test.util.TestMsm;
import ru.msmai.a1.test.util.TestStr;

@RunWith(Suite.class)
@SuiteClasses({ TestStr.class, TestFile.class, TestMsm.class })
public class TestAllMsm {
}
