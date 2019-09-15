$(document).ready(function() {
    // 获取当前语言
    var language = $(".title:eq(0) span").text() == "項目範圍" ? "zh" : "en";
    var tipsTest1 = "数据上传中...";
    var tipsTest2 = "數據導入成功！";
    var tipsTest3 = "數據上傳異常！";
    var tipsTest4 = "请输入正确的项目范围！";
    var tipsTest5 = "数据保存成功！";
    var tipsTest6 = "请输入正确的管道坐标！";
    var tipsTest7 = "数据保存成功！";
    var tipsTest8 = "数据保存成功！";
    if (language == "en") {
    	tipsTest1 = "Data uploading...";      
    	tipsTest2 = "Operating successfully!";       
    	tipsTest3 = "Operating exceptioning!";       
    	tipsTest4 = "Please check the input data!";   
    	tipsTest5 = "Operating successfully!";       
    	tipsTest6 = "Please check the input data!";   
    	tipsTest7 = "Operating successfully!";       
    	tipsTest8 = "Operating successfully!";       
    }
    /********************************************************************/
    var id = $("input[name=id]").val();
    $(".title:eq(0) input").css("background-color", "#D13F43");
    $(".title:eq(1) input").css("background-color", "#2AB673");
    // 表头按钮点击事件
    $(".title:eq(0) input[type=button]").click(function() {
    	$("#scope input[type=text]:last").remove();
    });
    $(".title:eq(1) input[type=button]").click(function() {
        $("#file").click();
    });
    $("#file").change(function() {
        if (this.files.length == 0)
            return false;
        var formFile = new FormData();
        formFile.append("id", $("input[name=id]").val());
        formFile.append("file", this.files[0]);
        $("#Tip").text(tipsTest1);
        $("#Tip").show();
        $(".title:eq(1) input").css("background-color", "#ccc");
        $(".title:eq(1) input").attr("disabled", true);
        if (FileAjax("importvalue", formFile))
            showTips(tipsTest2);
        else
            showTips(tipsTest3);
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    initTextBox();
    function initTextBox() {
        var place = "000000.000 000000.000";
        $("#scope input[type=text]").attr("name", "extent");
        $("#scope input[type=text]").attr("placeholder", place);
        $("#scope input[type=text]").bind("input", function() {
        	$(this).css("background-color", "#F3F3F3")
        });
    }
    $("#append").click(function() {
        $(this).before("<input type='text' name='extent'/>");
        initTextBox();
    });
    // 项目范围输入
    $("#commit").click(function() {
        var length = $("#scope input[type=text]").length;
        if (length == 1 || length == 2) {
            showTips(tipsTest4);
            return false;
        }
        for (var i = 0; i < $("#scope input[type=text]").length; i++) {
			var list = $("#scope input[type=text]").eq(i).val().split(" ");
			if (list.length == 2 && !isNaN(list[0]) && !isNaN(list[1]))
				continue;
			$("#scope input[type=text]").eq(i).css("background-color", "#f00");
			showTips(tipsTest4);
			return false;
        }
        var value = $("#scope input[name=extent]:eq(0)").val();
        $("#scope").append("<input type='hidden' name='extent' value='" + value + "'/>");
        $(this).attr("disabled", true);
        $(this).css("background-color", "#ccc");
        if (Ajax("inputextents", $("#form1").serialize()))
            showTips(tipsTest5);
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    $("#tab1 tr input[type=button]").each(function(index) {
        $(this).click(function() {
            /** 显示坐标值 */
            $("#form2 input[type=hidden]").val($(this).attr("id"));
            var parent = $(this).parents("tr");
            $(".mhTitle:eq(0)").text("Start MH：" + parent.find("td:eq(2)").text());
            $("#ftable1 input[type=text]:eq(0)").val(parent.find("td:eq(3)").text());
            $("#ftable1 input[type=text]:eq(1)").val(parent.find("td:eq(4)").text());
            $("#ftable1 input[type=text]:eq(2)").val(parent.find("td:eq(5)").text());

            $(".mhTitle:eq(1)").text("Finish MH：" + parent.find("td:eq(6)").text());
            $("#ftable2 input[type=text]:eq(0)").val(parent.find("td:eq(7)").text());
            $("#ftable2 input[type=text]:eq(1)").val(parent.find("td:eq(8)").text());
            $("#ftable2 input[type=text]:eq(2)").val(parent.find("td:eq(9)").text());

            $("#form3 input[type=hidden]").val($(this).attr("id"));
            $("#form3 input[type=text]:eq(0)").val($(this).parents("tr").find("td:eq(11)").text());
            $("#form3 input[type=text]:eq(1)").val($(this).parents("tr").find("td:eq(12)").text());
            $("#form3 input[type=text]:eq(2)").val($(this).parents("tr").find("td:eq(13)").text());
            $("#form3 input[type=text]:eq(3)").val($(this).parents("tr").find("td:eq(14)").text());
            /** 设置样式 */
            $("#tab1 tr").find("td:eq(0)").text("");
            parent.find("td:eq(0)").text("▶");
        });
    });
    $("#tab1 tr input[type=button]").eq(0).click();
    /********************************************************************/
    /** 设置管道坐标输入框只能输入数字 */
    $("#ftable1 input[type=text]").bind("input", function() {
        if ($(this).val() == "" || isNaN($(this).val()))
            $(this).css("background-color", "#FF0000");
        else
            $(this).css("background-color", "#F3F3F3");
    });
    $("#ftable2 input[type=text]").bind("input", function() {
        if ($(this).val() == "" || isNaN($(this).val()))
            $(this).css("background-color", "#FF0000");
        else
            $(this).css("background-color", "#F3F3F3");
    });
    $("#main3 input[type=text]").keypress(function(event) {
        if (event.which >= 48 && event.which <= 57 || event.which == 46)
            return true;
        return false;
    });
    /********************************************************************/
    /** 提交数据 */
    $("#common").click(function() {
    	var list = $("#ftable1 input, #ftable2 input");
        for (var i = 0; i < list.length; i++) {
			if (!isNaN(list.eq(i).val()))
				continue;
			showTips(tipsTest6);
			return false;
        }
        $(this).attr("disabled", true);
        $(this).css("background-color", "#ccc");
        if (Ajax("inputvalue", $("#form2").serialize()))
            showTips(tipsTest7);
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    $("#editlink").click(function() {
        $("#page").show();
    });
    $("#bar span").click(function() {
        $("#page").hide();
    });
    $("#webform input[type=button]").click(function() {
    	$(this).attr("disabled", true);
        $(this).css("background-color", "#ccc");
        if (Ajax("updategrade", $("#form3").serialize()))
        	showTips(tipsTest8);
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    /** 显示提示信息 */
    function showTips(text) {
        $("#Tip").show().delay(1800).hide(200);
        $("#Tip").text(text);
    }
    /** 执行AJAX操作 */
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url: url,
            data: data,
            type: "post",
            async: false,
            datatype: "json",
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    /** Ajax上传文件 */
    function FileAjax(url, data) {
        var result = null;
        $.ajax({
            url: url,
            data: data,
            type: "post",
            async: false,
            datatype: "json",
            processData: false,
            contentType: false,
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
});
