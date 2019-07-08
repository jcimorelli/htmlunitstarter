<script>
	$(function() {
		addSubmitButton_IFA();
	});

	function addSubmitButton_IFA() {
		$("#ifaBudgetAnalysisButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.");
			$.ajax({ url: "ifaBudgetAnalysis?"+$('#ifaBudgetAnalysisForm').serialize() });
			event.preventDefault();
		}); 
	}
</script>

<form id="ifaBudgetAnalysisForm">
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
		<button id="ifaBudgetAnalysisButton" class="button"><strong>Run IFA Budget Report</strong></button>
	</fieldset>
</form>