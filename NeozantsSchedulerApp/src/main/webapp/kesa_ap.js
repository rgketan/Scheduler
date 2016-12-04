/**
 * 
 */

 	var SIMULATE_BACKEND =false;
	var NEW_PROCESS_STATE = {
			
		//test connection not intiated
		"1":{
			"NXT":"HIDE",
			"PREV":"HIDE",
			"DONE":"HIDE"
		},
		// test connection successful
		"2":{
			"NXT":"SHOW",
			"PREV":"HIDE",
			"DONE":"HIDE"
		},
		//configuration filled 
		"3":{
			"NXT":"SHOW",
			"PREV":"SHOW",
			"DONE":"HIDE"
		},
		//confirmation screen
		"4":{
			"NXT":"HIDE",
			"PREV":"SHOW",
			"DONE":"SHOW"
		}
	};
	

var kesa = kesa || {};

$(function(){

	KESA = new kesa.AdminPanelController();
});
	


kesa.AdminPanelView = function(){
	this.allUIPagesClassIdentifier = "kesa-page";
	this.hideMeClassIdentifier="kesa-hide-me";
	this.pageVisibilityStateMapper =  {
		"1":"loginPage",
		"2":"dashBoardPage",
		"3":"newProcessPage",
		"4":"viewExistingProcessPage"
	};
	this.pageVisibilityState= 1;
	this.processStep = 1;
	
	this.serviceMgr = new kesa.AdminPanelServiceLayer();
	
	this.configurationObject={
			"FILE_NAME":"",
			"SERVER_FILE_PATH":"",
			"TIME":"",
			"DATE":"",
			"EXTENSION":"",
			"TYPE_OF_EVENT":"",
			"EMAIL":"",
			"RECIPIENT_ADDRESS":""
	};
	
	this.ftpObject={
		     "ftpHost":"",
			 "ftpUsername":"",
			 "ftpPassword":"",
			 "ftpFilePath":""		
	};
	
	///testFtpConnection
	this.recordList =[];
	
};	
kesa.AdminPanelView.prototype={
		
		onAppLoaded :function(){
			$("#kesa-loader").hide();
			this.pageVisibilityState =1 ;
			this.showPageAsPerState();
			this.addLoginListeners();
			
		},
		hideAll:function(){
			$("."+this.allUIPagesClassIdentifier).addClass(this.hideMeClassIdentifier);
			
		},
		showPageAsPerState :function(){
			this.hideAll();
			$("#"+this.pageVisibilityStateMapper[this.pageVisibilityState]).removeClass(this.hideMeClassIdentifier);
			
			if(this.pageVisibilityState === 3){
				this.processStep = 1;
				this.renderProcessSteps();
			}
			
		},
		renderProcessSteps :function(){
			this.renderUIBtns(NEW_PROCESS_STATE[this.processStep]);
			
			if(this.processStep === 3 &&  $('#kesa-ftp-radio').is(':checked')) { 
				$('#kesaNextBtn').prop('disabled', true);
			}else{
				$('#kesaNextBtn').prop('disabled', false);
			}
			
			switch(this.processStep){
			case 1:
			case 2:
				$("#process2").removeClass("btn-info");
				$("#process3").removeClass("btn-info");
				$("#process1").addClass("btn-info");
				$("#menu2").removeClass("active");
				$("#menu3").removeClass("active");
				$("#menu1").addClass("active");
				break;
			case 3:
				$("#process1").removeClass("btn-info");
				$("#process3").removeClass("btn-info");
				$("#process2").addClass("btn-info");
				$("#menu2").addClass("active");
				
				$("#menu1").removeClass("active");
				$("#menu3").removeClass("active");
				
				break;
			case 4:	
				$("#process1").removeClass("btn-info");
				$("#process2").removeClass("btn-info");
				$("#process3").addClass("btn-info");
				$("#menu3").addClass("active");
				
				$("#menu2").removeClass("active");
				$("#menu1").removeClass("active");
				
				break;
			}
		},
		renderUIBtns:function(obj){		
			//nxt
			if(obj["NXT"] === "SHOW" ){
				$("#kesaNextBtn").removeClass(this.hideMeClassIdentifier);
			}else {
				//console.log(this.hideMeClassIdentifier);
				$("#kesaNextBtn").addClass(this.hideMeClassIdentifier);
			}
			
			
			//nxt
			if(obj["PREV"] === "SHOW" ){
				$("#kesaBackBtn").removeClass(this.hideMeClassIdentifier);
				
			}else {
				$("#kesaBackBtn").addClass(this.hideMeClassIdentifier);
			}
			
			
			//nxt
			if(obj["DONE"] === "SHOW" ){
				$("#kesaDoneBtn").removeClass(this.hideMeClassIdentifier);
				
			}else {
				$("#kesaDoneBtn").addClass(this.hideMeClassIdentifier);
			}
			
			
		},
		addLoginListeners:function(){
			
			$("#kesaLoginBtn").on("click",this.loginClicked.bind(this));
			
			$("#kesaDashboardNewProcess").on("click",this.dashboardOptionClicked.bind(this));
			$("#kesaDashboardEalierProcess").on("click",this.dashboardOptionClicked.bind(this));
			$("#kesaDashboardEditUserDetails").on("click",this.dashboardOptionClicked.bind(this));
			$("#kesaDashboardCreateMoreUsers").on("click",this.dashboardOptionClicked.bind(this));
			
			$(".kesaNavigateToHome").on("click",this.navigateToHome.bind(this));
			
			$("#kesaTestConnectionBtn").on("click",this.testConnectivity.bind(this));
			
			$("#kesaNextBtn").on("click",this.processStepNextBtnClicked.bind(this));
			
			$("#kesaBackBtn").on("click",this.processStepPrevBtnClicked.bind(this));
			$("#kesaDoneBtn").on("click",this.processStepDoneBtnClicked.bind(this));

			//kesa-earlier-process-tbody

			$("#fetch-all-records").on("click",this.fetchAllRecordsClicked.bind(this));
			
			
			//$("#kesaFetchFileList").on("click",this.fetchFileListClicked.bind(this));//KV
			
			$('input:radio[name=kesa-repeat-type]').change(function() {
		        if (this.value == 'email') {
		            $("#ftpWrapper").hide();
		            $("#emailWrapper").show();
		            
		            //console.log("SHOW NEXT");
		            //$("#kesaNextBtn").removeClass(this.hideMeClassIdentifier);
		            $('#kesaNextBtn').prop('disabled', false);
					
		            /*$("#kesaNextBtn").show();
		            $("#kesaBackBtn").show();*/
		        }
		        else if (this.value == 'ftp') {
		            $("#emailWrapper").hide();
		            $("#ftpWrapper").show();
		            
		            //console.log("HIDE NEXT");
		            $('#kesaNextBtn').prop('disabled', true);
		            
		           // $("#kesaNextBtn").addClass(this.hideMeClassIdentifier);
		            
		            /*$("#kesaNextBtn").hide();
		            $("#kesaBackBtn").hide();*/
		            
		        }
		    });
			
			$("#kesaTestFTPConnectionBtn").on("click",this.testFTPConnectivity.bind(this));
			
		},
		processStepDoneBtnClicked :function(){
			
			var dataArr = this.configurationObject["DATE"].split("-");
			var timeArr = this.configurationObject["TIME"].split(":");
			var timerData ={};
			if(dataArr.length === 3){
				timerData["date"] =dataArr[2];
				timerData["month"] =dataArr[1];
				timerData["year"] =dataArr[0];
				timerData["repeatOn"] =this.configurationObject["REPEAT_ON_CODE"];
				if(timeArr.length === 2){
				
					timerData["minutes"] =timeArr[1];
					timerData["hour"] =timeArr[0];
					timerData["amPmMarker"] =this.configurationObject["AMPM"];//KV
				}
			}
			
			
			
			
			var reqObject ={
					"timerData":timerData,
					"sqlFilePath":this.configurationObject["SERVER_FILE_PATH"],
					"fileFormat":this.configurationObject["EXTENSION"] ,
					"fromEmailId":"",
					"toEmailId":this.configurationObject["EMAIL"],
					"recipientAddress":this.configurationObject["RECIPIENT_ADDRESS"],
					"alreadyCreated":false,
					"typeOfEvent":this.configurationObject["TYPE_OF_EVENT"],
					"outputFileName":this.configurationObject["FILE_NAME"],//$("#kesaUserName").val()+"_"+this.configurationObject["DATE"]+"_"+this.configurationObject["TIME"].replace(":","-")
			        "ftpRequest" :this.ftpObject
			};//CHANGE
			
			//console.log("REQUEST OBJECT WE GET IS::",reqObject);
			this.serviceMgr.triggerConfigSubmission(reqObject, this.onConfigSubmissionResponse.bind(this),this.onConfigSubmissionFailed.bind(this));
			//this.onConfigSubmissionResponse();
			
		},
		onConfigSubmissionFailed :function(){
			$("#kesa-loader").hide();
			$("#postConfigSubmissionMsg").html("<span style='color:red'>Failure in submission of configuration!</span>");
		},
		onConfigSubmissionResponse :function(response){
			$("#kesa-loader").hide();
			////console.log("EVENT BEEN TRIGGERED ::");
			////console.log(response);
			if(response.responseStatus === "success"){
			$("#postConfigSubmissionMsg").html("Sucessfully Submitted the configuration");
			}else{
				this.onConfigSubmissionFailedFromServer(response.detailMessageOnFailure);
			}
		},
		onConfigSubmissionFailedFromServer :function(message){
			$("#postConfigSubmissionMsg").html("<span style='color:red'>"+message+"!</span>");
		},
		
		fetchFileListClicked :function(){
			////console.log("FETCH FILE RESPONSE::")
			////console.log(response);
			//this.serviceMgr.triggerConnectivity({}, this.onfileNamesFetchedResponse.bind(this));
			//this.onfileNamesFetchedResponse();
			
		},
		onfileNamesFetchedResponse :function(resp){
			$("#kesaFileRadioList").empty();//KV
			
			////console.log("FETCH FILE RESPONSE::");
			////console.log(resp);
			var strHTML ="";
			for(var i =0;i<resp.length;i++){
				strHTML += '<div><input type="radio" name="optFilePathRadio" value="'+resp[i]+'" checked="checked">'+resp[i]+'</div>';
			}
			$("#kesaFileRadioList").append(strHTML);
			this.configurationObject["SERVER_FILE_PATH"] = ""; 
			
			//this.configurationObject
			//this.validateConfigurationSetting()
		},
		
		getSelectedRadioValue:function(str,codeReq){
			var bVal = "";
			var rates = document.getElementsByName(str);
			for(var i = 0; i < rates.length; i++){
			    if(rates[i].checked){
			    	if(codeReq == undefined){
			    		bVal = $(rates[i]).attr('kesa-data');
			    	}
			    	else{
			    		bVal = $(rates[i]).val();
			    	}
			    }
			}
			
			return bVal;
		},
		validateConfigurationSetting :function(){
			
			
			var rates = document.getElementsByName('optFilePathRadio');
			this.configurationObject["SERVER_FILE_PATH"] = "";
			this.configurationObject["TIME"] = "";
			this.configurationObject["DATE"] = "";
			this.configurationObject["EXTENSION"] = "" ;
			this.configurationObject["EMAIL"] = "" ;
			this.configurationObject["REPEAT_ON"] ="";
			this.configurationObject["REPEAT_ON_CODE"] ="";
			this.configurationObject["FILE_NAME"] ="";
			this.configurationObject["TYPE_OF_EVENT"] ="";
				
				
				
			this.configurationObject["REPEAT_ON"] = this.getSelectedRadioValue('kesa-repeat',true);
			this.configurationObject["REPEAT_ON_CODE"] =this.getSelectedRadioValue('kesa-repeat');
			//populate file path
			for(var i = 0; i < rates.length; i++){
			    if(rates[i].checked){
			    	////console.log("PATH WE::"+rates[i].value);
			    	this.configurationObject["SERVER_FILE_PATH"] = rates[i].value;
			    }
			}
			
			
			//KV 
			//populate date
			this.configurationObject["DATE"] = $("#kesaConfigDate").val();
			//this.configurationObject["TIME"] = $("#kesaConfigTime").val();
			
			var timeArray =$("#kesaConfigTime").val().split(':');
			var hours = timeArray[0]; 
	        var min = timeArray[1];
	        var amPm;
	        
	        if (hours < 12) {
	        	amPm= 'AM';
	        	if(hours==0){
	        		hours=12;
	        	}
	        } else {
	        	if(hours!=12){
	        		hours=hours - 12;
	        	}
	            hours=(hours < 10) ? '0'+hours:hours;	            
	            amPm= 'PM';
	        }
	        
	        this.configurationObject["TIME"] = hours+":"+min;
	        this.configurationObject["AMPM"] = amPm;	        
			
			this.configurationObject["EXTENSION"] = $("#kesaExtensionRadioList").val();
			this.configurationObject["FILE_NAME"]= $("#kesaFileName").val();
			
			//TODO: HARDCODING FOR NOW
			//this.configurationObject["ENVIRONMENT_NAME"] ="production";     //$("#kesaEnvironmentRadioList").val();
			
			
			
			
			this.configurationObject["TYPE_OF_EVENT"] =this.getSelectedRadioValue('kesa-repeat-type');
			//$("#kesaTypeOfReportRadioList").val();
			
			
			this.configurationObject["EMAIL"] = $("#kesaEmailIds").val();
			
			
			$("#configurationNotFilledMsg").html("");
			
			
			//console.log("CONFIGURATION TESTINGG");
			if(this.configurationObject["SERVER_FILE_PATH"] != undefined &&
					this.configurationObject["TIME"] != undefined &&
					this.configurationObject["AMPM"] != undefined &&
					this.configurationObject["DATE"] != undefined &&
					this.configurationObject["EXTENSION"] != undefined &&
					this.configurationObject["FILE_NAME"] != undefined &&
					this.configurationObject["TYPE_OF_EVENT"]!= undefined &&
					this.configurationObject["SERVER_FILE_PATH"] != "" && 
					this.configurationObject["TIME"] != "" &&
					this.configurationObject["AMPM"] != "" &&
					this.configurationObject["DATE"] != "" &&
					this.configurationObject["EXTENSION"] != "" &&
					this.configurationObject["FILE_NAME"] != "" &&
					this.configurationObject["TYPE_OF_EVENT"] != "" 
				){
				
				
				
				var validFlag=false;
				
				var detailString="";
				
				//console.log("EVENT VALUE WE GET IS::"+this.configurationObject["TYPE_OF_EVENT"]);
				
				if(this.configurationObject["TYPE_OF_EVENT"] == "EMAIL"){
					
					
					//console.log("NEOZANT CONFIG EMAIL:::"+this.configurationObject["EMAIL"]);
					
					if(this.configurationObject["EMAIL"] != "" &&
					   this.configurationObject["EMAIL"] !=  undefined){
						
							if(this.validateEmail(this.configurationObject["EMAIL"])){
								
								detailString="Recipient Address: "+this.configurationObject["EMAIL"]+","+this.configurationObject["RECIPIENT_ADDRESS"];
								validFlag=true;
							}
					}
					
				}else{
					  
					
					var ftpHost = $("#kesaFtpHost").val();
					var ftpUserName = $("#kesaFtpUserName").val();
					var ftpPassword = $("#kesaFtpPassword").val();
					var ftpPath = $("#kesaFtpPath").val();
					
					//console.log("Ftp Details: Host"+ftpHost+"| UserName:"+ftpUserName+"| Password:"+ftpPassword+"| Path:"+ftpPath);
					
					
					if(ftpHost != undefined &&
					   ftpUserName != undefined &&
					   ftpPassword != undefined &&
					   ftpPath != undefined &&
					   ftpHost != "" && 
					   ftpUserName != "" &&
					   ftpPassword != "" &&
					   ftpPath != ""){
						
						this.configurationObject["EMAIL"]=null;
						this.configurationObject["RECIPIENT_ADDRESS"]=null;
						
						
						//SETTING FTP OBJECT
						this.ftpObject={
							     "ftpHost":ftpHost,
								 "ftpUsername":ftpUserName,
								 "ftpPassword":ftpPassword,
								 "ftpFilePath":ftpPath		
						};
						detailString="Ftp Details: Host:"+ftpHost+"| UserName:"+ftpUserName+"| Password:"+ftpPassword+"| Path:"+ftpPath;
						validFlag=true;
					}
					
				}
				
				
				
				
				
			//console.log("VALID FLAG WE GET IS::::"+validFlag);
			
				if(validFlag){
						this.processStep++;
						this.renderProcessSteps();
						////console.log("CONFIGURATION OBJECT::");
						////console.log(this.configurationObject);
						$("#filePathConfirmation").html(this.configurationObject["SERVER_FILE_PATH"]);
						$("#fileNameConfirmation").html(this.configurationObject["FILE_NAME"]);
						
						$("#dateConfirmation").html(this.configurationObject["DATE"]);
						$("#timeConfirmation").html(this.configurationObject["TIME"] + this.configurationObject["AMPM"]);
						$("#outputConfirmation").html(this.configurationObject["EXTENSION"]);
						
						$("#typeOfEvent").html(this.configurationObject["TYPE_OF_EVENT"]);
						
						$("#detailConfirmation").html(detailString);
						
						//TODO: REMOVING FOR NOW
						//$("#environmentConfirmation").html(this.configurationObject["ENVIRONMENT_NAME"]);
						//$("#typeOfReportConfirmation").html(this.configurationObject["TYPE_OF_REPORT"]);
						
						
						$('#repeatConfirmation').hide();
							$('#repeatConfirmation').show();
							$('#repeatConfirmation').html(this.configurationObject["REPEAT_ON"]);
						
				}else{
					
					$("#configurationNotFilledMsg").html("ENTER VALID EMAIL ID");
				}
				
			}
			else{
				$("#configurationNotFilledMsg").html("Please fill in all the configuration");
			}
			
		},
		processStepPrevBtnClicked :function(){
			////console.log(this.processStep)
			this.processStep--;
			this.renderProcessSteps();
			this.clearSettingsData();	
		},
		clearSettingsData : function(){
			$('#configurationNotFilledMsg').val('');
		},
		processStepNextBtnClicked :function(){
			
			//console.log("NEXT BUTTON CLICKED::"+this.processStep);
			
			if(this.processStep === 3)
				{
					this.validateConfigurationSetting();
				}
			else{
				
				////console.log("PROCESS NEXT BUTTON");
				this.getConfigData();//KV
				
					//this.getConfigData();
					//this.processStep++;
					//this.renderProcessSteps();
				}
			this.clearSettingsData();
		},
		fetchDetailedView :function(evt){
			////console.log("ID : "+$(evt.currentTarget).attr("m-data")	);
			var recordId = $(evt.currentTarget).attr("m-data");
			var eventName = $(evt.currentTarget).attr("m-event-id");
			
			if (recordId) {
				this.serviceMgr.fetchedDetailedViewForId({uniqueId:recordId,eventName:eventName}, this.fetchedDetailsSucess.bind(this),this.fetchDetailsFailure.bind(this));	
			}
		},
		deleteScheduleEvent :function(evt){
			
			////console.log("DELTED EVENT ID : "+$(evt.currentTarget).attr("m-del-id")+"|||",evt	);
			var recordId = $(evt.currentTarget).attr("m-del-id");
			var eventName = $(evt.currentTarget).attr("m-event-id");
			
			////console.log("DELTED EVENT ID : "+$(evt.currentTarget).attr("m-del-id")+"|||EVENT NAME:"+eventName);
			if (recordId) {
				this.serviceMgr.deleteScheduleEventTask({uniqueId:recordId,eventName:eventName}, this.deleteScheduleEventSucess.bind(this),this.deleteScheduleEventFailure.bind(this));	
			}
		},
		deleteScheduleEventSucess :function(response){
			////console.log("DELETE SCHEDULE EVENT::",response);
			
			$("#kesa-loader").hide();
			if(response && response.responseStatus === "success"){
				 
				this.fetchAllRecordsClicked();
				//$("#kesa-earlier-processdetails-tbody").empty();			
				//this.createTablularDetailedUIBasedOnResponse(response);
				//$("#myModal").modal();
			}else{
				$("#fetchTableResultMessage").html("Unable to delete Event");	
			}
		},
		deleteScheduleEventFailure :function(){
			$("#kesa-loader").hide();
			$("#fetchTableResultMessage").html("unable to delete event");
		},
		fetchedDetailsSucess :function(response){
			////console.log("FETCHDETAILS::",response);
			
			$("#kesa-loader").hide();
			if(response && response.responseStatus === "success"){
				$("#kesa-earlier-processdetails-tbody").empty();			
				this.createTablularDetailedUIBasedOnResponse(response);
				$("#myModal").modal();
			}else{
				$("#fetchTableResultMessage").html("unable to fetch record details");	
			}
		},
		fetchDetailsFailure :function(){
			$("#kesa-loader").hide();
			$("#fetchTableResultMessage").html("unable to fetch record details");
		},
		fetchAllRecordsClicked :function(){
			$("#kesa-earlier-process-tbody").empty();
			this.serviceMgr.fetchAllRecords({}, this.fetchAllRecordsResponse.bind(this),this.fetchAllRecordsFailure.bind(this));
		
		},
		fetchAllRecordsFailure :function(){
			$("#kesa-loader").hide();
			$("#fetchTableResultMessage").html("DB -Connectivity test failed..");
		},
		fetchAllRecordsResponse:function(response){
			$("#kesa-loader").hide();
			
			
			////console.log("NEOZANT RESPONSE:::",response);
			
			if(response && response.responseStatus === "success"){
				this.createTablularUIBasedOnResponse(response);
			}else{
				$("#fetchTableResultMessage").html("FETCH RESPONSE ERROR.."+response.detailMessageOnFailure);
			}
		},
		
		createTablularUIBasedOnResponse :function(response){
			if (response && response.scheduledEventObject) {
				this.recordList = response.scheduledEventObject;
				var s1 = '';
				$("#kesa-earlier-process-tbody").empty();
				/*for (var i = 0; i<response.records.length;i++){
						s1 += '<tr>'
						s1 += '<td>'+ response.records[i].id + '</td>'
						s1 += '<td>'+ response.records[i].nameOfProcess + '</td>'
						s1 += '<td>'+ response.records[i].fileToExecute + '</td>'
						s1 += '<td>'+ response.records[i].dateTime + '</td>'
						s1 += '<td>'+ response.records[i].outputFormat + '</td>'
						s1 += '<td>'+ response.records[i].mode + '</td>'
						s1 += '<td>'+ response.records[i].emailList + '</td>'
						s1 += '<td>'+ response.records[i].status + '</td>'
						s1 += '<td> <input type="submit" value="View"  class="kesa-records-detailedView" m-data="'+response.records[i].id+'"> </td>'
						s1 += '</tr>'
				}*/
				for (var i = 0; i<response.scheduledEventObject.length;i++){
					s1 += '<tr>';
					s1 += '<td>'+ (i+1)+ '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].nameOfScheduledTask + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].fileToExecute + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].dateAndTimeInString + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].outputFileFormat + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].timerRepeatOn + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].typeOfEvent + '</td>';
					
					var statusOfEvent="";
					//console.log("TYPE OF EVENT:::"+response.scheduledEventObject[i].typeOfEvent );
					if(response.scheduledEventObject[i].typeOfEvent == "EMAIL"){
						statusOfEvent=response.scheduledEventObject[i].recipientAddress;
					}else{
						var host=response.scheduledEventObject[i].ftpRequest.ftpHost,
						    path=response.scheduledEventObject[i].ftpRequest.ftpFilePath;
						
						statusOfEvent="HOST:"+host+"|PATH::"+path;
					}
					s1 += '<td>'+ statusOfEvent + '</td>';
					s1 += '<td>'+ response.scheduledEventObject[i].status + '</td>';
					//s1 += '<td>'+ response.scheduledEventObject[i].environmentName + '</td>';
					//s1 += '<td>'+ response.scheduledEventObject[i].typeOfReport + '</td>';
					s1 += '<td> <input type="submit" value="View"  class="kesa-records-detailedView" m-data="'+response.scheduledEventObject[i].uniqueId+'"m-event-id="'+response.scheduledEventObject[i].nameOfScheduledTask+'"> </td>';
					s1 += '<td> <input type="submit" value="Delete"  class="kesa-records-deleteEvent" m-del-id="'+response.scheduledEventObject[i].uniqueId+'"m-event-id="'+response.scheduledEventObject[i].nameOfScheduledTask+'"> </td>';
					s1 += '</tr>';
			}
				
								
				$(".kesa-records-detailedView").off("click",this.fetchDetailedView.bind(this));
				
				$(".kesa-records-deleteEvent").off("click",this.deleteScheduleEvent.bind(this));
				
				$("#kesa-earlier-process-tbody").append(s1);
				$(".kesa-records-detailedView").on("click",this.fetchDetailedView.bind(this));
				$(".kesa-records-deleteEvent").on("click",this.deleteScheduleEvent.bind(this));
			}
		},
		createTablularDetailedUIBasedOnResponse :function(response){
			
			////console.log("FETCHDETAILS:::",response);
			//listOfDetailScheduledObject
			if (response && response.listOfDetailScheduledObject) {
				this.recordList = response.listOfDetailScheduledObject;
				var s1 = '';
				$("#kesa-earlier-processdetails-tbody").empty();
				for (var i = 0; i<response.listOfDetailScheduledObject.length;i++){
						s1 += '<tr>';
						s1 += '<td>'+ (i+1) + '</td>';
						s1 += '<td>'+ response.listOfDetailScheduledObject[i].executedTime + '</td>';
						s1 += '<td>'+ response.listOfDetailScheduledObject[i].result + '</td>';
						//s1 += '<td>'+ response.listOfDetailScheduledObject[i].outputFormat + '</td>';
						s1 += '<td>'+ response.listOfDetailScheduledObject[i].ouputFileName + '</td>';
						s1 += '<td>'+ response.listOfDetailScheduledObject[i].status + '</td>';
						//s1 += '<td>'+ response.listOfDetailScheduledObject[i].ftpStatus + '</td>';
						s1 += '</tr>';
				}
				$("#kesa-earlier-processdetails-tbody").append(s1);
			}
		},


		testConnectivity:function(){
			this.serviceMgr.triggerConnectivity({}, this.onConnectivityResponse.bind(this),this.onConnectivityFailure.bind(this));
			//this.onConnectivityResponse();
		},
		
		testFTPConnectivity:function(){
			
			
			/*ftphost=global-ftp.ovs.ten.fujitsu.com
			ftpusername=gcw|gl\\gcw_admin
			ftppassword=ZJt1J4bB
			productionsalespath=/gcw/TGS/FTCA/Sales/
			*/
			
			//kesaFtpHost,kesaFtpUserName,kesaFtpPassword,kesaFtpPath
			
			var ftpHost = $("#kesaFtpHost").val();
			var ftpUserName = $("#kesaFtpUserName").val();
			var ftpPassword = $("#kesaFtpPassword").val();
			var ftpPath = $("#kesaFtpPath").val();
			
			
			//console.log("FTP DETAILS WE GET IS::"+ftpHost+"| user name:"+ftpUserName+"|password:"+ftpPassword+"|PATH::"+ftpPath);
			
			this.ftpObject={
				     "ftpHost":ftpHost,
					 "ftpUsername":ftpUserName,
					 "ftpPassword":ftpPassword,
					 "ftpFilePath":ftpPath		
			};
			
			this.serviceMgr.triggerFtpConnectivity(this.ftpObject, this.onFtpConnectivityResponse.bind(this),this.onFtpConnectivityFailure.bind(this));
			//this.onFtpConnectivityResponse();
		},
		
		onConnectivityFailure :function(){
			$("#kesa-loader").hide();
			$("#connectionResultMessage").html("DB -Connectivity test failed..");
		},
		onFtpConnectivityFailure :function(){
			$("#kesa-loader").hide();
			$("#connectionResultMessage").html("FTP -Connectivity test failed..");
		},
		onConnectivityResponse:function(response){
			$("#kesa-loader").hide();
			
			////console.log("TEST DB CONNECTIVITY:::");
			////console.log(response);
			if(response.responseStatus === "success"){
				$("#kesaTestConnectionBtn").hide();
				$("#connectionResultMessage").html("DB - Connected successfully.");
				this.processStep = 2;
				this.renderProcessSteps();
				
				//$("#kesaTestConnectionBtn").removeClass(this.hideMeClassIdentifier);
			}else{
				
				$("#connectionResultMessage").html("DB - Connectivity test failed. "+response.detailMessageOnFailure);
				
				//this.onConnectivityFailure();
			}
			
		},
		onFtpConnectivityResponse:function(response){
			$("#kesa-loader").hide();
			
			//console.log("TEST FTP CONNECTIVITY:::"+response);
			
			
			//console.log("REQUEST OBJECT WE GET IS::"+this.processStep);
			
			
			if(response.responseStatus === "success"){
				//$("#kesaTestFTPConnectionBtn").hide();
				$("#configurationNotFilledMsg").html("FTP - Connection successful.");
				//$("#kesaNextBtn").removeClass(this.hideMeClassIdentifier);
	           
	            
				$('#kesaNextBtn').prop('disabled', false);
				/*$("#kesaNextBtn").show();
	            $("#kesaBackBtn").show();*/
	            
	           // this.processStep = 3;
				/*this.processStep = 3;
				this.renderProcessSteps();*/
				//$("#kesaTestConnectionBtn").removeClass(this.hideMeClassIdentifier);
				
			}else{
				
				$("#configurationNotFilledMsg").html("FTP - Connectivity test failed."+response.detailMessageOnFailure);
					
				// $("#kesaNextBtn").addClass(this.hideMeClassIdentifier);
				/*$("#kesaNextBtn").hide();
	            $("#kesaBackBtn").hide();*/
				//this.onConnectivityFailure();
			}
			
		},
		//KV
		getConfigData:function(){
			this.serviceMgr.triggerGetConfigData({}, this.onConfigResponse.bind(this),this.onConfigDataFailure.bind(this));
			//this.onConnectivityResponse();
		},
		onConfigDataFailure :function(){
			$("#kesa-loader").hide();
			$("#connectionResultMessage").html("CONFIG  -DATA test failed..");
		},
		onConfigResponse:function(response){
			$("#kesa-loader").hide();
			////console.log("CONFIG RESPONSE:::");
			////console.log(response);
			
			if(response.responseStatus === "success"){
				
				this.processStep++; //KV
				this.renderProcessSteps();
				
				this.onfileNamesFetchedResponse(response.listOfSourceFiles);
				
				$("#kesaEmailIds").val(response.recipientAddress);//=response.recipientAddress;
				
				var ftpRequest=response.ftpRequest;
				
							
				$("#kesaFtpHost").val(ftpRequest.ftpHost);
				$("#kesaFtpUserName").val(ftpRequest.ftpUsername);
				$("#kesaFtpPassword").val(ftpRequest.ftpPassword);
				$("#kesaFtpPath").val(ftpRequest.ftpFilePath);
				
			}else{
				$("#kesa-loader").hide();
				$("#connectionResultMessage").html(response.detailMessageOnFailure);
				
				//this.onConnectivityFailure();
			}
			
		},
		
		validateEmail:function (emailAddress) {
			var self = this;
            var regularExpression = /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))){2,6}$/i;
            
            var emails = emailAddress.split(",");
            ////console.log("emails ------- ", emails);
            var emailArray = [];
            var isEmailValid = true;
            $.each(emails, function(key, value) { 
            	////console.log("key :: "+key);
            	if(isEmailValid){
            		value = value.trim();
            		if(key ==0){
            			self.configurationObject["EMAIL"]=value;
            		}else{
            			emailArray.push(value);
            		}
            		var result = regularExpression.test(value);
            		
            		 if(!result){
            			 isEmailValid = result;
                 	}
            	}     	
            });
       
            this.configurationObject["RECIPIENT_ADDRESS"]=emailArray;
            //this.configurationObject["EMAIL"]=emailAddress;
           ////console.log("array : ", emailArray);
            return isEmailValid;

           // return regularExpression.test(emailAddress);
        },//KV
		
		
		navigateToHome :function(){
			$("#connectionResultMessage").html("");
			$("#kesaFileRadioList").empty();
			$("#kesaConfigDate").val("");
			$("#kesaConfigTime").val("");
			$("#kesaEmailIds").val("");
			
			$("#kesaFileName").val("");
			
			this.pageVisibilityState =2 ;
			this.showPageAsPerState();
			$("#kesaTestConnectionBtn").show();
			
		},
		dashboardOptionClicked:function(evt){
			switch($(evt.currentTarget).attr("data-map")){
				case "kesa_dashboard_01":
					this.pageVisibilityState =3 ;
					this.showPageAsPerState();
					break;
				case "kesa_dashboard_02":
					this.pageVisibilityState =4 ;
					this.showPageAsPerState();
			}
		},
		loginClicked:function(){
			$("#kesaLoginValidationMsg").html("");
			if($("#kesaUserName").val() === "" || $("#kesaPassword").val() === ""){
				$("#kesaLoginValidationMsg").html(" Please verify username & password ");
				return;
			}
			else{
				this.serviceMgr.triggerLogin({
					"username":$("#kesaUserName").val(),
					"password":$("#kesaPassword").val(),
					"role":"admin"
				}, this.onLoginResponse.bind(this),this.loginError.bind(this));
				
				//	this.loginError();
				//this.onLoginResponse();
			}
			
		},
		loginError :function(){
			$("#kesa-loader").hide();
			$("#kesaLoginValidationMsg").html(" Please verify username & password ");
		},
		onLoginResponse:function(response){
			$("#kesa-loader").hide();
			
			////console.log(response.responseStatus);
			if(response.responseStatus=== "success"){
				this.pageVisibilityState =2 ;
				this.showPageAsPerState();
			}else{
				////console.log("KESA:: LOGIN ERROR::"+response.detailMessageOnFailure);
				this.loginError();
			}
		}
};





