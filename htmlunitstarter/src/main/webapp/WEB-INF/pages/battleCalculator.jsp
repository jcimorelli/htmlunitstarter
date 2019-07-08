<script>
	$(function() {
		addAddFunction_BC();
		addRemoveFunctions_BC();
		addSubmitButton_BC();
	});

	function addAddFunction_BC() {
		const newTeamElement = '<tr><td><input name="team" class="ui-corner-all textInput"/></td><td><select name="prestige" class="ui-corner-all comboInput"><option value="A+" selected>A+</option><option value="A">A</option><option value="A-">A-</option><option value="B+">B+</option><option value="B">B</option><option value="B-">B-</option><option value="C+">C+</option><option value="C">C</option><option value="C-">C-</option><option value="D+">D+</option><option value="D">D</option><option value="D-">D-</option></select></td><td><input name="netprefs" class="ui-corner-all narrowTextInput" type="number" min="-16" max="16" step="1" value="0"/></td><td><input name="aps" class="ui-corner-all narrowTextInput" type="number" min="0" step="1" value="0"/></td><td><input type="hidden" name="cvs" value="0"><input class="ui-corner-all checkboxInput" type="checkbox" onclick="this.previousSibling.value=1-this.previousSibling.value"/></td><td><input name="hvs" class="ui-corner-all narrowTextInput" type="number" min="0" max="20" step="1" value="0"/></td><td><input type="hidden" name="starts" value="0"><input class="ui-corner-all checkboxInput" type="checkbox" onclick="this.previousSibling.value=1-this.previousSibling.value"/></td><td><select name="minutes" class="ui-corner-all comboInput"><option value="None" selected>None</option><option value="10">10</option><option value="15">15</option><option value="20">20</option><option value="25">25</option></select></td><td class="interestLevel"/><td class="signingOdds"/><td><a href="#" class="ui-icon ui-icon-minusthick removeBC" title="Remove Team"></a></td></tr>';
		$('a#add').button({icon : 'ui-icon-plusthick'}).click(function() {
			$(newTeamElement).fadeIn("slow").appendTo('#battleCalculatorTable');
			addRemoveFunctions_BC();
			return false;
		});
	}

	function addRemoveFunctions_BC() {
		$('.removeBC').click(function() {
			$(this).parent().parent().fadeOut(300, function() {
				$(this).empty();
				return false;
			});
		});
	}
	
	function addSubmitButton_BC() {
		$("#battleCalculatorButton").button().click(function(event) {
			$.ajax({ url: "battleCalculator?"+$('#battleCalculatorForm').serialize() })
				.done(function( response ) {
			    	console.log( {response} );
			        for (var i = 0; i < response.teams.length; i++) {
				        var team = response.teams[i];
			        	var nameInput = $('input').filter(function() { return this.value == team.name });
			         	console.log({name: team.name, nameInput: nameInput});
			         	var interestLevelTd = nameInput.parent().next().next().next().next().next().next().next().next();
			         	interestLevelTd.html(team.interestLevel);
			         	interestLevelTd.addClass('highlighted');
			         	var signingOddsTd = interestLevelTd.next();
			         	signingOddsTd.html(team.signingOdds + '%');
			         	signingOddsTd.addClass('highlighted');
			        }
		         	setTimeout(function () {
		         		$( ".interestLevel" ).removeClass('highlighted');
		         		$( ".signingOdds" ).removeClass('highlighted');
		            }, 250);
			    });
			event.preventDefault();
		}); 
	}
</script>

<form id="battleCalculatorForm">
	<fieldset>
		<table id="battleCalculatorTable">
			<tr>
				<th title="Name doesn't matter for calc, as long as it is unique">Team</th>
				<th title="At the time of the majority of recruiting effort (since it could be different across sessions)">Prestige</th>
				<th title="Sum of ALL preferences, where Very Good=2, Good=1, Neutral=0, Bad=-1, Very Bad=-2">Net Prefs</th>
				<th title="Total Attention Points">APs</th>
				<th title="Campus Visit?">CV</th>
				<th title="Total Home Visits">HVs</th>
				<th title="Was a start promised?">Start</th>
				<th title="How many minutes were promised?">Minutes</th>
				<th title="Resulting Recruit Interest Level">Int Level</th>
				<th title="Resulting Recruit Signing Percentage">Odds</th>
				<th title="Remove this team from the battle">Remove</th>
			</tr>
			<tr>
				<td><input name="team" class="ui-corner-all textInput"/></td>
				<td>
					<select name="prestige" class="ui-corner-all comboInput">
						<option value="A+" selected>A+</option>
						<option value="A">A</option>
						<option value="A-">A-</option>
						<option value="B+">B+</option>
						<option value="B">B</option>
						<option value="B-">B-</option>
						<option value="C+">C+</option>
						<option value="C">C</option>
						<option value="C-">C-</option>
						<option value="D+">D+</option>
						<option value="D">D</option>
						<option value="D-">D-</option>
					</select>
				</td>
				<td><input name="netprefs" class="ui-corner-all narrowTextInput" type="number" min="-16" max="16" step="1" value="0"/></td>
				<td><input name="aps" class="ui-corner-all narrowTextInput" type="number" min="0" step="1" value="0"/></td>
				<td><input type="hidden" name="cvs" value="0"><input class="ui-corner-all checkboxInput" type="checkbox" onclick="this.previousSibling.value=1-this.previousSibling.value"/></td>
				<td><input name="hvs" class="ui-corner-all narrowTextInput" type="number" min="0" max="20" step="1" value="0"/></td>
				<td><input type="hidden" name="starts" value="0"><input class="ui-corner-all checkboxInput" type="checkbox" onclick="this.previousSibling.value=1-this.previousSibling.value"/></td>
				<td>
					<select name="minutes" class="ui-corner-all comboInput">
						<option value="None" selected>None</option>
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
						<option value="25">25</option>
					</select>
				</td>
				<td class="interestLevel"/>
				<td class="signingOdds"/>
				<td><a href="#" class="ui-icon ui-icon-minusthick removeBC" title="Remove Team"></a></td>
			</tr>
		</table>
		<p>
			<a id="add"></a>
		</p>
		<br />  
		<button id="battleCalculatorButton" class="button"><strong>Calculate Battle</strong></button>
	</fieldset>
</form>