<script>
	$(function() {
		addSubmitButton_CHR();
	});

	function addSubmitButton_CHR() {
		$("#coachHiringReportButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.");
			$.ajax({ url: "coachHiringReport?"+$('#coachHiringReportForm').serialize() });
			event.preventDefault();
		}); 
	}
</script>

<form id="coachHiringReportForm">
	<fieldset>
		<label for="user">Username: </label> <input name="username" id="username" class="ui-corner-all textInput"/> 
		<label for="password">Password: </label> <input type="password" name="password" id="password" class="ui-corner-all textInput"/> 
		<label for="world">World: </label>
		<select name="world" id="world" class="ui-corner-all comboInput">
			<% for( String hbdWorld : ( String[] )request.getAttribute( "hbdWorldList" ) ){ %>
			<option value="<%=hbdWorld%>"><%=hbdWorld%></option>
			<% } %>
		</select>
		 <br /> <br /> <br /> 
		<label for="email">Email: </label> <input type="email" name="email" id="email" class="ui-corner-all wideTextInput"> 
		<button id="coachHiringReportButton" class="button"><strong>Run Coach Hiring Report</strong></button>
	</fieldset>
</form>