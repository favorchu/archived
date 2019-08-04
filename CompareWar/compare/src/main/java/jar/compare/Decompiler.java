package jar.compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;

public class Decompiler {

	public void decompile(File inputClass, File outputFile) throws FileNotFoundException {

		try (PrintStream printStream = new PrintStream(outputFile)) {

			OutputSinkFactory mySink = new OutputSinkFactory() {
				@Override
				public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
					// I only understand how to sink strings, regardless of what you have to give
					// me.
					return Collections.singletonList(SinkClass.STRING);
				}

				@Override
				public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
					return sinkType == SinkType.JAVA ? printStream::println : ignore -> {
					};
				}
			};

			CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).build();
			driver.analyse(Collections.singletonList(inputClass.getAbsolutePath()));

			// Close Stream
			printStream.flush();
			printStream.close();

		}
	}

}
