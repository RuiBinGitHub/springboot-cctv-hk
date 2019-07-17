function showimg(obj, event) {
	$(obj).css("cursor", "pointer");
	$(obj).css("text-decoration", "underline");
	$("#img").attr("src", "/CCTV/img/100000.png");
	$("#img").css("width", "400px");
	$("#img").css("height", "300px");
	$("#img").css({
		"left" : 20,
		"top" : event.pageY - 300
	});
	$("#img").css("position", "fixed");
	$("#img").css({
		"display" : "none",
		"z-index" : 99
	});
	var path = "http://192.168.0.125:8080/ItemImage/"
	var name = $(obj).attr("id");
	$("#img").attr("src", path + name + ".png");
	$("#img").show();
};

function hideimg(obj) {
	$(obj).css("text-decoration", "none");
	$("#img").hide();
};

// $("#tab2 a").each(function() {
// $(this).mouseenter(function(){
//		
// });
// $(this).mouseleave(function(){
// $(this).css("text-decoration","none");
// });
// });
