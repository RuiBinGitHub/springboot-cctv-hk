$(document).ready(function() {

    var language = $("#infoTop").text() == "項目列表" ? "zh" : "en";

    var tipsText1 = language == "zh" ? "確定要撤回該項目嗎？" : "Are you sure you want to revoke this data?";
    var tipsText2 = language == "zh" ? "數據撤回成功！" : "Operating successfully!";
    var tipsText3 = language == "zh" ? "確定要移除該數據嗎？" : "Are you sure you want to remove this data?";
    var tipsText4 = language == "zh" ? "數據移除成功！" : "Operating successfully!";
    /********************************************************************/
    var width = $("#infoMenu span:eq(0)").css("width");
    var length = width.substring(0, width.length - 2);
    if ($("#infoMenu input[type=button]").length == 4)
        $("#infoMenu div:eq(0)").css("width", 536 - length);
    else
        $("#infoMenu div:eq(0)").css("width", 610 - length);
    /********************************************************************/
    if ($("#menuText").val() == "") {
        $("#menuBtn1").attr("disabled", true);
    }
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    $("#menuBtn1").click(function() {
        window.location.href = "findlist";
    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val().trim();
        if (name.trim() != "")
            window.location.href = "findlist?name=" + name;
    });
    /********************************************************************/
    $("#combin").click(function() {
        window.open("combineview");
    });
    $("#commap").click(function() {
        window.open("/CCTV/imapinfo/showlist");
    });
    /********************************************************************/
    /** 初始化表格 */
    $("#tab1 tbody tr").each(function(i) {
        $(this).find("a").attr("target", "_blank");
        /*********************************************/
        var name = $("#menuText").val();
        if (name.trim() != "") {
            var exp = new RegExp(name,"gm")
            var text = $(this).find("td:eq(1)").text();
            text = text.replace(exp, "<font color='#f00'>" + name + "</font>");
            $(this).find("td:eq(1) a").html(text);
        }
        /*********************************************/
        var id = $(this).attr("id");
        $(this).find("input[type=button]:eq(0)").click(function() {
            window.open("/CCTV/download/file?id=" + id);
        });
        $(this).find("input[type=button]:eq(1)").click(function() {
            if (!confirm(tipsText1))
                return false;
            $(this).css("background-color", "#ccc");
            $(this).attr("disabled", true);
            if (Ajax("revoke", {id: id}))
                showTips(tipsText2);
            setTimeout("location.reload()", 2000);
        });
        $(this).find("input[type=button]:eq(2)").click(function() {
            if (!confirm(tipsText3))
                return false;
            $(this).css("background-color", "#ccc");
            $(this).attr("disabled", true);
            if (Ajax("remove", {id: id}))
                showTips(tipsText4);
            setTimeout("location.reload()", 2000);
        });
        $(this).click(function() {
            $("#tab1 tbody tr:even").find("td:eq(0)").css("background-color", "#FAFAFA");
            $("#tab1 tbody tr:odd").find("td:eq(0)").css("background-color", "#EEEEEE");
            $(this).find("td:eq(0)").css("background-color", "#FFD58D");
        });
    });
    /********************************************************************/
    /** 上一页 */
    $(".pagebtn:eq(0)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) - 1;
        window.location.href = "findlist?name=" + name + "&page=" + page;
    });
    /** 下一页 */
    $(".pagebtn:eq(1)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) + 1;
        window.location.href = "findlist?name=" + name + "&page=" + page;
    });
    /********************************************************************/
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
});
