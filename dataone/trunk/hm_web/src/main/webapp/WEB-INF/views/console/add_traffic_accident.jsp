<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script src="//openlayers.org/api/OpenLayers.js" type="text/javascript"></script>
<script src="../js/map.js" type="text/javascript"></script>

<h1>Add a New Traffic Accident</h1>
<div id="map" class="smallmap"></div>
<br />
<input value="Select Mode" type="button" onclick="changeToSelectMode()" />
<input value="Mouse Mode" type="button" onclick="changeToMouseMode()" />
<br />
<input value="Unselect All" type="button" onclick="unselectAll()" />
<input value="Submit" type="button" onclick="submit()" />
<br />
<br />
Time:
<input type="text" id="dateTime">
<br />
<textarea rows="10" cols="100" id="accidentDetail">
[示範資料]【馬路的事交通消息】較早前受到修路工程影響︰連翔道近柯士甸道西全線需要暫時封閉。柯士甸道西的車輛仍然暫時不能轉入連翔道。廣東道的車輛已經可以左轉入柯士甸道西。而九龍公園徑去佐敦方向的交通已經回復正常。
</textarea>
<style>
.smallmap {
	width: 900px;
	height: 400px;
	border: 1px solid #ccc;
}
</style>
<script>
	var selectCtrl;

	function changeToSelectMode() {
		selectCtrl.activate();

	}
	function changeToMouseMode() {
		selectCtrl.deactivate();
	}

	function submit() {
		if (lineLayer.selectedFeatures.length < 1) {
			alert("Please select road");
			return;
		}
		if (isBlank($("#dateTime").val())) {
			alert("Please fill in the datetime box");
			return;
		}
		if (isBlank($("#accidentDetail").val())) {
			alert("Please fill in the content");
			return;
		}

		var accidentDetail = $("#accidentDetail").val();
		var time = $("#dateTime").val();
		var ids = "";
		var features = lineLayer.selectedFeatures;
		for ( var i in features) {
			var data = features[i];
			ids += data.data.startPointKey + ":" + data.data.endPointKey + ";";
		}

		$.post("add", {
			"content" : accidentDetail,
			"ids" : ids,
			"time" : time
		}, function() {
			alert("Added!");
			location.href = "../trafficconsole/";

		}).error(function() {
			alert("can not apply the content");
		});

	}

	function unselectAll() {
		if (selectCtrl) {
			selectCtrl.unselectAll();
		}
	}

	function loadRoads() {
		$
				.ajax({
					url : "../realtimetraffic/roads",
					success : function(str) {
						var json = eval("(" + str + ")");

						lineLayer.removeAllFeatures();

						selectCtrl = new OpenLayers.Control.SelectFeature(
								lineLayer, {
									clickout : false,
									toggle : false,
									multiple : false,
									hover : false,
									toggleKey : "ctrlKey", // ctrl key removes from selection
									multipleKey : "shiftKey", // shift key adds to selection
									box : true,
									autoActivate : true
								});

						map.addControl(selectCtrl);

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
					}
				});
	}
	initMap(loadRoads);

	function isBlank(str) {
		return (!str || /^\s*$/.test(str));
	}
	$(function() {
		$("#dateTime").datetimepicker();
	});
</script>


