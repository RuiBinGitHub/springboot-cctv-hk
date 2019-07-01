$(document).ready(function() {
	
	$("#table2 .textbox1:eq(2)").attr("type", "password");
	/***************************************************************/
	/** 提交数据 */
	$(".combtn").click(function() {
		var name = $(".textbox1:eq(0)").val();
		var num1 = $(".textbox1:eq(1)").val();
		var num2 = $(".textbox1:eq(2)").val();
		var reg = /^[0-9]*[1-9][0-9]*$/;
		if (name == "") {
			$(".textbox1:eq(0)").css("border-color", "#ff3300");
			showTips("请输入公司名称!");
			return false;
		}
		if (num1 == "" || !reg.test(num1)) {
			$(".textbox1:eq(1)").css("border-color", "#ff3300");
			showTips("请输入公正确的公司规模!");
			return false;
		}
		if (num2 == "" || !reg.test(num2)) {
			$(".textbox1:eq(2)").css("border-color", "#ff3300");
			showTips("请输入公正确的使用期限!");
			return false;
		}
		/** ********************************************************* */
		var name = $("#table2 .textbox1:eq(0)").val();
		var username = $("#table2 .textbox1:eq(1)").val();
		var password = $("#table2 .textbox1:eq(2)").val();
		if (name.length < 1 || name.length > 9) {
			$("#table2 .textbox1:eq(0)").css("border-color", "#ff3300");
			showTips("用户名称格式不正确!");
			return false;
		}
		if (username.length < 6 || username.length > 16) {
			$("#table2 .textbox1:eq(1)").css("border-color", "#ff3300");
			showTips("登录账号格式不正确!");
			return false;
		}
		if (Ajax("/CCTV/user/isexistname", {name : username})) {
			$("#table2 .textbox1:eq(1)").css("border-color", "#ff3300");
			showTips("账号已被使用，请重新输入");
			return false;
		}
		if (password.length < 6 || password.length > 16) {
			$("#table2 .textbox1:eq(2)").css("border-color", "#ff3300");
			showTips("登录密码格式不正确!");
			return false;
		}
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
	/** 执行AJAX操作 */
	function Ajax(url, data) {
		var result = null;
		$.ajax({
			url:url,
			data:data,
			type:"post",
			async:false,
			datatype:"json",
			success:function(data) {
				result = data;
			}  
		});
		return result;
	}
});