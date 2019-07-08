<script>
	var saveSlotTotal = 1;

	$(function() {
		addSubmitButton_RExport();
		setupPresets();
	});

	function addSubmitButton_RExport() {
		$("#recruitExportButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.");
			$.ajax({ url: "recruitExport?"+$('#recruitExportForm').serialize() });
			event.preventDefault();
		});
	}

	function setupPresets() {
		$( "#saveDialog" ).dialog({
            autoOpen: false
         });
		$( "#restoreDialog" ).dialog({
            autoOpen: false
         });
		$("#saveButton").button().click(function(event) {
			$( "#saveDialog" ).dialog( "open" );
			event.preventDefault();
		}); 
		$("#restoreButton").button().click(function(event) {
			$( "#restoreDialog" ).dialog( "open" );
			event.preventDefault();
		}); 
		updateSaveSlots();
	}

    function saveAsPreset(slotNum) {
    	if($.jStorage.storageAvailable()){
    		// Save the name
    		var saveName = $('#saveName').val();
    		if(saveName && saveName != '(default)'){
    			var nameKey = "Slot"+slotNum+"Name";
    			$.jStorage.set(nameKey, saveName);
    		}
    		$('#saveName').text('(default)');
    		
    		// Save the form data
            var dataKey = "Slot"+slotNum+"Data";
            var formData = $('#recruitExportForm').serialize();
            $.jStorage.set(dataKey, formData);
            
            // Hide and update
            $('#saveDialog').dialog('close');
            updateSaveSlots();
        }
        else{
        	alert("Sorry, not enough jstorage available to save your preset.  Clear your browser cookies.");
       	}
    }
    
    function restorePreset(slotNum) {
        deserialize(getPresetData(slotNum));
        $('#restoreDialog').dialog('close');
        updateSaveSlots();
    }
    
    function getPresetData(slotNum) {
    	var key = "Slot"+slotNum+"Data";
    	return $.jStorage.get(key);
    }
    
  	function updateSaveSlots(){
  		updateSaveSlotTotal();
  		updateSaveSlotButtons();
  		updateSaveSlotStyles();
  		updateRestoreButton();
  	}
  	
  	function getSaveSlotName(slotNum){
  		var name = "Slot "+slotNum;
  		var key = "Slot"+slotNum+"Name";
  		var savedName = $.jStorage.get(key);
  		if(savedName){
  			name = savedName;
  		}
  		return name;
  	}
  	
  	function updateSaveSlotTotal(){
  		for(var i=1; i<=100; i++){
  			if(!getPresetData(i)){
  				saveSlotTotal = i;
  				break;
  			}
  		}
  	}
  	
  	function updateSaveSlotButtons(){
  		var saveButtonBarHtml = '';
  		for(var i=1; i<=saveSlotTotal; i++){
  			saveButtonBarHtml = saveButtonBarHtml+'<button id="save'+i+'" class="button" onclick="javascript:saveAsPreset('+i+');" style="margin-bottom:10px;"></button>'
		}
		$('#saveButtonBar').html(saveButtonBarHtml);
		
		var restoreButtonBarHtml = '';
  		for(var i=1; i<saveSlotTotal; i++){
  			restoreButtonBarHtml = restoreButtonBarHtml+'<button id="restore'+i+'" class="button" onclick="javascript:restorePreset('+i+');" style="margin-bottom:10px;"></button>'
		}
		$('#restoreButtonBar').html(restoreButtonBarHtml);
  	}
  	
  	function updateSaveSlotStyles(){
  		for(var i=1; i<=saveSlotTotal; i++){
      		$('#save'+i).text(getSaveSlotName(i));
      		$('#restore'+i).text(getSaveSlotName(i));
      		
      		if(getPresetData(i)){
      			$('#save'+i).addClass('btn-primary');
      			$('#restore'+i).addClass('btn-primary');
      			$('#restore'+i).removeAttr( 'disabled' );
      		}
      		else{
      			$('#save'+i).removeClass('btn-primary');
      			$('#restore'+i).removeClass('btn-primary');
      			$('#restore'+i).attr('disabled','true');
      		}
      	}
  	}
  	
  	function updateRestoreButton(){
  		if(saveSlotTotal==1){
  			$('#restoreButton').attr('disabled','true');
  		}
  		else{
  			$('#restoreButton').removeAttr( 'disabled' );
  		}
  	}

  	function toggleSaveNameInput(checkbox){
  	  	if(checkbox.checked){
			$('#saveNameLabel').show();
			$('#saveName').val('').show();
  	  	}
  	  	else{
			$('#saveNameLabel').hide();
			$('#saveName').val('(default)').hide();
  	  	}
  	}
</script>

