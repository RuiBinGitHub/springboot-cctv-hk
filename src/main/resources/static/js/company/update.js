$(document).ready(function() {
	
    /** 提交数据 */
    $(".combtn").click(function() {
        var num1 = $(".textbox1:eq(2)").val();
        var reg = /^[0-9]*[1-9][0-9]*$/;
        if (num1 == "" || !reg.test(num1)) {
            $(".textbox1:eq(2)").css("border-color", "#ff3300");
            showTips("格式输入不正确!");
            return false;
        }
        var num2 = $(".textbox1:eq(3)").val();
        if (num2 == "" || !reg.test(num2)) {
            $(".textbox1:eq(3)").css("border-color", "#ff3300");
            showTips("格式输入不正确!");
            return false;
        }
        $("#form1").submit();
    });
    $(".textbox1:eq(2),.textbox1:eq(3)").keypress(function(event) {
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
        $("#Tip").text(contex);
        $("#Tip").show().delay(1800).hide(200);
    }
});
