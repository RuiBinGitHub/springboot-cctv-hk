$(document).ready(function() {
	
	var language = $("#rightTop").text() == "項目列表" ? "zh" : "en";
	/********************************************************************/
    if ($("#menuText").val() == "") {
        $("#menuBtn1").css("background-color", "#CCC");
        $("#menuBtn1").attr("disabled", true);
    }
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    /********************************************************************/
    $("#menuBtn1").click(function() {
        window.location.href = "findlist";
    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "")
            window.location.href = "findlist?name=" + name;
    });
    /********************************************************************/
    $("#tab1 tbody tr").each(function(i) {
        $(this).find("a").attr("target", "_blank");
        /*************************************************/
        var name = $("#menuText").val();
        if (name.trim() != "") {
        	var exp = new RegExp(name,"gm")
            var text = $(this).find("td:eq(1)").text();
        	text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
            $(this).find("td:eq(1) a").html(text);
        }
        /*************************************************/
        var score1 = $(this).find("td:eq(4)").text();
        var score2 = $(this).find("td:eq(5)").text();
        $(this).find("td:eq(4)").text(Number(score1).toFixed(2));
        $(this).find("td:eq(5)").text(Number(score2).toFixed(2));
        if (score1 < 95)
            $(this).find("td:eq(4)").css("color", "#FF1000");
        else
            $(this).find("td:eq(4)").css("color", "#479911");
        if (score2 < 85)
            $(this).find("td:eq(5)").css("color", "#FF1000");
        else
            $(this).find("td:eq(5)").css("color", "#479911");
        /*************************************************/
        $(this).find("td:eq(4)").attr("title", "及格分數：95.00");
        $(this).find("td:eq(5)").attr("title", "及格分數：85.00");
        /*************************************************/
        $(this).find("input:eq(0)").click(function() {
        	var id = $(this).attr("name");
        	window.open("findinfo?id=" + id);
        });
        $(this).find("input:eq(1)").click(function() {
        	var tipsText = "確定要移除該數據嗎？";
        	var showText = "移除數據成功！";
        	if (language == "en") {
        		tipsText = "Are you sure you want to remove this data?";
        		showText = "Operating successfully!";
        	}
            if (confirm(tipsText)) {
            	$(this).css("background-color", "#CCC");
                $(this).attr("disabled", true);
                var id = $(this).attr("name");
                if (Ajax("remove", {id: id}))
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
