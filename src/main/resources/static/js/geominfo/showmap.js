$(document).ready(function() {
	
    $("#text").attr("placeholder", "请输入搜索地名");
    $(".location:eq(0)").attr("placeholder", "输入查询X坐标");
    $(".location:eq(1)").attr("placeholder", "输入查询Y坐标");
    /********************************************************************/
    /** 定义坐标转换 */
    var projection = new ol.proj.Projection({
        code: "EPSG:2326",
        extent: [793259.70, 799130.01, 870525.78, 848940.16],
        axisOrientation: "neu",
        units: "m",
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
    //初始化map
    var center = ol.proj.transform([840000, 820000], "EPSG:2326", "EPSG:3857");
    var view = new ol.View({
        center: center,
        zoom: 10
    });
    //定义地图中心和缩放等级	
    var imap = new ol.layer.Tile({
        source: new ol.source.OSM(),
        name: "地图"
    });
    var map = new ol.Map({
        target: "map",
        layers: [imap],
        view: view
    });
    map.addControl(new ol.control.Attribution());
    map.addControl(new ol.control.ScaleLine());
    map.addControl(new ol.control.MousePosition({
        undefinedHTML: "",
        projection: "EPSG:2326",
        coordinateFormat: function(coordinate) {
            return ol.coordinate.format(coordinate, "{x},{y}", 3);
        }
    }));
  //移除默认右击事件
    $("#map").bind("contextmenu", function() {
        return false;
    });
    $("#map").bind("mousedown", function(event) {
        if (event.which == 3) {
            var coordinate = map.getEventCoordinate(event);
            var temp = ol.proj.transform(coordinate, "EPSG:3857", "EPSG:2326");
            $("#coordinate a").text(temp[0].toFixed(3) + "，" + temp[1].toFixed(3));
        }
    });
    /********************************************************************/
    var pipeID = -1;
    var tableContext1 = "";
    var tableContext2 = "";
    var tableContext3 = "";
    /********************************************************************/
    /** 加载项目数据 */
    var geomproject = new ol.layer.Image({
    	name: "项目数据图层",
        source: new ol.source.ImageWMS({
            ratio: 1,
            url: "http://127.0.0.1:8080/geoserver/IDMS/wms",
            params: {
                LAYERS: "IDMS:geomproject",
                VERSION: "1.1.0"
            },
            serverType: "geoserver"
        }),
        minResolution: 0.2,
        zIndex: 3
        
    });
    var sourcePipe = new ol.layer.Image({
    	name: "管线数据图层",
        source: new ol.source.ImageWMS({
        	url: "http://127.0.0.1:8080/geoserver/cite/wms",
            ratio: 1,
            params: {
                LAYERS: "cite:pipegeom",
                VERSION: "1.1.0"
            },
            serverType: "geoserver"
        }),
        //maxResolution:0.3,
        zIndex: 6
        
    });
    var sourceItem = new ol.layer.Image({
    	name: "记录数据图层",
        source: new ol.source.ImageWMS({
            ratio: 1,
            url: "http://127.0.0.1:8080/geoserver/cite/wms",
            params: {
                LAYERS: "IDMS:itemgeom",
                VERSION: "1.1.0"
            },
            serverType: "geoserver"
        }),
        zIndex: 9
        //maxResolution:5.0,
        
    });
    //map.addLayer(geomproject);
    map.addLayer(sourcePipe);
   // map.addLayer(sourceItem);
    /********************************************************************/
    //显示查询结果地图标记
    var rsStyle = new ol.style.Style({
        image: new ol.style.Icon({
            src: "/CCTV/img/coordinate.png",
            color:[245,68,45,1],
            scale: 0.7
        })
    });
    var rsSource = new ol.source.Vector();
    var rsLayer = new ol.layer.Vector();
    rsLayer.setSource(rsSource);
    map.addLayer(rsLayer);
    //查询数据添加标记
    $("#datalist a").each(function() {
        var centers = $(this).text().split("，");
        var lon = Number(centers[0])
          , lat = Number(centers[1]);
        var center = ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857");
        var rsFeature = new ol.Feature();
        var rsPoint = new ol.geom.Point(center);
        rsFeature.setGeometry(rsPoint);
        rsFeature.setStyle(rsStyle);
        rsSource.addFeature(rsFeature);
    });
    /********************************************************************/
    /** 添加地图标记 */
    var iconStyle1 = new ol.style.Style({
        image: new ol.style.Icon({
            src: "/CCTV/img/coordinate.png",
            color: [25, 250, 40, 1],
            scale: 0.7
        })
    });
    var iconFeature = new ol.Feature();
    var iconVector = new ol.source.Vector();
    var vectorLayer = new ol.layer.Vector();
    iconFeature.setStyle(iconStyle1);
    iconVector.addFeature(iconFeature);
    vectorLayer.setSource(iconVector);
    map.addLayer(vectorLayer);
    function showIcon(coord) {
        var point = new ol.geom.Point(coord);
        iconFeature.setGeometry(point);
        vectorLayer.setZIndex(10);
    }
    function hideIcon() {
        iconFeature.setGeometry(null);
    }
    /********************************************************************/
    /** 测量距离 */
    var source = new ol.source.Vector();
    var vector = new ol.layer.Vector({
        source: source,
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: "#ffffff"
            }),
            stroke: new ol.style.Stroke({
                color: "#FF3300",
                width: 3
            }),
            image: new ol.style.Circle({
                fill: new ol.style.Fill({
                    color: "#FFCC33"
                }),
                radius: 5
            })
        })
    });
    map.addLayer(vector);
    var draw = new ol.interaction.Draw({
        source: source,
        type: "LineString",
        style: new ol.style.Style({
            fill: new ol.style.Fill({
                color: "rgba(255,255,255,0.2)"
            }),
            stroke: new ol.style.Stroke({
                color: "rgba(0,0,0,0.5)",
                lineDash: [10, 10],
                width: 2
            })
        })
    });
    //设置开始绘制事件
    draw.on("drawstart", function(event) {
        var geometry = event.feature.getGeometry();
        var toolTip = createTooltip();
        var listener = geometry.on("change", function(event) {
            var geom = event.target;
            var result = formatLength(geom);
            toolTip.getElement().innerHTML = result;
            tooltipCoord = geom.getLastCoordinate();
            toolTip.setPosition(tooltipCoord);
        });
    }, this);
    /********************************************************************/
    var data = Ajax("https://nominatim.openstreetmap.org/search.php", null);
    $("#text").keydown(function() {
        if (event.keyCode == 13)
            $("#find").click();
    });
    $("#find").click(function() {
        var city = $("#text").val();
        if (city.trim() == "")
            return false;
        var url = "https://nominatim.openstreetmap.org/search.php";
        var param = {
            "format": "json",
            "zoom": 18,
            "addressdetails": 1,
            "q": city
        };
        $.getJSON(url, param, function(data) {
            var context = "";
            for (var i = 0; i < data.length; i++) {
                var point = data[i].lon + "," + data[i].lat;
                context += "<tr>";
                context += "  <td class='colspan1'>" + initType(data[i].type) + "</td>";
                context += "  <td class='colspan2'><a name='" + point + "'>" + data[i].display_name + "</a></td>";
                context += "</tr>";
            }
            $("#showplace table").html(context);
            $("#showplace table a").each(function() {
                $(this).unbind();
                var centers = $(this).attr("name").split(",");
                var lon = Number(centers[0])
                  , lat = Number(centers[1]);
                var center = ol.proj.transform([lon, lat], "EPSG:4326", "EPSG:3857");
                $(this).click(function() {
                    view.setCenter(center);
                    view.setZoom(12);
                });
                $(this).mouseenter(function() {
                    showIcon(center);
                });
                $(this).mouseleave(function() {
                    hideIcon();
                });
            });
        });
        $("#result").show();
        $("#showplace table a:eq(0)").click();
    });
    $("#colse,#colseResult").click(function() {
        $("#text").val("");
        $("#result").hide();
    });
    /********************************************************************/
    //导航条控制按钮
    $("#navigbtn").click(function() {
        if ($(this).attr("value") == "+") {
            $(this).attr("value", "-");
            $("#navigation").show();
        } else {
            $(this).attr("value", "+");
            $("#navigation").hide();
        }
    });
    //高级查询
    $("#senior,#seniorBox").mouseenter(function() {
        $("#seniorBox").show();
    });
    $("#senior,#seniorBox").mouseleave(function() {
        $("#seniorBox").hide();
    });
    $("#navigation input[type=checkbox]").change(function() {
        //流向方向
        if ($(this).prop("checked"))
            showTips("显示流向图层!");
        else
            showTips("隐藏流向图层!");
    });
    $(".locaBtn:eq(0)").attr("disabled", true);
    $(".locaBtn:eq(0)").css("background-color", "#CCC");
    $(".locaBtn:eq(0)").click(function() {
        $(".location").val("");
        $(this).attr("disabled", true);
        $(this).css("background-color", "#CCC");
        hideIcon();
    });
    $(".locaBtn:eq(1)").click(function() {
        var lon = $(".location:eq(0)").val();
        var lat = $(".location:eq(1)").val();
        if (lon == "" || isNaN(lon)) {
            $(".location:eq(0)").css("background-color", "#ff4400");
            showTips("请输入正确坐标值!");
            return false;
        }
        if (lat == "" || isNaN(lat)) {
            $(".location:eq(1)").css("background-color", "#ff4400");
            showTips("请输入正确坐标值!");
            return false;
        }
        $(".locaBtn:eq(0)").attr("disabled", false);
        $(".locaBtn:eq(0)").css("background-color", "#FF711D");
        showIcon(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
        view.setCenter(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
        view.setZoom(12);
    });
    $(".location").focus(function() {
        $(this).css("background-color", "#ffffff");
    });
    /********************************************************************/
    var SelectStyle = new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: "#01B1EC",
            width: 4
        }),
        image: new ol.style.Circle({
            fill: new ol.style.Fill({
                color: "#01B1EC"
            }),
            radius: 5
        })
    });
    var SelectFeature1 = new ol.Feature();
    SelectFeature1.setStyle(SelectStyle);
    iconVector.addFeature(SelectFeature1);
    /********************************************************************/
    function mapClick1(event) {
        pipeID = -1;
        tableContext1 = "";
        tableContext2 = "";
        tableContext3 = "";
        SelectFeature1.setGeometry(null);
        var view = map.getView();
        /****************************************************************/
        var source = geomproject.getSource();
        var url = source.getGetFeatureInfoUrl(event.coordinate, view.getResolution(), view.getProjection(), {
            "INFO_FORMAT": "application/json",
            "FEATURE_COUNT": 50
        });
        var data = Ajax(url, null);
        if (data.features.length != 0) {
            var ilist = new Array();
            var coordinates = data.features[0].geometry.coordinates[0];
            for (var i = 0; i < coordinates.length; i++)
                ilist.push(ol.proj.transform(coordinates[i], "EPSG:2326", "EPSG:3857"));
            var polygon = new ol.geom.Polygon([ilist]);
            SelectFeature1.setGeometry(polygon);
            vectorLayer.setZIndex(4);

            var id = data.features[0].properties.projectid;
            var result = Ajax("/CCTV/project/showmap", {id: id});
            tableContext1 += "<tr><th colspan='2'>项目信息</th></tr>";
            tableContext1 += "<tr><td width='32%'>项目名称</td><td width='68%'>" + result.name + "</td></tr>";
            tableContext1 += "<tr><td>公司名称</td><td>" + result.client + "</td></tr>";
            tableContext1 += "<tr><td>标准</td><td>" + result.standard + "</td></tr>";
            tableContext1 += "<tr><td>斜坡</td><td>" + result.slope + "</td></tr>";
            tableContext1 += "<tr><td>操作人员</td><td>" + result.operator + "</td></tr>";
            tableContext1 += "<tr><td>创建日期</td><td>" + result.date + "</td></tr>";
        }
        /****************************************************************/
        //查询管道信息 
        var source = sourcePipe.getSource();
        var url = source.getGetFeatureInfoUrl(event.coordinate, view.getResolution(), view.getProjection(), {
            "INFO_FORMAT": "application/json",
            "FEATURE_COUNT": 50
        });
        var data = Ajax(url, null);
        if (data.features.length != 0) {
            var center1 = ol.proj.transform(data.features[0].geometry.coordinates[0], "EPSG:2326", "EPSG:3857");
            var center2 = ol.proj.transform(data.features[0].geometry.coordinates[1], "EPSG:2326", "EPSG:3857");
            var line = new ol.geom.LineString([center1, center2]);
            //根据鼠标点击位置生成一个点
            SelectFeature1.setGeometry(line);
            vectorLayer.setZIndex(7);

            var id = data.features[0].properties.pipeid;
            var result = Ajax("/CCTV/pipe/showmap", {id: id});
            tableContext2 += "<tr><th colspan='2'>管道信息</th></tr>";
            tableContext2 += "<tr><td width='32%'>起始井号</td><td width='68%'>" + result.smanholeno + "</td></tr>";
            tableContext2 += "<tr><td>终止井号</td><td>" + result.fmanholeno + "</td></tr>";
            tableContext2 += "<tr><td>管道通途</td><td>" + result.use + "</td></tr>";
            tableContext2 += "<tr><td>管道流向</td><td>" + result.direction + "</td></tr>";
            tableContext2 += "<tr><td>管道形状</td><td>" + result.shape + "</td></tr>";
            tableContext2 += "<tr><td>管道材质</td><td>" + result.material + "</td></tr>";
            tableContext2 += "<tr><td>管道大小</td><td>" + result.hsize + "</td></tr>";
            tableContext2 += "<tr><td>管道长度</td><td>" + result.totallength + "</td></tr>";
            pipeID = id;
            //设置管道对比按钮
            if ($("#infoTitle").val() == 2) {
                $("#link").css("background-color", "#FFF");
                $("#link").bind("click", openCompare);
            }
        }
        /****************************************************************/
        //查询记录信息
        var source = sourceItem.getSource();
        var url = source.getGetFeatureInfoUrl(event.coordinate, view.getResolution(), view.getProjection(), {
            "INFO_FORMAT": "application/json",
            "FEATURE_COUNT": 50
        });
        var data = Ajax(url, null);
        if (data.features.length != 0) {
            var center = ol.proj.transform(data.features[0].geometry.coordinates, "EPSG:2326", "EPSG:3857");
            var point = new ol.geom.Point(center);
            SelectFeature1.setGeometry(point);
            vectorLayer.setZIndex(10);

            var id = data.features[0].properties.itemid;
            var result = Ajax("/CCTV/item/showmap", {id: id});
            tableContext3 += "<tr><th colspan='2'>记录信息</th></tr>";
            tableContext3 += "<tr><td width='32%'>位置</td><td width='68%'>" + result.dist + "</td></tr>";
            tableContext3 += "<tr><td>代码</td><td>" + result.code + "</td></tr>";
            tableContext3 += "<tr><td>备注</td><td>" + result.remarks + "</td></tr>";
            tableContext3 += "<tr><td>图片</td><td><a class='" + result.picture + "'>" + result.photo + "</a></td></tr>";
            tableContext3 += "<tr><td>Clock At</td><td>" + result.clockAt + "</td></tr>";
            tableContext3 += "<tr><td>Clock To</td><td>" + result.clockTo + "</td></tr>";
            tableContext3 += "<tr><td>等级</td><td>" + result.grade + "</td></tr>";
            tableContext3 += "<tr><td>分数</td><td>" + result.score + "</td></tr>";
        }
        /****************************************************************/
        var html = "暂无数据";
        if (tableContext3 != "") {
            html = "<table>" + tableContext3 + "</table>";
            $("#infoTitle").val(3);
        } else if (tableContext2 != "") {
            html = "<table>" + tableContext2 + "</table>";
            $("#infoTitle").val(2);
        } else if (tableContext1 != "") {
            html = "<table>" + tableContext1 + "</table>";
            $("#infoTitle").val(1);
        }
        $("#infoList").html(html);

        var path = "http://127.0.0.1:8080/ItemImage/";
        /** 设置显示图片 */
        $("#infoList table a").attr("href", "javascript:void(0)");
        $("#infoList table a").mouseenter(function(e) {
            var path = $(this).attr("class");
            $("#showImg img").attr("src", path + path + ".png");
            $("#showImg").css({
                "left": -60,
                "top": -40
            });
            $("#showImg").show();
        });
        $("#infoList table a").mouseleave(function() {
            //鼠标离开事件
            $("#showImg").hide();
        });
    }
    function mapClick2(event) {
        var point = new ol.geom.Point(event.coordinate);
        source.addFeature(new ol.Feature(point));
    }
    map.on("click", function(event) {
        //绑定点击事件
        mapClick1(event);
    });
    /********************************************************************/
    var type = true;
    $("#rule").click(function() {
        //测量距离
        map.removeEventListener("click");
        //移除地图单击事件
        /****************************************************************/
        if (type == true) {
            map.addInteraction(draw);
            $("#map").css("cursor", "crosshair");
            $(this).css("background-color", "#CCC");
            map.on("click", function(event) {
                mapClick2(event);
            });
        } else {
            source.clear();
            $(".tooltip").remove();
            map.removeInteraction(draw);
            $("#map").css("cursor", "default");
            $(this).css("background-color", "#FFF");
            map.on("click", function(event) {
                mapClick1(event);
            });
        }
        type = !type;
    });
    /********************************************************************/
    if ($("#QueryBox input[type=text]:eq(0)").val() == "") {
        $("#QueryBox input[type=button]:eq(0)").attr("disabled", "true");
        $("#QueryBox input[type=button]:eq(0)").css("color", "#999")
        $("#QueryBox input[type=button]:eq(0)").css("background-color", "#CCC")
    }
    if ($("#QueryBox input[type=text]:eq(1)").val() == "") {
        $("#QueryBox input[type=button]:eq(2)").attr("disabled", "true");
        $("#QueryBox input[type=button]:eq(2)").css("color", "#999")
        $("#QueryBox input[type=button]:eq(2)").css("background-color", "#CCC")
    }
    if ($("#QueryBox input[type=text]:eq(2)").val() == "") {
        $("#QueryBox input[type=button]:eq(4)").attr("disabled", "true");
        $("#QueryBox input[type=button]:eq(4)").css("color", "#999")
        $("#QueryBox input[type=button]:eq(4)").css("background-color", "#CCC")
    }
    $("#QueryBox .btn1").click(function() {
        window.location.href = "showmap";
    });
    //高级查询
    $("#QueryBox input[type=button]:eq(1)").click(function() {
        //名称查询
        var value = $("#QueryBox input[type=text]:eq(0)").val();
        if (value.trim() == "")
            showTips("请输入查询条件!");
        else
            window.location.href = "showmapquery1?name1=" + value;
    });
    $("#QueryBox input[type=button]:eq(3)").click(function() {
        //种类查询
        var value = $("#QueryBox input[type=text]:eq(1)").val();
        if (value.trim() == "")
            showTips("请输入查询条件!");
        else
            window.location.href = "showmapquery2?name2=" + value;
    });
    $("#QueryBox input[type=button]:eq(5)").click(function() {
        //材质查询
        var value = $("#QueryBox input[type=text]:eq(2)").val();
        if (value.trim() == "")
            showTips("请输入查询条件!");
        else
            window.location.href = "showmapquery3?name3=" + value;
    });
    /********************************************************************/
    $("#showBtn").click(function() {
        var value = $(this).val();
        if (value == "显示") {
            $("#showBox").show();
            $(this).val("隐藏");
        } else {
            $("#showBox").hide();
            $(this).val("显示");
        }
    });
    $(".title").each(function(i) {
        $(this).click(function() {
            $(".title").css("color", "#000");
            $(this).css("color", "#ff4400");
            $(".panel").hide();
            $(".panel").eq(i).show();
        });
    });
    $(".title:eq(0)").click();
    /********************************************************************/
    //面板1配置
    $("input[name=data]:eq(0)").change(function() {
        showTips("数据图层切换完成!");
        if ($(this).prop("checked")) {
            $("#checkbox1").attr("disabled", false);
            $("#checkbox2").attr("disabled", false);
            $("#checkbox3").attr("disabled", false);
            $("#checkbox1").attr("checked", true);
            $("#checkbox2").attr("checked", true);
            $("#checkbox3").attr("checked", true);
            $("#checkbox4").attr("checked", false);
            $("#checkbox5").attr("checked", false);
            $("#checkbox6").attr("checked", false);
        } else {
            $("#checkbox1").attr("disabled", true);
            $("#checkbox2").attr("disabled", true);
            $("#checkbox3").attr("disabled", true);
        }
    });
    $("input[name=data]:eq(1)").change(function() {
        showTips("数据图层切换完成!");
        if ($(this).prop("checked")) {
            $("#checkbox4").attr("disabled", false);
            $("#checkbox5").attr("disabled", false);
            $("#checkbox6").attr("disabled", false);
            $("#checkbox1").attr("checked", false);
            $("#checkbox2").attr("checked", false);
            $("#checkbox3").attr("checked", false);
            $("#checkbox4").attr("checked", true);
            $("#checkbox5").attr("checked", true);
            $("#checkbox6").attr("checked", true);
        } else {
            $("#checkbox4").attr("disabled", true);
            $("#checkbox5").attr("disabled", true);
            $("#checkbox6").attr("disabled", true);
        }
    });
    $("#checkbox1,#checkbox4").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(geomproject);
        else
            map.removeLayer(geomproject);
    });
    $("#checkbox2,#checkbox5").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(sourcePipe);
        else
            map.removeLayer(sourcePipe);
    });
    $("#checkbox3,#checkbox6").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(sourceItem);
        else
            map.removeLayer(sourceItem);
    });
    /********************************************************************/
    var context = "";
    $("#nums option").each(function(i) {
        var text = $(this).text();
        var id = $(this).attr("id");
        var type = $(this).attr("class");
        context += "<div id='" + id + "' class='" + type + "'>" + text + "</div>";
        $(this).val($(this).text());
    });
    $("#resultList").html(context);
    initResultList();
    $("#resultText").on("input propertychange", function() {
        var value = $(this).val();
        if (value.trim() == "")
            $("#resultList").html(context);
        else {
            var temptext = "";
            $("#nums option[value*=" + value + "]").each(function(i) {
                var text = $(this).text();
                var id = $(this).attr("id");
                var type = $(this).attr("class");
                temptext += "<div id='" + id + "' class='" + type + "'>" + text + "</div>";
            });
            $("#resultList").html(temptext);
        }
        $("#resultList div").each(function() {
            var text = $(this).text();
            var exp = new RegExp(value,"gm")
            var cont = text.replace(exp, "<font color='#f00'>" + value + "</font>");
            $(this).html(cont);
        });
        initResultList();
    });
    /** 初始化项目列表 */
    function initResultList() {
        $("#resultList div").unbind();
        $("#resultList div").mouseenter(function() {
            var center = $(this).attr("class").split(" ");
            var lon = Number(center[0])
              , lat = Number(center[1]);
            showIcon(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
        });
        $("#resultList div").mouseleave(function() {
            hideIcon();
        });
        $("#resultList div").click(function() {
            var text = $(this).attr("id");
            var ilist = new Array();
            var datalist = text.split(", ");
            for (var i = 0; i < datalist.length; i++) {
                var list = datalist[i].split(" ");
                ilist.push(ol.proj.transform([list[0], list[1]], "EPSG:2326", "EPSG:3857"));
            }
            var polygon = new ol.geom.Polygon([ilist]);
            SelectFeature1.setGeometry(polygon);
            vectorLayer.setZIndex(4);
            var center = $(this).attr("class").split(" ");
            var lon = Number(center[0])
              , lat = Number(center[1]);
            view.setCenter(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
            view.setZoom(12);
        });
    }
    /********************************************************************/
    //面板3配置
    var path = "127.0.0.1:8080/ItemImage";
    $("#infoTitle").change(function() {
        var value = $(this).val();
        if ($(this).val() == 1) {
            $("#link").unbind("click");
            $("#link").css("background-color", "#CCC");
            var html = "<table>" + tableContext1 + "</table>";
            $("#infoList").html(html);
        } else if ($(this).val() == 2) {
            var html = "<table>" + tableContext2 + "</table>";
            $("#infoList").html(html);
            if (pipeID != -1) {
                $("#link").bind("click", openCompare);
                $("#link").css("background-color", "#FFF");
            }
        } else if ($(this).val() == 3) {
            $("#link").unbind("click");
            $("#link").css("background-color", "#CCC");
            var html = "<table>" + tableContext3 + "</table>";
            $("#infoList").html(html);
            $("#infoList table a").attr("href", "javascript:void(0)");
            $("#infoList table a").mouseenter(function(e) {
                var name = $(this).attr("class");
                $("#showImg img").attr("src", path + name+ ".png");
                $("#showImg").css({
                    "left": -60,
                    "top": -40
                });
                $("#showImg").show();
            });
            $("#infoList table a").mouseleave(function() {
                $("#showImg").hide();
            });
        }
    });
    function openCompare() {
        window.open("/CCTV/geomproject/compare?id=" + pipeID);
    }
    /********************************************************************/
    function createTooltip() {
        var toolTipElement = document.createElement("div");
        toolTipElement.className = "tooltip";
        var toolTip = new ol.Overlay({
            element: toolTipElement,
            positioning: "bottom-center",
            offset: [0, -10]
        });
        map.addOverlay(toolTip);
        return toolTip;
    }
    function formatLength(line) {
        var length = Math.round(line.getLength() * 100) / 100;
        if (length > 1000)
            return (Math.round(length / 1000 * 100) / 100) + "km";
        else
            return (Math.round(length * 100) / 100) + "m";
    }
    /********************************************************************/
    function initType(type) {
        var result = "地区";
        if (type == "administrative")
            result = "省份";
        else if (type == "city")
            result = "城市";
        else if (type == "house")
            result = "建筑";
        else if (type == "station")
            result = "火车站";
        else if (type == "stop")
            result = "停车点";
        else if (type == "pub")
            result = "酒馆";
        else if (type == "restaurant")
            result = "餐馆";
        else if (type == "town")
            result = "城镇";
        else if (type == "village" || type == "hamlet")
            result = "村庄";
        return result + "&nbsp;";
    }
    /********************************************************************/
    /** 显示提示信息 */
    function showTips(text) {
        $("#Tip").text(text);
        $("#Tip").show().delay(1800).hide(200);
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
    /********************************************************************/
});
