$("#createButton").click(function(){
	var data = new Object();
	data.id = $("#idField").val();
	data.msg = $("#msgField").val();
	$.ajax({
		url:"/Lab2/Todo",
		method:"POST",
		dataType: "json",
		data: JSON.stringify(data),
        contentType: 'application/json',
        mimeType: 'application/json',
		success: function(data,status,xhr){
			var res=JSON.parse(AJAX.responseText)
			$("#todoHeader").html(data);
			alert(data);
			alert(res);
		},
		error: function(data,status,xhr){
			$("#todoHeader").html("Error");
		},
	});
	
});

//If making a simple Get call to a server then there is a shorthand for the above ajax
$("#getTodoButton").click(function(){
	var id = $("#idField").val();
	$.ajax({
		url:"/Lab2/Todo",
		method:"POST",
		dataType: "json",
		success: function(data,status,xhr){
			$("#todoHeader").html(data);
		},
		error: function(data,status,xhr){
			$("#todoHeader").html("Error");
		},
		data: id
	});
	
});

//You can get pretty much anything even other html.
$("#getAllTodosButton").click(function(){
	$.get("/Lab2/ToDo", function(data){
		$("#todoHeader").html(data);
	});
	
});

//You can even grab other javascript files using getScript
$("#deleteTodoButton").click(function(){
	var id = $("#idField").val();
	$.ajax({
		url:"/Lab2/Todo",
		method:"POST",
		dataType: "json",
		success: function(data,status,xhr){
			$("#todoHeader").html(data);
		},
		error: function(data,status,xhr){
			$("#todoHeader").html("Error");
		},
		data: id
	});
});