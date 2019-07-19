$(document).ready(function() {

    var list1 = ["Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"];
    /** ***************************************************************** */
    var charShow1 = new Highcharts.chart("show1",{
        chart: {
            spacing: [0, 0, 0, 0]
        },
        title: {
            text: "20%",
            floating: true
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: false
                }
            }
        },
        exporting: {
            enabled: false // 不显示导出按钮
        },
        credits: {
            enabled: false // 不显示官网链接
        },
        series: [{
            type: "pie",
            innerSize: "40%",  // 内心圆大小
            data: [{
                name: "已完成项目",
                y: 10.5,
                color: "#FC851D"
            }, {
                name: "未完成项目",
                y: 12.5,
                color: "#DADADA"
            }]
        }]
    },function(c) {
        var centerY = c.series[0].center[1];
        var titleHeight = parseInt(c.title.styles.fontSize);
        c.setTitle({
            y: centerY + titleHeight / 2
        });
    }
    );
    var charShow2 = new Highcharts.chart("show2",{
        chart: {
            spacing: [0, 0, 0, 0]
        },
        title: {
            text: "20%",
            floating: true
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: false
                }
            }
        },
        exporting: {
            enabled: false
        },
        credits: {
            enabled: false
        },
        series: [{
            type: "pie",
            innerSize: "40%",
            data: [{
                name: "已完成项目",
                y: 10.5,
                color: "#90ED7D"
            }, {
                name: "未完成项目",
                y: 12.5,
                color: "#DADADA"
            }]
        }]
    },function(c) {
        var centerY = c.series[0].center[1];
        var titleHeight = parseInt(c.title.styles.fontSize);
        c.setTitle({
            y: centerY + titleHeight / 2
        });
    }
    );
    /** ***************************************************************** */
    var charMain2 = new Highcharts.Chart({
        chart: {
            renderTo: "main2",
            type: "column"
        },
        title: {
            text: ""
        },
        xAxis: {
            lineWidth: 2,
            labels: {
                y: 20
            },
            categories: list1
        },
        yAxis: {
            min: 0,
            lineWidth: 2,
            title: {
                text: "数量"
            }
        },
        legend: {
            align: "right",
            layout: "vertical",
            verticalAlign: "middle"
        },
        series: [{
            name: "2018年",
            data: [10, 15, 26, 35, 10, 36, 12, 14, 15, 10, 14, 15]
        }, {
            name: "2019年",
            data: [15, 23, 25, 10, 20, 16, 18, 15, 16, 10, 12, 13]
        }],
        tooltip: {
            formatter: function() {
                return this.x + "：" + this.y + "个";
            }
        }
    });
    /** ***************************************************************** */
    var charMain4 = new Highcharts.Chart({
        chart: {
            renderTo: "main4",
        },
        title: {
            text: ""
        },
        xAxis: {
            lineWidth: 2,
            labels: {
                y: 20
            },
            categories: list1
        },
        yAxis: {
            min: 0,
            lineWidth: 2,
            title: {
                text: "数量"
            }
        },
        legend: {
            align: "right",
            layout: "vertical",
            verticalAlign: "middle"
        },
        series: [{
            name: "项目",
            data: [10, 15, 26, 35, 10, 36, 12, 14, 15, 10, 14, 15]
        }, {
            name: "记录",
            data: [15, 23, 25, 10, 20, 16, 18, 15, 16, 10, 12, 13]
        }],
        tooltip: {
            formatter: function() {
                // 格式化鼠标滑向图表数据点时显示的提示框
                return this.x + "：" + this.y + "分";
            }
        }
    });
    /** ***************************************************************** */
    Highcharts.chart("main5", {
        chart: {
            type: "variablepie"
        },
        title: {
            text: "项目分数占比"
        },
        tooltip: {
            formatter: function() {
                // 格式化鼠标滑向图表数据点时显示的提示框
                return this.y + "段";
            }
        },
        series: [{
            innerSize: "20%",
            minPointSize: 10,
            zMin: 0,
            data: [{
                name: "94分",
                y: 20,
                z: 100
            }, {
                name: "95分",
                y: 30,
                z: 100
            }, {
                name: "96分",
                y: 21,
                z: 100
            }, {
                name: "97分",
                y: 17,
                z: 100
            }, {
                name: "98分",
                y: 18,
                z: 100
            }, {
                name: "99分",
                y: 25,
                z: 100
            }, {
                name: "100分",
                y: 24,
                z: 100
            }]
        }]
    });
    /** ***************************************************************** */
});
