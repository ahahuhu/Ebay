var chartDom = document.getElementById('box1');
var myChart1111 = echarts.init(chartDom);
var option1111;
var infodata = ['书籍', '电子数码', '运动户外', '生活用品', '食品', '其它'];

function fetchData() {
    const apiUrl = 'http://localhost:8016/BigScreen/box4Data';
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data1 => {
            console.log('获取到的数据:', data1);
            var data = data1.data.allxianzhi.map((value, index) => {
                return {
                    value: value,
                    name: infodata[index]
                };
            });
            var total = data.reduce((acc, cur) => acc + cur.value, 0);
            console.log(data);
            console.log(total);

            option1111 = {
                tooltip: {
                    trigger: 'item',
                    formatter: '{a} <br/>{b} : {c}%'
                },
                legend: {
                    data: infodata,
                    textStyle: {
                        color: 'white'
                    }
                },
                series: [{
                    name: 'Funnel',
                    type: 'funnel',
                    left: '10%',
                    top: 60,
                    bottom: 60,
                    width: '80%',
                    minSize: '0%',
                    maxSize: '100%',
                    sort: 'ascending',
                    gap: 2,
                    label: {
                        show: true,
                        position: 'inside'
                    },
                    labelLine: {
                        length: 10,
                        lineStyle: {
                            width: 1,
                            type: 'solid'
                        }
                    },
                    itemStyle: {
                        borderColor: '#fff',
                        borderWidth: 1
                    },
                    emphasis: {
                        label: {
                            fontSize: 20
                        }
                    },
                    data: data
                }]
            };

            myChart1111.setOption(option1111);
        })
        .catch(error => {
            console.error('请求失败:', error);
        });
}

// 初始加载数据
fetchData();

// 每5秒重新获取一次数据
setInterval(fetchData, 5000);

if (option1111 && typeof option1111 === "object") {
    myChart1111.setOption(option1111, true);
}