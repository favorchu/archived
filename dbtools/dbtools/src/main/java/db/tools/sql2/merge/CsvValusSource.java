package db.tools.sql2.merge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvValusSource {

	private Queue<CSVRecord> queue = new LinkedList<CSVRecord>();
	private List<String> header = new ArrayList<String>();

	public CsvValusSource(File file) throws IOException {
		super();
		init(file);
	}

	private void init(File file) throws IOException, FileNotFoundException {
		try (Reader in = new FileReader(file.getAbsolutePath())) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
			records.forEach(v -> queue.add(v));
			// Get the Header
			if (!queue.isEmpty()) {
				CSVRecord record = queue.poll();
				record.forEach(s -> header.add(s));
			}

		}
	}

	public Map<String, String> getNewRow() {
		if (!queue.isEmpty()) {
			CSVRecord record = queue.poll();
			Map<String, String> map = new TreeMap<String, String>();
			for (int i = 0, j = header.size(); i < j; i++) {
				map.put(header.get(i), record.get(i));
			}
			return map;
		}
		return null;
	}

}
