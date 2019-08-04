package jar.compare;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;

public class Hello {

	public static void main(String[] args) {
		OutputSinkFactory mySink = new OutputSinkFactory() {
			@Override
			public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
				// I only understand how to sink strings, regardless of what you have to give
				// me.
				return Collections.singletonList(SinkClass.STRING);
			}

			@Override
			public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
				return sinkType == SinkType.JAVA ? System.out::println : ignore -> {
				};
			}
		};

		CfrDriver driver = new CfrDriver.Builder().withOutputSink(mySink).build();
		driver.analyse(Collections
				.singletonList("C:/Proj/WarCompare/workspace/compare/cfr-0.135.jar_20181123.1509_43/org/benf/cfr/reader/bytecode/analysis/opgraph/op4rewriters/SwitchStringRewriter$TooOptimisticMatchException.class"));
	}

}
