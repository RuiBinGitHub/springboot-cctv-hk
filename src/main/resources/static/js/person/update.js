$(document).ready(function() {

	$("#tab1 input[type=text]").attr("readonly", true);
	$("#tab1 input[type=text]").css("background-color", "#f0f0f0");
	/** ****************************************** */
	var value = $("input[type=text]:eq(2)").val();
	var cont = getrRepeats("", value.length - 4);
	var text = value.replace(/(.{2}).*(.{2})/, cont);
	$("input[type=text]:eq(2)").val(text);
	/** ****************************************** */
	var value = $("input[type=text]:eq(3)").val();
	var cont = getrRepeats("", value.length - 13);
	var text = value.replace(/(.{3}).*(.{10})/, cont);
	$("input[type=text]:eq(3)").val(text);
	/** ****************************************** */
	function getrRepeats(str, length) {
		str += "$1";
		for (var i = 0; i < length; ++i)
			str += "*";
		str += "$2";
		return str;
	}
});