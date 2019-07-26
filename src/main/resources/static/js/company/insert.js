$(document).ready(function() {

	if ($("#Tip").text() != "")
		showTips($("#Tip").text())
	$("#table1 input[type=radio]:eq(0)").prop("checked", true);
	/** ************************************************************ */
	/** 提交数据 */
	$(".combtn").click(function() {
		var name = $(".textbox1:eq(0)").val();
		var reg = /^[0-9]*[1-9][0-9]*$/;
		if (name == "") {
			$(".textbox1:eq(0)").css("border-color", "#ff3300");
			showTips("请输入公司名称!");
			return false;
		}
		var num1 = $(".textbox1:eq(1)").val();
		if (num1 == "" || !reg.test(num1) || num1 < 1 || num1 > 1000) {
			$(".textbox1:eq(1)").css("border-color", "#ff3300");
			showTips("请输入正确的公司规模!");
			return false;
		}
		var num2 = $(".textbox1:eq(2)").val();
		if (num2 == "" || !reg.test(num2) || num1 < 1 || num1 > 9999) {
			$(".textbox1:eq(2)").css("border-color", "#ff3300");
			showTips("请输入正确的使用期限!");
			return false;
		}
		var define = $(".textbox1:eq(3)").val();
		if (name.length < 2 || name.length > 12) {
			$(".textbox1:eq(3)").css("border-color", "#ff3300");
			showTips("请输入正确的账号前缀!");
			return false;
		}
		/** ********************************************************* */
		$("#form1").submit();
	});

	// 设置输入框只能输入数字
	$(".textbox1:eq(1),.textbox1:eq(2)").keypress(function(event) {
		if (event.which >= 48 && event.which <= 57)
			return true;
		return false;
	});
	/** 输入框获取焦点事件 */
	$(".textbox1").each(function() {
		$(this).focus(function() {
			$(this).css("border-color", "#999");
		});
	});
	/** 显示提示信息 */
	function showTips(contex) {
		$("#Tip").show().delay(1800).hide(200);
		$("#Tip").text(contex);
	}
});