<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="//openlayers.org/api/OpenLayers.js" type="text/javascript"></script>
<script src="../js/map.js" type="text/javascript"></script>
<script src="../js/snapshots.js" type="text/javascript"></script>
<H1>Traffic Console</H1>
<br />

<div id="map" class="smallmap"></div>
<br />
<input type="button" value="Refresh" onclick="loadRoads()" />
<br />
<style>
.smallmap {
	width: 900px;
	height: 400px;
	border: 1px solid #ccc;
}
</style>
<script>
	var snapshotLayer;
	function loadRoads() {
		//load traffic snapshots
		var pointStyle2 = new OpenLayers.Style({
			externalGraphic : "../img/snapshot.png",
			pointRadius : 15,

		});

		var styleMapClusterClient = new OpenLayers.StyleMap({
			'default' : pointStyle2,
			"select" : pointStyle2
		});

		snapshotLayer = new OpenLayers.Layer.Vector("Snapshot Layer", {
			styleMap : styleMapClusterClient
		});
		map.addLayer(snapshotLayer);
		map.events.register("zoomend", map, function() {
			snapshotLayer.setVisibility(map.getZoom() >= 2);
		});
		snapshotLayer.setVisibility(map.getZoom() >= 2);

		selectCtrl = new OpenLayers.Control.SelectFeature(snapshotLayer, {
			autoActivate : true
		});

		snapshotLayer.events
				.on({
					'featureselected' : function(feature) {
						window
								.open(feature.feature.data.url, "snapshot",
										"toolbar=no, scrollbars=no, resizable=yes,  width=340, height=250");
					}
				});

		map.addControl(selectCtrl);
		for ( var i in snapShots) {
			var snapShot = snapShots[i];
			var point = new OpenLayers.Geometry.Point(snapShot.x, snapShot.y);
			var centerPoint = new OpenLayers.Feature.Vector(point, snapShot);

			snapshotLayer.addFeatures([ centerPoint ]);
		}

		lineLayer.removeAllFeatures();
		$.ajax({
			url : "../realtimetraffic/roaddetails",
			success : function(str) {
				var json = eval("(" + str + ")");

				for ( var i in json) {
					var item = json[i];
					var points = new Array(new OpenLayers.Geometry.Point(
							item.startEast, item.startNorth),
							new OpenLayers.Geometry.Point(item.endEast,
									item.endNorth));
					var line = new OpenLayers.Geometry.LineString(points);

					var targetStyle;
					if (item.status == 'G')
						targetStyle = {
							strokeColor : '#339900',
							strokeOpacity : 1,
							strokeWidth : 5,
							graphicZIndex : 10
						};
					else if (item.status == 'A')
						targetStyle = {
							strokeColor : '#FFCC00',
							strokeOpacity : 1,
							strokeWidth : 5,
							graphicZIndex : 100
						};
					else if (item.status == 'B')
						targetStyle = {
							strokeColor : '#CC0000',
							strokeOpacity : 1,
							strokeWidth : 5,
							graphicZIndex : 999
						};
					else {
						targetStyle = {
							strokeColor : '#666',
							strokeOpacity : 1,
							strokeWidth : 5,
							graphicZIndex : 1
						};
					}
					var lineFeature = new OpenLayers.Feature.Vector(line, null,
							targetStyle);
					lineLayer.addFeatures([ lineFeature ]);
				}
			}
		});
	}
	initMap(loadRoads);
</script>


