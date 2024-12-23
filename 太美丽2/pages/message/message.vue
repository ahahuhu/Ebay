<template>
	<view>
		<view class="page">
			<view class="list-item" v-for="(item,index) in users" :key="index" @click="connect(item)">
				<view class="avatar">
					<!-- 设置头像 read是否未读 -->
					<text class="round" v-if="!item.read"></text>
					<image :src="item.avatar_name" mode="widthFix"></image>
				</view>
				<view class="content">
					<!-- 设置名字和时间 -->
					<view class="title">
						<text class="name">{{ item.name }}</text>
						<text class="time">{{ item.time }}</text>
					</view>
					<!-- 设置最后一条消息 -->
					<view class="txt">{{ item.msg }}</view>
				</view>
			</view>
		</view>
		<u-tabbar :list="tabbar" :mid-button="true"></u-tabbar> <!-- 导航栏 -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				options: [{
					text: '取消',
					style: {
						backgroundColor: '#007aff'
					}
				}, {
					text: '确认',
					style: {
						backgroundColor: '#dd524d'
					}
				}],
				myId: 1, //该用户的ID
				users: [],
				tabbar: ''
			};
		},
		onLoad() {
			this.tabbar = [{
					iconPath: "/static/images/shop(unselected).png",
					selectedIconPath: "/static/images/shop(selected).png",
					text: '首页',
					count: 0,
					isDot: false,
					customIcon: false,
					pagePath: "/pages/home/home",
				},
				{
					iconPath: "/static/images/category(unselected).png",
					selectedIconPath: "/static/images/category(selected).png",
					text: '分类',
					customIcon: false,
					pagePath: "/pages/category/category",
				},
				{
					iconPath: "/static/images/sale(unselected).png",
					selectedIconPath: "/static/images/sale(selected).png",
					text: '卖二手',
					midButton: true,
					customIcon: false,
					pagePath: "/pages/sale/sale",
				},
				{
					iconPath: "/static/images/message(unselected).png",
					selectedIconPath: "/static/images/message(selected).png",
					text: '消息',
					customIcon: false,
					pagePath: "/pages/message/message",
				},
				{
					iconPath: "/static/images/my(unselected).png",
					selectedIconPath: "/static/images/my(selected).png",
					text: '我的',
					customIcon: false,
					pagePath: "/pages/my/my",
				},
			]
		},
		onShow() {
			const value = uni.getStorageSync("userinfo")
			console.log("我的id" + value.id)
			this.myId = value.id
			console.log("我的ID:" + this.myId)
			if (value.id !== undefined && value.id !== "undefined") {
				uni.request({
					url: `http://localhost:8016/order/requireHistory/${encodeURIComponent(this.myId)}`,
					method: 'GET',
					success: (res) => {
						console.log("请求消息列表");
						if (res.data.code === 200) {
							// 假设res.data.data是一个用户对象数组  
							let newUsers = res.data.data.map(user => {
								// 转换时间戳，这里假设user.time是原始时间戳字符串  
								let [dateStr, timeStr] = user.time.split(' ');
								let [hours, minutes] = timeStr.split(':').slice(0, 2);
								user.time = `${dateStr} ${hours}:${minutes}`; // 更新user.time为新的时间格式  
								// 如果需要，可以在这里添加其他属性的处理  
								return user; // 返回修改后的用户对象  
							});
							console.log(newUsers)
								this.users = newUsers;
						}
					},
					fail: (err) => {
						console.error("请求消息列表失败", err);
					}
				});
			}else{
				this.users=[]
			}
		},
		methods: {
			onClick(e) {
				console.log('点击了' + (e.position === 'left' ? '左侧' : '右侧') + e.content.text + '按钮')
			},
			swipeChange(e, index) {
				console.log('当前状态：' + e + '，下标：' + index)
			},
			connect(item) {
				console.log("点击了" + item.id)
				uni.navigateTo({
					url: `/pages/message/chattingbox?id=${item.id}&name=${item.name}&avatar=${item.avatar_name}`
					// 传过去对方的id,name,头像
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.page {
		padding: 0 32rpx;
		color: #333;
	}

	.list-item {
		display: flex;
		padding: 30rpx 0;
		border-bottom: 1px solid #ccced3;

		.avatar {
			width: 90rpx;
			height: 90rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
			position: relative;

			.round {
				position: absolute;
				width: 14rpx;
				height: 14rpx;
				border-radius: 50%;
				background: #ef5656;
				top: -4rpx;
				right: -4rpx;
				z-index: 1;
			}

			image {
				width: 100%;
				height: 100%;
				border-radius: 10rpx;
			}
		}

		.content {
			flex: 1;

			.title {
				display: flex;
				justify-content: space-between;

				.name {
					font-weight: bold;
				}

				.time {
					color: #999;
					font-size: 24rpx;
				}
			}

			.txt {
				margin-top: 10rpx;
				overflow: hidden;
				text-overflow: ellipsis;
				display: -webkit-box;
				-webkit-line-clamp: 1;
				-webkit-box-orient: vertical;
				text-align: left;
				color: #999;
				font-size: 26rpx;
			}
		}
	}
</style>