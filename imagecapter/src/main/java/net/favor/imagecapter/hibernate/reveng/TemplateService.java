package net.favor.imagecapter.hibernate.reveng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateService {
	private static Logger logger = LoggerFactory.getLogger(TemplateService.class);

	private Template template;

	public TemplateService(Template template) {
		this.template = template;
	}

	public void process(Map<String, Object> input, File file) throws IOException, TemplateException {
		Writer fileWriter = null;
		try {

			fileWriter = new FileWriter(file);
			template.process(input, fileWriter);
			fileWriter.flush();
		} finally {
			if (file != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

}
