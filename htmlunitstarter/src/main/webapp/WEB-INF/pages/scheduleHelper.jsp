<script>
	$(function() {
		addSubmitButton_SCH();
	});

	function addSubmitButton_SCH() {
		$("#scheduleHelperButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.");
			$.ajax({ url: "scheduleHelper?"+$('#scheduleHelperForm').serialize() });
			event.preventDefault();
		}); 
	}
</script>

<form id="scheduleHelperForm">
	<fieldset>
		<label for="user">Username: </label> <input name="username" id="username" class="ui-corner-all textInput"/> 
		<label for="password">Password: </label> <input type="password" name="password" id="password" class="ui-corner-all textInput"/> 
		<label for="world">World: </label>
		<select name="world" id="world" class="ui-corner-all comboInput">
			<% for( String hdWorld : ( String[] )request.getAttribute( "hdWorldList" ) ){ %>
			<option value="<%=hdWorld%>"><%=hdWorld%></option>
			<% } %>
		</select>
		 <br /> <br /> <br /> 
		<label for="email">Email: </label> <input type="email" name="email" id="email" class="ui-corner-all wideTextInput"> 
		<button id="scheduleHelperButton" class="button"><strong>Get Scheduling Data</strong></button>
	</fieldset>
</form>