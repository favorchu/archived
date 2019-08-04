var map;
var lineLayer;

// increase reload attempts
OpenLayers.IMAGE_RELOAD_ATTEMPTS = 3;

// default drawing vector style
OpenLayers.Feature.Vector.style['default']['strokeWidth'] = '2';

function initMap(callBack) {

	var mapName = "basemap";
	OpenLayers.DOTS_PER_INCH = 96;
	var maxExtent = new OpenLayers.Bounds(789233.444425, 799612.975590592,
			873900.111178013, 850412.975642408);
	var restrictedExtent = new OpenLayers.Bounds(799186, 799837, 859854, 847618);
	var options = {
		projection : new OpenLayers.Projection("EPSG:2326"),
		scales : new Array(250000, 125000,125000/2),
		maxScale : 125000/2,
		minScale : 250000,
		units : "m",
		numZoomLevels : 3,
		maxExtent : maxExtent,
		tileSize : new OpenLayers.Size(256, 256),
		restrictedExtent : restrictedExtent,
		eventListeners : {},
		controls : [ new OpenLayers.Control.Navigation(),
				new OpenLayers.Control.PanZoomBar(),
				new OpenLayers.Control.Attribution(),
				new OpenLayers.Control.LayerSwitcher() ]
	};

	map = new OpenLayers.Map('map', options);

	var tileSrc = new OpenLayers.Layer.TMS(mapName, "../tile", {
		'type' : 'png',
		'getURL' : get_my_url,
		isBaseLayer : true,
		transitionEffect : 'resize'
	});

	var defaultStyle = new OpenLayers.Style({
		strokeColor : '#339900',
		strokeOpacity : 1,
		strokeWidth : 5,
		graphicZIndex : 10
	});

	var selectStyle = new OpenLayers.Style({
		strokeColor : '#CC0000',
		strokeOpacity : 1,
		strokeWidth : 5,
		graphicZIndex : 999
	});

	var styleMap = new OpenLayers.StyleMap({
		'default' : defaultStyle,
		'select' : selectStyle
	});

	lineLayer = new OpenLayers.Layer.Vector("Road Layer", {
		styleMap : styleMap,
		rendererOptions : {
			zIndexing : true
		}
	});

	// add layers to map object
	map.addLayer(tileSrc);
	map.addLayer(lineLayer);
	map.zoomToExtent(maxExtent);

	if (callBack)
		callBack();
}

function get_my_url(bounds) {
	var res = this.map.getResolution();
	var x = Math.round((bounds.left - map.maxExtent.left)
			/ (res * this.tileSize.w));
	var y = Math.round((map.maxExtent.top - bounds.top)
			/ (res * this.tileSize.h));
	var z = this.map.getZoom();

	var path = "../tile/" + z + "/" + y + "/" + x;
	return path;
}
