$(document).ready(function() {

	// 获取当前语言
	var language = $("#top").text() == "更新項目" ? "zh" : "en";

	var tipsText1 = "請輸入項目編號！";
	var tipsText2 = "项目名称不能包含'/'字符！";
	var tipsText3 = "项目名称不能包含'\\'字符！";
	var tipsText4 = "請輸入公司名稱！";
	var tipsText5 = "請選擇操作人員！";
	var tipsText6 = "請輸入調查日期！";
	if (language == "en") {
		tipsText1 = "Please enter the Project No!";
		tipsText2 = "The Project No can't contain the '/' !";
		tipsText3 = "The Project No can't contain the '\\' !";
		tipsText4 = "Please enter the Company Name!";
		tipsText5 = "Please select Operator!";
		tipsText6 = "Please enter the Date!";
	}
	/** ***************************************************************** */
	$("#tab1 input[type=text]:eq(0)").attr("maxlength", 24);
	$("#tab1 input[type=text]:eq(2)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(2)").attr("title", "无法编辑");
	$("#tab1 input[type=text]:eq(2)").css("background-color", "#F0F0F0");
	$("#tab1 input[type=text]:eq(3)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(3)").focus(function() {
		laydate();
	});
	/** ***************************************************************** */
	$(".combtn").click(function() {
		if ($(".textbox:eq(0)").val() == "") {
			$(".textbox:eq(0)").css("background-color", "#f00");
			showTips(tipsText1);
			return false;
		}
		if ($(".textbox:eq(0)").val().indexOf("/") != -1) {
			$(".textbox:eq(0)").css("background-color", "#f00");
			showTips(tipsText2);
			return false;
		}
		if ($(".textbox:eq(0)").val().indexOf("\\") != -1) {
			$(".textbox:eq(0)").css("background-color", "#f00");
			showTips(tipsText3);
			return false;
		}
		if ($(".textbox:eq(1)").val() == "") {
			$(".textbox:eq(1)").css("background-color", "#f00");
			showTips(tipsText4);
			return false;
		}
		if ($("#tab1 select[name=operator]").val() == null) {
			$("select[name=operator]").css("background-color", "#f00");
			showTips(tipsText5);
			return false;
		}
		if ($("input[name=date]").val() == "") {
			$(".textbox:eq(2)").css("background-color", "#f00");
			showTips(tipsText6);
			return false;
		}
		/** 提交数据 */
		$(this).css("background-color", "#ccc");
		$(this).attr("disabled", true);
		$("#form1").submit();
	});

	/** 输入框获取焦点事件 */
	$("#tab1 input[type=text]").focus(function() {
		$(this).css("background-color", "#F2F2F2");
	});
	$("#tab1 select[name=operator]").focus(function() {
		$(this).css("background-color", "#F2F2F2");
	});
	/** ***************************************************************** */
	function showTips(text) {
		$("#Tip").show().delay(1800).hide(200);
		$("#Tip").text(text);
	}

});
