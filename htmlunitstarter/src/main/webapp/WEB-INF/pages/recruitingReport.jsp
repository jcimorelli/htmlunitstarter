<script>
	$(function() {
		addAddFunction_RR();
		addRemoveFunctions_RR();
		addSubmitButton_RR();
	});

	function addAddFunction_RR() {
		var newTeamElement = '<p>Team: <input name="team" class="ui-corner-all textInput"/><a href="#" class="ui-icon ui-icon-minusthick removeRR" title="Remove Team"/></p>';
		$('a#add').button({icon : 'ui-icon-plusthick'}).click(function() {
			$(newTeamElement).fadeIn("slow").appendTo('#teams');
			addRemoveFunctions_RR();
			return false;
		});
	}

	function addRemoveFunctions_RR() {
		$('.removeRR').click(function() {
			$(this).parent().fadeOut(300, function() {
				$(this).empty();
				return false;
			});
		});
	}
	
	function addSubmitButton_RR() {
		$("#recruitingReportButton").button().click(function(event) {
			alert("Hit OK to submit your request.  You can then close this window and monitor your email for the response.  Be patient, this operation can take a while.");
			$.ajax({ url: "recruitingReport?"+$('#recruitingReportForm').serialize() });
			event.preventDefault();
		}); 
	}
</script>

<form id="recruitingReportForm">
	<fieldset>
		<label for="user">Username: </label> <input name="username" id="username" class="ui-corner-all textInput"/> 
		<label for="password">Password: </label> <input type="password" name="password" id="password" class="ui-corner-all textInput"/> 
		<label for="world">World: </label>
		<select name="world" id="world" class="ui-corner-all comboInput">
			<% for( String hdWorld : ( String[] )request.getAttribute( "hdWorldList" ) ){ %>
			<option value="<%=hdWorld%>"><%=hdWorld%></option>
			<% } %>
		</select>
		<br />
		<div id="teams">
			<p>
				Team: <input name="team" class="ui-corner-all textInput"/>
			</p>
		</div>
		<p>
			<a id="add"></a>
		</p>
		<br /> 
		<label for="email">Email: </label> <input type="email" name="email" id="email" class="ui-corner-all wideTextInput"> 
		<button id="recruitingReportButton" class="button"><strong>Run Recruiting Report</strong></button>
	</fieldset>
</form>