kesa.AdminPanelServiceLayer = function(){
	
};

kesa.AdminPanelServiceLayer.prototype={
		triggerLogin :function(data,loginCallBack,loginCallBackError){
			
			$("#kesa-loader").show();
			/*data ={
					"username": "ROHAN",
					"role": "admin",
					"password": "1978"
			}*/
			if (!SIMULATE_BACKEND)	 {
				$.ajax({
					url:"neozant/user/login",
					type:"POST",
					contentType: "application/json; charset=utf-8",
				    data : JSON.stringify(data),
					success:loginCallBack,
					error:loginCallBackError,
					timeout:loginCallBackError
				});	
			}
			else {
				loginCallBack({
					responseStatus:'success'
				});
			}
			
		},
		triggerConnectivity:function(data,connectivityCallBack,connectivityCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
				$.ajax({
					url:"neozant/schedule/testDbConnection",
					contentType: "application/json; charset=utf-8",
				    data : JSON.stringify(data),
					type:"GET",
					success:connectivityCallBack,
					error:connectivityCallBackError,
					timeout:connectivityCallBackError
					
				});
			}
			else {
				connectivityCallBack({
					responseStatus:'success'
				});
			}
		},
		triggerFtpConnectivity:function(data,connectivityCallBack,connectivityCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
				$.ajax({
					url:"neozant/schedule/testFtpConnection",
					contentType: "application/json; charset=utf-8",
					data : JSON.stringify(data),
					type:"POST",
					success:connectivityCallBack,
					error:connectivityCallBackError,
					timeout:connectivityCallBackError
					
				});
			}
			else {
				connectivityCallBack({
					responseStatus:'success'
				});
			}
		},
		fetchAllRecords :function(data,configCallBack,configCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
				$.ajax({
					url:"neozant/schedule/getScheduledEvents",
					contentType: "application/json; charset=utf-8",
				    data : JSON.stringify(data),
					type:"GET",
					success:configCallBack,
					error:configCallBackError,
					timeout:configCallBackError
					
				});
			}
			else {
				var jsonResponse = {
					responseStatus:"success",
					records:[//PRADEEP PATIL, AURANGABAD KOLTE PATIL
						{
							"id":"1",
							"nameOfProcess":"X1",
							"fileToExecute":"X1/asbc.txt",
							"dateTime":"",
							"outputFormat":"xls",
							"mode":"EVERYDAY",
							"emailList":"abc@a.com,aaa@a.com",
							"status":"Completed"
						},
						{
							"id":"2",
							"nameOfProcess":"X1",
							"fileToExecute":"X1/asbc.txt",
							"dateTime":"",
							"outputFormat":"xls",
							"mode":"EVERYDAY",
							"emailList":"abc@a.com,aaa@a.com",
							"status":"Completed"
						},
						{
							"id":"3",
							"nameOfProcess":"X1",
							"fileToExecute":"X1/asbc.txt",
							"dateTime":"",
							"outputFormat":"xls",
							"mode":"EVERYDAY",
							"emailList":"abc@a.com,aaa@a.com",
							"status":"Completed"
						}

					]
				};
				configCallBack(jsonResponse);
			}

		},
		fetchedDetailedViewForId :function(data,configCallBack,configCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
			$.ajax({
				url:"neozant/schedule/getScheduledEventDetails",
				contentType: "application/json; charset=utf-8",
			    data : JSON.stringify(data),
				type:"POST",
				success:configCallBack,
				error:configCallBackError,
				timeout:configCallBackError
				
			});
		}
		else {
			var jsonResponse = {
					responseStatus:"success",
					records:[
						{
							"id":"1",
							"executedTime":"Execute Time",
							"result":"Result",
							"outputFormat":"xls",
							"outputFileName":"abc.txt",
							"emailStatus":"abc@a.com,aaa@a.com"
						},{
							"id":"2",
							"executedTime":"Execute Time",
							"result":"Result",
							"outputFormat":"xls",
							"outputFileName":"abc.txt",
							"emailStatus":"abc@a.com,aaa@a.com"
						},
						{
							"id":"3",
							"executedTime":"Execute Time",
							"result":"Result",
							"outputFormat":"xls",
							"outputFileName":"abc.txt",
							"emailStatus":"abc@a.com,aaa@a.com"
						},
						{
							"id":"4",
							"executedTime":"Execute Time",
							"result":"Result",
							"outputFormat":"xls",
							"outputFileName":"abc.txt",
							"emailStatus":"abc@a.com,aaa@a.com"
						},

					]
				};

				configCallBack(jsonResponse);
		}
			
		},
		deleteScheduleEventTask:function(data,configCallBack,configCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
			$.ajax({
				url:"neozant/schedule/deleteScheduledEvent",
				contentType: "application/json; charset=utf-8",
			    data : JSON.stringify(data),
				type:"POST",
				success:configCallBack,
				error:configCallBackError,
				timeout:configCallBackError
				
			});
		}
		else {
			var jsonResponse = {"recipientAddress":["rvaijapure@lao.ten.fujitsu.com"],"listOfSourceFiles":["C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\Deleted _TDC_ONHAND_INVENTORY.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\EBS USERS REPORT FOR KOBAYASHI.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\ITAC_Sales_query.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\TDC_onhand.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\TDC_ONHAND_INVENTORY.sql"],"outputFilePath":"C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY","sourceFilePath":"C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY","fileFormatSupported":["xls","csv"],"detailMessageOnFailure":null,"responseStatus":"success"};
			configCallBack(jsonResponse);
		}
			
		},
		triggerGetConfigData:function(data,configCallBack,configCallBackError){
			$("#kesa-loader").show();
			if (!SIMULATE_BACKEND)	 {
			$.ajax({
				url:"neozant/schedule/getConfigData",
				contentType: "application/json; charset=utf-8",
			    data : JSON.stringify(data),
				type:"GET",
				success:configCallBack,
				error:configCallBackError,
				timeout:configCallBackError
				
			});
		}
		else {
			var jsonResponse = {"recipientAddress":["rvaijapure@lao.ten.fujitsu.com"],"listOfSourceFiles":["C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\Deleted _TDC_ONHAND_INVENTORY.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\EBS USERS REPORT FOR KOBAYASHI.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\ITAC_Sales_query.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\TDC_onhand.sql","C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY\\TDC_ONHAND_INVENTORY.sql"],"outputFilePath":"C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\DESTINATION_DIRECTORY","sourceFilePath":"C:\\Users\\Ketan\\Desktop\\SCHEDULER_RELATED\\SOURCE_DIRECTORY","fileFormatSupported":["xls","csv"],"detailMessageOnFailure":null,"responseStatus":"success"};
			configCallBack(jsonResponse);
		}
			
		},
		
		triggerConfigSubmission:function(data,submissionCallBack,submissionCallBackError){
			$("#kesa-loader").show();
			$.ajax({
				url:"neozant/schedule/triggerEvent",
				contentType: "application/json; charset=utf-8",
			    data : JSON.stringify(data),
				type:"POST",
				success:submissionCallBack,
				error:submissionCallBackError,
				timeout:submissionCallBackError
				
			});
		}
		
};




kesa.AdminPanelController= function(){
	this.viewMgr ;
	this.createViewInstance();
	
};  


kesa.AdminPanelController.prototype={
		
		createViewInstance :function(){
			////console.log("view Instance");
			viewMgr = new kesa.AdminPanelView();
			viewMgr.onAppLoaded();
		},
		
};