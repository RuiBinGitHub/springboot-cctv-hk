$(document).ready(function() {

    var language = $("#top").text() == "項目合並" ? "zh" : "en";
    var tipsText1 = language == "zh" ? "請至少選擇兩個項目！" : "Please select at least 2 project!";
    var tipsText2 = language == "zh" ? "確定要合並項目嗎？" : "Are you sure you want to combine projects?";
    var tipsText3 = language == "zh" ? "項目合並成功！" : "Operating successfully!";
    /********************************************************************/
    $("#menuBtn1").attr("disabled", true);
    $("#menuText").on("input", function() {
        if ($(this).val() == "")
            $("#menuBtn1").attr("disabled", true);
        else
            $("#menuBtn1").attr("disabled", false);
    });
    $("#menuText").keydown(function() {
        if (event.keyCode == 13)
            $("#menuBtn2").click();
    });
    $("#menuBtn1").click(function() {
        $("#menuText").val("");
        initlist(Ajax("combinelist", null));

    });
    $("#menuBtn2").click(function() {
        var name = $("#menuText").val();
        if (name.trim() != "")
            initlist(Ajax("combinelist", {name: name}));
    });
    /********************************************************************/
    initlist(Ajax("combinelist", null));
    function initlist(data) {
        var context = "";
        for (var i = 0; i < data.length; i++) {
            context += "<tr align='center'>";
            context += "  <td width='50px'><input type='checkbox' value=" + data[i].id + "></td>";
            context += "  <td width='30px'>" + (i + 1) + "</td>";
            context += "  <td width='200px'><a>" + data[i].name + "</a></td>";
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
        $("#fieldset .label").each(function(i) {
            list.push(Number($(this).attr("id")));
        });
        $("#tab1 tr").each(function(i) {
            var id = $(this).find("input").attr("value");
            $(this).find("a").attr("target", "_blank");
            $(this).find("a").attr("href", "findinfo?id=" + id);
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
        });
    }
    // 设置复选框点击事件
    $("#tab1").on("click", "input[type=checkbox]", function() {
    	if ($(this).is(":checked") && $("#fieldset .label").length < 20) {
            var value = $(this).val();
            var name = $(this).parents("tr").find("td:eq(2)").text();
            var text = "<div id='" + value + "' class='label'>"
            text += name + "<div class='delete'>x</div>";
            text += "</div>";
            $("#fieldset").append(text);
    	} else
    		$(".label[id=" + $(this).val() + "]").remove();
    });
    $("#fieldset").on("mouseenter", ".label", function() {
    	$(this).find("div").show();
    });
    $("#fieldset").on("mouseleave", ".label", function() {
    	$(this).find("div").hide();
    });
    $("#fieldset").on("click", ".label div", function() {
    	var id = $(this).parents(".label").attr("id");
    	$("#tab1 input[value=" + id + "]").attr("checked", false);
    	$(this).parent().remove();
    });
    /********************************************************************/
    $(".combtn").click(function() {
        var list = new Array();
        $("#fieldset .label").each(function(i) {
            list.push(Number($(this).attr("id")));
        });
        if (list.length < 2) {
            showTips(tipsText1);
            return false;
        }
        if (!confirm(tipsText2))
            return false;
        $(this).attr("disabled", true);
        $(this).css("background-color", "#CCC");
        if (Ajax("combine", {list: list}))
            showTips(tipsText3);
        setTimeout("location.reload()", 2000);
    });
    /** 显示提示信息 */
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
