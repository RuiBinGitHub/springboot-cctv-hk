$(document).ready(function() {
    initList();
    drawPipe();
    var id = $("input[name=id]").val();
    var no = $("input[name=no]").val() == "" ? 0 : $("input[name=no]").val();
    /** ******************************************************************** */
    $(document).scroll(function(e) {
        if ($(document).scrollTop() >= 150)
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
        var value = $("#main2 table:eq(1) input:eq(2)").val();
        if (value == "")
            $("#main2 table:eq(1) input:eq(2)").val("--");
        else if (value == "BL")
            $("#main2 table:eq(1) input:eq(2)").val("Bus Lane");
        else if (value == "BLG")
            $("#main2 table:eq(1) input:eq(2)").val("Under Permament Building");
        else if (value == "DIF")
            $("#main2 table:eq(1) input:eq(2)").val("Difficult Access Area");
        else if (value == "EX")
            $("#main2 table:eq(1) input:eq(2)").val("Expressway");
        else if (value == "FLD")
            $("#main2 table:eq(1) input:eq(2)").val("Fields");
        else if (value == "FWY")
            $("#main2 table:eq(1) input:eq(2)").val("Footway");
        else if (value == "RD")
            $("#main2 table:eq(1) input:eq(2)").val("Road");
        else if (value == "PD")
            $("#main2 table:eq(1) input:eq(2)").val("Other Pedestrian Area");
        else if (value == "PR")
            $("#main2 table:eq(1) input:eq(2)").val("Property With Building");
        else if (value == "WWY")
            $("#main2 table:eq(1) input:eq(2)").val("Under a Waterway");
        else if (value == "Z")
            $("#main2 table:eq(1) input:eq(2)").val("Other");
        /** ******************************************************** */
        var value = $("#main2 table:eq(2) input:eq(2)").val();
        if (value == "")
        	$("#main2 table:eq(2) input:eq(2)").val("--");
        else if (value == "F")
            $("#main2 table:eq(2) input:eq(2)").val("Foul");
        else if (value == "S")
            $("#main2 table:eq(2) input:eq(2)").val("Surface water");
        else if (value == "C")
            $("#main2 table:eq(2) input:eq(2)").val("Combined");
        else if (value == "T")
            $("#main2 table:eq(2) input:eq(2)").val("Trade effluent");
        else if (value == "W")
            $("#main2 table:eq(2) input:eq(2)").val("Watercourse");
        else if (value == "O")
            $("#main2 table:eq(2) input:eq(2)").val("Others");
        else if (value == "U")
            $("#main2 table:eq(2) input:eq(2)").val("Unknown");
        /** ******************************************************** */
        var value = $("#main2 table:eq(2) input:eq(3)").val();
        if (value == "")
        	$("#main2 table:eq(2) input:eq(3)").val("--");
        else if (value == "U")
            $("#main2 table:eq(2) input:eq(3)").val("Upstream");
        else if (value == "D")
            $("#main2 table:eq(2) input:eq(3)").val("Downstream");
        /** ******************************************************** */
        var value = $("#main2 table:eq(2) input:eq(6)").val();
        if (value == "")
        	$("#main2 table:eq(2) input:eq(6)").val("--");
        else if (value == "A")
            $("#main2 table:eq(2) input:eq(6)").val("Arched with Flat Bottom");
        else if (value == "B")
            $("#main2 table:eq(2) input:eq(6)").val("Barrel e.g. Beer Barrel Shape");
        else if (value == "C")
            $("#main2 table:eq(2) input:eq(6)").val("Circular");
        else if (value == "E")
            $("#main2 table:eq(2) input:eq(6)").val("Egg Shape");
        else if (value == "H")
            $("#main2 table:eq(2) input:eq(6)").val("Horseshoe");
        else if (value == "O")
            $("#main2 table:eq(2) input:eq(6)").val("Oval");
        else if (value == "K")
            $("#main2 table:eq(2) input:eq(6)").val("Kerb Block");
        else if (value == "R")
            $("#main2 table:eq(2) input:eq(6)").val("Rectangular");
        else if (value == "S")
            $("#main2 table:eq(2) input:eq(6)").val("Square");
        else if (value == "T")
            $("#main2 table:eq(2) input:eq(6)").val("Trapezoidal");
        else if (value == "U")
            $("#main2 table:eq(2) input:eq(6)").val("U-shaped with Flat Top");
        else if (value == "Z")
            $("#main2 table:eq(2) input:eq(6)").val("Other");
        /** ******************************************************** */
        var value = $("#main2 table:eq(2) input:eq(7)").val();
        if (value == "")
        	$("#main2 table:eq(2) input:eq(7)").val("--");
        else if (value == "AC")
            $("#main2 table:eq(2) input:eq(7)").val("Asbestos Cement");
        else if (value == "BL")
            $("#main2 table:eq(2) input:eq(7)").val("Bitumen Lining");
        else if (value == "BR")
            $("#main2 table:eq(2) input:eq(7)").val("Brick");
        else if (value == "CI")
            $("#main2 table:eq(2) input:eq(7)").val("Cast Iron");
        else if (value == "CL")
            $("#main2 table:eq(2) input:eq(7)").val("Cement Mortar Lining");
        else if (value == "CO")
            $("#main2 table:eq(2) input:eq(7)").val("Concrete");
        else if (value == "CS")
            $("#main2 table:eq(2) input:eq(7)").val("Concrete Segments");
        else if (value == "DI")
            $("#main2 table:eq(2) input:eq(7)").val("Ductile Iron");
        else if (value == "EP")
            $("#main2 table:eq(2) input:eq(7)").val("Epoxy");
        else if (value == "FC")
            $("#main2 table:eq(2) input:eq(7)").val("Fibre Cement");
        else if (value == "FRP")
            $("#main2 table:eq(2) input:eq(7)").val("Fibre Reinforced Plastics");
        else if (value == "GI")
            $("#main2 table:eq(2) input:eq(7)").val("Galvanised Iron");
        else if (value == "MAC")
            $("#main2 table:eq(2) input:eq(7)").val("Masonry in Regular Courses");
        else if (value == "MAR")
            $("#main2 table:eq(2) input:eq(7)").val("Masonry in Randomly Coursed");
        else if (value == "PVC")
            $("#main2 table:eq(2) input:eq(7)").val("Polyvinyl Chloride");
        else if (value == "PE")
            $("#main2 table:eq(2) input:eq(7)").val("Polyethylene");
        else if (value == "PF")
            $("#main2 table:eq(2) input:eq(7)").val("Pitch Fibre");
        else if (value == "PP")
            $("#main2 table:eq(2) input:eq(7)").val("Polypropylene");
        else if (value == "PS")
            $("#main2 table:eq(2) input:eq(7)").val("Polyester");
        else if (value == "RC")
            $("#main2 table:eq(2) input:eq(7)").val("Reinforced Concrete");
        else if (value == "SPC")
            $("#main2 table:eq(2) input:eq(7)").val("Sprayed Concrete");
        else if (value == "ST")
            $("#main2 table:eq(2) input:eq(7)").val("Steel");
        else if (value == "VC")
            $("#main2 table:eq(2) input:eq(7)").val("Vitrified Clay");
        else if (value == "X")
            $("#main2 table:eq(2) input:eq(7)").val("Unidentified Material");
        else if (value == "XI")
            $("#main2 table:eq(2) input:eq(7)").val("Unidentified Type of Iron or Steel");
        else if (value == "XP")
            $("#main2 table:eq(2) input:eq(7)").val("Unidentified Type of Plastics");
        else if (value == "Z")
            $("#main2 table:eq(2) input:eq(7)").val("Other");
        /** ******************************************************** */
        var value = $("#main2 table:eq(2) input:eq(8)").val();
        if (value == "")
        	$("#main2 table:eq(2) input:eq(8)").val("--");
        else if (value == "CF")
            $("#main2 table:eq(2) input:eq(8)").val("Close Fit Lining");
        else if (value == "CIP")
            $("#main2 table:eq(2) input:eq(8)").val("Cured In Place Lining");
        else if (value == "CP")
            $("#main2 table:eq(2) input:eq(8)").val("Lining With Continuous Pipes");
        else if (value == "DP")
            $("#main2 table:eq(2) input:eq(8)").val("Lining With Discrete Pipes");
        else if (value == "M")
            $("#main2 table:eq(2) input:eq(8)").val("Lining Inserted During Manufacture");
        else if (value == "N")
            $("#main2 table:eq(2) input:eq(8)").val("No Lining");
        else if (value == "SEG")
            $("#main2 table:eq(2) input:eq(8)").val("Segmental Linings");
        else if (value == "SP")
            $("#main2 table:eq(2) input:eq(8)").val("Sprayed Lining");
        else if (value == "SW")
            $("#main2 table:eq(2) input:eq(8)").val("Spirally Wound Lining");
        else if (value == "Z")
            $("#main2 table:eq(2) input:eq(8)").val("Other");
        else if (value == "")
            $("#main2 table:eq(2) input:eq(8)").val("--");
        /** ******************************************************** */
        var value = $("#main2 table:eq(3) input:eq(6)").val();
        if (value == "")
            $("#main2 table:eq(3) input:eq(6)").val("--");
        else if (value == "Y")
            $("#main2 table:eq(3) input:eq(6)").val("Y");
        else if (value == "N")
            $("#main2 table:eq(3) input:eq(6)").val("N");
        /** ******************************************************** */
        var value = $("#main2 table:eq(3) input:eq(7)").val();
        if (value == "")
            $("#main2 table:eq(3) input:eq(7)").val("--");
        else if (value == "1")
            $("#main2 table:eq(3) input:eq(7)").val("Dry");
        else if (value == "2")
            $("#main2 table:eq(3) input:eq(7)").val("Heavy Rain");
        else if (value == "3")
            $("#main2 table:eq(3) input:eq(7)").val("Light Rain");
        else if (value == "4")
            $("#main2 table:eq(3) input:eq(7)").val("Showers");
    }
});
