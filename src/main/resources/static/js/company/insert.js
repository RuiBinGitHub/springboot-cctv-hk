$(document).ready(function() {

	if ($("#Tip").text() != "")
		showTips($("#Tip").text())
	$("#table1 input[type=radio]:eq(0)").prop("checked", true);
	/** ************************************************************ */
	/** 提交数据 */
	$(".combtn").click(function() {
		var reg = /^[0-9]*[1-9][0-9]*$/;
		if ($(".textbox1:eq(0)").val() == "") {
			$(".textbox1:eq(0)").css("background-color", "#ff3300");
			showTips("请输入公司名称!");
			return false;
		}
		var define = $(".textbox1:eq(1)").val();
		if (define.length < 2 || define.length > 12) {
			$(".textbox1:eq(1)").css("background-color", "#ff3300");
			showTips("请输入正确的账号前缀!");
			return false;
		}
		var num1 = $(".textbox2:eq(0)").val();
		if (num1 == "" || !reg.test(num1) || num1 < 1 || num1 > 1000) {
			$(".textbox2:eq(0)").css("background-color", "#ff3300");
			showTips("请输入正确的公司规模!");
			return false;
		}
		var num2 = $(".textbox2:eq(1)").val();
		if (num2 == "" || !reg.test(num2) || num1 < 1 || num1 > 1000) {
			$(".textbox2:eq(1)").css("background-color", "#ff3300");
			showTips("请输入正确的使用期限!");
			return false;
		}
		/** ********************************************************* */
		$("#form1").submit();
	});
	if ($(".textbox2:eq(0)").val() == "") {
		$(".textbox2:eq(0)").val(10);
	}
	if ($(".textbox2:eq(1)").val() == "") {
		$(".textbox2:eq(1)").val(10);
	}
	$(".btn1:eq(0)").click(function() {
		var value = $(".textbox2:eq(0)").val();
		if (!isNaN(value) && value > 1) {
			$(".textbox2:eq(0)").css("background-color", "#ffffff");
			$(".textbox2:eq(0)").val(Number(value) - 1);
		}
	});
	$(".btn2:eq(0)").click(function() {
		var value = $(".textbox2:eq(0)").val();
		if (!isNaN(value) && value < 1000) {
			$(".textbox2:eq(0)").css("background-color", "#ffffff");
			$(".textbox2:eq(0)").val(Number(value) + 1);
		}
	});
	$(".btn1:eq(1)").click(function() {
		var value = $(".textbox2:eq(1)").val();
		if (!isNaN(value) && value > 1) {
			$(".textbox2:eq(1)").css("background-color", "#ffffff");
			$(".textbox2:eq(1)").val(Number(value) - 1);
		}
	});
	$(".btn2:eq(1)").click(function() {
		var value = $(".textbox2:eq(1)").val();
		if (!isNaN(value) && value < 1000) {
			$(".textbox2:eq(1)").css("background-color", "#ffffff");
			$(".textbox2:eq(1)").val(Number(value) + 1);
		}
	});
	$(".textbox2").on("input", function() {
		var value = $(this).val();
		if (isNaN(value) || value < 1 || value > 1000)
			$(this).css("background-color", "#ff3300");
		else
			$(this).css("background-color", "#ffffff");
	})
	// 设置输入框只能输入数字
	$(".textbox2").keypress(function(event) {
		if (event.which >= 48 && event.which <= 57)
			return true;
		return false;
	});
	/** 输入框获取焦点事件 */
	$(".textbox1").on("input", function() {
		$(this).css("background-color", "#ffffff");
	});
	/** 显示提示信息 */
	function showTips(contex) {
		$("#Tip").show().delay(1800).hide(200);
		$("#Tip").text(contex);
	}
});