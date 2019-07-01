$(document).ready(function() {
	
    initList();
    /********************************************************************/
    var id = $("#id").val();
    var no = $("#no").val() == "" ? 0 : $("#no").val();
    var index = no >= $("#tab1 tr").length ? $("#tab1 tr").length-1 : no;
    $("#tab1 tbody tr").eq(index).find("td:eq(0)").text("▶");
    $("#tab2 tbody tr:eq(0)").find("td:eq(0)").text("▶");
    /********************************************************************/
    $(document).scroll(function(e) {
        var height = $(document).scrollTop();
        if (height >= 135)
            $("#TitleMemu").show();
        else
            $("#TitleMemu").hide();
    });
    if (no == 0) {
    	$("#TitleMemu input:eq(0)").css("background-color", "#CCC");
    	$("#TitleMemu input:eq(0)").attr("disabled", true);
    }
    if (no == $("#tab1 tr").length - 1) {
    	$("#TitleMemu input:eq(1)").css("background-color", "#CCC");
    	$("#TitleMemu input:eq(1)").attr("disabled", true);
    }
    $("#TitleMemu input:eq(0)").click(function() {
        var index = Number(no) - 1;
        window.location.href = "findinfo?id=" + id + "&no=" + index;
    });
    $("#TitleMemu input:eq(1)").click(function() {
        var index = Number(no) + 1;
        window.location.href = "findinfo?id=" + id + "&no=" + index;
    });
    /********************************************************************/
    $("#tab1 tr").each(function(i) {
    	var score1 = $(this).find("td:eq(4)").text();
        var score2 = $(this).find("td:eq(5)").text();
        $(this).find("td:eq(4)").text(Number(score1).toFixed(2));
        $(this).find("td:eq(5)").text(Number(score2).toFixed(2));
        if (score1 < 95)
            $(this).find("td:eq(4)").css("color", "#FF1000");
        else
            $(this).find("td:eq(4)").css("color", "#479911");
        if (score2 < 85)
            $(this).find("td:eq(5)").css("color", "#FF1000");
        else
            $(this).find("td:eq(5)").css("color", "#479911");
        
    	var socre1 = $(this).find("td:eq(6)").text();
    	var socre2 = $(this).find("td:eq(7)").text();
    	$(this).find("td:eq(6)").html(getImg(socre1));
    	$(this).find("td:eq(7)").html(getImg(socre2));
    	
        $(this).mouseenter(function() {
            $(this).find("input").show();
        });
        $(this).mouseleave(function() {
            $(this).find("input").hide();
        });
        $(this).find("input").click(function() {
            location.href = "findinfo?id=" + id + "&no=" + i;
        });
    });
    $("#showpipe").scrollTop(30 * index);
    function getImg(count){
    	var hrml = "";
    	for (var i = 0; i < count; i++)
    		hrml += "<img src='/CCTV/img/星星2.png'/>";
    	for (var i = count; i < 5; i++)
    		hrml += "<img src='/CCTV/img/星星1.png'/>";
    	return hrml;
    }
    $("#mainCenter input").attr("readonly", "true");
    /********************************************************************/
    var value1 = $("#showstar1").text();
    var value2 = $("#showstar2").text();
    $("#showstar1").html(getImg(value1));
    $("#showstar2").html(getImg(value2));
    $("#tab3 input").attr("readonly", true);
    $("#tab3 textarea").attr("readonly", true);
    /********************************************************************/
    var path = "http://192.168.0.125:8080/ItemIamge/";
    $("#tab2 tbody tr").each(function(i) {
        $(this).find("td:eq(3)").css("text-align", "right");
        $(this).find("td:eq(3)").css("padding-right", "10px");
        $(this).find("td:eq(12)").css("text-align", "right");
        $(this).find("td:eq(12)").css("padding-right", "10px");
        $(this).click(function() {
        	$("#tab2 tbody tr").find("td:eq(0)").text("");
        	$(this).find("td:eq(0)").text("▶");
            var name = $(this).find("td:eq(12)").text();
            if (name == "" || name.leng == 0)
                $("#pictures").attr("src", "/CCTV/img/blank-plus.png");
            else
                $("#pictures").attr("src", path + name + ".png");
        });
        var text = $(this).find("td:eq(11),td:eq(13)").text();
        $(this).find("td:eq(11),td:eq(13)").attr("title", text);
        if (Number($(this).find("td:eq(12)").text()) != 0)
            $(this).find("td:eq(12)").css("background-color", "#ff4400");
    });
    /********************************************************************/
    $("#video").click(function() {
        if ($("#video").attr("src") != undefined)
            video.paused ? video.play() : video.pause();
    });
    $("#video").dblclick(function() {
        $("#file1").click();
    });
    /** 视频文件选择器 */
    $("#file1").change(function() {
        if (this.files && this.files[0]) {
            var url = getURL(this.files[0]);
            $("#video").attr("src", url);
            $("#video").attr("poster", "");
            this.value = "";
        }
    });
    /** 根据文件获取路径 */
    function getURL(file) {
        var url = null;
        if (window.createObjectURL != undefined)
            url = window.createObjectURL(file);
        else if (window.URL != undefined)
            url = window.URL.createObjectURL(file);
        else if (window.webkitURL != undefined)
            url = window.webkitURL.createObjectURL(file);
        return url;
    }
    function showTips(text) {
        $("#Tip").text(text);
        $("#Tip").show().delay(1800).hide(200);
    }
    /********************************************************************/
    /** 格式化显示信息 */
    function initList() {
        /***********************************************************/
        var value = $("#maintab3 tr:eq(1) input:eq(2)").val();
        if (value == "F")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Foul");
        else if (value == "S")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Surface water");
        else if (value == "C")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Combined");
        else if (value == "T")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Trade effluent");
        else if (value == "W")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Watercourse");
        else if (value == "O")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Others");
        else if (value == "U")
            $("#maintab3 tr:eq(1) input:eq(2)").val("Unknown");
        /***********************************************************/
        var value = $("#maintab3 tr:eq(1) input:eq(3)").val();
        if (value == "U")
            $("#maintab3 tr:eq(1) input:eq(3)").val("Upstream");
        else if (value == "D")
            $("#maintab3 tr:eq(1) input:eq(3)").val("Downstream");
        /***********************************************************/
        var value = $("#maintab3 tr:eq(1) input:eq(6)").val();
        if (value == "A")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Arched with Flat Bottom");
        else if (value == "B")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Barrel e.g. Beer Barrel Shape");
        else if (value == "C")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Circular");
        else if (value == "E")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Egg Shape");
        else if (value == "H")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Horseshoe");
        else if (value == "O")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Oval");
        else if (value == "K")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Kerb Block");
        else if (value == "R")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Rectangular");
        else if (value == "S")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Square");
        else if (value == "T")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Trapezoidal");
        else if (value == "U")
            $("#maintab3 tr:eq(1) input:eq(6)").val("U-shaped with Flat Top");
        else if (value == "Z")
            $("#maintab3 tr:eq(1) input:eq(6)").val("Other");
        /***********************************************************/
        var value = $("#maintab3 tr:eq(1) input:eq(7)").val();
        if (value == "AC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Asbestos Cement");
        else if (value == "BL")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Bitumen Lining");
        else if (value == "BR")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Brick");
        else if (value == "CI")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Cast Iron");
        else if (value == "CL")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Cement Mortar Lining");
        else if (value == "CO")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Concrete");
        else if (value == "CS")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Concrete Segments");
        else if (value == "DI")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Ductile Iron");
        else if (value == "EP")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Epoxy");
        else if (value == "FC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Fibre Cement");
        else if (value == "FRP")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Fibre Reinforced Plastics");
        else if (value == "GI")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Galvanised Iron");
        else if (value == "MAC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Masonry in Regular Courses");
        else if (value == "MAR")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Masonry in Randomly Coursed");
        else if (value == "PVC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Polyvinyl Chloride");
        else if (value == "PE")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Polyethylene");
        else if (value == "PF")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Pitch Fibre");
        else if (value == "PP")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Polypropylene");
        else if (value == "PS")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Polyester");
        else if (value == "RC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Reinforced Concrete");
        else if (value == "SPC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Sprayed Concrete");
        else if (value == "ST")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Steel");
        else if (value == "VC")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Vitrified Clay");
        else if (value == "X")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Unidentified Material");
        else if (value == "XI")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Unidentified Type of Iron or Steel");
        else if (value == "XP")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Unidentified Type of Plastics");
        else if (value == "Z")
            $("#maintab3 tr:eq(1) input:eq(7)").val("Other");
        else if (value == "")
            $("#maintab3 tr:eq(1) input:eq(7)").val("--");
        /***********************************************************/
        var value = $("#maintab3 tr:eq(1) input:eq(8)").val();
        if (value == "CF")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Close Fit Lining");
        else if (value == "CIP")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Cured In Place Lining");
        else if (value == "CP")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Lining With Continuous Pipes");
        else if (value == "DP")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Lining With Discrete Pipes");
        else if (value == "M")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Lining Inserted During Manufacture");
        else if (value == "N")
            $("#maintab3 tr:eq(1) input:eq(8)").val("No Lining");
        else if (value == "SEG")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Segmental Linings");
        else if (value == "SP")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Sprayed Lining");
        else if (value == "SW")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Spirally Wound Lining");
        else if (value == "Z")
            $("#maintab3 tr:eq(1) input:eq(8)").val("Other");
        else if (value == "")
            $("#maintab3 tr:eq(1) input:eq(8)").val("--");
        /***********************************************************/
        var value = $("#maintab4 tr:eq(1) input:eq(8)").val();
        if (value == "")
            $("#maintab4 tr:eq(1) input:eq(8)").val("--");
        else if (value == "1")
            $("#maintab4 tr:eq(1) input:eq(8)").val("Dry");
        else if (value == "2")
            $("#maintab4 tr:eq(1) input:eq(8)").val("Heavy Rain");
        else if (value == "3")
            $("#maintab4 tr:eq(1) input:eq(8)").val("Light Rain");
        else if (value == "4")
            $("#maintab4 tr:eq(1) input:eq(8)").val("Showers");
    }
});
