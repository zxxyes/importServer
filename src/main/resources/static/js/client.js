
$(function () {
    
    
//    $("#upForm").submit(function (event) {
//        event.preventDefault();
//
//        var $form = $(this);
//
//        doUp($form);
//    });
	
//    $("#submitButton").click(function () {
//    	var formData = new FormData();
//    	doUp(formData);
//    });
	
	$.ajax({
        url: "/assetsTypes",
        type: "GET",
        contentType: "application/json",
        success: function (data, textStatus, jqXHR) {
            console.log(data);
            //var obj = JSON.parse(data);
            $("#assetsType").append("<option value='0000'>==请选择==</option>");
            for(var key in data){
            	  console.log("属性：" + key + ",值：" + data[key]);
            	  $("#assetsType").append("<option value='"+key+"'>"+data[key]+"</option>");
            	}
           
        },
        error: function (jqXHR, textStatus, errorThrown) {
            if (jqXHR.status === 401 || jqXHR.status === 403) {
                $('#loginErrorModal')
                    .modal("show")
                    .find(".modal-body")
                    .empty()
                    .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
            } else {
                throw new Error("an unexpected error occured: " + errorThrown);
            }
        }
        //$("#excelType").append("<option value='0000'>==请选择==</option>");
    });
	
	
	
	$("#assetsType").change(function(){
		var selectedVal = $("#assetsType option:selected").val();
		console.log(selectedVal);
		$.ajax({
	        url: "/imporotTypes",
	        type: "GET",
	        contentType: "application/json",
	        data:{ assetsType: selectedVal },
	        success: function (data, textStatus, jqXHR) {
	            console.log(data);
	            for(var key in data){
	            	  console.log("属性：" + key + ",值：" + data[key]);
	            	  $("#excelType").append("<option value='"+key+"'>"+data[key]+"</option>");
	            	}
	        },
	        error: function (jqXHR, textStatus, errorThrown) {
	            if (jqXHR.status === 401 || jqXHR.status === 403) {
	                $('#loginErrorModal')
	                    .modal("show")
	                    .find(".modal-body")
	                    .empty()
	                    .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
	            } else {
	                throw new Error("an unexpected error occured: " + errorThrown);
	            }
	        }
	    });
	});
	
    function doUp(formData) {
        $.ajax({
            url: "/upload",
            type: "POST",
            data: formData,
            contentType: "multipart/form-data",
            success: function (data, textStatus, jqXHR) {
                console.log(data);
                if(null != data && data != ""){
                	alert(data);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401 || jqXHR.status === 403) {
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
                } else {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    }
    
    $("#jsonButton").click(function(){
    	var formData = {"id":2,"name":"李四","age":0};
    	var formDataJson = JSON.stringify(formData);
    	$.ajax({
            url: "/transJson",
            type: "POST",
            data: formDataJson,
            contentType: "application/json",
            success: function (data, textStatus, jqXHR) {
                console.log(data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401 || jqXHR.status === 403) {
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Message from server:<br>" + jqXHR.responseText + "</p>");
                } else {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    });
    
});
