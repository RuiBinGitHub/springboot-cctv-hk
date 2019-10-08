$(document).ready(function() {

    // 获取当前语言
    var language = $("#top").text() == "编辑人员" ? "zh" : "en";
    /********************************************************************/
    var tipsText1 = "請輸入人員全名！";
    var tipsText2 = "人員名稱已經存在！";
    var tipsText3 = "請輸入人員姓氏！";
    var tipsText4 = "請輸入人員名字！";
    var tipsText5 = "請輸入會員等級！";
    var tipsText6 = "請輸入會員編號！";
    if (language == "zh") {
        $(".textbox:eq(0)").attr("placeholder", "人員名稱，2-12位");
        $(".textbox:eq(1)").attr("placeholder", "人員姓氏，1-6位");
        $(".textbox:eq(2)").attr("placeholder", "人員名字，1-6位");
        $(".textbox:eq(5)").attr("placeholder", "操作人員認證等級");
        $(".textbox:eq(6)").attr("placeholder", "操作人員認證編號");
    } else {
        $(".textbox:eq(0)").attr("placeholder", "Operator Full Name,2-12 place");
        $(".textbox:eq(1)").attr("placeholder", "Operator Last Name,1-6 place");
        $(".textbox:eq(2)").attr("placeholder", "Operator First Name,1-6 place");
        tipsText1 = "Please enter the Full Name!";  
    	tipsText2 = "The Full Name already exists!"; 
    	tipsText3 = "Please enter the Last Name!";  
    	tipsText4 = "Please enter the First Name!";  
    	tipsText5 = "Please enter the Member Level!";  
    	tipsText6 = "Please enter the Member Number!";  
    }
    /** ***************************************************************** */
    $(".combtn").click(function() {
        if ($(".textbox:eq(0)").val() == "") {
            $(".textbox:eq(0)").css("background-color", "#f00");
            showTips(tipsText1);
            return false;
        }
        var data = {id: $("input[name=id]").val(), name: $(".textbox:eq(0)").val()};
        if (Ajax("isexistname", data)) {
            $(".textbox:eq(0)").css("background-color", "#f00");
            showTips(tipsText2);
            return false;
        }
        if ($(".textbox:eq(1)").val() == "") {
            $(".textbox:eq(1)").css("background-color", "#f00");
            showTips(tipsText3);
            return false;
        }
        if ($(".textbox:eq(2)").val() == "") {
            $(".textbox:eq(2)").css("background-color", "#f00");
            showTips(tipsText4);
            return false;
        }
        if ($(".textbox:eq(5)").val() == "") {
            $(".textbox:eq(5)").css("background-color", "#f00");
            showTips(tipsText5);
            return false;
        }
        if ($(".textbox:eq(6)").val() == "") {
            $(".textbox:eq(6)").css("background-color", "#f00");
            showTips(tipsText5);
            return false;
        }
        /** 提交数据 */
        $(this).css("background-color", "#ccc");
        $(this).attr("disabled", true);
        $("#form1").submit();
    });
    /** 输入框获取焦点事件 */
    $("#tab1 input[type=text]").on("input", function() {
		$(this).css("background-color", "#FFFFFF");
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
