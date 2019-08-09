$(document).ready(function() {
	// 获取当前语言
	var language = $("#top").text() == "更新項目" ? "zh" : "en";
	/** ***************************************************************** */
	$("#tab1 input[type=text]:eq(0)").attr("maxlength", 24);
	$("#tab1 input[type=text]:eq(2)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(2)").css("background-color", "#F0F0F0");
	$("#tab1 input[type=text]:eq(3)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(3)").focus(function() {
		laydate();
	});
	/** ***************************************************************** */
	$(".combtn").click(function() {
		if ($(".textbox:eq(0)").val() == "") {
			$(".textbox:eq(0)").css("border-color", "#f00");
			if (language == "zh")
				showTips("請輸入項目編號！");
			else
				showTips("Please enter the Project No!");
			return false;
		}
		if ($(".textbox:eq(0)").val().indexOf("/") != -1) {
			if (language == "zh")
				showTips("项目名称不能包含'/'字符！");
			else
				showTips("The project name cannot contain the '/' !");
			return false;
		}
		if ($(".textbox:eq(0)").val().indexOf("\\") != -1) {
			if (language == "zh")
				showTips("项目名称不能包含'\\'字符！");
			else
				showTips("The project name cannot contain the '\\' !");
			return false;
		}
		if ($(".textbox:eq(1)").val() == "") {
			$(".textbox:eq(1)").css("border-color", "#f00");
			if (language == "zh")
				showTips("請輸入公司名稱！");
			else
				showTips("Please enter the Company Name!");
			return false;
		}
		if ($("#tab1 select[name=operator]").val() == null) {
			$("#tab1 select[name=operator]").css("border-color", "#f00");
			if (language == "zh")
				showTips("請選擇操作人員！");
			else
				showTips("Please select Operator!");
			return false;
		}
		if ($("input[name=date]").val() == "") {
			$("input[name=date]").css("border-color", "#f00");
			if (language == "zh")
				showTips("請輸入調查日期！");
			else
				showTips("Please enter the Survey Date!");
			return false;
		}
		/** 提交数据 */
		$(this).css("background-color", "#ccc");
		$(this).attr("disabled", true);
		$("#form1").submit();
	});

	/** 输入框获取焦点事件 */
	$("#tab1 .textbox").focus(function() {
		$(this).css("border-color", "#999");
	});
	$("#tab1 select[name=operator]").focus(function() {
		$(this).css("border-color", "#999");
	});
	/** ***************************************************************** */
	function showTips(text) {
		$("#Tip").show().delay(1800).hide(200);
		$("#Tip").text(text);
	}

});
