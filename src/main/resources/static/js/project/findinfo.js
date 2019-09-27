$(document).ready(function() {
	
    initList();
    drawPipe();
    var id = $("input[name=id]").val();
    var no = $("input[name=no]").val() == "" ? 0 : $("input[name=no]").val();
    /** ******************************************************************** */
    $(document).scroll(function(e) {
        if ($(document).scrollTop() >= 180)
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
    /** ******************************************************************** */
    $("#main2 input").attr("readonly", true);
    var index = $("#main input[name=no]").val();
    index = index >= $("#tab1 tr").length ? $ ("#tab1 tr").length - 1 : index;
    $("#tab1 tr").eq(index).find("td:eq(0)").css("background-color", "#FFD58D");
    $("#tab1 tr").eq(index).find("td:eq(0)").text("▶");
    $("#tab1").parents("div").scrollTop(index*24);
    $("#tab1 tr").each(function(no) {
        $(this).mouseenter(function() {
            $(this).find("input").show();
        });
        $(this).mouseleave(function() {
            $(this).find("input").hide();
        });
        $(this).find("input").css("background-color", "#ff8000");
        $(this).find("input").click(function() {
            var id = $("#main2 input:eq(0)").val();
            window.location.href = "findinfo?id=" + id + "&no=" + no;
        });
    });
    /** ******************************************************************** */
    $("#video").click(function() {
        if ($("#video").attr("src") != undefined && $("#video").attr("src") != "")
            video.paused ? video.play() : video.pause();
    });
    $("#video").dblclick(function() {
        $("#file1").click();
    });
    /** 视频文件选择器 */
    $("#file1").change(function() {
    	if (this.files.length == 0)
            return false;
        var url = getURL(this.files[0]);
        $("#video").attr("src", url);
        $("#video").attr("poster", "");
        this.value = "";
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
    /** ******************************************************************** */
    var path = $("#path").val();
    $("#tab2 tbody tr:eq(0) td:eq(0)").text("▶");
    $("#tab2 tbody tr").each(function(i) {
        $(this).unbind("click");
        $(this).click(function() {
            $("#tab2 tbody tr").find("td:eq(0)").text("");
            $(this).find("td:eq(0)").text("▶");
            var value = $(this).find("td:last").text();
            if (value.length > 0)
                $("#pic1").attr("src", path + value + ".png");
            else
                $("#pic1").attr("src", "/CCTV/img/blank.png");
        });
    	$(this).find("td:eq(12)").css("display", "none");
    	setCode($(this).find("td:eq(5)"), $(this).find("td:eq(5)").text());
    });
    /** ***************************************************************************** */
    function drawPipe() {
        var canvas = $("#showpipeimg")[0];
        var context = canvas.getContext("2d");
        context.font = "12px Courier New";
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.beginPath();
        context.fillStyle = "#A1A1A1";
        context.strokeStyle = "#000000";
        context.rect(30, 100, 30, 640);
        context.fillRect(31, 101, 28, 638);
        context.stroke();
        context.closePath();

        context.beginPath();
        context.fillStyle = "#A0A0A0";
        context.strokeStyle = "#606060";
        var tl = 0.0;
        var distlist = new Array();
        var joinlist = new Array();
        $("#tab2 tbody tr").each(function() {
            if (Number($(this).find("td:eq(3)").text()) > tl)
                tl = $(this).find("td:eq(3)").text();
            if ($(this).find("td:eq(5)").text() == "MH")
                distlist.push($(this).find("td:eq(3)").text());
            if ($(this).find("td:eq(5)").text() == "JN")
                joinlist.push($(this).find("td:eq(3)").text());
        });
        tl = tl <= 0.0 ? 1 : tl;
        var use = $("#input3 input:eq(2)").val();
        for (var i = 0; i < distlist.length; i++) {
            if (use == "Foul") {
                var distance = i > 0 ? 100 : 40;
                var location = distlist[i] / tl * 640 + distance;
                context.fillRect(15, location, 60, 60);
            } else {
                var distance = i > 0 ? 130 : 70;
                var location = distlist[i] / tl * 640 + distance;
                context.moveTo(75, location);
                context.arc(45, location, 30, 0, Math.PI * 2);
                context.fill();
            }
        }
        for (var i = 0; i < joinlist.length; i++) {
            var location = joinlist[i] / tl * 640 + 90;
            context.fillRect(10, location, 19, 10);
        }
        function Note(dist, code) {
            this.dist = dist;
            this.code = code;
        }
        var list = new Array();
        $("#tab2 tbody tr").each(function() {
            if ($(this).find("td:eq(3)").text().length != 0) {
                var dist = $(this).find("td:eq(3)").text();
                var code = $(this).find("td:eq(5)").text();
                var note = new Note(dist,code);
                list.push(note);
            }
        });
        
        var i = 0;
        context.fillStyle = "#000000";
        var itemlength = 100;
        while (i < list.length) {
            var distance = Math.round(list[i].dist / tl * 640 + 100);
            location = distance - itemlength < 0 ? itemlength : distance;
            itemlength = location + 15;
            context.moveTo(30, distance);
            context.lineTo(60, distance);
            context.moveTo(60, distance);
            context.lineTo(110, location);
            context.lineTo(125, location);
            context.fillText(list[i].dist, 130, location + 4);
            context.fillText(list[i].code, 170, location + 4);
            i++;
        }
        context.stroke();
        context.closePath();
    }
    /** 返回顶部 */
    $("#toTop1").click(function() {
        $("body,html").animate({scrollTop: 0}, 100);
    });
    /** 格式化显示信息 */
    function initList() {
        var value = $("#input3 input:eq(2)").val();
        if (value == "F")
            $("#input3 input:eq(2)").val("Foul");
        else if (value == "S")
            $("#input3 input:eq(2)").val("Surface water");
        else if (value == "C")
            $("#input3 input:eq(2)").val("Combined");
        else if (value == "T")
            $("#input3 input:eq(2)").val("Trade effluent");
        else if (value == "W")
            $("#input3 input:eq(2)").val("Watercourse");
        else if (value == "O")
            $("#input3 input:eq(2)").val("Others");
        else if (value == "U")
            $("#input3 input:eq(2)").val("Unknown");
        /***********************************************************/
        var value = $("#input3 input:eq(3)").val();
        if (value == "U")
            $("#input3 input:eq(3)").val("Upstream");
        else if (value == "D")
            $("#input3 input:eq(3)").val("Downstream");
        /***********************************************************/
        var value = $("#input3 input:eq(6)").val();
        if (value == "A")
            $("#input3 input:eq(6)").val("Arched with Flat Bottom");
        else if (value == "B")
            $("#input3 input:eq(6)").val("Barrel e.g. Beer Barrel Shape");
        else if (value == "C")
            $("#input3 input:eq(6)").val("Circular");
        else if (value == "E")
            $("#input3 input:eq(6)").val("Egg Shape");
        else if (value == "H")
            $("#input3 input:eq(6)").val("Horseshoe");
        else if (value == "O")
            $("#input3 input:eq(6)").val("Oval");
        else if (value == "K")
            $("#input3 input:eq(6)").val("Kerb Block");
        else if (value == "R")
            $("#input3 input:eq(6)").val("Rectangular");
        else if (value == "S")
            $("#input3 input:eq(6)").val("Square");
        else if (value == "T")
            $("#input3 input:eq(6)").val("Trapezoidal");
        else if (value == "U")
            $("#input3 input:eq(6)").val("U-shaped with Flat Top");
        else if (value == "Z")
            $("#input3 input:eq(6)").val("Other");
        /***********************************************************/
        var value = $("#input3 input:eq(7)").val();
        if (value == "AC")
            $("#input3 input:eq(7)").val("Asbestos Cement");
        else if (value == "BL")
            $("#input3 input:eq(7)").val("Bitumen Lining");
        else if (value == "BR")
            $("#input3 input:eq(7)").val("Brick");
        else if (value == "CI")
            $("#input3 input:eq(7)").val("Cast Iron");
        else if (value == "CL")
            $("#input3 input:eq(7)").val("Cement Mortar Lining");
        else if (value == "CO")
            $("#input3 input:eq(7)").val("Concrete");
        else if (value == "CS")
            $("#input3 input:eq(7)").val("Concrete Segments");
        else if (value == "DI")
            $("#input3 input:eq(7)").val("Ductile Iron");
        else if (value == "EP")
            $("#input3 input:eq(7)").val("Epoxy");
        else if (value == "FC")
            $("#input3 input:eq(7)").val("Fibre Cement");
        else if (value == "FRP")
            $("#input3 input:eq(7)").val("Fibre Reinforced Plastics");
        else if (value == "GI")
            $("#input3 input:eq(7)").val("Galvanised Iron");
        else if (value == "MAC")
            $("#input3 input:eq(7)").val("Masonry in Regular Courses");
        else if (value == "MAR")
            $("#input3 input:eq(7)").val("Masonry in Randomly Coursed");
        else if (value == "PVC")
            $("#input3 input:eq(7)").val("Polyvinyl Chloride");
        else if (value == "PE")
            $("#input3 input:eq(7)").val("Polyethylene");
        else if (value == "PF")
            $("#input3 input:eq(7)").val("Pitch Fibre");
        else if (value == "PP")
            $("#input3 input:eq(7)").val("Polypropylene");
        else if (value == "PS")
            $("#input3 input:eq(7)").val("Polyester");
        else if (value == "RC")
            $("#input3 input:eq(7)").val("Reinforced Concrete");
        else if (value == "SPC")
            $("#input3 input:eq(7)").val("Sprayed Concrete");
        else if (value == "ST")
            $("#input3 input:eq(7)").val("Steel");
        else if (value == "VC")
            $("#input3 input:eq(7)").val("Vitrified Clay");
        else if (value == "X")
            $("#input3 input:eq(7)").val("Unidentified Material");
        else if (value == "XI")
            $("#input3 input:eq(7)").val("Unidentified Type of Iron or Steel");
        else if (value == "XP")
            $("#input3 input:eq(7)").val("Unidentified Type of Plastics");
        else if (value == "Z")
            $("#input3 input:eq(7)").val("Other");
        else if (value == "")
            $("#input3 input:eq(7)").val("--");
        /***********************************************************/
        var value = $("#input3 input:eq(8)").val();
        if (value == "CF")
            $("#input3 input:eq(8)").val("Close Fit Lining");
        else if (value == "CIP")
            $("#input3 input:eq(8)").val("Cured In Place Lining");
        else if (value == "CP")
            $("#input3 input:eq(8)").val("Lining With Continuous Pipes");
        else if (value == "DP")
            $("#input3 input:eq(8)").val("Lining With Discrete Pipes");
        else if (value == "M")
            $("#input3 input:eq(8)").val("Lining Inserted During Manufacture");
        else if (value == "N")
            $("#input3 input:eq(8)").val("No Lining");
        else if (value == "SEG")
            $("#input3 input:eq(8)").val("Segmental Linings");
        else if (value == "SP")
            $("#input3 input:eq(8)").val("Sprayed Lining");
        else if (value == "SW")
            $("#input3 input:eq(8)").val("Spirally Wound Lining");
        else if (value == "Z")
            $("#input3 input:eq(8)").val("Other");
        else if (value == "")
            $("#input3 input:eq(8)").val("--");
        /***********************************************************/
        var value = $("#input4 input:eq(7)").val();
        if (value == "")
            $("#input4 input:eq(7)").val("--");
        else if (value == "Y")
            $("#input4 input:eq(7)").val("Y");
        else if (value == "N")
            $("#input4 input:eq(7)").val("N");
        /***********************************************************/
        var value = $("#input4 input:eq(8)").val();
        if (value == "")
            $("#input4 input:eq(8)").val("--");
        else if (value == "1")
            $("#input4 input:eq(8)").val("Dry");
        else if (value == "2")
            $("#input4 input:eq(8)").val("Heavy Rain");
        else if (value == "3")
            $("#input4 input:eq(8)").val("Light Rain");
        else if (value == "4")
            $("#input4 input:eq(8)").val("Showers");
    }
    
    function setCode(obj, code) {
    	if (code == "DE E-1") {
    		obj.attr("title", "Encrustation Heavy, >20% cross section area loss");
    		obj.text("DE E");
    	} else if (code == "DE E-2") {
    		obj.attr("title", "Encrustation Light, <5% cross section area loss");
    		obj.text("DE E");
    	} else if (code == "DE E-3") {
    		obj.attr("title", "Encrustation Medium, 5%-20% cross section area loss");
    		obj.text("DE E");
    	} else if (code == "H-1") {
    		obj.attr("title", "Hole Above");
    		obj.text("H");
    	} else if (code == "H-2") {
    		obj.attr("title", "Hole Below");
    		obj.text("H");
    	} else if (code == "MM-1") {
    		obj.attr("title", "Mortar loss Large (>50mm)");
    		obj.text("MM");
    	} else if (code == "MM-2") {
    		obj.attr("title", "Mortar loss Medium (15-50mm)");
    		obj.text("MM");
    	} else if (code == "MM-3") {
    		obj.attr("title", "Mortar loss Slight (<15mm)");
    		obj.text("MM");
    	} else if (code == "SS-1") {
    		obj.attr("title", "Surface Damage - Spalling Large (Deep voids)");
    		obj.text("SS");
    	} else if (code == "SS-2") {
    		obj.attr("title", "Surface Damage - Spalling Media (Aggregate)");
    		obj.text("SS");
    	} else if (code == "SS-3") {
    		obj.attr("title", "Surface Damage - Spalling Small (Surface exposed)");
    		obj.text("SS");
    	}  else if (code == "SW-1") {
    		obj.attr("title", "Surface Damage - Wear, Increased Roughness. Large (Deep voids)");
    		obj.text("SW");
    	} else if (code == "SW-2") {
    		obj.attr("title", "Surface Damage - Wear, Increased Roughness. Media (Aggregate)");
    		obj.text("SW");
    	} else if (code == "SW-3") {
    		obj.attr("title", "Surface Damage - Wear, Increased Roughness. Small (Surface exposed)");
    		obj.text("SW");
    	} else if (code == "#4-1") {
    		obj.attr("title", "Survey Abandoned - Out of Survey Area");
    		obj.text("#4");
    	} else if (code == "#4-2") {
    		obj.attr("title", "Survey Abandoned - Complete from next side");
    		obj.text("#4");
    	} else if (code == "#4-3") {
    		obj.attr("title", "Survey Abandoned - Defect");
    		obj.text("#4");
    	} else if (code == "#4-4") {
    		obj.attr("title", "Survey Abandoned - Seal Pipe");
    		obj.text("#4");
    	} else if (code == "#4-5") {
    		obj.attr("title", "Survey Abandoned - U-Trap");
    		obj.text("#4");
    	} 
    }
});
