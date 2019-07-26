$(document).ready(function() {
	
	var language = $("#rightTop").text() == "項目列表" ? "zh" : "en";
	/********************************************************************/
	if ($("#menuText").val().trim() == "") {
        $("#menuBtn1").css("background-color", "#CCC");
        $("#menuBtn1").attr("disabled", true);
    }
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    /********************************************************************/
    $("#menuBtn1").click(function() {
        window.location.href = "marklist";
    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "")
            window.location.href = "marklist?name=" + name;
    });
    /********************************************************************/
    $("#tab1 tbody tr").each(function(i) {
        /*************************************************/
        var id = $(this).find("a").attr("name");
        $(this).find("a").attr("target", "_blank");
        $(this).find("a").attr("href", "/CCTV/project/findinfo?id=" + id);
        /*************************************************/
        var name = $("#menuText").val();
        if (name.trim() != "") {
        	var exp = new RegExp(name,"gm")
            var text = $(this).find("td:eq(1)").text();
        	text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
            $(this).find("td:eq(1) a").html(text);
        }
        /*************************************************/
        var score1 = $(this).find("td:eq(5)").text();
        var score2 = $(this).find("td:eq(6)").text();
        $(this).find("td:eq(5)").text(Number(score1).toFixed(2));
        $(this).find("td:eq(6)").text(Number(score2).toFixed(2));
        if (score1 < 95)
            $(this).find("td:eq(5)").css("color", "#FF1000");
        else
            $(this).find("td:eq(5)").css("color", "#479911");
        if (score2 < 85)
            $(this).find("td:eq(6)").css("color", "#FF1000");
        else
            $(this).find("td:eq(6)").css("color", "#479911");
        /*************************************************/
        var date = $(this).find("td:eq(7)").text();
        if (date != null && date.length > 10)
            $(this).find("td:eq(7)").text(date.substring(0, 10));
        /*************************************************/
        var id = $(this).attr("id");
        $(this).find("input:eq(0)").click(function() {
            window.open("editinfo?id=" + id);
        });
        $(this).find("input:eq(1)").click(function() {
        	var tipsText = "確定要刪除該數據嗎？";
        	var showText = "刪除數據成功！";
        	if (language == "en") {
        		tipsText = "Are you sure you want to delete this data?";
        		showText = "Operating successfully!";
        	}
            if (confirm(tipsText)) {
            	$(this).css("background-color", "#CCC");
                $(this).attr("disabled", true);
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
    /********************************************************************/
    /** 上一页 */
    $(".pagebtn:eq(0)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) - 1;
        window.location.href = "marklist?name=" + name + "&page=" + page;
    });
    /** 下一页 */
    $(".pagebtn:eq(1)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) + 1;
        window.location.href = "marklist?name=" + name + "&page=" + page;
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
    /********************************************************************/
    function showTips(text) {
        $("#Tip").show().delay(1800).hide(200);
        $("#Tip").text(text);
    }
    /** 执行AJAX操作 */
    function Ajax(url, data) {
        var result = null;
        $.ajax({
            url:url,
            data:data,
            type:"post",
            async:false,
            datatype:"json",
            success: function(data) {
                result = data;
            }
        });
        return result;
    }
    
});
