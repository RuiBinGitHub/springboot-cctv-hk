$(document).ready(function() {
	// 获取当前语言
	var language = $("#top").text() == "新建項目" ? "zh" : "en";
	
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
	$("#tab1 input[type=text]:eq(0)").attr("placeholder", "Up to 24 bits of input");
	$("#tab1 input[type=radio]:eq(0)").prop("checked", true);
	$("#tab1 input[type=radio]:eq(3)").prop("checked", true);
	$("#tab1 input[type=text]:eq(2)").attr("readonly", true);
	$("#tab1 input[type=text]:eq(2)").focus(function() {
		laydate();  // 
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
		if ($("select[name=operator]").val() == null) {
			$("select[name=operator]").css("background-color", "#f00");
			showTips(tipsText5);
			return false;
		}
		if ($(".textbox:eq(2)").val() == "") {
			$(".textbox:eq(2)").css("background-color", "#f00");
			showTips(tipsText6);
			return false;
		}
		/** 提交数据 */
		$(this).css("background-color", "#ccc");
		$(this).attr("disabled", true);
		$("#form1").submit();
	});
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
