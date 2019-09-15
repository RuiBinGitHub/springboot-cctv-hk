$(document).ready(function() {
	// 获取当前语言
	var language = $("#infoTop").text() == "信息列表" ? "zh" : "en";
	
	var tipsText1 = language == "zh" ? "確定要刪除該數據嗎？" : "Are you sure you want to delete this data?";
    var tipsText2 = language == "zh" ? "數據刪除成功！" : "Operating successfully!";
    var html1 = language == "zh" ? "<font color='#2AB673'>已读</font>" : "<font color='#2AB673'>Already</font>";
    var html2 = language == "zh" ? "<font color='#FFB000'>未读</font>" : "<font color='#FFB000'>Unread</font>";
	/** ********************************************** */
	$("#mtab1 tr").each(function(i) {
		var id = $(this).attr("id");
		$(this).find("a").click(function() {
			var data = Ajax("findinfo", {id : id});
			if (data == null)
				return null;
			$("#info1 span:eq(0)").text(data.title);
			$("#info1 span:eq(1)").text(data.senderName);
			$("#info1 span:eq(2)").text(data.acceptName);
			$("#info1 span:eq(3)").text(data.date);
			$("#info2").html(data.text);
			$("#info2 a").attr("target", "_blank");
			$("#page").show();
			/** ****************************************** */
			$("#tab1 tr").eq(i).find("td:eq(3)").html(html1);
		});
		/** ********************************************** */
		var date = $(this).find("td:eq(2)").text();
		$(this).find("td:eq(2)").text(date.substring(0, 10));
		/** ********************************************** */
		if ($(this).find("td:eq(3)").text() == "已读")
			$(this).find("td:eq(3)").html(html1);
		else
			$(this).find("td:eq(3)").html(html2);
		/** ********************************************** */
		$(this).find("input[type=button]").click(function() {
			if (!confirm(tipsText1)) 
				return false;
			$(this).css("background-color", "#CCC");
			$(this).attr("disabled", true);
			if (Ajax("delete", {id : id}))
				showTips(tipsText2);
			setTimeout("location.reload()", 2000);
		});
	});
	/** ***************************************************************** */
	/** 关闭弹出页面 */
	$(".colse").click(function() {
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
			url: url,
			data: data,
			type: "post",
			async: false,
			datatype: "json",
			success: function(data) {
				result = data;
			}
		});
		return result;
	}
});
