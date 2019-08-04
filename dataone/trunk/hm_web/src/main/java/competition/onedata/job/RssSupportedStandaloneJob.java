package competition.onedata.job;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public abstract class RssSupportedStandaloneJob extends StandaloneJob {

	protected List<SyndEntry> getRssEntries(String url)
			throws IllegalArgumentException, FeedException, IOException {
		URL feedUrl = new URL(url);
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed sf = input.build(new XmlReader(feedUrl));
		return sf.getEntries();
	}
}
