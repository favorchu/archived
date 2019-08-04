package photo2s;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.kohsuke.args4j.Option;

public class Arguments {

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	@Option(name = "-src", required = true, usage = "Source Folder of the media")
	private String src;

	@Option(name = "-dest", required = true, usage = "Destination Folder of the media")
	private String dest;

	@Option(name = "-del", usage = "Delete the moved source files?")
	private boolean del = false;

	@Option(name = "-fromDt", usage = "From Date e.g. 20171201")
	private String fromDt;

	@Option(name = "-toDt", usage = "To Date e.g. 20171201")
	private String toDt;

	public String getFromDt() {
		return fromDt;
	}

	public Date getFromDate() throws ParseException {
		return fromDt == null ? null : DATE_FORMAT.parse(fromDt);
	}

	public void setFromDt(String fromDt) {
		this.fromDt = fromDt;
	}

	public String getToDt() {
		return toDt;
	}

	public void setToDt(String toDt) {
		this.toDt = toDt;
	}

	public Date getToDate() throws ParseException {
		return toDt == null ? null : DATE_FORMAT.parse(toDt);
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public boolean isDel() {
		return del;
	}

	public void setDel(boolean del) {
		this.del = del;
	}

}
