<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Simple upload</title>
<link href="../../resources/css/upload.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../../resources/js/swfupload.js"></script>
<script type="text/javascript" src="../../resources/js/swfupload.queue.js"></script>
<script type="text/javascript" src="../../resources/js/fileprogress.js"></script>
<script type="text/javascript" src="../../resources/js/handlers.js"></script>
<script type="text/javascript">
	var swfu;

	window.onload = function() {
		var settings = {
			flash_url : "../../resources/swfupload.swf",
			upload_url : "",
			post_params : {/*"PHPSESSID" : "<?php echo session_id(); ?>"*/},
			file_size_limit : "20 MB",
			file_types : "*.jpg",
			file_types_description : "All Files",
			file_upload_limit : 10000,
			file_queue_limit : 0,
			custom_settings : {
				progressTarget : "fsUploadProgress",
				cancelButtonId : "btnCancel"
			},
			debug : false,

			// Button settings
			button_image_url : "../../resources/img/XPButtonUploadText_61x22.png",
			button_width : "61",
			button_height : "22",
			button_placeholder_id : "spanButtonPlaceHolder",
			button_text : '',
			button_text_style : ".theFont { font-size: 16; }",
			button_text_left_padding : 12,
			button_text_top_padding : 3,

			// The event handler functions are defined in handlers.js
			file_queued_handler : fileQueued,
			file_queue_error_handler : fileQueueError,
			file_dialog_complete_handler : fileDialogComplete,
			upload_start_handler : uploadStart,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,
			queue_complete_handler : queueComplete
		// Queue plugin event
		};

		swfu = new SWFUpload(settings);
	};
</script>
</head>
<body>
	<div id="header">
		<h1 id="logo">
			<a href="../">SWFUpload</a>
		</h1>
		<div id="version">v2.2.0</div>
	</div>

	<div id="content">
		<h2>Simple Demo</h2>
		<form id="form1" action="index.php" method="post" enctype="multipart/form-data">
			<p>This page demonstrates a simple usage of SWFUpload. It uses the Queue Plugin to simplify uploading or
				cancelling all queued files.</p>

			<div class="fieldset flash" id="fsUploadProgress">
				<span class="legend">Upload Queue</span>
			</div>
			<div id="divStatus">0 Files Uploaded</div>
			<div>
				<span id="spanButtonPlaceHolder"></span> <input id="btnCancel" type="button" value="Cancel All Uploads"
					onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;" />
			</div>
			<div>
				<a href="../album/thai2013s/">Back to album</a>
			</div>

		</form>
	</div>
</body>
</html>