<form id="recruitExportForm">
	<fieldset>
		<label for="user">Username: </label> <input name="username" id="username" class="ui-corner-all textInput"/> 
		<label for="password">Password: </label> <input type="password" name="password" id="password" class="ui-corner-all textInput"/> 
		<label for="world">World: </label>
		<select name="world" id="world" class="ui-corner-all comboInput">
			<% for( String hdWorld : ( String[] )request.getAttribute( "hdWorldList" ) ){ %>
			<option value="<%=hdWorld%>"><%=hdWorld%></option>
			<% } %>
		</select>
	</fieldset>
	<fieldset>
		<h3>Weights for Custom Overall (COVR):</h3>
		<p>
			<label for="athleticismWeight">Athleticism: </label> <input name="athleticismWeight" id="athleticismWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="speedWeight">Speed: </label> <input name="speedWeight" id="speedWeight" class="ui-corner-all narrowTextInput" value="10"/> 
		</p>
		<p>
			<label for="reboundingWeight">Rebounding: </label> <input name="reboundingWeight" id="reboundingWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="defenseWeight">Defense: </label> <input name="defenseWeight" id="defenseWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="shotBlockingWeight">Shot Blocking: </label> <input name="shotBlockingWeight" id="shotBlockingWeight" class="ui-corner-all narrowTextInput" value="10"/> 
		</p>
		<p>
			<label for="lowPostWeight">Low Post: </label> <input name="lowPostWeight" id="lowPostWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="perimeterWeight">Perimeter: </label> <input name="perimeterWeight" id="perimeterWeight" class="ui-corner-all narrowTextInput" value="10"/>
			<label for="ftShootingWeight">FT Shooting: </label> <input name="ftShootingWeight" id="ftShootingWeight" class="ui-corner-all narrowTextInput" value="10"/> 
		</p>
		<p>
			<label for="ballHandlingWeight">Ball Handling: </label> <input name="ballHandlingWeight" id="ballHandlingWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="passingWeight">Passing: </label> <input name="passingWeight" id="passingWeight" class="ui-corner-all narrowTextInput" value="10"/> 
		</p>
		<p>
			<label for="staminaWeight">Stamina: </label> <input name="staminaWeight" id="staminaWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="durabilityWeight">Durability: </label> <input name="durabilityWeight" id="durabilityWeight" class="ui-corner-all narrowTextInput" value="10"/> 
			<label for="workEthicWeight">Work Ethic: </label> <input name="workEthicWeight" id="workEthicWeight" class="ui-corner-all narrowTextInput" value="10"/> 
		</p>
	</fieldset>
	<fieldset>
		<button id="saveButton" class="button">Save Preset</button>
		<button id="restoreButton" class="button">Restore Preset</button>
	</fieldset>
	<fieldset>
		<h3>Player Filters:</h3>
		<p>
			<label for="decisionStatusFilter">Decision Status: </label>
			<select name="decisionStatusFilter" id="decisionStatusFilter" class="ui-corner-all comboInput">
				<option value="Any">Any</option>
				<option value="Unsigned">Unsigned</option>
				<option value="Signed">Signed</option>
			</select>  
			<label for="divisionFilter">Division: </label>
			<select name="divisionFilter" id="divisionFilter" class="ui-corner-all comboInput">
				<option value="All">All</option>
				<option value="DI">DI</option>
				<option value="DII">DII</option>
				<option value="DIII">DIII</option>
			</select>  
		</p>
		<p>
			<label for="includeUnknown">Include Unknown Recruits: </label>
			<input name="includeUnknown" id="includeUnknown" class="ui-corner-all checkboxInput" type="checkbox"/>  
		</p>
	</fieldset>
	<fieldset>
		<br />
		<label for="email">Email: </label> <input type="email" name="email" id="email" class="ui-corner-all wideTextInput"> 
		<button id="recruitExportButton" class="button"><strong>Export Recruit Pool</strong></button>
	</fieldset>
</form>
<div id="saveDialog" title="Save Preset">
	<p>
		<label for="changeSlotName">Change Slot Name?</label>
		<input id="changeSlotName" class="ui-corner-all checkboxInput" type="checkbox" onclick="javascript:toggleSaveNameInput(this);" checked/>
	</p>
	<p>
		<label for="saveName" id="saveNameLabel">Save Preset as:</label>
		<input id="saveName" value="" class="ui-corner-all textInput"/>
	</p>
	<div id="saveButtonBar" style="padding: 14px 15px 6px;"></div>
</div>
<div id="restoreDialog" title="Restore Preset">
	<p>Restore a preset.</p>
	<div id="restoreButtonBar" style="padding: 14px 15px 6px;"></div>
</div>