<script>
	$(function() {
		addSubmitButton_RCC();
		$("#firstNRecruits").hide();
		$("#firstNRecruitsLabel").hide();
	});

	function addSubmitButton_RCC() {
		$("#recruitColorCodingButton").button().click(function(event) {
			event.preventDefault();
			$("#recruitColorCodingForm").submit();
			alert("Your request has been submitted.  You can close this window and monitor your email for the response.");
			return false;
		}); 
	}

	function addSubmitButton_RCC() {
		$("#recruitColorCodingButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.");
			$.ajax({ url: "recruitColorCoding?"+$('#recruitColorCodingForm').serialize() });
			event.preventDefault();
		}); 
	}
	
	function changeUpdateAll(cb){
		if(cb.checked){
			$("#firstNRecruits").hide();
			$("#firstNRecruitsLabel").hide();
		}
		else{
			$("#firstNRecruits").show();
			$("#firstNRecruitsLabel").show();
		}
	}
</script>

<form id="recruitColorCodingForm">
	<fieldset>
		<label for="user">Username: </label> <input name="username" id="username" class="ui-corner-all textInput"/> 
		<label for="password">Password: </label> <input type="password" name="password" id="password" class="ui-corner-all textInput"/> 
		<label for="world">World: </label>
		<select name="world" id="world" class="ui-corner-all comboInput">
			<% for( String hdWorld : ( String[] )request.getAttribute( "hdWorldList" ) ){ %>
			<option value="<%=hdWorld%>"><%=hdWorld%></option>
			<% } %>
		</select> 
		<br /> <br /> 
	</fieldset>
	<fieldset>
		<p>
			<label for="position">Position: </label>
			<select name="position" id="position" class="ui-corner-all comboInput">
				<% for( String position : ( String[] )request.getAttribute( "positionList" ) ){ %>
				<option value="<%=position%>"><%=position%></option>
				<% } %>
			</select>
			<label for="projectedLevel">Projected Level: </label>
			<select name="projectedLevel" id="projectedLevel" class="ui-corner-all comboInput">
				<% for( String projectedLevel : ( String[] )request.getAttribute( "projectedLevelList" ) ){ %>
				<option value="<%=projectedLevel%>"><%=projectedLevel%></option>
				<% } %>
			</select>
			<label for="currentColor">Current Color: </label>
			<select name="currentColor" id="currentColor" class="ui-corner-all comboInput" disabled>
				<% for( String currentColor : ( String[] )request.getAttribute( "hdColorCriteriaList" ) ){ %>
				<option value="<%=currentColor%>"><%=currentColor%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="distanceFrom">Distance From: </label>
			<select name="distanceFrom" id="distanceFrom" class="ui-corner-all comboInput">
				<% for( String distance : ( String[] )request.getAttribute( "distanceList" ) ){ %>
				<option value="<%=distance%>"><%=distance%></option>
				<% } %>
			</select>
			<label for="distanceTo">Distance To: </label>
			<select name="distanceTo" id="distanceTo" class="ui-corner-all comboInput">
				<% for( String distance : ( String[] )request.getAttribute( "distanceList" ) ){ %>
				<option value="<%=distance%>"><%=distance%></option>
				<% } %>
			</select>
			<label for="state">State: </label>
			<select name="state" id="state" class="ui-corner-all comboInput">
				<% for( String state : ( String[] )request.getAttribute( "stateList" ) ){ %>
				<option value="<%=state%>"><%=state%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="eligibleYears">Eligible Years: </label>
			<select name="eligibleYears" id="eligibleYears" class="ui-corner-all comboInput">
				<% for( String eligibleYears : ( String[] )request.getAttribute( "eligibleYearsList" ) ){ %>
				<option value="<%=eligibleYears%>"><%=eligibleYears%></option>
				<% } %>
			</select>
			<label for="eligibility">Eligibility: </label>
			<select name="eligibility" id="eligibility" class="ui-corner-all comboInput">
				<% for( String eligibility : ( String[] )request.getAttribute( "eligibilityList" ) ){ %>
				<option value="<%=eligibility%>"><%=eligibility%></option>
				<% } %>
			</select>
			<label for="scoutingLevel">Scouting Level: </label>
			<select name="scoutingLevel" id="scoutingLevel" class="ui-corner-all comboInput">
				<% for( String scoutingLevel : ( String[] )request.getAttribute( "scoutingLevelList" ) ){ %>
				<option value="<%=scoutingLevel%>"><%=scoutingLevel%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="decisionStatus">Decision Status: </label>
			<select name="decisionStatus" id="decisionStatus" class="ui-corner-all comboInput">
				<% for( String decisionStatus : ( String[] )request.getAttribute( "decisionStatusList" ) ){ %>
				<option value="<%=decisionStatus%>"><%=decisionStatus%></option>
				<% } %>
			</select>
			<label for="signingPref">Signing Pref: </label>
			<select name="signingPref" id="signingPref" class="ui-corner-all comboInput">
				<% for( String signingPref : ( String[] )request.getAttribute( "signingPrefList" ) ){ %>
				<option value="<%=signingPref%>"><%=signingPref%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="playingPref">Playing Pref: </label>
			<select name="playingPref" id="playingPref" class="ui-corner-all comboInput">
				<% for( String playingPref : ( String[] )request.getAttribute( "playingPrefList" ) ){ %>
				<option value="<%=playingPref%>"><%=playingPref%></option>
				<% } %>
			</select>
			<label for="distancePref">Distance Pref: </label>
			<select name="distancePref" id="distancePref" class="ui-corner-all comboInput">
				<% for( String distancePref : ( String[] )request.getAttribute( "distancePrefList" ) ){ %>
				<option value="<%=distancePref%>"><%=distancePref%></option>
				<% } %>
			</select>
		</p>	
		<p>
			<label for="stylePref">Style Pref: </label>
			<select name="stylePref" id="stylePref" class="ui-corner-all comboInput">
				<% for( String stylePref : ( String[] )request.getAttribute( "stylePrefList" ) ){ %>
				<option value="<%=stylePref%>"><%=stylePref%></option>
				<% } %>
			</select>
			<label for="offensePref">Offense Pref: </label>
			<select name="offensePref" id="offensePref" class="ui-corner-all comboInput">
				<% for( String offensePref : ( String[] )request.getAttribute( "offensePrefList" ) ){ %>
				<option value="<%=offensePref%>"><%=offensePref%></option>
				<% } %>
			</select>
			<label for="defensePref">Defense Pref: </label>
			<select name="defensePref" id="defensePref" class="ui-corner-all comboInput">
				<% for( String defensePref : ( String[] )request.getAttribute( "defensePrefList" ) ){ %>
				<option value="<%=defensePref%>"><%=defensePref%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="successPref">Success Pref: </label>
			<select name="successPref" id="successPref" class="ui-corner-all comboInput">
				<% for( String successPref : ( String[] )request.getAttribute( "successPrefList" ) ){ %>
				<option value="<%=successPref%>"><%=successPref%></option>
				<% } %>
			</select>
			<label for="conferencePref">Conference Pref: </label>
			<select name="conferencePref" id="conferencePref" class="ui-corner-all comboInput">
				<% for( String conferencePref : ( String[] )request.getAttribute( "conferencePrefList" ) ){ %>
				<option value="<%=conferencePref%>"><%=conferencePref%></option>
				<% } %>
			</select>
			<label for="longevityPref">Longevity Pref: </label>
			<select name="longevityPref" id="longevityPref" class="ui-corner-all comboInput">
				<% for( String longevityPref : ( String[] )request.getAttribute( "longevityPrefList" ) ){ %>
				<option value="<%=longevityPref%>"><%=longevityPref%></option>
				<% } %>
			</select>
		</p>
		<p>
			<label for="sortBy">Sort By: </label>
			<select name="sortBy" id="sortBy" class="ui-corner-all comboInput">
				<% for( String sortBy : ( String[] )request.getAttribute( "sortByList" ) ){ %>
				<option value="<%=sortBy%>"><%=sortBy%></option>
				<% } %>
			</select>
			<label for="sortDirection">Sort Direction: </label>
			<select name="sortDirection" id="sortDirection" class="ui-corner-all comboInput">
				<% for( String sortDirection : ( String[] )request.getAttribute( "sortDirectionList" ) ){ %>
				<option value="<%=sortDirection%>"><%=sortDirection%></option>
				<% } %>
			</select>
		</p>
	</fieldset>
	<fieldset>
		<p>
			<label for="updateColor">Update to Color: </label>
			<select name="updateColor" id="updateColor" class="ui-corner-all comboInput">
				<% for( String color : ( String[] )request.getAttribute( "hdColorSetList" ) ){ %>
				<option value="<%=color%>"><%=color%></option>
				<% } %>
			</select>
			<label for="updateAll">Update All: </label>
			<input name="updateAll" id="updateAll" class="ui-corner-all checkboxInput" type="checkbox" onchange="changeUpdateAll(this);" checked/>  
			<label for="firstNRecruits" id="firstNRecruitsLabel">Update first N Recruits: </label>
			<input name="firstNRecruits" id="firstNRecruits" class="ui-corner-all textInput"/>
		</p>
		<br /> 
		<label for="email">Email: </label> <input type="email" name="email" id="email" class="ui-corner-all wideTextInput"> 
		<button id="recruitColorCodingButton" class="button"><strong>Update Color Codes</strong></button>
	</fieldset>
</form>