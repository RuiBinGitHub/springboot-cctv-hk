$(document).ready(function() {
	// 获取当前语言
	var language = $("#topMenu span").text() == "項目範圍" ? "zh" : "en";
    /********************************************************************/
	$("#topMenu input").click(function(){
		if (confirm("确定删除当前项目全部坐标数据吗？")) {
			var id = $("input[name=id]").val();
			window.location.href = "delete?id=" + id;
		}
	});
	/********************************************************************/
	var tempFeature = null;
    initInputExtent();
    $("#append").click(function() {
        var context = "<div><a>坐標：</a><input type='text'/><img/></div>";
        $("#extent").append(context);
        initInputExtent();
    });
    function initInputExtent() {
        $("#extent div").each(function(i) {
        	$(this).unbind();
            $(this).mouseenter(function(e) {
                $(this).find("img").show();
            });
            $(this).mouseleave(function(e) {
                $(this).find("img").hide();
            });
        });
        var place = "000000.000 000000.000";
        $("#extent input[type=text]").attr("name", "extent");
        $("#extent input[type=text]").attr("placeholder", place);
        $("#extent input[type=text]").focus(function() {
            $(this).css("border-color", "#CCCCCC");
        });
        $("#extent img").each(function(i) {
        	var tipsText = "確定刪除該坐標嗎?";
        	if (language == "en")
        		tipsText = "Are you sure you want to delete this data?";
            $(this).attr("src", "/CCTV/img/remove.png");
            $(this).unbind("click");
            $(this).click(function() {
                if (confirm(tipsText))
                	$(this).parent().remove();
            });
        });
    }
    $("#commit").click(function() {
    	if ($("#extent input[name=extent]").length == 0) {
    		if (Ajax("inputextents", $("#extent").serialize())) {
    	        if (language == "zh")
    	    		showTips("数据保存成功！");
    	    	else
    	    		showTips("Operating successfully!");
    		}
    		setTimeout("location.reload()", 2000);
    		return true;
    	}
        if ($("#extent input[name=extent]").length <= 2) {
        	if (language == "zh")
        		showTips("请输入正确的项目范围！");
        	else
        		showTips("Please check the input data!");
            return false;
        }
        var result = true;
        $("#extent input[name=extent]").each(function() {
            var list = $(this).val().split(" ");
            if (list.length != 2 || isNaN(list[0]) || isNaN(list[1])) {
                $(this).css("border-color", "#f00");
                result = false;
            }
        });
        if (result == false) {
            if (language == "zh")
        		showTips("請輸入正確的項目範圍！");
        	else
        		showTips("Please check the input data!");
            return false;
        }
        var value = $("#extent input[name=extent]:eq(0)").val();
        var context = "<input type='hidden' name='extent' value='" + value + "'/>";
        $(this).css("background-color", "#CDCDCD");
        $(this).attr("disabled", true);
        $("#extent").append(context);
        if (Ajax("inputextents", $("#extent").serialize())) {
	        if (language == "zh")
	    		showTips("数据保存成功！");
	    	else
	    		showTips("Operating successfully!");
        }
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    $("#memu1 input[type=button]").click(function() {
        $("#xfile").click();
    });
    $("#xfile").change(function() {
        if (this.files.length == 0)
            return false;
        var formFile = new FormData();
        formFile.append("id", $("input[name=id]").val());
        formFile.append("file", this.files[0]);
        if (language == "zh")
        	$("#Tip").text("数据上传中...");
        else
        	$("#Tip").text("Data uploading...");
        $("#Tip").show();
        var showText1 = "數據導入成功！";
    	var showText2 = "上傳數據異常！";
    	if (language == "en") {
    		showText1 = "Operating successfully!";
    		showText2 = "Operating exceptioning!";
    	}
    	$("#memu1 input[type=button]").css("background-color", "#CDCDCD");
    	$("#memu1 input[type=button]").attr("disabled", true);
        if (FileAjax("importvalue", formFile))
	    	showTips(showText1);
        else
	    	showTips(showText2);
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    $("#tab1 tr input[type=button]").each(function(index) {
        $(this).click(function() {
            /** 显示坐标值 */
            $("#form1 input[type=hidden]").val($(this).attr("name"));
            $("#ftable1 span:eq(0)").text($(this).parents("tr").find("td:eq(2)").text());
            $("#ftable1 input[type=text]:eq(0)").val($(this).parents("tr").find("td:eq(3)").text());
            $("#ftable1 input[type=text]:eq(1)").val($(this).parents("tr").find("td:eq(4)").text());
            $("#ftable1 input[type=text]:eq(2)").val($(this).parents("tr").find("td:eq(5)").text());
            $("#ftable1 span:eq(1)").text($(this).parents("tr").find("td:eq(6)").text());
            $("#ftable1 input[type=text]:eq(3)").val($(this).parents("tr").find("td:eq(7)").text());
            $("#ftable1 input[type=text]:eq(4)").val($(this).parents("tr").find("td:eq(8)").text());
            $("#ftable1 input[type=text]:eq(5)").val($(this).parents("tr").find("td:eq(9)").text());

            $("#form2 input[type=hidden]").val($(this).attr("name"));
            $("#form2 input[type=text]:eq(0)").val($(this).parents("tr").find("td:eq(11)").text());
            $("#form2 input[type=text]:eq(1)").val($(this).parents("tr").find("td:eq(12)").text());
            $("#form2 input[type=text]:eq(2)").val($(this).parents("tr").find("td:eq(13)").text());
            $("#form2 input[type=text]:eq(3)").val($(this).parents("tr").find("td:eq(14)").text());
            /** 设置样式 */
            $("#tab1 tr").find("td:eq(0)").text("");
            $(this).parents("tr").find("td:eq(0)").text("▶");
            $("#tab1 tr").find("td:eq(0)").css("background-color", "#ffffff");
            $(this).parents("tr").find("td:eq(0)").css("background-color", "#FBCA41");
        });
    });
    $("#tab1 tr input[type=button]").eq(0).click();
    /********************************************************************/
    /** 设置管道坐标输入框只能输入数字 */
    $("#ftable1 input[type=text]").each(function(i) {
        $(this).bind("input", function() {
            if ($(this).val() == "" || isNaN($(this).val()))
                $(this).css("border-color", "#FF4400");
            else
                $(this).css("border-color", "#CCCCCC");
        });
        $(this).keypress(function(event) {
            if (event.which == 46 || (event.which >= 48 && event.which <= 57))
                return true;
            return false;
        });
    });
    /********************************************************************/
    /** 提交数据 */
    $("#common").click(function() {
        for (var i = 0; i < $("#ftable1 input[type=text]").length; i++) {
            var value = $("#ftable1 input[type=text]").eq(i).val();
            if (value == "" || isNaN(value)) {
            	if (language == "zh")
            		showTips("请检查输入数据！");
            	else
            		showTips("Please check the input data!");
                return false;
            }
        }
        if (Ajax("inputvalue", $("#form1").serialize())) {
        	if (language == "zh")
        		showTips("数据保存成功！");
        	else
        		showTips("Operating successfully!");
        }
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    $("#editlink").click(function() {
        $("#page").show();
    });
    $("#bar span").click(function() {
        $("#page").hide();
    });
    $("#form input[type=button]").click(function() {
        if (Ajax("updategrade", $("#form2").serialize())) {
        	if (language == "zh")
        		showTips("数据保存成功！");
        	else
        		showTips("Operating successfully!");
        }
        setTimeout("location.reload()", 2000);
    });
    /********************************************************************/
    var location = 0;
    $("#link1").click(function() {
        location = 1;
        showMap();
    });
    $("#link2").click(function() {
        location = 2;
        showMap();
    });
    /********************************************************************/
    $("#close").click(function() {
        $("#map").hide();
    });
    $("#value").click(function() {
        var data = $("#coordinate a").text();
        var list = data.split("，");
        if (location == 1) {
            $("#ftable1 tr:eq(1) input[type=text]:eq(0)").val(list[0]);
            $("#ftable1 tr:eq(1) input[type=text]:eq(1)").val(list[1]);
        }
        if (location == 2) {
            $("#ftable1 tr:eq(3) input[type=text]:eq(0)").val(list[0]);
            $("#ftable1 tr:eq(3) input[type=text]:eq(1)").val(list[1]);
        }
        $("#coordinate a").text("000000.000，000000.000");
        $("#map").hide();
    });
    /********************************************************************/
    /** 定义坐标转换 */
    var projection = new ol.proj.Projection({
        code:"EPSG:2326",
        extent:[793259.70, 799130.01, 870525.78, 848940.16],
        axisOrientation:"neu",
        units:"m",
    });
    proj4.defs("EPSG:2326", "+proj=tmerc+lat_0=22.31213333333334+lon_0=114.1785555555556+k=1+x_0=836694.05+y_0=819069.8+ellps=intl+towgs84=-162.619,-276.959,-161.764,0.067753,-2.24365,-1.15883,-1.09425+units=m +no_defs");
    ol.proj.addProjection(projection);
    //定义坐标转换
    ol.proj.addCoordinateTransforms("EPSG:4326", "EPSG:2326", function(coordinate) {
        return proj4("EPSG:4326", "EPSG:2326", coordinate);
    }, function(coordinate) {
        return proj4("EPSG:2326", "EPSG:4326", coordinate);
    });
    ol.proj.addCoordinateTransforms("EPSG:3857", "EPSG:2326", function(coordinate) {
        return proj4("EPSG:3857", "EPSG:2326", coordinate);
    }, function(coordinate) {
        return proj4("EPSG:2326", "EPSG:3857", coordinate);
    });
    /********************************************************************/
    var center = ol.proj.transform([840000, 820000], "EPSG:2326", "EPSG:3857");
    var view = new ol.View({
        center: center,
        zoom: 10
    });
    var imap = new ol.layer.Tile({
        source: new ol.source.OSM(),
        name: "地图"
    });
    var map = new ol.Map({
        target: "map",
        layers: [imap],
        view: view
    });
    map.addControl(new ol.control.ScaleLine());
    map.addControl(new ol.control.Attribution());
    map.addControl(new ol.control.MousePosition({
        undefinedHTML: "",
        projection: "EPSG:2326",
        coordinateFormat: function(coordinate) {
            return ol.coordinate.format(coordinate, "{x},{y}", 3);
        }
    }));
    /********************************************************************/
    var vectorSource = new ol.source.Vector();
    function showMap() {
        tempFeature = null;
        var m1 = null, m2 = null;
        var iconFeature1 = null, iconFeature2 = null;
        vectorSource.clear();
        var x1 = $("#ftable1 tr:eq(1) input[type=text]:eq(0)").val();
        var y1 = $("#ftable1 tr:eq(1) input[type=text]:eq(1)").val();
        if (x1 != 0.0 && y1 != 0.0) {
            m1 = ol.proj.transform([x1, y1], "EPSG:2326", "EPSG:3857");
            iconFeature1 = new ol.Feature({
                geometry: new ol.geom.Point(m1)
            });
            var iconStyle1 = new ol.style.Style({
                image: new ol.style.Icon({
                    src: "/CCTV/img/coordinate.png",
                    color: [25, 250, 40, 1],
                    scale: 0.7
                })
            });
            iconFeature1.setStyle(iconStyle1);
        }
        var x2 = $("#ftable1 tr:eq(3) input[type=text]:eq(0)").val();
        var y2 = $("#ftable1 tr:eq(3) input[type=text]:eq(1)").val();
        if (x2 != 0.0 && y2 != 0.0) {
            m2 = ol.proj.transform([x2, y2], "EPSG:2326", "EPSG:3857");
            iconFeature2 = new ol.Feature({
                geometry: new ol.geom.Point(m2)
            });
            var iconStyle2 = new ol.style.Style({
                image: new ol.style.Icon({
                    src: "/CCTV/img/coordinate.png",
                    color: [216, 36, 10, 1],
                    scale: 0.7
                })
            });
            iconFeature2.setStyle(iconStyle2);
        }
        if (iconFeature1 != null)
            vectorSource.addFeature(iconFeature1);
        if (iconFeature2 != null)
            vectorSource.addFeature(iconFeature2);
        if (m1 != null && m2 != null) {
            var feature = new ol.Feature({
                geometry: new ol.geom.LineString([m1, m2])
            });
            feature.setStyle(new ol.style.Style({
                fill: new ol.style.Fill({
                    color: [255, 90, 10, 1],
                }),
                stroke: new ol.style.Stroke({
                    color: [255, 130, 20, 1],
                    width: 4
                })
            }));
            vectorSource.addFeature(feature);
        }
        var vectorLayer = new ol.layer.Vector({
            source: vectorSource
        });
        map.addLayer(vectorLayer);
        $("#map").show();
    }
    /********************************************************************/
    var iconStyle1 = new ol.style.Style({
        image: new ol.style.Icon({
            src: "/CCTV/img/coordinate.png",
            color: [240, 235, 40, 1],
            scale: 0.7
        })
    });
    //设置点击事件
    $("#map").on("click", function(event) {
        var coordinate = map.getEventCoordinate(event);
        var temp = ol.proj.transform(coordinate, "EPSG:3857", "EPSG:2326");
        $("#coordinate a").text(temp[0].toFixed(3) + "，" + temp[1].toFixed(3));
        var point = new ol.geom.Point(coordinate);
        if (tempFeature != null)
            vectorSource.removeFeature(tempFeature);
        tempFeature = new ol.Feature({
            geometry: point
        });
        tempFeature.setStyle(iconStyle1);
        vectorSource.addFeature(tempFeature);
    });
    $("#map").hide();
    /********************************************************************/
    /** 显示提示信息 */
    function showTips(text) {
        $("#Tip").show().delay(1800).hide(200);
        $("#Tip").text(text);
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
    /** Ajax上传文件 */
    function FileAjax(url, data) {
        var result = null;
        $.ajax({
            url:url,
            data:data,
            type:"post",
            async:false,
            datatype:"json",
            processData:false,
            contentType:false,
            success:function(data) {
                result = data;
            }
        });
        return result;
    }
});
