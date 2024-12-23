var dom10 = document.getElementById("box4");
var myChart101 = echarts.init(dom10);

var option10 = {
	legend: {
		textStyle: {
			color: '#fff',
			fontSize: 12
		},
		data: ['目前各类总闲置', '目前各类总求购', '7日内的新增闲置', '7日内的新增求购']
	},
	radar: [{
			indicator: [{
					text: '书籍'
				},
				{
					text: '电子数码'
				},
				{
					text: '运动户外'
				},
				{
					text: '生活用品'
				},
				{
					text: '食品'
				},
				{
					text: '其他'
				}
			],
			center: ['25%', '50%'],
			radius: 120,
			startAngle: 90,
			splitNumber: 4,
			shape: 'circle',
			name: {
				formatter: '【{value}】',
				textStyle: {
					color: '#72ACD1'
				}
			},
			splitArea: {
				areaStyle: {
					color: [
						'rgba(114, 172, 209, 0.2)',
						'rgba(114, 172, 209, 0.4)',
						'rgba(114, 172, 209, 0.6)',
						'rgba(114, 172, 209, 0.8)',
						'rgba(114, 172, 209, 1)',
						'rgba(114, 172, 209, 0.2)'
					],
					shadowColor: 'rgba(0, 0, 0, 0.3)',
					shadowBlur: 10
				}
			},
			axisLine: {
				lineStyle: {
					color: 'rgba(255, 255, 255, 0.5)'
				}
			},
			splitLine: {
				lineStyle: {
					color: 'rgba(255, 255, 255, 0.5)'
				}
			}
		},
		{
			indicator: [{
					text: '书籍'
				},
				{
					text: '电子数码'
				},
				{
					text: '运动户外'
				},
				{
					text: '生活用品'
				},
				{
					text: '食品'
				},
				{
					text: '其他'
				}
			],
			center: ['75%', '50%'],
			radius: 120
		}
	],
	series: [{
			name: '目前总共',
			type: 'radar',
			itemStyle: {
				emphasis: {
					// color: 各异,
					lineStyle: {
						width: 4
					}
				}
			},
			data: [{
					value: [100, 8, 0.40, -80, 2000, 2],
					name: '目前各类总闲置',
					symbol: 'rect',
					symbolSize: 6,
					lineStyle: {
						normal: {
							type: 'dashed'
						}
					}
				},
				{
					value: [60, 5, 0.30, -100, 1500, 9],
					name: '目前各类总求购',
					areaStyle: {
						normal: {
							color: 'rgba(255, 255, 255, 0.5)'
						}
					}
				}
			]
		},
		{
			name: '近7天',
			type: 'radar',
			radarIndex: 1,
			data: [{
					value: [120, 118, 130, 100, 99, 70],
					name: '7日内的新增闲置',
					label: {
						normal: {
							show: true,
							formatter: function(params) {
								return params.value;
							}
						}
					}
				},
				{
					value: [90, 113, 140, 30, 70, 60],
					name: '7日内的新增求购',
					areaStyle: {
						normal: {
							opacity: 0.9,
							color: new echarts.graphic.RadialGradient(0.5, 0.5, 1, [{
									color: '#B8D3E4',
									offset: 0
								},
								{
									color: '#72ACD1',
									offset: 1
								}
							])
						}
					}
				}
			]
		}
	]
};
var series1 = [
	[]
];
// 请求数据并更新图表
function fetchData101() {
	return fetch('http://localhost:8016/BigScreen/box4Data')
		.then(response => response.json())
		.then(data => {
			console.log(data);
			productlist = data.result;
			console.log(option10.series[0]);
			console.log(option10.series[0].data[0].value);
			option10.series[0].data[0].value = productlist.allxianzhi;
			option10.series[0].data[1].value = productlist.allxuqiu;
			option10.series[1].data[0].value = productlist.xianzhi7;
			option10.series[1].data[1].value = productlist.xuqiu7;
			console.log(productlist.allxianzhi);
		})
		.catch(error => {
			console.error('请求失败', error);
		});
}


// 初始化图表并加载数据
if (option10 && typeof option10 === "object") {
	console.log("准备更新option")
	fetchData101(); // 加载数据
	myChart101.setOption(option10, true);

}
// 周期定时器,用于实时更新大屏数据
timer = setInterval(() => {
	
	fetchData101(); // 加载数据
	myChart101.setOption(option10, true);
	
	// 使用新的option设置图表
	if (option10 && typeof option10 === "object") {
		myChart101.setOption(option10, true);
		console.log(option10.series[0]);
	} else {
		console.log("赋值失败")
	}


}, 3000);