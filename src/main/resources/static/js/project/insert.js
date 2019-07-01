$(document).ready(function() {
	// 获取当前语言
	var language = $("#top").text() == "新建項目" ? "zh" : "en";
	/** ***************************************************************** */
	$("#tab1 input[type=text]:eq(0)").attr("maxlength", 24);
	$("#tab1 input[type=radio]:eq(0)").prop("checked", true);
	$("#tab1 input[type=radio]:eq(3)").prop("checked", true);
	$("#tab1 input[type=text]:eq(2)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(2)").focus(function() {
		laydate();
	});
	/** ***************************************************************** */
	var myDate = new Date();
	var y = myDate.getFullYear();
	var m = myDate.getMonth() + 1;
	var d = myDate.getDate();
	m = m < 10 ? "0" + m : m;
	d = d < 10 ? "0" + d : d;
	var text = y + "-" + m + "-" + d;
	$("#tab1 input[type=text]:eq(2)").val(text);
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
		if ($(".textbox:eq(1)").val() == "") {
			$(".textbox:eq(1)").css("border-color", "#f00");
			if (language == "zh")
				showTips("請輸入公司名稱！");
			else
				showTips("Please enter the Company Name!");
			return false;
		}
		if ($("select[name=operator]").val() == null) {
			$("select[name=operator]").css("border-color", "#f00");
			if (language == "zh")
				showTips("請選擇操作人員！");
			else
				showTips("Please select Operator!");
			return false;
		}
		if ($(".textbox:eq(2)").val() == "") {
			$(".textbox:eq(2)").css("border-color", "#f00");
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
	$("#tab1 input[type=text]").focus(function() {
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
