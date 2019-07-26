$(document).ready(function() {

	// 获取当前语言
	var language = $("#rightTop").text() == "信息列表" ? "zh" : "en";
	/** ********************************************** */
	$("#tab1 tr").each(function(i) {
		var id = $(this).attr("id");
		$(this).find("a").click(function() {
			var data = Ajax("findinfo", {id : id});
			if (data == null)
				return null;
			$("#messageInfo1 span:eq(0)").text(data.title);
			$("#messageInfo1 span:eq(1)").text(data.senderName);
			$("#messageInfo1 span:eq(2)").text(data.acceptName);
			$("#messageInfo1 span:eq(3)").text(data.date);
			$("#messageInfo2").html(data.text);
			$("#messageInfo2 a").attr("target", "_blank");
			$("#page").show();
			/** ****************************************** */
			if (language == "zh")
				html = "<font color='#2AB673'>已读</font>";
			else
				html = "<font color='#2AB673'>Already</font>";
			$("#tab1 tr").eq(i).find("td:eq(3)").html(html);
		});
		/** ********************************************** */
		var date = $(this).find("td:eq(2)").text();
		$(this).find("td:eq(2)").text(date.substring(0, 10));
		/** ********************************************** */
		var html = "";
		var text = $(this).find("td:eq(3)").text();
		if (language == "zh" && text == "未读")
			html = "<font color='#FFB000'>未读</font>";
		if (language == "en" && text == "未读")
			html = "<font color='#FFB000'>Unread</font>";
		if (language == "zh" && text == "已读")
			html = "<font color='#2AB673'>已读</font>";
		if (language == "en" && text == "已读")
			html = "<font color='#2AB673'>Already</font>";
		$(this).find("td:eq(3)").html(html);
		/** ********************************************** */
		$(this).find("input[type=button]").click(function() {
			var tipsText = "確定要刪除該數據嗎？";
        	var showText = "刪除數據成功！";
        	if (language == "en") {
        		tipsText = "Are you sure you want to delete this data?";
        		showText = "Operating successfully!";
        	}
            if (confirm(tipsText)) {
            	$(this).css("background-color", "#CCC");
                $(this).attr("disabled", true);
                if (Ajax("delete", {id: id}))
                	showTips(showText);
                setTimeout("location.reload()", 2000);
            }
		});
	});
	/** ***************************************************************** */
	/** 关闭弹出页面 */
	$(".closebtn").click(function() {
		$("#page").hide();
	});
	/** ***************************************************************** */
	/** 上一页 */
	$(".pagebtn:eq(0)").click(function() {
		var page = Number($("#page1").text()) - 1;
		window.location.href = "showlist?page=" + page;
	});
	/** 下一页 */
	$(".pagebtn:eq(1)").click(function() {
		var page = Number($("#page1").text()) + 1;
		window.location.href = "showlist?page=" + page;
	});
	/** ***************************************************************** */
	var page1 = $("#page1").text();
	var page2 = $("#page2").text();
	if (page1 <= 1) {
		$(".pagebtn:eq(0)").attr("disabled", true);
		$(".pagebtn:eq(0)").css("color", "#999");
	}
	if (page1 == page2) {
		$(".pagebtn:eq(1)").attr("disabled", true);
		$(".pagebtn:eq(1)").css("color", "#999");
	}
	/** **************************************************************** */
	function showTips(text) {
		$("#Tip").show().delay(1800).hide(200);
		$("#Tip").text(text);
	}
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
