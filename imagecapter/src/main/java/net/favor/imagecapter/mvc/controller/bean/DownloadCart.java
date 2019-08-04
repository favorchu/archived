package net.favor.imagecapter.mvc.controller.bean;

import java.util.ArrayList;
import java.util.List;

public class DownloadCart {

	private List<String> toDownloadImageKeys = new ArrayList<String>();

	public List<String> getToDownloadImageKeys() {
		return toDownloadImageKeys;
	}

	public void setToDownloadImageKeys(List<String> toDownloadImageKeys) {
		this.toDownloadImageKeys = toDownloadImageKeys;
	}

}
