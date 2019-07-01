$(document).ready(function() {
    $("#text").attr("placeholder", "请输入搜索地名");
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
    //初始化map
    var center = ol.proj.transform([840000, 820000], "EPSG:2326", "EPSG:3857");
    var view = new ol.View({
        center:center,
        zoom:10
    });
    //定义地图中心和缩放等级	
    var imap = new ol.layer.Tile({
        source: new ol.source.OSM(),
        name:"地图"
    });
    var map = new ol.Map({
        target:"showMap",
        layers:[imap],
        view:view
    });
    // 地图添加控件
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
    $("#showMap").bind("contextmenu", function() {
        return false;
    });
    /********************************************************************/
    var pipeID = -1;
    var tableContext1 = "";
    var tableContext2 = "";
    var tableContext3 = "";
    /********************************************************************/
    /** 加载项目数据 */
    var sourcePipe = new ol.layer.Image({
    	name: "管线数据图层",
        source: new ol.source.ImageWMS({
        	url: "http://192.168.0.103:8080/geoserver/cite/wms",
            ratio: 1,
            params: {
                LAYERS: "cite:geompipe",
                VERSION: "1.1.0"
            },
            serverType: "geoserver"
        }),
        //maxResolution:0.3,
        zIndex: 6
        
    });
    
    map.addLayer(sourcePipe);
    /********************************************************************/
    //显示查询结果地图标记
    var rsStyle = new ol.style.Style({
        image: new ol.style.Icon({
            src: "/CCTV/img/coordinate.png",
            color: [245, 68, 45, 1],
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
        var lon = Number(centers[0]);
        var lat = Number(centers[1]);
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
    // var data = Ajax("https://nominatim.openstreetmap.org/search.php", null);
    $("#text").keydown(function() {
        if (event.keyCode == 13)
            $("#find").click();
    });
    $("#find").click(function() {
        var city = $("#text").val();
        if (city.trim() == "")
            return false;
        var url = "https://nominatim.openstreetmap.org/search.php";
        var param = {"format": "json", "addressdetails": 1, "zoom": 18, "q": city};
        $.getJSON(url, param, function(data) {
            var context = "";
            for (var i = 0; i < data.length; i++) {
                var point = data[i].lon + "," + data[i].lat;
                var itext = data[i].display_name.substring(0, 25);
                context += "<div class='"+point+"'><img src='/CCTV/img/查看.png'>" + itext + "</div>";
            }
            $("#showplace").html(context);
            $("#showplace div").each(function() {
                $(this).unbind();
                var centers = $(this).attr("class").split(",");
                var lon = Number(centers[0]);
                var lat = Number(centers[1]);
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
        $("#showplace table a:eq(0)").click();
        $("#result").show();
    });
    $("#btn1").click(function() {
        $("#result").hide();
        $("#text").val("");
    });
    /********************************************************************/
    //导航条控制按钮
    $(".locaBtn:eq(0)").attr("disabled", true);
    $(".locaBtn:eq(0)").css("background-color", "#CCC");
    $(".locaBtn:eq(0)").click(function() {
        $("input[name*=loacltion]").val("");
        $(this).attr("disabled", true);
        $(this).css("background-color", "#CCC");
        hideIcon();
    });
    $(".locaBtn:eq(1)").click(function() {
        var lon = $("input[name=loacltion1]").val();
        var lat = $("input[name=loacltion2]").val();
        if (lon == "" || isNaN(lon)) {
            $("input[name=loacltion1]").css("background-color", "#ff4400");
            showTips("请输入正确坐标值!");
            return false;
        }
        if (lat == "" || isNaN(lat)) {
            $("input[name=loacltion2]").css("background-color", "#ff4400");
            showTips("请输入正确坐标值!");
            return false;
        }
        $(".locaBtn:eq(0)").attr("disabled", false);
        $(".locaBtn:eq(0)").css("background-color", "#FF711D");
        showIcon(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
        view.setCenter(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
        view.setZoom(12);
    });
    $("input[name*=loacltion]").focus(function() {
        $(this).css("background-color", "#ffffff");
    });
    /********************************************************************/
    // 控制显示和隐藏图层
    $("#checkbox1").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(geomproject);
        else
            map.removeLayer(geomproject);
    });
    $("#checkbox2").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(sourcePipe);
        else
            map.removeLayer(sourcePipe);
    });
    $("#checkbox3").change(function() {
        if ($(this).prop("checked"))
            map.addLayer(sourceItem);
        else
            map.removeLayer(sourceItem);
    });
    /********************************************************************/
    // 项目列表初始化
    $("input[name=btn]").click(function(){
    	var name = $("input[name=name]").val();
    	var option = $("select[name=option]").val();
    	window.location.href = "showmap?name=" + name + "&option=" + option;
    });
    $("#resultlist div").mouseenter(function() {
//        var center = $(this).attr("class").split(" ");
//        var lon = Number(center[0]);
//        var lat = Number(center[1]);
//        showIcon(ol.proj.transform([lon, lat], "EPSG:2326", "EPSG:3857"));
    });
    $("#resultlist div").mouseleave(function() {
        hideIcon();
    });
    $("#resultlist div").click(function() {
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
        SelectFeature1.setGeometry(null);
        var view = map.getView();
        /****************************************************************/
        var source = sourcePipe.getSource();
        var url = source.getGetFeatureInfoUrl(event.coordinate, view.getResolution(), view.getProjection(), {
            "INFO_FORMAT": "application/json",
            "FEATURE_COUNT": 50
        });
        console.log(url);
        $.ajax({
            url:url,
            data:null,
            type:"get",
            dataType:'json',
            jsonp:"callback",
            jsonpCallback:"jsonpHandler",
            dataFilter:function(json){    
                console.log("jsonp.filter:"+json);    
                return json;    
            },    
            success:function(data) {
            	console.log(data);
                // result = data;
            },
            error:function(mes) {
            	console.log(mes);
            }
        });
        function jsonpHandler(data) {
        	console.log(data);
        }
        //var data = Ajax(url, null);
        // console.log(data);
//        if (data.features.length != 0) {
//            var ilist = new Array();
//            var coordinates = data.features[0].geometry.coordinates[0];
//            for (var i = 0; i < coordinates.length; i++)
//                ilist.push(ol.proj.transform(coordinates[i], "EPSG:2326", "EPSG:3857"));
//            var polygon = new ol.geom.Polygon([ilist]);
//            SelectFeature1.setGeometry(polygon);
//            vectorLayer.setZIndex(4);
//
//            var id = data.features[0].properties.projectid;
//            var result = Ajax("/CCTV/project/showmap", {id: id});
//            tableContext1 += "<tr><th colspan='2'>项目信息</th></tr>";
//            tableContext1 += "<tr><td width='32%'>项目名称</td><td width='68%'>" + result.name + "</td></tr>";
//            tableContext1 += "<tr><td>公司名称</td><td>" + result.client + "</td></tr>";
//            tableContext1 += "<tr><td>标准</td><td>" + result.standard + "</td></tr>";
//            tableContext1 += "<tr><td>斜坡</td><td>" + result.slope + "</td></tr>";
//            tableContext1 += "<tr><td>操作人员</td><td>" + result.operator + "</td></tr>";
//            tableContext1 += "<tr><td>创建日期</td><td>" + result.date + "</td></tr>";
//        }
        /****************************************************************/
       
    }
    //绑定点击事件
    map.on("click", function(event) {
        mapClick1(event);
    });
    /********************************************************************/
    $("#QueryBox .btn1").click(function() {
        window.location.href = "showmap";
    });
    //高级查询
    
    /********************************************************************/
    //面板1配置
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
    function openCompare() {
        window.open("/CCTV/geomproject/compare?id=" + pipeID);
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
            dataType:"jsonp",
            jsonpCallback:"jsonpHandler",
            success:function(data) {
            	console.log(data);
                // result = data;
            }
        });
        return result;
    }
    /********************************************************************/
});
