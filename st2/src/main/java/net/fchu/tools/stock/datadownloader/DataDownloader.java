package net.fchu.tools.stock.datadownloader;

import net.fchu.minapp.service.HttpService;

import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DataDownloader {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataDownloader.class);

	private static final String SPLITER = "_";

	private HttpService httpService = new HttpService();



	protected abstract String getDownloadSuffix();

	protected abstract String getUrl(String stock);

}
