$(document).ready(function() {

	var role = $("#table1 tr:eq(2) td:eq(1)").text();
	if (role == "Role2")
		$("#table1 tr:eq(2) td:eq(1)").text("管理人员");
	else if (role == "Role3")
		$("#table1 tr:eq(2) td:eq(1)").text("评分人员");
	else if (role == "Role4")
		$("#table1 tr:eq(2) td:eq(1)").text("操作人员");
	// 计算百分比
	var count1 = $(".count:eq(0)").text();
	var count2 = $(".count:eq(1)").text();
	var count3 = $(".count:eq(2)").text();
	var count4 = $(".count:eq(3)").text();
	count1 = count1 == 0 ? 1 : count1;
	count3 = count3 == 0 ? 1 : count3;
	$("#bili1").text((count2 / count1).toFixed(2) * 100 + "%");
	$("#bili2").text((count4 / count3).toFixed(2) * 100 + "%");

	var id = $("#person").val();
	/** ***************************************************************** */
	var list1 = new Array();
	var list2 = new Array();
	var list3 = new Array();
	for (var i = 0; i < $("#title a").length; i = i + 3) {
		list1.push($("#title a").eq(i).text());
		var value1 = Number($("#title a").eq(i + 1).text()).toFixed(2);
		var value2 = Number($("#title a").eq(i + 2).text()).toFixed(2);
		list2.push(Number(value1));
		list3.push(Number(value2));
	}
	/** ***************************************************************** */
	var chart = new Highcharts.Chart({
		chart : {
			renderTo : "combox1" // 关联页面元素div的ID
		},
		title : {
			text : "项目分数曲线图"
		},
		xAxis : {
			lineWidth : 2,
			labels : {
				y : 20
			},
			categories : list1
		},
		yAxis : {
			min : 0,
			max : 100,
			lineWidth : 2,
			title : {
				text : "分数"
			}
		},
		legend : {
			align : "right",
			layout : "vertical",
			verticalAlign : "middle"
		},
		series : [ {
			name : "管线",
			data : list2
		}, {
			name : "记录",
			data : list3
		} ],
		tooltip : {
			formatter : function() { // 格式化鼠标滑向图表数据点时显示的提示框
				return this.x + "：" + this.y + "分";
			}
		}
	});
	/** ***************************************************************** */
});