<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="//openlayers.org/api/OpenLayers.js" type="text/javascript"></script>
<script src="../js/map.js" type="text/javascript"></script>
<H1>Traffic Console</H1>
<br />
<div id="map" class="smallmap"></div>
<div style="width: 300pt; height: 400pt; float: left; overflow: scroll;"
	id="rightList" name="rightList"></div>

<br />

<br />
<style>
.smallmap {
	width: 500px;
	height: 400px;
	border: 1px solid #ccc;
	float: left;
}
</style>
<script>
	function loadRoads() {
		$
				.ajax({
					url : "../realtimetraffic/roads",
					success : function(str) {
						var json = eval("(" + str + ")");

						lineLayer.removeAllFeatures();

						for ( var i in json) {
							var item = json[i];
							var points = new Array(
									new OpenLayers.Geometry.Point(
											item.startEast, item.startNorth),
									new OpenLayers.Geometry.Point(item.endEast,
											item.endNorth));
							var line = new OpenLayers.Geometry.LineString(
									points);

							var lineFeature = new OpenLayers.Feature.Vector(
									line, item);

							lineLayer.addFeatures([ lineFeature ]);
						}

						selectCtrl = new OpenLayers.Control.SelectFeature(
								lineLayer);

						map.addControl(selectCtrl);
					}
				});

		$("#rightList").load("list");

	}
	initMap(loadRoads);
	function show(key) {
		var metaKey = key;
		$
				.ajax({
					url : "accident/" + metaKey,
					success : function(json) {
						try {
							selectCtrl.unselectAll();
						} catch (e) {
							console.log(e);
						}

						for ( var i in json) {
							var item = json[i];
							for ( var j in lineLayer.features) {
								var feature = lineLayer.features[j];
								if (feature.data.startPointKey == item.sectionStartKey
										&& feature.data.endPointKey == item.sectionEndKey) {
									selectCtrl.select(feature);
								}
							}

						}

						if (lineLayer.selectedFeatures
								&& lineLayer.selectedFeatures.length > 1) {
							var item = lineLayer.selectedFeatures[0];
							var lonlat = new OpenLayers.LonLat(item.startEast,
									item.startNorth);
							map.panTo(lonlat);
						}

					}
				});
	}
	function del(key) {
		var metaKey = key;

		if (!confirm("Confirm to del?"))
			return;

		$.ajax({
			url : "del/" + metaKey,
			success : function() {
				location.reload();
			}
		});
	}
</script>


