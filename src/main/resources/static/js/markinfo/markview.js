$(document).ready(function() {
	
	var language = $("#rightTop").text() == "項目列表" ? "zh" : "en";
	/********************************************************************/
    if ($("#menuText").val() == "") {
        $("#menuBtn1").attr("disabled", true);
        $("#menuBtn1").css("background-color", "#CCC");
    }
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    $("#menuBtn1").click(function() {
        window.location.href = "markview";
    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "")
            window.location.href = "markview?name=" + name;
    });
    /********************************************************************/
    $("#tab1 tbody tr").each(function(i) {
    	var name = $("#menuText").val();
        if (name.trim() != "") {
        	var exp = new RegExp(name,"gm")
            var text = $(this).find("td:eq(1)").text();
        	text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
            $(this).find("td:eq(1) a").html(text);
        }
        /*************************************************/
        var btnText = language == "zh" ? "編輯" : "Edit";
        /*************************************************/
        $(this).find("td:eq(6)").mouseenter(function(e) {
        	var id = $(this).parents("tr").attr("id");
            $(this).css("background-color", "#FFE793");
            if ($(this).text() == 0) 
            	return false;
            var context = "";
            var data = Ajax("showlist", {id: id});
            for (var i = 0; i < data.length; i++) {
                context += "<tr>";
                context += "  <td width='20%'>" + Number(data[i].score1).toFixed(2) + "</td>";
                context += "  <td width='20%'>" + Number(data[i].score2).toFixed(2) + "</td>";
                context += "  <td width='40%'>" + data[i].date.substring(0, 10) + "</td>";
                context += "  <td width='20%'><input type='button' name='" + data[i].id + "'/></td>";
                context += "</tr>";
            }
            $("#show table tbody").html(context);
            $("#show").css("top", $(this).position().top);
            $("#show").css("left", $(this).position().left + 96);
            $("#show table tbody tr").each(function() {
                $(this).find("input").attr("value", btnText);
                $(this).find("input").click(function() {
                    var pid = $(this).attr("name");
                    window.open("editinfo?id=" + pid);
                });
            });
            $("#show").show();
        });
        $(this).find("td:eq(6)").mouseleave(function(e) {
            $(this).css("background-color", "#FFFFFF");
            $("#show").hide();
        });
        /*************************************************/
        $(this).find("input:eq(0)").click(function() {
        	var id = $(this).parents("tr").attr("id");
            window.open("/CCTV/project/findinfo?id=" + id);
        });
        $(this).find("input:eq(1)").click(function() {
        	var id = $(this).parents("tr").attr("id");
            window.open("insert?id=" + id);
        });
        $(this).click(function() {
            $("#tab1 tbody tr:even").find("td:eq(0)").css("background-color", "#FFFFFF");
            $("#tab1 tbody tr:odd").find("td:eq(0)").css("background-color", "#FAFAFA");
            $(this).find("td:eq(0)").css("background-color", "#FFD58D");
        });
    });
    $("#show").mouseenter(function(e) {
        $(this).show();
    });
    $("#show").mouseleave(function(e) {
        $(this).hide();
    });
    /********************************************************************/
    /** 上一页 */
    $(".pagebtn:eq(0)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) - 1;
        window.location.href = "markview?name=" + name + "&page=" + page;
    });
    /** 下一页 */
    $(".pagebtn:eq(1)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) + 1;
        window.location.href = "markview?name=" + name + "&page=" + page;
    });
    /********************************************************************/
    $(".pagebtn:eq(0)").attr("disabled", false);
    $(".pagebtn:eq(1)").attr("disabled", false);
    var page1 = $("#page1").text();
    var page2 = $("#page2").text();
    if (page1 <= 1) {
        $(".pagebtn:eq(0)").attr("disabled", true);
        $(".pagebtn:eq(0)").css("color", "#999");
    }
    if (page1 == page2) {
        $(".pagebtn:eq(1)").attr("disabled", true);
        $(".pagebtn:eq(1)").css("color", "#999");
    }
    /*******************************************************************/
    /** 执行AJAX操作 */
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