$(document).ready(function() {

    $("#pass").attr("type", "password");
    $("#name").attr("placeholder", "Please enter your username");
    $("#pass").attr("placeholder", "Please enter your password");
    /********************************************************************/
    $("#btn").click(function() {
        var name = $("#name").val(); //获取输入账号
        var pass = $("#pass").val(); //获取输入密码
        if (name == "" || pass == "")
        	$("#tips").text("*Please enter your username or password!");
        else {
            $(this).css("background-color", "#ccc");
            $(this).attr("disable", true);
            $(this).val("Sign In...");
            $("#form1").submit();
        }
    });

    /** 输入框获取焦点事件 */
    $("#name,#pass").click(function() {
        $("#tips").text("");
    });
    /** 键盘按下事件 */
    $("#name,#pass").keydown(function() {
        if (event.keyCode == 13)
        	$("#btn").click();
    });
});