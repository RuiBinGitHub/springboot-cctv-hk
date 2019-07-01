$(document).ready(function() {
	
	// 获取当前语言
	var language = $("#rightTop").text() == "用戶列表" ? "zh" : "en";
	/********************************************************************/
	if ($("#menuText").val().trim() == "") {
        $("#menuBtn1").css("background-color", "#CCC");
        $("#menuBtn1").attr("disabled", true);
    }
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    $("#menuBtn1").click(function() {
        window.location.href = "showlist";
    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "")
            window.location.href = "showlist?name=" + name;
    });
    /********************************************************************/
    /** 初始化表格 */
    $("#tab1 tbody tr").each(function(i) {
        $(this).find("td:eq(1) a").attr("target", "_blank");
        /*********************************************/
        var name = $("#menuText").val();
        if (name.trim() != "") {
        	var exp = new RegExp(name,"gm")
            var text = $(this).find("td:eq(1)").text();
        	text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
            $(this).find("td:eq(1) a").html(text);
        }
        /*********************************************/
        var pass = $(this).find("td:eq(3)").text();
        var cont = getrRepeats("", pass.length - 4);
        var text = pass.replace(/(.{2}).*(.{2})/, cont);
    	$(this).find("td:eq(3)").text(text);
        /*********************************************/
    	var mail = $(this).find("td:eq(4)").text();
        var cont = getrRepeats("", mail.length - 13);
        var text = mail.replace(/(.{3}).*(.{10})/, cont);
    	$(this).find("td:eq(4)").text(text);
    	/*********************************************/
        var role = $(this).find("td:eq(6)").text();
        if (role == "Role2")
            $(this).find("td:eq(6)").text("管理人員");
        else if (role == "Role3")
            $(this).find("td:eq(6)").text("評分人員");
        else if (role == "Role4")
            $(this).find("td:eq(6)").text("操作人員");
        /*********************************************/
        $(this).find(".tablebtn1").click(function() {
            var id = $(this).attr("name");
            window.open("updateview?id=" + id);
        });
        $(this).find(".tablebtn2").click(function() {
        	var tipsText = "確定要刪除該數據嗎？";
        	var showText = "刪除數據成功！";
        	if (language == "en") {
        		tipsText = "Are you sure you want to delete this data?";
        		showText = "Operating successfully!";
        	}
            if (confirm(tipsText)) {
            	$(this).css("background-color", "#CCC");
                $(this).attr("disabled", true);
                var id = $(this).attr("name");
                if (Ajax("delete", {id: id}))
                	showTips(showText);
                setTimeout("location.reload()", 2000);
            }
        });
        $(this).click(function() {
            $("#tab1 tbody tr:even").find("td:eq(0)").css("background-color", "#FFFFFF");
            $("#tab1 tbody tr:odd").find("td:eq(0)").css("background-color", "#FAFAFA");
            $(this).find("td:eq(0)").css("background-color", "#FFD58D");
        });
    });
    function getrRepeats(str, length){
    	str += "$1";
        for(var i=0; i<length; ++i)
            str += "*";
        str += "$2";
        return str;
    }
    /********************************************************************/
    /** 上一页 */
    $(".pagebtn:eq(0)").click(function() {
        var name = $("#menuText").val();
        var page = Number($("#page1").text()) - 1;
        window.location.href = "showlist?name=" + name + "&page=" + page;
    });
    /** 下一页 */
    $(".pagebtn:eq(1)").click(function() {
        var name = $("#menuText").val();
        var page = Number($("#page1").text()) + 1;
        window.location.href = "showlist?name=" + name + "&page=" + page;
    });
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
    /********************************************************************/
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
