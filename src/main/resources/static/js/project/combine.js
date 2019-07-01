$(document).ready(function() {
	var language = $("#top").text() == "項目合並" ? "zh" : "en"; 
	/********************************************************************/
    initlist(Ajax("combinelist", null));
    $("#menuBtn1").attr("disabled", true);
    $("#menuBtn1").css("background-color", "#CCCCCC");
    $("#menuText").bind("input", function() {
    	if ($(this).val() == "") {
            $("#menuBtn1").css("background-color", "#CCCCCC");
            $("#menuBtn1").attr("disabled", true);
    	} else {
            $("#menuBtn1").css("background-color", "#2AB673");
            $("#menuBtn1").attr("disabled", false);
    	}
    });
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    $("#menuBtn1").click(function() {
        $("#menuText").val("");
        var result = Ajax("combinelist", null);
        initlist(result);

    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "") {
            var result = Ajax("combinelist", {name: name});
            initlist(result);
        }
    });
    /********************************************************************/
    function initlist(data) {
        var context = "";
        for (var i = 0; i < data.length; i++) {
            context += "<tr align='center'>";
            context += "  <td width='50px'><input type='checkbox' value=" + data[i].id + "></td>";
            context += "  <td width='30px'>" + (i + 1) + "</td>";
            context += "  <td width='200px'><a name='" + data[i].id + "'>" + data[i].name + "</a></td>";
            context += "  <td width='240px'>" + data[i].client + "</td>";
            context += "  <td width='120px'>" + data[i].standard + "</td>";
            context += "  <td width='100px'>" + data[i].slope + "</td>";
            context += "  <td width='120px'>" + data[i].personName + "</td>";
            context += "  <td width='120px'>" + data[i].date + "</td>";
            context += "</tr>";
        }
        $("#tab1").html(context);
        /****************************************************************/
        var list = new Array();
        $("#fieldset .text").each(function(i) {
            list.push(Number($(this).attr("name")));
        });
        $("#tab1 tr").each(function(i) {
        	 var id = $(this).find("a").attr("name");
             $(this).find("a").attr("target", "_blank");
             $(this).find("a").attr("href", "/CCTV/project/findinfo?id=" + id);
             /*********************************************/
            if ($("#menuText").val() != "") {
                var name = $("#menuText").val();
                var exp = new RegExp(name,"gm")
                var text = $(this).find("td:eq(2)").text();
            	text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
                $(this).find("td:eq(2) a").html(text);
            }
            /*********************************************/
            if (list.indexOf(Number(id)) != -1)
                $(this).find("input[type=checkbox]").prop("checked", true);
            /*********************************************/
            $(this).find("input[type=checkbox]").click(function() {
                if ($(this).is(":checked") && $("#fieldset .label").length < 20) {
                    var value = $(this).val();
                    var line = $("#tab1 tr").eq(i).find("td:eq(1)").text();
                    var name = $("#tab1 tr").eq(i).find("td:eq(2)").text();
                    var text = "<div class='label'>";
                    text += "  <div class='num'>" + line + "</div>";
                    text += "  <a class='text' name='" + value + "'>" + name + "</a>";
                    text += "  <div class='btn'>X</div>";
                    text += "</div>";
                    $("#fieldset").append(text);
                    initBtn();
                } else {
                    var id = $(this).val();
                    $(".text[name=" + id + "]").parents(".label").remove();
                    $(this).attr("checked", false);
                }
            });
        });
    }
    /********************************************************************/
    function initBtn() {
        $("#fieldset .btn").click(function() {
            var id = $(this).prev().attr("name");
            $(this).parents(".label").remove();
            $("#tab1 input[value=" + id + "]").attr("checked", false);
        });
    }
    /********************************************************************/
    $(".combtn").click(function() {
        var list = new Array();
        $("#fieldset .text").each(function(i) {
            list.push(Number($(this).attr("name")));
        });
        if (list.length < 2) {
            if (language == "zh")
            	showTips("請至少選擇兩個項目！");
			else
				showTips("Please select at least 2 project!");
            return false;
        }
        var tipsText = "確定要合並項目嗎？";
    	var showText = "項目合並成功！";
    	if (language == "en") {
    		tipsText = "Are you sure you want to combine projects?";
    		showText = "Operating successfully!";
    	}
        if (confirm(tipsText)) {
            $(this).css("background-color", "#CCC");
            $(this).attr("disabled", true);
            if (Ajax("combine", {list: list}))
            	showTips(showText);
            setTimeout("location.reload()", 2000);
        }
    });
    /** 显示提示信息 */
    function showTips(text) {
        $("#Tip").show().delay(1800).hide(200);
        $("#Tip").text(text);
    }
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url:url,
            data:data,
            type:"post",
            async:false,
            datatype:"json",
            success:function(data) {
                result = data;
            }
        });
        return result;
    }
});
