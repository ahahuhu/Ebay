<template>
	<view>
		<view class="nav-container">
			<!-- 顶部导航 -->
			<view class="nav">
				<!-- 按时间排序 -->
				<view class="nav-item" :class="{ active: activeSort === 'time' }" @click="changeSort('time')">
					<text>按时间排序</text>
					<button class="sort-btn" @click.stop="toggleOrder('time')">
						<text v-if="sortOrders.time === 'asc'">↑</text>
						<text v-else>↓</text>
					</button>
				</view>
				<!-- 按价格排序 -->
				<view class="nav-item" :class="{ active: activeSort === 'price' }" @click="changeSort('price')">
					<text>按价格排序</text>
					<button class="sort-btn" @click.stop="toggleOrder('price')">
						<text v-if="sortOrders.price === 'asc'">↑</text>
						<text v-else>↓</text>
					</button>
				</view>
			</view>
		</view>
		<view>
			<u-search border-color="#9a9a9a" bg-color="#ffffff" placeholder="搜索需求商品" margin=8px :clearabled="true"
				:show-action="true" action-text="搜索" :action-style="SearchButtonStyle" v-model="keyword"
				@custom="onSearch()"></u-search>
			<view v-if="infoList" class="comment" v-for="(res, index) in infoList" :key="index">

				<view class="left">
					<image :src="res.pic" mode="aspectFill"></image>
				</view>
				<view class="right">
					<view class="top">
						<view class="name">{{ res.nickname }}</view>
					</view>
					<view class="top">
						<view class="title">#{{ res.name }}</view>
					</view>
					<view class="content">{{ res.description }}</view>
					<view class="money">预期：￥{{res.price}}</view>
					<view class="bottom">
						<view>{{res.address}}</view>
						<text>·</text>
						{{ res.releaseTime }}
						<view class="reply">与Ta联系</view>
					</view>
				</view>

			</view>
			<view>
				<u-divider>没有更多了</u-divider>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				infoList: [],
				ainfo: {
					seekerId: 0,
					userId: 0,
					name: "",
					description: "1233",
					price: 0,
					address: "",
					releaseTime: "",
					nickname: "",
					pic: "",
					isOpen: 0,
				},
				SearchButtonStyle: {
					backgroundColor: '#ff5511', // 定义控件的背景颜色  
					// fontSize: '13px', // 定义控件的字体大小 
					color: '#ffffff',
					borderRadius: '8px', // 添加圆角
					// 你可以继续添加其他需要的样式属性  
				},
				keyword: "",
				sortOrders: {
					time: 'desc', // 'asc' 表示升序，'desc' 表示降序
					price: 'desc' // 'asc' 表示升序，'desc' 表示降序
				},
				activeSort: 'time' // 用于记录当前选中的排序方式
			};
		},
		onShow() {
			this.keyword = uni.getStorageSync("wantkey")
			console.log(this.keyword)
			this.getkeyComment();
		},
		methods: {
			getkeyComment() {
				this.infoList = []
				console.log("获取需求列表")
				console.log(this.activeSort)
				if(this.activeSort == "time"){
					var goif = {
						"asc": this.sortOrders.time == 'asc'? 1:0,
						"words": this.keyword,
					}
					console.log(goif)
					uni.request({
						url: "http://localhost:8016/searchSeekerByNameTime",
						data: goif,
						success: (res) => {
							console.log(res)
							if (res.statusCode == 200) {
					
								this.infoList = res.data.result
					
							}
							console.log(this.infoList)
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				else{
					var goif = {
						"asc": this.sortOrders.price == 'asc'? 1:0,
						"words": this.keyword,
					}
					console.log(goif)
					uni.request({
						url: "http://localhost:8016/searchSeekerByNamePrice",
						data: goif,
						success: (res) => {
							console.log(res)
							if (res.statusCode == 200) {
					
								this.infoList = res.data.result
					
							}
							console.log(this.infoList)
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
			},
			onSearch() {
				uni.setStorageSync("wantkey", this.keyword)
				uni.redirectTo({
					url: '/pages/sale/want_info'
				})
			},
			// 改变排序方式的方法
			changeSort(type) {
				this.activeSort = type; // 设置当前选中的排序方式
				// 这里可以编写改变排序的逻辑，例如发送请求或更新本地数据
				console.log(`按${type}排序`);
				this.getkeyComment();
			},
			// 切换排序顺序
			toggleOrder(type) {
				this.sortOrders[type] = this.sortOrders[type] === 'asc' ? 'desc' : 'asc';
				this.changeSort(type);
				this.getkeyComment();
			}
		}
	};
</script>

<style lang="scss" scoped>
	.comment {
		display: flex;
		padding: 30rpx;

		.left {
			image {
				width: 64rpx;
				height: 64rpx;
				border-radius: 50%;
				background-color: #f2f2f2;
			}
		}

		.right {
			flex: 1;
			padding-left: 20rpx;
			font-size: 30rpx;

			.top {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 10rpx;

				.name {
					color: #c8c2b9;
				}

				.title {
					color: #f07b3f;
					font-size: 40rpx;
				}
			}

			.bottom {
				margin-top: 20rpx;
				display: flex;
				font-size: 24rpx;
				color: #9a9a9a;

				.reply {
					color: #FFCC22;
					margin-left: 10rpx;
				}
			}

			.content {}

			.money {
				color: red;
			}
		}
	}

	.nav-container {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		height: 50px;
		background-color: #f1f1f1;
		box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
	}

	.nav {
		display: flex;
		justify-content: space-between;
		width: 100%;
	}

	.nav-item {
		display: flex;
		align-items: center;
		cursor: pointer;
		margin-right: 20px;
		padding: 10px;
		border-radius: 5px;
		transition: background-color 0.3s;
	}

	.nav-item.active {
		background-color: #ff9800;
		/* 橙色主题 */
		color: white;
	}

	.nav-item.active .sort-btn {
		color: white;
	}

	.sort-btn {
		margin-left: 5px;
		padding: 2px 5px;
		background-color: transparent;
		border: none;
		cursor: pointer;
		color: #333;
	}
</style>