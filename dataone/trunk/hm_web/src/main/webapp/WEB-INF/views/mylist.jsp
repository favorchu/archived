<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.3/dojox/grid/resources/Grid.css" />
<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/dojo/1.8.3/dojox/grid/resources/claroGrid.css" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
 
<script>
	
	var currentYear = '${currentYear}';
	var currentWeek = '${currentWeek}';
	var cwStart = new Date('${cwStart}');
	var cwEnd = new Date('${cwEnd}');
	var cwNumWorkDay = '${cwNumWorkDay}';
	var tsJsonStr = '${tsJson}';
	var pojJsonStr = '${pojJson}';
	var pojCatJsonStr = '${pojCatJson}';
	var pojCatJson = eval('('+pojCatJsonStr+')');
	var pojJson = eval('('+pojJsonStr+')');
	var weekDayList = new Array();
	var numOfDay = (cwEnd - cwStart)/86400000 + 1;
	var tempCwStart = new Date('${cwStart}');
	var grid;
	var summaryGrid;
	var gridLayout;
	var tsStore;
	var summaryTsJsonStr = '[{"id":"0","tsUserId":"","tsDateSat":"0.0","tsDateTue":"0.0","tsDateSun":"0.0","tsYear":"0","tsWeek":"0","tsDateFri":"0.0","tsDateMon":"0.0","tsPojCod":"","tsDateThu":"0.0","tsDateWed":"0.0", "tsPojCat":""}]';
	var summaryTsStore;
	var summaryGridLayout;
	
	for(var j=0;j<numOfDay;j++)
	{
		weekDayList.push(new Date(tempCwStart));
		tempCwStart.setDate(tempCwStart.getDate()+1);
	}
	
	//Create dojo grid
	require(
	[
		"dojox/grid/DataGrid",
		"dojox/grid/cells",
		"dojox/grid/cells/dijit",
		"dojo/store/Memory",
		"dojo/data/ObjectStore",
		"dojo/date/locale",
		"dojo/currency",
		"dijit/form/DateTextBox",
		"dijit/form/NumberTextBox",
		"dijit/form/FilteringSelect",
		"dijit/form/CurrencyTextBox",
		"dijit/form/HorizontalSlider",
		"dojo/domReady!",
		"dojo/data/ItemFileReadStore",
		"dojo/data/ItemFileWriteStore",
		"dojo/data/ObjectStore"
	], 
		function(DataGrid, cells, cellsDijit, Memory, ObjectStore, locale, currency, DateTextBox, CurrencyTextBox, HorizontalSlider)
		{			
			var catSore = new dojo.data.ItemFileReadStore({
				data:{
					"identifier": "name",
		            "label": "Category",
		            "items": pojCatJson
				}
			});
			
			var pojStore = new dojo.data.ItemFileWriteStore({
				data:{
					"identifier": "name",
		            "label": "Project",
		            "items": pojJson
				}
			});
			
			
			gridLayout = 
			[[
				//{ name: 'Project Id', styles: 'text-align: center;', width:20, field: 'id', editable: false, formatter:function(e){return e.tsProjectCod;}},	
				/*{name:'Category', styles: 'text-align: center;', width:5, field: 'tsPojCat',editable: true,
					type: cells.ComboBox,
	                options: pojCatJson
				},
				{ name: 'Project', styles: 'text-align: center;', width:9, field: 'tsPojCod',editable: true, type: dojox.grid.cells._Widget,
					type: cells.ComboBox,
	                options: ""
				},	
				*/
				{	field: 'tsPojCat',
			        name: 'Category',
			        styles: 'text-align: center;',
			        editable: true,
			        required: true,
			        width: 7,
			        type: dojox.grid.cells._Widget,
			        widgetClass: dijit.form.FilteringSelect,
			        widgetProps: {
			            store: catSore
		        	}
			    },
				{	field: 'tsPojCod',
			        name: 'Project',
			        styles: 'text-align: center;',
			        editable: true,
			        required: true,
			        width: 12,
			        type: dojox.grid.cells._Widget,
			        widgetClass: dijit.form.FilteringSelect,
			        widgetProps: {
			            store: pojStore
		        	}  
				},
				{ name: '/', field: 'tsDateSun', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateMon', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateTue', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateWed', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateThu', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateFri', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: '/', field: 'tsDateSat', width: 5, styles: 'text-align: center;', editable: true, datatype:"number", widgetClass: dijit.form.NumberTextBox},
				{ name: 'Remarks', field: 'tsRemarks', width: 12, styles: 'text-align: center;', editable: true},
				{ name: 'Action', width:5, styles: 'text-align: center;', formatter:function(e, index){return '<a href="javascript:delBtn_onClick('+index+')" value="Delete">Delete</a>';}}
				
			]];
			
			summaryGridLayout = 
				[[
					//{ name: 'Project Id', styles: 'text-align: center;', width:20, field: 'id', editable: false, formatter:function(e){return e.tsProjectCod;}},	
					{ name: 'Category', styles: 'text-align: center;', width:7, field: 'tsPojCat',editable: false},	
					{ name: 'Project', styles: 'text-align: center;', width:12, field: 'tsPojCod',editable: false},	
					{ name: '/', field: 'tsDateSun', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateMon', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateTue', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateWed', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateThu', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateFri', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: '/', field: 'tsDateSat', width: 5, styles: 'text-align: center;', editable: false, datatype:"number", widgetClass: dijit.form.NumberTextBox},
					{ name: 'Remarks', field: 'tsRemarks', width: 12, styles: 'text-align: center;', editable: false, },
					{ name: 'Action', width:5, styles: 'text-align: center;'}
					
				]];
		
			for(var i=0;i<weekDayList.length;i++)
			{
				var day = weekDayList[i].getDay();
				
				switch(day)
				{
					case 0:
						gridLayout[0][2].name = "Sun "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 1:
						gridLayout[0][3].name = "Mon "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 2:
						gridLayout[0][4].name = "Tue "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 3:
						gridLayout[0][5].name = "Wed "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 4:
						gridLayout[0][6].name = "Thu "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 5:
						gridLayout[0][7].name = "Fri "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					case 6:
						gridLayout[0][8].name = "Sat "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
						break;
						
					default:
						break;
			
				}
			}
			
			var data =  eval('('+tsJsonStr+')');

			var objectStore = new Memory({data:data});

			// global var "test_store"
			tsStore = new ObjectStore({objectStore: objectStore});

			//	create the grid.
			grid = new DataGrid({
				store: tsStore,
				structure: gridLayout,
				escapeHTMLInData: false,
				"class": "grid",
				rowSelector: '10px',
				style:'width:100%;height:400px',
				onApplyEdit: calSummary,
				//onApplyCellEdit : onCatChanged
				canSort:false,
				onStartEdit: onPojSelected,
				onApplyCellEdit: onCatChanged
			}, "grid");
			grid.startup();	  
			
			//create summary grid
			var summaryData = eval('('+summaryTsJsonStr+')');
			var summaryObjectStore = new Memory({data:summaryData});
			summaryTsStore = new ObjectStore({objectStore:summaryObjectStore});
			summaryGrid = new DataGrid({
				store: summaryTsStore,
				structure: summaryGridLayout,
				escapeHTMLInData: false,
				"class": "grid",
				rowSelector: '20px',
				style:'width:100%;height:80px'
			},'summaryGrid');
			summaryGrid.startup();
			summaryGrid.viewsHeaderNode.style.display = 'none';
			calSummary();
		}
	);
	
	//create dojo button(next week & previous week & submit & delete)
	require(["dijit/form/Button", "dojo/domReady!", "dijit/form/TextBox"], 
			function(Button, TextBox, ready)
			{
       			var preWkBtn = new Button(
       			{
          			label: "Previous Week",
           			onClick: preWkBtn_onClick,
           			style:'float:right'
       			}, "preWkBtn");
       			preWkBtn.startup();
       			
       			var nextWkBtn = new Button(
        			{
        				label: "Next Week",
            			onClick: nextWkBtn_onClick,
            			style:'',
            			style:'float:right'
        			}, "nextWkBtn");
       			nextWkBtn.startup();
       			
       			var subBtn = new Button(
        			{
           			label: "Submit",
            			onClick: subBtn_onClick,
            			style:'float:right'
        			}, "subBtn");
       			subBtn.startup();
       			
       			var newBtn = new Button(
        			{
           				label: "New",
            			onClick: newBtn_onClick
        			}, "newBtn");
       			newBtn.startup();
       			
       			var weekTextBox = new dijit.form.TextBox(
       					{
       						name: "firstname",
       			            value: "" /* no or empty value! */,
       			        	disabled:'disabled',
       			            id:'weekText',
       			            width:10,
       			         	style:'float:right'
       			      
       					}, "weekTextBox");
       			weekTextBox.startup();
       			document.getElementById('weekText').value = cwStart.getDate()+'/'+(cwStart.getMonth()+1)+'/'+cwStart.getFullYear()+' - '+cwEnd.getDate()+'/'+(cwEnd.getMonth()+1)+'/'+cwEnd.getFullYear();	
       		
      		}
	);
	

	
	function nextWkBtn_onClick()
	{
		restoreGridLayout();
		require(["dojo/_base/xhr"], 
		function(xhr)
		{
		    xhr.get({
		        url:"getNextWkSchedule.do",
		        content: {year:currentYear, week:currentWeek},
		        load: function(returnStr)
	        	{
		        	var returnJson = eval('('+returnStr+')');
	            	currentYear = returnJson.id.cwYear;
	            	currentWeek = returnJson.id.cwWeek;
	            	cwStart = new Date(returnJson.cwStart);
	            	cwEnd = new Date(returnJson.cwEnd);
	            	cwNumWorkDay = returnJson.cwNumWorkDay;
	            	modifyColum(cwStart, cwEnd);
	            	getMyTsList();
	        	}
		    });
		});
		
	}
	
	function preWkBtn_onClick()
	{
		restoreGridLayout();
		require(["dojo/_base/xhr"], 
		function(xhr)
		{
		    // get some data, convert to JSON
		    xhr.get({
		        url:"getPreWkSchedule.do",
		        content: {year:currentYear, week:currentWeek},
		        load: function(returnStr)
	        	{
		        	var returnJson = eval('('+returnStr+')');
	            	currentYear = returnJson.id.cwYear;
	            	currentWeek = returnJson.id.cwWeek;
	            	cwStart = new Date(returnJson.cwStart);
	            	cwEnd = new Date(returnJson.cwEnd);
	            	cwNumWorkDay = returnJson.cwNumWorkDay;
	            	modifyColum(cwStart, cwEnd);
	            	getMyTsList();
	        	}
		    });
		});
	}
	
	function delBtn_onClick(index)
	{
		grid.store.deleteItem(grid.getItem(index));
		grid.store.save();
		calSummary();
	}
	
	function subBtn_onClick()
	{
		var tsArray = grid.store.objectStore.data;
		var valid;
		if(cwNumWorkDay != 5)
			valid = specialValidate(tsArray);
		else
			valid = validate(tsArray);
		
		if(valid)
		{
			require(["dojo/_base/xhr"], 
					function(xhr)
					{
						var tsArrayStr = ''+JSON.stringify(tsArray)+'';
						
					    xhr.get({
					        url:"SetTsList.do",
					        content: {tsStr:tsArrayStr},
					        load: function(returnStr)
				        	{
					        	if(returnStr == 'success')
					        	{
					        		alert('Saved successfully');
					        		location.reload(true);
					        	}
					        	else
					        		alert('Error');
				        	}
					    });
					});
		}
	}
	
	function newBtn_onClick()
	{
		var newId;
		if(grid.store.objectStore.data.length == 0)
			newId = '0';
		else
			newId = (parseFloat(grid.getItem(grid.store.objectStore.data.length-1).id)+1)+'';
		
		var myNewItem = {
				id:newId,
				tsPojCat:'',
				tsRemarks:'',
				tsPojCod: '',
	            tsDateSun: '0',
	            tsDateMon: '0',
	            tsDateTue: '0',
	            tsDateWed: '0',
	            tsDateThu: '0',
	            tsDateFri: '0',
	            tsDateSat: '0',
	            tsWeek: currentWeek,
	            tsYear: currentYear
	        };
		grid.store.newItem(myNewItem);
		grid.store.save();
	}
	
	function restoreGridLayout()
	{
		for(var i=2;i<9;i++)
		{
			grid.layout.cells[i].name = '/';	
		}
		grid.update();
	}
	
	function modifyColum(start,end)
	{
		numOfDay = (end - start)/86400000 + 1;
		tempCwStart = new Date(start);
		weekDayList = new Array();

		for(var j=0;j<numOfDay;j++)
		{
			weekDayList.push(new Date(tempCwStart));
			tempCwStart.setDate(tempCwStart.getDate()+1);
		}
		
		for(var i=0;i<weekDayList.length;i++)
		{
			var day = weekDayList[i].getDay();
			switch(day)
			{
				case 0:
					grid.layout.cells[2].name = "Sun "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 1:
					grid.layout.cells[3].name = "Mon "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 2:
					grid.layout.cells[4].name = "Tue "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 3:
					grid.layout.cells[5].name = "Wed "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 4:
					grid.layout.cells[6].name = "Thu "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 5:
					grid.layout.cells[7].name = "Fri "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				case 6:
					grid.layout.cells[8].name = "Sat "+weekDayList[i].getDate()+'/'+(weekDayList[i].getMonth()+1)+'/'+weekDayList[i].getFullYear();
					break;
					
				default:
					break;
		
			}
		}
		grid.update();
		document.getElementById('weekText').value = cwStart.getDate()+'/'+(cwStart.getMonth()+1)+'/'+cwStart.getFullYear()+' - '+cwEnd.getDate()+'/'+(cwEnd.getMonth()+1)+'/'+cwEnd.getFullYear();	

	}	
	
	function getMyTsList()
	{
		require(["dojo/_base/xhr",
		         "dojo/store/Memory",
		         "dojo/data/ObjectStore",
		         "dojo/domReady!"
		         ], 
		function(xhr, Memory, ObjectStore)
		{
		    xhr.get({
		        url:"getMyTsList.do",
		        content: {year:currentYear, week:currentWeek},
		        load: function(returnStr)
	        	{
		        	var data = eval('('+returnStr+')');
					var objectStore = new Memory({data:data});
					var tsStore = new ObjectStore({objectStore: objectStore});
		        	grid.setStore(tsStore);
		        	grid.update();
	            	calSummary();
	        	}
		    });
		});
	}
	
	function specialValidate(tsArray){
		var valid = true;
		var tsSun = 0;
		var tsMon = 0;
		var tsTue = 0;
		var tsWed = 0;
		var tsThu = 0;
		var tsFri = 0;
		var tsSat = 0;
		var errMsg = 'Please Validate Input Data!\n';
		var duplicatedPoj = false;
		
		if(tsArray.length == 0)
		{
			errMsg = errMsg + 'Please input at least 1 project!\n';
			valid = false;
		}
		
		for(var i=0; i<tsArray.length;i++)
		{
			tsSun = tsSun + parseFloat(tsArray[i].tsDateSun);
			tsMon = tsMon + parseFloat(tsArray[i].tsDateMon);
			tsTue = tsTue + parseFloat(tsArray[i].tsDateTue);
			tsWed = tsWed + parseFloat(tsArray[i].tsDateWed);
			tsThu = tsThu + parseFloat(tsArray[i].tsDateThu);
			tsFri = tsFri + parseFloat(tsArray[i].tsDateFri);
			tsSat = tsSat + parseFloat(tsArray[i].tsDateSat);
			
			if(tsArray[i].tsPojCod=='')
			{
				errMsg = errMsg + 'Please select a project!\n';
				valid = false;
			}
			
			for(var j=0;j<tsArray.length;j++)
			{
				if(tsArray[i].tsPojCod == tsArray[j].tsPojCod && i!=j)
					duplicatedPoj = true;
			}
			
			var totalHrForPoj = parseFloat(tsArray[i].tsDateSun) + parseFloat(tsArray[i].tsDateMon) + parseFloat(tsArray[i].tsDateTue) + parseFloat(tsArray[i].tsDateWed) + parseFloat(tsArray[i].tsDateThu) + parseFloat(tsArray[i].tsDateFri) + parseFloat(tsArray[i].tsDateSat);
			if(totalHrForPoj<0.5)
			{
				errMsg = errMsg + 'Please input at least 0.5 hr for 1 project!\n';
				valid = false;
			}
			if(duplicatedPoj)
			{
				errMsg = errMsg + "Duplicated porject!\n";
				valid = false;	
			}
		}
		
		var numOfDay = (cwEnd - cwStart)/86400000 + 1;
		var tempCwStart = new Date(cwStart);
		var weekDayList = new Array();

		for(var j=0;j<numOfDay;j++)
		{
			weekDayList.push(new Date(tempCwStart));
			tempCwStart.setDate(tempCwStart.getDate()+1);
		}
		
		for(var i=0;i<weekDayList.length;i++)
		{
			var day = weekDayList[i].getDay();
			switch(day)
			{
				case 0:
					if(tsSun!=0)
					{
						errMsg = errMsg + "Work hour for Sunday is 0\n";
						valid = false;
					}
					break;
					
				case 1:
					if(tsMon!=7.5)
					{
						errMsg = errMsg + "Work hour for Monday is 7.5\n";
						valid = false;
					}
					break;
					
				case 2:
					if(tsTue!=7.5)
					{
						errMsg = errMsg + "Work hour for Tuesday is 7.5\n";
						valid = false;
					}
					break;
					
				case 3:
					if(tsWed != 7.5)
					{
						errMsg = errMsg + "Work hour for Wednesday is 7.5\n";
						valid = false;
					}
					break;
					
				case 4:
					if(tsThu!=7.5)
					{
						errMsg = errMsg + "Work hour for Thusday is 7.5\n";
						valid = false;
					}
					break;
					
				case 5:
					if(tsFri!=7.5)
					{
						errMsg = errMsg + "Work hour for Friday is 7.5\n";
						valid = false;
					}
					break;
					
				case 6:
					if(tsSat!=0)
					{
						errMsg = errMsg + "Work hour for Saturday is 0\n";
						valid = false;
					}
					break;
					
				default:
					break;
		
			}
		}
		
		var totalHr = cwNumWorkDay*7.5;
		var inputTotalHr = tsSun+tsMon+tsTue+tsWed+tsThu+tsFri+tsSat;
		if(inputTotalHr!=totalHr){
			valid = false;
			errMsg = errMsg + "Total Work hour should not exceed "+totalHr+"\n";
		}
		
		if(valid){
			if(confirm("Confirm to submit?"))
				return true;
			else
				return false;
		} else {
			alert(errMsg);
			return false;
		}
	}
	
	function validate(tsArray)
	{
		//check duplicated project
		var valid = true;
		var errMsg = 'Please Validate Input Data!\n';
		var tsSun = 0;
		var tsMon = 0;
		var tsTue = 0;
		var tsWed = 0;
		var tsThu = 0;
		var tsFri = 0;
		var tsSat = 0;
		var duplicatedPoj = false;
		
		if(tsArray.length == 0)
		{
			errMsg = errMsg + 'Please input at least 1 project!\n';
			valid = false;
		}
		
		for(var i=0; i<tsArray.length;i++)
		{
			tsSun = tsSun + parseFloat(tsArray[i].tsDateSun);
			tsMon = tsMon + parseFloat(tsArray[i].tsDateMon);
			tsTue = tsTue + parseFloat(tsArray[i].tsDateTue);
			tsWed = tsWed + parseFloat(tsArray[i].tsDateWed);
			tsThu = tsThu + parseFloat(tsArray[i].tsDateThu);
			tsFri = tsFri + parseFloat(tsArray[i].tsDateFri);
			tsSat = tsSat + parseFloat(tsArray[i].tsDateSat);
			
			if(tsArray[i].tsPojCod=='')
			{
				errMsg = errMsg + 'Please select a project!\n';
				valid = false;
			}
			
			for(var j=0;j<tsArray.length;j++)
			{
				if(tsArray[i].tsPojCod == tsArray[j].tsPojCod && i!=j)
					duplicatedPoj = true;
			}
			
			var totalHrForPoj = parseFloat(tsArray[i].tsDateSun) + parseFloat(tsArray[i].tsDateMon) + parseFloat(tsArray[i].tsDateTue) + parseFloat(tsArray[i].tsDateWed) + parseFloat(tsArray[i].tsDateThu) + parseFloat(tsArray[i].tsDateFri) + parseFloat(tsArray[i].tsDateSat);
			if(totalHrForPoj<0.5)
			{
				errMsg = errMsg + 'Please input at least 0.5 hr for 1 project!\n';
				valid = false;
			}
		}
		if(duplicatedPoj)
		{
			errMsg = errMsg + "Duplicated porject!\n";
			valid = false;	
		}
		
		
		if(tsSun!=0)
		{
			errMsg = errMsg + "Work hour for Sunday is 0\n";
			valid = false;
		}
		if(tsSat!=0)
		{
			errMsg = errMsg + "Work hour for Saturday is 0\n";
			valid = false;
		}
		if(tsMon!=7.5)
		{
			errMsg = errMsg + "Work hour for Monday is 7.5\n";
			valid = false;
		}
		if(tsTue!=7.5)
		{
			errMsg = errMsg + "Work hour for Tuesday is 7.5\n";
			valid = false;
		}
		if(tsWed != 7.5)
		{
			errMsg = errMsg + "Work hour for Wednesday is 7.5\n";
			valid = false;
		}
		if(tsThu!=7.5)
		{
			errMsg = errMsg + "Work hour for Thusday is 7.5\n";
			valid = false;
		}
		if(tsFri!=7.5)
		{
			errMsg = errMsg + "Work hour for Friday is 7.5\n";
			valid = false;
		}
		
		if(tsMon==7.5 && tsTue==0)
		{
			errMsg = errMsg + "Work hour for Tuesday is 7.5\n";
			valid = false;
		}
		if(tsTue==7.5 && tsWed==0)
		{
			errMsg = errMsg + "Work hour for Wednesday is 7.5\n";
			valid = false;
		}
		if(tsWed==7.5 && tsThu==0)
		{
			errMsg = errMsg + "Work hour for Thursday is 7.5\n";
			valid = false;
		}
		if(tsThu==7.5 && tsFri==0)
		{
			errMsg = errMsg + "Work hour for Friday is 7.5\n";
			valid = false;
		}
		
		
		
		if(!valid)
		{
			alert(errMsg);
			return false;
		}
		else
		{
			var warning = false;
			var warningMsg = "";
			
			if(tsMon==0)
			{
				warningMsg+="Work hour for Monday is 0\n";
				warning = true;
			}
			if(tsTue==0)
			{
				warningMsg+="Work hour for Tue is 0\n";
				warning = true;
			}
			if(tsWed==0)
			{
				warningMsg+="Work hour for Wednesday is 0\n";
				warning = true;
			}
			if(tsThu==0)
			{
				warningMsg+="Work hour for Thusday is 0\n";
				warning = true;
			}
			if(tsFri==0)
			{
				warningMsg+="Work hour for Friday is 0\n";
				warning = true;
			}
			
			if(warning)
			{
				warningMsg+="\nConfirm to submit?\n";
				if(confirm(warningMsg))
					return true;
				else
					return false;
			}
			else
			{
				if(confirm("Confirm to submit?"))
					return true;
				else
					return false;
			}	
		}
	}
	
	function calSummary()
	{
		var tsArray = grid.store.objectStore.data;
		
		var tsSun = 0;
		var tsMon = 0;
		var tsTue = 0;
		var tsWed = 0;
		var tsThu = 0;
		var tsFri = 0;
		var tsSat = 0;
		for(var i=0; i<tsArray.length;i++)
		{
			tsSun = tsSun + parseFloat(tsArray[i].tsDateSun);
			tsMon = tsMon + parseFloat(tsArray[i].tsDateMon);
			tsTue = tsTue + parseFloat(tsArray[i].tsDateTue);
			tsWed = tsWed + parseFloat(tsArray[i].tsDateWed);
			tsThu = tsThu + parseFloat(tsArray[i].tsDateThu);
			tsFri = tsFri + parseFloat(tsArray[i].tsDateFri);
			tsSat = tsSat + parseFloat(tsArray[i].tsDateSat);
		}
		
		
		var summaryItem = summaryGrid.getItem(0);
		summaryGrid.store.setValue(summaryItem, 'tsDateMon', tsMon+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateTue', tsTue+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateWed', tsWed+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateThu', tsThu+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateFri', tsFri+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateSat', tsSat+'');		
		summaryGrid.store.setValue(summaryItem, 'tsDateSun', tsSun+'');		

		summaryGrid.store.save();
	}
	
	
	
	function onPojSelected(inCell, inRowIndex)
	{	
		if(inCell.name == 'Project')
		{
			
			//console.log(grid.getCell(0).markup[5]);
			if(grid.store.objectStore.data[inRowIndex].tsPojCat)
				if(grid.store.objectStore.data[inRowIndex].tsPojCat!='')
				{
					var tempPojOpt = new Array();
					var tempPojJson = eval('('+pojJsonStr+')');
					
					
					for(var i=0;i<tempPojJson.length;i++)
					{
						if(tempPojJson[i].cpPojCat==grid.store.objectStore.data[inRowIndex].tsPojCat)	
							tempPojOpt.push(tempPojJson[i]);
					}
					

					
					
					var onSaveComplete = function ()
					{
						 //console.log( grid.structure[0][1].widgetProps.store);
						 for(var i=0;i<tempPojOpt.length;i++)
						{
							grid.structure[0][1].widgetProps.store.newItem(tempPojOpt[i]);
						}
					};
					
					var delAllItems = function (items, request) {
					      for (var i = 0; i < items.length; i++) {
					    	  grid.structure[0][1].widgetProps.store.deleteItem(items[i]);
					    	 
					      }
					      
					      grid.structure[0][1].widgetProps.store.save({onComplete:onSaveComplete});
					     
					     
					};
					
					grid.structure[0][1].widgetProps.store.fetch({onComplete:delAllItems});
					
					
				}
				else
				{
					alert('Please select a project category!');
				}
			else
			{
				alert('Please select a project category!');
			}
		}
	}
	
	function onCatChanged(inValue, inRowIndex, inFieldIndex)
	{
		if(inFieldIndex == 'tsPojCat')
		{
			grid.store.objectStore.data[inRowIndex].tsPojCod = '';
			grid.store.save();
		}
	}
	
	
	 
	
</script>


<div id='newBtn'></div>
<div id='nextWkBtn'></div>
<div id='weekTextBox'></div>
<div id='preWkBtn'></div>
<div id='grid'></div>
<div id='summaryGrid'></div>
<div id='subBtn'></div>




