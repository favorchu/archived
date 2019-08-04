package competition.onedata.mvc.controller.tiles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import competition.onedata.service.tiles.TileService;

@Controller
@RequestMapping(value = "/tile")
public class TilesController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TilesController.class);

	@RequestMapping(value = "/{level}/{row}/{col}")
	public ResponseEntity<byte[]> get(@PathVariable int level,
			@PathVariable int row, @PathVariable int col) {
		try {
			TileService tilesService = TileService.getInstance();

			byte[] bytes = tilesService.get(level, row, col);
			if (bytes == null)
				return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("Content-Type", "image/png");

			return new ResponseEntity<byte[]>(bytes,responseHeaders, HttpStatus.OK);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
