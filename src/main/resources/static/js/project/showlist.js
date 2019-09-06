$(document).ready(function() {
    // 获取当前语言
    var language = $("#infoTop").text() == "項目列表" ? "zh" : "en";
    var tipsText1 = "數據正在上傳中...";
    var tipsText2 = "確定要提交該數據嗎？";
    var tipsText3 = "數據提交成功！";
    var tipsText4 = "確定要刪除該數據嗎？";
    var tipsText5 = "數據刪除成功！";
    if (language == "en") {
    	tipsText1 = "Data uploading...";
    	tipsText2 = "Are you sure you want to submit this data?";
    	tipsText3 = "Operating successfully!";
    	tipsText4 = "Are you sure you want to delete this data?";
    	tipsText5 = "Operating successfully!";
    }
    /********************************************************************/
    var width = $("#infoMenu span:eq(0)").css("width");
    var length = width.substring(0, width.length - 2);
    $("#infoMenu div:eq(0)").css("width", 536 - length);
    /********************************************************************/
    if ($("#menuText").val().trim() == "") {
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
    /** 新建项目 */
    $("#append").click(function() {
        window.open("insertview");
    });
    /** 导入按钮 */
    $("#import").click(function() {
        $("#file1").click();
    });
    $("#file1").attr("accept", ".mdb");
    $("#file1").change(function() {
        if (this.files.length == 0)
            return false;
        $("#Tip").text(tipsText1);
        $("#form1").submit();
        $("#page").show();
        $("#Tip").show();
        this.value = "";
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
        var id = $(this).attr("id");
        $(this).find("input[type=button]:eq(0)").click(function() {
            window.open("updateview?id=" + id);
        });
        $(this).find("input[type=button]:eq(1)").click(function() {
            if (confirm(tipsText2)) {
                $(this).css("background-color", "#ccc");
                $(this).attr("disabled", true);
                if (Ajax("submit", {id: id}))
                    showTips(tipsText3);
                setTimeout("location.reload()", 2000);
            }
        });
        $(this).find("input[type=button]:eq(2)").click(function() {
            if (confirm(tipsText4)) {
                $(this).css("background-color", "#ccc");
                $(this).attr("disabled", true);
                if (Ajax("delete", {id: id}))
                    showTips(tipsText5);
                setTimeout("location.reload()", 2000);
            }
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
        window.location.href = "showlist?name=" + name + "&page=" + page;
    });
    /** 下一页 */
    $(".pagebtn:eq(1)").click(function() {
        var name = $("#menuText").val().trim();
        var page = Number($("#page1").text()) + 1;
        window.location.href = "showlist?name=" + name + "&page=" + page;
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
    /********************************************************************/
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
